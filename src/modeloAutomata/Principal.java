package modeloAutomata;

public class Principal {
	public static void main(String[] args) {
		AutomataReal automata = new AutomataReal();
		String ide = " a1.1";
		for (int i = 0; i < ide.length(); i++) {
			automata.makeTransition(String.valueOf(ide.charAt(i)));
		}
		System.out.println(automata.getCursor().isAceptacion());
	}
}