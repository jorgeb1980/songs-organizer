package songs.index;

import songs.files.Song;
import songs.index.models.Album;
import songs.index.models.Artist;

import java.util.List;

public interface Index {

    List<Song> anonymousSongs();
    List<Artist> artists();
    List<Album> anonymousAlbums();

    void indexSong(Song s);
}