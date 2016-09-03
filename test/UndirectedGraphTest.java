package test;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class UndirectedGraphTest {
	
	UndirectedGraph<Integer,Integer> graph;
	
	@Before
	public void initialize(){
		try {
			graph = Utility.makeGraphFromFile("test\\testdata\\graphtestdata.txt");
		} catch (GraphFileReaderException e) {
			System.out.println(e.getError());
		}
	}

	@Test
	public void testSize() {
		assertEquals(4,graph.size());
		assertNotEquals(3,graph.size());
	}
	
	@Test
	public void testEdgeCount(){		
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		assertEquals(4,graph.getEdgeCount());
		graph.addEdge(vertices.get(1), vertices.get(3));
		assertEquals(5,graph.getEdgeCount());
		graph.removeVertex(vertices.get(1));
		assertEquals(2,graph.getEdgeCount());
	}

	@Test
	public void testDegree() {
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		assertTrue(2==graph.degree(vertices.get(1)));
		assertTrue(3==graph.degree(vertices.get(2)));
		assertFalse(5==graph.degree(vertices.get(3)));
	}

	@Test
	public void testContainsEdge() {
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
	
		assertTrue(graph.containsEdge(vertices.get(0), vertices.get(1)));
		assertFalse(graph.containsEdge(vertices.get(0), vertices.get(3)));
	}

	@Test
	public void testClear() {	
		assertFalse(0==graph.size());
		graph.clear();
		assertTrue(0==graph.size());
	}

	@Test
	public void testAddVertex() {		
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("0, 1, 2, 3, ", out);
		assertNotEquals("0, 1, 2, 3, 4, ", out);
		graph.addVertex(4);
		assertNotEquals("0, 1, 2, 3, 4, ", out);
		assertEquals(5,graph.size());
	
	}

	@Test
	public void testAddEdge() {		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("2, 3, 1, 2, 0, 2, 0, 1, ", out);
		
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		graph.addEdge(vertices.get(0), vertices.get(3));
		
		it = graph.edges();
		
		out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("0, 3, 2, 3, 1, 2, 0, 2, 0, 1, ", out);
		
	}

	@Test
	public void testRemoveVertex() {
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("2, 3, 1, 2, 0, 2, 0, 1, ", out);
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		graph.removeVertex(vertices.get(3));
		assertEquals(3,graph.size());
		Iterator<Graph.Vertex<Integer>> it2 = graph.vertices();
		
		String out2 = "";
		while (it2.hasNext()){
			Graph.Vertex<Integer> v = it2.next();
			out2 += v.getElement()+", ";
		}
		
		assertEquals("0, 1, 2, ", out2);
		
	}

	@Test
	public void testRemoveEdge() {
		List<Graph.Edge<Integer>> edges = new ArrayList<Graph.Edge<Integer>>();
		Iterator<Graph.Edge<Integer>> eIt = graph.edges();
		while(eIt.hasNext())
			edges.add(eIt.next());
		
		graph.removeEdge(edges.get(2));
		graph.removeEdge(edges.get(3));
		
		Iterator<Graph.Edge<Integer>> it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("2, 3, 1, 2, ", out);
	}

	@Test
	public void testVertices() {		
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("0, 1, 2, 3, ", out);
		assertEquals(4,graph.size());
	}

	@Test
	public void testEdges() {		
		Iterator<Graph.Edge<Integer>> it = graph.edges();		
		it = graph.edges();
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge)it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("2, 3, 1, 2, 0, 2, 0, 1, ", out);
		
	}

	@Test
	public void testNeighbours() {	
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		Iterator<Graph.Vertex<Integer>> it = graph.neighbours(vertices.get(2));
		
		String out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("0, 1, 3, ", out);
		
		it = graph.neighbours(vertices.get(1));
		
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("0, 2, ", out);
		
		
		graph.removeVertex(vertices.get(2));
		it = graph.vertices();
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("0, 1, 3, ", out);
		
		it = graph.neighbours(vertices.get(0));
		
		out = "";
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			out += v.getElement()+", ";
		}
		
		assertEquals("1, ", out);	
	}

	@Test
	public void testConnectingEdges() {
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		Iterator<Graph.Edge<Integer>> it = graph.connectingEdges(vertices.get(1));
		
		String out = "";
		while (it.hasNext()){
			UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge) it.next();
			out += e.getSource().getElement()+", "+e.getDestination().getElement()+", ";
		}
		
		assertEquals("1, 2, 0, 1, ", out);
	}
	
	@Test
	public void testDepthFirstTraversal(){		
		Graph.Vertex<Integer> v5 = graph.addVertex(4);
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		graph.addEdge(vertices.get(0), vertices.get(4));
		List<Graph.Vertex<Integer>> list = graph.depthFirstTraversal(vertices.get(1));		
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
		
		assertEquals("1, 2, 3, 0, 4, ", out2);
		assertEquals("0, 1, 2, 3, 4, ", out);
	}
	
	@Test
	public void testBreadthFirstTraversal(){
		Graph.Vertex<Integer> v5 = graph.addVertex(4);
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		graph.addEdge(vertices.get(0), vertices.get(4));
		List<Graph.Vertex<Integer>> list = graph.breadthFirstTraversal(vertices.get(1)); 
		
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
		
		assertEquals("1, 0, 2, 4, 3, ", out2);
	}

}
