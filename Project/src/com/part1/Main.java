package com.part1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	private static final String outputDir = "output/src/";
	private static final String outputClasses = "output/classes/";
	private static final String outputJarDir = "output/jar/";
	private static final String outputJarFile = "output.jar";
	private static Map<String, String> schema;
	
	public static void main(String[] args) throws Exception {
		// Store the variable and corresponding data type
		loadSchema();

		// Create an Output Folder for generating the whole code
		new File(outputDir).mkdirs();
		new File(outputClasses).mkdirs();
		new File(outputJarDir).mkdirs();
		
		// Copy static files from resources to output folder
		try {
			String resourcesDir = "resources/";
			Utils.copy(new File(resourcesDir + "Sale.java"), new File(outputDir + "Sale.java"));
			Utils.copy(new File(resourcesDir + "DbConnection.java"), new File(outputDir + "DbConnection.java"));
			Utils.copy(new File(resourcesDir + "EMFStructure.java"), new File(outputDir + "EMFStructure.java"));
			Utils.copy(new File(resourcesDir + "manifest.txt"), new File(outputDir + "manifest.txt"));
			Utils.copy(new File(resourcesDir + "postgresql-42.2.24.jar"), new File(outputClasses + "postgresql-42.2.24.jar"));
		} catch (IOException e) {
			System.err.println("Failed in copying files from resources dir to output dir " + e);
		}

		List<String> gas = new ArrayList<>();
		gas.add("cust");
		gas.add("prod");
		String nodeClass = NodeGenerator.generate(gas, schema);
		Utils.dump(nodeClass, outputDir + "Node.java");
		
		List<String> aggregates = new ArrayList<>();
		aggregates.add("sum_quant");
		aggregates.add("min_quant");
		aggregates.add("max_quant");
		aggregates.add("count");
		String gvClass = GVGenerator.generate(aggregates, schema);
		Utils.dump(gvClass, outputDir + "GroupingVariable.java");
		// System.out.println(emfClass);
		
		List<String> selAttrs = new ArrayList<>();
		selAttrs.add("cust");
		selAttrs.add("prod");
		selAttrs.add("1_sum_quant");
		selAttrs.add("2_sum_quant");
		selAttrs.add("3_sum_quant");
		
		selAttrs.add("1_max_quant");
		selAttrs.add("2_max_quant");
		selAttrs.add("3_max_quant");
		
		selAttrs.add("1_count");
		selAttrs.add("2_count");
		selAttrs.add("3_count");
		
		List<String> conditions = new ArrayList<>();
		conditions.add("1.state='NY'");
		conditions.add("2.state='NJ'");
		conditions.add("3.state='CT'");
		List<String> predicates = GeneratePredicates.parseSigmas(conditions, schema);
		
		String mainClass = GenerateMain.generateMainClass(gas, selAttrs, predicates, schema);
		Utils.dump(mainClass, outputDir + "Main.java");
		
		
		
		pack(outputDir, outputClasses);
	}
	
	private static void loadSchema() {
		schema = new HashMap<>();
		schema.put("cust", "String");
		schema.put("prod", "String");
		schema.put("day", "int");
		schema.put("month", "int");
		schema.put("year", "int");
		schema.put("state", "String");
		schema.put("quant", "int");
		
	}
	
	public static void pack(String outputSrc, String outputDest) {
		// Compile the .java files in outputSrc to outputDest
		File dir = new File(outputSrc);
		File[] javaFilesFromSrc = dir.listFiles((curDir, fileName) -> fileName.endsWith(".java"));
		String cmd = "javac -d " + outputDest;
		for(File javaFile: javaFilesFromSrc) {
			cmd += " " + outputSrc + javaFile.getName();
		}
		Utils.exec(cmd);
		
		// Extract the postgre-sql.jar file in outputDest
		String extractCmd = String.format("jar -xf postgresql-42.2.24.jar");
		Utils.execInDir(extractCmd, outputDest);
		
		
		String packageCmd = String.format("jar cmvf %s/manifest.txt %s/%s -C %s .", outputDir, outputJarDir, outputJarFile, outputClasses);
		System.out.println(packageCmd);
		Utils.exec(packageCmd);
	}


}
