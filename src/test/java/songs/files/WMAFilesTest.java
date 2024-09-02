package songs.files;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static test.Sandbox.sandbox;

public class WMAFilesTest {

    private void testWMA(File f, String artist, String title) {
        testWMA(f, artist, title, null);
    }

    private void testWMA(File f, String artist, String title, String year) {
        try {
            var song = new WMAFile(f);
            assertEquals(artist, song.artist());
            assertEquals(title, song.title());
            assertEquals(year, song.year());
            assertEquals("%s - %s".formatted(artist, title), song.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWMAFiles() {
        var sb = sandbox();
        sb.runTest((File sandbox) -> {
            // v1 - some artist - some song - 2024
            var song1 = sb.copyResource("wma/recording_tags_1.wma");
            // v2 - the cataphracts - song 12345
            var song2 = sb.copyResource("wma/recording_tags_2.wma");
            // v2 - cataphracts - song 54321
            var song3 = sb.copyResource("wma/recording_tags_3.wma");
            // v1 - Cataphracts - another song
            var song4 = sb.copyResource("wma/recording_tags_4.wma");

            testWMA(song1, "some artist", "some song", "2024");
            testWMA(song2, "the cataphracts", "song 12345");
            testWMA(song3, "cataphracts", "song 54321");
            testWMA(song4, "Cataphracts", "another song");
        });
    }
}
