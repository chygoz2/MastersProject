import java.util.Iterator;

import Jama.Matrix;

public class Test {
	public static void main(String [] args){
		double[][] a = {{1,2,3},{4,5,6},{7,8,9}};
		Matrix mA = new Matrix(a);
		//System.out.println(mA.getRowDimension() + " is the row dimension");
		//System.out.println(mA.getColumnDimension() + " is the column dimension");
		//mA.print(3, 2);
		Matrix squareMatrix = mA.times(mA);
		//squareMatrix.print(3, 0);
		
		
		//Testing graph stuff
		UndirectedGraph graph = new UndirectedGraph<Integer,Integer>();
		System.out.println("Graph size is "+graph.size());
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		System.out.println("Graph size is "+graph.size());
		Iterator<Vertex> it = graph.vertices();
		
		System.out.println("Graph vertices");
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			System.out.print(v.getElement()+", ");
		}
		System.out.println("\nGraph edges:");
		Iterator<Edge> it2 = graph.edges();
		while (it2.hasNext()){
			UndirectedGraph.UnEdge edge = (UndirectedGraph.UnEdge)it2.next();
			System.out.print("\n"+edge.source.getElement()+", "+ edge.destination.getElement());
		}
		System.out.println();
		System.out.println("The degree of vertex v3 whose element is "+v3.getElement()+" is "+graph.degree(v3));
		
		System.out.println("get v2 neighbours");
		Iterator<Vertex> it3 = graph.neighbours(v2);

		while(it3.hasNext()){
			//System.out.println("True");
			Vertex<Integer> v = it3.next();
			System.out.print(v.getElement()+", ");
		}
	}
}
