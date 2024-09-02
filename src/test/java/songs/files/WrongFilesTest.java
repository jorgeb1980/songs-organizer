package songs.files;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static test.Sandbox.sandbox;

public class WrongFilesTest {

    private enum FileType { MP3, WMA }

    private void testEmptyMP3(File f) {
        testEmpty(f, FileType.MP3);
    }

    private void testEmptyWMA(File f) {
        testEmpty(f, FileType.WMA);
    }

    private void testEmpty(File f, FileType type) {
        try {
            Song song = type == FileType.MP3 ? new MP3File(f) : new WMAFile(f);
            assertNull(song.title());
            assertNull(song.artist());
            assertNull(song.album());
            assertNull(song.year());
            assertNull(song.composer());
            assertNull(song.comments());
            assertNull(song.originalArtist());
            assertNull(song.trackNumber());
            assertNull(song.genre());
            assertNull(song.author());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void wrongMP3Files() {
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

            testEmptyMP3(song1);
            testEmptyMP3(song2);
            testEmptyMP3(song3);
            testEmptyMP3(song4);
        });
    }

    @Test
    public void wrongWMAFiles() {
        var sb = sandbox();
        sb.runTest((File sandbox) -> {
            // v1 - tester - test - 2024
            var song1 = sb.copyResource("mp3/recording_tags_1.mp3");
            // v2 - paco - a test - 2024
            var song2 = sb.copyResource("mp3/recording_tags_2.mp3");
            // v2 - pepe - song 1 ; comment: áéíóú
            var song3 = sb.copyResource("mp3/recording_tags_3.mp3");
            // v1 - pepe - song 2
            var song4 = sb.copyResource("mp3/recording_tags_4.mp3");
            // v2 - cataphracts - another song
            var song5 = sb.copyResource("mp3/recording_tags_5.mp3");
            // v2 - the cataphracts - main song
            var song6 = sb.copyResource("mp3/recording_tags_6.mp3");
            // v1 - the cataphracts - yet another song from the cat
            var song7 = sb.copyResource("mp3/recording_tags_7.mp3");
            // v2 - cataphracts - something completely different
            var song8 = sb.copyResource("mp3/recording_tags_8.mp3");

            testEmptyWMA(song1);
            testEmptyWMA(song2);
            testEmptyWMA(song3);
            testEmptyWMA(song4);
            testEmptyWMA(song5);
            testEmptyWMA(song6);
            testEmptyWMA(song7);
            testEmptyWMA(song8);
        });
    }
}
