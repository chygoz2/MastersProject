package gui;

import java.util.concurrent.ExecutionException;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import generate.PatternFreeGraphGenerator;

public class GenerateWorker extends SwingWorker<String,Void>{

	private String type;
	private int n;
	private int l;
	private JEditorPane outputArea;
	
	public GenerateWorker(String type, int n, int l, JEditorPane a){
		this.type = type;
		this.n = n;
		this.l = l;
		this.outputArea = a;
	}
	
	@Override
	protected String doInBackground() throws Exception {
		String out = "";
		PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
		if(type.equals("Diamond-free")){
			String filename = g.generateDiamondFreeGraph(n);
			out = String.format("Name of graph file: %s", filename);
		}else if(type.equals("Claw-free")){
			String filename = g.generateClawFreeGraph(n);
			out = String.format("Name of graph file: %s", filename);
		}else if(type.equals("K4-free")){
			String filename = g.generateK4FreeGraph(n);
			out = String.format("Name of graph file: %s", filename);
		}else if(type.equals("Triangle-free")){
			String filename = g.generateTriangleFreeGraph(n);
			out = String.format("Name of graph file: %s", filename);
		}else if(type.equals("KL-free")){
			try{
				String filename = g.generateKLFreeGraph(n,l);
				out = String.format("Name of graph file: %s", filename);
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(null, "The size entered is not an integer");
			}
		}
		return out;
	}

	@Override
	protected void done() {
		String out = "";
		try {
			out = get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		outputArea.setText(outputArea.getText()+out+"\n\n");
	}	
}
