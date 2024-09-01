package songs.metadata.mp3.frames.models;

import static songs.metadata.mp3.frames.models.Encoding.MPEG_1;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2_5;
import static java.util.Arrays.asList;
import static java.util.Map.of;

import java.util.HashMap;
import java.util.Map;

import songs.metadata.mp3.exceptions.FormatException;

enum Layer {
	
	RESERVED(0, of(), 0),
	LAYER_I(
		3,
		of(
			MPEG_1, 384,
			MPEG_2, 384,
			MPEG_2_5, 384
		),
		4
	),
	LAYER_II(
		2,
		of(
			MPEG_1, 1152,
			MPEG_2, 1152,
			MPEG_2_5, 1152
		),
		1
	),
	LAYER_III(
		1,
		of(
			MPEG_1, 1152,
			MPEG_2, 576,
			MPEG_2_5, 576
		),
		1
	);
	
	private int value;
	private Map<Encoding, Integer> samplesPerFrame = new HashMap<>();
	private int slotSize;
	
	Layer(int value, Map<Encoding, Integer> samplesPerFrame, int slotSize) {
		this.value = value;
		this.samplesPerFrame = samplesPerFrame;
		this.slotSize = slotSize;
	}
	
	int samplesPerFrame(Encoding encoding) {
		if (!samplesPerFrame.containsKey(encoding)) throw new FormatException("Wrong encoding");
		return samplesPerFrame.get(encoding);
	}
	
	int bitSlotSize() {
		return 8 * slotSize;
	}
	
	public static Layer from(int valor) {
		return asList(values()).stream().filter(v -> (v.value == valor)).findFirst().get();
	}
}