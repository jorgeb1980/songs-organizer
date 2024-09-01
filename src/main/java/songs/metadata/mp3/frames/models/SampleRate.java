package songs.metadata.mp3.frames.models;

import static songs.metadata.mp3.frames.models.Encoding.MPEG_1;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2_5;
import static java.util.Arrays.asList;
import static java.util.Map.of;

import java.util.HashMap;
import java.util.Map;

// https://www.codeproject.com/Articles/8295/MPEG-Audio-Frame-Header#SamplingRate
enum SampleRate {
	
	ZERO(0,
		of(
			MPEG_1, 44100,
			MPEG_2, 22050,
			MPEG_2_5, 11025
		)
	),
	ONE(1,
		of(
			MPEG_1, 48000,
			MPEG_2, 24000,
			MPEG_2_5, 12000
		)
	),
	TWO(2,
		of(
			MPEG_1, 32000,
			MPEG_2, 16000,
			MPEG_2_5, 8000
		)
	),
	RESERVED(3, of());
	
	private int value;
	private Map<Encoding, Integer> frequencyTableHertz = new HashMap<>();
	
	SampleRate(int value, Map<Encoding, Integer> frequencyTableHertz) {
		this.value = value;
		this.frequencyTableHertz = frequencyTableHertz;
	}
	
	int sampleFrequency(Encoding encoding) {
		return frequencyTableHertz.get(encoding);
	}
	
	public static SampleRate from(int value) {
		return asList(values()).stream().filter(v -> v.value == value).findFirst().get();
	}
}