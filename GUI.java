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
		setBounds(100, 100, 450, 300);
		setTitle("Subgraph identification program");
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(8, 1, 5, 0));
		
		menuBar = new JMenuBar();
		contentPane.add(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mnDetect = new JMenu("Detect");
		menuBar.add(mnDetect);
		
		mnGenerate = new JMenu("Generate");
		menuBar.add(mnGenerate);
		
		panel_2 = new JPanel();
		contentPane.add(panel_2);
		
		fileNameTextField = new JTextField();
		panel_2.add(fileNameTextField);
		fileNameTextField.setColumns(20);
		
		JButton selectFileButton = new JButton("Select File");
		panel_2.add(selectFileButton);
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
		
		panel_1 = new JPanel();
		contentPane.add(panel_1);
		diamondRadioButton = new JRadioButton("Diamond");
		panel_1.add(diamondRadioButton);
		diamondRadioButton.setSelected(true);
		
		clawRadioButton = new JRadioButton("Claw");
		panel_1.add(clawRadioButton);
		
		k4RadioButton = new JRadioButton("K4");
		panel_1.add(k4RadioButton);
		
		simplVertexRadioButton = new JRadioButton("Simpl. Vertex");
		panel_1.add(simplVertexRadioButton);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		buttonGroup.add(clawRadioButton);
		buttonGroup.add(diamondRadioButton);
		buttonGroup.add(k4RadioButton);
		buttonGroup.add(simplVertexRadioButton);
		
		btnDetect = new JButton("Detect");
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
									out+=vertices.next().getElement()+","; //remove the trailing comma
								}
								out = out.substring(0,out.length()-1);
								System.out.println(out);
							}else{
								System.out.println("Diamond not found");
							}
						}else if(selectedButton.equals("Claw")){
							
							UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
							if(claw!=null){
//								Utility.printGraph(claw);
								Iterator<Graph.Vertex<Integer>> vertices = claw.vertices();
								String out = "";
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+","; //remove the trailing comma
								}
								out = out.substring(0,out.length()-1);
								System.out.println(out);
							}else
								System.out.println("Claw not found");
						}else if(selectedButton.equals("K4")){
							UndirectedGraph<Integer,Integer> k4 = DetectK4.detect(graph);
							if(k4!=null){
								Iterator<Graph.Vertex<Integer>> vertices = k4.vertices();
								String out = "";
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+",";
								}
								out = out.substring(0,out.length()-1); //remove the trailing comma
								System.out.println(out);
							}else
								System.out.println("K4 not found");
						}else if(selectedButton.equals("Simpl. Vertex")){
							Graph.Vertex<Integer> simpVertex = DetectSimplicialVertex.detect(graph);
							if(simpVertex!=null){
								String out = simpVertex.getElement()+"";
								System.out.println(out);
							}else
								System.out.println("Simplicial vertex not found");
						}
					
					}
					
				};
				new Thread(r).start();
			}
		});
		
		panel_1.add(btnDetect);
		
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
