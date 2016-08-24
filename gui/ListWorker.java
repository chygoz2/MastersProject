package gui;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JEditorPane;
import javax.swing.SwingWorker;

import efficientlisting.ListClaws;
import efficientlisting.ListDiamonds;
import efficientlisting.ListK4;
import efficientlisting.ListKL;
import efficientlisting.ListSimplicialVertices;
import efficientlisting.ListTriangles;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListWorker extends SwingWorker<String,Void>{

	private String type;
	private String filename;
	private int l;
	private JEditorPane outputArea;
	
	public ListWorker(String type, String filename, int l, JEditorPane a){
		this.type = type;
		this.filename = filename;
		this.l = l;
		this.outputArea = a;
	}
	
	@Override
	protected String doInBackground() throws Exception {
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(filename);
		if(graph==null)
			return null;
		
		String out = "";
		if(type.equals("Diamond")){
			ListDiamonds d = new ListDiamonds();
			List<List<Vertex<Integer>>> diamonds = d.detect(graph);
			if(!diamonds.isEmpty()){
				for(List<Graph.Vertex<Integer>> diamond: diamonds){
					out += printList(diamond)+"\n";
				}
				out = String.format("Diamond found%nVertices:%n%s", out);
				out += String.format("Number of diamonds found: %d%n", diamonds.size());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Claw")){
			ListClaws d = new ListClaws();
			List<List<Vertex<Integer>>> claws = d.detect(graph);
			if(!claws.isEmpty()){
				for(List<Graph.Vertex<Integer>> claw: claws){
					out += printList(claw)+"\n";
				}
				out = String.format("Claw found%nVertices:%n%s", out);
				out += String.format("Number of claws found: %d%n", claws.size());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
				System.out.println(out);
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("K4")){
			ListK4 d = new ListK4();
			List<List<Vertex<Integer>>> k4s = d.detect(graph);
			if(!k4s.isEmpty()){
				for(List<Graph.Vertex<Integer>> k4: k4s){
					out += printList(k4)+"\n";
				}
				out = String.format("K4 found%nVertices:%n%s", out);
				out += String.format("Number of K4s found: %d%n", k4s.size());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Simplicial Vertex")){
			ListSimplicialVertices d = new ListSimplicialVertices();
			List<Graph.Vertex<Integer>> simpVertex = d.detect(graph);
			if(!simpVertex.isEmpty()){
				out = printList(simpVertex);
				out = String.format("Simplicial vertices found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Simplicial vertices not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Triangle")){
			ListTriangles d = new ListTriangles();
			List<List<Vertex<Integer>>> triangles = d.detect(graph);
			if(!triangles.isEmpty()){
				for(List<Graph.Vertex<Integer>> triangle: triangles){
					out += printList(triangle)+"\n";
				}
				out = String.format("Triangle found%nVertices:%n%s", out);
				out += String.format("Number of triangles found: %d%n", triangles.size());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("KL")){
			ListKL d = new ListKL();
			List<List<Vertex<Integer>>> kls = d.detect(graph, l);
			if(!kls.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> kl: kls){
					out += printList(kl)+"\n";
				}
				out = String.format("K"+l+" found%nVertices:%n%s", out);
				out += String.format("Number of K"+l+"s found: %d%n", kls.size());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
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
	
	public String printList(List<Graph.Vertex<Integer>> list){
		String out = "";

		for(Graph.Vertex<Integer> v:list){
			out+=v.getElement()+","; 
		}
		out = out.substring(0,out.length()-1);
		return out;
	}
	
	public int getTotalTime(String s){
		String[] tokens = s.split("[ ]+");
		int time = 0;
		int limit = tokens[tokens.length-2].equals("not") ? tokens.length-2: tokens.length-1;
		for(int i=0; i<limit; i++){
			if(!tokens[i].equals("-"))
				time += Integer.parseInt(tokens[i]);
		}
		return time;
	}
}
