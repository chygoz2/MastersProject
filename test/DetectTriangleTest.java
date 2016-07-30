/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import detectsubgraphs.*;
import general.*;

/**
 * @author Chigozie Ekwonu
 *
 */
public class DetectTriangleTest {
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices;

	@Before
	public void before(){
		String fileName = "test\\testdata\\triangletestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		lowDegreeVertices = Utility.partitionVertices(graph)[0];
	}
	
	/**
	 * Test method for {@link detectsubgraphs.DetectTriangle#detect(general.UndirectedGraph)}.
	 */
	@Test
	public void testDetect() {
		List<UndirectedGraph<Integer,Integer>> actualResult = DetectTriangle.detect(graph);
		int[][] expectedResult = {{0,1,2},{2,3,4},{0,2,4}}; //expected result should have 3 triangles 
															//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			List<Integer> vList = new ArrayList<Integer>();
			Iterator<Graph.Vertex<Integer>> aRIt = actualResult.get(i).vertices();
			while(aRIt.hasNext())
				vList.add(aRIt.next().getElement());
			for(int j=0; j<expectedResult[i].length; j++){
				assertTrue(vList.contains(expectedResult[i][j]));
			}
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

	/**
	 * Test method for {@link detectsubgraphs.DetectTriangle#phaseOne(general.UndirectedGraph, java.util.List)}.
	 */
	@Test
	public void testPhaseOne() {
		List<UndirectedGraph<Integer,Integer>> actualResult = DetectTriangle.phaseOne(graph, lowDegreeVertices);
		int[][] expectedResult = {{0,1,2},{2,3,4}}; //expected result should have 2 triangles 
													//with the specified vertex elements
	
		for(int i=0; i<expectedResult.length;i++){
			List<Integer> vList = new ArrayList<Integer>();
			Iterator<Graph.Vertex<Integer>> aRIt = actualResult.get(i).vertices();
			while(aRIt.hasNext())
				vList.add(aRIt.next().getElement());
			for(int j=0; j<expectedResult[i].length; j++){
				assertTrue(vList.contains(expectedResult[i][j]));
			}
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

	/**
	 * Test method for {@link detectsubgraphs.DetectTriangle#phaseTwo(general.UndirectedGraph, java.util.List)}.
	 */
	@Test
	public void testPhaseTwo() {
		List<UndirectedGraph<Integer,Integer>> actualResult = DetectTriangle.phaseTwo(graph, lowDegreeVertices);
		int[][] expectedResult = {{0,2,4}};//expected result should have 1 triangle 
											//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			List<Integer> vList = new ArrayList<Integer>();
			Iterator<Graph.Vertex<Integer>> aRIt = actualResult.get(i).vertices();
			while(aRIt.hasNext())
				vList.add(aRIt.next().getElement());
			for(int j=0; j<expectedResult[i].length; j++){
				assertTrue(vList.contains(expectedResult[i][j]));
			}
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
