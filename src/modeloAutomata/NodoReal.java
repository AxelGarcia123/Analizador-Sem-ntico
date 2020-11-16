package modeloAutomata;

public class NodoReal {
	private String nombre;			
	private boolean aceptacion;		
	private NodoReal nodo1;		
	private NodoReal nodo2;
	private NodoReal nodo3;
								

	public NodoReal(String nombre, boolean aceptacion) {
		this.nombre = nombre;
		this.aceptacion = aceptacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isAceptacion() {
		return aceptacion;
	}

	public void setAceptacion(boolean aceptacion) {
		this.aceptacion = aceptacion;
	}

	public NodoReal getNodo1() {
		return nodo1;
	}

	public void setNodo1(NodoReal nodo1) {
		this.nodo1 = nodo1;
	}

	public NodoReal getNodo2() {
		return nodo2;
	}

	public void setNodo2(NodoReal nodo2) {
		this.nodo2 = nodo2;
	}

	public NodoReal getNodo3() {
		return nodo3;
	}

	public void setNodo3(NodoReal nodo3) {
		this.nodo3 = nodo3;
	}

	


}
