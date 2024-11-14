package songs.index.models;

import songs.files.Song;
import songs.index.Index;
import songs.index.impl.DirectoryCrawler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Collection {

    private Collection() {}

    private File root;

    private List<Artist> artists = new LinkedList<>();
    // Should be songs without metadata for an artist
    private List<Song> anonymousSongs = new LinkedList<>();
    // These should be albums composed entirely by songs without metadata for an
    //  artist but yes for an album
    private List<Album> anonymousAlbums = new LinkedList<>();

    public static Collection createCollection(File f, Index index) {
        System.out.printf("Calculating files under %s ...%n", f.getAbsolutePath());
        var files = DirectoryCrawler.crawl(f);
        System.out.printf("Indexing %s - detected %d files ...%n", f.getAbsolutePath(), files.size());
        //files.parallelStream().
        return null;
    }
}
