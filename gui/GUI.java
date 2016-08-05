package gui;
import java.awt.BorderLayout;
//import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import efficientdetection.DetectClaw;
import efficientdetection.DetectDiamond;
import efficientdetection.DetectK4;
import efficientdetection.DetectSimplicialVertex;
import efficientdetection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class GUI extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JRadioButton diamondRadioButton;		
	private JRadioButton clawRadioButton;
	private JRadioButton k4RadioButton;
	private JRadioButton simpVertexRadioButton;
	private JRadioButton triangleRadioButton;
	private JRadioButton kLRadioButton;
	private JButton detectButton;
	
	private JTextField vertexCountField;
	private JTextField edgeProbabilityField;
	private JTextField graphNoField;
	private JTextField selectFileField;
	
	private JButton selectFileButton;
	private JTextArea outputArea;
	private JButton generateButton;
	
	private ButtonGroup buttonGroup;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Subgraph Identification Tool");
		setBounds(300, 100, 600, 350);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10,10));
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		contentPane.add(panel1, BorderLayout.WEST);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel detectPanel = new JPanel();
		JPanel generatePanel = new JPanel();
		tabbedPane.addTab("Detect", detectPanel);
		tabbedPane.addTab("Generate", generatePanel);
		
		panel1.add(tabbedPane);
		
		detectPanel.setLayout(new GridLayout(9,2,5,5));
		selectFileButton = new JButton("Select File");
		detectPanel.add(selectFileButton);
		selectFileField = new JTextField(5);
		detectPanel.add(selectFileField);
		
		JLabel detectionTypeLabel = new JLabel("Select Detection Type");
		JLabel blankLabel = new JLabel();
		detectPanel.add(detectionTypeLabel);
		detectPanel.add(blankLabel);
		
		diamondRadioButton = new JRadioButton("Diamond");		
		clawRadioButton = new JRadioButton("Claw");
		k4RadioButton = new JRadioButton("K4");
		simpVertexRadioButton = new JRadioButton("Simplicial Vertex");
		triangleRadioButton = new JRadioButton("Triangle");
		triangleRadioButton.setSelected(true);
		kLRadioButton = new JRadioButton("KL");
		detectButton = new JButton("Detect");
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(diamondRadioButton);
		buttonGroup.add(clawRadioButton);
		buttonGroup.add(k4RadioButton);
		buttonGroup.add(simpVertexRadioButton);
		buttonGroup.add(triangleRadioButton);
		buttonGroup.add(kLRadioButton);
		
		JLabel blankLabel2 = new JLabel();
		JLabel blankLabel3 = new JLabel();
		JLabel blankLabel4 = new JLabel();
		JLabel blankLabel5 = new JLabel();
		JLabel blankLabel6 = new JLabel();
		JLabel blankLabel7 = new JLabel();
		JLabel blankLabel8 = new JLabel();
		
		detectPanel.add(triangleRadioButton);	detectPanel.add(blankLabel7);
		detectPanel.add(diamondRadioButton);	detectPanel.add(blankLabel2);
		detectPanel.add(clawRadioButton);		detectPanel.add(blankLabel3);
		detectPanel.add(simpVertexRadioButton);	detectPanel.add(blankLabel5);
		detectPanel.add(k4RadioButton);			detectPanel.add(blankLabel4);
		detectPanel.add(kLRadioButton);			detectPanel.add(blankLabel8);
		detectPanel.add(blankLabel6);			detectPanel.add(detectButton);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout());
		JLabel outputLabel = new JLabel("Output Area");
		panel4.add(outputLabel, BorderLayout.NORTH);
		outputArea = new JTextArea(15,15);
		JScrollPane scrollPane = new JScrollPane(outputArea);
		outputArea.setEditable(false);
		panel4.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panel4, BorderLayout.CENTER);
		
		generatePanel.setLayout(new BorderLayout(5,20));
		JLabel vertexCountLabel = new JLabel("Vertex Count");
		JLabel edgeProbabilityLabel = new JLabel("Edge probability");
		JLabel graphNoLabel = new JLabel("No. of graphs");
		
		JTextField vertexCountField = new JTextField(10);
		JTextField edgeProbabilityField = new JTextField(10);
		JTextField graphNoField = new JTextField(10);
		
		JLabel blankLabel9 = new JLabel();
		generateButton = new JButton("Generate");
		
		JPanel panel5 = new JPanel();
		JPanel panel6 = new JPanel();
		panel6.setLayout(new BorderLayout(5,5));
		panel6.add(new JLabel("Random Graph Generation"), BorderLayout.NORTH);
		panel6.add(panel5, BorderLayout.SOUTH);
		
		panel5.setLayout(new GridLayout(4,2));
		panel5.add(vertexCountLabel); 		panel5.add(vertexCountField);
		panel5.add(edgeProbabilityLabel);	panel5.add(edgeProbabilityField);
		panel5.add(graphNoLabel);			panel5.add(graphNoField);
		panel5.add(blankLabel9);			panel5.add(generateButton);
		generatePanel.add(panel6,BorderLayout.NORTH);
		
		JPanel panel7 = new JPanel();
		panel7.setLayout(new BorderLayout());
		panel7.add(new JLabel("Pattern-less graph generation"));
		
		generatePanel.add(panel7,BorderLayout.CENTER);
		
		detectButton.addActionListener(this);
		generateButton.addActionListener(this);
		selectFileButton.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton source = (JButton)e.getSource();
		if(source.equals(detectButton)){
			String selectedButton = getSelectedButtonText(buttonGroup);
			
			String fileName = selectFileField.getText();
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
			if(graph==null)
				return;
			
			Runnable r = new Runnable(){
				@Override
				public void run() {
					if(selectedButton.equals("Diamond")){
						UndirectedGraph<Integer,Integer> diamond = DetectDiamond.detect(graph);
						if(diamond!=null){
							Iterator<Graph.Vertex<Integer>> vertices = diamond.vertices();
							String out = "";
							
							while(vertices.hasNext()){
								out+=vertices.next().getElement()+","; 
							}
							out = String.format("Diamond found %n%s", out.substring(0,out.length()-1));
							outputArea.setText(out);
						}else{
							outputArea.setText("Diamond not found");
						}
					}else if(selectedButton.equals("Claw")){
						
						UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
						if(claw!=null){
							Iterator<Graph.Vertex<Integer>> vertices = claw.vertices();
							String out = "";
							while(vertices.hasNext()){
								out+=vertices.next().getElement()+","; 
							}
							out = String.format("Claw found %n%s", out.substring(0,out.length()-1));
							outputArea.setText(out);
						}else
							outputArea.setText("Claw not found");
					}else if(selectedButton.equals("K4")){
						List<UndirectedGraph<Integer,Integer>> k4 = DetectK4.detect(graph);
						if(!k4.isEmpty()){
							Iterator<Graph.Vertex<Integer>> vertices = k4.get(0).vertices();
							String out = "";
							while(vertices.hasNext()){
								out+=vertices.next().getElement()+",";
							}
							out = String.format("K4 found %n%s", out.substring(0,out.length()-1));
							outputArea.setText(out);
						}else
							outputArea.setText("K4 not found");
					}else if(selectedButton.equals("Simplicial Vertex")){
						List<Graph.Vertex<Integer>> simpVertex = DetectSimplicialVertex.detect(graph);
						if(!simpVertex.isEmpty()){
							String out = simpVertex.get(0).getElement()+"";
							out = String.format("Simplicial vertex found %n%s", out);
							outputArea.setText(out);
						}else
							outputArea.setText("Simplicial vertex not found");
					}else if(selectedButton.equals("Triangle")){
						List<UndirectedGraph<Integer,Integer>> triangle = DetectTriangle.detect(graph);
						if(triangle!=null){
							Iterator<Graph.Vertex<Integer>> vertices = triangle.get(0).vertices();
							String out = "";
							while(vertices.hasNext()){
								out+=vertices.next().getElement()+",";
							}
							out = String.format("Triangle found %n%s", out.substring(0,out.length()-1));
							outputArea.setText(out);
						}else
							outputArea.setText("Triangle not found");
					}			
				}
				
			};
			new Thread(r).start();
		}else if(source.equals(generateButton)){
			
		}else if(source.equals(selectFileButton)){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(new File("").getAbsolutePath()));
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "txt files", "txt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		      

		       String name = chooser.getCurrentDirectory().getAbsolutePath()+File.separator+chooser.getSelectedFile().getName();
		       selectFileField.setText(name);
		    }

			
		}
		
	}
	
	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		Enumeration<AbstractButton> bs = buttonGroup.getElements();
        while (bs.hasMoreElements()) {
            AbstractButton button = bs.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

}
