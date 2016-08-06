package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectClaw {
	
	private static String time = "";
	
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
		
		
		UndirectedGraph graph = new UndirectedGraph();
		Graph.Vertex v1 = graph.addVertex(0);
		Graph.Vertex v2 = graph.addVertex(1);
		Graph.Vertex v3 = graph.addVertex(2);
		Graph.Vertex v4 = graph.addVertex(3);
		Graph.Vertex v5 = graph.addVertex(4);
		Graph.Vertex v6 = graph.addVertex(5);
		Graph.Vertex v7 = graph.addVertex(6);
		
		graph.addEdge(v1, v5);
		graph.addEdge(v1, v3);
		graph.addEdge(v2, v3);
		graph.addEdge(v3, v4);
		graph.addEdge(v4, v5);
		graph.addEdge(v4, v6);
		graph.addEdge(v4, v7);
		graph.addEdge(v6, v7);
		graph.addEdge(v6, v5);
		graph.addEdge(v5, v7);
		
		//graph.mapVertexToId();
		
		//UndirectedGraph<Integer,Integer> graph = Utility.makeRandomGraph(7, 0.4);
		
		UndirectedGraph<Integer,Integer> claw = detect(graph);
		if(claw!=null){
			Utility.printGraph(claw);
		}else
			System.out.println("Claw not found");
	}
	
	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> claw = null;
		long starttime = System.currentTimeMillis();
		claw = phaseOne(graph);
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
		
		if(claw==null){
			starttime = System.currentTimeMillis();
			claw = phaseTwo(graph);
			stoptime = System.currentTimeMillis();
			time += "phase2("+(stoptime-starttime)+")_";
		}
		
		if(claw!=null)
			time+="1";
		else
			time+="0";
		System.out.println(time);
		DetectClaw.resetTime();
		return claw;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseOne(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> clawGraph = null;
		int edgeCount = 0;
		Iterator<Graph.Edge<Integer>> edgeIt = graph.edges();
		while(edgeIt.hasNext()){
			edgeIt.next();
			edgeCount++;
		}
		
		double maxEdgeCount = 2*Math.sqrt(edgeCount);
		Iterator<Graph.Vertex<Integer>> vertices = (Iterator<Graph.Vertex<Integer>>)graph.vertices();
		
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v = vertices.next();
			if(graph.degree(v) > maxEdgeCount){
				//look for claw with v as central vertex	
				UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
				
				//get components of v's neighbourhood graph
				List<UndirectedGraph<Integer,Integer>> vNeighComps = Utility.getComponents(vNeighGraph);
				if(vNeighComps.size() >= 3){
					//if the number of components is less than 3, then no claw exists in the neighbourhood of v
					Iterable<Graph.Vertex<Integer>> c1Vertices = (Iterable<Graph.Vertex<Integer>>)vNeighComps.get(0).vertices();
					Iterable<Graph.Vertex<Integer>> c2Vertices = (Iterable<Graph.Vertex<Integer>>)vNeighComps.get(1).vertices();
					Iterable<Graph.Vertex<Integer>> c3Vertices = (Iterable<Graph.Vertex<Integer>>)vNeighComps.get(2).vertices();
					
					List<Graph.Vertex<Integer>> clawVertices = new ArrayList<Graph.Vertex<Integer>>();
					clawVertices.add(v);
					
					for(Graph.Vertex<Integer> vv: c1Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Graph.Vertex<Integer> vv: c2Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Graph.Vertex<Integer> vv: c3Vertices){
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
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		//create a mapping between vertices and matrix indices
		List<Graph.Vertex<Integer>> vertexIndexMap1 = new ArrayList<Graph.Vertex<Integer>>();
		
		Iterator<Graph.Vertex<Integer>> vs = graph.vertices();
		while(vs.hasNext()){
			Graph.Vertex<Integer> v = vs.next();
			vertexIndexMap1.add(v);
		}
		
		//double[][] A = graph.getAdjacencyMatrix();
		
		here:
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeigh = Utility.getNeighbourGraph(graph, v);
			
			if(vNeigh.size()<3)
				continue;
			
			double[][] cm = vNeigh.getComplementMatrix();
			double[][] cmSquared;
			try {
				cmSquared = Utility.multiplyMatrix(cm, cm);
			} catch (MatrixException e) {
				continue;
			}
			
			//create a map between vertices and matrix indices
			List<Graph.Vertex<Integer>> vertexIndexMap2 = new ArrayList<Graph.Vertex<Integer>>();
			
			Iterator<Graph.Vertex<Integer>> vss = vNeigh.vertices();
			while(vss.hasNext()){
				Graph.Vertex<Integer> vv = vss.next();
				vertexIndexMap2.add(vv);
			}
			
			//check for presence of triangle in compliment
			for(int i=0; i<cm.length;i++){
				for(int j=i+1; j<cm.length;j++){
					//if there is an edge between i and j and there is a path of length 2 between
					//i and j, then there is a triangle in that neighbourhood
					if((int)cm[i][j]==1 && (int)cmSquared[i][j]>0){
						//find the third vertex asides i and j to complete the triangle

						//get i's and j's neighbour vertices
						Graph.Vertex<Integer> iVertex = vertexIndexMap2.get(i);
						Graph.Vertex<Integer> jVertex = vertexIndexMap2.get(j);
						Graph.Vertex<Integer> kVertex;
						

						//look for the index of the third vertex to complete the claw in the adjacency matrix
						for(int k=0; k<cm.length;k++){
							if(k!=i && k!= j && ((int)cm[k][i] == 1) && ((int)cm[k][j] == 1)){
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
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
