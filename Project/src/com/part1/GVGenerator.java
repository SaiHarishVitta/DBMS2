package com.part1;

import java.util.List;
import java.util.Map;

public class GVGenerator {
	
	public static String generate(List<String> aggregates, Map<String, String> schema) {
		String gvClass = 
				"import java.util.function.Predicate;\n"
				+ "\n"
				+ "public class GroupingVariable {\n"
				+ "	\n"
				+ "	public Predicate<Sale> predicate;\n";
		
		for(String aggregate: aggregates)
			gvClass += String.format("	public long %s = 0;\n", aggregate);
			
		gvClass = gvClass
				+ "	\n"
				+ "	public GroupingVariable(Predicate<Sale> predicate) {\n"
				+ "		this.predicate = predicate;\n"
				+ "	}\n"
				+ "	\n"
				+ "	public void process(Sale sale) {\n"
				+ "		// Record doesn't come under this grouping Variable\n"
				+ "		if(!predicate.test(sale)) return;\n"
				+ "		\n"
				+ "		// Process the record for all the aggregate functions\n";
		
		for(String aggregate: aggregates) {
			String[] aggParts = aggregate.split("_");
			String func = aggParts[0];
			if(func.equalsIgnoreCase("SUM")) {
				gvClass += String.format("		this.%s += sale.%s;\n", aggregate, aggParts[1]);
			} else if(func.equalsIgnoreCase("MAX")) {
				gvClass += String.format("		this.%s = Math.max(sale.%s, %s);\n", aggregate, aggParts[1], aggregate);
			} else if(func.equalsIgnoreCase("MIN")) {
				gvClass += String.format("		this.%s = Math.min(sale.%s, %s);\n", aggregate, aggParts[1], aggregate);
			} else if(func.equalsIgnoreCase("COUNT")) {
				gvClass += String.format("		this.%s++;\n", aggregate);
			} else {
				throw new RuntimeException("Unknown Aggregate Found - " + func);
			}
		}
		
		gvClass = gvClass
				+ "	}\n"
				+ "}";
		return gvClass;
	}

}
