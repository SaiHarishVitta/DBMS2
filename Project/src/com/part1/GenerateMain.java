package com.part1;

import java.util.List;
import java.util.Map;

public class GenerateMain {

	private static String generateOutputDumper(List<String> gas, List<String> selAttrs) {
		String dumpMethod = 
				"	private static void dumpOutput(Map<Node, List<GroupingVariable>> records) {\n"
				+ "		try {\n" 
				+ "			File file = new File(\"output.csv\");\n"
				+ "			if (file.exists()) {\n"
				+ "				File dest = new File(file.getName() + \".backup\");\n"
				+ "				file.renameTo(dest);\n" 
				+ "			}\n" 
				+ "			\n"
				+ "			file.createNewFile();\n" 
				+ "			FileWriter fw = new FileWriter(file);\n"
				+ "			for (Map.Entry<Node, List<GroupingVariable>> record : records.entrySet()) {\n"
				+ "				String line = \"\";\n";

		dumpMethod += "				line = line";

		for (String selAttr : selAttrs) {
			if (selAttr.indexOf("_") == -1) {
				if(gas.contains(selAttr))
					dumpMethod += String.format(" + record.getKey().%s + \", \"", selAttr);
			} else {
				String[] parts = selAttr.split("_", 2);
				int index = Integer.parseInt(parts[0]) - 1;
				
				if(parts[1].startsWith("avg_")) {
					String[] individualParts = Utils.splitAvgVar(parts[1]);
					dumpMethod += String.format("+ (record.getValue().get(%d).%s / record.getValue().get(%d).%s) + \", \"", index, individualParts[0], index, individualParts[1]);
				} else {
					dumpMethod += String.format(" + record.getValue().get(%d).%s + \", \"", index, parts[1]);
				}
			}
		}

		// Loop through other collection forms and append to the line
		dumpMethod = dumpMethod 
				+ ";\n" 
				+ "				fw.write(line + \"\\n\");\n"
				+ "				//System.out.println(line);\n" 
				+ "			}\n" 
				+ "			\n"
				+ "			fw.close();\n" 
				+ "		} catch (IOException e) {\n" 
				+ "			e.printStackTrace();\n"
				+ "		}\n" 
				+ "	}\n";
		return dumpMethod;
	}
	
	private static String generateMainMethod(List<String> predicates, Map<String, String> schema) {
		String mainMethod = 
				"public static void main(String[] args) {\n"
				+ "		List<Predicate<Sale>> predicates = new ArrayList<>();\n";
		for(String predicate: predicates)
			mainMethod += String.format("		predicates.add(%s);\n", predicate);
		mainMethod = mainMethod
				+ "		\n"
				+ "		EMFStructure emf = new EMFStructure(predicates);\n"
				+ "		DbConnection.processData(emf);\n"
				+ "		Map<Node, List<GroupingVariable>> records = emf.getResults();\n"
				+ "		\n"
				+ "		dumpOutput(records);\n"
				+ "	}";
		return mainMethod;
	}
	
	public static String generateMainClass(List<String> gas, List<String> selAttrs, 
			List<String> predicates, Map<String, String> schema) {
		String mainClass = 
				"import java.io.*;\n"
				+ "import java.util.*;\n"
				+ "import java.util.function.Predicate;\n"
				+ "\n"
				+ "public class Main {\n"
				+ "\n"
				+ generateMainMethod(predicates, schema)
				+ "\n"
				+ generateOutputDumper(gas, selAttrs)
				+ "}";
		return mainClass;
	}

}
