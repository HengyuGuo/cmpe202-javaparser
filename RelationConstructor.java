package javaToUML;

import java.util.ArrayList;

public class RelationConstructor{
	private ArrayList<String> pType;
	private ArrayList<String> npMethodType;
	private ArrayList<String> constructionPT;
	private ArrayList<String> methodBody;
	private ArrayList<String> fileName;
	private ArrayList<String> relations;
	private ArrayList<String> connectionFormat;
	private ArrayList<String> npVarType;
	private ArrayList<String> itfList;
	private String extendClass;
	private String currentClass;
	private String itfName;
		
	public RelationConstructor(FieldVisitor fv, ClassOrInterfaceVisitor cin, ArrayList<String> fileName, ArrayList<String> itfList, ArrayList<String> cf, MethodVisitor mv, ConstructorVisitor cv){
		this.fileName = fileName;
		this.itfList = itfList;
		this.pType = mv.getNPrimeType();
		this.connectionFormat = cf;
		this.methodBody = mv.getMFormat();
		this.extendClass = cin.getExtend();
		this.currentClass = cin.getClassName();
		this.itfName = cin.getImp();
		this.relations = new ArrayList<String>();
		this.npVarType = fv.getNonReserve();
		this.npMethodType = mv.getNonReserveReturnType();
		this.constructionPT = cv.getNoReserveType();	
	}
	
	public void BuildRelationship(){
		if (extendClass != null){	
			extendClass = extendClass.substring(extendClass.indexOf("[")+1, extendClass.indexOf("]"));
			relations.add(extendClass+" <|-- "+ currentClass);
		}
		if(itfName != null){			
			if(itfName.split(",").length > 1){
				itfName = itfName.substring(itfName.indexOf("[")+1, itfName.indexOf("]"));
				String[] interFace = itfName.split(",");
				for(int i = 0; i < interFace.length; i++){
					interFace[i] = interFace[i].replaceAll("^\\s*|\\s*$", "");
					relations.add(interFace[i] + " <|.. " + currentClass);
				}
			}else{
				itfName = itfName.substring(itfName.indexOf("[")+1, itfName.indexOf("]"));
				relations.add(itfName + " <|.. " + currentClass);
			}
		}
		if(constructionPT != null){
			for(int i = 0; i < constructionPT.size(); i++){
				if(fileName.contains(constructionPT.get(i))){
					if(itfList.contains(constructionPT.get(i))){
						if(itfList.contains(currentClass)){
							continue;
						}
						relations.add(currentClass+" ..> "+constructionPT.get(i)+":use");
						continue;
					}
				}
			}
		}
		if(npVarType != null){
			for(int i = 0; i < npVarType.size(); i++){
				boolean multi = false, pair = false;
				int index=0;
				String temp = npVarType.get(i);
				if(npVarType.get(i).contains("*")){
					multi= true;
					temp = temp.substring(0, temp.length()-1);
				}
				if(fileName.contains(temp)) {			
					if(itfList.contains(temp)){
						for(int idx=connectionFormat.size() - 1;idx>=0;idx--){
							if(connectionFormat.get(idx).equals(temp) && connectionFormat.get(idx).equals(currentClass)){ //revise
								pair = true;
								index = idx;
								break;
							}
						}
					}else{
						for(int idx=connectionFormat.size() - 1;idx>=0;idx--){
							if(connectionFormat.get(idx).contains(temp) && connectionFormat.get(idx).contains(currentClass)){ //revise
								pair = true;
								index = idx;
								break;
							}
						}
					}
					int check = temp.compareTo(currentClass);
					if(!pair){
						if(check < 0){
							if(multi)
								connectionFormat.add(temp+ " \"*\" " +" -- "+currentClass);
							else
								connectionFormat.add(temp+ " \"1\" " +" -- "+currentClass);
						}
						else if(check > 0){
							if(multi)
								connectionFormat.add(currentClass+" -- "+ " \"*\" " + temp);
							else
								connectionFormat.add(currentClass+" -- "+ " \"1\" " + temp);
						}
					}else{
						String str = connectionFormat.get(index);
						
						if(check < 0){
							int pos = str.indexOf("-");
							if(multi){
								str = str.substring(0, pos) + " \"*\" " + str.substring(pos, str.length());
								connectionFormat.set(index, str);
							}
							else{
								str = str.substring(0, pos) + " \"1\" " + str.substring(pos, str.length());
								connectionFormat.set(index, str);
							}
						}
						else if(check > 0){
							int pos = str.lastIndexOf("-");
							if(multi){
								str = str.substring(0, pos+1) + " \"*\" " + str.substring(pos+1, str.length());
								connectionFormat.set(index, str);
							}
							else{
								str = str.substring(0, pos+1) + " \"1\" " + str.substring(pos+1, str.length());
								connectionFormat.set(index, str);
							}
						}
					}
				}
			}
		}
		if(pType != null){
			for(int i = 0; i < pType.size(); i++){
				if(fileName.contains(pType.get(i))){
					if(itfList.contains(pType.get(i))){
						if(itfList.contains(currentClass)){
							continue;
						}
						relations.add(currentClass+" ..> "+pType.get(i)+":use");
						continue;
					}
					int check = pType.get(i).compareTo(currentClass);
					if(check < 0)
						relations.add(pType.get(i)+"--"+currentClass);
					else if(check > 0)
						relations.add(currentClass + "--" + pType.get(i));
				}
			}
		}
		if(npMethodType != null){
			for(int i = 0; i < npMethodType.size(); i++){
				if (fileName.contains(npMethodType.get(i))) {
					if (itfList.contains(npMethodType.get(i))) {
						if (itfList.contains(currentClass)) {
							continue;
						}
						relations.add(currentClass + " ..> " + npMethodType.get(i) + ":use");
						continue;
					}
					int check = npMethodType.get(i).compareTo(currentClass);
					if (check < 0)
						relations.add(npMethodType.get(i) + "--" + currentClass);
					else if (check > 0)
						relations.add(currentClass + "--" + npMethodType.get(i));
				}
			}
		}
		if (methodBody != null) {
			for (int i = 0; i < methodBody.size(); i++) {
				if (itfList.contains(methodBody.get(i))) {
					if(itfList.contains(currentClass)) {
						continue;
					}
					relations.add(currentClass + " ..> " + methodBody.get(i) + ":use");
				}
			}
		}
	}
	
	public ArrayList<String> getConnectionFormat(){
		return connectionFormat;
	}
	
	public ArrayList<String> getRelations(){
		return relations;
	}
}
