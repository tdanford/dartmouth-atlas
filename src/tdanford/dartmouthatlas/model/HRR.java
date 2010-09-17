package tdanford.dartmouthatlas.model;

import java.util.*;
import java.util.regex.*;
import java.io.*;

/**
 * HRR is a "Hospital Referral Region," and is the lowest-level unit of measurement
 * in the Atlas Dataset.
 * 
 * An HRR is identified by the combination of its number, name, and state.
 * 
 * @author tdanford
 */
public class HRR implements AtlasSubject {

	private int number;
	private String name;
	private State state;
	
	public HRR(int n, String nm, State st) { 
		number = n;
		name = nm;
		state = st;
	}
	
	public int hashCode() { 
		return name.hashCode();
	}
	
	public boolean equals(Object o) { 
		if(!( o instanceof HRR)) { return false; }
		HRR h = (HRR)o;
		return number == h.number && name.equals(h.name) && state.equals(h.state);
	}
	
	public String toString() { 
		return String.format("(#%d) %s %s", number, name, state.toString());
	}

	public boolean partOf(AtlasSubject s) {
		if(s instanceof State) { 
			State st = (State)s;
			return st.equals(state);
		}
		return false;
	}
}
