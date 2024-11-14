package songs.index.models;

import lombok.Getter;
import songs.files.Song;

import java.util.LinkedList;
import java.util.List;

public class Album {

    @Getter
    private String name;
    private List<String> aliases = new LinkedList<>();
    private List<Song> songs = new LinkedList<>();

    public Album(String name) { this.name = name; }
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    // Return defensive copies
    public List<String> getAliases() {
        return aliases.stream().toList();
    }

    public List<Song> getSongs() {
        return songs.stream().toList();
    }
}
