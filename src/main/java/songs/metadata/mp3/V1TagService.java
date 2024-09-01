package songs.metadata.mp3;

import static songs.files.Utils.slice;
import static songs.metadata.mp3.Constants.START_ARTIST;
import static songs.metadata.mp3.Constants.START_YEAR;
import static songs.metadata.mp3.Constants.START_COMMENTS;
import static songs.metadata.mp3.Constants.START_GENRE;
import static songs.metadata.mp3.Constants.START_TITLE;
import static songs.metadata.mp3.Constants.START_ALBUM;
import static songs.metadata.mp3.Constants.SIZE_ARTIST;
import static songs.metadata.mp3.Constants.SIZE_YEAR;
import static songs.metadata.mp3.Constants.SIZE_COMMENTS;
import static songs.metadata.mp3.Constants.SIZE_GENRE;
import static songs.metadata.mp3.Constants.SIZE_TITLE;
import static songs.metadata.mp3.Constants.SIZE_ALBUM;
import static songs.metadata.mp3.TagUtils.decodeAndTrim;
import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.io.RandomAccessFile;

import songs.metadata.mp3.models.V1Tag;
import songs.metadata.mp3.frames.models.Genre;

public class V1TagService {
	
	private V1TagService() {}
    
    /** V1 Tag length (fixed) */
    private static final int LENGTH = 128;
	
	/**
     * Builds a V1 tag on a raf file
     * @param raf Input file (will not try to close it)
     */
    public static V1Tag buildTag(RandomAccessFile raf) throws IOException {
        V1Tag ret = null;
        if (raf.length() >= LENGTH) {
	        raf.seek(raf.length() - LENGTH);
	        if (raf.length() >= LENGTH) {
	        	var buffer = new byte[LENGTH];
	            // Bring the cursor to the end of the tag
	            raf.seek(raf.length() - LENGTH);
	            var read = raf.read(buffer, 0, LENGTH);
	            if (read == LENGTH) {
	                ret = read(buffer);
	            }
	        }
        }
        return ret;
    }

    private static V1Tag read(byte[] tag) {
    	V1Tag ret = null;
        if (tag[0] == 'T' && tag[1] == 'A' && tag[2] == 'G') {
            // V1 Tag
            var title = decodeAndTrim(slice(tag, START_TITLE, SIZE_TITLE), ISO_8859_1);
            var artist = decodeAndTrim(slice(tag, START_ARTIST, SIZE_ARTIST), ISO_8859_1);
            var album = decodeAndTrim(slice(tag, START_ALBUM, SIZE_ALBUM), ISO_8859_1);
            var year = decodeAndTrim(slice(tag, START_YEAR, SIZE_YEAR), ISO_8859_1);
            var comments = decodeAndTrim(slice(tag, START_COMMENTS, SIZE_COMMENTS), ISO_8859_1);
            var genre = slice(tag, START_GENRE, SIZE_GENRE);
            Genre processedGenre = Genre.from(genre[0]);
            ret = new V1Tag(
            	title,
            	artist,
            	album,
            	year,
            	comments,
                processedGenre
            );
        }
        return ret;
    }
	
}
