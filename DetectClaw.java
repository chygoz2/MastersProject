import java.util.*;

public class DetectClaw {
	public static void main(String [] args){
		UndirectedGraph graph = new UndirectedGraph();
		Vertex v1 = graph.addVertex(1);
		Vertex v2 = graph.addVertex(2);
		Vertex v3 = graph.addVertex(3);
		Vertex v4 = graph.addVertex(4);
		
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v4, v2);
		
		Map phase1Result = phaseOne(graph);
		if((boolean)phase1Result.get("clawFound")){
			UndirectedGraph claw = (UndirectedGraph)phase1Result.get("claw");
			DetectDiamond.printGraph(claw);
		}else{
			//Map phase2Result = phaseTwo(graph);
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
				UndirectedGraph vNeighGraph = DetectDiamond.getNeighbourGraph(graph, v);
				
				//get components of v's neighbourhood graph
				List<UndirectedGraph> vNeighComps = DetectDiamond.getComponents(vNeighGraph);
				if(vNeighComps.size() >= 3){
					//if the number of components is less than 3, then no claw exists in the neighbourhood of v
					Iterable<Vertex> c1Vertices = (Iterable<Vertex>)vNeighComps.get(0).vertices();
					Iterable<Vertex> c2Vertices = (Iterable<Vertex>)vNeighComps.get(1).vertices();
					Iterable<Vertex> c3Vertices = (Iterable<Vertex>)vNeighComps.get(2).vertices();
					
					List<Vertex> clawVertices = new ArrayList<Vertex>();
					Vertex v1 = graph.getVertexWithId(((UndirectedGraph.UnVertex)v).getId());
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
					
					UndirectedGraph clawGraph = DetectDiamond.makeGraphFromVertexSet(graph, clawVertices);
					phase1Results.put("claw", clawGraph);
				}
			}
		}
		phase1Results.put("clawFound", clawFound);
		return phase1Results;
	}
	
	public static void phaseTwo(){
		
	}
}
