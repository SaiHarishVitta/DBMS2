package com.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GeneratePredicates {
	
	public static List<String> parseSigmas(List<String> sigmas, Map<String, String> schema) {
		List<String> predicates = new ArrayList<>();
		
		for(String sigma: sigmas) {
			String predicate = "(sale) -> " + parseSigma(sigma, schema) + "";
			predicates.add(predicate);
		}
		return predicates;
	}
	
	/**
	 * 1.state = 'NJ' and 1.quant > 1000
	 */
	private static String parseSigma(String sigma, Map<String, String> schema) {
		Pattern pattern = Pattern.compile("and|or|AND|OR");
		Matcher matches = pattern.matcher(sigma);
		String parsedCondition = "";
		
		int index = 0;
		while(matches.find(index)) {
			// Format Operator
			String operator = "";
			String logicalOperator = matches.group().toLowerCase();
			if(logicalOperator.equals("and")) operator = " && ";
			else operator = " || ";
			
			String condition = sigma.substring(index, matches.start());
			parsedCondition += parseCondition(condition.trim(), schema) + operator;
			index = matches.end() + 1;
		}
		
		
		parsedCondition += parseCondition(sigma.substring(index).trim(), schema);
		return parsedCondition;
	}
	
	/**
	 * 1.state = 'NJ' or 1.quant > 1000
	 */
	private static String parseCondition(String condition, Map<String, String> schema) {
		int index = condition.indexOf(".");
		condition = condition.substring(index+1);	//state = 'NJ' or quant > 1000
		
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9']+");
        Matcher matcher = pattern.matcher(condition);
        if(!matcher.find()) return null;
        
        String property = condition.substring(0, matcher.start());
        String value = condition.substring(matcher.end());
        String operator = condition.substring(matcher.start(), matcher.end()).trim();
        if(operator.equals("=")) operator = "==";
        if(schema.get(property).equalsIgnoreCase("string")) {
        	value = value.replaceAll("'", "\"");
        	return String.format("sale.%s.compareTo(%s) %s 0", property, value, operator);
        } else {
        	return String.format("sale.%s %s %s", property, operator, value);
        }
        
	}	
}
