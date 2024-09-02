package songs.files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static test.Sandbox.sandbox;

public class MP3FilesTest {

    private void test(File f, String artist, String title) {
        test(f, artist, title, null, null);
    }

    private void test(File f, String artist, String title, String year, String comments) {
        try {
            var song = new MP3File(f);
            assertEquals(artist, song.artist());
            assertEquals(title, song.title());
            assertEquals(year, song.year());
            assertEquals(comments, song.comments());
            assertEquals("%s - %s".formatted(artist, title), song.toString());
        } catch (FileNotFoundException e) {
            fail(e);
        }
    }

    @Test
    public void testMP3Files() {
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

            test(song1, "tester", "test", "2024", null);
            test(song2, "paco", "a test", "2024", null);
            test(song3, "pepe", "song 1", null, "áéíóú");
            test(song4, "pepe", "song 2");
            test(song5, "cataphracts", "another song");
            test(song6, "the cataphracts", "main song");
            test(song7, "the cataphracts", "yet another song from the cat");
            test(song8, "cataphracts", "something completely different");
        });
    }
}
