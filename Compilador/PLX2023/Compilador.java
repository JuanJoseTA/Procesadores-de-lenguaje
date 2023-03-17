import java.util.*;
public class Compilador {
	
	private static int auxIndex = -1;
	private static int auxTag = -1;
	private static boolean ECHAR = false;
	public static HashMap<String, ArrayList<String>> variables = new HashMap<>();
	public static HashMap<String, Integer> arrays = new HashMap<>();
	public static HashMap<String, String> tipo = new HashMap<>();
	
	public static void print_error()
	{
		System.out.println("error;");
	}
	
	public static String print(String v){
		if(tipo.containsKey(v) && tipo.get(v).substring(0,2).equals("s_")) printSet(v);
		else{
			boolean esChar = ECHAR;
			if(v.length()==3){
				if(v.substring(0,1).equals("'") && v.substring(2,3).equals("'")){
					v = String.valueOf((int) v.charAt(1));
					esChar = true;
				}
			}
			if(v.length()>3){
				if(v.substring(0,1).equals("'") && v.substring(3,4).equals("'")){
					v = String.valueOf((int) v.charAt(1));
					esChar = true;
				}
				if(v.length()>7){
					if(v.substring(0,3).equals("'\\u")){
						v = String.valueOf((int) Integer.parseInt(v.substring(3, 7), 16));
						esChar = true;
					}
				}
			}
			if(v != null){
				if(variables.containsKey(v)){
					if(tipo.get(v).equals("char") || tipo.containsKey(v+"C") || tipo.get(v).equals("a_char")){
						 esChar = true;
						 tipo.remove(v+"C");
					}else esChar = false;
				}
				if(esChar) System.out.println("printc " + v + " ;");
				else System.out.println("print " + v + " ;");
			}
			else{
				print_error();
				System.out.println("halt;");
				System.exit(0);
			}
			ECHAR = false;
		}
		return v;
	}
	
	public static void printSet(String v){
		String aux5 = newVariableAuxiliar();
		System.out.println(aux5 + " = " + 0 + " ;");
		String tr6 = generarTag();
		etiqueta(tr6);
		String tr7 = generarTag();
		String tr8 = generarTag();
		System.out.print("if (" + aux5 + " < " + v + "_length" + ") "); goToLabel(tr7);
		goToLabel(tr8);
		etiqueta(tr7);
		String aux6 = newVariableAuxiliar();
		System.out.println(aux6 + " = " + v + "[" + aux5 + "] ;");
		if(tipo.get(v).equals("s_char")) System.out.println("writec " + aux6 + " ;");
		else System.out.println("write " + aux6 + " ;");
		System.out.println("writec " + 32 + " ;");
		System.out.println(aux5 + " = " + aux5 + " + 1 ;");
		goToLabel(tr6);
		etiqueta(tr8);
		System.out.println("writec " + 10 + " ;");
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
		}else if(arrays.containsKey(k)){
			return k;
		}else return null;
	}
	
	public static String comprobarArray(String k){
		if(arrays.containsKey(k)) return k;
		else return null;
	}
	
	public static String lengthArray(String k){
		if(arrays.containsKey(k)) return k + "_length";
		else return null;
	}
	
	public static String posArray(String k, String v, int level){
		String r = comprobarArray(k);
		if(r == null){
			print_error();
			System.out.println("halt;");
			System.exit(0);
			return null;
		}else{
			String aux = newVariableAuxiliar();
			String tr = generarTag();
			String f = generarTag();
			System.out.print("if (" + v + " < " + 0 + ") "); goToLabel(tr);
			System.out.print("if (" + arrays.get(k) + " < " + v + ") "); goToLabel(tr);
			System.out.print("if (" + arrays.get(k) + " == " + v + ") "); goToLabel(tr);
			goToLabel(f);
			etiqueta(tr);
			print_error();
			System.out.println("halt;");
			etiqueta(f);
			System.out.println(aux + " = " + k + "[" + v + "] ;");
			tipo.put(aux, tipo.get(k).substring(2, tipo.get(k).length()));
			ArrayList<String> arr = new ArrayList<>();
			arr.add(String.valueOf(level));
			variables.put(aux, arr);
			return aux;
		}
	}
	
	public static void declararArray(String k, String t, String v){
		String r = comprobarArray(k);
		if(r != null){
			print_error();
			System.out.println("halt;");
			System.exit(0);
		}else{
			int n = 0;
			try{
				n = Integer.parseInt(v);
			}catch(NumberFormatException eee){
				print_error();
				System.out.println("halt;");
				System.exit(0);
			}
			System.out.println(k + "_length = " + v + " ;");
			ArrayList<String> arr = new ArrayList<>();
			arr.add(String.valueOf(0));
			variables.put(k + "_length", arr);
			tipo.put(k + "_length", "int");
			arrays.put(k, n);
			tipo.put(k, "a_" + t);
		}
	}
	
	public static String asigArray(String k, String v, String e){
		e = convertirASCII(e);
		String r = comprobarArray(k);
		if(r == null){
			print_error();
			System.out.println("halt;");
			System.exit(0);
		}else{
			int n = 0;
			try{
				n = Integer.parseInt(v);
				if(n > arrays.get(k)){
					print_error();
					System.out.println("halt;");
					System.exit(0);
				}
			}catch(NumberFormatException eee){
			}
			System.out.println(k + "[" + v + "] = " + e + " ;");
		}
		return k;
	}
	
	public static void iniciaArray(String k, ArrayList<String> a){
		if(tipo.get(k).substring(0,2).equals("s_")) iniciaSet(k, a);
		else{
			String aux = newVariableAuxiliar();
			String aux2 = newVariableAuxiliar();
			for(int i = 0; i<a.size(); i++){
				System.out.println(aux + "[" + i + "] = " + convertirASCII(a.get(i)) + " ;");
			}
			for(int j = 0; j<a.size(); j++){
				System.out.println(aux2 + " = " + aux + "[" + j + "] ;");
				System.out.println(k + "[" + j + "] = " + aux2 + " ;");
			}
			System.out.println(k + " = " + aux + " ;");
		}
	}
	
	public static void iniciaSet(String k, ArrayList<String> a){
		String aux0 = newVariableAuxiliar();
		for(int i = 0; i<a.size(); i++){
			if(tipo.get(k).equals("s_int")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("int")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					try{
						Integer.parseInt(a.get(i));
					}catch(NumberFormatException e){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			if(tipo.get(k).equals("s_float")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("float")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					if(!a.get(i).contains(".")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			System.out.println(aux0 + "[" + i + "] = " + convertirASCII(a.get(i)) + " ;");
		}
		System.out.println(k + "_length = " + 0 + " ;");
		ArrayList<String> arr = new ArrayList<>();
		arr.add(String.valueOf(0));
		variables.put(k + "_length", arr);
		tipo.put(k + "_length", "int");
		String aux1 = newVariableAuxiliar();
		System.out.println(aux1 + " = " + 0 + " ;");
		String tr0 = generarTag();
		etiqueta(tr0);
		String tr1 = generarTag();
		String tr2 = generarTag();
		System.out.print("if (" + aux1 + " < " + a.size() + ") "); goToLabel(tr1);
		goToLabel(tr2);
		etiqueta(tr1);
		String aux2 = newVariableAuxiliar();
		System.out.println(aux2 + " = " + aux0 + "[" + aux1 + "] ;");
		String aux3 = newVariableAuxiliar();
		System.out.println(aux3 + " = " + 0 + " ;");
		String tr3 = generarTag();
		etiqueta(tr3);
		String tr4 = generarTag();
		System.out.print("if (" + aux3 + " == " + k + "_length" + ") "); goToLabel(tr4);
		String aux4 = newVariableAuxiliar();
		System.out.println(aux4 + " = " + k + "[" + aux3 + "] ;");
		String tr5 = generarTag();
		System.out.print("if (" + aux4 + " == " + aux2 + ") "); goToLabel(tr5);
		System.out.println(aux3 + " = " + aux3 + " + 1 ;");
		goToLabel(tr3);
		etiqueta(tr4);
		System.out.println(k + "[" + k + "_length" + "] = " + aux2 + " ;");
		System.out.println(k + "_length" + " = " + k + "_length" + " + 1 ;");
		etiqueta(tr5);
		System.out.println(aux1 + " = " + aux1 + " + 1 ;");
		goToLabel(tr0);
		etiqueta(tr2);
	}
	
	public static void addSet(String k, ArrayList<String> a){
		String aux0 = newVariableAuxiliar();
		for(int i = 0; i<a.size(); i++){
			/*
			if(tipo.get(k).equals("s_int")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("int")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					try{
						Integer.parseInt(a.get(i));
					}catch(NumberFormatException e){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			if(tipo.get(k).equals("s_float")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("float")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					if(!a.get(i).contains(".")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			*/
			System.out.println(aux0 + "[" + i + "] = " + convertirASCII(a.get(i)) + " ;");
		}
		String aux1 = newVariableAuxiliar();
		System.out.println(aux1 + " = " + 0 + " ;");
		String tr0 = generarTag();
		etiqueta(tr0);
		String tr1 = generarTag();
		String tr2 = generarTag();
		System.out.print("if (" + aux1 + " < " + a.size() + ") "); goToLabel(tr1);
		goToLabel(tr2);
		etiqueta(tr1);
		String aux2 = newVariableAuxiliar();
		System.out.println(aux2 + " = " + aux0 + "[" + aux1 + "] ;");
		String aux3 = newVariableAuxiliar();
		System.out.println(aux3 + " = " + 0 + " ;");
		String tr3 = generarTag();
		etiqueta(tr3);
		String tr4 = generarTag();
		System.out.print("if (" + aux3 + " == " + k + "_length" + ") "); goToLabel(tr4);
		String aux4 = newVariableAuxiliar();
		System.out.println(aux4 + " = " + k + "[" + aux3 + "] ;");
		String tr5 = generarTag();
		System.out.print("if (" + aux4 + " == " + aux2 + ") "); goToLabel(tr5);
		System.out.println(aux3 + " = " + aux3 + " + 1 ;");
		goToLabel(tr3);
		etiqueta(tr4);
		System.out.println(k + "[" + k + "_length" + "] = " + aux2 + " ;");
		System.out.println(k + "_length" + " = " + k + "_length" + " + 1 ;");
		etiqueta(tr5);
		System.out.println(aux1 + " = " + aux1 + " + 1 ;");
		goToLabel(tr0);
		etiqueta(tr2);
	}
	
	public static void addSet(String e1, String e2){
		String aux1 = newVariableAuxiliar();
		System.out.println(aux1 + " = " + 0 + " ;");
		String tr0 = generarTag();
		etiqueta(tr0);
		String tr1 = generarTag();
		String tr2 = generarTag();
		System.out.print("if (" + aux1 + " < " + e2 + "_length) "); goToLabel(tr1);
		goToLabel(tr2);
		etiqueta(tr1);
		String aux2 = newVariableAuxiliar();
		System.out.println(aux2 + " = " + e2 + "[" + aux1 + "] ;");
		String aux3 = newVariableAuxiliar();
		System.out.println(aux3 + " = " + 0 + " ;");
		String tr3 = generarTag();
		etiqueta(tr3);
		String tr4 = generarTag();
		System.out.print("if (" + aux3 + " == " + e1 + "_length" + ") "); goToLabel(tr4);
		String aux4 = newVariableAuxiliar();
		System.out.println(aux4 + " = " + e1 + "[" + aux3 + "] ;");
		String tr5 = generarTag();
		System.out.print("if (" + aux4 + " == " + aux2 + ") "); goToLabel(tr5);
		System.out.println(aux3 + " = " + aux3 + " + 1 ;");
		goToLabel(tr3);
		etiqueta(tr4);
		System.out.println(e1 + "[" + e1 + "_length" + "] = " + aux2 + " ;");
		System.out.println(e1 + "_length" + " = " + e1 + "_length" + " + 1 ;");
		etiqueta(tr5);
		System.out.println(aux1 + " = " + aux1 + " + 1 ;");
		goToLabel(tr0);
		etiqueta(tr2);
	}
	
	public static void delSet(String e1, ArrayList<String> a){
		String aux5 = newVariableAuxiliar();
		for(int i = 0; i<a.size(); i++){
			if(tipo.get(e1).equals("s_int")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("int")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					try{
						Integer.parseInt(a.get(i));
					}catch(NumberFormatException e){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			if(tipo.get(e1).equals("s_float")){
				if(tipo.containsKey(a.get(i))){
					if(!tipo.get(a.get(i)).equals("float")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}else{
					if(!a.get(i).contains(".")){
						print_error();
						System.out.println("halt;");
						System.exit(0);
					}
				}
			}
			System.out.println(aux5 + "[" + i + "] = " + convertirASCII(a.get(i)) + " ;");
		}
		String tr6 = generarTag();
		etiqueta(tr6);
		String tr7 = generarTag();
		String tr8 = generarTag();
		String aux6 = newVariableAuxiliar();
		System.out.print("if (" + aux6 + " < " + a.size() + " ) "); goToLabel(tr7);
		goToLabel(tr8);
		etiqueta(tr7);
		String aux7 = newVariableAuxiliar();
		System.out.println(aux7 + " = " + aux5 + "[" + aux6 + "] ;");
		String aux8 = newVariableAuxiliar();
		System.out.println(aux8 + " = " + 0 + " ;");
		String tr9 = generarTag();
		etiqueta(tr9);
		String tr11 = generarTag();
		System.out.print("if (" + e1 + "_length" + " == " + aux8 + ") "); goToLabel(tr11);
		String aux10 = newVariableAuxiliar();
		System.out.println(aux10 + " = " + e1 + "[" + aux8 + "] ;");
		String tr12 = generarTag();
		System.out.print("if (" + aux10 + " == " + aux7 + ") "); goToLabel(tr12);
		System.out.println(aux8 + " = " + aux8 + " + 1 ;");
		goToLabel(tr9);
		etiqueta(tr12);
		System.out.println(e1 + "_length" + " = " + e1 + "_length" + " - 1 ;");
		String tr10 = generarTag();
		etiqueta(tr10);
		System.out.print("if (" + e1 + "_length" + " == " + aux8 + ") "); goToLabel(tr11);
		String aux9 = newVariableAuxiliar();
		System.out.println(aux9 + " = " + aux8 + " ;");
		System.out.println(aux8 + " = " + aux8 + " + 1 ;");
		System.out.println(aux10 + " = " + e1 + "[" + aux8 + "] ;");
		System.out.println(e1 + "[" + aux9 + "] = " + aux10 + " ;");
		goToLabel(tr10);
		etiqueta(tr11);
		System.out.println(aux6 + " = " + aux6 + " + 1 ;");
		goToLabel(tr6);
		etiqueta(tr8);
	}
	
	public static void delSet(String e1, String e2){
		String aux5 = newVariableAuxiliar();
		System.out.println(aux5 + " = " + e2 + " ;");
		String tr6 = generarTag();
		etiqueta(tr6);
		String tr7 = generarTag();
		String tr8 = generarTag();
		String aux6 = newVariableAuxiliar();
		System.out.print("if (" + aux6 + " < 1 ) "); goToLabel(tr7);
		goToLabel(tr8);
		etiqueta(tr7);
		String aux7 = newVariableAuxiliar();
		System.out.println(aux7 + " = " + aux5 + "[" + aux6 + "] ;");
		String aux8 = newVariableAuxiliar();
		System.out.println(aux8 + " = " + 0 + " ;");
		String tr9 = generarTag();
		etiqueta(tr9);
		String tr11 = generarTag();
		System.out.print("if (" + e1 + "_length" + " == " + aux8 + ") "); goToLabel(tr11);
		String aux10 = newVariableAuxiliar();
		System.out.println(aux10 + " = " + e1 + "[" + aux8 + "] ;");
		String tr12 = generarTag();
		System.out.print("if (" + aux10 + " == " + aux7 + ") "); goToLabel(tr12);
		System.out.println(aux8 + " = " + aux8 + " + 1 ;");
		goToLabel(tr9);
		etiqueta(tr12);
		System.out.println(e1 + "_length" + " = " + e1 + "_length" + " - 1 ;");
		String tr10 = generarTag();
		etiqueta(tr10);
		System.out.print("if (" + e1 + "_length" + " == " + aux8 + ") "); goToLabel(tr11);
		String aux9 = newVariableAuxiliar();
		System.out.println(aux9 + " = " + aux8 + " ;");
		System.out.println(aux8 + " = " + aux8 + " + 1 ;");
		System.out.println(aux10 + " = " + e1 + "[" + aux8 + "] ;");
		System.out.println(e1 + "[" + aux9 + "] = " + aux10 + " ;");
		goToLabel(tr10);
		etiqueta(tr11);
		System.out.println(aux6 + " = " + aux6 + " + 1 ;");
		goToLabel(tr6);
		etiqueta(tr8);
	}
	
	public static void declarar(String k, int level, String t){
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
			tipo.put(k, t);
		}
	}
	
	public static String puts(String v){
		System.out.println(v + " ;");
		return v;
	}
	
	public static String asig(String k, String v, int level){
		String r = comprobar(k);
		String res = k;
		if (!k.equals(v)){
			if(r == null) r = comprobarArray(k);
			String conversion = "";
			if(r == null){
				print_error();
				System.out.println("halt;");
				System.exit(0);
			}
			else{
				if(tipo.containsKey(v)){
					if(tipo.get(k).contains("a_")){
						if(tipo.get(v).contains("a_")){
							if(tipo.get(v).equals(tipo.get(k))){
								if(arrays.get(k) >= arrays.get(v)){
									String aux = newVariableAuxiliar();
									for(int i = 0; i<arrays.get(v); i++){
										System.out.println(aux + " = " + v + "[" + i + "] ;");
										System.out.println(k + "[" + i + "] = " + aux + " ;");
									}
								}
								else{
									print_error();
									System.out.println("halt;");
									System.exit(0);
								}
							}else{
								print_error();
								System.out.println("halt;");
								System.exit(0);
							}
						}else{
							print_error();
							System.out.println("halt;");
							System.exit(0);
						}
					}
					else if(tipo.get(k).contains("s_")) System.out.println(k + "_length = " + v + "_length ;");
					else if(!tipo.get(k).equals(tipo.get(v)) && (tipo.get(k).equals("int") || tipo.get(k).equals("float"))) conversion = "(" + tipo.get(k) + ") ";
					else if(tipo.get(k).equals("char") && tipo.get(v).equals("int")){
						if(!tipo.containsKey(v+"C")){
							print_error();
							System.out.println("halt;");
							System.exit(0);
						}
						tipo.remove(v+"C");
					}
				}
				else{
					if(tipo.get(k).equals("int") && !v.contains("[")){
						try{
							Integer.parseInt(v);
						}catch(NumberFormatException e){
							print_error();
							System.out.println("halt;");
							System.exit(0);
						}
					}
				}
				if(variables.containsKey(k)){
					if(tipo.get(k).equals("char") && v.contains("'")) v = String.valueOf((int) v.charAt(1));
					if(level == 0){
						if(tipo.containsKey(v)){
							if(tipo.get(v).contains("s_")){
								if(!tipo.get(k).equals(tipo.get(v))){
									print_error();
									System.out.println("halt;");
									System.exit(0);
								}	
							}
						}
						System.out.println(k + " = " + conversion + v + " ;");
					}
					else if(variables.get(k).contains(String.valueOf(level))){
						System.out.println(k + "_" + (level-1) + " = " + conversion + v + " ;");
						res = k + "_" + (level-1);
					}
					else if(variables.containsKey(k)) System.out.println(k + " = " + conversion + v + " ;");
				}
			}
		}
		return res;
	}
	
	public static String newVariableAuxiliar(){
		auxIndex++;
		return "t" + auxIndex;
	}
	
	public static String convertirASCII(String ch){
		if(ch.length()==3){
			if(ch.substring(0,1).equals("'") && ch.substring(2,3).equals("'")){
				ch = String.valueOf((int) ch.charAt(1));
			}
		}
		if(ch.length()>3){
			if(ch.substring(0,1).equals("'") && ch.substring(3,4).equals("'")){
				ch = String.valueOf((int) ch.charAt(1));
			}
			if(ch.length()>7){
				if(ch.substring(0,3).equals("'\\u")){
					ch = String.valueOf((int) Integer.parseInt(ch.substring(3, 7), 16));
				}
			}
		}
		return ch;
	}
	
	public static String cambioChar(String op, String c, int level){
		c = convertirASCII(c);
		String aux = newVariableAuxiliar();
		String tr = generarTag();
		System.out.println(aux + " = " + c + " ;");
		tipo.put(aux, "char");
		ArrayList<String> arr = new ArrayList<>();
		arr.add(String.valueOf(level));
		variables.put(aux, arr);
		if(op.equals("!")){
			System.out.print("if (" + c + " < " + 97 + ") "); goToLabel(tr);
			System.out.print("if (" + 122 + " < " + c + ") "); goToLabel(tr);
			System.out.println(aux + " = " + c + " - 32 ;");
			etiqueta(tr);
		}
		if(op.equals("~")){
			String tr2 = generarTag();
			System.out.print("if (" + c + " < " + 65 + ") "); goToLabel(tr);
			System.out.print("if (" + 122 + " < " + c + ") "); goToLabel(tr);
			System.out.print("if (" + 96 + " < " + c + ") "); goToLabel(tr2);
			System.out.print("if (" + 90 + " < " + c + ") "); goToLabel(tr);
			System.out.println(aux + " = " + c + " + 32 ;");
			goToLabel(tr);
			etiqueta(tr2);
			System.out.println(aux + " = " + c + " - 32 ;");
			etiqueta(tr);
		}
		return aux;
	}
	
	private static void asigSetUnion(String aux10, String c1){

		String aux11 = newVariableAuxiliar();
		System.out.println(aux11 + " = 0 ;");
		String l12 = generarTag();
		etiqueta(l12);
		String l13 = generarTag();
		String l14 = generarTag();
		System.out.print("if (" + aux11 + " < " + c1 + "_length) "); goToLabel(l13);
		goToLabel(l14);
		etiqueta(l13);
		String aux12 = newVariableAuxiliar();
		System.out.println(aux12 + " = " + c1 + "[" + aux11 + "] ;");
		String aux13 = newVariableAuxiliar();
		System.out.println(aux13 + " = 0 ;");
		String l15 = generarTag();
		etiqueta(l15);
		String l16 = generarTag();
		System.out.print("if (" + aux13 + " == " + aux10 + "_length) "); goToLabel(l16);
		String aux14 = newVariableAuxiliar();
		System.out.println(aux14 + " = " + aux10 + "[" + aux13 + "] ;");
		String l17 = generarTag();
		System.out.print("if (" + aux14 + " == " + aux12 + ") "); goToLabel(l17);
		System.out.println(aux13 + " = " + aux13 + " + 1 ;");
		goToLabel(l15);
		etiqueta(l16);
		System.out.println(aux10 + "[" + aux10 + "_length] = " + aux12 + " ;");
		System.out.println(aux10 + "_length = " + aux10 + "_length + 1 ;");
		etiqueta(l17);
		System.out.println(aux11 + " = " + aux11 + " + 1 ;");
		goToLabel(l12);
		etiqueta(l14);
	}
	
	public static String unionSetNumero(String c1, String c2){
		String aux11 = newVariableAuxiliar();
		System.out.println(aux11 + " = 0 ;");
		String l6 = generarTag();
		etiqueta(l6);
		String l7 = generarTag();
		String aux13 = newVariableAuxiliar();
		System.out.print("if (" + aux13 + " == " + c1 + "_length) "); goToLabel(l7);
		String aux14 = newVariableAuxiliar();
		System.out.println(aux14 + " = " + c1 + "[" + aux13 + "] ;");
		String l8 = generarTag();
		System.out.print("if (" + aux14 + " == " + c2 + ") "); goToLabel(l8);
		System.out.println(aux13 + " = " + aux13 + " + 1 ;");
		goToLabel(l6);
		etiqueta(l7);
		System.out.println(c1 + "[" + c1 + "_length] = " + c2 + " ;");
		System.out.println(c1 + "_length = " + c1 + "_length + 1 ;");
		etiqueta(l8);
		return c1;
	}
	
	public static String unionSet(String c1, String c2){
		String aux10 = newVariableAuxiliar();
		System.out.println(aux10 + "_length = 0 ;");
		asigSetUnion(aux10, c1);
		asigSetUnion(aux10, c2);
		tipo.put(aux10, tipo.get(c1));
		return aux10;
	}
	
	public static String resSet(String c1, String c2){
		String aux10 = newVariableAuxiliar();
		System.out.println(aux10 + "_length = 0 ;");
		asigSetUnion(aux10, c1);
		tipo.put(aux10, tipo.get(c1));
		String l18 = generarTag();
		etiqueta(l18);
		String aux15 = newVariableAuxiliar();
		String l19 = generarTag();
		String l20 = generarTag();
		System.out.print("if (" + aux15 + " < " + c2 + "_length) "); goToLabel(l19);
		goToLabel(l20);
		etiqueta(l19);
		String aux16 = newVariableAuxiliar();
		System.out.println(aux16 + " = " + c2 + "[" + aux15 + "] ;");
		String aux17 = newVariableAuxiliar();
		System.out.println(aux17 + " = 0 ;");
		String l21 = generarTag();
		etiqueta(l21);
		String l23 = generarTag();
		String aux19 = newVariableAuxiliar();
		System.out.print("if (" + aux10 + "_length == " + aux17 + ") "); goToLabel(l23);
		System.out.println(aux19 + " = " + aux10 + "[" + aux17 + "] ;");
		String l24 = generarTag();
		System.out.print("if (" + aux19 + " == " + aux16 + ") "); goToLabel(l24);
		System.out.println(aux17 + " = " + aux17 + " + 1 ;");
		goToLabel(l21);
		etiqueta(l24);
		System.out.println(aux10 + "_length = " + aux10 + "_length - 1 ;");
		String l22 = generarTag();
		etiqueta(l22);
		System.out.print("if (" + aux10 + "_length == " + aux17 + ") "); goToLabel(l23);
		String aux18 = newVariableAuxiliar();
		System.out.println(aux18 + " = " + aux17 + " ;");
		System.out.println(aux17 + " = " + aux17 + " + 1 ;");
		System.out.println(aux19 + " = " + aux10 + "[" + aux17 + "] ;");
		System.out.println(aux10 + "[" + aux18 + "] = " + aux19 + " ;");
		goToLabel(l22);
		etiqueta(l23);
		System.out.println(aux15 + " = " + aux15 + " + 1 ;");
		goToLabel(l18);
		etiqueta(l20);
		return aux10;
	}
	
	public static String interSet(String c1, String c2){
		String aux10 = newVariableAuxiliar();
		System.out.println(aux10 + "_length = 0 ;");
		String l12 = generarTag();
		etiqueta(l12);
		String aux11 = newVariableAuxiliar();
		String l17 = generarTag();
		System.out.print("if (" + aux11 + " == " + c1 + "_length) "); goToLabel(l17);
		String aux13 = newVariableAuxiliar();
		String aux12 = newVariableAuxiliar();
		System.out.println(aux13 + " = " + c1 + "[" + aux11 + "] ;");
		System.out.println(aux12 + " = 0 ;");
		tipo.put(aux10, tipo.get(c1));
		String l14 = generarTag();
		etiqueta(l14);
		String l16 = generarTag();
		System.out.print("if (" + aux12 + " == " + c2 + "_length) "); goToLabel(l16);
		String aux14 = newVariableAuxiliar();
		System.out.println(aux14 + " = " + c2 + "[" + aux12 + "] ;");
		String l15 = generarTag();
		System.out.print("if (" + aux13 + " == " + aux14 + ") "); goToLabel(l15);
		System.out.println(aux12 + " = " + aux12 + " + 1 ;");
		goToLabel(l14);
		etiqueta(l15);
		System.out.println(aux10 + "[" + aux10 + "_length] = " + aux13 + " ;");
		System.out.println(aux10 + "_length = " + aux10 + "_length + 1 ;");
		etiqueta(l16);
		System.out.println(aux11 + " = " + aux11 + " + 1 ;");
		goToLabel(l12);
		etiqueta(l17);
		return aux10;
	}
	
	public static String operacion(String op, String number1, String number2){
		number1 = convertirASCII(number1);
		number2 = convertirASCII(number2);
		
		if(tipo.containsKey(number1) && tipo.get(number1).contains("s_")){
			String res = null;
			boolean puerta = true;
			try{
				Integer.parseInt(number2);
				if(op.equals("+")) res = unionSetNumero(number1, number2);
				//if(op.equals("-")) res = resSetNumero(number1, number2);
				if(op.equals("*")){
					String aux34 = newVariableAuxiliar();
					System.out.println(aux34 + "[0] = " + number2);
					tipo.put(aux34, tipo.get(number2));
					res = interSet(number1, aux34);
				}
				puerta = false;
			}catch(Exception err){}
			if(puerta){
				if(op.equals("+")) res = unionSet(number1, number2);
				if(op.equals("-")) res = resSet(number1, number2);
				if(op.equals("*")) res = interSet(number1, number2);
			}
			return res;
		}else{
			String aux = newVariableAuxiliar();
			if(tipo.get(number1) != null && tipo.get(number2) != null){
				if(tipo.get(number1).equals("int") && tipo.get(number2).equals("float")){
					String aux2 = newVariableAuxiliar();
					System.out.println(aux + " = " + "(" + "float" + ") " + number1 + " ;");
					tipo.put(aux, "float");
					number1 = aux;
					aux = aux2;
				}else if(tipo.get(number1).equals("float") && tipo.get(number2).equals("int")){
					String aux2 = newVariableAuxiliar();
					System.out.println(aux + " = " + "(" + "float" + ") " + number2 + " ;");
					tipo.put(aux, "float");
					number2 = aux;
					aux = aux2;
				}
			}
			if(tipo.get(number1) != null){
				if(tipo.get(number1).equals("float")){
					op+="r";
					tipo.put(aux, "float");
				}
				else if(tipo.get(number1).equals("int")) tipo.put(aux, "int");
			}
			if(tipo.get(number2) != null){
				if(tipo.get(number2).equals("float")){
					op+="r";
					tipo.put(aux, "float");
				}
				else if(tipo.get(number2).equals("int")) tipo.put(aux, "int");
			}
			if(number1.contains(".") || number2.contains(".")){
				op+="r";
				tipo.put(aux, "float");
			}
			try{
				Integer.parseInt(number1);
				Integer.parseInt(number2);
				tipo.put(aux, "int");
			}catch(Exception err){}
			if(number2.equals("non")) System.out.println(aux + " = " + "-" + number1 + " ;");
			else System.out.println(aux + " = " + number1 + " " + op + " " + number2 + " ;");
			return aux;
		}
	}
		
		
	public static String conversion(String t, String e){
		String aux = null;
		if(t.equals("char")){
			if(tipo.containsKey(e)){
				if(tipo.get(e).equals("int")){
					aux = e;
					tipo.put(aux+"C", t);
				}
				else{
					print_error();
					System.out.println("halt;");
					System.exit(0);
				}
			}else{
				aux = e;
			}
			ECHAR = true;
		}else if(t.equals("int") && e.contains("'\\u")){
			aux = String.valueOf((int) Integer.parseInt(e.substring(3, 7), 16));
			ECHAR = false;
		}else if(t.equals("int") && tipo.containsKey(e) && tipo.get(e).equals("int")) {
			aux = e;
			ECHAR = false;
		}else if(t.equals("int") && tipo.containsKey(e) && tipo.get(e).equals("char")) {
			aux = e;
			ECHAR = false;
		}else if(t.equals("int") && e.contains("'")){
			aux = String.valueOf((int) e.charAt(1));
			ECHAR = false;
		}else{
			aux = newVariableAuxiliar();
			System.out.println(aux + " = " + "(" + t + ") " + e + " ;");
			tipo.put(aux, t);
			ECHAR = false;
		}
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
	
	public static String forEach(String e1, String e2, String tv){
		String tipoE2 = tipo.get(e2);
		if(tipo.get(e1).equals(tipoE2.substring(2,tipoE2.length()))){
			String aux5 = newVariableAuxiliar();
			String tf = generarTag();
			System.out.println(aux5 + " = 0 ;");
			etiqueta(tv);
			System.out.print("if (" + e2 + "_length" + " == " + aux5 + ") "); goToLabel(tf);
			System.out.println(e1 + " = " + e2 + "[" + aux5 + "] ;");
			System.out.println(aux5 + " = " + aux5 + " + 1 ;");
			return tf;
		}else{
			print_error();
			System.out.println("halt;");
			System.exit(0);
			return null;
		}
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
