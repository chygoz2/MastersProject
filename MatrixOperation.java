
public class MatrixOperation {
	
	public static double[][] multiply(double[][] a, double[][] b){
		
		if (a.length==0 || b.length==0) 
        	throw new RuntimeException("Empty matrix found");
		
		if (a[0].length != b.length) 
        	throw new RuntimeException("Matrix dimensions are not in correct form");
       
        double[][] result = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[i].length; j++)
                for (int k = 0; k < a[i].length; k++)
                    result[i][j] += a[i][k] * b[k][j];
        return result;
	}
}
