import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class UndirectedGraphTest {

	@Test
	public void testSize() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		assertEquals("Graph size",4,graph.size());
		assertFalse("Graph size 2", 3==graph.size());
	}

	@Test
	public void testDegree() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		
		Iterator<Vertex> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("2, 4, 3, 1, ", out);
		assertEquals(4,graph.size());
	
	}

	@Test
	public void testAddEdgeVertexOfEVertexOfE() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		
		Iterator<Edge> it = graph.edges();
		
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		graph.removeVertex(v3);
		
		
		Iterator<Edge> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("1, 3, ", out);
	
		Iterator<Vertex> it2 = graph.vertices();
		
		String out2 = "";
		while (it2.hasNext()){
			Vertex<Integer> v = it2.next();
			out2 += v.getElement()+", ";
		}
		
		assertEquals("2, 3, 1, ", out2);
		assertEquals(3,graph.size());
	}

	@Test
	public void testRemoveEdge() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		Edge<Integer> e4 = graph.addEdge(v1, v3);
		
		graph.removeEdge(e3);
		graph.removeEdge(e4);
		
		Iterator<Edge> it = graph.edges();
		
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		
		Iterator<Vertex> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("2, 4, 3, 1, ", out);
		assertEquals(4,graph.size());
	}

	@Test
	public void testEdges() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		
		Iterator<Edge> it = graph.edges();
		assertFalse(it.hasNext());
		
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		Iterator<Vertex> it = graph.neighbours(v3);
		
		String out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("1, 2, 3, ", out);
		
		it = graph.neighbours(v2);
		
		out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("4, 1, ", out);
	}

	@Test
	public void testConnectingEdges() {
		//fail("Not yet implemented");
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		
		Iterator<Edge> it = graph.connectingEdges(v2);
		
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
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
		List<Vertex<Integer>> list = new ArrayList<Vertex<Integer>>();
		list.add(v2); list.add(v1); list.add(v5); list.add(v3); list.add(v4); 
		
		Iterator<Vertex> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertTrue(list.equals(graph.depthFirstTraversal(v2)));
		assertEquals("5, 2, 4, 3, 1, ", out);
	}
	
	@Test
	public void testBreadthFirstTraversal(){
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		Edge<Integer> e3 = graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
		List<Vertex<Integer>> list = new ArrayList<Vertex<Integer>>();
		list.add(v2); list.add(v3); list.add(v1); list.add(v4); list.add(v5); 
		
		Iterator<Vertex> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertTrue(list.equals(graph.breadthFirstTraversal(v2)));
		assertEquals("5, 2, 4, 3, 1, ", out);
	}

}
