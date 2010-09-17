package tdanford.dartmouthatlas.model;

/**
 * A <tt>Statistic</tt> is a triple of a (<tt>AtlasSubject</tt>, <tt>Measurement</tt>, value) 
 * where "value" is a real number.  The <tt>Statistic</tt> also carries a "time" associated
 * with it, represented by an integer, for use in assembling time series data.  
 * 
 * (So I suppose that a <tt>Statistic</tt> is actually a <i>quadruple</i> of subject, 
 * measurement, value, and time.)  
 * 
 * @author tdanford
 *
 * @param <T>
 */
public class Statistic<T extends AtlasSubject> implements Comparable<Statistic<T>> {

	public T subject;
	public Double value;
	public int time;
	public Measurement measure;
	
	public Statistic(T subj, Measurement m, int t, Double v) {
		subject = subj;
		measure = m;
		time = t;
		value = v;
	}
	
	public String toString() { 
		return String.format("%.2f = %s", value, subject);
	}
	
	public int hashCode() { 
		int code = measure.hashCode(); code *= 37;
		code += time; code *= 37;
		code += subject.hashCode(); code *= 37;
		return code;
	}
	
	public boolean equals(Object o) { 
		if(!(o instanceof Statistic)) { return false; }
		Statistic s = (Statistic)o;
		return s.subject.equals(subject) && s.measure.equals(measure) && s.time == time;
	}

	public int compareTo(Statistic<T> o) {
		return value.compareTo(o.value);
	}

	public static double mean(Statistic[] stats) {
		double sum = 0.0;
		for(int i = 0; i < stats.length; i++) { 
			sum += stats[i].value;
		}
		sum /= (double)stats.length;
		return sum;
	}
	
	public static double stddev(Statistic[] stats) { 
		double mean = mean(stats);
		double sum = 0.0;
		for(int i = 0; i < stats.length; i++) { 
			double d = stats[i].value - mean;
			sum += (d*d);
		}
		sum /= (double)stats.length;
		return Math.sqrt(sum);
	}
}
