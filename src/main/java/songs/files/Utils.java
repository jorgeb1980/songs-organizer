package songs.files;

import java.util.Arrays;

public class Utils {
	
	public static byte[] slice(byte[] data, int from, int numBytes) {
		return Arrays.copyOfRange(data, from, from + numBytes);
	}
}
