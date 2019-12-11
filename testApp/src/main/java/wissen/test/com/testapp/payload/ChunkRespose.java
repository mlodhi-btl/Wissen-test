package wissen.test.com.testapp.payload;

public class ChunkRespose {
	
	private String fileName;
	private int chunk_number;
	
	private String message;

	
	
	public ChunkRespose(String fileName, int chunk_number, String message) {
		this.fileName=fileName;
		this.chunk_number=chunk_number;
		this.message=message;
		
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getChunk_number() {
		return chunk_number;
	}

	public void setChunk_number(int chunk_number) {
		this.chunk_number = chunk_number;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
