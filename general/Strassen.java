package general;
/*
	Please read the README file for more information about this project
    Copyright (C) 2013 "Srinivas Prasad Gumdelli"
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 * @author gumdelli
 */
import java.util.*;


public class Strassen {

    Random r, r1;

//    private static double[][] a = new double[32][32];
//    private static double[][] b = new double[32][32];
//    private static double[][] c = new double[32][32];
    private static double[][] a ;
    private static double[][] b;
    private static double[][] c;

    public static void multiply(double[][] a, double[][] b)
    {
        int i,j;

        for(i=0;i<a.length;i++)
        {
            for(j =0; j<a.length;j++)
            {   c[i][j]=0;
                for(int k = 0;k<a.length;k++)
                {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        System.out.println("Matrix multiplication by regular method : ");
        System.out.println();
        for(i=0;i<a.length;i++)
        {
            for(j =0; j<a.length;j++)
            {
                System.out.print(c[i][j] + "  ");
            }
            System.out.println();
        }
    }

    public static double [][] strassen(double [][] a, double [][] b)
	{
		int n = a.length;
		double [][] result = new double[n][n];

		if((n%2 != 0 ) && (n !=1))
		{
			double[][] a1, b1, c1;
			int n1 = n+1;
			a1 = new double[n1][n1];
			b1 = new double[n1][n1];
			c1 = new double[n1][n1];

			for(int i=0; i<n; i++)
				for(int j=0; j<n; j++)
				{
					a1[i][j] =a[i][j];
					b1[i][j] =b[i][j];
				}
			c1 = strassen(a1, b1);
			for(int i=0; i<n; i++)
				for(int j=0; j<n; j++)
					result[i][j] =c1[i][j];
			return result;
		}

		if(n == 1)
		{
			result[0][0] = a[0][0] * b[0][0];
		}
		else
		{
			double [][] A11 = new double[n/2][n/2];
			double [][] A12 = new double[n/2][n/2];
			double [][] A21 = new double[n/2][n/2];
			double [][] A22 = new double[n/2][n/2];

			double [][] B11 = new double[n/2][n/2];
			double [][] B12 = new double[n/2][n/2];
			double [][] B21 = new double[n/2][n/2];
			double [][] B22 = new double[n/2][n/2];

			divide(a, A11, 0 , 0);
			divide(a, A12, 0 , n/2);
			divide(a, A21, n/2, 0);
			divide(a, A22, n/2, n/2);

			divide(b, B11, 0 , 0);
			divide(b, B12, 0 , n/2);
			divide(b, B21, n/2, 0);
			divide(b, B22, n/2, n/2);

			double [][] P1 = strassen(add(A11, A22), add(B11, B22));
			double [][] P2 = strassen(add(A21, A22), B11);
			double [][] P3 = strassen(A11, sub(B12, B22));
			double [][] P4 = strassen(A22, sub(B21, B11));
			double [][] P5 = strassen(add(A11, A12), B22);
			double [][] P6 = strassen(sub(A21, A11), add(B11, B12));
			double [][] P7 = strassen(sub(A12, A22), add(B21, B22));

			double [][] C11 = add(sub(add(P1, P4), P5), P7);
			double [][] C12 = add(P3, P5);
			double [][] C21 = add(P2, P4);
			double [][] C22 = add(sub(add(P1, P3), P2), P6);

			copy(C11, result, 0 , 0);
			copy(C12, result, 0 , n/2);
			copy(C21, result, n/2, 0);
			copy(C22, result, n/2, n/2);
		}
		return result;
	}

	public static double [][] add(double [][] A, double [][] B)
	{
		int n = A.length;

		double [][] result = new double[n][n];

		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
			result[i][j] = A[i][j] + B[i][j];

		return result;
	}

	public static double [][] sub(double [][] A, double [][] B)
	{
		int n = A.length;

		double [][] result = new double[n][n];

		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
			result[i][j] = A[i][j] - B[i][j];

		return result;
	}

	public static void divide(double[][] p1, double[][] c1, int iB, int jB)
	{
		for(int i1 = 0, i2=iB; i1<c1.length; i1++, i2++)
			for(int j1 = 0, j2=jB; j1<c1.length; j1++, j2++)
			{
				c1[i1][j1] = p1[i2][j2];
			}
	}

	public static void copy(double[][] c1, double[][] p1, int iB, int jB)
	{
		for(int i1 = 0, i2=iB; i1<c1.length; i1++, i2++)
			for(int j1 = 0, j2=jB; j1<c1.length; j1++, j2++)
			{
				p1[i2][j2] = c1[i1][j1];
			}
	}

    
        public static void print(double [][] array)
	{
		int n = array.length;

		System.out.println();
		for(int i=0; i<n; i++)
		{
			for(int j=0; j<n; j++)
			{
				System.out.print(array[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
    public static void main(String[] args) {
       Random r = new Random(10000);
       Random r1 = new Random(1000);
       int n = 256;
       double[][] arr = new double[n][n];
       a = new double[n][n];
       b = new double[n][n];
       c = new double[n][n];
       int i,j;
       for(i=0;i<200;i++)
        {
            for(j =0; j<200;j++)
            {
                a[i][j] = r.nextInt(10000);
                b[i][j] = r1.nextInt(1000);
            }
        }
       long start = System.currentTimeMillis();
       arr = strassen(a,b);
       long end = System.currentTimeMillis();
       String out = "Time taken by strassen= "+(end-start);
       print(arr);
       start = System.currentTimeMillis();
       multiply(a,b);
       end = System.currentTimeMillis();
       out +=("\nTime taken by normal= "+(end-start));
       System.out.println(out);
       
    }

}