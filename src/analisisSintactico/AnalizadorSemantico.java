package analisisSintactico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import archivos.EscribirFichero;

public class AnalizadorSemantico {
	private ArbolDerivacion arbol;
	private EscribirFichero fichero;
	
	public AnalizadorSemantico() {
		this.arbol = new ArbolDerivacion();
		this.fichero = new EscribirFichero();
	}
	
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
	
	public void revisarArbol() throws IOException {
		fichero.vaciarArchivoTabla();
		for(int i = 0; i < arbol.getSize(); i++) {
			if(arbol.getDatoNumero(i) == 1) {
				if(arbol.getDatoLexema(i).equals("INTEGER")) {
					int j = i;
					TablaValores tabla = new TablaValores();
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
											break;
										}
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
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
								while(!arbol.getDatoLexema(k).equals("=") /*&& arbol.getDatoPadre(k + 1) != arbol.getDatoPadre(j)*/) 
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
							else 
								System.out.println("Dato incorrecto");
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
					fichero.escribirTabla(newTable);					
				}
				
				//ES CUANDO ENCUENTRA UN REAL
				else if(arbol.getDatoLexema(i).equals("REAL")) {
					int j = i;
					TablaValores tabla = new TablaValores();
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
											break;
										}
									}
									else {
										System.out.println("Esa variable ya existe. Ya fue declarada");
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
								while(!arbol.getDatoLexema(k).equals("=") /*&& arbol.getDatoPadre(k + 1) != arbol.getDatoPadre(j)*/) 
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
							else 
								System.out.println("Dato incorrecto");
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
					fichero.escribirTabla(newTable);
				}
				
				//DECLARACIÓN DE OPERACIÓN
				else if(revisarArchivos("src\\ficheros\\identificador.txt", arbol.getDatoLexema(i))) {
					if(new File("src\\ficheros\\filename.txt").length() == 0)
						System.out.println("Error");
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
							break;
						}
						int j = i + 2;
						List<String> tiposDeDatos = new ArrayList<String>();
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
					int j = i;
					boolean flag = true;
					j++;
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
								break;
							}
						}
						else
							j++;
					}while(!arbol.getDatoLexema(j).equals("Asignacion"));
					if(flag) {
						if(arbol.getDatoLexema(j - 1).contains("INTEGER") && arbol.getDatoLexema(j - 3).contains("INTEGER"))
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   INTEGER");
						else if(arbol.getDatoLexema(j - 1).contains("REAL") && arbol.getDatoLexema(j - 3).contains("REAL"))
							arbol.getLexema().set(j, arbol.getDatoLexema(j) + "   REAL");
						else {
							System.out.println("Los datos no cumplen");
							break;
						}
					}
					else
						break;
				}
				else 
					continue;
			}
			else
				continue;
		}
		
		for(int i = 0; i < arbol.getSize(); i++)
			System.out.println(arbol.getDatoNumero(i) + "\t" + arbol.getDatoLexema(i) + "\t" + arbol.getDatoPadre(i));
	}

    public static void main(String[] args) throws IOException {
    	AnalizadorSemantico semantico = new AnalizadorSemantico();
        semantico.cargarArbol("src\\ficheros\\arbolDerivacion.txt");
        semantico.revisarArbol();
        
        if(new File("src\\ficheros\\filename.txt").length() == 0)
        	System.out.println("Está vacio");
        else
        	System.out.println("No está vacio");
    }
    
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
}