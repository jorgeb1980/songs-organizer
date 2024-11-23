package songs.metadata.wma;

import songs.files.Utils;
import songs.metadata.wma.models.ASFTag;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static songs.metadata.wma.ASFUtils.areEqual;
import static songs.metadata.wma.Constants.FORMAT_PROBLEM;
import static songs.metadata.wma.Constants.ID_CONTENT_DESCRIPTOR;
import static songs.metadata.wma.Constants.ID_DATA_OBJECT;
import static songs.metadata.wma.Constants.ID_EXTENDED_CONTENT_DESCRIPTOR;
import static songs.metadata.wma.Constants.ID_HEADER;
import static songs.metadata.wma.Constants.LENGTH_IDENTIFIER_BYTES;
import static songs.metadata.wma.Constants.LENGTH_INFO_SIZE_FIELD_BYTES;
import static songs.metadata.wma.Constants.LENGTH_SIZE_FIELD_BYTES;
import static songs.metadata.wma.Constants.TYPE_BOOL;
import static songs.metadata.wma.Constants.TYPE_BYTES;
import static songs.metadata.wma.Constants.TYPE_DWORD;
import static songs.metadata.wma.Constants.TYPE_QWORD;
import static songs.metadata.wma.Constants.TYPE_UNICODE_STRING;
import static songs.metadata.wma.Constants.TYPE_WORD;
import static songs.metadata.wma.Constants.WM_ALBUM;
import static songs.metadata.wma.Constants.WM_ARTIST;
import static songs.metadata.wma.Constants.WM_COMPOSER;
import static songs.metadata.wma.Constants.WM_GENRE;
import static songs.metadata.wma.Constants.WM_TRACKNUMBER;
import static songs.metadata.wma.Constants.WM_YEAR;

public class WMATagService {
	
	private WMATagService() {}
	
	public static ASFTag buildTag(File file) throws IOException {
		ASFTag ret = null;
		var foundData = false;
	    try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
	        var buffer = new byte[LENGTH_IDENTIFIER_BYTES];
	        raf.read(buffer);
	        var ctx = new TranslationContext();
	        
	        if (areEqual(buffer, ID_HEADER)) {
	        	raf.seek(0);
	        	var identifier = new byte[LENGTH_IDENTIFIER_BYTES];
		        var fileSize = raf.length();
		        while (!foundData && ctx.nextObject() < fileSize) {
		            readIdentifier(raf, ctx.nextObject(), identifier);
		            if (areEqual(identifier, ID_DATA_OBJECT)) {
						// we are done
		                foundData = true;
		            }
		            else if (areEqual(identifier, ID_CONTENT_DESCRIPTOR)) {
		                readContentDescriptor(raf, ctx);
		            }
		            else if (areEqual(identifier, ID_EXTENDED_CONTENT_DESCRIPTOR)) {
		                readExtendedContentDescriptor(raf, ctx);
		            }
		            else {
		                // Something else - not interested, calculate next object
		                calculateStartNextObject(raf, ctx);
		            }
		        }
	        }
	        ret = ctx.builder().build();
        }
        return ret;
	}
	
	/**
	 * Read optional fields in the descriptor.  Assumes the cursor is at the end of
	 * current object identifier
     */ 
    private static void readExtendedContentDescriptor(
		RandomAccessFile raf,
		TranslationContext ctx
	) throws IOException {
        calculateStartNextObject(raf, ctx);
        var numDescriptors = readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var map = new HashMap<String, Object>();
        for (int i = 0; i < numDescriptors; i++) readDescriptor(raf, map);
        var c = ctx.builder();
        for (String key: map.keySet())
        	switch(key) {
	        	case WM_ALBUM -> c.album((String) map.get(key));
	        	case WM_ARTIST -> c.artist((String) map.get(key));
	        	case WM_COMPOSER -> c.composer((String) map.get(key));
	        	case WM_GENRE -> c.genre((String) map.get(key));
	        	case WM_TRACKNUMBER -> {
		            	// String or long integer?
		            	if (map.get(key) instanceof Long) {
		            		 c.trackNumber(((Long) map.get(key)).toString());
		            	}
		            	else if (map.get(key) instanceof String) {
		            		c.trackNumber((String) map.get(key));
		            	}
		            }
	        	case WM_YEAR -> c.year((String) map.get(key));
        	}
    }

    /**
     * Reads the current descriptor content into the map passed as parameter
     */
    private static void readDescriptor(
		RandomAccessFile raf,
		Map<String, Object> map
	) throws IOException {
        var nameLength = readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var propertyName = readUnicodeString(raf, nameLength);
        var dataType = (int) readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var size = (int) readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
    	switch(dataType) {
        	case TYPE_UNICODE_STRING -> {
        		map.put(propertyName, readUnicodeString(raf, size));
        	}
        	case TYPE_BYTES -> {
        	    var buffer = new byte[size];
        		raf.read(buffer);
        		map.put(propertyName, new String(buffer));
        	}
        	case TYPE_BOOL -> {
        	    // 4-byte boolean
        		map.put(propertyName, readInteger(raf, size) != 0);
        	}
        	case TYPE_DWORD, TYPE_QWORD, TYPE_WORD -> {
        		map.put(propertyName, readInteger(raf, size));
        	}
        	default -> readInteger(raf, size);
        }
    }

    /**
	 * Calculate offset for the next object based on its stated size.
	 * We assume the cursor is at the end of the current object.
     */
    private static void calculateStartNextObject(
		RandomAccessFile raf,
		TranslationContext ctx
	) throws IOException {
        var objectSize = readInteger(raf, LENGTH_SIZE_FIELD_BYTES);
        if (objectSize <= 0) throw new IOException(FORMAT_PROBLEM);
        ctx.incrementNextObject(objectSize);
    }
    
    /**
	 * Reads an N-byte integer (little endian), moving the cursor at the same time
     */
    private static long readInteger(
		RandomAccessFile raf,
		int lengthInBytes
	) throws IOException {
        var ret = -1;
        var integer = new byte[lengthInBytes];
        var readBytes = raf.read(integer);
        if (readBytes == lengthInBytes) {
            ret = 0;
            for (int i = 0; i < lengthInBytes; i++) {
                int value = integer[i] & 0xFF;
                // Accumulate the value (remembering it is little endian)
                ret += (value * (int) Math.pow(256, i));
            }            
        }
        return ret;
    }

    /**
	 * Stores relevant information in the content descriptor (title, author, etc.)
	 * It assumes the cursor is at the end of the identifier of the ASF content
	 * descriptor object.
     */
    private static void readContentDescriptor(
		RandomAccessFile raf,
		TranslationContext ctx
	) throws IOException {
        calculateStartNextObject(raf, ctx);
        var titleLength = 		readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var authorLength = 		readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var copyrightLength = 	readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var descriptionLength = readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
		// Discard the length of the rating
        readInteger(raf, LENGTH_INFO_SIZE_FIELD_BYTES);
        var c = ctx.builder();
        c.title(readUnicodeString(raf, titleLength));
        c.author(readUnicodeString(raf, authorLength));
        // Not actually interested in the copyright
        raf.skipBytes((int) copyrightLength);
        c.description(readUnicodeString(raf, descriptionLength));
    }

    /**
	 * Reads a unicode string from the file starting in the cursor, for the specified
	 * length.  It moves the cursor! Non-idempotent
     */
    private static String readUnicodeString(
		RandomAccessFile raf,
		long length
	) throws IOException {
        var bufferBytes = new byte[(int)length];
        raf.read(bufferBytes);
		return Utils.decodeAndTrim(bufferBytes, StandardCharsets.UTF_16LE);
    }

    /**
	 * Jumps to the position and reads whatever identifier is there into the byte array
     */
    private static void readIdentifier(
		RandomAccessFile raf,
		long position,
		byte[] identifier
	) throws IOException {
        raf.seek(position);
        raf.read(identifier);
    }
    
    private static class TranslationContext {

    	final private ASFTag.ASFTagBuilder builder;
    	private long nextObject;
    	
    	TranslationContext() {
			builder = ASFTag.builder();
			// Initial object: +30 bytes
    		nextObject = 30;
    	}
    	
    	public ASFTag.ASFTagBuilder builder() { return builder; }
    	public void incrementNextObject(long value) { nextObject += value; }
    	public long nextObject() { return nextObject; }
    }
}
