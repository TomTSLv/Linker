import java.util.*;

public class Module {
	
	private int previousModuleLength;
	
	private HashMap<String,Integer> definitionList;
	
	private LinkedHashMap<String,ArrayList<Integer>> useList;
	
	private ArrayList<Integer> programText;
	
	private LinkedHashMap<Integer, Integer> addressMap;
	
	private HashMap<Integer, String> errorMap;

	public HashMap<String, Integer> getDefinitionList() {
		return definitionList;
	}

	public void setDefinitionList(HashMap<String, Integer> definitionList) {
		this.definitionList = definitionList;
	}

	public LinkedHashMap<String, ArrayList<Integer>> getUseList() {
		return useList;
	}

	public void setUseList(LinkedHashMap<String, ArrayList<Integer>> useList) {
		this.useList = useList;
	}

	public Module(){
		addressMap=new LinkedHashMap<Integer,Integer>();
		definitionList=new HashMap<String,Integer>();
		useList=new LinkedHashMap<String, ArrayList<Integer>>();
		errorMap=new HashMap<Integer, String>();
	}
	
	public int getPreviousModuleLength() {
		return previousModuleLength;
	}

	public void setPreviousModuleLength(int previousModuleLength) {
		this.previousModuleLength = previousModuleLength;
	}

	public void addDefinition(String definition, int num){
		definitionList.put(definition, num);
	}

	public void addUse(String variable, ArrayList<Integer> subList){
		useList.put(variable, subList);
	}

	public ArrayList<Integer> getProgramText() {
		return programText;
	}

	public void setProgramText(ArrayList<Integer> programText) {
		this.programText = programText;
		setAddressMap();
	}

	public LinkedHashMap<Integer, Integer> getAddressMap() {
		return addressMap;
	}

	public void setAddressMap(LinkedHashMap<Integer, Integer> addressMap) {
		this.addressMap = addressMap;
	}

	public void setAddressMap(){
		int count=0;
		for (Integer i:programText){
			addressMap.put(count, i);
			count++;
		}
	}
	
	public void addError(int address,String error){
		if (errorMap.containsKey(address)) errorMap.put(address, errorMap.get(address)+" "+ error);
		else errorMap.put(address, error);
	}

	public HashMap<Integer, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(HashMap<Integer, String> errorMap) {
		this.errorMap = errorMap;
	}
}
