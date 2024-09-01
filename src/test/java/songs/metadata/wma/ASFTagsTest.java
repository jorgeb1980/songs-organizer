package songs.metadata.wma;

import org.junit.jupiter.api.Test;
import songs.metadata.wma.models.ASFTag;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static test.Sandbox.sandbox;

public class ASFTagsTest {

    private void testASF(File f, String artist, String title) {
        testASF(f, artist, title, null);
    }

    private void testASF(File f, String artist, String title, String year) {
        ASFTag tag = null;
        try {
            tag = WMATagService.buildTag(f);
        } catch (IOException e) {
            fail(e);
        }
        assertEquals(artist, tag.artist());
        assertEquals(title, tag.title());
        assertEquals(year, tag.year());
    }

    @Test
    public void testASF() {
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

            testASF(song1, "some artist", "some song", "2024");
            testASF(song2, "the cataphracts", "song 12345");
            testASF(song3, "cataphracts", "song 54321");
            testASF(song4, "Cataphracts", "another song");
        });
    }
}
