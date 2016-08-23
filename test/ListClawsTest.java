/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import efficientlisting.ListClaws;
import general.*;
import general.Graph.Vertex;

/**
 * @author Chigozie Ekwonu
 *
 */
public class ListClawsTest {
	UndirectedGraph<Integer,Integer> graph;

	@Before
	public void before(){
		String fileName = "test\\testdata\\clawtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
	}
	
	/**
	 * Test method for {@link efficientdetection.DetectTriangle#detect(general.UndirectedGraph)}.
	 */
	@Test
	public void testDetect() {
		List<List<Vertex<Integer>>> actualResult = new ListClaws().detect(graph);
		int[][] expectedResult = {{0,1,2,3},{2,3,4,6},{2,3,4,5}}; //expected result should have 3 claws 
															//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			List<Integer> vList = new ArrayList<Integer>();
			Collection<Graph.Vertex<Integer>> aRIt = actualResult.get(i);
			for(Graph.Vertex<Integer> v: aRIt)
				vList.add(v.getElement());
			for(int j=0; j<expectedResult[i].length; j++){
				assertTrue(vList.contains(expectedResult[i][j]));
			}
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
