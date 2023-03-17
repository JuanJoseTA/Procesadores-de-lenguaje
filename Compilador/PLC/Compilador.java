import java.util.*;
public class Compilador {
	
	private static int auxIndex = -1;
	private static int auxTag = -1;
	
	public static void print_error()
	{
		System.out.println("error;");
	}
	
	public static String print(String v){
		System.out.println("print " + v + " ;");
		return v;
	}
	
	public static String puts(String v){
		System.out.println(v + " ;");
		return v;
	}
	
	public static String asig(String k, String v){
		System.out.println(k + " = " + v + " ;");
		return k;
	}
	
	private static String newVariableAuxiliar(){
		auxIndex++;
		return "t" + auxIndex;
	}
	
	public static String operacion(String number){
		String aux = newVariableAuxiliar();
		System.out.println(aux + " = " + number + " ;");
		return aux;
	}
	
	public static void etiqueta(String label){
		System.out.println("label " + label + " ;");
	}
	
	public static void goToLabel(String label){
		System.out.println("goto " + label + " ;");
	}
	
	public static String generarTag(){
		auxTag++;
		return "L" + auxTag;
	}
	
	public static Tag condicion(String cond, String arg1, String arg2){
		Tag result = new Tag(generarTag(),generarTag());
		switch(cond){
			case "IGUAL": 
				System.out.println("if(" + arg1 + "==" + arg2 + ") goto " + result.getA() + " ;");
				System.out.println("goto " + result.getB() + " ;");
			break;
			case "DIST":
				System.out.println("if(" + arg1 + "==" + arg2 + ") goto " + result.getB() + " ;");
				System.out.println("goto " + result.getA() + " ;");
			break;
			case "MENOR":
				System.out.println("if(" + arg1 + "<" + arg2 + ") goto " + result.getA() + " ;");
				System.out.println("goto " + result.getB() + " ;");
			break;
			case "MAYOR":
				System.out.println("if(" + arg2 + "<" + arg1 + ") goto " + result.getA() + " ;");
				System.out.println("goto " + result.getB() + " ;");
			break;
			case "MAYOREQ":
				System.out.println("if(" + arg1 + "<" + arg2 + ") goto " + result.getB() + " ;");
				System.out.println("goto " + result.getA() + " ;");
			break;
			case "MENOREQ":
				System.out.println("if(" + arg2 + "<" + arg1 + ") goto " + result.getB() + " ;");
				System.out.println("goto " + result.getA() + " ;");
			break;
		}
		return result;
	}
	
	public static Tag operador(String op, Tag t1, Tag t2){
		Tag result = t2;
		switch(op){
			case "NOT":
				result = new Tag (t1.getB(),t1.getA());
			break;
			case "AND":
				etiqueta(t1.getB());
				goToLabel(t2.getA());
			break;
			case "OR":
				etiqueta(t1.getA());
				goToLabel(t2.getB());
			break;
		}
		return result;
	}
	
}
