package songs.classifiers;

import org.reflections.Reflections;
import songs.files.Song;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SongsFactory {

    private static List<SongsClassifier> classifiers = getSongClassifiers();

    private static List<SongsClassifier> getSongClassifiers() {
        Reflections reflections = new Reflections(SongsFactory.class.getPackageName());
        Set<Class<? extends SongsClassifier>> classes = reflections.getSubTypesOf(SongsClassifier.class);
        return classes.stream().map(c -> buildClassifier(c)).toList();
    }

    private static SongsClassifier buildClassifier(Class<? extends SongsClassifier> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Song song(File f) {
        List<Song> songs = classifiers.stream().map(c -> c.classify(f)).filter(Objects::nonNull).toList();
        return switch(songs.size()) {
            case 0 -> null;
            case 1 -> songs.getFirst();
            default -> {
                List<String> recognizedFormats = songs.stream().map(c -> c.getClass().getName()).toList();
                System.err.format(
                    "Recognized the following formats [%s] for the file %s%n",
                    String.join(",", recognizedFormats),
                    f.getAbsolutePath()
                );
                yield null;
            }
        };
    }
}
