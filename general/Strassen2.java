package general;
import java.util.Random;

import exception.MatrixException;

public class Strassen2
{
    Random r, r1;

//  private static int[][] a = new int[32][32];
//  private static int[][] b = new int[32][32];
//  private static int[][] c = new int[32][32];
    private int[][] a ;
    private int[][] b;
    private int[][] c;
  
    /** Function to multiply matrices **/
    public int[][] multiply(int[][] A, int[][] B)
    {        
        int n = A.length;
        if(n>250){
			try {
				return Utility.multiplyMatrix(A, B);
			} catch (MatrixException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        int[][] R = new int[n][n];
        /** base case **/
        if (n == 1)
            R[0][0] = A[0][0] * B[0][0];
        else
        {
            int[][] A11 = new int[n/2][n/2];
            int[][] A12 = new int[n/2][n/2];
            int[][] A21 = new int[n/2][n/2];
            int[][] A22 = new int[n/2][n/2];
            int[][] B11 = new int[n/2][n/2];
            int[][] B12 = new int[n/2][n/2];
            int[][] B21 = new int[n/2][n/2];
            int[][] B22 = new int[n/2][n/2];
 
            /** Dividing matrix A into 4 halves **/
            split(A, A11, 0 , 0);
            split(A, A12, 0 , n/2);
            split(A, A21, n/2, 0);
            split(A, A22, n/2, n/2);
            /** Dividing matrix B into 4 halves **/
            split(B, B11, 0 , 0);
            split(B, B12, 0 , n/2);
            split(B, B21, n/2, 0);
            split(B, B22, n/2, n/2);
 
            /** 
              M1 = (A11 + A22)(B11 + B22)
              M2 = (A21 + A22) B11
              M3 = A11 (B12 - B22)
              M4 = A22 (B21 - B11)
              M5 = (A11 + A12) B22
              M6 = (A21 - A11) (B11 + B12)
              M7 = (A12 - A22) (B21 + B22)
            **/
 
            int [][] M1 = multiply(add(A11, A22), add(B11, B22));
            int [][] M2 = multiply(add(A21, A22), B11);
            int [][] M3 = multiply(A11, sub(B12, B22));
            int [][] M4 = multiply(A22, sub(B21, B11));
            int [][] M5 = multiply(add(A11, A12), B22);
            int [][] M6 = multiply(sub(A21, A11), add(B11, B12));
            int [][] M7 = multiply(sub(A12, A22), add(B21, B22));
 
            /**
              C11 = M1 + M4 - M5 + M7
              C12 = M3 + M5
              C21 = M2 + M4
              C22 = M1 - M2 + M3 + M6
            **/
            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);
 
            /** join 4 halves into one result matrix **/
            join(C11, R, 0 , 0);
            join(C12, R, 0 , n/2);
            join(C21, R, n/2, 0);
            join(C22, R, n/2, n/2);
        }
        /** return result **/    
        return R;
    }
    /** Funtion to sub two matrices **/
    public int[][] sub(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }
    /** Funtion to add two matrices **/
    public int[][] add(int[][] A, int[][] B)
    {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }
    /** Funtion to split parent matrix into child matrices **/
    public void split(int[][] P, int[][] C, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                C[i1][j1] = P[i2][j2];
    }
    /** Funtion to join child matrices intp parent matrix **/
    public void join(int[][] C, int[][] P, int iB, int jB) 
    {
        for(int i1 = 0, i2 = iB; i1 < C.length; i1++, i2++)
            for(int j1 = 0, j2 = jB; j1 < C.length; j1++, j2++)
                P[i2][j2] = C[i1][j1];
    } 
    
    public static void main(String[] args) {
        Random r = new Random(1000);
//        Random r1 = new Random(1000);
        int n = 6000;
        Strassen2 s = new Strassen2();
        int[][] arr = new int[n][n];
        s.a = new int[n][n];
//        s.b = new int[n][n];
        s.c = new int[n][n];
        
        for(int i=0;i<n;i++)
         {
             for(int j =0; j<n;j++)
             {
                 s.a[i][j] = r.nextInt(1000);
//                 s.b[i][j] = r1.nextInt(1000);
             }
         }
        
        long start = System.currentTimeMillis();
        arr = s.multiply(s.a,s.a);
        long end = System.currentTimeMillis();
        String out = "Time taken by strassen= "+(end-start);
        
//        for (int i = 0; i < 200; i++)
//        {
//            for (int j = 0; j < 200; j++)
//                System.out.print(arr[i][j] +" ");
//            System.out.println();
//        }
        
//        start = System.currentTimeMillis();
//        
//        multiply(a,b);
//        end = System.currentTimeMillis();
//        out +=("\nTime taken by normal= "+(end-start));
        System.out.println(out);
        
     }
}