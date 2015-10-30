package javaToUML;

import java.io.FileInputStream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ClassOrInterfaceVisitor extends VoidVisitorAdapter {
	private boolean isInterface = false; 
	private String Name;
	private String extend;
	private String implement;
	
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		if(n.getName().toString() != null){
			Name = n.getName().toString();
			if(n.isInterface()){
				isInterface = true;
			}else{
				if(n.getExtends().toString()!= "[]"){
					extend = n.getExtends().toString();
				}
				if(n.getImplements().toString()!= "[]"){
					implement = n.getImplements().toString();
				}
			}
		}
    }
	
	public boolean isInt(){
		return isInterface;
	}
	public String getClassName(){
		return Name;
	}
	public String getExtend(){
		return extend;
	}
	public String getImp(){
		return implement;
	}
	
	public static void main(String[] args) throws Exception{
		FileInputStream in = new FileInputStream("B2.java");

        CompilationUnit cu;
        try {
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        ClassOrInterfaceVisitor civ = new ClassOrInterfaceVisitor();
        civ.visit(cu, null);
        System.out.println(civ.getClassName());
        System.out.println(civ.getExtend());
        System.out.println(civ.getImp());
        System.out.println(civ.isInt());
	}
}
