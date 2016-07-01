import static org.junit.Assert.*;

import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class AdjacencyListGraphTest {
	AdjacencyListGraph graph;
	
	public void reset(){
		graph = new AdjacencyListGraph(10);
		
		graph.addVertex(1);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(2);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(3, 1);
	}

	@Test
	public void testOrder() {
		//fail("Not yet implemented");
		reset();
		
		assertEquals("Graph order",4,graph.order());
		assertEquals("Graph size",4,graph.size());
		assertFalse("Graph order 2", 3==graph.order());
	}

	@Test
	public void testDegree() {
		//fail("Not yet implemented");
		reset();
		
		assertTrue("Degree testing for v2", 2==graph.degree(2));
		assertTrue("Degree testing for v3", 3==graph.degree(3));
	}

	@Test
	public void testContainsEdge() {
		//fail("Not yet implemented");
	
		reset();		
	
		assertTrue("Testing edge presence 1", graph.containsEdge(1, 2));
		assertFalse("Testing edge presence 2", graph.containsEdge(1, 4));
	}

	@Test
	public void testClear() {
		//fail("Not yet implemented");
		
		reset();
		
		assertFalse(0==graph.order());
		graph.clear();
		assertTrue(0==graph.order());
		assertTrue(0==graph.size());
	}

	@Test
	public void testAddVertex() {
		//fail("Not yet implemented");
		reset();
		
		Iterator<Integer> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Integer v = it.next();
			out += v+", ";
		}
		
		assertEquals("1, 2, 3, 4, ", out);
		assertEquals(4,graph.order());
	
	}

	@Test
	public void testAddEdge() {
		//fail("Not yet implemented");
		reset();
		
		graph.addEdge(1, 4);
		assertTrue(graph.containsEdge(4, 1));
		
	}

	@Test
	public void testRemoveVertex() {
		//fail("Not yet implemented");
		reset();
		
		assertTrue(4==graph.order());
		assertTrue(4==graph.size());
		
		graph.removeVertex(3);
		
		assertEquals(3, graph.order());
		assertEquals(1, graph.size());
		
	}

	@Test
	public void testRemoveEdge() {
		//fail("Not yet implemented");
		reset();
		graph.removeEdge(3, 4);
		assertEquals(3,graph.size());
		assertFalse(graph.containsEdge(4,3));
	}

	@Test
	public void testNeighbours() {
		//fail("Not yet implemented");
		reset();
		
		Iterator<Integer> it = graph.neighbours(3);
		
		String out = "";
		while (it.hasNext()){
			int v = it.next();
			out += v+", ";
		}
		
		assertEquals("1, 2, 4, ", out);
	
	}
	
	@Test
	public void testDepthFirstTraversal(){
		reset();
		graph.addVertex(5);
		graph.addEdge(1, 5);
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(2); list.add(3); list.add(4); list.add(1); list.add(5); 
		
		Iterator<Integer> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Integer v = it.next();
			out += v+", ";
		}
		
		assertTrue(list.equals(graph.depthFirstTraversal(2)));
		assertEquals("1, 2, 3, 4, 5, ", out);
	}
	
	@Test
	public void testBreadthFirstTraversal(){
		reset();
		graph.addVertex(5);
		graph.addEdge(1, 5);
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(2); list.add(1); list.add(3); list.add(5); list.add(4); 
		
		Iterator<Integer> it = graph.vertices();
		
		String out = "";
		while (it.hasNext()){
			Integer v = it.next();
			out += v+", ";
		}
		
		assertTrue(list.equals(graph.breadthFirstTraversal(2)));
	}

}
