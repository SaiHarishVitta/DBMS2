import java.util.*;
import java.util.function.Predicate;

public class EMFStructure {
	List<Predicate<Sale>> predicates = new ArrayList<>();
	Map<Node, List<GroupingVariable>> records = new HashMap<>();
	
	public EMFStructure(List<Predicate<Sale>> predicates) {
		this.predicates = predicates;
	}
	
	public Node generateKey(Sale sale) {
		Node node = new Node();
		node.cust = sale.cust;
		node.prod = sale.prod;
		return node;
	}
	
	public void process(Sale sale) {
		Node keyNode = generateKey(sale);
		if(!records.containsKey(keyNode)) {
			List<GroupingVariable> gvs = new ArrayList<>();
			for(int i = 0; i < predicates.size(); i++) {
				GroupingVariable gv = new GroupingVariable(predicates.get(i));
				gvs.add(gv);
			}
			records.put(keyNode, gvs);
		}
		
		// Updating the grouping variables
		List<GroupingVariable> gvs = records.get(keyNode);
		for(GroupingVariable gv: gvs) {
			gv.process(sale);
		}
	}
	
	public Map<Node, List<GroupingVariable>> getResults() {
		return records;
	}
}
