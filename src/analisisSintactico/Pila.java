package analisisSintactico;

import java.util.ArrayList;
import java.util.List;

public class Pila {
	private List<String> stack;
	private List<String> cadena;
	private List<String> accion;

	public Pila() {
		this.stack = new ArrayList<String>();
		this.cadena = new ArrayList<String>();
		this.accion = new ArrayList<String>();
	}

	public void addPila(String dato) {
		stack.add(dato);
	}

	public void addCadena(String dato) {
		cadena.add(dato);
	}

	public void addAccion(String dato) {
		accion.add(dato);
	}

	public String getLastPositionPila() {
		return stack.get(stack.size() - 1);
	}

	public String getLastPositionCadena() {
		return cadena.get(cadena.size() - 1);
	}

	public String getLastPositionAccion() {
		return accion.get(accion.size() - 1);
	}

	public List<String> getPila() {
		return this.stack;
	}

	public List<String> getCadena() {
		return this.cadena;
	}

	public List<String> getAccion() {
		return this.accion;
	}
	
	public int getSize() {
		return this.stack.size();
	}
	
	public void limpiarPila() {
		this.stack.clear();
		this.cadena.clear();
		this.accion.clear();
	}
	//
	
	public String getDatoPila(int position) {
		return this.stack.get(position);
	}

	public String getDatoCadena(int position) {
		return this.cadena.get(position);
	}

	public String getDatoAccion(int position) {
		return this.accion.get(position);
	}
}