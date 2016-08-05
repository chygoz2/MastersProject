package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import efficientdetection.DetectDiamond;
import efficientdetection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectDiamondTest {
	
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices;

	@Before
	public void before(){
		String fileName = "test\\testdata\\diamondtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		lowDegreeVertices = Utility.partitionVertices(graph)[0];
	}
	
	@Test
	public void testPhaseOne() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDetect(){
		UndirectedGraph<Integer,Integer> d = DetectDiamond.detect(graph);
		
		List<UndirectedGraph<Integer,Integer>> actualResult = new ArrayList<UndirectedGraph<Integer,Integer>>();
		actualResult.add(d);
		int[][] expectedResult = {{0,1,2,4}}; //expected result should have one diamond 
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
