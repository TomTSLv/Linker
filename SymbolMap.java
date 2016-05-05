import java.util.*;

public class SymbolMap {
	private TreeMap<String,Integer> symbolMap;
	
	private HashMap<String,String> errorMap;
	//Check if the variable is ever used in the symbol table
	private TreeMap<String,Boolean> usedMap;
	//Store in which module the variable is defined
	private HashMap<String,Integer> definitionMap;
	
	public SymbolMap(){
		symbolMap=new TreeMap<String,Integer>();
		errorMap=new HashMap<String,String>();
		usedMap=new TreeMap<String,Boolean>();
		definitionMap=new HashMap<String,Integer>();
	}
	
	public void addSymbol(String variable, int value){
		symbolMap.put(variable, value);
		usedMap.put(variable, false);
	}
	
	public void addError(String variable, String error){
		if (errorMap.containsKey(variable)) errorMap.put(variable, errorMap.get(variable)+" "+ error);
		else errorMap.put(variable, error);
	}

	public TreeMap<String, Integer> getSymbolMap() {
		return symbolMap;
	}

	public void setSymbolMap(TreeMap<String, Integer> symbolMap) {
		this.symbolMap = symbolMap;
	}

	public HashMap<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(HashMap<String, String> errorMap) {
		this.errorMap = errorMap;
	}
	
	public void changeUsedStatusToTrue(String variable){
		usedMap.put(variable, true);
	}

	public TreeMap<String, Boolean> getUsedMap() {
		return usedMap;
	}

	public void setUsedMap(TreeMap<String, Boolean> usedMap) {
		this.usedMap = usedMap;
	}
	
	public void addDefinition(String variable, int moduleNum){
		definitionMap.put(variable, moduleNum);
	}

	public HashMap<String, Integer> getDefinitionMap() {
		return definitionMap;
	}

	public void setDefinitionMap(HashMap<String, Integer> definitionMap) {
		this.definitionMap = definitionMap;
	}
	
}
