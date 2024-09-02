package songs.files;

import lombok.Getter;

import java.io.File;

@Getter
public abstract class Song {

    protected String absolutePath = null;
    protected String fileName = null;
    
    public Song(File fichero) {
        super();
        if (fichero != null) {
		    // Ruta y nombre de fichero
		    absolutePath = fichero.getAbsolutePath();
		    fileName = fichero.getName();
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!artist().isEmpty() && !title().isEmpty()) {
            // artist - title
            sb.append(artist());
            sb.append(" - ");
            sb.append(title());
        }
        else if (artist().isEmpty() && !title().isEmpty()) {
            // title
            sb.append(title());
        }
        else {
            // just the file name
            sb.append(absolutePath);
        }
        // Devolver el buffer
        return sb.toString();
    }

    public abstract String title();
    public abstract String artist();
    public abstract String album();
    public abstract String year();
    public abstract String composer();
    public abstract String comments();
    public abstract String originalArtist();
    public abstract String trackNumber();
    public abstract String genre();
    public abstract String author();
}
