package tdanford.dartmouthatlas.model;

/**
 * State represents a (U.S.) state in which Medicare data has been collected
 * and aggregated.  States are identified by their (unique) String names.  
 * 
 * @author tdanford
 */
public class State implements AtlasSubject {

	private String name;
	
	public State(String n) { 
		name = n;
	}
	
	public String getName() { return name; }
	
	public String toString() { return name; }
	
	public int hashCode() { return name.hashCode(); }
	
	public boolean equals(Object o) { 
		return o instanceof State && 
			((State)o).name.equals(name);
	}

	public boolean partOf(AtlasSubject s) {
		return false;
	}
}
