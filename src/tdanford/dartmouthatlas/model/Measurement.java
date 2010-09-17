package tdanford.dartmouthatlas.model;

/**
 * A <tt>Measurement</tt> corresponds to a value which is measured by the 
 * Atlas dataset. For example: 
 * <blockquote>Medicare reimbursements for inpatient long stays per enrollee (1992)</blockquote>
 * is the name of a measurement taken from the 1992 spreadsheet for HRR reimbursement
 * data.  
 * 
 * A <tt>Measurement</tt> is uniquely identified by its name.  
 * 
 * @author tdanford
 */
public class Measurement {

	private String name;
	
	public Measurement(String n) { 
		name = n;
	}
	
	public String getName() { return name; }
	
	public String toString() { return name; }
	
	public int hashCode() { return name.hashCode(); }
	
	public boolean equals(Object o) { 
		return o instanceof Measurement && 
			((Measurement)o).name.equals(name);
	}
}
