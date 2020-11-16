package modeloAutomata;

public class Nodo {
	private String nombre;			// Nombre del nodo, ejemplo: Q0 o Q1
	private boolean aceptacion;		// Si en este nodo del diagrama a cumplido aceptación, la cadena es valida
	private Nodo nodoIzquierda;		// El nodo al que avanzará en el diagrama si lee un 1
	private Nodo nodoDerecha;		// El nodo al que avanzará en el diagrama si lee un 0
									// Izquierda corresponde a 0 y Derecha a 1

	public Nodo(String nombre, boolean aceptacion) {
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

	public Nodo getNodoIzquierda() {
		return nodoIzquierda;
	}

	public void setNodoIzquierda(Nodo nodoIzquierda) {
		this.nodoIzquierda = nodoIzquierda;
	}

	public Nodo getNodoDerecha() {
		return nodoDerecha;
	}

	public void setNodoDerecha(Nodo nodoDerecha) {
		this.nodoDerecha = nodoDerecha;
	}

}
