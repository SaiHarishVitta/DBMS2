import java.util.function.Predicate;

public class GroupingVariable {
	
	public Predicate<Sale> predicate;
	public long sum_quant = 0;
	public long min_quant = 0;
	public long max_quant = 0;
	public long count = 0;
	
	public GroupingVariable(Predicate<Sale> predicate) {
		this.predicate = predicate;
	}
	
	public void process(Sale sale) {
		// Record doesn't come under this grouping Variable
		if(!predicate.test(sale)) return;
		
		// Process the record for all the aggregate functions
		this.sum_quant += sale.quant;
		this.min_quant = Math.min(sale.quant, min_quant);
		this.max_quant = Math.max(sale.quant, max_quant);
		this.count++;
	}
}