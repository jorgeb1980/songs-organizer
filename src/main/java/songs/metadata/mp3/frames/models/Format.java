package songs.metadata.mp3.frames.models;

record Format(
	Encoding encoding,
	Layer layer
) {
	public static Format from(Encoding encoding, Layer layer) {
		return new Format(encoding, layer);
	}
}
