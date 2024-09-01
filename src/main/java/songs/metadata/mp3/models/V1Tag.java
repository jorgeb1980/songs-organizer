package songs.metadata.mp3.models;

import songs.metadata.mp3.frames.models.Genre;

public record V1Tag(
    String title,
    String artist,
    String album,
    String year,
    String comments,
    Genre genre
) {
	
	public static final V1Tag EMPTY = new V1Tag("", "", "", "", "", null);
}
