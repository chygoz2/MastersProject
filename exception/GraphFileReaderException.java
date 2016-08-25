package exception;

public class GraphFileReaderException extends Exception {
	
	private String error;
	
	public GraphFileReaderException(String s){
		this.error = s;
	}
	
	public String getError(){
		return error;
	}
}
