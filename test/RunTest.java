package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTest {

	public static void main(String[] args){
		run();
	}
	
	public static void run(){
		System.out.println("Testing Graph ADT class");
		Result result = JUnitCore.runClasses(UndirectedGraphTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing Claw detection class");
		result = JUnitCore.runClasses(DetectClawTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing diamond detection class");
		result = JUnitCore.runClasses(DetectDiamondTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing K4 detection class");
		result = JUnitCore.runClasses(DetectK4Test.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing Triangle detection class");
		result = JUnitCore.runClasses(DetectTriangleTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing Claw Listing class");
		result = JUnitCore.runClasses(ListClawsTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing diamond Listing class");
		result = JUnitCore.runClasses(ListDiamondsTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing K4 Listing class");
		result = JUnitCore.runClasses(ListK4Test.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing Simplicial vertices Listing class");
		result = JUnitCore.runClasses(ListSimplicialVertexTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
		
		System.out.println("Testing Triangle Listing class");
		result = JUnitCore.runClasses(ListTrianglesTest.class);
		if(result.wasSuccessful()) {
			System.out.println("Test passed");
		}else {
			System.out.println("Test failed");
		}

		for(Failure failure: result.getFailures()) {
			System.out.println(failure);
		}
	}
}
