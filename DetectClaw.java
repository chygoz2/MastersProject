import java.util.*;

import Jama.Matrix;

public class DetectClaw {
	public static void main(String [] args){
//		UndirectedGraph graph = new UndirectedGraph();
//		Vertex v1 = graph.addVertex(1);
//		Vertex v2 = graph.addVertex(2);
//		Vertex v3 = graph.addVertex(3);
//		Vertex v4 = graph.addVertex(4);
//		
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v4, v2);
//		
//		graph.mapVertexToId();
		
		
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = graph.addVertex(0);
		Vertex v2 = graph.addVertex(1);
		Vertex v3 = graph.addVertex(2);
		Vertex v4 = graph.addVertex(3);
		Vertex v5 = graph.addVertex(4);
		Vertex v6 = graph.addVertex(5);
		Vertex v7 = graph.addVertex(6);
		
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
		
		Map phase1Result = phaseOne(graph);
		if((boolean)phase1Result.get("clawFound")){
			UndirectedGraph claw = (UndirectedGraph)phase1Result.get("claw");
			Utility.printGraph(claw);
		}else{
			Map phase2Result = phaseTwo(graph);
			if((boolean)phase2Result.get("clawFound")){
				UndirectedGraph claw = (UndirectedGraph)phase2Result.get("claw");
				Utility.printGraph(claw);
			}else{
				System.out.println("Claw not found");
			}
		}
	}
	
	public static Map phaseOne(UndirectedGraph graph){
		Map phase1Results = new HashMap();
		boolean clawFound = false;
		
		int edgeCount = graph.size();
		double maxEdgeCount = 2*Math.sqrt(edgeCount);
		Iterable<Vertex> vertices = (Iterable<Vertex>)graph.vertices();
		
		for(Vertex v: vertices){
			if(graph.degree(v) > maxEdgeCount){
				//look for claw with v as central vertex	
				UndirectedGraph vNeighGraph = Utility.getNeighbourGraph(graph, v);
				
				//get components of v's neighbourhood graph
				List<UndirectedGraph> vNeighComps = Utility.getComponents(vNeighGraph);
				if(vNeighComps.size() >= 3){
					//if the number of components is less than 3, then no claw exists in the neighbourhood of v
					Iterable<Vertex> c1Vertices = (Iterable<Vertex>)vNeighComps.get(0).vertices();
					Iterable<Vertex> c2Vertices = (Iterable<Vertex>)vNeighComps.get(1).vertices();
					Iterable<Vertex> c3Vertices = (Iterable<Vertex>)vNeighComps.get(2).vertices();
					
					List<Vertex> clawVertices = new ArrayList<Vertex>();
					Vertex v1 = graph.getVertexWithElement((int) v.getElement());
					clawVertices.add(v1);
					
					for(Vertex vv: c1Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Vertex vv: c2Vertices){
						clawVertices.add(vv);
						break;
					}
					for(Vertex vv: c3Vertices){
						clawVertices.add(vv);
						break;
					}
					
					clawFound = true;
					
					UndirectedGraph clawGraph = Utility.makeGraphFromVertexSet(graph, clawVertices);
					phase1Results.put("claw", clawGraph);
				}
			}
		}
		phase1Results.put("clawFound", clawFound);
		return phase1Results;
	}
	
	public static Map phaseTwo(UndirectedGraph graph){
		Map phase2Results = new HashMap();
		List<Vertex> clawVertices = new ArrayList<Vertex>();
		
		Iterable<Vertex> vertices = (Iterable<Vertex>)graph.vertices();
		
		//create a mapping between vertices and matrix indices
		List<Vertex> vertexIndexMap1 = new ArrayList<Vertex>();
		int a = 0;
		
		for(Vertex v: (Iterable<Vertex>)graph.vertices()){
			vertexIndexMap1.add(a, v);
			a++;
		}
		double[][] A = graph.getAdjacencyMatrix();
		
		here:
		for(Vertex v: vertices){
			//get neighbourhood graph
			UndirectedGraph vNeigh = Utility.getNeighbourGraph(graph, v);
			
			if(vNeigh.size()<3)
				continue;
			
			double[][] complementMatrix = vNeigh.getComplementMatrix();
			Matrix cm = new Matrix(complementMatrix);
			Matrix cmSquared = cm.times(cm);
			
			//create a map between vertices and matrix indices
			Map<Integer, Vertex> vertexIndexMap2 = new HashMap<Integer, Vertex>();
			int b = 0;
			
			for(Vertex vv: (Iterable<Vertex>)vNeigh.vertices()){
				vertexIndexMap2.put(b,vv);
				b++;
			}
			
			//check for presence of triangle in compliment
			for(int i=0; i<cm.getRowDimension();i++){
				for(int j=0; j<cm.getRowDimension();j++){
					//if there is an edge between i and j and there is a path of length 2 between
					//i and j, then there is a triangle in that neighbourhood
					if(i!=j && cm.get(i,j)>0 && cmSquared.get(i,j)>0){
						//find the third vertex asides i and j to complete the triangle

						//get i's and j's neighbour vertices
						Vertex iVertex = vertexIndexMap2.get(i);
						Vertex jVertex = vertexIndexMap2.get(j);
						Vertex kVertex;
						
						int iVertexId = (int) ((UndirectedGraph.UnVertex)iVertex).getElement();
						int jVertexId = (int) ((UndirectedGraph.UnVertex)jVertex).getElement();
						
						//get indices of these matrices in the parent graph
						int ip = vertexIndexMap1.indexOf(graph.getVertexWithElement(iVertexId));
						int jp = vertexIndexMap1.indexOf(graph.getVertexWithElement(jVertexId));
						
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
								phase2Results.put("claw", Utility.makeGraphFromVertexSet(graph, clawVertices));
								break here;
							}
						}
							
					}
					
				}
			}
		}
		phase2Results.put("clawFound", !clawVertices.isEmpty());
		return phase2Results;
	}
}
