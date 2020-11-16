package modeloAutomata;

import java.util.Arrays;
import java.util.List;

public class AutomataEntero {
	private boolean interrupcion; 
	private Nodo cursor; 
	private Nodo nodoQ0; 

	private Nodo nodoQ1;
	private Nodo nodoQ2;
	private Nodo nodoQ3;
	private Nodo nodoQ4;
	private Nodo nodoQ5;
	private Nodo nodoQ6;


	public AutomataEntero() {
		inicializarNodos();
		interrupcion = false;
		corresponderNodos(); 
		cursor = nodoQ0;	
	}

	public void makeTransition(String valor) {
		if (esLetra(valor)) {
			cursor = cursor.getNodoDerecha(); 
		} else if (esNumero(valor)) {	
			cursor = cursor.getNodoIzquierda();	
		} else {
			interrupcion = true;				
		}
	}

	public void inicializarNodos() {			
		nodoQ0 = new Nodo("Q0", false);
		nodoQ1 = new Nodo("Q1", true);
		nodoQ2 = new Nodo("Q2", true);
		nodoQ3 = new Nodo("Q3", true);
		nodoQ4 = new Nodo("Q4", true);
		nodoQ5 = new Nodo("Q5", true);
		nodoQ6 = new Nodo("Q6", false);
	}

	public void corresponderNodos() {			
		nodoQ0.setNodoIzquierda(nodoQ1);

		nodoQ1.setNodoIzquierda(nodoQ2);
		nodoQ1.setNodoDerecha(nodoQ6);

		nodoQ2.setNodoIzquierda(nodoQ3);
		nodoQ2.setNodoDerecha(nodoQ6);
		
		nodoQ3.setNodoIzquierda(nodoQ4);
		nodoQ3.setNodoDerecha(nodoQ6);
		
		nodoQ4.setNodoIzquierda(nodoQ5);
		nodoQ4.setNodoDerecha(nodoQ6);
		
		nodoQ5.setNodoIzquierda(nodoQ6);
		nodoQ5.setNodoDerecha(nodoQ6);
		
		nodoQ6.setNodoIzquierda(nodoQ6);
		nodoQ6.setNodoDerecha(nodoQ6);
		
	}

	public boolean isInterrupcion() {
		return interrupcion;
	}

	public void setInterrupcion(boolean interrupcion) {
		this.interrupcion = interrupcion;
	}

	public Nodo getCursor() {
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
