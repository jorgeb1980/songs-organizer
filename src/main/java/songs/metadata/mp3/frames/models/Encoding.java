package songs.metadata.mp3.frames.models;

import static java.util.Arrays.asList;

// https://www.codeproject.com/Articles/8295/MPEG-Audio-Frame-Header#MPEGAudioFrameHeader
enum Encoding {
	
	MPEG_2_5(0), RESERVED(1), MPEG_2(2), MPEG_1(3);
	
	private int value;
	
	Encoding(int value) { this.value = value; }
	
	public static Encoding from(int value) {
		return asList(values()).stream().filter(v -> (v.value == value)).findFirst().get();
	}
}