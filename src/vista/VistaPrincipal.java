package vista;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import analisisSintactico.ArbolDerivacion;
import analisisSintactico.Main;
import analisisSintactico.Pila;
import analisisSintactico.TablaValores;
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
import java.util.Map.Entry;

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
	private Pila pila;
	private HashMap<String, String>reglas;
	private ArbolDerivacion arbol;
	private String listaTokens = "";
	private List<Integer> erroresDelAnalizador;
	private List<Integer> erroresDelAnalizadorSemantico;
	String reglaInicioFin = "";

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
		identificadores.add("Indentificador\tValor\tTipo");
		pila = new Pila();
		reglas = new HashMap<String, String>();
		arbol = new ArbolDerivacion();
		erroresDelAnalizador = new ArrayList<Integer>();
		erroresDelAnalizadorSemantico = new ArrayList<Integer>();
		llenarReglas();

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
				int[] errores = new int[lineas.length];
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
												errores[i] = 0;
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
																errores[i] = 0;
															}else {
																String texto = editorPane.getText();
																texto+="Error en la linea :"+(i+1)+"\n";
																errores[i] = 1;
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
															errores[i] = 0;
														}else {
															String texto = editorPane.getText();
															texto+="Error en la linea :"+(i+1)+"\n";
															errores[i] = 1;
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
													errores[i] = 0;
												}else {
													String texto = editorPane.getText();
													texto+="Error en la linea :"+(i+1)+"\n";
													errores[i] = 1;
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
						listaTokens = generarFichero.crearListasTokens(string);
					}
				}


				System.out.println(toke);
				System.out.println(identificadores);


				try {
					editorPane.setText("");
					analizadorSintactico(lineas, errores);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(erroresDelAnalizador.size());
				String todosLosErrores = "";
				for (Integer error: erroresDelAnalizador) 
					todosLosErrores += editorPane.getText() + "Error sintáctico en la línea: "+ error + "\n";
				
				try {
					editorPane.setText("");
					cargarArbol("src\\ficheros\\arbolDerivacion.txt");
					revisarArbol();
					String arbolDerivacion = "";
					for(int j = 0; j <arbol.getSize(); j++) {
						if(j != arbol.getSize() - 1)
							arbolDerivacion += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j) + "\n";
						else
							arbolDerivacion += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j);
					}
					generarFichero.crearListasArbolDerivacion(arbolDerivacion);
					arbol.vaciarArbol();
				} catch (Exception e) {
					// TODO: handle exception
				}
				for (Integer error: erroresDelAnalizadorSemantico)
					todosLosErrores += editorPane.getText() + "Error semántico en la línea: "+ error + "\n";
				editorPane.setText(todosLosErrores);
				if(!reglaInicioFin.isEmpty())
					editorPane.setText(editorPane.getText() + reglaInicioFin);
				else {
//					BEGIN {
//						INTEGER a1;
//						} END
					try {
						cargarArbol("src\\ficheros\\arbolDerivacion.txt");
						arbol.addNumero(arbol.getSize());
						arbol.addLexema("Cuerpo");
						arbol.addPadre(0);
						for(int i = arbol.getSize(); i > 0; i--) {
							if(!arbol.getDatoLexema(i - 1).equals("Cuerpo") && !arbol.getDatoLexema(i - 1).equals("Inicio") && !arbol.getDatoLexema(i - 1).equals("Fin") && arbol.getDatoPadre(i - 1) == 0) 
								arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
							else
								continue;
						}
						arbol.addNumero(arbol.getSize());
						arbol.addLexema("Programa");
						arbol.addPadre(0);
						for(int i = arbol.getSize(); i > 0; i--) {
							if(!arbol.getDatoLexema(i - 1).equals("Programa") && arbol.getDatoPadre(i - 1) == 0) 
								arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
							else
								continue;
						}
						String arbolFinal = "";
						for(int j = 0; j <arbol.getSize(); j++) {
							if(j != arbol.getSize() - 1)
								arbolFinal += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j) + "\n";
							else
								arbolFinal += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j);
						}
						generarFichero.crearListasArbolDerivacion(arbolFinal);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
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


	/////////////////////////////////////////////DESDE AQUÍ///////////////////////////////////////////////////////////////
	/////////////////////////////////////////////COMIENZA EL//////////////////////////////////////////////////////////////
	/////////////////////////////////////////ANALIZADOR SINTÁCTICO////////////////////////////////////////////////////////

	public void analizadorSintactico(String[] cadena, int[] errores) throws IOException {
		/** INICIO DE TODO EL ALGORITMO*/
		boolean flag = true;
		for(int i = 0; i < cadena.length; i++) {
			cadena[i] = cadena[i].replace(" ", "").replace("\r", "").replace("\t", "");
			pila.limpiarPila();
			flag = false;
			/* En este IF queremos decir que si en el arreglo de errores, en la posición "i" hay un cero, vamos
			 * a hacer el procedimiento del analizador sintáctico.
			 * En caso de que haya un error desde el programa anterior, se evitará este proceso*/
			if(errores[i] == 0) {
				String token = "";
				FileReader f = new FileReader(listaTokens);
				BufferedReader b = new BufferedReader(f);
				while((token = b.readLine()) != null) {
					for(int j = 0; j < cadena[i].length(); j++) {
						if(cadena[i].charAt(j) == ' ') {
							break;
						}
						else {
							//BEGIN		//BEGIN 
							if(cadena[i].charAt(j) == token.charAt(j + 1))
								flag = true;
							else {
								if(token.charAt(j + 1) == ' ') {
									flag = true;
									break;
								}
								else {
									flag = false;
									break;
								}
							}
						}
					}
					if(flag)
						break;
					else
						continue;
				}
				//Esta parte quiere decir que sí ha encontrado coincidencia con alguna palabra reservada
				if(flag) {
					switch(token.substring(1, token.indexOf(" "))) {
					case "BEGIN":
						if(reglaBegin(cadena[i]))
							arbolDerivacion();
						else
							erroresDelAnalizador.add(i + 1);
						break;
					case "}":
						if(reglaEnd(cadena[i])) {
							arbolDerivacion();
						}
						else
							erroresDelAnalizador.add(i + 1);
						break;
					case "INTEGER":
						if(declaracionFinal(cadena[i].replace(" ", ""), token))
							arbolDeTipo();
						else 
							erroresDelAnalizador.add(i + 1);
						break;
					case "REAL":
						if(declaracionFinal(cadena[i].replace(" ", ""), token))
							arbolDeTipo();
						else
							erroresDelAnalizador.add(i + 1);
						break;
					case "READ":
						if(funcion(cadena[i].replace(" ", ""), token.substring(1, token.indexOf(" "))))
							arbolDeTipo();
						else
							erroresDelAnalizador.add(i + 1);
						break;
					case "WRITE":
						if(funcion(cadena[i].replace(" ", ""), token.substring(1, token.indexOf(" "))))
							arbolDeTipo();
						else
							erroresDelAnalizador.add(i + 1);
						break;
					default:
						/**PENDIENTE PARA IDENTIFICADORES*/
						if(revisarArchivos("src\\ficheros\\identificador.txt", token.substring(1, token.indexOf(" ")))) {
							if(cadena[i].charAt(token.substring(1, token.indexOf(" ")).length()) == '='){
								if(resultadoFinal(cadena[i].replace(" ", "")))
									arbolDeTipo();
								else {
									pila.limpiarPila();
									if(asignacionSimple(cadena[i])) {
										arbolDeTipo();
									}
									else
										erroresDelAnalizador.add(i + 1);
								}
							}
							else 
								erroresDelAnalizador.add(i + 1);
						}
						else 
							erroresDelAnalizador.add(i + 1);
					}
				}
				else 
					erroresDelAnalizador.add(i + 1);
				b.close();
			}
			else
				continue;
		}
		String arbolDerivacion = "";
		for(int j = 0; j <arbol.getSize(); j++) {
			if(j != arbol.getSize() - 1)
				arbolDerivacion += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j) + "\n";
			else
				arbolDerivacion += arbol.getDatoNumero(j) + "\t\t" + arbol.getDatoLexema(j) + "\t\t"+ arbol.getDatoPadre(j);
		}
		generarFichero.crearListasArbolDerivacion(arbolDerivacion);
		arbol.vaciarArbol();
	}
	
	public boolean asignacionSimple(String linea) throws IOException {
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");
		for(int k = 0; k < 4; k++) {
			if(k == 3)
				break;
			else {
				String asig = buscarListaToken(pila.getLastPositionCadena());
				if(!asig.isEmpty()) {
					if(pila.getLastPositionPila().equals("0")) {
						pila.addPila(asig);
						pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(asig) + asig.length()));
						pila.addAccion("Llevar a pila");
					}
					else {
						pila.addPila(pila.getLastPositionPila() + asig);
						try {
							pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(asig) + asig.length()));
						} catch (IndexOutOfBoundsException e) {
							return false;
						}
						pila.addAccion("Llevar a pila");
					}
				}
				else
					break;
			}
		}
		if(pila.getLastPositionCadena().equals(";")) {
			if(asignacion(pila.getLastPositionPila())) {
				pila.getPila().set(pila.getSize() - 1, pila.getLastPositionPila() + pila.getDatoCadena(pila.getSize() - 2));
				String auxAccion = pila.getLastPositionAccion();
				pila.getAccion().set(pila.getSize() - 1, "Llevar a pila");
				pila.addPila("Asignacion");
				pila.addCadena("$");
				pila.addAccion(auxAccion + ";");
				for(int i = 0; i < pila.getSize(); i++)
					System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
				return true;
			}
			else {
				System.out.println("ES CUANDO EL MÉTODO ASIGNACIÓN NO FUNCIONA");
				return false;
			}
		}
		else {
			System.out.println("ES CUANDO NO HAY ;");
			return false;
		}
	}

	/**ESTA REGLA LA PUEDO REDUCIR DEMASIADO O MÁS O MENOS*/
	public boolean reglaBegin(String linea) throws IOException {
		//BEGIN{
		linea.replace(" ", "");
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");		//BEGIN{			//Sustraer		//De BEGIN{				//BEGIN
		//BEGIN
		pila.addPila(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf("BEGIN"), pila.getLastPositionCadena().indexOf("BEGIN") + "BEGIN".length()));
		pila.addCadena(pila.getLastPositionCadena().replace(pila.getLastPositionPila(), ""));//{
		pila.addAccion("Llevar a pila");

		String token = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		while((token = b.readLine()) != null) {
			for(int i = 0; i < pila.getLastPositionCadena().length(); i++) {
				if(pila.getLastPositionCadena().charAt(i) == token.charAt(i + 1))
					flag = true;
				else {
					if(token.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
					else {
						flag = false;
						break;
					}
				}
			}
			if(flag)
				break;
			else
				continue;
		}
		b.close();
		if(!token.isEmpty()) {
			pila.addPila(pila.getLastPositionPila() + " " + pila.getLastPositionCadena());
			pila.addCadena(pila.getLastPositionCadena().replace(String.valueOf(token.charAt(1)), ""));
			pila.addAccion("Llevar a pila");
			if(pila.getLastPositionCadena().isEmpty()) {
				pila.getCadena().set(pila.getSize() - 1, "$");
				if(pila.getLastPositionPila().equals(reglas.get("Inicio"))) {//BEGIN{
					pila.addPila("Inicio");
					pila.addCadena("$");
					pila.addAccion("Inicio -> BEGIN {");
					for(int i = 0; i < pila.getSize(); i++)
						System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
					return true;
				}
				else {
					System.out.println("La cadena no está correcta porque es un elemento que no cumple con la regla de INICIO");
					return false;
				}
			}
			else {
				System.out.println(pila.getLastPositionCadena().length());
				System.out.println("Última posición de la cadena:"+pila.getLastPositionCadena());
				System.out.println("La cadena no es correcta porque aún hay más datos y debería de estar solamente el {");
				return false;
			}
		}
		else {
			System.out.println("Caracter no encontrado. Cadena incorrecta");
			return false;
		}
	}

	/**PILA		CADENA		ACCION
	 * 			}END				
	 * }		END			LLEVAR A PILA
	 * @throws IOException */
	public boolean reglaEnd(String linea) throws IOException {
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");
		pila.addPila(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf("}"), "}".length()));
		pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf("}") + "}".length()));
		pila.addAccion("Llevar a pila");
		String siguientePalabra = buscarListaToken(pila.getLastPositionCadena());
		if(!siguientePalabra.isEmpty()) {
			pila.addPila(pila.getLastPositionPila() + " " + siguientePalabra);
			pila.addCadena(pila.getLastPositionCadena().replace(siguientePalabra, ""));
			pila.addAccion("Llevar a pila");
			if(pila.getLastPositionCadena().isEmpty()) {
				pila.getCadena().set(pila.getCadena().size() - 1, "$");
				if(reglas.get("Fin").equals(pila.getLastPositionPila())) {
					pila.addPila("Fin");
					pila.addCadena("$");
					pila.addAccion("Fin -> } END");
					for(int i = 0; i < pila.getSize(); i++)
						System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
		else
			return false;
	}

	public boolean declaracionFinal(String linea, String token) throws IOException {
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");
		/*ESTO SE PUEDE SIMPLIFICAR DEMASIADO.
		 * TENERLO EN CUENTA*/
		pila.addPila(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(token.substring(1, token.indexOf(" "))), pila.getLastPositionCadena().indexOf(token.substring(1, token.indexOf(" "))) + token.substring(1, token.indexOf(" ")).length()));
		pila.addCadena(pila.getLastPositionCadena().replace(pila.getLastPositionPila(), ""));
		pila.addAccion("Llevar a pila");
		pila.addPila("Tipo");
		pila.addCadena(pila.getLastPositionCadena());
		pila.addAccion("Tipo -> "+ token.substring(1, token.indexOf(" ")));
		while(pila.getLastPositionCadena() != "$") {
			String tokenAux = "";
			FileReader f = new FileReader(listaTokens);
			BufferedReader b = new BufferedReader(f);
			boolean flag = true;
			while((tokenAux = b.readLine()) != null) {
				for(int i = 0; i < pila.getLastPositionCadena().length(); i++) {
					if(pila.getLastPositionCadena().charAt(i) == tokenAux.charAt(i + 1))
						flag = true;
					else {
						if(tokenAux.charAt(i + 1) == ' ') {
							flag = true;
							break;
						}
						else {
							flag = false;
							break;
						}
					}
				}
				if(flag)
					break;
				else
					continue;
			}
			if(!(tokenAux == null)) {
				if(tokenAux.substring(1, tokenAux.indexOf(" ")).equals(",") || tokenAux.substring(1, tokenAux.indexOf(" ")).equals(";"))
					pila.addPila(pila.getLastPositionPila() + tokenAux.substring(1, tokenAux.indexOf(" ")));
				else 
					pila.addPila(pila.getLastPositionPila() + " " + tokenAux.substring(1, tokenAux.indexOf(" ")));
				pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length(), pila.getLastPositionCadena().length()));
				pila.addAccion("Llevar a pila");
				if(pila.getLastPositionCadena().isEmpty()) {
					pila.getCadena().set(pila.getSize() - 1, "$");
				}
			}
			else
				return false;
		}
		if(declaracion()) {
			System.out.println("DECLARACIÓN FINAL: "+ pila.getLastPositionPila());
			System.out.println(reglas.get("Declaracion Final").equals(pila.getLastPositionPila()));
			if(reglas.get("Declaracion Final").equals(pila.getLastPositionPila())) {
				pila.addPila("Declaracion Final");
				pila.addCadena("$");
				pila.addAccion("Declaracion Final -> Tipo Declaracion;");
				for(int i = 0; i < pila.getSize(); i++)
					System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
				return true;
			}
			else
				return false;
		}
		else 
			return false;
	}

	public boolean declaracion() throws IOException {
		boolean flag = false;
		String auxPila = pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(" "));
		auxPila = auxPila.replace(" ", "");
		String aux = "";
		for(int i = 0; i < auxPila.length(); i++) {
			if(auxPila.charAt(i) != ',' && auxPila.charAt(i) != ';') 
				aux += auxPila.charAt(i);
			else {
				if(auxPila.charAt(i) == ',') {
					if(!iterarContenidoReglas(aux)) {
						if(asignacion(aux)) {
							aux = "";
							continue;
						}
						else {
							if(revisarArchivos("src\\ficheros\\identificador.txt", aux)) {
								pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(aux)) + "Declaracion"+ pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(aux) + aux.length()));
								pila.addCadena("$");
								pila.addAccion("Declaracion -> "+ aux);
							}
							else
								return false;
						}
					}
					else {
						flag = true;
						if(aux.equals("Declaracion")) {
							if(auxPila.substring(auxPila.indexOf("Declaracion") + aux.length(), aux.length() + 1).equals(",")) {
								String word = "";
								for(int j = auxPila.indexOf(",") + 1; j < auxPila.length(); j++) {
									if(auxPila.charAt(j) != ',' && auxPila.charAt(j) != ';') 
										word += auxPila.charAt(j);
									else 
										break;
								}
								if(word.equals("Declaracion")) {
									String result = pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Declaracion, Declaracion")))) + "Declaracion" +/*Segunda parte*/ 
											pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Declaracion, Declaracion") + "Declaracion, Declaracion".length());
									pila.addPila(result);
									pila.addCadena("$");
									pila.addAccion("Declaracion -> Declaracion, Declaracion");
									break;
								}
								else if(word.equals("Asignacion")) {
									pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(word)) + "Declaracion" + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(word) + word.length()));
									pila.addCadena("$");
									pila.addAccion("Declaracion -> Asignacion");
								}
								else {
									System.out.println("Esa palabra no existe "+ aux);
									return false;
								}
							}
						}
						else if(aux.equals("Asignacion")) {
							pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(aux)) + "Declaracion"+ pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(aux) + aux.length()));
							System.out.println("Posicion de la pila: "+ pila.getLastPositionPila());
							pila.addCadena("$");
							pila.addAccion("Declaracion -> Asignacion");
						}
						else {
							System.out.println("Esa palabra no existe "+ aux);
							return false;
						}
					}
					if(flag)
						break;
				}
				else {
					if(!iterarContenidoReglas(aux)) {
						if(asignacion(aux)) {
							aux = "";
							continue;
						}
						else {
							if(revisarArchivos("src\\ficheros\\identificador.txt", aux)) {
								pila.addPila(pila.getLastPositionPila().replace(aux, "Declaracion"));
								pila.addCadena("$");
								pila.addAccion("Declaracion -> "+ aux);
							}
							else {
								System.out.println("Hay un error");
								return false;
							}
						}
					}
					else {
						flag = true;
						if(pila.getLastPositionPila().equals(reglas.get("Declaracion Final"))) {
							System.out.println("Cumple con la regla");
							return true;
						}
						else if(aux.equals("Asignacion")) {
							pila.addPila(pila.getLastPositionPila().replace("Asignacion", "Declaracion"));
							pila.addCadena("$");
							pila.addAccion("Declaracion -> Asignacion");
							if(pila.getLastPositionPila().equals(reglas.get("Declaracion Final"))) {
								System.out.println("Va a dar true");
								return true;
							}
								
							else
								return false;
						}
						else
							return false;
					}
				}
				aux = "";
			}
		}
		if(pila.getLastPositionPila().contains(";")) {
			if(pila.getLastPositionPila().equals("Tipo Declaracion"))
				return true;
			else 
				return declaracion();
		}
		return
				false;
	}

	public boolean asignacion(String linea) throws IOException {
		if(pila.getSize() != 0) {
			int count = 0;
			String nuevaCadena = "";
			String aux = "";
			if(linea.contains("=")) {
				for(int i = 0; i < linea.length(); i++) {
					if(linea.charAt(i) != '=') {
						aux += linea.charAt(i);
						continue;
					}
					else {
						if(revisarArchivos("src\\ficheros\\identificador.txt", aux)) 
							nuevaCadena += "Variable ";
						else
							return false;
					}
					if(linea.charAt(i) == '=') {
						nuevaCadena += "= ";
						count = i;
						break;
					}
					else {
						System.out.println("ES CUANDO NO HA ENCONTRADO UN =");
						return false;
					}
				}

				String auxLinea = linea.substring(count + 1, linea.length());
				if(isNumeric(auxLinea)) {
					nuevaCadena += "Numero";
					if(nuevaCadena.equals(reglas.get("Asignacion"))) {
						pila.addPila(pila.getLastPositionPila().replace(aux + " = "+ auxLinea, "Asignacion"));
						pila.addCadena("$");
						pila.addAccion("Asignacion -> "+ aux + " = "+ auxLinea);
						return true;
					}
					else {
						System.out.println("ES CUANDO NO CUMPLE CON LA REGLA DE ASIGNACIÓN");
						return false;
					}
				}
				else if(isDecimal(auxLinea)) {
					nuevaCadena += "Numero";
					if(nuevaCadena.equals(reglas.get("Asignacion"))) {
						pila.addPila(pila.getLastPositionPila().replace(aux + " = "+ auxLinea, "Asignacion"));
						pila.addCadena("$");
						pila.addAccion("Asignacion -> "+ aux + " = "+ auxLinea);
						return true;
					}
					else {
						System.out.println("ES CUANDO NO CUMPLE CON LA REGLA DE ASIGNACIÓN. Segunda");
						return false;
					}
				}
				/**PENDIENTE PARA CUANDO NO SEA NI DECIMAL NI ENTERO Y CUANDO LA PILA TENGA DATOS YA ADENTRO*/
				else {
					System.out.println("Entra aquí en el else vacio");
				}
			}
		}
		return false;
	}

	public boolean resultadoFinal(String linea) throws IOException {
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");
		pila.addPila(pila.getLastPositionCadena().substring(0, pila.getLastPositionCadena().indexOf("=")));
		pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf("=")));
		pila.addAccion("Llevar a pila");
		pila.addAccion("Variable -> "+ pila.getLastPositionPila());
		pila.addPila("Variable");
		pila.addCadena(pila.getLastPositionCadena());
		while(pila.getLastPositionCadena() != "$") {
			String tokenAux = "";
			FileReader f = new FileReader(listaTokens);
			BufferedReader b = new BufferedReader(f);
			boolean flag = true;
			while((tokenAux = b.readLine()) != null) {
				for(int i = 0; i < pila.getLastPositionCadena().length(); i++) {
					if(pila.getLastPositionCadena().charAt(i) == tokenAux.charAt(i + 1)) {
						flag = true;
					}
					else {
						if(tokenAux.charAt(i + 1) == ' ') {
							flag = true;
							break;
						}
						else {
							flag = false;
							break;
						}
					}
				}
				if(flag)
					break;
				else
					continue;
			}
			if(flag) {
				if(tokenAux.substring(1, tokenAux.indexOf(" ")).equals(",") || tokenAux.substring(1, tokenAux.indexOf(" ")).equals(";") || tokenAux.substring(1, tokenAux.indexOf(" ")).equals("(") || tokenAux.substring(1, tokenAux.indexOf(" ")).equals(")") || pila.getLastPositionPila().substring(pila.getLastPositionPila().length() - 1).equals("("))
					pila.addPila(pila.getLastPositionPila() + tokenAux.substring(1, tokenAux.indexOf(" ")));
				else 
					pila.addPila(pila.getLastPositionPila() + " " + tokenAux.substring(1, tokenAux.indexOf(" ")));
				pila.addCadena(pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length(), pila.getLastPositionCadena().length()));
				pila.addAccion("Llevar a pila");
				if(pila.getLastPositionCadena().isEmpty()) {
					pila.getCadena().set(pila.getSize() - 1, "$");
				}
			}
			else
				return false;
		}
		try {
			if(operacion(pila.getLastPositionPila().substring("Variable = ".length()))) {
				for(int i = 0; i < pila.getSize(); i++)
					System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	//MÉTODO DE LA REGLA OPERACIÓN
	public boolean operacion(String linea) throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		while((tokenAux = b.readLine()) != null) {
			for(int i = 0; i < linea.length(); i++) {
				if(linea.charAt(i) == tokenAux.charAt(i + 1)) {
					flag = true;
				}
				else {
					if(tokenAux.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
					else {
						flag = false;
						break;
					}
				}
			}
			if(flag)
				break;
			else
				continue;
		}
		if(flag) {
			if(iterarContenidoReglas(tokenAux.substring(1, tokenAux.indexOf(" ")))) {
				/**CONSIDERAR CONVERTIR LA PALABRA RESERVADA A TIPO OPERACION ANTES DE EL IF QUE ESTÁ DEBAJO*/
				pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" ")))) + 
						"Tipo Operacion " + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length()));
				pila.addCadena("$");
				pila.addAccion("Tipo Operacion -> "+ tokenAux.substring(1, tokenAux.indexOf(" ")));
				if(linea.charAt(tokenAux.substring(1, tokenAux.indexOf(" ")).length()) == '(') {
					String cuerpoOperacion = linea.substring(tokenAux.substring(1, tokenAux.indexOf(" ")).length() + 1);
					if(cuerpoOperacion(cuerpoOperacion)) {
						reglas();
					}
					else
						return false;
				}
				else {
					System.out.println("Debería de entrar aquí y retornar falso");
					//ES CUANDO DESPUÉS DE LA PALABRA RESERVADA NO HAY UN PARÉNTESIS
					return false;
				}
			}
			else {
				//ES CUANDO NO HA ENCONTRADO NINGUNA PALABRA RESERVADA
				System.out.println("ES CUANDO NO HA ENCONTRADO NINGUNA PALABRA RESERVADA");
				return false;
			}
		}
		return true;
	}

	/**MÉTODO DE LA >>"REGLA CUERPO OPERACIÓN"<<*/
	public boolean cuerpoOperacion(String cuerpo) throws IOException {
		if(cuerpo.equals("Opcion));") || cuerpo.equals("Opcion);"))
			return true;
		else if(cuerpo.contains("Opcion),"))
			return cuerpoOperacion(cuerpo.substring(cuerpo.indexOf("Opcion),") + "Opcion),".length() + 1));
		else {
			String tokenAux = "";
			FileReader f = new FileReader(listaTokens);
			BufferedReader b = new BufferedReader(f);
			boolean flag = true;
			while((tokenAux = b.readLine()) != null) {
				for(int i = 0; i < cuerpo.length(); i++) {
					if(cuerpo.charAt(i) == tokenAux.charAt(i + 1)) {
						flag = true;
					}
					else {
						if(tokenAux.charAt(i + 1) == ' ') {
							flag = true;
							break;
						}
						else {
							flag = false;
							break;
						}
					}
				}
				if(flag)
					break;
				else
					continue;
			}
			if(flag) {
				if(revisarArchivos("src\\ficheros\\identificador.txt", tokenAux.substring(1, tokenAux.indexOf(" ")))) {
					pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" ")))) + 
							"Factor" + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length()));
					pila.addCadena("$");
					pila.addAccion("Factor -> "+ tokenAux.substring(1, tokenAux.indexOf(" ")));
					if(iterarContenidoReglas("Factor")) {
						pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf("Factor")) + "Opcion" + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Factor") + "Factor".length()));
						pila.addCadena("$");
						pila.addAccion("Opcion -> Factor");
						if(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length(), (pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 1)).equals(",")) {
							String newWord = "";
							try {
								newWord = pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2).substring(0, "Tipo Operacion".length());
							} catch (StringIndexOutOfBoundsException e) {
								return cuerpoOperacion(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2));
							}
							if(newWord.equals("Tipo Operacion")) {
								newWord = pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2).substring("Tipo Operacion".length());
								return cuerpoOperacion(newWord.substring(newWord.indexOf("Opcion") + "Opcion".length() + 2));
							}
							else {
								return cuerpoOperacion(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2));
							}
						}
					}
					else {
						//ES CUANDO NO HA ENCONTRADO UN FACTOR
						return false;
					}
				}
				else if(revisarArchivos("src\\recursos\\PALABRASRESERVADAS.txt", tokenAux.substring(1, tokenAux.indexOf(" "))))
					return operacion(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" ")))));
				else {
					//IMPORTANTE
					if(isNumeric(tokenAux.substring(1, tokenAux.indexOf(" "))) || isDecimal(tokenAux.substring(1, tokenAux.indexOf(" ")))) {
						pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" ")))) + 
								"Factor" + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length()));
						pila.addCadena("$");
						pila.addAccion("Factor -> "+ tokenAux.substring(1, tokenAux.indexOf(" ")));
						if(iterarContenidoReglas("Factor")) {
							pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf("Factor")) + "Opcion" + pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Factor") + "Factor".length()));
							pila.addCadena("$");
							pila.addAccion("Opcion -> Factor");
							if(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length(), (pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 1)).equals(",")) {
								String newWord = "";
								try {
									newWord = pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2).substring(0, "Tipo Operacion".length());
								} catch (StringIndexOutOfBoundsException e) {
									return cuerpoOperacion(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2));
								}
								if(newWord.equals("Tipo Operacion")) {
									newWord = pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2).substring("Tipo Operacion".length());
									return cuerpoOperacion(newWord.substring(newWord.indexOf("Opcion") + "Opcion".length() + 2));
								}
								else {
									return cuerpoOperacion(pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("Opcion") + "Opcion".length() + 2));
								}
							}
						}
					}
					else {
						System.out.println("Es el return false");
						return false;
					}
				}
			}
			else
				return false;
		}
		return true;
	}

	public void reglas() {
		if(pila.getLastPositionPila().indexOf(reglas.get("Elementos")) != -1) {
			pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(reglas.get("Elementos"))) + "Elementos" +
					pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(reglas.get("Elementos")) + reglas.get("Elementos").length()));
			pila.addCadena("$");
			pila.addAccion("Elementos -> Opcion, Opcion");
		}

		else if(pila.getLastPositionPila().indexOf(reglas.get("Cuerpo Operacion")) != -1) {
			pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(reglas.get("Cuerpo Operacion"))) + "Cuerpo Operacion" +
					pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(reglas.get("Cuerpo Operacion")) + reglas.get("Cuerpo Operacion").length()));
			pila.addCadena("$");
			pila.addAccion("Cuerpo Operacion -> (Elementos)");
		}

		else if(pila.getLastPositionPila().indexOf(reglas.get("Operacion")) != -1) {
			pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf(reglas.get("Operacion"))) + "Operacion" +
					pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(reglas.get("Operacion")) + reglas.get("Operacion").length()));
			pila.addCadena("$");
			pila.addAccion("Operacion -> Tipo Operacion Cuerpo Operacion");
		}

		else if(pila.getLastPositionPila().indexOf("(Opcion, Operacion)") != -1) {
			pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf("(Opcion, Operacion)")) + "(Opcion, Opcion)" +
					pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("(Opcion, Operacion)") + "(Opcion, Operacion)".length()));
			pila.addCadena("$");
			pila.addAccion("Opcion -> Operacion");
		}

		else if(pila.getLastPositionPila().indexOf("(Operacion, Opcion)") != -1) {
			pila.addPila(pila.getLastPositionPila().substring(0, pila.getLastPositionPila().indexOf("(Operacion, Opcion)")) + "(Opcion, Opcion)" +
					pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf("(Operacion, Opcion)") + "(Operacion, Opcion)".length()));
			pila.addCadena("$");
			pila.addAccion("Opcion -> Operacion");
		}
		if(pila.getLastPositionPila().indexOf(reglas.get("Resultado Final")) != -1) {
			pila.addPila("Resultado Final");
			pila.addCadena("$");
			pila.addAccion("Resultado Final -> Variable = Operacion;");
		}
		else if(pila.getLastPositionPila().equals("Resultado Final")) {

		}
		else
			reglas();
	}

	//MÉTODO DE LA REGLA FUNCIÓN
	public boolean funcion(String linea, String token) throws IOException {
		pila.addPila("0");
		pila.addCadena(linea);
		pila.addAccion("0");
		while(pila.getLastPositionCadena() != "$") {
			String tokenAux = "";
			FileReader f = new FileReader(listaTokens);
			BufferedReader b = new BufferedReader(f);
			boolean flag = true;
			while((tokenAux = b.readLine()) != null) {
				for(int i = 0; i < pila.getLastPositionCadena().length(); i++) {
					if(pila.getLastPositionCadena().charAt(i) == tokenAux.charAt(i + 1)) {
						flag = true;
					}
					else {
						if(tokenAux.charAt(i + 1) == ' ') {
							flag = true;
							break;
						}
						else {
							flag = false;
							break;
						}
					}
				}
				if(flag)
					break;
				else
					continue;
			}
			if(flag) {
				String tokenAUsar = tokenAux.substring(1, tokenAux.indexOf(" "));
				if(!pila.getLastPositionPila().equals("0")) {
					pila.addPila(pila.getLastPositionPila() + pila.getLastPositionCadena().substring(pila.getLastPositionCadena().indexOf(tokenAUsar), tokenAUsar.length()));
					pila.addCadena(pila.getLastPositionCadena().replace(tokenAUsar, ""));
					pila.addAccion("Llevar a pila");
				}
				else {
					pila.addPila(tokenAux.substring(1, tokenAux.indexOf(" ")));
					pila.addCadena(pila.getLastPositionCadena().replace(tokenAUsar, ""));
					pila.addAccion("Llevar a pila");
				}
				if(pila.getLastPositionCadena().isEmpty())
					pila.getCadena().set(pila.getSize() - 1, "$");
			}
			else
				return false;
		}

		if(tipoFuncion()) {
			if(reglas.get("Funcion").equals(pila.getLastPositionPila())) {
				pila.addPila("Funcion");
				pila.addCadena("$");
				pila.addAccion("Funcion -> Tipo Funcion Cuerpo Funcion;");
				for(int i = 0; i < pila.getSize(); i++)
					System.out.println(pila.getDatoPila(i) + "\t\t\t\t" + pila.getDatoCadena(i) + "\t\t\t\t" + pila.getDatoAccion(i));
				return true;
			}
			else
				return false;
		}
		return false;
	}

	public boolean tipoFuncion() throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		while((tokenAux = b.readLine()) != null) {
			for(int i = 0; i < pila.getLastPositionPila().length(); i++) {
				if(pila.getLastPositionPila().charAt(i) == tokenAux.charAt(i + 1)) {
					flag = true;
				}
				else {
					if(tokenAux.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
					else {
						flag = false;
						break;
					}
				}
			}
			if(flag)
				break;
			else
				continue;
		}
		if(flag) {
			if(reglas.get("Tipo Funcion r").equals(tokenAux.substring(1, tokenAux.indexOf(" "))) || reglas.get("Tipo Funcion w").equals(tokenAux.substring(1, tokenAux.indexOf(" ")))) {
				pila.addPila("Tipo Funcion "+ pila.getLastPositionPila().substring(tokenAux.substring(1, tokenAux.indexOf(" ")).length()));
				pila.addCadena("$");
				pila.addAccion("Tipo Funcion -> "+ tokenAux.substring(1, tokenAux.indexOf(" ")));
				if(cuerpoFuncion())
					return true;
				else
					return false;
			}
			else
				return false;
		}
		return false;
	}

	public boolean cuerpoFuncion() throws IOException {
		String aux = pila.getLastPositionPila().substring("Tipo Funcion".length() + 1);
		String cadenaFinal = "";
		String tokenAux = "";
		String identificador = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		int j = 0;
		while(j < 3) {
			while((tokenAux = b.readLine()) != null) {
				for(int i = 0; i < aux.length(); i++) {
					if(aux.charAt(i) == tokenAux.charAt(i + 1)) {
						flag = true;
					}
					else {
						if(tokenAux.charAt(i + 1) == ' ') {
							flag = true;
							break;
						}
						else {
							flag = false;
							break;
						}
					}
				}
				if(flag)
					break;
				else
					continue;
			}
			if(flag) {
				if(revisarArchivos("src\\ficheros\\identificador.txt", tokenAux.substring(1, tokenAux.indexOf(" ")))) {
					aux = aux.replace(tokenAux.substring(1, tokenAux.indexOf(" ")), "");
					cadenaFinal += "Variable";
					identificador = tokenAux.substring(1, tokenAux.indexOf(" "));
				}
				else {
					cadenaFinal += tokenAux.substring(1, tokenAux.indexOf(" "));
					aux = aux.replace(tokenAux.substring(1, tokenAux.indexOf(" ")), "");
				}
			}
			else
				return false;
			j++;
		}
		if(reglas.get("Cuerpo Funcion").equals(cadenaFinal)) {
			pila.addPila(pila.getLastPositionPila().replace(pila.getLastPositionPila().substring("Tipo Funcion".length() + 1), "Cuerpo Funcion"+ pila.getLastPositionPila().substring(pila.getLastPositionPila().indexOf(tokenAux.substring(1, tokenAux.indexOf(" "))) + tokenAux.substring(1, tokenAux.indexOf(" ")).length())));
			pila.addCadena("$");
			pila.addAccion("Cuerpo Funcion -> ("+ identificador + ")");
			return true;
		}
		return false;
	}

	/**ÁRBOL DE DERIVACIÓN
	 * @throws IOException */
	public void arbolDerivacion() throws IOException {
		for(int k = 0; k < pila.getSize(); k++) {
			if(!pila.getDatoAccion(k).equals("Llevar a pila") && !pila.getDatoAccion(k).equals("0")) {
				String lexema = pila.getDatoAccion(k).substring(pila.getDatoAccion(k).indexOf("-> ") + "-> ".length());
				if(iterarReglas(lexema)) {

				}
				else if(iterarContenidoReglas(lexema)) {
					lexema = lexema.replace(" ", "");
					String aux = "";
					do {
						//INVERTIMOS LA PILA DE BEGIN{ -> {BEGIN
						String newToken = buscarListaToken(lexema);
						aux = newToken + aux;
						lexema = lexema.replace(newToken, "");
					}while(!lexema.isEmpty());
					int i = 1;
					do {
						//AGREGAMOS AL ÁRBOL
						arbol.addNumero(i);
						String newToken = buscarListaToken(aux);
						arbol.addLexema(newToken);
						arbol.addPadre(0);
						aux = aux.replace(newToken, "");
						i++;
					}while(!aux.isEmpty());
				}
			}
			else
				continue;
		}
		String ultimoLexema = pila.getLastPositionPila();
		//AQUÍ METEMOS LA ÚLTIMA REGLA QUE SE ENCUENTRA EN LA PILA "INICIO"
		arbol.addNumero(arbol.getLastPositionNum() + 1);
		arbol.addLexema(ultimoLexema);
		arbol.addPadre(0);
		for(int i = arbol.getSize(); i > 0; i--) {
			if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
				//INICIO -> BEGIN{				//EN EL LEXEMA UNA POSICIÓN ANTERIOR 	"INICIO"	"BEGIN"
				if(reglas.get(arbol.getLastPositionLex()).indexOf(arbol.getDatoLexema(i - 1)) != -1) {
					arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
				}
			}
		}
		//		System.out.println("Número\tLexema\tPadre");
		//IMPRIMIMOS
		//		for(int i = 0; i < arbol.getSize(); i++)
		//			System.out.println(arbol.getDatoNumero(i) + "\t" + arbol.getDatoLexema(i) + "\t" + arbol.getDatoPadre(i));
	}

	//Otro método
	public void arbolDeTipo() throws IOException {
		int contador = 1;
		for(int k = 0; k < pila.getSize(); k++) {
			if(!pila.getDatoAccion(k).equals("Llevar a pila") && !pila.getDatoAccion(k).equals("0")) {
				String lexema = pila.getDatoAccion(k).substring(pila.getDatoAccion(k).indexOf("-> ") + "-> ".length());
				int count = 0;
				if(lexema.contains(" ")) {
					for(int i = 0; i < lexema.length(); i++) {
						if(lexema.charAt(i) == ' ')
							count++;
					}
					if(count == 1) {
						/**QUIERE DECIR QUE HAY DOS OPCIONES DE CONTENIDO QUE SON REGLAS
						 * PUEDE SER
						 * 		OPCION, OPCION
						 * 		DECLARACION, DECLARACION
						 * 		TIPO DECLARACION;*/
						switch(lexema) {
						case "Cuerpo Funcion":
							arbol.addNumero(contador);
							arbol.addLexema("Cuerpo Funcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if((arbol.getDatoLexema(i - 1 ).equals("(") && arbol.getDatoLexema(i - 2).equals("Variable") && arbol.getDatoLexema(i - 3).equals(")")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;

						case "Tipo Operacion":
							arbol.addNumero(contador);
							arbol.addLexema("Tipo Operacion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0) {
									if(arbol.getDatoLexema(i - 1).equals("ADD") || arbol.getDatoLexema(i - 1).equals("SUB") || arbol.getDatoLexema(i - 1).equals("MUL") || arbol.getDatoLexema(i - 1).equals("DIV")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;

						case "Cuerpo Operacion":
							arbol.addNumero(contador);
							arbol.addLexema("Cuerpo Operacion");
							arbol.addPadre(0);
							contador++;
							//ITERAR DE FORMA CONTRARIA
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0) {
									if((arbol.getDatoLexema(i - 1 ).equals("(") && arbol.getDatoLexema(i - 2).equals("Elementos") && arbol.getDatoLexema(i - 3).equals(")")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;
						case "Tipo Funcion":
							arbol.addNumero(contador);
							arbol.addLexema("Tipo Funcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if(arbol.getDatoLexema(i - 1).equals("READ") || arbol.getDatoLexema(i - 1).equals("WRITE")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;

						case "Opcion, Opcion":
							arbol.addNumero(contador);
							arbol.addLexema("Opcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0) {
									if(arbol.getDatoLexema(i - 1).equals("Factor") || arbol.getDatoLexema(i - 1).equals("Operacion")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
							arbol.addNumero(contador);
							arbol.addLexema(",");
							arbol.addPadre(0);
							contador++;

							arbol.addNumero(contador);
							arbol.addLexema("Opcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if(arbol.getDatoLexema(i - 1).equals("Factor") || arbol.getDatoLexema(i - 1).equals("Operacion")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;
							//DECLARACION
							//,
							//DECLARACION
						case "Declaracion, Declaracion":
							arbol.addNumero(contador);
							arbol.addLexema("Declaracion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && (i - 1 != arbol.getSize() - 1)) {
									if(arbol.getDatoLexema(i - 1).equals("Asignacion") && arbol.getDatoPadre(i - 1) == 0) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
									else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1)) && arbol.getDatoPadre(i - 1) == 0) {
										if(arbol.getDatoLexema(i - 2) != "=")  {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											break;
										}
										else
											continue;
									}
									else if(arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoPadre(i - 1) == 0) {
										if((arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoLexema(i - 2).equals(",") && arbol.getDatoLexema(i - 3).equals("Declaracion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
											break;
										}
									}
								}
							}

							arbol.addNumero(contador);
							arbol.addLexema(",");
							arbol.addPadre(0);
							contador++;

							arbol.addNumero(contador);
							arbol.addLexema("Declaracion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && (i - 1 != arbol.getSize() - 1)) {
									if(arbol.getDatoLexema(i - 1).equals("Asignacion") && arbol.getDatoPadre(i - 1) == 0) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
									else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1)) && arbol.getDatoPadre(i - 1) == 0) {
										if(arbol.getDatoLexema(i - 2) != "=")  {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											break;
										}
										else
											continue;
									}
									else if(arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoPadre(i - 1) == 0) {
										if((arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoLexema(i - 2).equals(",") && arbol.getDatoLexema(i - 3).equals("Declaracion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
											break;
										}
									}
									else
										continue;
								}
							}
							break;
						case "Tipo Declaracion;":
							arbol.addNumero(contador);
							arbol.addLexema(";");
							arbol.addPadre(0);
							contador++;

							arbol.addNumero(contador);
							arbol.addLexema("Declaracion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && (i - 1 != arbol.getSize() - 1)) {
									if(arbol.getDatoLexema(i - 1).equals("Asignacion") && arbol.getDatoPadre(i - 1) == 0) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
									else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1)) && arbol.getDatoPadre(i - 1) == 0) {
										if(arbol.getDatoLexema(i - 2) != "=")  {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											break;
										}
										else
											continue;
									}
									else if(arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoPadre(i - 1) == 0) {
										if((arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoLexema(i - 2).equals(",") && arbol.getDatoLexema(i - 3).equals("Declaracion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
											break;
										}
									}
									else
										continue;
								}
							}

							arbol.addNumero(contador);
							arbol.addLexema("Tipo");
							arbol.addPadre(0);
							contador++;

							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if(arbol.getDatoLexema(i - 1).equals("REAL") || arbol.getDatoLexema(i - 1).equals("INTEGER")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
							break;
						}
					}
					else if(count == 2) {
						/**QUIERE DECIR QUE HAY DOS OPCIONES DE REGLAS
						 * PUEDE SER
						 * 		VARIABLE = NUMERO
						 * 		VARIABLE = OPERACION*/ // ES AQUÍ DONDE BUSCAS EL IDENTIFICADOR
						String identificador = buscarLexema(lexema);
						if(revisarArchivos("src\\ficheros\\identificador.txt", identificador)) {
							String restoDeLaCadena = lexema.substring(lexema.indexOf("= ") + "= ".length());
							String newNumero = buscarListaToken(restoDeLaCadena);
							restoDeLaCadena = restoDeLaCadena.replace(newNumero, "");
							if(isDecimal(newNumero) || isNumeric(newNumero)) {
								if(restoDeLaCadena.isEmpty()) {
									arbol.addNumero(contador);
									arbol.addLexema(newNumero);
									arbol.addPadre(0);
									contador++;

									arbol.addNumero(contador);
									arbol.addLexema("=");
									arbol.addPadre(0);
									contador++;

									arbol.addNumero(contador);
									arbol.addLexema(identificador);
									arbol.addPadre(0);
									contador++;
								}
								else {
									String token = buscarListaToken(restoDeLaCadena);
									if(!token.isEmpty() && token.equals(";")) {
										arbol.addNumero(contador);
										arbol.addLexema(token);
										arbol.addPadre(0);
										contador++;
										
										arbol.addNumero(contador);
										arbol.addLexema(newNumero);
										arbol.addPadre(0);
										contador++;

										arbol.addNumero(contador);
										arbol.addLexema("=");
										arbol.addPadre(0);
										contador++;

										arbol.addNumero(contador);
										arbol.addLexema(identificador);
										arbol.addPadre(0);
										contador++;
									}
								}
							}
						}
						else {
							switch(lexema) {
							case "Variable = Operacion;":
								arbol.addNumero(contador);
								arbol.addLexema(";");
								arbol.addPadre(0);
								contador++;

								arbol.addNumero(contador);
								arbol.addLexema("Operacion");
								arbol.addPadre(0);
								contador++;
								for(int i = arbol.getSize(); i > 0; i--) {
									if(arbol.getDatoPadre(i - 1) == 0) {
										if((arbol.getDatoLexema(i - 1).equals("Tipo Operacion") && arbol.getDatoLexema(i - 2).equals("Cuerpo Operacion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0)) {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
											break;
										}
									}
								}

								arbol.addNumero(contador);
								arbol.addLexema("=");
								arbol.addPadre(0);
								contador++;

								arbol.addNumero(contador);
								arbol.addLexema("Variable");
								arbol.addPadre(0);
								contador++;
								for(int i = arbol.getSize(); i > 0; i--) {
									if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
										if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1))) {
											arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
											break;
										}
									}
								}
								break;
							}
						}
					}
					else if(count == 3) {
						/**QUIERE DECIR QUE HAY DOS OPCIONES DE REGLAS
						 * PUEDE SER
						 * 		TIPO OPERACION CUERPO OPERACION*/
						if(lexema.equals("Tipo Operacion Cuerpo Operacion")) {
							arbol.addNumero(contador);
							arbol.addLexema("Cuerpo Operacion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if((arbol.getDatoLexema(i - 1 ).equals("(") && arbol.getDatoLexema(i - 2).equals("Elementos") && arbol.getDatoLexema(i - 3).equals(")")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
										break;
									}
								}
							}

							arbol.addNumero(contador);
							arbol.addLexema("Tipo Operacion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0) {
									if(arbol.getDatoLexema(i - 1).equals("ADD") || arbol.getDatoLexema(i - 1).equals("SUB") || arbol.getDatoLexema(i - 1).equals("MUL") || arbol.getDatoLexema(i - 1).equals("DIV")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
						}

						else if(lexema.equals("Tipo Funcion Cuerpo Funcion;")) {
							arbol.addNumero(contador);
							arbol.addLexema(";");
							arbol.addPadre(0);
							contador++;

							arbol.addNumero(contador);
							arbol.addLexema("Cuerpo Funcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if((arbol.getDatoLexema(i - 1 ).equals("(") && revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 2)) && arbol.getDatoLexema(i - 3).equals(")")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
										break;
									}
								}
							}

							arbol.addNumero(contador);
							arbol.addLexema("Tipo Funcion");
							arbol.addPadre(0);
							contador++;
							for(int i = arbol.getSize(); i > 0; i--) {
								if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
									if(arbol.getDatoLexema(i - 1 ).equals("READ") || arbol.getDatoLexema(i - 1 ).equals("WRITE")) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
								}
							}
						}
					}
				}
				//SE SUPONE QUE NO HAY ESPACIOS
				else {
					switch(lexema) {
					case "INTEGER":
					case "REAL":
					case "Variable":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						break;
						
					case "Asignacion":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
								if((revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1)) && arbol.getDatoLexema(i - 2).equals("=") && (isNumeric(arbol.getDatoLexema(i - 3)) || isDecimal(arbol.getDatoLexema(i - 3)))) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
									arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
									break;
								}
							}
						}
						break;

					case "Declaracion":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && (i - 1 != arbol.getSize() - 1)) {
								if(arbol.getDatoLexema(i - 1).equals("Asignacion") && arbol.getDatoPadre(i - 1) == 0) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									break;
								}
								else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1)) && arbol.getDatoPadre(i - 1) == 0) {
									if(arbol.getDatoLexema(i - 2) != "=")  {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										break;
									}
									else
										continue;
								}
								else if(arbol.getDatoLexema(i - 1).equals("Declaracion")) {
									if((arbol.getDatoLexema(i - 1).equals("Declaracion") && arbol.getDatoLexema(i - 2).equals(",") && arbol.getDatoLexema(i - 3).equals("Declaracion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
										arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
										arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
										break;
									}
								}
								else
									continue;
							}
						}
						break;

					case "(Elementos)":
						arbol.addNumero(contador);
						arbol.addLexema(")");
						arbol.addPadre(0);
						contador++;

						arbol.addNumero(contador);
						arbol.addLexema("Elementos");
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
								if((arbol.getDatoLexema(i - 1 ).equals("Opcion") && arbol.getDatoLexema(i - 2).equals(",") && arbol.getDatoLexema(i - 3).equals("Opcion")) && (arbol.getDatoPadre(i - 1) == 0 && arbol.getDatoPadre(i - 2) == 0 && arbol.getDatoPadre(i - 3) == 0)) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
									arbol.getPadre().set(i - 3, arbol.getLastPositionNum());
									break;
								}
							}
						}

						arbol.addNumero(contador);
						arbol.addLexema("(");
						arbol.addPadre(0);
						contador++;
						break;

					case "ADD":
					case "SUB":
					case "MUL":
					case "DIV":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						break;

					case "READ":
					case "WRITE":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						break;

					case "Opcion":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
								if(arbol.getDatoLexema(i - 1).equals("Factor") || arbol.getDatoLexema(i - 1).equals("Operacion")) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									break;
								}
							}
						}
						break;

					case "Factor":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
								if(isNumeric(arbol.getDatoLexema(i - 1)) || isDecimal(arbol.getDatoLexema(i - 1)) || revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i - 1))) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									break;
								}
							}
						}
						break;
					case "Operacion":
						arbol.addNumero(contador);
						arbol.addLexema(lexema);
						arbol.addPadre(0);
						contador++;
						for(int i = arbol.getSize(); i > 0; i--) {
							if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
								if(arbol.getDatoLexema(i - 1).equals("Tipo Operacion") && arbol.getDatoLexema(i - 2).equals("Cuerpo Operacion")) {
									arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
									arbol.getPadre().set(i - 2, arbol.getLastPositionNum());
									break;
								}
							}
						}
						break;
					default:
						if(revisarArchivos("src\\ficheros\\identificador.txt", lexema)) {
							arbol.addNumero(contador);
							arbol.addLexema(lexema);
							arbol.addPadre(0);
							contador++;
						}
						else if(!buscarListaToken(lexema).isEmpty()) {
							if(isNumeric(lexema) || isDecimal(lexema)) {
								arbol.addNumero(contador);
								arbol.addLexema(lexema);
								arbol.addPadre(0);
								contador++;
							}
							else if(lexema.equals("(" + buscarUnIdentificador(lexema.substring(1, lexema.length() - 1)) + ")")) {
								arbol.addNumero(contador);
								arbol.addLexema(")");
								arbol.addPadre(0);
								contador++;

								arbol.addNumero(contador);
								arbol.addLexema(lexema.substring(1, lexema.length() - 1));
								arbol.addPadre(0);
								contador++;

								arbol.addNumero(contador);
								arbol.addLexema("(");
								arbol.addPadre(0);
								contador++;
							}
						}
						break;
					}
				}
			}
		}

		if(pila.getDatoPila(1).equals("READ") || pila.getDatoPila(1).equals("WRITE")) {
			String ultimoLexema = pila.getLastPositionPila();
			arbol.addNumero(arbol.getLastPositionNum() + 1);
			arbol.addLexema(ultimoLexema);
			arbol.addPadre(0);
			String ultimaRegla = reglas.get(arbol.getLastPositionLex()).replace(" ", "");
			for(int i = arbol.getSize(); i > 0; i--) {
				if(ultimaRegla.isEmpty())
					break;
				else {
					if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
						if(reglas.get(arbol.getLastPositionLex()+"1").indexOf(arbol.getDatoLexema(i - 1)) != -1) {
							arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
							ultimaRegla = ultimaRegla.replace(arbol.getDatoLexema(i - 1), "");
						}
					}
				}
			}
		}
		else if(pila.getLastPositionPila().equals("Asignacion")) {
			String ultimoLexema = pila.getLastPositionPila();
			arbol.addNumero(arbol.getLastPositionNum() + 1);
			arbol.addLexema(ultimoLexema);
			arbol.addPadre(0);
			for(int i = arbol.getSize(); i > arbol.getSize() - 5; i--)
				arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
		}
		else {
			String ultimoLexema = pila.getLastPositionPila();
			arbol.addNumero(arbol.getLastPositionNum() + 1);
			arbol.addLexema(ultimoLexema);
			arbol.addPadre(0);
			String ultimaRegla = reglas.get(arbol.getLastPositionLex()).replace(" ", "");
			for(int i = arbol.getSize(); i > 0; i--) {
				if(ultimaRegla.isEmpty())
					break;
				else {
					if(arbol.getDatoPadre(i - 1) == 0 && !arbol.getDatoLexema(i - 1).equals(arbol.getLastPositionLex())) {
						if(reglas.get(arbol.getLastPositionLex()).indexOf(arbol.getDatoLexema(i - 1)) != -1) {
							arbol.getPadre().set(i - 1, arbol.getLastPositionNum());
							ultimaRegla = ultimaRegla.replace(arbol.getDatoLexema(i - 1), "");
						}
					}
				}
			}
		}
		//		System.out.println("\nNúmero\t\tLexema\t\tPadre");
		//		for(int i = 0; i < arbol.getSize(); i++)
		//			System.out.println(arbol.getDatoNumero(i) + "\t" + arbol.getDatoLexema(i) + "\t" + arbol.getDatoPadre(i));
	}
	
	
	/////////////////////////////////////////////DESDE AQUÍ///////////////////////////////////////////////////////////////
	/////////////////////////////////////////////COMIENZA EL//////////////////////////////////////////////////////////////
	/////////////////////////////////////////ANALIZADOR SEMÁNTICO////////////////////////////////////////////////////////
	
	public void cargarArbol(String archivo) throws IOException {
        String cadena;
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        String[] datos;
        while((cadena = b.readLine())!=null) {
            datos = cadena.split("\t\t");
            arbol.addNumero(Integer.parseInt(datos[0]));
            arbol.addLexema(datos[1]);
            arbol.addPadre(Integer.parseInt(datos[2]));
        }
        b.close();
    }
	
	public TablaValores cargarTablaValores() throws NumberFormatException, IOException {
		TablaValores tabla = new TablaValores();
		String cadena;
        FileReader f = new FileReader("src\\ficheros\\filename.txt");
        BufferedReader b = new BufferedReader(f);
        String[] datos;
        while((cadena = b.readLine())!=null) {
            datos = cadena.split("\t");
            tabla.addIdentificador(datos[0]);
            tabla.addValor(datos[1]);
            tabla.addTipo(datos[2]);
        }
        b.close();
        if(tabla.getSize() != 0)
        	return tabla;
        else
        	return new TablaValores();
	}
	
	public void revisarArbol() throws IOException {
		int lineaDeErrores = 0;
		generarFichero.vaciarArchivoTabla();
		for(int i = 0; i < arbol.getSize(); i++) {
			if(arbol.getDatoNumero(i) == 1) {
				if(arbol.getDatoLexema(i).equals("INTEGER")) {
					lineaDeErrores++;
					int j = i;
					TablaValores tabla = cargarTablaValores();
					int findNumber = 0;
					String auxNumber = "";
					do {
						if(findNumber != 0)
							findNumber++;
						if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(j))) {
							if(new File("src\\ficheros\\filename.txt").length() == 0 && tabla.getSize() == 0) {
								if(findNumber == 3) {
									tabla.addIdentificador(arbol.getDatoLexema(j));
									tabla.addValor(auxNumber);
									tabla.addTipo("Integer");
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
									findNumber = 0;
								}
								else {
									tabla.addIdentificador(arbol.getDatoLexema(j));
									tabla.addValor("");
									tabla.addTipo("Integer");
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
								}
							}
							else {
								if(findNumber == 3) {
									if(!revisarTabla(arbol.getDatoLexema(j))) {
										if(!tabla.contains(arbol.getDatoLexema(j))) {
											tabla.addIdentificador(arbol.getDatoLexema(j));
											tabla.addValor(auxNumber);
											tabla.addTipo("Integer");
											arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
											findNumber = 0;
										}
										else {
											System.out.println("Esa variable ya está declarada");
											erroresDelAnalizadorSemantico.add(lineaDeErrores);
											
											break;
										}
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										
										break;
									}
								}
								else {
									if(!revisarTabla(arbol.getDatoLexema(j))) {
										tabla.addIdentificador(arbol.getDatoLexema(j));
										tabla.addValor("");
										tabla.addTipo("Integer");
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										
										break;
									}
								}
							}
						}
						else if(isNumeric(arbol.getDatoLexema(j))) {
							auxNumber = arbol.getDatoLexema(j);
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
							findNumber++;
						}
						else if(isDecimal(arbol.getDatoLexema(j))) {
							auxNumber = arbol.getDatoLexema(j);
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
							findNumber++;
						}
						else if(arbol.getDatoLexema(j).equals("Asignacion")) {
							int k = j;
							boolean flag = true;
							do {
								while(!arbol.getDatoLexema(k).equals("=")) 
									k--;
								if(arbol.getDatoPadre(k) == arbol.getDatoNumero(j))
									flag = false;
								else {
									k--;
									continue;
								}
							}while(flag);
							
							if(arbol.getDatoLexema(k + 1).contains("INTEGER") && arbol.getDatoLexema(k - 1).contains("INTEGER"))
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
							else {
								System.out.println("Dato incorrecto");
								erroresDelAnalizadorSemantico.add(lineaDeErrores);
								
								break;
							}
						}
						j++;
						if(j == arbol.getSize())
							break;
					}
					while(arbol.getDatoNumero(j) != 1);
					i = j - 1;
					String newTable = "";
					for(int t = 0; t < tabla.getSize(); t++)
						newTable += tabla.getIdentificador(t) + "\t" + tabla.getValor(t) + "\t" + tabla.getTipo(t) + "\n";
					generarFichero.escribirTabla(newTable);					
				}
				
				//ES CUANDO ENCUENTRA UN REAL
				else if(arbol.getDatoLexema(i).equals("REAL")) {
					lineaDeErrores++;
					int j = i;
					TablaValores tabla = cargarTablaValores();
					int findNumber = 0;
					String auxNumber = "";
					do {
						if(findNumber != 0)
							findNumber++;
						if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(j))) {
							if(new File("src\\ficheros\\filename.txt").length() == 0 && tabla.getSize() == 0) {
								if(findNumber == 3) {
									tabla.addIdentificador(arbol.getDatoLexema(j));
									tabla.addValor(auxNumber);
									tabla.addTipo("Real");
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
									findNumber = 0;
								}
								else {
									tabla.addIdentificador(arbol.getDatoLexema(j));
									tabla.addValor("");
									tabla.addTipo("Real");
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
								}
							}
							else {
								if(findNumber == 3) {
									if(!revisarTabla(arbol.getDatoLexema(j))) {
										if(!tabla.contains(arbol.getDatoLexema(j)) ) {
											tabla.addIdentificador(arbol.getDatoLexema(j));
											tabla.addValor(auxNumber);
											tabla.addTipo("Real");
											arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
											findNumber = 0;
										}
										else {
											System.out.println("Esa variable ya ha sido declarada");
											erroresDelAnalizadorSemantico.add(lineaDeErrores);
											
											break;
										}
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										
										break;
									}
								}
								else {
									if(!revisarTabla(arbol.getDatoLexema(j))) {
										tabla.addIdentificador(arbol.getDatoLexema(j));
										tabla.addValor("");
										tabla.addTipo("Real");
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										
										break;
									}
								}
							}
						}
						else if(isNumeric(arbol.getDatoLexema(j))) {
							auxNumber = arbol.getDatoLexema(j);
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
							findNumber++;
						}
						else if(isDecimal(arbol.getDatoLexema(j))) {
							auxNumber = arbol.getDatoLexema(j);
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
							findNumber++;
						}
						else if(arbol.getDatoLexema(j).equals("Asignacion")) {
							int k = j;
							boolean flag = true;
							do {
								while(!arbol.getDatoLexema(k).equals("=")) 
									k--;
								if(arbol.getDatoPadre(k) == arbol.getDatoNumero(j))
									flag = false;
								else {
									k--;
									continue;
								}
							}while(flag);
							
							if(arbol.getDatoLexema(k + 1).contains("REAL") && arbol.getDatoLexema(k - 1).contains("REAL"))
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
							else {
								System.out.println("Dato incorrecto");
								erroresDelAnalizadorSemantico.add(lineaDeErrores);
								
								break;
							}
						}
						j++;
						if(j == arbol.getSize())
							break;
					}
					while(arbol.getDatoNumero(j) != 1);
					i = j - 1;
					String newTable = "";
					for(int t = 0; t < tabla.getSize(); t++)
						newTable += tabla.getIdentificador(t) + "\t" + tabla.getValor(t) + "\t" + tabla.getTipo(t) + "\n";
					generarFichero.escribirTabla(newTable);
				}
				
				//DECLARACIÓN DE OPERACIÓN
				else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i))) {
					lineaDeErrores++;
					if(new File("src\\ficheros\\filename.txt").length() == 0) {
						System.out.println("Error");
						erroresDelAnalizadorSemantico.add(lineaDeErrores);
						
						continue;
					}
					else {
						String tipoDeDato = "";
						if(revisarTabla(arbol.getDatoLexema(i))) {
							String dato = encontrarTipo(arbol.getDatoLexema(i));
							if(dato.contains("Integer")) {
								arbol.getLexema().set(i, arbol.getDatoLexema(i) + "   INTEGER");
								tipoDeDato = "INTEGER";
							}
							else if(dato.contains("Real")) {
								arbol.getLexema().set(i, arbol.getDatoLexema(i) + "   REAL");
								tipoDeDato = "REAL";
							}
						}
						else {
							System.out.println("Esta variable no ha sido declarada");
							erroresDelAnalizadorSemantico.add(lineaDeErrores);
							
							continue;
						}
						int j = i + 2;
						List<String> tiposDeDatos = new ArrayList<String>();
						boolean errorInterno = false;
						do {
							if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(j))) {
								if(revisarTabla(arbol.getDatoLexema(j))) {
									if(encontrarTipo(arbol.getDatoLexema(j)).contains("Integer")) {
										tiposDeDatos.add("INTEGER");
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
										j++;
									}
									else {
										tiposDeDatos.add("REAL");
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
										j++;
									}
								}
								else {
									System.out.println("La variable no ha sido declarada");
									erroresDelAnalizadorSemantico.add(lineaDeErrores);
									
									errorInterno = true;
									break;
								}
							}
							else if(isNumeric(arbol.getDatoLexema(j))) {
								tiposDeDatos.add("INTEGER");
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
								j++;
							}
							else if(isDecimal(arbol.getDatoLexema(j))) {
								tiposDeDatos.add("REAL");
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
								j++;
							}
							else if(arbol.getDatoLexema(j).equals("Operacion")) {
								if(tiposDeDatos.size() == 2) {
									if(tiposDeDatos.get(0).equals(tipoDeDato) && tiposDeDatos.get(1).equals(tipoDeDato)) {
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   "+ tipoDeDato);
										j++;
									}
									else {
										System.out.println("Los tipos de datos no coinciden. Primer Operacion");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										
										errorInterno = true;
										break;
									}
								}
								else {
									if(tiposDeDatos.get(0).equals(tipoDeDato) && tiposDeDatos.get(1).equals(tipoDeDato)) {
										arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   "+ tipoDeDato);
										tiposDeDatos.remove(0);
										tiposDeDatos.remove(1);
										tiposDeDatos.add(tipoDeDato);
										j++;
									}
									else {
										System.out.println("Los tipos de datos no coinciden. Segundo Operacion");
										erroresDelAnalizadorSemantico.add(lineaDeErrores);
										errorInterno = true;
										break;
									}
								}
							}
							else
								j++;
						}while(!arbol.getDatoLexema(j).equals("Resultado Final"));
						i = j;
					}
				}
				
				//ES CUANDO HAY UNA DECLARACIÓN SIMPLE
				else if(arbol.getDatoLexema(i).equals(";")) {
					lineaDeErrores++;
					System.out.println("Ha entrado en el método");
					TablaValores tabla = cargarTablaValores();
					int j = i;
					boolean flag = true;
					j++;
					boolean errorInterno = false;
					do {
						if(!arbol.getDatoLexema(j).equals("=")) {
							if(isNumeric(arbol.getDatoLexema(j))) {
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
								j++;
								continue;
							}
							else if(isDecimal(arbol.getDatoLexema(j))) {
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
								j++;
								continue;
							}
							
							if(revisarTabla(arbol.getDatoLexema(j))) {
								String dato = encontrarTipo(arbol.getDatoLexema(j));
								if(dato.contains("Integer")) {
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
									int k = 0;
									while(k < tabla.getSize()) {
										if(tabla.getIdentificador(k).equals(buscarLexema(arbol.getDatoLexema(j)))) {
											tabla.getValor().set(k, buscarLexema(arbol.getDatoLexema(j - 2)));
											break;
										}
										else {
											k++;
											continue;
										}
									}
									j++;
									continue;
								}
								else if(dato.contains("Real")) {
									arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
									j++;
									continue;
								}
							}
							else {
								System.out.println("Esta variable no ha sido declarada");
								flag = false;
								erroresDelAnalizadorSemantico.add(lineaDeErrores);
								errorInterno = true;
								break;
							}
						}
						else
							j++;
					}while(!arbol.getDatoLexema(j).equals("Asignacion"));
					if(errorInterno)
						continue;
					else {
						if(flag) {
							if(arbol.getDatoLexema(j - 1).contains("INTEGER") && arbol.getDatoLexema(j - 3).contains("INTEGER"))
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
							else if(arbol.getDatoLexema(j - 1).contains("REAL") && arbol.getDatoLexema(j - 3).contains("REAL"))
								arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
							else {
								System.out.println("Los datos no cumplen");
								erroresDelAnalizadorSemantico.add(lineaDeErrores);
								continue;
							}
						}
						else {
							erroresDelAnalizadorSemantico.add(lineaDeErrores);
							continue;
						}
						String newTable = "";
						for(int t = 0; t < tabla.getSize(); t++) {
							newTable += tabla.getIdentificador(t) + "\t" + tabla.getValor(t) + "\t" + tabla.getTipo(t) + "\n";
							System.out.println(newTable);
						}
						generarFichero.escribirTabla(newTable);	
					}
				}
				
				//ES CUANDO ENCUENTRA READ O WRITE
				else if(arbol.getDatoLexema(i).equals("READ") || arbol.getDatoLexema(i).equals("WRITE")) {
					lineaDeErrores++;
					if(revisarTabla(arbol.getDatoLexema(i + 2))) {
						if(encontrarTipo(arbol.getDatoLexema(i + 2)).contains("Integer"))
							arbol.getLexema().set(i + 2, arbol.getDatoLexema(i + 2) + "   INTEGER");
						else
							arbol.getLexema().set(i + 2, arbol.getDatoLexema(i + 2) + "   REAL");
					}
					else {
						System.out.println("La variable no ha sido declarada");
						erroresDelAnalizadorSemantico.add(lineaDeErrores);
						continue;
					}
				}
				else 
					continue;
			}
			else
				continue;
		}
		
		for(int i = 0; i < arbol.getSize(); i++)
			System.out.println(arbol.getDatoNumero(i) + "\t" + arbol.getDatoLexema(i) + "\t" + arbol.getDatoPadre(i));
		if(!existeInicio() && !existeFin()) {
			if(arbol.getDatoLexema(arbol.getSize() - 1).equals("Fin") && arbol.getDatoLexema(2).equals("Inicio")) {
				System.out.println("Cumple con la regla");
				if(contarInicioFin() == 2) 
					System.out.println("Cumple bien con la regla");
				else 
					reglaInicioFin = "Hay más de un Inicio o un Fin. ¡ERROR!";
			}
			else
				reglaInicioFin = "La regla Inicio o Fin no están escritas en orden. ¡ERROR!";
		}
		else
			reglaInicioFin = "No existe Inicio";
	}

	public void llenarReglas() {
		/**PENTIENTE PARA SABER SI A LAS REGLAS LE AGREGAMOS EL tk_ o no se lo agregamos*/
		reglas.put("Inicio", "BEGIN {");
		reglas.put("Fin", "} END");
		reglas.put("Declaracion Final", "Tipo Declaracion;");
		reglas.put("Declaracion", "Declaracion");
		reglas.put("Declaracion asig", "Asignacion");
		reglas.put("Declaracion dec", "Declaracion, Declaracion");
		reglas.put("Asignacion", "Variable = Numero");
		reglas.put("Resultado Final", "Variable = Operacion;");
		reglas.put("Operacion", "Tipo Operacion Cuerpo Operacion");
		reglas.put("Tipo Operacion add", "ADD");
		reglas.put("Tipo Operacion sub", "SUB");
		reglas.put("Tipo Operacion mul", "MUL");
		reglas.put("Tipo Operacion div", "DIV");
		reglas.put("Cuerpo Operacion", "(Elementos)");
		reglas.put("Elementos", "Opcion, Opcion");
		reglas.put("Opcion fac", "Factor");
		reglas.put("Opcion oper", "Operacion");
		reglas.put("Funcion", "Tipo Funcion Cuerpo Funcion;");
		reglas.put("Tipo Funcion r", "READ");
		reglas.put("Tipo Funcion w", "WRITE");
		reglas.put("Cuerpo Funcion", "(Variable)"); //TENER EN CUENTA
		reglas.put("Tipo INT", "INTEGER");
		reglas.put("Tipo REAL", "REAL");
		reglas.put("Factor num", "Numero");
		reglas.put("Factor var", "Variable");
		reglas.put("Funcion1", "Tipo Funcion Cuerpo Funcion;");
		//CREO QUE CON ESTO YA QUEDÓ
	}
	
	public int contarInicioFin() throws IOException {
		String tokenAux = "";
		int count = 0;
		FileReader f = new FileReader("src\\ficheros\\arbolDerivacion.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains("BEGIN"))
				count++;
			else if(tokenAux.contains("END"))
				count++;
			else 
				continue;
		}
		return count;
	}
	
	public boolean existeInicio() throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader("src\\ficheros\\filename.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains("Inicio"))
				return true;
			else 
				continue;
		}
		return false;
	}
	
	public boolean existeFin() throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader("src\\ficheros\\filename.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains("Fin"))
				return true;
			else 
				continue;
		}
		return false;
	}

	/**ITERADOR DE REGLAS*/
	public boolean iterarContenidoReglas(String cadena) {
		for (Entry<String, String> entry : reglas.entrySet()) {
			if(cadena.equals(entry.getValue())) 
				return true;
		}
		return false;
	}

	public boolean iterarReglas(String cadena) {
		for (Entry<String, String> entry : reglas.entrySet()) {
			if(cadena.equals(entry.getKey())) 
				return true;
		}
		return false;
	}

	//PARA SABER SI ES NUMÉRICO
	public boolean isNumeric(String cadena) {
		boolean resultado;
		try {
			Integer.parseInt(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	//PARA SABER SI ES DECIMAL
	public boolean isDecimal(String cadena) {
		boolean resultado;
		try {
			Double.parseDouble(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	//REVISA UN ARCHIVO POR TI
	public boolean revisarArchivos(String ruta, String buscar) throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader(ruta);
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(buscar.equals(tokenAux))
				return true;
			else 
				continue;
		}
		return false;
	}

	public String buscarListaToken(String aux) throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		int j = 0;
		while((tokenAux = b.readLine()) != null) {
			for(int i = 0; i < aux.length(); i++) {
				if(aux.charAt(i) == tokenAux.charAt(i + 1)) {
					flag = true;
				}
				else {
					if(tokenAux.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
					else {
						flag = false;
						break;
					}
				}
			}
			if(flag)
				break;
			else
				continue;
		}
		if(flag)
			return tokenAux.substring(1, tokenAux.indexOf(" "));
		else
			return "";
	}
	
	public String buscarLexema(String aux) throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader(listaTokens);
		BufferedReader b = new BufferedReader(f);
		boolean flag = true;
		int j = 0;
		while((tokenAux = b.readLine()) != null) {
			for(int i = 0; i < aux.length(); i++) {
				if(aux.charAt(i) == tokenAux.charAt(i + 1)) {
					flag = true;
					if(aux.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
				}
				else {
					if(tokenAux.charAt(i + 1) == ' ') {
						flag = true;
						break;
					}
					else {
						flag = false;
						break;
					}
				}
			}
			if(flag)
				break;
			else
				continue;
		}
		if(flag)
			return tokenAux.substring(1, tokenAux.indexOf(" "));
		else
			return "";
	}
	public String buscarUnIdentificador(String buscar) throws IOException {
		String tokenAux = "";
		FileReader f = new FileReader("src\\ficheros\\identificador.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(buscar.equals(tokenAux))
				return tokenAux;
			else 
				continue;
		}
		return "";
	}
	
	public String encontrarTipo(String buscar) throws IOException {
    	String tokenAux = "";
		FileReader f = new FileReader("src\\ficheros\\filename.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains(buscar))
				return tokenAux;
			else 
				continue;
		}
		return "";
    }
	
	public String encontrarIdentificador(String ruta, String buscar) throws IOException {
    	String tokenAux = "";
		FileReader f = new FileReader(ruta);
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains(buscar))
				return tokenAux;
			else 
				continue;
		}
		return "";
    }
	
	public boolean revisarTabla(String buscar) throws IOException {
    	String tokenAux = "";
		FileReader f = new FileReader("src\\ficheros\\filename.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null) {
			if(tokenAux.contains(buscar))
				return true;
			else 
				continue;
		}
		return false;
    }
}