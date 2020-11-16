package modelo;

import java.util.List;
public class LinkedList {
	Nodo head = null;
	Nodo end = null;
	int contador = 0;

	public void addNewElement(Object data) {

		if(head == null){
			head = new Nodo(data);
			end = head;
		}
		else {
			end.next = new Nodo(data);
			end = end.next;
		}
		contador++;
	}

	public String getElements() {
		Nodo temp = head;
		String result = "";
		while(temp != null){
			result += temp.data + " ";
			temp = temp.next;
		}
		return result;
	}
	

	public boolean getToken(List<String> palabra) {

		if(palabra.size() == contador) {
			int t = 0;
			Nodo temp = head;
			while(temp != null && t < palabra.size()){
				if(palabra.get(t).equals(temp.data)) {
					temp = temp.next;
					t++;
					continue;	
				}
				return false;
			}
		}
		else {
			System.out.println("Longitud diferente"+contador);
			return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return contador==0;
	}
}