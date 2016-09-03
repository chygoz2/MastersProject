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

public class Controller implements ActionListener, ItemListener, ChangeListener{
	
	private GUI frame;
	private DetectWorker detectWorker;
	private ListWorker listWorker;
	private GenerateWorker generateWorker;
	
	public Controller(GUI f){
		this.frame = f;
	}
	
	@Override
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

	@Override
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if(source.equals(frame.getDetectButton())){
			detectSubgraph();
		}else if(source.equals(frame.getListButton())){
			listSubgraphs();
		}
		else if(source.equals(frame.getGenerateButton())){
			generateGraph();
		}
		else if(source.equals(frame.getSelectFileButton()) ||
				source.equals(frame.getSelectFileButton2())){
			getSelectedFile();
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
	
	public void detectSubgraph(){
		String selectedButton = getSelectedButtonText(frame.getButtonGroup());
		String fileName = frame.getSelectFileField().getText().trim();
		int l=0;
		try{
			if(fileName.length()==0){
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
			
			detectWorker = new DetectWorker(selectedButton,fileName,l, frame.getOutputArea());
			detectWorker.execute();
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
			return;
		}
	}
	
	public void listSubgraphs(){
		String selectedButton = getSelectedButtonText(frame.getButtonGroup2());
		String fileName = frame.getSelectFileField2().getText().trim();
		int l=0;
		try{
			if(fileName.length()==0){
				JOptionPane.showMessageDialog(null, "Please select input file");
				return;
			}
			if(selectedButton.equals("KL")){
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
			listWorker = new ListWorker(selectedButton,fileName,l,frame.getOutputArea());
			listWorker.execute();
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size of the complete subgraph entered is not an integer");
			return;
		}
	}
	
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
			}else if(l<1 && selectedButton.equals("KL-free")){
				JOptionPane.showMessageDialog(null, "Size of complete subgraph should be greater than zero");
			}else{
				
				generateWorker = new GenerateWorker(selectedButton,n,l,frame.getOutputArea());
				generateWorker.execute();
			}
		}catch(NumberFormatException f){
			JOptionPane.showMessageDialog(null, "The size entered is not an integer");
			return;
		}
	}
	
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
