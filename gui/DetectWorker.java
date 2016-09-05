package gui;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import efficient.detection.DetectClaw;
import efficient.detection.DetectDiamond;
import efficient.detection.DetectK4;
import efficient.detection.DetectKL;
import efficient.detection.DetectSimplicialVertex;
import efficient.detection.DetectTriangle;
import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

/**
 * worker class for detecting presence of subgraph in a graph. It calls on the respective
 * detect classes for the operation
 * @author Chigozie Ekwonu
 *
 */
public class DetectWorker extends SwingWorker<String,String>{

	//instance variables
	private String type; //type of subgraph to be detected
	private String filename; //name of graph file
	private int l;			//size of complete subgraph to be detected. It is only needed for complete subgraph detection
	private JEditorPane outputArea; //output area of GUI
	private JButton detectButton;	//detect button of GUI
	
	/**
	 * constructor to initialize instance variables
	 * @param type			type of subgraph to be detected
	 * @param filename		name of graph file
	 * @param l				size of complete subgraph to be detected. It is only needed for complete subgraph detection
	 * @param a				output area of GUI
	 * @param detectButton	detect button of GUI
	 */
	public DetectWorker(String type, String filename, int l, JEditorPane a,JButton detectButton){
		this.type = type;
		this.filename = filename;
		this.l = l;
		this.outputArea = a;
		this.detectButton = detectButton;
	}
	
	/**
	 * method that performs the detection process
	 */
	protected String doInBackground() {			
		UndirectedGraph<Integer, Integer> graph = null;
		try {
			graph = Utility.makeGraphFromFile(filename); //create graph from file
		} catch (GraphFileReaderException e) { //notify user if error occurs
			JOptionPane.showMessageDialog(null, e.getError());
		}
		
		if(graph==null)
			return null;
		
		publish("Detecting...\n");
		String out = "";
		if(type.equals("Diamond")){ //for diamond detection
			DetectDiamond d = new DetectDiamond();
			List<Graph.Vertex<Integer>> diamond = d.detect(graph);
			if(diamond!=null){
				out = Utility.printList(diamond);
				out = String.format("Diamond found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Claw")){ //for claw detection
			DetectClaw d = new DetectClaw();
			List<Graph.Vertex<Integer>> claw = d.detect(graph);
			if(claw!=null){
				out = Utility.printList(claw);
				out = String.format("Claw found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("K4")){ //for K4 detection
			DetectK4 d = new DetectK4();
			List<Graph.Vertex<Integer>> k4 = d.detect(graph);
			if(k4!=null){
				out = Utility.printList(k4);
				out = String.format("K4 found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Simplicial Vertex")){ //for simplicial vertex detection
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			Graph.Vertex<Integer> simpVertex = d.detect(graph);
			if(simpVertex!=null){
				out = String.format("Simplicial vertex found%nOne such vertex: %s%n", simpVertex.getElement());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				return out;
			}else{
				out = String.format("Simplicial vertex not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Triangle")){ //for triangle detection
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph);
			if(triangle!=null){
				out = Utility.printList(triangle);
				out = String.format("Triangle found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("KL")){ //for general complete subgraph detection
			DetectKL d = new DetectKL();
			List<Graph.Vertex<Integer>> kl = d.detect(graph, l);
			if(kl!=null){
				out = Utility.printList(kl);
				out = String.format("K"+l+" found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}
		return out;
	}
	
	/**
	 * method that writes out data to the GUI output area while worker thread is executing
	 */
	protected void process(List<String> data){
		String s = data.get(data.size()-1);
		outputArea.setText(outputArea.getText()+s);
	}
	
	/**
	 * method that displays the output after the detection operation is complete, interrupted or is cancelled
	 */
	protected void done() {
		String out = null;
		try {
			out = get();
		} catch (ExecutionException e) {
			if (e.getCause() instanceof OutOfMemoryError) {
				out = "System ran out of memory. Please choose a graph with a smaller size";
			}
		}catch(InterruptedException e){
			out = "Subgraph detection interrupted";
		}catch(CancellationException e){
			out = "Subgraph detection cancelled";
		}
		detectButton.setEnabled(true);
		if(out!=null)
			outputArea.setText(outputArea.getText()+out+"\n\n");
	}
}
