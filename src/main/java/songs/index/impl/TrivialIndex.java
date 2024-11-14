package songs.index.impl;

import songs.index.Index;
import songs.index.models.Collection;

import java.io.File;

public class TrivialIndex implements Index {

    @Override
    public Collection createCollection(File f) {
        System.out.printf("Calculating files under %s ...%n", f.getAbsolutePath());
        var files = DirectoryCrawler.crawl(f);
        System.out.printf("Indexing %s - detected %d files ...%n", f.getAbsolutePath(), files.size());
        //files.parallelStream().
        return null;
    }
}
