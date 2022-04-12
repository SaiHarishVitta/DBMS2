import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class Main {

public static void main(String[] args) {
		List<Predicate<Sale>> predicates = new ArrayList<>();
		predicates.add((sale) -> sale.state.compareTo("NY") == 0);
		predicates.add((sale) -> sale.state.compareTo("NJ") == 0);
		predicates.add((sale) -> sale.state.compareTo("CT") == 0);
		
		EMFStructure emf = new EMFStructure(predicates);
		DbConnection.processData(emf);
		Map<Node, List<GroupingVariable>> records = emf.getResults();
		
		dumpOutput(records);
	}
	private static void dumpOutput(Map<Node, List<GroupingVariable>> records) {
		try {
			File file = new File("output.csv");
			if (file.exists()) {
				File dest = new File(file.getName() + ".backup");
				file.renameTo(dest);
			}
			
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			for (Map.Entry<Node, List<GroupingVariable>> record : records.entrySet()) {
				String line = "";
				line = line + record.getKey().cust + ", " + record.getKey().prod + ", " + record.getValue().get(0).sum_quant + ", " + record.getValue().get(1).sum_quant + ", " + record.getValue().get(2).sum_quant + ", " + record.getValue().get(0).max_quant + ", " + record.getValue().get(1).max_quant + ", " + record.getValue().get(2).max_quant + ", " + record.getValue().get(0).count + ", " + record.getValue().get(1).count + ", " + record.getValue().get(2).count + ", ";
				fw.write(line + "\n");
				//System.out.println(line);
			}
			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}