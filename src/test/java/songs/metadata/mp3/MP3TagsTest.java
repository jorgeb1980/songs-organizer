package songs.metadata.mp3;

import org.junit.jupiter.api.Test;
import songs.metadata.mp3.models.V1Tag;
import songs.metadata.mp3.models.V2Tag;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static test.Sandbox.sandbox;

public class MP3TagsTest {

    private void testV1(File f, String artist, String title) {
        testV1(f, artist, title, "");
    }

    private void testV1(File f, String artist, String title, String year) {
        V1Tag tag = null;
        try {
            tag = V1TagService.buildTag(f);
        } catch (IOException e) {
            fail(e);
        }
        assertEquals(title, tag.title());
        assertEquals(artist, tag.artist());
        assertEquals(year, tag.year());
    }

    private void testV2(File f, String artist, String title) {
        testV2(f, artist, title, null, null);
    }

    private void testV2(File f, String artist, String title, String year, String comments) {
        V2Tag tag = null;
        try {
            tag = V2TagService.buildTag(f);
        } catch (IOException e) {
            fail(e);
        }
        assertEquals(title, tag.title());
        assertEquals(artist, tag.artist());
        assertEquals(year, tag.year());
        assertEquals(comments, tag.comments());
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

            testV1(song1, "tester", "test", "2024");
            testV2(song2, "paco", "a test", "2024", null);
            testV2(song3, "pepe", "song 1", null, "áéíóú");
            testV1(song4, "pepe", "song 2");
            testV2(song5, "cataphracts", "another song");
            testV2(song6, "the cataphracts", "main song");
            testV1(song7, "the cataphracts", "yet another song from the cat");
            testV2(song8, "cataphracts", "something completely different");
        });
    }
    
    @Test
    public void testWrongFormats() {
        var sb = sandbox();
        sb.runTest((File sandbox) -> {
            // v1 - tester - test - 2024
            var song1 = sb.copyResource("mp3/recording_tags_1.mp3");
            // v2 - paco - a test - 2024
            var song2 = sb.copyResource("mp3/recording_tags_2.mp3");
            // v2 - pepe - song 1
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

            assertNotV2(song1);
            assertNotV1(song2);
            assertNotV1(song3);
            assertNotV2(song4);
            assertNotV1(song5);
            assertNotV1(song6);
            assertNotV2(song7);
            assertNotV1(song8);
        });
    }

    private void assertNotV2(File f) {
        try {
            assertNull(V2TagService.buildTag(f));
        } catch (Exception e) {
            fail(e);
        }
    }

    private void assertNotV1(File f) {
        try {
            assertNull(V1TagService.buildTag(f));
        } catch (Exception e) {
            fail(e);
        }
    }
}
