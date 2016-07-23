import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class UndirectedGraphTest2 {

	@Test
	public void testOrder() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		
		assertEquals("Graph order",4,graph.size());
//		assertEquals("Graph size",3,graph.size());
		assertFalse("Graph order 2", 3==graph.size());
	}

	@Test
	public void testDegree() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		assertTrue("Degree testing for v2", 2==graph.degree(v2));
		assertTrue("Degree testing for v3", 3==graph.degree(v3));
	}

	@Test
	public void testContainsEdge() {
		//fail("Not yet implemented");
	
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
	
		assertTrue("Testing edge presence 1", graph.containsEdge(v1, v2));
		assertFalse("Testing edge presence 2", graph.containsEdge(v1, v4));
	}

	@Test
	public void testClear() {
		//fail("Not yet implemented");
		
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		assertFalse(0==graph.size());
		graph.clear();
		assertTrue(0==graph.size());
	}

	@Test
	public void testAddVertex() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("1, 2, 3, 4, ", out);
		assertEquals(4,graph.size());
	
	}

	@Test
	public void testAddEdgeVertexOfEVertexOfE() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("4, 3, 1, 3, ", out);
		
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		it = graph.edges();
		
		out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("1, 4, 4, 2, 4, 3, 1, 3, ", out);
		
	}

	//@Test
	public void testAddEdgeVertexOfEVertexOfEA() {
		//fail("Not yet implemented");
	}

	@Test
	public void testRemoveVertex() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		graph.removeVertex(v4);
		
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("1, 4, 4, 3, 1, 3, ", out);
		assertEquals(3,graph.size());
		Iterator<Graph.Vertex<Integer>> it2 = graph.vertices();
		
		String out2 = "";
		while (it2.hasNext()){
			Graph.Vertex<Integer> v = it2.next();
			out2 += v.getElement()+", ";
		}
		
		assertEquals("1, 3, 4, ", out2);
		assertEquals(3,graph.size());
		
	}

	@Test
	public void testRemoveEdge() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		Graph.Edge<Integer> e4 = graph.addEdge(v1, v3);
		
		graph.removeEdge(e3);
		graph.removeEdge(e4);
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("4, 3, 1, 3, ", out);
	}

	@Test
	public void testVertices() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		
		Set<Integer> resultSet = new HashSet<Integer>();
		Set<Integer> resultSet2 = new HashSet<Integer>();
		resultSet2.add(1); resultSet2.add(3); resultSet2.add(4); resultSet2.add(2);    
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			resultSet.add(v.getElement());
		}
		
		
		assertTrue((resultSet.containsAll(resultSet2)));
		assertEquals(4,graph.size());
	}

	@Test
	public void testEdges() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		assertFalse(it.hasNext());
		
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		
		it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge)it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("4, 2, 4, 3, 1, 3, ", out);
		
	}

	@Test
	public void testNeighbours() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		Iterator<Graph.Vertex<Integer>> it = graph.neighbours(v3);
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("3, 2, 1, ", out);
		
		it = graph.neighbours(v2);
		
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("1, 4, ", out);
		
		
		graph.removeVertex(v3);
		it = graph.vertices();
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("1, 2, 3, ", out);
		
		it = graph.neighbours(v1);
		
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("3, ", out);
		
		graph = new UndirectedGraph<Integer,Integer>();
		
		v1 = graph.addVertex(1);
		v2 = graph.addVertex(3);
		v3 = graph.addVertex(4);
		v4 = graph.addVertex(2);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
//		Iterator<Graph.Vertex<Integer>> vit = graph.neighbours(v2);
//		while(vit.hasNext())
//			System.out.print(vit.next().getElement()+", ");
//		System.out.println();
		
	}

	@Test
	public void testConnectingEdges() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(3);
		Graph.Vertex<Integer> v3 = graph.addVertex(4);
		Graph.Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		Iterator<Graph.Edge<Integer>> it = graph.connectingEdges(v2);
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("4, 3, 1, 3, ", out);
	}
	
	@Test
	public void testDepthFirstTraversal(){
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(2);
		Graph.Vertex<Integer> v3 = graph.addVertex(3);
		Graph.Vertex<Integer> v4 = graph.addVertex(4);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
		List<Graph.Vertex<Integer>> list = graph.depthFirstTraversal(v2);
//		list.add(v2); list.add(v1); list.add(v5); list.add(v3); list.add(v4); 
		
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		String out2 = "";
		for (Graph.Vertex<Integer> v: list){
			out2 += v.getElement()+", ";
		}
		
		assertEquals("2, 3, 4, 1, 5, ", out2);
		assertEquals("1, 2, 3, 4, 5, ", out);
	}
	
	@Test
	public void testBreadthFirstTraversal(){
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(2);
		Graph.Vertex<Integer> v3 = graph.addVertex(3);
		Graph.Vertex<Integer> v4 = graph.addVertex(4);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Graph.Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
		List<Graph.Vertex<Integer>> list = graph.breadthFirstTraversal(v2);
//		list.add(v2); list.add(v3); list.add(v1); list.add(v4); list.add(v5); 
		
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		String out2 = "";
		for (Graph.Vertex<Integer> v: list){
			out2 += v.getElement()+", ";
		}
		
		assertEquals("2, 1, 3, 5, 4, ", out2);
		assertEquals("1, 2, 3, 4, 5, ", out);
	}

}
