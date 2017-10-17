package userInterfact;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JTextField;

import engine.Controller;
import engine.Expression;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.CardLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Window {
	private JFrame frmDefeasibleArgumentation;
	private JTextField expressionField;
	private JTextField argNameField;
	private JTextField factInputField;
	JComboBox<String> defeasibleComboBox;
	JTextArea premisesTextArea;
	JTextArea conclusionsTextArea;
	JTextArea factsTextArea;
	JTextArea argumentsTextArea;
	JTextArea resultsTextArea;
	
	private Controller cont;
	private ArrayList<String> premises=new ArrayList<>();
	private ArrayList<String> conclusions=new ArrayList<>();
	
	public Window(final Controller cont){
		
		this.cont=cont;
		
		frmDefeasibleArgumentation=new JFrame();
		frmDefeasibleArgumentation.setResizable(false);
		frmDefeasibleArgumentation.setTitle("Defeasible Argumentation");
		frmDefeasibleArgumentation.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDefeasibleArgumentation.getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 737, 357);
		frmDefeasibleArgumentation.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JPanel argumentInputPanel = new JPanel();
		argumentInputPanel.setBounds(0, 0, 737, 195);
		panel_1.add(argumentInputPanel);
		GridBagLayout gbl_argumentInputPanel = new GridBagLayout();
		gbl_argumentInputPanel.columnWidths = new int[]{137, 269, 294, 0};
		gbl_argumentInputPanel.rowHeights = new int[]{100, 0};
		gbl_argumentInputPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_argumentInputPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		argumentInputPanel.setLayout(gbl_argumentInputPanel);
		
		JPanel topControlPanel = new JPanel();
		GridBagConstraints gbc_topControlPanel = new GridBagConstraints();
		gbc_topControlPanel.fill = GridBagConstraints.BOTH;
		gbc_topControlPanel.insets = new Insets(0, 0, 0, 5);
		gbc_topControlPanel.gridx = 0;
		gbc_topControlPanel.gridy = 0;
		argumentInputPanel.add(topControlPanel, gbc_topControlPanel);
		GridBagLayout gbl_topControlPanel = new GridBagLayout();
		gbl_topControlPanel.columnWidths = new int[] {163};
		gbl_topControlPanel.rowHeights = new int[] {90, 102};
		gbl_topControlPanel.columnWeights = new double[]{0.0};
		gbl_topControlPanel.rowWeights = new double[]{0.0, 0.0};
		topControlPanel.setLayout(gbl_topControlPanel);
		
		JPanel expressionInputPanel = new JPanel();
		GridBagConstraints gbc_expressionInputPanel = new GridBagConstraints();
		gbc_expressionInputPanel.fill = GridBagConstraints.BOTH;
		gbc_expressionInputPanel.insets = new Insets(0, 0, 5, 0);
		gbc_expressionInputPanel.gridx = 0;
		gbc_expressionInputPanel.gridy = 0;
		topControlPanel.add(expressionInputPanel, gbc_expressionInputPanel);
		expressionInputPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("Conc");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String exp=expressionField.getText();
				if(exp.length()!=0 && Expression.correctFormat(exp)){
					//correct format entered, adding conclusion
					conclusions.add(exp);
					updateDisplay();
				}
			}
		});
		btnNewButton.setBounds(10, 62, 70, 23);
		expressionInputPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Prem");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String exp=expressionField.getText();
				if(exp.length()!=0 && Expression.correctFormat(exp)){
					//correct format entered, adding premise
					premises.add(exp);
					updateDisplay();
				}
			}
		});
		btnNewButton_1.setBounds(83, 62, 70, 23);
		expressionInputPanel.add(btnNewButton_1);
		
		expressionField = new JTextField();
		expressionField.setBounds(10, 31, 143, 20);
		expressionInputPanel.add(expressionField);
		expressionField.setColumns(10);
		
		JLabel lblExpression = new JLabel("Expression:");
		lblExpression.setBounds(10, 6, 77, 14);
		expressionInputPanel.add(lblExpression);
		
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) new Color(0, 0, 0)));
		panel.setLayout(null);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		topControlPanel.add(panel, gbc_panel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 8, 39, 14);
		panel.add(lblName);
		
		argNameField = new JTextField();
		argNameField.setBounds(59, 5, 94, 20);
		panel.add(argNameField);
		argNameField.setColumns(10);
		
		JButton btnAddArgument = new JButton("Insert");
		btnAddArgument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!premises.isEmpty()&&!conclusions.isEmpty()){
					String name=argNameField.getText();
					boolean defeasible;
					if(defeasibleComboBox.getSelectedItem().equals("yes"))
						defeasible=true;
					else
						defeasible=false;
					cont.createArgument(premises, conclusions, name, defeasible);
					
					resetArgCreation();
					updateDisplay();
				}
			}
		});
		btnAddArgument.setBounds(10, 68, 70, 23);
		panel.add(btnAddArgument);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetArgCreation();
				updateDisplay();
			}
		});
		btnReset.setBounds(83, 68, 70, 23);
		panel.add(btnReset);
		
		defeasibleComboBox = new JComboBox<>();
		defeasibleComboBox.addItem("yes");
		defeasibleComboBox.addItem("no");
		defeasibleComboBox.setMaximumRowCount(2);
		defeasibleComboBox.setBounds(92, 37, 61, 20);
		panel.add(defeasibleComboBox);
		
		JLabel lblDefeasible = new JLabel("Defeasible:");
		lblDefeasible.setBounds(10, 43, 72, 14);
		panel.add(lblDefeasible);
		
		JScrollPane premisesScrollPane = new JScrollPane();
		GridBagConstraints gbc_premisesScrollPane = new GridBagConstraints();
		gbc_premisesScrollPane.fill = GridBagConstraints.BOTH;
		gbc_premisesScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_premisesScrollPane.gridx = 1;
		gbc_premisesScrollPane.gridy = 0;
		argumentInputPanel.add(premisesScrollPane, gbc_premisesScrollPane);
		
		JLabel lblPremises = new JLabel("Premises:");
		premisesScrollPane.setColumnHeaderView(lblPremises);
		
		premisesTextArea = new JTextArea();
		premisesTextArea.setEditable(false);
		premisesScrollPane.setViewportView(premisesTextArea);
		
		JScrollPane conclusionsScrollPane = new JScrollPane();
		GridBagConstraints gbc_conclusionsScrollPane = new GridBagConstraints();
		gbc_conclusionsScrollPane.fill = GridBagConstraints.BOTH;
		gbc_conclusionsScrollPane.gridx = 2;
		gbc_conclusionsScrollPane.gridy = 0;
		argumentInputPanel.add(conclusionsScrollPane, gbc_conclusionsScrollPane);
		
		JLabel lblConclusions = new JLabel("Conclusions:");
		conclusionsScrollPane.setColumnHeaderView(lblConclusions);
		
		conclusionsTextArea = new JTextArea();
		conclusionsTextArea.setEditable(false);
		conclusionsScrollPane.setViewportView(conclusionsTextArea);
		
		JPanel factInputAndWorkingMemory = new JPanel();
		factInputAndWorkingMemory.setBounds(0, 206, 737, 159);
		panel_1.add(factInputAndWorkingMemory);
		GridBagLayout gbl_factInputAndWorkingMemory = new GridBagLayout();
		gbl_factInputAndWorkingMemory.columnWidths = new int[]{170, 266, 295, 0};
		gbl_factInputAndWorkingMemory.rowHeights = new int[]{149, 0};
		gbl_factInputAndWorkingMemory.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_factInputAndWorkingMemory.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		factInputAndWorkingMemory.setLayout(gbl_factInputAndWorkingMemory);
		
		JPanel bottomControlPanel = new JPanel();
		GridBagConstraints gbc_bottomControlPanel = new GridBagConstraints();
		gbc_bottomControlPanel.fill = GridBagConstraints.BOTH;
		gbc_bottomControlPanel.insets = new Insets(0, 0, 0, 5);
		gbc_bottomControlPanel.gridx = 0;
		gbc_bottomControlPanel.gridy = 0;
		factInputAndWorkingMemory.add(bottomControlPanel, gbc_bottomControlPanel);
		GridBagLayout gbl_bottomControlPanel = new GridBagLayout();
		gbl_bottomControlPanel.columnWidths = new int[]{165, 0};
		gbl_bottomControlPanel.rowHeights = new int[]{72, 76, 0};
		gbl_bottomControlPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_bottomControlPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		bottomControlPanel.setLayout(gbl_bottomControlPanel);
		
		JPanel factInput = new JPanel();
		factInput.setBorder(new MatteBorder(1, 0, 1, 0, (Color) new Color(0, 0, 0)));
		GridBagConstraints gbc_factInput = new GridBagConstraints();
		gbc_factInput.fill = GridBagConstraints.BOTH;
		gbc_factInput.insets = new Insets(0, 0, 5, 0);
		gbc_factInput.gridx = 0;
		gbc_factInput.gridy = 0;
		bottomControlPanel.add(factInput, gbc_factInput);
		factInput.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Fact:");
		lblNewLabel.setBounds(10, 9, 49, 14);
		factInput.add(lblNewLabel);
		
		factInputField = new JTextField();
		factInputField.setBounds(69, 6, 86, 20);
		factInput.add(factInputField);
		factInputField.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("Insert");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String exp=factInputField.getText();
				if(exp.length()!=0 && Expression.correctFormat(exp)){
					System.out.println("Fact inserted: " + exp);
					cont.createFact(exp);
					factInputField.setText("");
					updateWorkingMemoryLists();
				}
			}
		});
		btnNewButton_2.setBounds(10, 34, 70, 23);
		factInput.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Delete");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String exp=factInputField.getText();
				System.out.println("Fact deleted: " + exp);
				cont.removeFact(exp);
				factInputField.setText("");
				updateWorkingMemoryLists();
			}
		});
		btnNewButton_3.setBounds(85, 34, 70, 23);
		factInput.add(btnNewButton_3);
		
		JPanel fireRules = new JPanel();
		GridBagConstraints gbc_fireRules = new GridBagConstraints();
		gbc_fireRules.fill = GridBagConstraints.BOTH;
		gbc_fireRules.gridx = 0;
		gbc_fireRules.gridy = 1;
		bottomControlPanel.add(fireRules, gbc_fireRules);
		fireRules.setLayout(null);
		
		JButton btnNewButton_4 = new JButton("Fire Rules");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont.fireRules();
				System.out.println("Rules fired");
			}
		});
		btnNewButton_4.setBounds(10, 11, 145, 54);
		fireRules.add(btnNewButton_4);
		
		JScrollPane factsScrollPane = new JScrollPane();
		GridBagConstraints gbc_factsScrollPane = new GridBagConstraints();
		gbc_factsScrollPane.fill = GridBagConstraints.BOTH;
		gbc_factsScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_factsScrollPane.gridx = 1;
		gbc_factsScrollPane.gridy = 0;
		factInputAndWorkingMemory.add(factsScrollPane, gbc_factsScrollPane);
		
		JLabel lblFacts = new JLabel("Facts:");
		factsScrollPane.setColumnHeaderView(lblFacts);
		
		factsTextArea = new JTextArea();
		factsTextArea.setEditable(false);
		factsScrollPane.setViewportView(factsTextArea);
		
		JScrollPane argumentsScrollPane = new JScrollPane();
		GridBagConstraints gbc_argumentsScrollPane = new GridBagConstraints();
		gbc_argumentsScrollPane.fill = GridBagConstraints.BOTH;
		gbc_argumentsScrollPane.gridx = 2;
		gbc_argumentsScrollPane.gridy = 0;
		factInputAndWorkingMemory.add(argumentsScrollPane, gbc_argumentsScrollPane);
		
		argumentsTextArea = new JTextArea();
		argumentsTextArea.setEditable(false);
		argumentsScrollPane.setViewportView(argumentsTextArea);
		
		JLabel lblArguments = new JLabel("Arguments:");
		argumentsScrollPane.setColumnHeaderView(lblArguments);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(747, 0, 192, 354);
		frmDefeasibleArgumentation.getContentPane().add(scrollPane);
		
		JLabel lblResults = new JLabel("Results:");
		scrollPane.setColumnHeaderView(lblResults);
		
		resultsTextArea = new JTextArea();
		resultsTextArea.setEditable(false);
		scrollPane.setViewportView(resultsTextArea);
		
		frmDefeasibleArgumentation.setSize(964, 397);
		
		JMenuBar menuBar = new JMenuBar();
		frmDefeasibleArgumentation.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmPrintToDb = new JMenuItem("Print to DB");
		mntmPrintToDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cont.printToDataBase();
			}
		});
		mnFile.add(mntmPrintToDb);
		
		JMenuItem mntmReadFromDb = new JMenuItem("Read from DB");
		mntmReadFromDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cont.readFromDataBase();
			}
		});
		mnFile.add(mntmReadFromDb);
	}
	
	private void resetArgCreation(){
		premises=new ArrayList<>();
		conclusions=new ArrayList<>();
		expressionField.setText("");
		argNameField.setText("");
	}
	
	private void updateDisplay(){
		premisesTextArea.setText("");
		for(String i:premises){
			premisesTextArea.append(i);
			premisesTextArea.append("\n");
		}
		conclusionsTextArea.setText("");
		for(String i:conclusions){
			conclusionsTextArea.append(i);
			conclusionsTextArea.append("\n");
		}
		
		updateWorkingMemoryLists();
	}
	
	private void updateWorkingMemoryLists(){
		cont.updateListsFromWorkingMemory();
	}
	
	public void printFacts(ArrayList<String> facts){
		resetFactsTextArea();
		for(String i:facts){
			writelnOnFactsTextArea(i);
		}
	}
	
	public void printArguments(ArrayList<String> arguments){
		resetArgumentsTextArea();
		for(String i:arguments){
			writelnOnArgumentsTextArea(i);
		}
	}
	
	public void printResults(ArrayList<String> results){
		resetResultsTextArea();
		for(String i:results){
			writelnOnResultsTextArea(i);
		}
	}
	
	private void resetResultsTextArea(){
		resultsTextArea.setText("");
	}
	
	private void writelnOnResultsTextArea(String text){
		resultsTextArea.append(text);
		resultsTextArea.append("\n");
	}
	
	private void resetFactsTextArea(){
		factsTextArea.setText("");
	}
	
	private void writelnOnFactsTextArea(String text){
		factsTextArea.append(text);
		factsTextArea.append("\n");
	}
	
	private void resetArgumentsTextArea(){
		argumentsTextArea.setText("");
	}
	
	private void writelnOnArgumentsTextArea(String text){
		argumentsTextArea.append(text);
		argumentsTextArea.append("\n");
	}
	
	public void setVisible(boolean visible){
		frmDefeasibleArgumentation.setVisible(visible);
	}
	public void rePaint(){
		frmDefeasibleArgumentation.repaint();
	}
}
