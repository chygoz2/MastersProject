package gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import general.Graph;
import general.Utility;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JRadioButton diamondRadioButton;		
	private JRadioButton clawRadioButton;
	private JRadioButton k4RadioButton;
	private JRadioButton simpVertexRadioButton;
	private JRadioButton triangleRadioButton;
	private JRadioButton kLRadioButton;
	private JButton detectButton;
	
	private JRadioButton diamondRadioButton2;		
	private JRadioButton clawRadioButton2;
	private JRadioButton k4RadioButton2;
	private JRadioButton simpVertexRadioButton2;
	private JRadioButton triangleRadioButton2;
	private JRadioButton kLRadioButton2;
	private JButton listButton;
	
	private JTextField vertexCountField;
	private JTextField selectFileField;
	private JTextField selectFileField2;
	
	private JButton selectFileButton;
	private JButton selectFileButton2;
	private JEditorPane outputArea;
	private JButton generateButton;
	
	private JButton cancelButton1;
	private JButton cancelButton2;
	private JButton cancelButton3;
	
	private ButtonGroup buttonGroup;
	private ButtonGroup buttonGroup2;
	private ButtonGroup buttonGroup3;
	private JPanel klPanel;
	private JTextField sizeField;
	
	private JPanel klPanel2;
	private JTextField sizeField2;
	
	private JPanel klPanel3;
	private JTextField sizeField3;

	private JTabbedPane tabbedPane;
	private JRadioButton triangleFreeButton;
	private JRadioButton diamondFreeButton;
	private JRadioButton clawFreeButton;
	private JRadioButton k4FreeButton;
	private JRadioButton klFreeButton; 

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
		setTitle("Small Induced Subgraph Identification Tool");
		setBounds(300, 100, 800, 350);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(10,10));
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		contentPane.add(panel1, BorderLayout.WEST);
		
		tabbedPane = new JTabbedPane();
		JPanel detectPanel = new JPanel();
		JPanel generatePanel = new JPanel();
		JPanel listPanel = new JPanel();
		tabbedPane.addTab("Detect", detectPanel);
		tabbedPane.addTab("List", listPanel);
		tabbedPane.addTab("Generate", generatePanel);
		
		panel1.add(tabbedPane);
		
		//for detect panel
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
		JLabel blankLabel7 = new JLabel();
		
		cancelButton1 = new JButton("Cancel");
		klPanel = new JPanel();
		klPanel.setVisible(false);
		JLabel klLabel = new JLabel("Enter size: ");
		sizeField = new JTextField(5);
		klPanel.add(klLabel); klPanel.add(sizeField);
		
		detectPanel.add(triangleRadioButton);	detectPanel.add(blankLabel7);
		detectPanel.add(diamondRadioButton);	detectPanel.add(blankLabel2);
		detectPanel.add(clawRadioButton);		detectPanel.add(blankLabel3);
		detectPanel.add(simpVertexRadioButton);	detectPanel.add(blankLabel5);
		detectPanel.add(k4RadioButton);			detectPanel.add(blankLabel4);
		detectPanel.add(kLRadioButton);			detectPanel.add(klPanel);
		detectPanel.add(cancelButton1);			detectPanel.add(detectButton);
		
		//for list panel
		listPanel.setLayout(new GridLayout(9,2,5,5));
		selectFileButton2 = new JButton("Select File");
		listPanel.add(selectFileButton2);
		selectFileField2 = new JTextField(5);
		listPanel.add(selectFileField2);
		
		JLabel listTypeLabel = new JLabel("Select Listing Type");
		JLabel blankLabel22 = new JLabel();
		listPanel.add(listTypeLabel);
		listPanel.add(blankLabel22);
		
		diamondRadioButton2 = new JRadioButton("Diamond");		
		clawRadioButton2 = new JRadioButton("Claw");
		k4RadioButton2 = new JRadioButton("K4");
		simpVertexRadioButton2 = new JRadioButton("Simplicial Vertex");
		triangleRadioButton2 = new JRadioButton("Triangle");
		triangleRadioButton2.setSelected(true);
		kLRadioButton2 = new JRadioButton("KL");
		listButton = new JButton("List");
		
		buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(diamondRadioButton2);
		buttonGroup2.add(clawRadioButton2);
		buttonGroup2.add(k4RadioButton2);
		buttonGroup2.add(simpVertexRadioButton2);
		buttonGroup2.add(triangleRadioButton2);
		buttonGroup2.add(kLRadioButton2);
		
		JLabel blankLabel12 = new JLabel();
		JLabel blankLabel13 = new JLabel();
		JLabel blankLabel14 = new JLabel();
		JLabel blankLabel15 = new JLabel();
		JLabel blankLabel17 = new JLabel();
		
		cancelButton2 = new JButton("Cancel");
		klPanel2 = new JPanel();
		klPanel2.setVisible(false);
		JLabel klLabel2 = new JLabel("Enter size: ");
		sizeField2 = new JTextField(5);
		klPanel2.add(klLabel2); klPanel2.add(sizeField2);
		
		listPanel.add(triangleRadioButton2);	listPanel.add(blankLabel17);
		listPanel.add(diamondRadioButton2);		listPanel.add(blankLabel12);
		listPanel.add(clawRadioButton2);		listPanel.add(blankLabel13);
		listPanel.add(simpVertexRadioButton2);	listPanel.add(blankLabel15);
		listPanel.add(k4RadioButton2);			listPanel.add(blankLabel14);
		listPanel.add(kLRadioButton2);			listPanel.add(klPanel2);
		listPanel.add(cancelButton2);			listPanel.add(listButton);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout());
		JLabel outputLabel = new JLabel("Output Area");
		panel4.add(outputLabel, BorderLayout.NORTH);
		outputArea = new JEditorPane();
		JScrollPane scrollPane = new JScrollPane(outputArea);
		outputArea.setEditable(false);
		panel4.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(panel4, BorderLayout.CENTER);
		
		//for generate panel
		JLabel titleLabel = new JLabel("Generate Random Pattern-free Graph");
		generatePanel.setLayout(new BorderLayout());
		generatePanel.add(titleLabel, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(8,2));
		JLabel vertexCountLabel = new JLabel("Vertex Count");
		vertexCountField = new JTextField(5);
		centerPanel.add(vertexCountLabel); centerPanel.add(vertexCountField);
		centerPanel.add(new JLabel("Select pattern type")); centerPanel.add(new JLabel());
		
		triangleFreeButton = new JRadioButton("Triangle-free");
		triangleFreeButton.setSelected(true);
		diamondFreeButton = new JRadioButton("Diamond-free");
		clawFreeButton = new JRadioButton("Claw-free");
		k4FreeButton = new JRadioButton("K4-free");
		klFreeButton = new JRadioButton("KL-free");
		
		buttonGroup3 = new ButtonGroup();
		buttonGroup3.add(triangleFreeButton);
		buttonGroup3.add(diamondFreeButton);
		buttonGroup3.add(clawFreeButton);
		buttonGroup3.add(k4FreeButton);
		buttonGroup3.add(klFreeButton);
		
		centerPanel.add(triangleFreeButton); 	centerPanel.add(new JLabel());
		centerPanel.add(diamondFreeButton); 	centerPanel.add(new JLabel());
		centerPanel.add(clawFreeButton);		centerPanel.add(new JLabel());
		centerPanel.add(k4FreeButton);			centerPanel.add(new JLabel());
		centerPanel.add(klFreeButton);
		
		klPanel3 = new JPanel();
		klPanel3.add(new JLabel("Enter size: "));
		sizeField3 = new JTextField(5);
		klPanel3.add(sizeField3);
		klPanel3.setVisible(false);
		
		centerPanel.add(klPanel3);
		generateButton = new JButton("Generate");
		cancelButton3 = new JButton("Cancel");
		centerPanel.add(cancelButton3);			centerPanel.add(generateButton);
		
		generatePanel.add(centerPanel, BorderLayout.CENTER);
		
		Controller controller = new Controller(this);
		detectButton.addActionListener(controller);
		listButton.addActionListener(controller);
		generateButton.addActionListener(controller);
		selectFileButton.addActionListener(controller);
		selectFileButton2.addActionListener(controller);
		kLRadioButton.addItemListener(controller);
		kLRadioButton2.addItemListener(controller);
		klFreeButton.addItemListener(controller);
		tabbedPane.addChangeListener(controller);
		cancelButton1.addActionListener(controller);
		cancelButton2.addActionListener(controller);
		cancelButton3.addActionListener(controller);
		
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

	/**
	 * @return the diamondRadioButton
	 */
	public JRadioButton getDiamondRadioButton() {
		return diamondRadioButton;
	}

	/**
	 * @return the clawRadioButton
	 */
	public JRadioButton getClawRadioButton() {
		return clawRadioButton;
	}

	/**
	 * @return the k4RadioButton
	 */
	public JRadioButton getK4RadioButton() {
		return k4RadioButton;
	}

	/**
	 * @return the simpVertexRadioButton
	 */
	public JRadioButton getSimpVertexRadioButton() {
		return simpVertexRadioButton;
	}

	/**
	 * @return the triangleRadioButton
	 */
	public JRadioButton getTriangleRadioButton() {
		return triangleRadioButton;
	}

	/**
	 * @return the kLRadioButton
	 */
	public JRadioButton getkLRadioButton() {
		return kLRadioButton;
	}

	/**
	 * @return the detectButton
	 */
	public JButton getDetectButton() {
		return detectButton;
	}

	/**
	 * @return the diamondRadioButton2
	 */
	public JRadioButton getDiamondRadioButton2() {
		return diamondRadioButton2;
	}

	/**
	 * @return the clawRadioButton2
	 */
	public JRadioButton getClawRadioButton2() {
		return clawRadioButton2;
	}

	/**
	 * @return the k4RadioButton2
	 */
	public JRadioButton getK4RadioButton2() {
		return k4RadioButton2;
	}

	/**
	 * @return the simpVertexRadioButton2
	 */
	public JRadioButton getSimpVertexRadioButton2() {
		return simpVertexRadioButton2;
	}

	/**
	 * @return the triangleRadioButton2
	 */
	public JRadioButton getTriangleRadioButton2() {
		return triangleRadioButton2;
	}

	/**
	 * @return the kLRadioButton2
	 */
	public JRadioButton getkLRadioButton2() {
		return kLRadioButton2;
	}

	/**
	 * @return the listButton
	 */
	public JButton getListButton() {
		return listButton;
	}

	/**
	 * @return the vertexCountField
	 */
	public JTextField getVertexCountField() {
		return vertexCountField;
	}

	/**
	 * @return the selectFileField
	 */
	public JTextField getSelectFileField() {
		return selectFileField;
	}

	/**
	 * @return the selectFileField2
	 */
	public JTextField getSelectFileField2() {
		return selectFileField2;
	}

	/**
	 * @return the selectFileButton
	 */
	public JButton getSelectFileButton() {
		return selectFileButton;
	}

	/**
	 * @return the selectFileButton2
	 */
	public JButton getSelectFileButton2() {
		return selectFileButton2;
	}

	/**
	 * @return the outputArea
	 */
	public JEditorPane getOutputArea() {
		return outputArea;
	}

	/**
	 * @return the generateButton
	 */
	public JButton getGenerateButton() {
		return generateButton;
	}

	/**
	 * @return the buttonGroup
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @return the buttonGroup2
	 */
	public ButtonGroup getButtonGroup2() {
		return buttonGroup2;
	}

	/**
	 * @return the buttonGroup3
	 */
	public ButtonGroup getButtonGroup3() {
		return buttonGroup3;
	}

	/**
	 * @return the klPanel
	 */
	public JPanel getKlPanel() {
		return klPanel;
	}

	/**
	 * @return the sizeField
	 */
	public JTextField getSizeField() {
		return sizeField;
	}

	/**
	 * @return the klPanel2
	 */
	public JPanel getKlPanel2() {
		return klPanel2;
	}

	/**
	 * @return the sizeField2
	 */
	public JTextField getSizeField2() {
		return sizeField2;
	}

	/**
	 * @return the klPanel3
	 */
	public JPanel getKlPanel3() {
		return klPanel3;
	}

	/**
	 * @return the sizeField3
	 */
	public JTextField getSizeField3() {
		return sizeField3;
	}

	/**
	 * @return the tabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	/**
	 * @return the triangleFreeButton
	 */
	public JRadioButton getTriangleFreeButton() {
		return triangleFreeButton;
	}

	/**
	 * @return the diamondFreeButton
	 */
	public JRadioButton getDiamondFreeButton() {
		return diamondFreeButton;
	}

	/**
	 * @return the clawFreeButton
	 */
	public JRadioButton getClawFreeButton() {
		return clawFreeButton;
	}

	/**
	 * @return the k4FreeButton
	 */
	public JRadioButton getK4FreeButton() {
		return k4FreeButton;
	}

	/**
	 * @return the klFreeButton
	 */
	public JRadioButton getKlFreeButton() {
		return klFreeButton;
	}

	/**
	 * @return the cancelButton1
	 */
	public JButton getCancelButton1() {
		return cancelButton1;
	}

	/**
	 * @return the cancelButton2
	 */
	public JButton getCancelButton2() {
		return cancelButton2;
	}

	/**
	 * @return the cancelButton3
	 */
	public JButton getCancelButton3() {
		return cancelButton3;
	}
}
