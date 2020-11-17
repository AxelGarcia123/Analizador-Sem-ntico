package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import modelo.LinkedList;
import modeloAutomata.AutomataEntero;
import modeloAutomata.AutomataIdentificador;
import modeloAutomata.AutomataReal;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;

import archivos.EscribirFichero;

public class VistaPrincipal extends JFrame {

	private JPanel contentPane;
	private Toolkit tool;
	private File fichero;
	private HashMap<String, LinkedList> tokens;
	private HashMap<String, String> simbolos;
	private JTextPane editPane;
	private AutomataIdentificador automataIdentificador;
	private AutomataReal automataReal;
	private AutomataEntero automataEntero;
	private List<String> toke;
	private List<String> identificadores;
	private EscribirFichero generarFichero;
	private JEditorPane editorPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VistaPrincipal frame = new VistaPrincipal();
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
	public VistaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tool = getToolkit();
		setBounds((int) tool.getScreenSize().width / 2 - 350, (int) tool.getScreenSize().height / 2 - 235, 700, 470);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(44, 198, 197));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(20, 20));
		setContentPane(contentPane);

		tokens = new HashMap<String, LinkedList>();
		simbolos = new HashMap<String, String>();
		automataEntero = new AutomataEntero();
		automataIdentificador = new AutomataIdentificador();
		automataReal = new AutomataReal();
		toke = new ArrayList<>();
		identificadores = new ArrayList<>();
		identificadores.add("Indentificador\tvalor");

		try {
			llenarNodos();
			cargarSimbolos();
		} catch (IOException e) {
			e.printStackTrace();
		}

		editorPane = new JEditorPane();
		editorPane.setPreferredSize(new Dimension(106, 200));

		JScrollPane scrollPane = new JScrollPane(editorPane);
		scrollPane.setPreferredSize(new Dimension(108, 120));
		contentPane.add(scrollPane, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("                ");
		contentPane.add(lblNewLabel, BorderLayout.WEST);

		JLabel label = new JLabel("                ");
		contentPane.add(label, BorderLayout.EAST);

		JScrollPane scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, BorderLayout.CENTER);

		editPane = new JTextPane();
		scrollPane_1.setViewportView(editPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(20, 15));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		menuBar.setBackground(new Color(176, 224, 230));
		panel.add(menuBar, BorderLayout.WEST);
		menuBar.setBorder(UIManager.getBorder("RadioButton.border"));

		JMenu mnNewMenu = new JMenu("File");

		mnNewMenu.setFont(new Font("Microsoft JhengHei UI Light", Font.PLAIN, 16));
		menuBar.add(mnNewMenu);

		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
				chooser.setFileFilter(filter);
				int seleccion = chooser.showOpenDialog(null);
				if (seleccion == JFileChooser.APPROVE_OPTION) {
					fichero = chooser.getSelectedFile();
					FileReader fr;
					try {
						fr = new FileReader(fichero);
						BufferedReader bf = new BufferedReader(fr);
						String linea ="";
						String aux ="";

						while(linea!= null){

							linea = bf.readLine();
							if(linea!=null){
								aux += linea+"\r\n";
							}
						}

						if(aux!=null){
							editPane.setText(aux);
						}


						fr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				} else {

				}
			}
		});
		mntmAbrir.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 16));
		mnNewMenu.add(mntmAbrir);

		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		mntmSalir.setFont(new Font("Microsoft New Tai Lue", Font.PLAIN, 16));
		mnNewMenu.add(mntmSalir);

		JButton btnAnalizar = new JButton("Analizar");
		btnAnalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String cadena = editPane.getText();
				enumerarLineas();

				String[] lineas = cadena.split("\n");
				int cont;
				for (int i = 0; i < lineas.length; i++) {
					String[] tabuladores = lineas[i].split("\t");
					for (int j = 0; j < tabuladores.length; j++) {
						String[] espacios = tabuladores[j].split(" ");
						for (int k = 0; k < espacios.length; k++) {
							cont=0;
							espacios[k]=espacios[k].replace(" ", "").replace("\r", "").replace("\t", "");
							if (!espacios[k].isEmpty()) {
								List<String> cad = new ArrayList<>();
								String status = "";
								while (!espacios[k].isEmpty()) {
									if(cont==0) {
										cad.add(Character.toString(espacios[k].charAt(cont)));
										if (esLetra(cad)) {
											status="Identificador";
										}else if(esNumero(cad)){
											status="Numero";
										}else {
											try {
												simbolos.get(Character.toString(espacios[k].charAt(cont))).toString();
												status="simbolo aceptado";
												toke.add("("+cad.get(0)+" , tk_"+cad.get(0)+")");
											} catch (Exception e) {
												status = "Error";
											}
										}
										cont++;
										if(cont<espacios[k].length() && status.equals("simbolo aceptado")) {
											espacios[k] = espacios[k].substring(1);
											cad = new ArrayList<>();
											cont = 0;
										}

									}else {
										if(cont<espacios[k].length()) {
											if (status.equals("Numero")) {
												if (String.valueOf(espacios[k].charAt(cont)).equals(".")) {
													cad.add(Character.toString(espacios[k].charAt(cont)));
													cont++;
												}else {
													try {
														simbolos.get(Character.toString(espacios[k].charAt(cont))).toString();
														String cade = "";
														for (String string : cad) {
															cade+=string;
														}
														String resultado = evaluar(cad, status);
														if (!resultado.equalsIgnoreCase("Simbolo")) {
															if (!resultado.equalsIgnoreCase("Error")) {
																if (resultado.equalsIgnoreCase("Identificador") && !identificadores.contains(cade)) {
																	identificadores.add(cade+"\t ");
																}
																toke.add("("+cade+" , tk_"+cade+")");
															}else {
																String texto = editorPane.getText();
																texto+="Error en la linea :"+(i+1)+"\n";
																editorPane.setText(texto);
															}
														}
														if(cont<espacios[k].length()) {
															espacios[k] = espacios[k].substring(cont);
															cad = new ArrayList<>();
															cont = 0;
														}
													} catch (NullPointerException e) {
														cad.add(Character.toString(espacios[k].charAt(cont)));
														cont++;
													}
												}

											}else {
												if (simbolos.get(Character.toString(espacios[k].charAt(cont)))!=null) {
													String resultado = evaluar(cad, status);
													String cade = "";
													for (String string : cad) {
														cade+=string;
													}
													if (!resultado.equalsIgnoreCase("Simbolo")) {
														if (!resultado.equalsIgnoreCase("Error")) {
															if (resultado.equalsIgnoreCase("Identificador") && !identificadores.contains(cade)) {
																identificadores.add(cade);

															}
															toke.add("("+cade+" , tk_"+cade+")");
														}else {
															String texto = editorPane.getText();
															texto+="Error en la linea :"+(i+1)+"\n";
															editorPane.setText(texto);
														}
													}

													if(cont<espacios[k].length()) {
														espacios[k] = espacios[k].substring(cont);
														cad = new ArrayList<>();
														cont = 0;
													}
												}else {
													cad.add(Character.toString(espacios[k].charAt(cont)));
													cont++;
												}
											}
										}
										if(cont==espacios[k].length()){
											String cade = "";
											for (String string : cad) {
												cade+=string;
											}
											String resultado = evaluar(cad, status);
											if (!resultado.equalsIgnoreCase("Simbolo")) {
												if (!resultado.equalsIgnoreCase("Error")) {
													if (resultado.equalsIgnoreCase("Identificador") && !identificadores.contains(cade)) {
														identificadores.add(cade+"\t ");

													}
													toke.add("("+cade+" , tk_"+cade+")");
												}else {
													String texto = editorPane.getText();
													texto+="Error en la linea :"+(i+1)+"\n";
													editorPane.setText(texto);

												}
											}
											espacios[k]="";
										}
									}
								}

							}
						}
					}
				}


				generarFichero = new EscribirFichero();


				if(!identificadores.isEmpty()){

					if(generarFichero.eliminar()){	   
						for (String string : identificadores) {
							generarFichero.crearListasId(string);
						}
						generarFichero.crearPDF();
					}

				}

				if(!toke.isEmpty()){
					for (String string : toke) {
						generarFichero.crearListasTokens(string);
					}
				}


				System.out.println(toke);
				System.out.println(identificadores);
			} 


			//				
			//				if (tokens.get("I").getToken(arreglo)) 
			//					System.out.println("Jalo");
			//				else 
			//					System.out.println("No jalo");
			//			


		});
		btnAnalizar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnAnalizar.setBorder(UIManager.getBorder("RadioButton.border"));
		btnAnalizar.setContentAreaFilled(false);
		panel.add(btnAnalizar, BorderLayout.EAST);

		JLabel lblAnalizadorLxico = new JLabel("Analizador L\u00E9xico");
		lblAnalizadorLxico.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 22));
		lblAnalizadorLxico.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblAnalizadorLxico, BorderLayout.CENTER);

	}

	public void llenarNodos() throws IOException {
		String cadena;
		FileReader f = new FileReader("src/recursos/PALABRASRESERVADAS.txt");
		BufferedReader b = new BufferedReader(f);
		while ((cadena = b.readLine()) != null) {
			LinkedList nodo = new LinkedList();
			int t = 0;
			while (t < cadena.length()) {
				nodo.addNewElement(String.valueOf(cadena.charAt(t)));
				t++;
			}
			tokens.put(cadena, nodo);
		}
		b.close();
	}

	public boolean esNumero(List<String> cad) {
		for (int i = 0; i < 10; i++) {
			if (cad.get(0).startsWith(String.valueOf(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean esLetra(List<String> vector) {
		List<String> alfabetoLetras = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
				"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

		for (String valor : alfabetoLetras) {
			if (vector.get(0).startsWith(valor)) {
				return true;
			}
		}
		return false;
	}

	public void cargarSimbolos() throws IOException {
		String cadena;
		FileReader f = new FileReader("src/recursos/SIMBOLOSACEPTADOS.txt");
		BufferedReader b = new BufferedReader(f);
		while ((cadena = b.readLine()) != null) {
			simbolos.put(cadena, cadena);
		}
		b.close();
	}

	public String evaluar(List<String> vector, String estado){

		if (estado.equalsIgnoreCase("Identificador")) {
			String clave="";
			automataIdentificador = new AutomataIdentificador();
			for (int i = 0; i < vector.size(); i++) {
				clave+=vector.get(i);
				automataIdentificador.makeTransition(vector.get(i));			
			}
			if (automataIdentificador.getCursor().isAceptacion()) {
				return "Identificador";
			}else {
				try {
					tokens.get(clave).getToken(vector);
					return "Palabra reservada";
				} catch (NullPointerException e) {
					return "Error";
				}
			}
		}else if (estado.equalsIgnoreCase("numero")) {
			boolean bandera=false;
			automataReal = new AutomataReal();
			for (int i = 0; i < vector.size(); i++) {
				automataReal.makeTransition(vector.get(i));
			}
			if (automataReal.getCursor().isAceptacion()) {
				return "Numero Real";
			}else {
				automataEntero = new AutomataEntero();
				for (int i = 0; i < vector.size(); i++) {
					automataEntero.makeTransition(vector.get(i));
				}
				if (automataEntero.getCursor().isAceptacion()) {
					return "Numero Entero";
				}else {
					return "Error";
				}
			}
		}else {
			return "Simbolo";
		}
	}

	public void enumerarLineas() {
		String[] lineas = editPane.getText().split("\n");
		String remplazo="";
		for (int i = 0; i < lineas.length; i++) {
			remplazo+=(i+1)+"  "+lineas[i];
		}
		editPane.setText(remplazo);
	}

}
