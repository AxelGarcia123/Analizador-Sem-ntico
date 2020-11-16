package modeloAutomata;

import java.util.Arrays;
import java.util.List;

public class AutomataReal {
	private boolean interrupcion; // Controla si se le un dato que no se encuentre en el alfabeto {0,1}, cuando el
									// diagrama no encuentra un camino para un símbolo.
	private NodoReal cursor; // El nodo en el que se encuentra (posición en la representación del diagrama).
	private NodoReal nodoQ0; // Nodo inicial.

	private NodoReal nodoQ1; // Lista de nodos requeridos.
	private NodoReal nodoQ2;
	private NodoReal nodoQ3;
	private NodoReal nodoQ4;
	private NodoReal nodoQ5;
	private NodoReal nodoQ6;
	private NodoReal nodoQ7;
	private NodoReal nodoQ8;
	private NodoReal nodoQ9;
	private NodoReal nodoQ10;


	public AutomataReal() {
		inicializarNodos(); // Metodo que inicializa las variables nodo.
		interrupcion = false;
		corresponderNodos(); // Método que establece que un nodo apunta a otros dos nodos, representando el
								// diagrama.
		cursor = nodoQ0;	//Posicion incial.
	}

	public void makeTransition(String valor) {
		if (esNumero(valor)) {
			cursor = cursor.getNodo1(); 
		} else if (esLetra(valor)) {	
			cursor = cursor.getNodo2();	
		} else if (valor.equals(".")) {
			cursor = cursor.getNodo3();
		}else{
			interrupcion = true;				
		}
	}

	public void inicializarNodos() {			//Se inicalizan las varibles nodo
		nodoQ0 = new NodoReal("Q0", false);
		nodoQ1 = new NodoReal("Q1", false);
		nodoQ2 = new NodoReal("Q2", false);
		nodoQ3 = new NodoReal("Q3", false);
		nodoQ4 = new NodoReal("Q4", false);
		nodoQ5 = new NodoReal("Q5", false);
		nodoQ6 = new NodoReal("Q6", false);
		nodoQ7 = new NodoReal("Q7", true);
		nodoQ8 = new NodoReal("Q8", true);
		nodoQ9 = new NodoReal("Q9", false);
		nodoQ10 = new NodoReal("Q10", false);
	}

	public void corresponderNodos() {			//Se establece correspondencia de nodos según el diagrama
		nodoQ0.setNodo1(nodoQ1);
		nodoQ0.setNodo2(nodoQ10);
		nodoQ0.setNodo3(nodoQ10);
		
		nodoQ1.setNodo1(nodoQ2);
		nodoQ1.setNodo2(nodoQ10);
		nodoQ1.setNodo3(nodoQ6);
		
		nodoQ2.setNodo1(nodoQ3);
		nodoQ2.setNodo2(nodoQ10);
		nodoQ2.setNodo3(nodoQ6);
		
		nodoQ3.setNodo1(nodoQ4);
		nodoQ3.setNodo2(nodoQ10);
		nodoQ3.setNodo3(nodoQ6);
		
		nodoQ4.setNodo1(nodoQ5);
		nodoQ4.setNodo2(nodoQ10);
		nodoQ4.setNodo3(nodoQ6);
		
		nodoQ5.setNodo1(nodoQ10);
		nodoQ5.setNodo2(nodoQ10);
		nodoQ5.setNodo3(nodoQ6);
		
		nodoQ6.setNodo1(nodoQ7);
		nodoQ6.setNodo2(nodoQ9);
		nodoQ6.setNodo3(nodoQ9);
		
		nodoQ7.setNodo1(nodoQ8);
		nodoQ7.setNodo2(nodoQ9);
		nodoQ7.setNodo3(nodoQ9);
		
		nodoQ8.setNodo1(nodoQ9);
		nodoQ8.setNodo2(nodoQ9);
		nodoQ8.setNodo3(nodoQ9);
		
		nodoQ9.setNodo1(nodoQ9);
		nodoQ9.setNodo2(nodoQ9);
		nodoQ9.setNodo3(nodoQ9);
		
		nodoQ10.setNodo1(nodoQ10);
		nodoQ10.setNodo2(nodoQ10);
		nodoQ10.setNodo3(nodoQ10);
		
	}

	public boolean isInterrupcion() {
		return interrupcion;
	}

	public void setInterrupcion(boolean interrupcion) {
		this.interrupcion = interrupcion;
	}

	public NodoReal getCursor() {
		return cursor;
	}
	
	public boolean esNumero(String cad) {
		for (int i = 0; i < 10; i++) {
			if (cad.startsWith(String.valueOf(i))) {
				return true;
			}
		}
		return false;
	}

	public boolean esLetra(String cad) {
		List<String> alfabetoLetras = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
				"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
		
		for (String valor : alfabetoLetras) {
			if (cad.startsWith(valor)) {
				return true;
			}
		}
		return false;
	}

}
