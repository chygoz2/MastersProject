import java.util.Iterator;

//import Jama.Matrix;

public class Test {
	public static void main(String [] args){
		double[][] a = {{1,2,3},{4,5,6},{7,8,9}};
		//Matrix mA = new Matrix(a);
		//System.out.println(mA.getRowDimension() + " is the row dimension");
		//System.out.println(mA.getColumnDimension() + " is the column dimension");
		//mA.print(3, 2);
		//Matrix squareMatrix = mA.times(mA);
		//squareMatrix.print(3, 0);
		double[][] result = MatrixOperation.multiply(a, a);
		for(int i=0; i<result.length; i++){
			for(int j=0; j<result[i].length; j++){
				System.out.print((int)result[i][j]+" ");
			}
			System.out.println();
		}
		
		//Testing graph stuff
//		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
//		System.out.println("Graph size is "+graph.size());
//		
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(3);
//		Graph.Vertex<Integer> v3 = graph.addVertex(4);
//		Graph.Vertex<Integer> v4 = graph.addVertex(2);
//		Graph.Vertex<Integer> v5 = graph.addVertex(5);
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v1, v5);
//		System.out.println("Graph size is "+graph.size());
//		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
//		
//		System.out.println("Graph vertices");
//		while (it.hasNext()){
//			Graph.Vertex<Integer> v = it.next();
//			System.out.print(v.getElement()+", ");
//		}
//		System.out.println("\nGraph edges:");
//		Iterator<Graph.Edge<Integer>> it2 = graph.edges();
//		while (it2.hasNext()){
//			UndirectedGraph.UnEdge edge = (UndirectedGraph.UnEdge)it2.next();
//			System.out.print("\n"+edge.getSource().getElement()+", "+ edge.getDestination().getElement());
//		}
//		System.out.println();
//		System.out.println("The degree of vertex v3 whose element is "+v3.getElement()+" is "+graph.degree(v3));
//		
//		System.out.println("v2 neighbours");
//		Iterator<Graph.Vertex<Integer>> it3 = graph.neighbours(v2);
//
//		while(it3.hasNext()){
//			//System.out.println("True");
//			Graph.Vertex<Integer> v = it3.next();
//			System.out.print(v.getElement()+", ");
//		}
//		
//		System.out.println("\nv1 neighbours");
//		it3 = graph.neighbours(v1);
//
//		while(it3.hasNext()){
//			//System.out.println("True");
//			Graph.Vertex<Integer> v = it3.next();
//			System.out.print(v.getElement()+", ");
//		}
//		
//		System.out.println("\nv3 neighbours");
//		it3 = graph.neighbours(v3);
//
//		while(it3.hasNext()){
//			//System.out.println("True");
//			Graph.Vertex<Integer> v = it3.next();
//			System.out.print(v.getElement()+", ");
//		}
//		
//		System.out.println("\nv4 neighbours");
//		it3 = graph.neighbours(v4);
//
//		while(it3.hasNext()){
//			//System.out.println("True");
//			Graph.Vertex<Integer> v = it3.next();
//			System.out.print(v.getElement()+", ");
//		}
//		
//		System.out.println("\nv5 neighbours");
//		it3 = graph.neighbours(v5);
//
//		while(it3.hasNext()){
//			//System.out.println("True");
//			Graph.Vertex<Integer> v = it3.next();
//			System.out.print(v.getElement()+", ");
//		}
//		
//		System.out.println();
//		System.out.println("Printing out adjacency matrix. A: ");
//		double[][] A = graph.getAdjacencyMatrix();
//		for(int i=0; i<A.length; i++){
//			for(int j=0; j<A[i].length; j++){
//				System.out.print((int)A[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		System.out.println("Printing out complement matrix. CompA: ");
//		A = graph.getComplementMatrix();
//		for(int i=0; i<A.length; i++){
//			for(int j=0; j<A[i].length; j++){
//				System.out.print((int)A[i][j]+" ");
//			}
//			System.out.println();
//		}
	}
}
