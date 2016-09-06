import java.awt.EventQueue;
import java.util.Scanner;

import exception.GraphFileReaderException;
import general.*;
import gui.GUI;
import test.RunTest;

/**
 * class which provides a command line interface to the tool. 
 * @author Chigozie Ekwonu
 *
 */
public class RunMe {

	private static Thread thread; //thread object which handles the detection, listing or generation operation

	public static void main(String[] args){
		//brief instructions printed out to the user on how to use the tool
		String operationvalues = "Possible values for [operation_class]: help, detect, list, generate, quit, gui, test, cancel";
		String detectionValues = "Possible values for [operation_class_type]: triangle, claw, k4, kl, simplicial, diamond. "
				+ "Note that if 'generate' is entered as [operation_class], the simplicial keyword is not available as an option for [operation_class_type]";
		String optionValues = "[option] could be the graph file relative location (for detect and list operations) "
				+ "or graph size (for generate operation)";
		String subgraphSize = "[subgraph_size] should be the size of complete subgraph to be detected, listed or generated. "
				+ "It should only be provided if [operation_class_type] is kl";

		String info = "Please enter commands in the form: "
				+ "[operation_class] [operation_class_type] [option] [subgraph_size]";
		String errorMessage = "You entered fewer arguments than required.\n"
				+ "Arguments should be in the form\n"
				+ "[operation_class] [operation_class_type] [option]";

		//print out instruction to user
		System.out.println(info);
		System.out.println(operationvalues);
		System.out.println(detectionValues);
		System.out.println(optionValues);
		System.out.println(subgraphSize);

		Scanner sc = new Scanner(System.in); //scanner for receiving user input

		System.out.print("> ");
		String input = sc.nextLine(); //get user input
		input = input.toLowerCase(); //convert input to lower case
		String[] words = input.split("[ ]+"); 
		while(!words[0].equals("quit")){ //while user has not entered the quit command
			if(words[0].equals("detect") || words[0].equals("list")){ //if user want to perform detection or listing
				if(words.length < 3){ //notify user that insufficient arguments were entered
					System.out.println(errorMessage);
				}else{
					String path = words[2];	
					UndirectedGraph<Integer, Integer> graph = null;
					try {
						graph = Utility.makeGraphFromFile(path);
					} catch (GraphFileReaderException e1) { //if an exception occured while reading graph file, notify user
						System.out.println(e1.getError());
					}
					if(graph!=null){
						if(words[1].equals("kl")){ //if user wants to detect or list KL, ensure that the size argument for the kl size is provided
							try{
								String sizeString = words[3];
								if(words[0].equals("detect")){
									thread = new DetectThread(words[1],graph, sizeString); //create thread to handle the detection
								}else{
									thread = new ListThread(words[1],graph, sizeString); //create thread to handle the listing
								}
							}catch(ArrayIndexOutOfBoundsException e){
								System.out.println("Please enter the size of the complete subgraph to be detected "
										+ "as the 4th argument");
							}
						}else{ //otherwise the size argument does not matter
							if(words[0].equals("detect")){
								thread = new DetectThread(words[1],graph, null);
							}else{
								thread = new ListThread(words[1],graph, null);
							}
						}
						if(thread!=null) //if thread is not null, start it
							try{
								thread.start();
							}catch(IllegalThreadStateException e){} //exception is thrown if a previous thread had completed
						//and a check for a subsequent operation failed, which prevented the thread reference to be
						//pointed to a new thread object
					}	
				}
			}

			else if(words[0].equals("generate")){ //if user want to generate graphs
				if(words.length < 3){ //notify user if insufficient arguments are provided
					System.out.println(errorMessage);
				}else{
					if(words[1].equals("kl")){
						try{
							thread = new GenerateThread(words[1],words[2],words[3]); //create thread to generate random graph 
						}catch(ArrayIndexOutOfBoundsException e){ //if user does not enter the 4th argument for kl-free graph generation, inform user
							System.out.println("Please enter the size of the complete subgraph that the generated graph should not contain"
									+ " as the 4th argument");
						}
					}else{
						thread = new GenerateThread(words[1],words[2], null);
					}
					if(thread!=null){
						try{
							thread.start(); //start the thread
						}catch(IllegalThreadStateException e){} //exception is thrown if a previous thread had completed
							//and a check for a subsequent operation failed, which prevented the thread reference to be
							//pointed to a new thread object
					}
				}
			}
			else if(words[0].equals("test")){ //if user wants to run unit test
				RunTest.run();
			}
			else if(words[0].equals("cancel")){ //if user wants to cancel the current running operation
				if(thread!=null && thread.isAlive()){ //if a detection, listing or generation thread is running, 
					thread.stop(); //kill the thread
					thread = null;
					System.out.println("Operation Cancelled"); //notify user that the operation has been cancelled
				}
			}
			else if(words[0].equals("gui")){ //if user wants to call up the GUI
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GUI frame = new GUI(); //create GUI object
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			else if(words[0].equals("help")){ //if user wants to display info on how to use tool
				System.out.println(info);
				System.out.println(operationvalues);
				System.out.println(detectionValues);
				System.out.println(optionValues);
				System.out.println(subgraphSize);
			}
			else{ //user entered invalid keyword as the first argument
				System.out.println("Invalid keyword entered.");
				System.out.println("The first argument should be one of the following: "
						+ "detect, generate, quit, list, test, gui ");
			}
			System.out.print("> ");
			input = sc.nextLine();
			input = input.toLowerCase();
			words = input.split("[ ]+");
		}
		sc.close();
		if(words[0].equals("quit")){ //if user wants to exit the tool
			System.exit(0); //terminate program
		}
	}

}
