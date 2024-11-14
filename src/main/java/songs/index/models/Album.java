package songs.index.models;

import lombok.Getter;
import songs.files.Song;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Album {

    private String name;
    private List<String> aka = new LinkedList<>();
    private List<Song> songs = new LinkedList<>();
}
