package songs.classifiers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import songs.files.MP3File;
import songs.files.WMAFile;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static test.Sandbox.sandbox;

public class ClassifiersTest {

    @Test
    public void testClasssifiers() {
        var sb = sandbox();
        sb.runTest((File sandbox) -> {
            testMP3(sb.copyResource("mp3/recording_tags_1.mp3"));
            testMP3(sb.copyResource("mp3/recording_tags_2.mp3"));
            testMP3(sb.copyResource("mp3/recording_tags_3.mp3"));
            testWMA(sb.copyResource("wma/recording_tags_1.wma"));
            testWMA(sb.copyResource("wma/recording_tags_2.wma"));
            testWMA(sb.copyResource("wma/recording_tags_3.wma"));
            testNull(sb.copyResource("misc/picture.jpg"));
            testNull(sb.copyResource("misc/picture.zip"));
        });
    }

    private void testNull(File f) {
        assertNull(SongsFactory.song(f));
    }

    private void testMP3(File f) {
        var song = SongsFactory.song(f);
        assertNotNull(song);
        assertInstanceOf(MP3File.class, song);
    }

    private void testWMA(File f) {
        var song = SongsFactory.song(f);
        assertNotNull(song);
        assertInstanceOf(WMAFile.class, song);
    }
}
