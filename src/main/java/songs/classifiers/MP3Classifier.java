package songs.classifiers;

import songs.files.MP3File;
import songs.files.Song;
import songs.metadata.mp3.V1TagService;
import songs.metadata.mp3.V2TagService;
import songs.metadata.mp3.frames.models.AudioFrameHeader;
import songs.metadata.mp3.models.V1Tag;
import songs.metadata.mp3.models.V2Tag;

import java.io.File;
import java.io.RandomAccessFile;

import static java.util.Arrays.copyOfRange;

public class MP3Classifier implements SongsClassifier {

    private static final Integer BUFFER_SIZE = 10 * 1024;

    @Override
    public Song classify(File f) {
        Song ret = null;
        try {
            V2Tag tag2 = V2TagService.buildTag(f);
            V1Tag tag1 = V1TagService.buildTag(f);
            if (tag1 != null || tag2 != null) {
                ret = new MP3File(f, tag1, tag2);
            }
            if (ret == null) {
                try (RandomAccessFile bis = new RandomAccessFile(f, "r")) {
                    // Apply a heuristic method - if a valid MP3 frame is not found in the first BUFFER_SIZE bytes, then
                    //  we decide it is not an MP3 file
                    var bufferRecon = new byte[BUFFER_SIZE];
                    bis.seek(0);
                    var read = bis.read(bufferRecon, 0,  bufferRecon.length);
                    var test = copyOfRange(bufferRecon, 0, read);

                    /* Apply the heuristic suggested in

                    https://www.codeproject.com/Articles/8295/MPEG-Audio-Frame-Header
                    https://stackoverflow.com/questions/11360286/detect-if-a-file-is-an-mp3-file

                    in order to determine if the file is an MP3 file. Steps:

                    - Search for the sync word in the file (0xFFF or 0xFFE).
                    - Parse the header parameters.
                    - Determine the frame length using the header parameters.
                    - Seek to the next frame using the frame length.
                    - If you find another sync word after seeking, then the file is mostly an MP3 file.
                    - To be sure, repeat the process to find N consecutive MP3 frames. N can be increased for a better hit-rate.
                    */
                    if (lookForFirstFrames(test)) {
                        ret = new MP3File(f);
                    }
                }
            }
        } catch (Exception e) {
            System.err.format("Discarded %s -> %s%n", f.getAbsolutePath(), e.getMessage());
            e.printStackTrace();
            ret = null;
        }
        return ret;
    }

    private boolean lookForFirstFrames(byte[] buffer) throws Exception {
        int hits = 0;
        int i = 0;
        while (i < buffer.length - 3) {
            // Look for the synchronization word
            if (
                (buffer[i] & 0xFF) == 0xFF
                    &&
                    (buffer[i + 1] & 0xE0) == 0xE0
            ) {
                AudioFrameHeader header = AudioFrameHeader.build(copyOfRange(buffer, i, i + 4));
                int frameSizeBytes = header.frameSizeBits() / 8;
                i += frameSizeBytes;
                hits++;
            } else i++;
        }
        return (hits >= 2);
    }
}
