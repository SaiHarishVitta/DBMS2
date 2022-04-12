package com.part1;

import java.util.List;
import java.util.Map;

public class NodeGenerator {
	public static String generate(List<String> gas, Map<String, String> schema) {
		String nodeClass = 
				"import java.util.*;\n"
				+ "\n"
				+ "public class Node {\n";
		
		for(String ga: gas) {
			nodeClass += String.format("    public %s %s;\n", schema.get(ga), ga);
		}
		
		nodeClass = nodeClass
				+ "    \n"
				+ "    @Override\n"
				+ "    public int hashCode() {\n"
				+ "        final int prime = 31;\n"
				+ "        int result = 1;\n"
				+ "        result = prime * result + Objects.hash(cust, prod);\n"
				+ "        return result;\n"
				+ "    }\n"
				+ "\n"
				+ "    @Override\n"
				+ "    public boolean equals(Object obj) {\n"
				+ "        if (this == obj)\n"
				+ "            return true;\n"
				+ "        if (obj == null)\n"
				+ "            return false;\n"
				+ "        if (getClass() != obj.getClass())\n"
				+ "            return false;\n"
				+ "        Node other = (Node) obj;\n"
				+ "        return ";
		
		for(int i = 0; i < gas.size(); i++) {
			String ga = gas.get(i);
			String line = "";
			if(schema.get(ga).equals("String")) {
				line = "this.%s.equals(other.%s)";
			} else {
				line = "this.%s == other.%s";
			}
			
			if(i == gas.size() - 1)
				line += ";";
			else
				line += " && ";
			nodeClass += String.format(line, ga, ga);
		}
		
		nodeClass = nodeClass
				+ "		}\n"
				+ "}";
		return nodeClass;
	}

}
