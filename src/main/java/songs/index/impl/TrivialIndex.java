package songs.index.impl;

import songs.files.Song;
import songs.index.Index;
import songs.index.models.Album;
import songs.index.models.Artist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TrivialIndex implements Index {

    private List<Song> songs = new LinkedList<>();
    private Map<String, Artist> artists = new HashMap<>();
    private Map<String, Album> albums = new HashMap<>();

    @Override
    public List<Album> anonymousAlbums() {
        return albums.values().stream().toList();
    }

    @Override
    public List<Artist> artists() {
        return artists.values().stream().toList();
    }

    @Override
    public List<Song> anonymousSongs() {
        // Defensive copy
        return songs.stream().toList();
    }

    @Override
    public void indexSong(Song s) {
        if (s != null) {
            if (s.artist() == null && s.album() == null) {
                // anonymous songs without album
                songs.add(s);
            } else if (s.artist() == null && s.album() != null){
                // anonymous albums
                albums.computeIfAbsent(s.album(), title -> new Album(title)).addSong(s);
            } else {
                // index by artist and then by album
                var artist = artists.computeIfAbsent(s.artist(), name -> new Artist(name));
                if (s.album() != null) artist.getOrCreateAlbum(s.album()).addSong(s);
                else artist.addSong(s);
            }
        }
    }
}
