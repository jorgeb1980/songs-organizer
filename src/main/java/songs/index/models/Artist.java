package songs.index.models;

import lombok.Getter;
import songs.files.Song;

import java.util.LinkedList;
import java.util.List;

public class Artist {

    @Getter
    private String name;
    private List<String> aliases = new LinkedList<>();
    private List<Album> albums = new LinkedList<>();
    private List<Song> songs = new LinkedList<>();

    public Artist(String name) { this.name = name; }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    // Return defensive copies
    public List<String> getAliases() {
        return aliases.stream().toList();
    }

    public List<Album> getAlbums() {
        return albums.stream().toList();
    }

    public List<Song> getSongs() {
        return songs.stream().toList();
    }

    public Album getOrCreateAlbum(String name) {
        var results = albums.stream().filter(album -> album.getName().equals(name)).toList();
        if (results.isEmpty()) {
            var album = new Album(name);
            albums.add(album);
            return album;
        } else return results.getFirst();
    }
}
