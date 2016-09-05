/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import efficient.detection.DetectTriangle;
import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;

/**
 * @author Chigozie Ekwonu
 *
 */
public class DetectTriangleTest {
	UndirectedGraph<Integer,Integer> graph;

	@Before
	public void before() throws GraphFileReaderException{
		String fileName = "test\\testdata\\triangletestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
	}
	
	/**
	 * Test method for {@link efficient.detection.DetectTriangle#detect(general.UndirectedGraph)}.
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

}
