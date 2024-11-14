package songs.index.models;

import songs.files.Song;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Collection {

    private File root;

    private List<Artist> artists = new LinkedList<>();
    private List<Song> songs = new LinkedList<>();
}
