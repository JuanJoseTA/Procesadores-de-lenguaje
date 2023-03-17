import java.util.*;
public class Compilador {
	
	private static int auxIndex = -1;
	private static int auxTag = -1;
	private static HashMap<String, ArrayList<String>> variables = new HashMap<>();
	
	public static void print_error()
	{
		System.out.println("error;");
	}
	
	public static String print(String v){
		if(v != null) System.out.println("print " + v + " ;");
		else{
			print_error();
			System.out.println("halt;");
			System.exit(0);
		}
		return v;
	}
	
	public static void eliminar(int level){
		Set<String> set = variables.keySet();
		HashMap<String, ArrayList<String>> v = new HashMap<>();
		for(String e : set){
			ArrayList<String> aux = variables.get(e);
			aux.remove(String.valueOf(level));
			if(aux.size()>0) v.put(e, aux);
		}
		variables = v;
	}
	
	public static String comprobar(String k){
		if(variables.containsKey(k)) return k;
		else return null;
	}
	
	public static String comprobar(String k , int level){
		if(k.length()>2) if(k.charAt(k.length()-2) == '_') k = k.substring(0, k.length()-2);
		if(variables.containsKey(k)){
			if(level == 0) return k;
			else{
				for(int i = level; i>0 ; i--){
					if(variables.get(k).contains(String.valueOf(i))) return k + "_" + (i-1);	
				}
				return k;
			}
		}else return null;
	}
	
	public static void declarar(String k, int level){
		String r = comprobar(k);
		if(r != null){
			if(variables.get(r).contains(String.valueOf(level))){
				print_error();
				System.out.println("halt;");
				System.exit(0);
			}else{
				ArrayList<String> aux = variables.get(r);
				aux.add(String.valueOf(level));
				variables.replace(r, aux);
			}	
		}
		else{
			ArrayList<String> aux = new ArrayList<>();
			aux.add(String.valueOf(level));
			variables.put(k, aux);
		}
	}
	
	public static String puts(String v){
		System.out.println(v + " ;");
		return v;
	}
	
	public static String asig(String k, String v, int level){
		String r = comprobar(k);
		String res = k;
		if(r == null){
			print_error();
			System.out.println("halt;");
			System.exit(0);
		}
		else if(level == 0) System.out.println(k + " = " + v + " ;");
		else{
			if(variables.get(k).contains(String.valueOf(level))){
				System.out.println(k + "_" + (level-1) + " = " + v + " ;");
				res = k + "_" + (level-1);
			}
			else System.out.println(k + " = " + v + " ;");
		}
		return res;
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
	
	public static String post(String op, String id){
		String aux = newVariableAuxiliar();
		System.out.println(aux + " = " + id + " ;");
		System.out.println(id + " = " + id + " " + op + " 1 ;");
		return aux;
	}
	
	public static String pre(String op, String id){
		System.out.println(id + " = " + id + " " + op + " 1 ;");
		return id;
	}
	
	public static String step(String op, String e1 , String e2){
		String aux = newVariableAuxiliar();
		System.out.println(aux + " = " + e2 + " ;");
		System.out.println(e1 + " = " + e1 + " " + op + aux + " ;");
		return aux;
	}
	
	public static String modulo(String e1, String e2){
		String aux1 = newVariableAuxiliar();
		String aux2 = newVariableAuxiliar();
		String id = newVariableAuxiliar();
		System.out.println(aux1 + " = " + e1 + " / " + e2 + " ;");
		System.out.println(aux2 + " = " + aux1 + " * " + e2 + " ;");
		System.out.println(id + " = " + e1 + " - " + aux2 + " ;");
		return id;
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
