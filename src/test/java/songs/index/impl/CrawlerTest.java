package songs.index.impl;

import test.Sandbox;
import test.sandbox.SandboxTest;

import java.io.File;

import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrawlerTest {

    @SandboxTest
    public void testCrawler(Sandbox sb) {
        var rootDirectory = sb.getSandbox();
        // Populate the sandbox
        sb.createResource("f1", "aaa", defaultCharset());
        sb.createResource("dir1/d1f1", "aaa", defaultCharset());
        sb.createResource("dir3/d3f1", "aaa", defaultCharset());
        sb.createResource("dir3/d3f2", "aaa", defaultCharset());

        var files = DirectoryCrawler.crawl(rootDirectory);
        assertEquals(4, files.size());
        assertTrue(files.contains(new File(rootDirectory, "f1")));
        assertTrue(files.contains(new File(rootDirectory, "dir1/d1f1")));
        assertTrue(files.contains(new File(rootDirectory, "dir3/d3f1")));
        assertTrue(files.contains(new File(rootDirectory, "dir3/d3f2")));
        assertFalse(files.contains(new File(rootDirectory, "something/else")));
    }
}
