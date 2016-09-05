package generate;

import general.Utility;

/**
 * class to generate random graphs given the range of graph sizes, number of graphs to be
 * generated and density steps
 * @author Chigozie Ekwonu
 *
 */
public class RandomGraphGenerator {
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String[] args){
		int start=0, end=0, no=0, densitySteps=0;
		if(args.length>0){
			try{
				start = Integer.parseInt(args[0]); //graph size to start from
				end = Integer.parseInt(args[1]); //graph size to end
				densitySteps = Integer.parseInt(args[2]); //density spread 
				no = Integer.parseInt(args[3]);  //number per density and graph size
				generate(start, end, densitySteps, no);
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
	
	/**
	 * makes multiple graphs for a given graph size and density
	 * @param v		the graph size
	 * @param p		the density or edge probability
	 * @param no	the number of graphs to be generated per graph size and density
	 */
	public static void generateRandomGraphFile(int v, double p, int no){
		for(int i=1; i<=no; i++){
			System.out.printf("Generating graph_%d_%.1f_%d%n",v,p,no);
			int[][] A = makeRandomGraph(v,p);
			Utility.saveGraphToFile(A,p,i);
		}
	}

	/**
	 * method that generates a random graph with a given vertex size and edge probability
	 * @param v		graph size
	 * @param p		edge probability
	 * @return		adjacency matrix of graph
	 */
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
