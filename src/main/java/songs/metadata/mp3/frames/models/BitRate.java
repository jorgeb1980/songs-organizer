package songs.metadata.mp3.frames.models;

import static songs.metadata.mp3.frames.models.Layer.LAYER_I;
import static songs.metadata.mp3.frames.models.Layer.LAYER_II;
import static songs.metadata.mp3.frames.models.Layer.LAYER_III;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_1;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2;
import static songs.metadata.mp3.frames.models.Encoding.MPEG_2_5;
import static java.util.Arrays.asList;
import static java.util.Map.of;

import java.util.HashMap;
import java.util.Map;

import songs.metadata.mp3.exceptions.FormatException;

// https://www.codeproject.com/Articles/8295/MPEG-Audio-Frame-Header#Bitrate
enum BitRate {
	
	RESERVED(0, of()),
	ONE(
		1,
		of(
			Format.from(MPEG_1, LAYER_I), 32,
			Format.from(MPEG_1, LAYER_II), 32,
			Format.from(MPEG_1, LAYER_III), 32,
			Format.from(MPEG_2, LAYER_I), 32,
			Format.from(MPEG_2, LAYER_II), 32,
			Format.from(MPEG_2, LAYER_III), 8,
			Format.from(MPEG_2_5, LAYER_I), 32,
			Format.from(MPEG_2_5, LAYER_II), 32,
			Format.from(MPEG_2_5, LAYER_III), 8
		)		
	),
	TWO(
		2,
		of(
			Format.from(MPEG_1, LAYER_I), 64,
			Format.from(MPEG_1, LAYER_II), 48,
			Format.from(MPEG_1, LAYER_III), 40,
			Format.from(MPEG_2, LAYER_I), 48,
			Format.from(MPEG_2, LAYER_II), 16,
			Format.from(MPEG_2, LAYER_III), 16,
			Format.from(MPEG_2_5, LAYER_I), 48,
			Format.from(MPEG_2_5, LAYER_II), 16,
			Format.from(MPEG_2_5, LAYER_III), 16
		)
	),
	THREE(
		3,
		of(
			Format.from(MPEG_1, LAYER_I), 96,
			Format.from(MPEG_1, LAYER_II), 56,
			Format.from(MPEG_1, LAYER_III), 48,
			Format.from(MPEG_2, LAYER_I), 56,
			Format.from(MPEG_2, LAYER_II), 24,
			Format.from(MPEG_2, LAYER_III), 24,
			Format.from(MPEG_2_5, LAYER_I), 56,
			Format.from(MPEG_2_5, LAYER_II), 24,
			Format.from(MPEG_2_5, LAYER_III), 24
		)
	),
	FOUR(
		4,
		of(
			Format.from(MPEG_1, LAYER_I), 128,
			Format.from(MPEG_1, LAYER_II), 64,
			Format.from(MPEG_1, LAYER_III), 56,
			Format.from(MPEG_2, LAYER_I), 64,
			Format.from(MPEG_2, LAYER_II), 32,
			Format.from(MPEG_2, LAYER_III), 32,
			Format.from(MPEG_2_5, LAYER_I), 64,
			Format.from(MPEG_2_5, LAYER_II), 32,
			Format.from(MPEG_2_5, LAYER_III), 32
		)
	),
	FIVE(
		5,
		of(
			Format.from(MPEG_1, LAYER_I), 160,
			Format.from(MPEG_1, LAYER_II), 80,
			Format.from(MPEG_1, LAYER_III), 64,
			Format.from(MPEG_2, LAYER_I), 80,
			Format.from(MPEG_2, LAYER_II), 40,
			Format.from(MPEG_2, LAYER_III), 40,
			Format.from(MPEG_2_5, LAYER_I), 80,
			Format.from(MPEG_2_5, LAYER_II), 40,
			Format.from(MPEG_2_5, LAYER_III), 40
		)
	),
	SIX(
		6,
		of(
			Format.from(MPEG_1, LAYER_I), 192,
			Format.from(MPEG_1, LAYER_II), 96,
			Format.from(MPEG_1, LAYER_III), 80,
			Format.from(MPEG_2, LAYER_I), 96,
			Format.from(MPEG_2, LAYER_II), 48,
			Format.from(MPEG_2, LAYER_III), 48,
			Format.from(MPEG_2_5, LAYER_I), 96,
			Format.from(MPEG_2_5, LAYER_II), 48,
			Format.from(MPEG_2_5, LAYER_III), 48
		)
	),
	SEVEN(
		7,
		of(
			Format.from(MPEG_1, LAYER_I), 224,
			Format.from(MPEG_1, LAYER_II), 112,
			Format.from(MPEG_1, LAYER_III), 96,
			Format.from(MPEG_2, LAYER_I), 112,
			Format.from(MPEG_2, LAYER_II), 56,
			Format.from(MPEG_2, LAYER_III), 56,
			Format.from(MPEG_2_5, LAYER_I), 112,
			Format.from(MPEG_2_5, LAYER_II), 56,
			Format.from(MPEG_2_5, LAYER_III), 56
		)
	),
	EIGHT(
		8,
		of(
			Format.from(MPEG_1, LAYER_I), 256,
			Format.from(MPEG_1, LAYER_II), 128,
			Format.from(MPEG_1, LAYER_III), 112,
			Format.from(MPEG_2, LAYER_I), 128,
			Format.from(MPEG_2, LAYER_II), 64,
			Format.from(MPEG_2, LAYER_III), 64,
			Format.from(MPEG_2_5, LAYER_I), 128,
			Format.from(MPEG_2_5, LAYER_II), 64,
			Format.from(MPEG_2_5, LAYER_III), 64
		)
	),
	NINE(
		9,
		of(
			Format.from(MPEG_1, LAYER_I), 288,
			Format.from(MPEG_1, LAYER_II), 160,
			Format.from(MPEG_1, LAYER_III), 128,
			Format.from(MPEG_2, LAYER_I), 144,
			Format.from(MPEG_2, LAYER_II), 80,
			Format.from(MPEG_2, LAYER_III), 80,
			Format.from(MPEG_2_5, LAYER_I), 144,
			Format.from(MPEG_2_5, LAYER_II), 80,
			Format.from(MPEG_2_5, LAYER_III), 80
		)
	),
	TEN(
		10,
		of(
			Format.from(MPEG_1, LAYER_I), 320,
			Format.from(MPEG_1, LAYER_II), 192,
			Format.from(MPEG_1, LAYER_III), 160,
			Format.from(MPEG_2, LAYER_I), 160,
			Format.from(MPEG_2, LAYER_II), 96,
			Format.from(MPEG_2, LAYER_III), 96,
			Format.from(MPEG_2_5, LAYER_I), 160,
			Format.from(MPEG_2_5, LAYER_II), 96,
			Format.from(MPEG_2_5, LAYER_III), 96
		)
	),
	ELEVEN(
		11,
		of(
			Format.from(MPEG_1, LAYER_I), 352,
			Format.from(MPEG_1, LAYER_II), 224,
			Format.from(MPEG_1, LAYER_III), 192,
			Format.from(MPEG_2, LAYER_I), 176,
			Format.from(MPEG_2, LAYER_II), 112,
			Format.from(MPEG_2, LAYER_III), 112,
			Format.from(MPEG_2_5, LAYER_I), 176,
			Format.from(MPEG_2_5, LAYER_II), 112,
			Format.from(MPEG_2_5, LAYER_III), 112
		)
	),
	TWELVE(
		12,
		of(
			Format.from(MPEG_1, LAYER_I), 384,
			Format.from(MPEG_1, LAYER_II), 256,
			Format.from(MPEG_1, LAYER_III), 224,
			Format.from(MPEG_2, LAYER_I), 192,
			Format.from(MPEG_2, LAYER_II), 128,
			Format.from(MPEG_2, LAYER_III), 128,
			Format.from(MPEG_2_5, LAYER_I), 192,
			Format.from(MPEG_2_5, LAYER_II), 128,
			Format.from(MPEG_2_5, LAYER_III), 128
		)
	),
	THIRTEEN(
		13,
		of(
			Format.from(MPEG_1, LAYER_I), 416,
			Format.from(MPEG_1, LAYER_II), 320,
			Format.from(MPEG_1, LAYER_III), 256,
			Format.from(MPEG_2, LAYER_I), 224,
			Format.from(MPEG_2, LAYER_II), 144,
			Format.from(MPEG_2, LAYER_III), 144,
			Format.from(MPEG_2_5, LAYER_I), 224,
			Format.from(MPEG_2_5, LAYER_II), 144,
			Format.from(MPEG_2_5, LAYER_III), 144
		)
	),
	FOURTEEN(
		14,
		of(
			Format.from(MPEG_1, LAYER_I), 448,
			Format.from(MPEG_1, LAYER_II), 384,
			Format.from(MPEG_1, LAYER_III), 320,
			Format.from(MPEG_2, LAYER_I), 256,
			Format.from(MPEG_2, LAYER_II), 160,
			Format.from(MPEG_2, LAYER_III), 160,
			Format.from(MPEG_2_5, LAYER_I), 256,
			Format.from(MPEG_2_5, LAYER_II), 160,
			Format.from(MPEG_2_5, LAYER_III), 160
		)
	),
	FREE(15, of());
	
	final private int value;
	private Map<Format, Integer> kbpsTable = new HashMap<>();
	
	BitRate(int value, Map<Format, Integer> kbpsTable) {
		this.value = value; this.kbpsTable = kbpsTable;
	}
	
	int kbps(Encoding encoding, Layer layer) {
		if (!kbpsTable.containsKey(Format.from(encoding, layer)))
			throw new FormatException("Wrong codification and/or layer");
		return kbpsTable.get(Format.from(encoding, layer));
	}
	
	static BitRate from(int value) {
		return asList(values()).stream().filter(v -> (v.value == value)).findFirst().get();
	}
}