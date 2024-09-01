package songs.metadata.mp3.exceptions;

import java.io.Serial;

public class FormatException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -1361438817316160770L;

	public FormatException(String message) {
		super(message);
	}
}
