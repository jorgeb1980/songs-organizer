package songs.metadata.mp3;

import org.mozilla.intl.chardet.nsDetector;

import static songs.files.Utils.slice;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16LE;

import java.nio.charset.Charset;

class TagUtils {

	public static String readField(byte[] field) {
		var encoding = retrieveCharset(field);
		return decodeAndTrim(slice(field, 1, field.length - 1), encoding);
	}
	
	private static Charset retrieveCharset(byte[] string) {
		var detector = new nsDetector();
		detector.DoIt(string, string.length, false);
		detector.DataEnd();
		return Charset.forName(detector.getProbableCharsets()[0]);
	}
	
	public static String decodeAndTrim(byte[] field, Charset encoding) {
		// Removes padding
		return new String(field, encoding).trim().replaceAll("[\\p{Cf}]", "");
	}
    
    /* https://id3.org/id3v2.3.0#Comments -
    	This frame is intended for any kind of full text information that does not fit in any other frame.
    	It consists of a frame header followed by encoding, language and content descriptors and is 
    	ended with the actual comment as a text string. Newline characters are allowed in the comment text string. 
    	There may be more than one comment frame in each tag, but only one with the same language and content descriptor.
    */
    public static String retrieveExtendedText(byte[] data) {
        var encoding = retrieveCharset(data);
        // Discard header of the data
        var buffer = slice(data, 4, data.length - 4);
        int index = 0;
        if (encoding == ISO_8859_1) index = findLastDescriptorIndex(buffer);
        else if (encoding == UTF_16LE) index = findSecondBOMIndex(buffer);
        return decodeAndTrim(slice(buffer, index, buffer.length - index), encoding);
    }

	private static int findSecondBOMIndex(byte[] buffer) {
		var found = 0;
		var index = 0;
		while (found < 2 && index < buffer.length - 2) {
			if (((buffer[index] & 0xFF) == 0xFF) && ((buffer[index + 1] & 0xFE) == 0xFE))
				found++;
			if (found < 2) index++;
		}
		if (found == 2) return index; else return 0;
	}

	private static int findLastDescriptorIndex(byte[] buffer) {
		int index = 0;
		while (index < buffer.length && buffer[index] != 0) index++;
		if (buffer[index] == 0) return index; else return 0;
	}
}
