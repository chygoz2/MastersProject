import java.util.*;

import Jama.Matrix;

public class DetectClaw {
	public static void main(String [] args){
//		UndirectedGraph graph = new UndirectedGraph();
//		Graph.Vertex v1 = graph.addVertex(1);
//		Graph.Vertex v2 = graph.addVertex(2);
//		Graph.Vertex v3 = graph.addVertex(3);
//		Graph.Vertex v4 = graph.addVertex(4);
//		
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v4, v2);
//		
//		graph.mapVertexToId();
		
		
//		UndirectedGraph graph = new UndirectedGraph();
//		Graph.Vertex v1 = graph.addVertex(0);
//		Graph.Vertex v2 = graph.addVertex(1);
//		Graph.Vertex v3 = graph.addVertex(2);
//		Graph.Vertex v4 = graph.addVertex(3);
//		Graph.Vertex v5 = graph.addVertex(4);
//		Graph.Vertex v6 = graph.addVertex(5);
//		Graph.Vertex v7 = graph.addVertex(6);
//		
//		graph.addEdge(v1, v5);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v2, v3);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v4, v5);
//		graph.addEdge(v4, v6);
//		graph.addEdge(v4, v7);
//		graph.addEdge(v6, v7);
//		graph.addEdge(v6, v5);
//		graph.addEdge(v5, v7);
		
		//graph.mapVertexToId();
		
		UndirectedGraph<Integer,Integer> graph = Utility.makeRandomGraph(7, 0.4);
		
		UndirectedGraph<Integer,Integer> claw = detect(graph);
		if(claw!=null){
			Utility.printGraph(claw);
		}else
			System.out.println("Claw not found");
	}
	
	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> claw = null;
		claw = phaseOne(graph);
		if(claw==null){
			claw = phaseTwo(graph);
		}
		return claw;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseOne(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> clawGraph = null;
		int edgeCount = graph.size();
		double maxEdgeCount = 2*Math.sqrt(edgeCount);
		Iterable<Graph.Vertex> vertices = (Iterable<Graph.Vertex>)graph.vertices();
		
		for(Graph.Vertex v: vertices){
			if(graph.degree(v) > maxEdgeCount){
				//look for claw with v as central vertex	
				UndirectedGraph vNeighGraph = Utility.getNeighbourGraph(graph, v);
				
				//get components of v's neighbourhood graph
				List<UndirectedGraph<Integer,Integer>> vNeighComps = Utility.getComponents(vNeighGraph);
				if(vNeighComps.size() >= 3){
					//if the number of components is less than 3, then no claw exists in the neighbourhood of v
					Iterable<Graph.Vertex> c1Vertices = (Iterable<Graph.Vertex>)vNeighComps.get(0).vertices();
					Iterable<Graph.Vertex> c2Vertices = (Iterable<Graph.Vertex>)vNeighComps.get(1).vertices();
					Iterable<Graph.Vertex> c3Vertices = (Iterable<Graph.Vertex>)vNeighComps.get(2).vertices();
					
					List<Graph.Vertex<Integer>> clawVertices = new ArrayList<Graph.Vertex<Integer>>();
					Graph.Vertex v1 = graph.getVertexWithElement((int) v.getElement());
					clawVertices.add(v1);
					
					for(Graph.Vertex vv: c1Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Graph.Vertex vv: c2Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Graph.Vertex vv: c3Vertices){
						clawVertices.add(vv);
						break;
					}
					
					clawGraph = Utility.makeGraphFromVertexSet(graph, clawVertices);
				}
			}
		}
		return clawGraph;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseTwo(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> claw = null;
		List<Graph.Vertex<Integer>> clawVertices = new ArrayList<Graph.Vertex<Integer>>();
		
		Iterable<Graph.Vertex> vertices = (Iterable<Graph.Vertex>)graph.vertices();
		
		//create a mapping between vertices and matrix indices
		List<Graph.Vertex> vertexIndexMap1 = new ArrayList<Graph.Vertex>();
		int a = 0;
		
		for(Graph.Vertex v: (Iterable<Graph.Vertex>)graph.vertices()){
			vertexIndexMap1.add(a, v);
			a++;
		}
		double[][] A = graph.getAdjacencyMatrix();
		
		here:
		for(Graph.Vertex v: vertices){
			//get neighbourhood graph
			UndirectedGraph vNeigh = Utility.getNeighbourGraph(graph, v);
			
			if(vNeigh.size()<3)
				continue;
			
			double[][] complementMatrix = vNeigh.getComplementMatrix();
			Matrix cm = new Matrix(complementMatrix);
			Matrix cmSquared = cm.times(cm);
			
			//create a map between vertices and matrix indices
			Map<Integer, Graph.Vertex> vertexIndexMap2 = new HashMap<Integer, Graph.Vertex>();
			int b = 0;
			
			for(Graph.Vertex vv: (Iterable<Graph.Vertex>)vNeigh.vertices()){
				vertexIndexMap2.put(b,vv);
				b++;
			}
			
			//check for presence of triangle in compliment
			for(int i=0; i<cm.getRowDimension();i++){
				for(int j=i+1; j<cm.getRowDimension();j++){
					//if there is an edge between i and j and there is a path of length 2 between
					//i and j, then there is a triangle in that neighbourhood
					if(cm.get(i,j)>0 && cmSquared.get(i,j)>0){
						//find the third vertex asides i and j to complete the triangle

						//get i's and j's neighbour vertices
						Graph.Vertex iVertex = vertexIndexMap2.get(i);
						Graph.Vertex jVertex = vertexIndexMap2.get(j);
						Graph.Vertex kVertex;
						
//						int iVertexId = (int) ((UndirectedGraph.UnVertex)iVertex).getElement();
//						int jVertexId = (int) ((UndirectedGraph.UnVertex)jVertex).getElement();
//						
//						//get indices of these matrices in the parent graph
//						int ip = vertexIndexMap1.indexOf(graph.getVertexWithElement(iVertexId));
//						int jp = vertexIndexMap1.indexOf(graph.getVertexWithElement(jVertexId));
//						
						//look for the index of the third vertex to complete the claw in the adjacency matrix
						for(int k=0; k<cm.getRowDimension();k++){
							if(k!=i && k!= j && (cm.get(k, i) == 1) && (cm.get(k, j) == 1)){
								//then k is the index of the last vertex of the claw 
								kVertex = vertexIndexMap2.get(k);
								clawVertices.add(iVertex);
								clawVertices.add(jVertex);
								clawVertices.add(v);
								clawVertices.add(kVertex);
//								System.out.println("Vertices are "+iVertex.getElement()+" and "+
//											jVertex.getElement() + " and "+kVertex.getElement());
								claw = Utility.makeGraphFromVertexSet(graph, clawVertices);
								break here;
							}
						}
							
					}
					
				}
			}
		}
		return claw;
	}
}
