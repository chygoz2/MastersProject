import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JLabel;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField fileNameTextField;
	private JPanel panel_1;
	private JRadioButton diamondRadioButton;
	private JRadioButton clawRadioButton;
	private JPanel panel_2;
	private JButton btnDetect;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnDetect;
	private JMenu mnGenerate;
	private JRadioButton k4RadioButton;
	private JRadioButton simplVertexRadioButton;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JPanel panel_3;
	private JLabel lblVertexCount;
	private JTextField vertexCountField;
	private JLabel lblEdgeProbability;
	private JTextField probabilityField;
	private JLabel lblNoOfGraphs;
	private JTextField graphNoField;
	private JButton generateButton;
	private JLabel lblNewLabel;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 450);
		setTitle("Subgraph identification program");
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{super.getWidth(), 0};
		gbl_contentPane.rowHeights = new int[]{203, 203, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{super.getWidth(), 0};
		gbl_panel.rowHeights = new int[]{33, 33, 33, 33, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		menuBar = new JMenuBar();
		GridBagConstraints gbc_menuBar = new GridBagConstraints();
		gbc_menuBar.fill = GridBagConstraints.BOTH;
		gbc_menuBar.insets = new Insets(0, 0, 5, 0);
		gbc_menuBar.gridx = 0;
		gbc_menuBar.gridy = 0;
		panel.add(menuBar, gbc_menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mnDetect = new JMenu("Detect");
		menuBar.add(mnDetect);
		
		mnGenerate = new JMenu("Generate");
		menuBar.add(mnGenerate);
		
		panel_3 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		flowLayout.setVgap(2);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.anchor = GridBagConstraints.WEST;
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridheight = 4;
		gbc_panel_3.fill = GridBagConstraints.VERTICAL;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		//panel.add(panel_3, gbc_panel_3);
		
		panel_2 = new JPanel();
		panel_3.add(panel_2);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Detect", null, panel_3);
		panel.add(tabbedPane, gbc_panel_3);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Generate", null, panel_4);
		panel_4.setLayout(new GridLayout(4, 2, 5, 5));
		
		lblVertexCount = new JLabel("Vertex count");
		panel_4.add(lblVertexCount);
		
		vertexCountField = new JTextField();
		panel_4.add(vertexCountField);
		vertexCountField.setColumns(10);
		
		lblEdgeProbability = new JLabel("Edge probability");
		panel_4.add(lblEdgeProbability);
		
		probabilityField = new JTextField();
		panel_4.add(probabilityField);
		probabilityField.setColumns(10);
		
		lblNoOfGraphs = new JLabel("No. of graphs");
		panel_4.add(lblNoOfGraphs);
		
		graphNoField = new JTextField();
		panel_4.add(graphNoField);
		graphNoField.setColumns(10);
		
		lblNewLabel = new JLabel("");
		panel_4.add(lblNewLabel);
		
		generateButton = new JButton("Generate");
		panel_4.add(generateButton);
		
		fileNameTextField = new JTextField();
		panel_2.add(fileNameTextField);
		fileNameTextField.setColumns(20);
		
		JButton selectFileButton = new JButton("Select File");
		panel_2.add(selectFileButton);
		
		panel_1 = new JPanel();
		panel_3.add(panel_1);
		diamondRadioButton = new JRadioButton("Diamond");
		panel_1.add(diamondRadioButton);
		diamondRadioButton.setSelected(true);
		
		clawRadioButton = new JRadioButton("Claw");
		panel_1.add(clawRadioButton);
		
		k4RadioButton = new JRadioButton("K4");
		panel_1.add(k4RadioButton);
		
		simplVertexRadioButton = new JRadioButton("Simpl. Vertex");
		panel_1.add(simplVertexRadioButton);
		
		buttonGroup.add(clawRadioButton);
		buttonGroup.add(diamondRadioButton);
		buttonGroup.add(k4RadioButton);
		buttonGroup.add(simplVertexRadioButton);
		
		btnDetect = new JButton("Detect");
		
		
		panel_1.add(btnDetect);
		
		JTextArea outputArea = new JTextArea(10,10);
		outputArea.setEnabled(false);
		scrollPane = new JScrollPane(outputArea);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0,0,5,0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		btnDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedButton = getSelectedButtonText(buttonGroup);
				
				String fileName = fileNameTextField.getText();;
				UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
				
				Runnable r = new Runnable(){
					@Override
					public void run() {
						if(selectedButton.equals("Diamond")){
							UndirectedGraph<Integer,Integer> diamond = DetectDiamond.detect(graph);
							if(diamond!=null){
//								Utility.printGraph(diamond);
								Iterator<Graph.Vertex<Integer>> vertices = diamond.vertices();
								String out = "";
								
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+","; 
								}
								out = String.format("Diamond found %n%s", out.substring(0,out.length()-1));
								outputArea.setText(out);
								//System.out.println(out);
							}else{
								outputArea.setText("Diamond not found");
							}
						}else if(selectedButton.equals("Claw")){
							
							UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
							if(claw!=null){
//								Utility.printGraph(claw);
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
							UndirectedGraph<Integer,Integer> k4 = DetectK4.detect(graph);
							if(k4!=null){
								Iterator<Graph.Vertex<Integer>> vertices = k4.vertices();
								String out = "";
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+",";
								}
								out = String.format("K4 found %n%s", out.substring(0,out.length()-1));
								outputArea.setText(out);
							}else
								outputArea.setText("K4 not found");
						}else if(selectedButton.equals("Simpl. Vertex")){
							Graph.Vertex<Integer> simpVertex = DetectSimplicialVertex.detect(graph);
							if(simpVertex!=null){
								String out = simpVertex.getElement()+"";
								out = String.format("Simplicial vertex found %n%s", out);
								outputArea.setText(out);
							}else
								outputArea.setText("Simplicial vertex not found");
						}
					
					}
					
				};
				new Thread(r).start();
			}
		});
		selectFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(new File("").getAbsolutePath()));
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "txt files", "txt");
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			      

			       String name = chooser.getCurrentDirectory().getAbsolutePath()+File.separator+chooser.getSelectedFile().getName();
			       fileNameTextField.setText(name);
			    }

			}
		});
		
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
