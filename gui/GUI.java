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
import javax.swing.filechooser.FileNameExtensionFilter;

import general.Graph;
import general.Utility;

public class GUI extends JFrame 
		implements ActionListener, ItemListener{

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		JLabel blankLabel6 = new JLabel();
		JLabel blankLabel7 = new JLabel();
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
		detectPanel.add(blankLabel6);			detectPanel.add(detectButton);
		
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
		JLabel blankLabel16 = new JLabel();
		JLabel blankLabel17 = new JLabel();
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
		listPanel.add(blankLabel16);			listPanel.add(listButton);
		
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
		centerPanel.add(new JLabel());			centerPanel.add(generateButton);
		
		generatePanel.add(centerPanel, BorderLayout.CENTER);
		
		
		detectButton.addActionListener(this);
		listButton.addActionListener(this);
		generateButton.addActionListener(this);
		selectFileButton.addActionListener(this);
		selectFileButton2.addActionListener(this);
		kLRadioButton.addItemListener(this);
		kLRadioButton2.addItemListener(this);
		klFreeButton.addItemListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if(source.equals(detectButton)){
			String selectedButton = getSelectedButtonText(buttonGroup);
			String fileName = selectFileField.getText().trim();
			int l=0;
			try{
				if(fileName.length()==0){
					JOptionPane.showMessageDialog(null, "Please select input file");
					return;
				}
				if(selectedButton.equals("KL")){
					String s = sizeField.getText().trim();
					if(s.length()==0){
						JOptionPane.showMessageDialog(null, "Please enter size of the complete subgraph to be listed");
						return;
					}
					l = Integer.parseInt(s);
					if(l<1){
						JOptionPane.showMessageDialog(null, "Size of the complete subgraph should be greater than 0");
						return;
					}
				}
				
				DetectWorker w = new DetectWorker(selectedButton,fileName,l, outputArea);
				w.execute();
			}catch(NumberFormatException f){
				JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
				return;
			}
			
		}else if(source.equals(listButton)){
			String selectedButton = getSelectedButtonText(buttonGroup2);
			String fileName = selectFileField2.getText().trim();
			int l=0;
			try{
				if(fileName.length()==0){
					JOptionPane.showMessageDialog(null, "Please select input file");
					return;
				}
				if(selectedButton.equals("KL")){
					String s = sizeField2.getText().trim();
					if(s.length()==0){
						JOptionPane.showMessageDialog(null, "Please enter size of the complete subgraph to be listed");
						return;
					}
					l = Integer.parseInt(s);
					if(l<1){
						JOptionPane.showMessageDialog(null, "Size of the complete subgraph should be greater than 0");
						return;
					}
				}
				ListWorker w = new ListWorker(selectedButton,fileName,l,outputArea);
				w.execute();
			}catch(NumberFormatException f){
				JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
				return;
			}
		}
		else if(source.equals(generateButton)){
			String selectedButton = getSelectedButtonText(buttonGroup3);
			int n = 0, l=0;
			try{
				n = Integer.parseInt(vertexCountField.getText().trim());
				String s = sizeField3.getText().trim();
				if(s.length()>0)
					l = Integer.parseInt(s);
				if(n<1){
					JOptionPane.showMessageDialog(null, "Number of vertices should be greater than zero");
				}else if(l<1 && selectedButton.equals("KL-free")){
					JOptionPane.showMessageDialog(null, "Size of complete subgraph should be greater than zero");
				}else{
					
					GenerateWorker w = new GenerateWorker(selectedButton,n,l,outputArea);
					w.execute();
				}
			}catch(NumberFormatException f){
				JOptionPane.showMessageDialog(null, "The size entered is not an integer");
				return;
			}
		}
		else if(source.equals(selectFileButton)){
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
		else if(source.equals(selectFileButton2)){
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(new File("").getAbsolutePath()));
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "txt files", "txt");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(getParent());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       String name = chooser.getCurrentDirectory().getAbsolutePath()+File.separator+chooser.getSelectedFile().getName();
		       selectFileField2.setText(name);
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

	@Override
	public void itemStateChanged(ItemEvent e) {
		JRadioButton source = (JRadioButton) e.getItem();
		if(source.equals(kLRadioButton)){
			if(e.getStateChange()==ItemEvent.SELECTED){
				klPanel.setVisible(true);
			}
			else{
				klPanel.setVisible(false);
				sizeField.setText("");
			}
		}
		else if(source.equals(kLRadioButton2)){
			if(e.getStateChange()==ItemEvent.SELECTED){
				klPanel2.setVisible(true);
			}
			else{
				klPanel2.setVisible(false);
				sizeField2.setText("");
			}
		}
		else if(source.equals(klFreeButton)){
			if(e.getStateChange()==ItemEvent.SELECTED){
				klPanel3.setVisible(true);
			}
			else{
				klPanel3.setVisible(false);
				sizeField3.setText("");
			}
		}
	}
}
