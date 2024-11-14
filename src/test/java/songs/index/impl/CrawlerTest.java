package songs.index.impl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static test.Sandbox.sandbox;

public class CrawlerTest {

    @Test
    public void testCrawler() {
        sandbox().runTest((File sandbox) -> {
            // Populate the sandbox
            var d1 = new File(sandbox, "dir1"); d1.mkdir();
            var d2 = new File(sandbox, "dir2"); d2.mkdir();
            var d3 = new File(sandbox, "dir3"); d3.mkdir();
            new File(sandbox, "f1").createNewFile();
            new File(d1, "d1f1").createNewFile();
            new File(d3, "d3f1").createNewFile();
            new File(d3, "d3f2").createNewFile();

            var files = DirectoryCrawler.crawl(sandbox);
            assertEquals(4, files.size());
            assertTrue(files.contains(new File(sandbox, "f1")));
            assertTrue(files.contains(new File(sandbox, "dir1/d1f1")));
            assertTrue(files.contains(new File(sandbox, "dir3/d3f1")));
            assertTrue(files.contains(new File(sandbox, "dir3/d3f2")));
            assertFalse(files.contains(new File(sandbox, "something/else")));
        });
    }
}
