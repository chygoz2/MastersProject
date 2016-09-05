package gui;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import generate.PatternFreeGraphGenerator;

public class GenerateWorker extends SwingWorker<String,String>{

	private String type; //type of subgraph that should be absent
	private int n;		//the size of graph to be generated
	private int l;		//size of complete subgraph to be absent. It is only needed for complete subgraph-free generation
	private JEditorPane outputArea; //output area of GUI
	private JButton generateButton;	//generate button of GUI
	
	/**
	 * constructor to initialize instance variables
	 * @param type			type of subgraph that should be absent
	 * @param n				the size of graph to be generated
	 * @param l				size of complete subgraph to be absent. It is only needed for complete subgraph-free generation
	 * @param a				output area of GUI
	 * @param b				generate button of GUI
	 */
	public GenerateWorker(String type, int n, int l, JEditorPane a, JButton b){
		this.type = type;
		this.n = n;
		this.l = l;
		this.outputArea = a;
		this.generateButton = b;
	}
	
	/**
	 * method that performs the generation process
	 */
	protected String doInBackground() {
		publish("Generating...\n");
		String out = "";
		PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
		if(type.equals("Diamond-free")){ //for diamond-free graph generation
			String filename = g.generateDiamondFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("Claw-free")){ //for claw-free graph generation
			String filename = g.generateClawFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("K4-free")){ //for k4-free graph generation
			String filename = g.generateK4FreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("Triangle-free")){ //for triangle-free graph generation
			String filename = g.generateTriangleFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("KL-free")){ //for other complete induced subgraph sizes
			try{
				String filename = g.generateKLFreeGraph(n,l);
				out = String.format("Name of graph file: %s%n", filename);
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, "The size entered is not an integer");
			}
		}
		out += String.format("CPU time taken: %d milliseconds", g.getTime());
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
	 * method that displays the output after the generation operation is complete, interrupted or is cancelled
	 */
	protected void done() {
		String out = null;
		try {
			out = get();
		} catch (ExecutionException e) {
			if (e.getCause() instanceof OutOfMemoryError) {
				out = "System ran out of memory. Please choose a smaller "
						+ "graph size to be generated";
			}
		}catch(InterruptedException e){
			out = "Graph generation interrupted";
		}catch(CancellationException e){
			out = "Graph generation cancelled";
		}
		generateButton.setEnabled(true);
		if(out!=null)
			outputArea.setText(outputArea.getText()+out+"\n\n");
	}
}
