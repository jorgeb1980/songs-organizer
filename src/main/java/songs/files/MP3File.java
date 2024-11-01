package songs.files;

import songs.metadata.mp3.V1TagService;
import songs.metadata.mp3.V2TagService;
import songs.metadata.mp3.frames.models.Genre;
import songs.metadata.mp3.models.V1Tag;
import songs.metadata.mp3.models.V2Tag;

import java.io.File;
import java.io.FileNotFoundException;

import static java.util.Optional.ofNullable;


/**
 * Contains the metadata on an MP3 file.  They can be V1 or V2,
 * implemented in:
 * <code>songs.metadata.mp3.models.V1Tag</code> y
 * <code>songs.metadata.mp3.models.V2Tag</code>
 * <p>
 * In order to find the first frame, look for this structure, with each
 * letter standing for a single bit
 * <p>
 * AAAAAAAA   AAABBCCD   EEEEFFGH   IIJJKLMM
 * <p>
 * Bits "A" must be 1
 * Bits "E" must not be 1111
 * Bits "F" must not be 11
 * <p>
 * These conditions are good enough to consider we have a valid MP3 frame
 */
public class MP3File extends Song {

    private File file = null;

    /** V1 Tag, fixed-size 128 bits at the end of the file (if present), optional */
    private V1Tag v1Tag = null;
    /** V2 Tag, variable size, at the beginning of the file if present, optional */
    private V2Tag v2Tag = null;
    
    public MP3File(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
        build();
    }

    public MP3File(File file, V1Tag v1Tag, V2Tag v2Tag) {
        super(file);
        this.v1Tag = v1Tag;
        this.v2Tag = v2Tag;
    }

    private void build() {
        createV1Tag();
        createV2Tag();
    }

    private void createV2Tag() {
        if (file != null) {
        	try {
	            v2Tag = V2TagService.buildTag(file);
        	}
	        catch (Exception e) {
	        	e.printStackTrace();
	        }
        }
    }
    
    private void createV1Tag() {
        if (file != null) {
	        try {
		        v1Tag = V1TagService.buildTag(file);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
        }
    }    
    
    private V1Tag v1Tag() {
    	return ofNullable(v1Tag).orElse(V1Tag.EMPTY);
    }
    
    private V2Tag v2Tag() {
    	return ofNullable(v2Tag).orElse(V2Tag.EMPTY);
    }

    public String title() {
        return decide(v1Tag().title(), v2Tag().title());
    }

    public String artist() {
        return decide(v1Tag().artist(), v2Tag().artist());
    }
    
    public String album() {
        return decide(v1Tag().album(), v2Tag().album());
    }

    public String year() {
        return decide(v1Tag().year(), v2Tag().year());
    }

    public String comments() {
        return decide(v1Tag().comments(), v2Tag().comments());
    }

    public String originalArtist() {
        return v2Tag().originalArtist();
    }

    public String composer() {
        return v2Tag().composer();
    }

    public String author() {
        return null;
    }

    public String genre() {
        return decide(translateV1Genre(v1Tag().genre()), v2Tag().genre());
    }

    private String translateV1Genre(Genre genre) {
        return genre == null ? null : genre.toString();
    }

    public String trackNumber() {
        return v2Tag().trackNumber();
    }
    
    /**
     * This will return the value in v2 tag if it is filled; if not,
     * the value in v1 tag if it is filled; if not, null
     */
    private String decide(String valorV1, String valorV2) {
        if (isValid(valorV2)) return valorV2.trim();
        else if (isValid(valorV1)) return valorV1.trim();
        else return null;
    }
    
    private boolean isValid(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
