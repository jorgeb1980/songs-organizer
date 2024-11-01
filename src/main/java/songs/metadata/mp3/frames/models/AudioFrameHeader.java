package songs.metadata.mp3.frames.models;

import static java.math.RoundingMode.UP;

import java.math.BigDecimal;

// Some unimportant fields are discarded
public record AudioFrameHeader(
	Encoding encoding,
	Layer layer,
	boolean protection,
	BitRate bitRate,
	SampleRate sampleRate,
	boolean padding
) {
	private static final BigDecimal MIL = new BigDecimal(1000);
	
	// Tries to build a header out of the first 32 bits
	public static AudioFrameHeader build(byte[] buffer) {
		if (buffer.length != 4) return null;
		
		// Bits 0 - 10 - start mark
		// Bits 11, 12 - codification
		var codification = Encoding.from((buffer[1] & 0x18) >> 3);
		// Bits 13, 14 - capa
		var layer = Layer.from((buffer[1] & 0x06) >> 1);
		// Bits 16, 17, 18, 19 - bitrate
		var bitRate = BitRate.from((buffer[2] & 0xF0) >> 4);
		// Bits 20, 21 - sample rate
		var sampleRate = SampleRate.from((buffer[2] & 0x0C) >> 2);
		// Bit 22: do we have a padding slot at the beginning of the frame?
		boolean isTherePadding = ((buffer[2] & 0x02) >> 1) == 1;
		
		return new AudioFrameHeader(codification, layer, isTherePadding, bitRate, sampleRate, isTherePadding);
	}

	// return int(((m_dwCoefficients[m_bLSF][m_Layer] * m_dwBitrate / m_dwSamplesPerSec) + m_dwPaddingSize)) * m_dwSlotSizes[m_Layer];
	public int frameSizeBits() {
		// Frame Size in Bits = ( (Samples Per Frame * Sample size in Bits) + Padding Size
		var bitSlotSize = new BigDecimal(layer.bitSlotSize());
		
		var samplesPerFrame = new BigDecimal(layer.samplesPerFrame(encoding));
		
		var bitsPerSecond = new BigDecimal(bitRate.kbps(encoding, layer)).multiply(MIL);
		var sampleFrequency = new BigDecimal(sampleRate.sampleFrequency(encoding));
		
		var seconds = samplesPerFrame.divide(sampleFrequency);
		var sizeInBits = seconds.multiply(bitsPerSecond);
		var slotFrameSize = sizeInBits.divide(bitSlotSize, UP);
		if (padding) slotFrameSize = slotFrameSize.add(new BigDecimal(1));
		var frameSizeBits = slotFrameSize.multiply(bitSlotSize);
		
		return frameSizeBits.intValue();
	}
}