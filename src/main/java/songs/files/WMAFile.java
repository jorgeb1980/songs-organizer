package songs.files;

import songs.metadata.wma.WMATagService;
import songs.metadata.wma.models.ASFTag;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class WMAFile extends Song {
    
    private ASFTag tag = null;
    
    public WMAFile(File f) {
        super(f);
        try {
            tag = WMATagService.buildTag(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ASFTag tag() {
        return Optional.ofNullable(tag).orElse(ASFTag.EMPTY);
    }
    
    public String artist() {
        return tag().artist();
    }
    
    public String title() {
        return tag().title();
    }

    public String originalArtist() {
        return null;
    }

    public String album() {
        return tag().album();
    }

    public String year() {
        return tag().year();
    }

    public String composer() {
        return tag().composer();
    }

    public String comments() {
        return tag().description();
    }

    public String genre() {
        return tag().genre();
    }

    public String trackNumber() {
        return tag().trackNumber();
    }

    public String author() {
        return tag().author();
    }
    
}
