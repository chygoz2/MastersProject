import Jama.Matrix;

public class Test {
	public static void main(String [] args){
		double[][] a = {{1,2,3},{4,5,6},{7,8,9}};
		Matrix mA = new Matrix(a);
		System.out.println(mA.getRowDimension() + " is the row dimension");
		System.out.println(mA.getColumnDimension() + " is the column dimension");
		mA.print(3, 2);
		Matrix squareMatrix = mA.times(mA);
		squareMatrix.print(3, 0);
	}
}
