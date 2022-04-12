public class Sale {
	public String cust, prod;
	public int day, month, year;
	public String state;
	public int quant;
	
	public Sale(String cust, String prod, int day, int month, int year, String state, int quant) {
		super();
		this.cust = cust;
		this.prod = prod;
		this.day = day;
		this.month = month;
		this.year = year;
		this.state = state;
		this.quant = quant;
	}

	@Override
	public String toString() {
		return "{cust=" + cust + ", prod=" + prod + ", day=" + day + ", month=" + month + ", year=" + year
				+ ", state=" + state + ", quant=" + quant + "}";
	}
}
