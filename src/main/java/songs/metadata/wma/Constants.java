package songs.metadata.wma;

public interface Constants {

    /** Header ID: 3026B275-8E66-CF11-A6D9-00AA0062CE6C */
    int[] ID_HEADER =
        new int[] { 0x30, 0x26, 0xB2, 0x75, 0x8E, 0x66, 0xCF, 0x11, 0xA6, 0xD9,
            0x00, 0xAA, 0x00, 0x62, 0xCE, 0x6C };
    
    /** Content descriptor ID: 3326B275-8E66-CF11-A6D9-00AA0062CE6C  */
    int[] ID_CONTENT_DESCRIPTOR =
        new int[] { 0x33, 0x26, 0xB2, 0x75, 0x8E, 0x66, 0xCF, 0x11, 0xA6, 0xD9,
            0x00, 0xAA, 0x00, 0x62, 0xCE, 0x6C };
    
    /** Extended content descriptor ID: 40A4D0D2-70E3-D211-97F0-00A0C95EA850 */
    int[] ID_EXTENDED_CONTENT_DESCRIPTOR =
        new int[] { 0x40, 0xA4, 0xD0, 0xD2, 0x07, 0xE3, 0xD2, 0x11, 0x97, 0xF0,
            0x00, 0xA0, 0xC9, 0x5E, 0xA8, 0x50 };
    
    /** Data object ID: 3675B226-8E66-CF11-A6D9-00AA0062CE6C */
    int[] ID_DATA_OBJECT =
        new int[] { 0x36, 0x5B, 0xB2, 0x26, 0x8E, 0x66, 0xCF, 0x11, 0xA6, 0xD9,
            0x00, 0xAA, 0x00, 0x62, 0xCE, 0x6C };

    int LENGTH_IDENTIFIER_BYTES = 16;
    int LENGTH_SIZE_FIELD_BYTES = 8;

    String FORMAT_PROBLEM =  "Problem trying to read a WMA file";

    int LENGTH_INFO_SIZE_FIELD_BYTES = 2;

 	String WM_TRACKNUMBER = "WM/TrackNumber";
 	String WM_YEAR = "WM/Year";
 	String WM_COMPOSER = "WM/Composer";
 	String WM_GENRE = "WM/Genre";
 	String WM_ALBUM = "WM/AlbumTitle";
 	String WM_ARTIST = "WM/AlbumArtist";
 	
 	// Data types in the optional descriptors
	int TYPE_UNICODE_STRING = 0;
 	int TYPE_BYTES = 1;
 	int TYPE_BOOL = 2;
 	int TYPE_DWORD = 3;
 	int TYPE_QWORD = 4;
 	int TYPE_WORD = 5;
}
