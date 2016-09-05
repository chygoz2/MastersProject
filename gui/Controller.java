package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * controller class for the GUI
 * @author Chigozie Ekwonu
 *
 */
public class Controller implements ActionListener, ItemListener, ChangeListener{
	
	//instance variables
	private GUI frame; //the frame object
	private DetectWorker detectWorker; //worker object to handle subgraph detection
	private ListWorker listWorker; //worker object to handle subgraph listing
	private GenerateWorker generateWorker; //worker object to handle random graph generation
	
	/**
	 * constructor to initialize the frame instance variable
	 * @param f		the frame that the controller belongs to
	 */
	public Controller(GUI f){
		this.frame = f;
	}
	
	/**
	 * event handler for handling item event generated. It shows or hides the text field for 
	 * collecting the complete subgraph size to be detected, listed or generated.
	 */
	public void itemStateChanged(ItemEvent e) {
		JRadioButton source = (JRadioButton) e.getItem();
		if(source.equals(frame.getkLRadioButton())){
			if(e.getStateChange()==ItemEvent.SELECTED){
				frame.getKlPanel().setVisible(true);
			}
			else{
				frame.getKlPanel().setVisible(false);
				frame.getSizeField().setText("");
			}
		}
		else if(source.equals(frame.getkLRadioButton2())){
			if(e.getStateChange()==ItemEvent.SELECTED){
				frame.getKlPanel2().setVisible(true);
			}
			else{
				frame.getKlPanel2().setVisible(false);
				frame.getSizeField2().setText("");
			}
		}
		else if(source.equals(frame.getKlFreeButton())){
			if(e.getStateChange()==ItemEvent.SELECTED){
				frame.getKlPanel3().setVisible(true);
			}
			else{
				frame.getKlPanel3().setVisible(false);
				frame.getSizeField3().setText("");
			}
		}
	}

	/**
	 * method for handling change event generated when a different pane of 
	 * the tabbedpane is selected. It ensures that the selected graph file is retained
	 * across panes
	 */
	public void stateChanged(ChangeEvent e) {
		int selectedIndex = frame.getTabbedPane().getSelectedIndex();
		if(selectedIndex==0){
			String selectedFile2 = frame.getSelectFileField2().getText().trim();
			if(selectedFile2.length()>0){
				frame.getSelectFileField().setText(selectedFile2);
			}
		}
		else if(selectedIndex==1){
			String selectedFile = frame.getSelectFileField().getText().trim();
			if(selectedFile.length()>0){
				frame.getSelectFileField2().setText(selectedFile);
			}
		}
	}
	
	/**
	 * method that handles the action event generated when a button is clicked.
	 * It starts the respective worker thread to handle the desired operation
	 */
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if(source.equals(frame.getDetectButton())){
			detectSubgraph(); //call method to start detection if the detect button is clicked
		}else if(source.equals(frame.getListButton())){
			listSubgraphs(); //call method to start listing if the list button is clicked
		}
		else if(source.equals(frame.getGenerateButton())){
			generateGraph();//call method to start generation if the generate button is clicked
		}
		else if(source.equals(frame.getCancelButton1())){
			//cancels subgraph detection if the cancel button of the detect pane is clicked
			if(detectWorker!=null && !detectWorker.isDone()){
				detectWorker.cancel(true);
				detectWorker = null;
			}
		}
		else if(source.equals(frame.getCancelButton2())){
			//cancels subgraph listing if the cancel button of the list pane is clicked
			if(listWorker!=null && !listWorker.isDone()){
				listWorker.cancel(true);
				listWorker = null;
			}
		}
		else if(source.equals(frame.getCancelButton3())){
			//cancels graph generation if the cancel button of the generate pane is clicked
			if(generateWorker!=null && !generateWorker.isDone()){
				generateWorker.cancel(true);
				generateWorker = null;
			}
		}
		else if(source.equals(frame.getSelectFileButton()) ||
				source.equals(frame.getSelectFileButton2())){
			getSelectedFile(); //get the graph file selected in the JFileChooser panel
		}
	}
	
	/**
	 * method to get which radio button in a button group is selected
	 * @param buttonGroup	the button group containing the radio buttons
	 * @return				the text of the selected radio button
	 */
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
	
	/**
	 * method to detect a subgraph by calling the respective detection class depending on the 
	 * subgraph type to be detected
	 */
	public void detectSubgraph(){
		String selectedButton = getSelectedButtonText(frame.getButtonGroup());
		String fileName = frame.getSelectFileField().getText().trim(); //get graph file name
		int l=0;
		try{
			if(fileName.length()==0){//ensure that user selects a file before proceeding
				JOptionPane.showMessageDialog(null, "Please select input file", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(selectedButton.equals("KL")){
				String s = frame.getSizeField().getText().trim();
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
			detectWorker = new DetectWorker(selectedButton,fileName,l, frame.getOutputArea(),frame.getDetectButton());
			frame.getDetectButton().setEnabled(false);
			detectWorker.execute();
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
			return;
		}
	}
	
	/**
	 * method to list a subgraph by calling the respective listing class depending on the 
	 * subgraph type to be listed
	 */
	public void listSubgraphs(){
		String selectedButton = getSelectedButtonText(frame.getButtonGroup2());
		String fileName = frame.getSelectFileField2().getText().trim();//get selected graph file
		int l=0;
		try{
			if(fileName.length()==0){//ensure that user selects a file before proceeding
				JOptionPane.showMessageDialog(null, "Please select input file");
				return;
			}
			if(selectedButton.equals("KL")){
				//ensure complete subgraph size is valid
				String s = frame.getSizeField2().getText().trim();
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
			listWorker = new ListWorker(selectedButton,fileName,l,frame.getOutputArea(),frame.getListButton());
			frame.getListButton().setEnabled(false);
			listWorker.execute();
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
			return;
		}
	}
	
	/**
	 * method to generate a random graph by calling the respective generation class method depending on the 
	 * subgraph type to be absent from the generated graph
	 */
	public void generateGraph(){
		String selectedButton = getSelectedButtonText(frame.getButtonGroup3());
		int n = 0, l=0;
		try{
			n = Integer.parseInt(frame.getVertexCountField().getText().trim());
			String s = frame.getSizeField3().getText().trim();
			if(s.length()>0)
				l = Integer.parseInt(s);
			if(n<1){
				JOptionPane.showMessageDialog(null, "Number of vertices should be greater than zero");
			}else if(l<2 && selectedButton.equals("KL-free")){
				JOptionPane.showMessageDialog(null, "Size of complete subgraph should be greater than one");
			}else{
				
				generateWorker = new GenerateWorker(selectedButton,n,l,frame.getOutputArea(),frame.getGenerateButton());
				frame.getGenerateButton().setEnabled(false);
				generateWorker.execute();
			}
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size entered is not an integer");
			return;
		}
	}
	
	/**
	 * get file selected by user from the JFileChooser
	 */
	public void getSelectedFile(){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(new File("").getAbsolutePath()));
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "txt files", "txt");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(frame.getParent());
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       String name = chooser.getCurrentDirectory().getAbsolutePath()+File.separator+chooser.getSelectedFile().getName();
	       frame.getSelectFileField().setText(name);
	       frame.getSelectFileField2().setText(name);
	    }
	}
}
