package generate;

import java.util.Random;

import general.Utility;

public class RandomGraphGenerator {
	
	public static void main(String[] args){
		int start=0, end=0, no=0, density=0;
		if(args.length>0){
			try{
				start = Integer.parseInt(args[0]);
				end = Integer.parseInt(args[1]);
				density = Integer.parseInt(args[2]);
				no = Integer.parseInt(args[3]); 
				generate(start, end, density, no);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Arguments should be in the form [min_graph_size] [max_graph_size] "
						+ "[density_steps] [number_per_density]");
			}
		}
	}
	
	/**
	 * method to generate random graphs
	 * @param vmax		the maximum number of vertices 
	 * @param no		the number of graphs for each graph size
	 */
	public static void generate(int vmin, int vmax, int density,int no){
		double d = 1/(double)density;
		d = Math.round(d*10)/10.0;
		for(int v=vmin; v<=vmax; v++){
			for(double p=d; p<=1.0; p+=d){
				if(p<=0.9){
					generateRandomGraphFile(v, p, no);
				}
				else{
					generateRandomGraphFile(v, p, 1);
				}
			}
		}
	}
	
	public static void generateRandomGraphFile(int v, double p, int no){
		for(int i=1; i<=no; i++){
			System.out.printf("Generating graph_%d_%.1f_%d%n",v,p,no);
			int[][] A = makeRandomGraph(v,p);
			Utility.saveGraphToFile(A,p,i);
		}
	}

	public static int[][] makeRandomGraph(int v, double p){
		if(p<0.0 || p>1.0){
			System.out.println("Edge probability should be between 0 and 1");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[v][v];
		
		//add the edges with given edge probability
		for(int i=0; i<v; i++){
			for(int j=i+1; j<v; j++){
				double rand = Math.random();
				if(rand<p){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = adjMatrix[i][j];
				}
			}
		}
		
		return adjMatrix;
	}
}
