package songs.metadata.mp3;

public interface Constants {

    // Fixed sizes for the expected fields in a V1 tag
    int SIZE_TITLE = 30;
    int SIZE_ARTIST = 30;
    int SIZE_ALBUM = 30;
    int SIZE_YEAR = 4;
    int SIZE_COMMENTS = 30;
    int SIZE_GENRE = 1;
    // Offsets for each expected field inside the V1 tag
    int START_TITLE = 3;
    int START_ARTIST = 33;
    int START_ALBUM = 63;
    int START_YEAR = 93;
    int START_COMMENTS = 97;
    int START_GENRE = 127;
    // These constants help to properly identify a V1 tag by its first bytes
    int START_SIZE_BYTES = 3;
    byte[] ID3 = {'I','D','3'};

    // Most common identifiers for V2 TAGs
    /** Track number */
    String TRACK_NUMBER = "TRCK";
    /** Frame identifier */
    String ID_FRAME = "TCOP";
    /** Original Artist */
    String ORIGINAL_ARTIST = "TOPE";
    /** Composer */
    String COMPOSER = "TCOM";
    /** Genre */
    String GENRE = "TCON";
    /** Comments */
    String COMMENTS = "COMM";
    /** Year */
    String YEAR = "TYER";
    /** Album */
    String ALBUM = "TALB";
    /** Artist */
    String ARTIST = "TPE1";
    /** Song name */
    String TITLE = "TIT2";
    /** Size of the id field of a frame */
    int SIZE_ID_BYTES = 4;

}
