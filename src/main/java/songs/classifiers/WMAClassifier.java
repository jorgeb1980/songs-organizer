package songs.classifiers;

import songs.files.Song;
import songs.files.WMAFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import static songs.files.Utils.compare;
import static songs.metadata.wma.Constants.ID_HEADER;
import static songs.metadata.wma.Constants.LENGTH_IDENTIFIER_BYTES;

public class WMAClassifier implements SongsClassifier {

    @Override
    public Song classify(File f) {
        Song ret = null;
        // First 16 bytes
        try (var stream = new BufferedInputStream(new FileInputStream(f))) {
            var buffer = new byte[LENGTH_IDENTIFIER_BYTES];
            stream.read(buffer);

            if (compare(buffer, ID_HEADER)) {
                // yay!
                ret = new WMAFile(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }
        return ret;
    }
}
