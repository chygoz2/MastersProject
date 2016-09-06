package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import efficient.detection.DetectDiamond;
import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectDiamondTest {
	
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices;
	DetectDiamond detector;

	@Before
	public void before() throws GraphFileReaderException{
		String fileName = "test"+File.separator+"testdata"+File.separator+"diamondtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		detector = new DetectDiamond();
		lowDegreeVertices = detector.partitionVertices(graph)[0];
	}
	
	@Test
	public void testDetect(){
		List<Graph.Vertex<Integer>> d = detector.detect(graph);
		
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
