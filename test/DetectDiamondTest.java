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
	public void testDetect(){
		List<Graph.Vertex<Integer>> d = DetectDiamond.detect(graph);
		
		List<Integer> actualResult = new ArrayList<Integer>();
		for(Graph.Vertex<Integer> v: d){
			actualResult.add(v.getElement());
		}
		
		int[] expectedResult = {0,1,2,4}; //expected result should have one diamond 
															//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			assertTrue(actualResult.contains(expectedResult[i]));
			
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
