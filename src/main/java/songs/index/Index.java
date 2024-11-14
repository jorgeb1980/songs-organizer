package songs.index;

import songs.index.models.Collection;

import java.io.File;

public interface Index {

    Collection createCollection(File f);
}