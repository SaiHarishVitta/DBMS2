import java.util.*;

public class Node {
    public String cust;
    public String prod;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hash(cust, prod);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        return this.cust.equals(other.cust) && this.prod.equals(other.prod);		}
}