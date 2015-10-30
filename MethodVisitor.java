package javaToUML;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter {
	private ArrayList<String> mFormat = new ArrayList<String>();
	private ArrayList<String> pType = new ArrayList<String>();
	private ArrayList<String> nPrime = new ArrayList<String>();
	private ArrayList<String> nonReserveReturnType = new ArrayList<String>();
	private ArrayList<String> reserveTypes = new ArrayList<String>(Arrays.asList("void","byte","short","int","long","float","double","boolean","char","Integer","String", "Character"));
	private ArrayList<String> bType = new ArrayList<String>();
	
	public void visit(MethodDeclaration n, Object arg){
		StringBuilder sb = new StringBuilder();
		
		if(n.getName().toString()==null){
			mFormat = null;
		}else{
			switch(n.getModifiers()){
				case 0: 
					sb.append("+");break;
				case 1:
					sb.append("+");break;
				case 2:
					return;
				case 4:
					sb.append("#");break;
				case 17:
					sb.append("+");break;
				case 9: 
					sb.append("+");break;
				case 25:
					sb.append("+");break;
				case 1025: 
					sb.append("+");break;
			}
			sb.append(n.getName().toString());
			if (n.getName().equals("main")) {
				String varBlock = n.getBody().getStmts().get(0).toString();
				String[] lines = varBlock.split(";");
				String[] temp;
				for(String line : lines){
					temp = line.split(" ");
					bType.add(temp[0]);
				}
			}
			sb.append("(");
			if(n.getParameters().isEmpty()){
				sb.append("):");
			}else{
				String parameter = "";
				String param = n.getParameters().toString();
				param = param.substring(param.indexOf("[")+1, param.lastIndexOf("]"));
				String[] params = param.split(",");
				for(int i = 0; i < params.length; i++){
					params[i] = params[i].replaceAll("^\\s*|\\s*$", "");
				}
				for(int i = 0; i < params.length; i++){
					String[] oneparam = params[i].split(" ");
					parameter = parameter + oneparam[1]+ ":" + oneparam[0] + ",";
					pType.add(oneparam[0]);
				}
				parameter = parameter.substring(0, parameter.length()-1);
				sb.append(parameter);
				sb.append("):");
			}
			for(int i = 0; i < pType.size(); i++){
				String thistype = pType.get(i);
				if(!reserveTypes.contains(thistype)){
					String substr;
					if(pType.get(i).contains("<")){
						substr = pType.get(i).substring(thistype.indexOf("<")+1, thistype.indexOf(">"));
						if(nPrime.contains(substr)){
							continue;
						}else{
							nPrime.add(substr);
						}
					}else if(thistype.contains("[")){
						substr = thistype.substring(0, thistype.indexOf("["));
						if(nPrime.contains(substr)){
							continue;
						}else{
							if(!reserveTypes.contains(substr)){
								nPrime.add(substr);
							}
						}
					}else{
						if(!nPrime.contains(thistype)){
							nPrime.add(thistype);
						}
					}
				}
			}
			String type = n.getType().toString();
			sb.append(type);
			String result = sb.toString();
			mFormat.add(result);
			if(!reserveTypes.contains(type)){ 
				if(type.contains("<")){ 
					type = type.substring(type.indexOf("<")+1, type.indexOf(">"));
					if(!nonReserveReturnType.contains(type)){ 
						nonReserveReturnType.add(type); 
					} 
				}else if(type.contains("[")){ 
					type = type.substring(0, type.indexOf("[")); 
					if(!nonReserveReturnType.contains(type)){ 
						if(!reserveTypes.contains(type)){ 
							nonReserveReturnType.add(type); 
						} 
					} 
				}else{ 
					if(!nonReserveReturnType.contains(type)){ 
						nonReserveReturnType.add(type); 
					} 
				} 
			} 
		}
	}
	
	public ArrayList<String> getNPrimeType(){
		return nPrime;
	}
	
	public ArrayList<String> getMFormat(){
		return mFormat;
	}
	
	public ArrayList<String> getNonReserveReturnType(){
		return nonReserveReturnType;
	}
	
	public ArrayList<String> getBType() {
		return bType;
	}
	
	public static void main(String[] args) throws Exception{
		FileInputStream in = new FileInputStream("sortArray.java");
        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        MethodVisitor mv = new MethodVisitor();
        mv.visit(cu, null);
	    System.out.println(mv.getMFormat());
	    System.out.println(mv.getNPrimeType());
	    System.out.println(mv.getNonReserveReturnType());
	}
}
