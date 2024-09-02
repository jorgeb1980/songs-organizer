package songs.metadata.mp3.models;

import lombok.Builder;

@Builder
public record V2Tag(
	String trackNumber,
	String originalArtist,
	String composer,
	String genre,
	String comments,
	String year,
	String album,
	String artist,
	String title
) {
	
	public static final V2Tag EMPTY = new V2Tag(
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null
		);

	public record Frame(
		String id,
		byte[] size,
		long translatedSize,
		byte[] flags,
		byte[] body
	) {}

	public record Header(
		byte[] version,
		byte[] size,
		byte[] flags,
		long translatedSize
	) {}
}


