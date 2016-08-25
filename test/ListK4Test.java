package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import efficient.detection.DetectK4;
import efficient.listing.ListK4;
import exception.GraphFileReaderException;
import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class ListK4Test {
	
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices, highDegreeVertices;

	@Before
	public void before() throws GraphFileReaderException{
		String fileName = "test\\testdata\\k4testdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		lowDegreeVertices = new ListK4().partitionVertices(graph)[0];
		highDegreeVertices = new ListK4().partitionVertices(graph)[1];
	}
	
	@Test
	public void testDetect() {
		List<List<Vertex<Integer>>> actualResult = new ListK4().detect(graph);
		int[][] expectedResult = {{0,1,2,3},{1,2,3,4},{0,2,3,5}}; //expected result should have 2 k4's 
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

	@Test
	public void testPhaseOne() {
		List<List<Vertex<Integer>>> actualResult = new ListK4().phaseOne(graph, highDegreeVertices);
		int[][] expectedResult = {{0,1,2,3}}; //expected result should have 1 k4 
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

	@Test
	public void testPhaseTwo() {
		List<List<Vertex<Integer>>> actualResult = new ListK4().phaseTwo(graph, lowDegreeVertices);
		int[][] expectedResult = {{1,2,3,4},{0,2,3,5}}; //expected result should have 2 k4's 
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
