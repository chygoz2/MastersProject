package gui;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import general.Utility;
import generate.PatternFreeGraphGenerator;

public class GenerateWorker extends SwingWorker<String,String>{

	private String type;
	private int n;
	private int l;
	private JEditorPane outputArea;
	private JButton generateButton;
	
	public GenerateWorker(String type, int n, int l, JEditorPane a, JButton b){
		this.type = type;
		this.n = n;
		this.l = l;
		this.outputArea = a;
		this.generateButton = b;
	}
	
	@Override
	protected String doInBackground() {
		publish("Generating...\n");
		String out = "";
		PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
		if(type.equals("Diamond-free")){
			String filename = g.generateDiamondFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("Claw-free")){
			String filename = g.generateClawFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("K4-free")){
			String filename = g.generateK4FreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("Triangle-free")){
			String filename = g.generateTriangleFreeGraph(n);
			out = String.format("Name of graph file: %s%n", filename);
		}else if(type.equals("KL-free")){
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

	protected void process(List<String> data){
		String s = data.get(data.size()-1);
		outputArea.setText(outputArea.getText()+s);
	}
	
	@Override
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
