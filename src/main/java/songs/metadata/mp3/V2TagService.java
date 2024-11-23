package songs.metadata.mp3;

import songs.metadata.mp3.exceptions.SizeTranslationException;
import songs.metadata.mp3.models.V2Tag;

import static songs.files.Utils.slice;
import static songs.metadata.mp3.Constants.ARTIST;
import static songs.metadata.mp3.Constants.ORIGINAL_ARTIST;
import static songs.metadata.mp3.Constants.COMMENTS;
import static songs.metadata.mp3.Constants.COMPOSER;
import static songs.metadata.mp3.Constants.GENRE;
import static songs.metadata.mp3.Constants.ID3;
import static songs.metadata.mp3.Constants.TRACK_NUMBER;
import static songs.metadata.mp3.Constants.SIZE_ID_BYTES;
import static songs.metadata.mp3.Constants.START_SIZE_BYTES;
import static songs.metadata.mp3.Constants.TITLE;
import static songs.metadata.mp3.Constants.ALBUM;
import static songs.metadata.mp3.TagUtils.readField;
import static songs.metadata.mp3.TagUtils.retrieveExtendedText;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class V2TagService {

	/**
	 * Wherever this mask returns other than zero, it will mark the beginning of the
	 * MP3 file padding after the header
	 */
	private static final int MASK = Integer.parseInt("10000000", 2);
	private static final int SIZE_SIZE_BYTES = 4;
	private static final int SIZE_VERSION_BYTES = 2;
	private static final int INDEX_VERSION = 0;
	private static final int INDEX_HEADER_FLAGS = 2;
	private static final int INDEX_HEADER_SIZE = 3;
	private static final int SIZE_HEADER_FLAGS_BYTES = 1;
	private static final int HEADER_TOTAL_SIZE_BYTES =
		SIZE_SIZE_BYTES + SIZE_VERSION_BYTES + SIZE_HEADER_FLAGS_BYTES;
	private static final int INDEX_ID = 0;

	public static final int HEADER_FRAME_SIZE_BYTES = 10;
	private static final int INDEX_FRAME_SIZE = 4;
	private static final int FRAME_FLAGS_SIZE_BYTES = 2;
	private static final int IND_FLAGS_FRAME = 8;

	// Used for resolution of the year between the possible options
	private static final List<String> PRIORITY_YEAR_FIELDS = Arrays.asList(
		"TYER", "TRDA", "TIME", "TDAT", "TDRC" 
	);

	private V2TagService() {}

	public static V2Tag buildTag(File file) throws IOException {
		V2Tag ret = null;
		try(var raf = new RandomAccessFile(file, "r")) {
			// Get the file start (only to tell if this is the right format)
			var buffer = new byte[START_SIZE_BYTES];
			raf.read(buffer, 0, START_SIZE_BYTES);
			if (buffer[0] == ID3[0] && buffer[1] == ID3[1] && buffer[2] == ID3[2]) {
				var ctx = new TranslationContext();
				var headerBuffer = new byte[HEADER_TOTAL_SIZE_BYTES];
				raf.read(headerBuffer, 0, HEADER_TOTAL_SIZE_BYTES);
				var header = buildHeader(headerBuffer, ctx);
				if (header != null) {
					long remaining = header.translatedSize();
					boolean keepOn = readFrame(raf, ctx);
					while (keepOn) {
						try {
							// Keep on until there are no more frames or we are past the total
							//	declared size
							keepOn = readFrame(raf, ctx) && (ctx.accumulatedBytes() < remaining);
						} catch (Error e) {
							e.printStackTrace();
							System.err.println(e.getClass().getName());
						}
					}
					ctx.solveYearCandidates();
					ret = ctx.tagBuilder().build();
				}
			}
		}
		return ret;
	}

	/**
	 * Try to read a <code>Frame</code> and add it to the <code>Frames</code> list.
	 * It is possible to return false since the V2 Tag writer program may have left
	 * an empty slot at the end for convenience.  This is admisible by the standard.
	 * @param raf Random access file (not trying to close it)
	 * @param ctx Translation context - will accumulate here information in order to
	 *            better solve later things like the year
	 * @return True if it managed to read a <code>Frame</code>, false in any other case
	 * 
	 */
	private static boolean readFrame(RandomAccessFile raf, TranslationContext ctx) throws IOException {
		var ret = false;
		try {
			var buffer = new byte[HEADER_FRAME_SIZE_BYTES];
			var read = raf.read(buffer, 0, HEADER_FRAME_SIZE_BYTES);
			if (read == HEADER_FRAME_SIZE_BYTES) {
				if (isValidTag(buffer)) {
					ret = true;
					// ID
					var id = slice(buffer, INDEX_ID, SIZE_ID_BYTES);
					var size = slice(buffer, INDEX_FRAME_SIZE, SIZE_SIZE_BYTES);
					var translatedSize = translateSize(size, ctx);
					// Flags
					var flags = slice(buffer, IND_FLAGS_FRAME, FRAME_FLAGS_SIZE_BYTES);
					ctx.incrementAccumulatedBy(translatedSize);
                    byte[] body = null;
					if (translatedSize > 0) {
						var tmp = new byte[(int) (translatedSize)];
						int readBytes = raf.read(tmp, 0, (int) (translatedSize));
						body = slice(tmp, 0, readBytes);
					}
					var f = new V2Tag.Frame(new String(id), size, translatedSize, flags, body);
					storeFrame(f.id(), f, ctx);
					// Update total size in the translation context
					ctx.increaseTotalSizeBy(HEADER_FRAME_SIZE_BYTES + translatedSize);
				}
			}
		} catch (SizeTranslationException eep) {
			// We have reached the padding
			ret = false;
		}
		return ret;
	}

	/**
	 * Calculate total frame size in bytes, counting too the id3 tag size.  If the
	 * total translated size is greater than the expected, we have reached the padding.
	 * 
	 * @param integerNumber 4-byte array
	 * @param ctx    Contains - Total size of the id3 accumulated in the header
	 *              - Size accumulated by the prior frames
	 * @return Total size for the frame
	 */
	private static long translateSize(byte[] integerNumber, TranslationContext ctx)
			throws SizeTranslationException {
		var ret = translateSize(integerNumber);
		if ((ret + ctx.accumulatedBytes()) > ctx.totalSizeBytes()) {
			// Reached the padding
			throw new SizeTranslationException();
		}
		return ret;
	}

	/**
	 * Translates a 4-byte array into a long integer
	 * 
	 * @param integerNumber 4-byte array
	 * @return The size of some field as a long integer
	 */
	private static long translateSize(byte[] integerNumber) throws SizeTranslationException {
        for (var b : integerNumber) {
            if ((b & MASK) != 0) {
                throw new SizeTranslationException();
            }
        }
		var ret = 0L;
		// Calculate the sizes shifting left as many positions as necessary
		ret += integerNumber[3];
		var size2 = integerNumber[2] << 7;
		ret += size2;
		var size1 = integerNumber[1] << 14;
		ret += size1;
		var size0 = integerNumber[0] << 21;
		ret += size0;
		return ret;
	}

	// Current frame may be any of the possible data types (title, artist, etc.)
	private static void storeFrame(
		String id,
		V2Tag.Frame f,
		TranslationContext ctx
	) {
		var c = ctx.tagBuilder();
		switch(id) {
			case TITLE -> c.title(readField(f.body()));
			case ARTIST -> c.artist(readField(f.body()));
			case ORIGINAL_ARTIST -> c.originalArtist(readField(f.body()));
			case "TDRC", "TDAT", "TIME", "TRDA", "TYER" -> 
				ctx.addYearCandidate(id, readField(f.body()));
			case COMMENTS -> c.comments(retrieveExtendedText(f.body()));
			case COMPOSER -> c.composer(readField(f.body()));
			case GENRE -> c.genre(readField(f.body()));
			case TRACK_NUMBER -> c.trackNumber(readField(f.body()));
			case ALBUM -> c.album(readField(f.body()));
		}
	}

	/**
	 * Is the current identifier a valid one or have we read padding trash
	 * from the end of the tag?
	 *
	 * @param buffer Maybe a header
	 * @return True if the buffer checks to be a valid header
	 */
	private static boolean isValidTag(byte[] buffer) {
		boolean ret = true;
		int i = 0;
		while (ret && i < SIZE_ID_BYTES) {
			ret = ret && (('a' <= buffer[i] && buffer[i] <= 'z') || ('A' <= buffer[i] && buffer[i] <= 'Z')
				|| ('0' <= buffer[i] && buffer[i] <= '9'));
			i++;
		}
		return ret;
	}

	/**
	 * Builds all the header fields
	 * 
	 * @param buffer Byte array where we expect the header to be
	 */
	private static V2Tag.Header buildHeader(byte[] buffer, TranslationContext ctx) {
		V2Tag.Header ret;
		try {
			var version = slice(buffer, INDEX_VERSION, SIZE_VERSION_BYTES);
			var flags = slice(buffer, INDEX_HEADER_FLAGS, SIZE_HEADER_FLAGS_BYTES);
			var size = slice(buffer, INDEX_HEADER_SIZE, SIZE_SIZE_BYTES);
			var translatedSize = translateSize(size);
			ret = new V2Tag.Header(version, size, flags, translatedSize);
			ctx.increaseTotalSizeBy(translatedSize);
		} catch (SizeTranslationException eep) {
			// We have reached the padding
			ret = null;
		}
		return ret;
	}

	private static class TranslationContext {

		// TDRC (recording time) consolidates TDAT (date), TIME (time), TRDA (recording dates), and TYER (year)
		private final Map<String, String> yearCandidates = new HashMap<>();
		
		private long accumulatedBytes = 0;

		// Actual size - whatever wrote the file may have included
		//	padding for convenience and we need to take that into account
		private long totalSizeBytes = 0;

		private final V2Tag.V2TagBuilder tagBuilder = V2Tag.builder();

		public TranslationContext() {}

		public long accumulatedBytes() {
			return accumulatedBytes;
		}
		
		public void addYearCandidate(String field, String value) {
			yearCandidates.put(field, value);
		}
		
		public void solveYearCandidates() {
			for (String field: PRIORITY_YEAR_FIELDS) {
				if (yearCandidates.containsKey(field)) tagBuilder.year(yearCandidates.get(field));
			}
		}

		public long totalSizeBytes() {
			return totalSizeBytes;
		}

		public V2Tag.V2TagBuilder tagBuilder() {
			return tagBuilder;
		}

		public void incrementAccumulatedBy(long bytes) {
			accumulatedBytes += bytes;
		}

		public void increaseTotalSizeBy(long bytes) {
			totalSizeBytes += bytes;
		}
	}
}
