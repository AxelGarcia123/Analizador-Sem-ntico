package analisisSintactico;

import java.util.ArrayList;
import java.util.List;

public class ArbolDerivacion {
	private List<Integer> numero;
	private List<String> lexema;
	private List<Integer> padre;
	private int count = 1;
	
	public ArbolDerivacion() {
		this.numero = new ArrayList<Integer>();
		 this.lexema = new ArrayList<String>();
		 this.padre = new ArrayList<Integer>();
	}
	
	public void addNumero(int num) {
		this.numero.add(num);
	}
	
	public void addLexema(String lex) {
		this.lexema.add(lex);
	}
	
	public void addPadre(int papa) {
		this.padre.add(papa);
	}
	
	public List<Integer> getNumero() {
		return this.numero;
	}
	
	public List<String> getLexema() {
		return this.lexema;
	}
	
	public List<Integer> getPadre() {
		return this.padre;
	}
	
	public int getSize() {
		return this.lexema.size();
	}
	
	public int getDatoNumero(int input) {
		return this.numero.get(input);
	}
	
	public String getDatoLexema(int input) {
		return this.lexema.get(input);
	}
	
	public int getDatoPadre(int input) {
		return this.padre.get(input);
	}
	
	public int getLastPositionNum() {
		return this.numero.get(this.numero.size() - 1);
	}
	
	public String getLastPositionLex() {
		return this.lexema.get(this.lexema.size() - 1);
	}
	
	public int getLastPositionPadre() {
		return this.padre.get(this.padre.size() - 1);
	}
	
	public void vaciarArbol() {
		this.numero.clear();
		this.lexema.clear();
		this.padre.clear();
	}
	
	public void agregarDatoArbol(String dato, boolean esRegla, String numReglas) {
		if(esRegla) {
			switch(numReglas) {
			
			}
		}
		else {
			addNumero(count);
			addLexema(dato);
			addPadre(0);
		}
	}
}