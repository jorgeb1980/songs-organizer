package songs.metadata.wma;

public class ASFUtils {

    /**
     * Compares a byte array, taking each byte as an unsigned integer, against an integer array
     * @return True if the bytes (as unsigned) have the same value as the integers
     */
    public static boolean areEqual(byte[] byteArray, int[] integerArray) {
        var ret = true;
        if (byteArray.length != integerArray.length) {
            ret = false;
        }
        else {
            int i = 0;
            while (i < byteArray.length && ret) {
                // Compare the unsigned integer value of the byte against the integer
                ret = ((byteArray[i] & 0xFF) == integerArray[i]);
                i++;
            }
        }
        return ret;
    }


}
