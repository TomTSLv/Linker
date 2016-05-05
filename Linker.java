import java.util.*;

public class Linker {
	public static void main(String[] args){
		System.out.println("Please enter the input: \n");
		Scanner in=new Scanner(System.in);
		ArrayList<Module> moduleList=getModuleList(in);
		moduleList=setPreviousModuleLength(moduleList);
		SymbolMap symbolTable=new SymbolMap();
		symbolTable=createSymbolTable(moduleList,symbolTable);
		String output="\nSymbol Table\n";
		for (String variable:symbolTable.getSymbolMap().keySet()){
			output=output.concat(variable+"="+symbolTable.getSymbolMap().get(variable)+" ");
			if (symbolTable.getErrorMap().containsKey(variable)) output=output.concat(symbolTable.getErrorMap().get(variable));
			output=output.concat("\n");
		}
		output=output.concat("\nMemory Map\n");
		String warningMessage=refreshAddressMap(moduleList,symbolTable);
		for (Module m:moduleList){
			for (int row:m.getAddressMap().keySet()){
				output=output.concat((row+m.getPreviousModuleLength())+": "+m.getAddressMap().get(row)+" ");
				if (m.getErrorMap().containsKey(row)) output=output.concat(m.getErrorMap().get(row));
				output=output.concat("\n");
			}
		}
		output=output.concat("\n"+warningMessage);
		System.out.println(output);
	}
	
	//Get the input from Java Scanner and put the data into a list of modules
	private static ArrayList<Module> getModuleList(Scanner in){
		ArrayList<Module> moduleList=new ArrayList<Module>();
		while (in.hasNext()){
			Module module=new Module();
			String firstInModule=in.next();
			if (firstInModule.equals(".") || firstInModule.length()>1) break;
			else{
				int numDefs=Integer.parseInt(firstInModule);
				String name;
				int value;
				for (int i=0;i<numDefs;i++){
					name=in.next();
					value=in.nextInt();
					module.addDefinition(name, value);
				}//Import definition list for each module
				int numUses=in.nextInt();
				ArrayList<Integer> useList;
				for (int j=0;j<numUses;j++){
					useList=new ArrayList<Integer>();
					name=in.next();
					value=in.nextInt();
					while(value!=-1){
						useList.add(value);
						value=in.nextInt();
					}
					module.addUse(name, useList);
				}//Import use list for each module
				int programNum=in.nextInt();
				ArrayList<Integer> programList=new ArrayList<Integer>();
				for (int k=0;k<programNum;k++){
					programList.add(in.nextInt());
				}
				module.setProgramText(programList);//Import program text for each module
				moduleList.add(module);
			}
		}
		in.close();
		return moduleList;
	}
	
	//Calculate the length of all previous modules for each module. Used the create the symbol map and calculate relative addresses.
	private static ArrayList<Module> setPreviousModuleLength(ArrayList<Module> moduleList){
		int count=1;
		int length=0;
		int previous=0;
		for (Module m : moduleList){
			if (count==1) {
				m.setPreviousModuleLength(0);
			}
			else {
				m.setPreviousModuleLength(length+previous);
			}
			length=m.getProgramText().size();
			previous=m.getPreviousModuleLength();
			count++;
		}
		return moduleList;
	}
	
	//Create the symbol Table 
	private static SymbolMap createSymbolTable(ArrayList<Module> moduleList, SymbolMap symbolTable){
		HashMap<String,Integer> definitionList;
		for (Module m: moduleList){
			definitionList=m.getDefinitionList();
			for (String variable:definitionList.keySet()){
				//If the variable is already in the symbol table, then it's multiply defined.
				if (symbolTable.getSymbolMap().containsKey(variable)) symbolTable.addError(variable, "Error: This variable is multiply defined; last value used.");
				//Judge if the definition exceeds the module size by comparing it with the length of the program text.
				if (definitionList.get(variable)>=m.getProgramText().size()) {
					symbolTable.addError(variable, "Error: Definition exceeds module size; last word in module used.");
					symbolTable.addSymbol(variable, m.getProgramText().size()-1+m.getPreviousModuleLength());
				}
				else symbolTable.addSymbol(variable, definitionList.get(variable)+m.getPreviousModuleLength());
				symbolTable.addDefinition(variable, moduleList.indexOf(m));
			}
		}
		return symbolTable;
	}
	
	//Refresh the addressMap in each module.
	private static String refreshAddressMap(ArrayList<Module> moduleList, SymbolMap symbolTable){
		String warningMessage="";
		for (Module m:moduleList){
			HashMap<String,ArrayList<Integer>> useList=m.getUseList();
			HashMap<Integer,Integer> addressMap=m.getAddressMap();
			//Process the use list in each module to solve all the external addresses
			for (String variable:useList.keySet()){
				int externalAddress;
				if (symbolTable.getSymbolMap().containsKey(variable)) externalAddress=symbolTable.getSymbolMap().get(variable);
				else {
					externalAddress=111;
					for (int num:useList.get(variable)) m.addError(num, "Error: "+variable+" is not defined; 111 used.");
				}
				for (int num:useList.get(variable)){
					int updateAddress;
					if (num>=m.getProgramText().size()){
						m.addError(num, "Error: Use exceeds module size; ignore this use and continue.");
						continue;
					}
					symbolTable.changeUsedStatusToTrue(variable);//Make the variable as used. Used in generating the warning message
					int address=addressMap.get(num);
					if (address/10000!=0){
						if (address%10==4){
							updateAddress=address/10000*1000+externalAddress;
							m.getAddressMap().put(num, updateAddress);
						}
					}
					else{
						m.addError(num, "Error: Multiple variables used in instruction; all but last ignored.");
						updateAddress=address/1000*1000+externalAddress;
						m.getAddressMap().put(num, updateAddress);
					}
				}
			}
			//Loop the addressMap to solve all the other addresses, including absolute, immediate and relative
			for (int row:addressMap.keySet()){
				int address=addressMap.get(row);
				if (address/10000!=0){
					//relative address
					if (address%10==3){
						if (address/10%1000>=m.getProgramText().size()){
							m.addError(row, "Error: Relative address exceeds module size; largest module address used.");
							addressMap.put(row, address/10000*1000+m.getProgramText().size()-1+m.getPreviousModuleLength());
						}
						else addressMap.put(row, address/10+m.getPreviousModuleLength());
					}
					//Immediate address
					else if (address%10==1) addressMap.put(row, address/10);
					//Absolute address
					else if (address%10==2){
						if (address/10%1000>300){
							m.addError(row, "Error: Absolute address exceeds machine size; largest address used.");
							addressMap.put(row, address/10000*1000+299);
						}
						else addressMap.put(row, address/10);
					}
				}
			}
		}
		//Get warning messages if there's any
		for (String variable : symbolTable.getUsedMap().keySet()){
			if (!symbolTable.getUsedMap().get(variable)) warningMessage=warningMessage.concat("Warning: "+variable+" was defined in module "
																						+symbolTable.getDefinitionMap().get(variable)+" but never used.\n");
		}
		return warningMessage;
	}
}
