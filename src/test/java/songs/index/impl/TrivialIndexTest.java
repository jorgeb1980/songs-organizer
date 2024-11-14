package songs.index.impl;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import songs.files.Song;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrivialIndexTest {

    @Builder
    private static class MockSong extends Song {

        private String title;
        private String artist;
        private String album;
        private String year;
        private String composer;
        private String comments;
        private String originalArtist;
        private String trackNumber;
        private String genre;
        private String author;

        @Override
        public String title() { 
            return title;
        }
        @Override
        public String artist() {
            return artist;
        }
        @Override
        public String album() {
            return album;
        }
        @Override
        public String year() {
            return year;
        }
        @Override
        public String composer() {
            return composer;
        }
        @Override
        public String comments() {
            return comments;
        }
        @Override
        public String originalArtist() {
            return originalArtist;
        }
        @Override
        public String trackNumber() {
            return trackNumber;
        }
        @Override
        public String genre() {
            return genre;
        }
        @Override
        public String author() {
            return author;
        }
    }

    @Test
    public void testTrivialIndex() {
        var index = new TrivialIndex();
        index.indexSong(MockSong.builder().artist("ar1").title("s1").build());
        index.indexSong(MockSong.builder().artist("ar1").title("s2").build());
        index.indexSong(MockSong.builder().artist("ar2").album("al1").title("s1").build());
        index.indexSong(MockSong.builder().artist("ar2").album("al1").title("s2").build());
        index.indexSong(MockSong.builder().artist("ar2").album("al1").title("s3").build());
        index.indexSong(MockSong.builder().artist("ar2").album("al2").title("s1").build());
        index.indexSong(MockSong.builder().artist("ar2").album("al2").title("s2").build());

        assertTrue(index.anonymousSongs().isEmpty());
        assertTrue(index.anonymousAlbums().isEmpty());
        assertEquals(2, index.artists().size());
        assertEquals(1, index.artists().stream().filter(a -> a.getName().equals("ar1")).toList().size());
        assertEquals(1, index.artists().stream().filter(a -> a.getName().equals("ar2")).toList().size());

        // artist 1 has 2 songs without album, no albums
        var ar1 = index.artists().stream().filter(a -> a.getName().equals("ar1")).findFirst().get();
        assertTrue(ar1.getAlbums().isEmpty());
        assertEquals(2, ar1.getSongs().size());
        assertEquals(1, ar1.getSongs().stream().filter(song -> song.title().equals("s1")).toList().size());
        assertEquals(1, ar1.getSongs().stream().filter(song -> song.title().equals("s2")).toList().size());

        // artist 2 has 2 albums, 1 with 3 songs, 1 with 2 songs, no songs without album
        var ar2 = index.artists().stream().filter(a -> a.getName().equals("ar2")).findFirst().get();
        assertEquals(2, ar2.getAlbums().size());
        var ar2al1 = ar2.getOrCreateAlbum("al1");
        assertEquals(3, ar2al1.getSongs().size());
        assertEquals(1, ar2al1.getSongs().stream().filter(song -> song.title().equals("s1")).toList().size());
        assertEquals(1, ar2al1.getSongs().stream().filter(song -> song.title().equals("s2")).toList().size());
        assertEquals(1, ar2al1.getSongs().stream().filter(song -> song.title().equals("s3")).toList().size());

        var ar2al2 = ar2.getOrCreateAlbum("al2");
        assertEquals(2, ar2al2.getSongs().size());
        assertEquals(1, ar2al2.getSongs().stream().filter(song -> song.title().equals("s1")).toList().size());
        assertEquals(1, ar2al2.getSongs().stream().filter(song -> song.title().equals("s2")).toList().size());

    }
}
