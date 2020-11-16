package modelo;

public class Nodo {
	public Nodo next;
	public Object data;

	public Nodo(Object data) {
		this.data = data;
		next = null;
	}
}