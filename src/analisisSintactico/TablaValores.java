package analisisSintactico;

import java.util.ArrayList;
import java.util.List;

public class TablaValores {
	private List<String> identificador;
	private List<String> valor;
	private List<String> tipo;
	
	public TablaValores() {
		this.identificador = new ArrayList<String>();
		this.valor = new ArrayList<String>();
		this.tipo = new ArrayList<String>();
	}

	public void addIdentificador(String identificador) {
		this.identificador.add(identificador);
	}
	
	public void addValor(String valor) {
		this.valor.add(valor);
	}
	
	public void addTipo(String tipo) {
		this.tipo.add(tipo);
	}
	
	public String getIdentificador(int position) {
		return this.identificador.get(position);
	}
	
	public String getValor(int position) {
		return this.valor.get(position);
	}
	
	public String getTipo(int position) {
		return this.tipo.get(position);
	}
	
	public int getSize() {
		return this.identificador.size();
	}
	
	public boolean contains(String identificador) {
		return this.identificador.contains(identificador);
	}
}