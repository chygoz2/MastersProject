/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import efficientdetection.*;
import general.*;
import general.Graph.Vertex;

/**
 * @author Chigozie Ekwonu
 *
 */
public class DetectTriangleTest {
	UndirectedGraph<Integer,Integer> graph;
//	List<Graph.Vertex<Integer>> lowDegreeVertices;

	@Before
	public void before(){
		String fileName = "test\\testdata\\triangletestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
//		lowDegreeVertices = Utility.partitionVertices(graph)[0];
	}
	
	/**
	 * Test method for {@link efficientdetection.DetectTriangle#detect(general.UndirectedGraph)}.
	 */
	@Test
	public void testDetect() {
		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) new DetectTriangle().detect(graph);
		int[] expectedResult = {1,2,0}; //expected result should have 1 triangle 
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int j=0; j<expectedResult.length; j++){
			assertTrue(vList.contains(expectedResult[j]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

//	/**
//	 * Test method for {@link efficientdetection.DetectTriangle#phaseOne(general.UndirectedGraph, java.util.List)}.
//	 */
//	@Test
//	public void testPhaseOne() {
//		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) DetectTriangle.detect(graph);
//		int[] expectedResult = {1,2,0}; //expected result should have 1 triangle 
//													//with the specified vertex elements
//		List<Integer> vList = new ArrayList<Integer>();
//		for(Graph.Vertex<Integer> v: actualResult)
//			vList.add(v.getElement());
//		for(int j=0; j<expectedResult.length; j++){
//			assertTrue(vList.contains(expectedResult[j]));
//		}
//		assertEquals(actualResult.size(),expectedResult.length);
//	}
//
//	/**
//	 * Test method for {@link efficientdetection.DetectTriangle#phaseTwo(general.UndirectedGraph, java.util.List)}.
//	 */
//	@Test
//	public void testPhaseTwo() {
//		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) DetectTriangle.phaseTwo(graph,lowDegreeVertices);
//
//		int[] expectedResult = {0,2,4}; //expected result should have 1 triangle 
//													//with the specified vertex elements
//		List<Integer> vList = new ArrayList<Integer>();
//		for(Graph.Vertex<Integer> v: actualResult)
//			vList.add(v.getElement());
//		for(int j=0; j<expectedResult.length; j++){
//			assertTrue(vList.contains(expectedResult[j]));
//		}
//		assertEquals(actualResult.size(),expectedResult.length);
//	}

}
