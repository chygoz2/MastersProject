package gui;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import efficient.listing.ListClaws;
import efficient.listing.ListDiamonds;
import efficient.listing.ListK4;
import efficient.listing.ListKL;
import efficient.listing.ListSimplicialVertices;
import efficient.listing.ListTriangles;
import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

/**
 * worker class for listing subgraphs in a graph. It calls on the respective
 * listing classes for the operation
 * @author Chigozie Ekwonu
 *
 */
public class ListWorker extends SwingWorker<String,String>{

	//instance variables
	private String type; //type of subgraph to be listed
	private String filename; //name of graph file
	private int l;			//size of complete subgraph to be listed. It is only needed for complete subgraph listing
	private JEditorPane outputArea; //output area of GUI
	private JButton listButton;	//list button of GUI
	
	/**
	 * constructor to initialize instance variables
	 * @param type			type of subgraph to be listed
	 * @param filename		name of graph file
	 * @param l				size of complete subgraph to be listed. It is only needed for complete subgraph listing
	 * @param a				output area of GUI
	 * @param b				detect button of GUI
	 */
	public ListWorker(String type, String filename, int l, JEditorPane a, JButton b){
		this.type = type;
		this.filename = filename;
		this.l = l;
		this.outputArea = a;
		this.listButton = b;
	}
	
	/**
	 * method that performs the listing operation
	 */
	protected String doInBackground() {
		UndirectedGraph<Integer, Integer> graph = null;
		try {
			graph = Utility.makeGraphFromFile(filename); //create graph from file
		} catch (GraphFileReaderException e) {
			JOptionPane.showMessageDialog(null, e.getError());
		}
		if(graph==null)
			return null;
		
		publish("Listing...\n");
		String out = "";
		if(type.equals("Diamond")){ //for diamond listing
			ListDiamonds d = new ListDiamonds();
			List<List<Vertex<Integer>>> diamonds = d.detect(graph);
			if(!diamonds.isEmpty()){
				for(List<Graph.Vertex<Integer>> diamond: diamonds){
					out += Utility.printList(diamond)+"\n";
				}
				out = String.format("Diamond found%nVertices:%n%s", out);
				out += String.format("Number of diamonds found: %d%n", diamonds.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Claw")){ //for claw listing
			ListClaws d = new ListClaws();
			List<List<Vertex<Integer>>> claws = d.detect(graph);
			if(!claws.isEmpty()){
				for(List<Graph.Vertex<Integer>> claw: claws){
					out += Utility.printList(claw)+"\n";
				}
				out = String.format("Claw found%nVertices:%n%s", out);
				out += String.format("Number of claws found: %d%n", claws.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("K4")){ //for K4 listing
			ListK4 d = new ListK4();
			List<List<Vertex<Integer>>> k4s = d.detect(graph);
			if(!k4s.isEmpty()){
				for(List<Graph.Vertex<Integer>> k4: k4s){
					out += Utility.printList(k4)+"\n";
				}
				out = String.format("K4 found%nVertices:%n%s", out);
				out += String.format("Number of K4s found: %d%n", k4s.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Simplicial Vertex")){ //for simplicial vertices listing
			ListSimplicialVertices d = new ListSimplicialVertices();
			List<Graph.Vertex<Integer>> simpVertex = d.detect(graph);
			if(!simpVertex.isEmpty()){
				out = Utility.printList(simpVertex);
				out = String.format("Simplicial vertices found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Simplicial vertices not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("Triangle")){ //for triangle listing
			ListTriangles d = new ListTriangles();
			List<List<Vertex<Integer>>> triangles = d.detect(graph);
			if(!triangles.isEmpty()){
				for(List<Graph.Vertex<Integer>> triangle: triangles){
					out += Utility.printList(triangle)+"\n";
				}
				out = String.format("Triangle found%nVertices:%n%s", out);
				out += String.format("Number of triangles found: %d%n", triangles.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}else if(type.equals("KL")){ //for general complete subgraph listing
			ListKL d = new ListKL();
			List<List<Vertex<Integer>>> kls = d.detect(graph, l);
			if(!kls.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> kl: kls){
					out += Utility.printList(kl)+"\n";
				}
				out = String.format("K"+l+" found%nVertices:%n%s", out);
				out += String.format("Number of K"+l+"s found: %d%n", kls.size());
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
	 * method that displays the output after the listing operation is complete, interrupted or is cancelled
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
			out = "Subgraph listing interrupted";
		}catch(CancellationException e){
			out = "Subgraph listing cancelled";
		}
		listButton.setEnabled(true);
		if(out!=null)
			outputArea.setText(outputArea.getText()+out+"\n\n");
	}
}
