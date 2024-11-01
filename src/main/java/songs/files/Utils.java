package songs.files;

import org.mozilla.intl.chardet.nsDetector;

import java.nio.charset.Charset;
import java.util.Arrays;

public class Utils {

	public static boolean compare(byte[] byteArray, int[] intArray) {
		if (byteArray.length == intArray.length) {
			var same = true;
			int i = 0;
			while (i < byteArray.length && same) {
				same = ((byteArray[i] & 0xFF) == intArray[i]);
				i++;
			}
			return same;
		}
		else return false;
	}

	public static byte[] slice(byte[] data, int from, int numBytes) {
		return Arrays.copyOfRange(data, from, from + numBytes);
	}

	public static Charset retrieveCharset(byte[] string) {
		var detector = new nsDetector();
		detector.DoIt(string, string.length, false);
		detector.DataEnd();
		return Charset.forName(detector.getProbableCharsets()[0]);
	}

	public static String decodeAndTrim(byte[] field, Charset encoding) {
		// Removes padding
		return new String(field, encoding).trim().replaceAll("[\\p{Cf}]", "");
	}
}
