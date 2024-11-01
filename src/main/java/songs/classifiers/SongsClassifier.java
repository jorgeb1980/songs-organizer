package songs.classifiers;

import songs.files.Song;

import java.io.File;

public interface SongsClassifier {

    Song classify(File f);
}
