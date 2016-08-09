package general;


public class MatrixException extends Exception{
	private int status;
	
	public MatrixException(int s){
		this.status = s;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
}
