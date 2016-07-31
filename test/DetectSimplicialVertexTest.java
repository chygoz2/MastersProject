package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import detectsubgraphs.DetectK4;
import detectsubgraphs.DetectSimplicialVertex;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectSimplicialVertexTest {
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices, highDegreeVertices;
	
	@Before
	public void before(){
		String fileName = "test\\testdata\\simplicialtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		lowDegreeVertices = Utility.partitionVertices(graph)[0];
		highDegreeVertices = Utility.partitionVertices(graph)[1];
	}
	
	@Test
	public void testDetect() {
		List<Graph.Vertex<Integer>> actualResult = DetectSimplicialVertex.detect(graph);
		int[] expectedResult = {4,0,2}; //expected result should have 3 simplicial vertices 
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int i=0; i<expectedResult.length;i++){	
			assertTrue(vList.contains(expectedResult[i]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

	@Test
	public void testPhaseOne() {
		List<Graph.Vertex<Integer>> actualResult = DetectSimplicialVertex.phaseOne(graph, lowDegreeVertices);
		int[] expectedResult = {4}; //expected result should have 1 simplicial vertex
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int i=0; i<expectedResult.length;i++){	
			assertTrue(vList.contains(expectedResult[i]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

	@Test
	public void testPhaseTwo() {
		List<Graph.Vertex<Integer>> actualResult = DetectSimplicialVertex.phaseTwo(graph, lowDegreeVertices, highDegreeVertices);
		int[] expectedResult = {0,2}; //expected result should have 2 simplicial vertices 
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int i=0; i<expectedResult.length;i++){	
			assertTrue(vList.contains(expectedResult[i]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
