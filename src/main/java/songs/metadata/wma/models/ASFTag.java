package songs.metadata.wma.models;

import lombok.Builder;

/**
 * Very nice docs here:
 * <a href="https://docs.fileformat.com/video/asf/">...</a>
 * <a href="https://learn.microsoft.com/en-us/windows/win32/medfound/asf-file-structure">...</a>
 * <p>
 * Some highlights:
 * <p>
 * Represents the data structure found in the ASF headers.  Type definitions:
 * Type	Size (bits)	Signed
 *		BYTE	8	no
 *		WCHAR 	16	no
 *		WORD 	16	no
 *		DWORD  	32	no
 *		QWORD  	64	no
 *		GUID	128	no
 * <p>
 * Strings should end in one (or many) null value(s), but it is not mandatory.
 * <p>
 * The file consists of ASF objects with the following structure:
 * <p>
 * -----------------------------------------------------
 * Bytes		- 		    Content
 * -----------------------------------------------------
 * 0-15			-			Identifier
 * 16-23		-			Size in bytes - Little endian!!
 * 24-*			-			Content
 * <p>
 * The size is between 24 and 2^64 - 24 - 1.  Bytes are stored in little-endian.
 * Whenever it is necessary to skip objects, it can be done by reading the first
 * 24 bytes.  The first 16 bytes are enough to discard an object and the next 8
 * help to calculate the start of the next object.
 * <p>
 * The file has a single header, a data object and from 0 to N index objects.  We
 * are mainly interested in the header.
 * <p>
 * The header consists of property objects, stream property objects, header extensions objects,
 * content description objects (the ones we need!), objects with scripts meant to be
 * ran by the player (?) and marker objects meant to help searching inside the file.
 * They will not follow any particular order.
 * <p>
 * A valid header will have exactly 1 properties object, 1 header extension object and 1+
 * stream properties objects.
 * <p>
 * ---> The ASF header has the following structure:
 * <p>
 * -----------------------------------------------------
 * Bytes		- 			Content
 * -----------------------------------------------------
 * 0-15			-			Identifier (we don't care right now)
 * 16-23		-			Size in bytes - little endian!!
 * 24-27		-			Number of objects in the header - little endian!!
 *                          If this is zero, we ignore this header
 * 28			-			Reserved 0x01
 * 29			-			Reserved 0x02
 * <p>
 * Still inside the header, we have the objects it contains.  We are interested in
 * content description objects (0 or 1) and the extended object for content description
 * (also optional, 0 or 1).
 * <p>
 * ---> Content description object:
 * It consists of variable-length fields, preceded by their length.
 * <p>
 * -----------------------------------------------------
 * Bytes		- 			Content
 * -----------------------------------------------------
 * 0-15			-			Identifier.  Apparently, always
 * 							75B22633-668E-11CF-A6D9-00AA0062CE6C.
 * 							However encoded as:
 * 							3326B275-8E66-CF11-A6D9-00AA0062CE6C
 * 16-23		-			Size in bytes - little endian!!
 * 24-25		-			Title length - little endian!!
 * 26-27		-			Author length - little endian!!
 * 28-29		-			Copyright length - little endian!!
 * 30-31		-			Description length - little endian!!
 * 32-33		-			Rating length - little endian!!
 * VARIABLE		-			Title (WCHAR)
 * VARIABLE		-			Author (WCHAR)
 * VARIABLE		-			Copyright (WCHAR)
 * VARIABLE		-			Description (WCHAR)
 * VARIABLE		-			Rating (WCHAR)
 * <p>
 * ----> Extended content description object:
 * Consists of key-value pairs expressed as WCHAR
 * <p>
 * -----------------------------------------------------
 * Bytes		- 			Content
 * -----------------------------------------------------
 * 0-15			-			Identifier.  Apparently, always
 * 							D2D0A440-E307-11D2-97F0-00A0C95EA850.
 * 							However encoded as:
 * 							40A4D0D2-07E3-D211-97F0-00A0C95EA850
 * 16-23		-			Size in bytes - little endian!!
 * 24-25		-			Number of descriptors in the object - Little endian!!
 * 26-...		-			Descriptors.
 * <p>
 * Each descriptor consists of this structure:
 * <p>
 * --> Content descriptor
 * Consists the data and some metadata
 * <p>
 * -----------------------------------------------------
 * Bytes		- 			Content
 * -----------------------------------------------------
 * 0-1			-			Name size - Little endian!!
 * 2-n			-			Name (WCHAR)
 * (n+1)-(n+2)	-			Data type (see later)
 * (n+3)-(n+4)	-			Value length in bytes - little endian!!
 * (n+5)-...	-			Value  (WCHAR)
 * <p>
 * The possible data types are:
 * 0x0000	-> Unicode string.  See length field for size.
 * 0x0001	-> Byte array.  See length field for size.
 * 0x0002	-> BOOL.  Length is 4 bytes. False if zero, true otherwise
 * 0x0003	-> DWORD.  Length is 4 bytes.
 * 0x0004	-> QWORD.  Length is 8 bytes.
 * 0x0005	-> WORD.  Length is 2 bytes.
 * <p>
 * I have found the following possible pairs key/value (always in WCHAR):
 * WM/Lyrics
 * WM/TrackNumber
 * WM/Year
 * WM/Composer
 * WM/Publisher
 * WM/Genre
 * WM/AlbumTitle
 * WM/AlbumArtist
 * <p>
 * Very fortunately this format disallows padding between frames.  However,
 * the extended content descriptor object allows cheating by leaving zeroes
 * between the last descriptor and the next object.  This should not be an
 * issue if we respect the descriptor counter.
 */
@Builder
public record ASFTag(
	String title,
    String author,
    String description,
    String artist,
    String trackNumber,
    String year,
    String genre,
    String composer,
    String album
) {

    public static final ASFTag EMPTY = new ASFTag(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    // Some programs are a bit confused about where to store the artist
    @Override
    public String artist() {
        if (artist != null && !artist.isEmpty()) return artist;
        else if (author != null && !author.isEmpty()) return author;
        else return null;
    }
}
