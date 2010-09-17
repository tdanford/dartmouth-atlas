package tdanford.dartmouthatlas.model;

import java.util.*;
import java.io.*;

import org.apache.poi.ss.usermodel.Cell;

import tdanford.dartmouthatlas.*;

/**
 * A Dataset object corresponds to a table of data from the Atlas dataset.  
 * 
 * The columns correspond to <tt>Measurement</tt> objects.  The (ordered) array 
 * of Measurement objects can be accessed with the <tt>measurements()</tt> method.
 * 
 * The rows of the table correspond to <tt>T</tt> objects (where <tt>T</tt> is a type parameter
 * to the Dataset class, and must be a sub-class of <tt>AtlasSubject</tt>).  The
 * complete set of subjects for this table can be retrieved using the <tt>subjects()</tt> 
 * method. 
 * 
 * Associated with every pair of T and Measurement is a Statistic<T> object,
 * which gives the value of the measurement for that subject.  These can be 
 * accessed through <tt>getStatistic()</tt>.
 * 
 * @author tdanford
 *
 * @param <T>
 */
public class Dataset<T extends AtlasSubject> {

	private Map<T,Map<Measurement,Statistic<T>>> measurements;
	private ArrayList<T> subjects;
	private ArrayList<Measurement> measures;
	private int time;
	
	public Dataset(int t, XLSExtracter extract, int h, SubjectExtracter<T> subjExtract) {
		time = t;
		measurements = new HashMap<T,Map<Measurement,Statistic<T>>>();
		subjects = new ArrayList<T>();
		measures = new ArrayList<Measurement>();
		
		int rows = extract.getNumRows();
		int dataRows = rows - h;
		
		for(int j = subjExtract.getNumSubjectCols(); j < extract.getNumCols(h-1); j++) { 
			String header = extract.findHeader(j);
			Measurement m = new Measurement(header);
			measures.add(m);
			//System.out.println(String.format("Measure: %d = \"%s\"", measures.size()-1, measures.get(measures.size()-1)));
		}
		
		for(int i = 0; i < dataRows; i++) { 
			T subj = subjExtract.extractSubject(extract.getRow(h+i));
			if(subj == null) { 
				continue;
			}

			subjects.add(subj);
			
			for(int j = 0; j < measures.size(); j++) { 
				int jj = subjExtract.getNumSubjectCols() + j;
				int ctype = extract.getCellType(i, jj);
				Double value = ctype == Cell.CELL_TYPE_NUMERIC ? 
						(Double)extract.getCellValue(i, jj) : null;
				Measurement m = measures.get(j);
				if(m == null) { 
					throw new IllegalArgumentException(String.format(
							"%d = null", j));
				}
				Statistic<T> stat = new Statistic<T>(subj, m, time, value);
				addStatistic(stat);
			}
		}
	}
	
	public Collection<T> subjects() { 
		return subjects;
	}
	
	public Measurement[] measurements() { 
		return measures.toArray(new Measurement[0]);
	}
	
	public int getTime() { return time; }
	
	public Statistic<T> getStatistic(T subject, Measurement m) { 
		if(measurements.containsKey(subject)) { 
			if(measurements.get(subject).containsKey(m)) { 
				return measurements.get(subject).get(m);
			}
		}
		return null;
	}
	
	public Statistic[] statistics(Measurement m) { 
		ArrayList<Statistic> stats = new ArrayList<Statistic>();
		for(T subject : measurements.keySet()) { 
			if(measurements.get(subject).containsKey(m)) { 
				Statistic stat = measurements.get(subject).get(m);
				if(stat.value != null) { 
					stats.add(stat);
				}
			}
		}
		Statistic[] statarray = stats.toArray(new Statistic[0]);
		Arrays.sort(statarray);
		return statarray;
	}
	
	public void addStatistic(Statistic<T> s) { 
		if(!measurements.containsKey(s.subject)) { 
			measurements.put(s.subject, new HashMap<Measurement,Statistic<T>>());
		}
		measurements.get(s.subject).put(s.measure, s);
		//System.out.println(String.format("+ %s", s.toString()));
	}
}
