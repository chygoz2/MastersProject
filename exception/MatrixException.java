package exception;


public class MatrixException extends Exception{
	private String status;
	
	public MatrixException(String s){
		this.status = s;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
}
