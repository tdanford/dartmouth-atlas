package tdanford.dartmouthatlas.model;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import tdanford.dartmouthatlas.AtlasProperties;
import tdanford.dartmouthatlas.XLSExtracter;

public class HRRReimbursementDataset extends Dataset<HRR> {
	
	public static void main(String[] args) { 
		AtlasProperties props = new AtlasProperties();
		try {
			Map<String, File> fs = props.findHRRReimbursementFiles();
			HRRReimbursementDataset[] datasets = load(props);
			
			Dataset ds = datasets[datasets.length-2];
			Measurement[] ms = ds.measurements();
			for(int i = 0; i < ms.length; i++) { 
				System.out.println(String.format("%d: \t%s", i, ms[i]));
			}
			

			Iterator<String> itr = fs.keySet().iterator();
			for(int i = 0; i < datasets.length; i++) { 
				Dataset dds = datasets[i];
				Measurement[] mms = dds.measurements();
				Measurement m = mms[12];
				String key = itr.next();
				
				Statistic[] stats = dds.statistics(m);
				if(stats.length > 0) {
					double mean = Statistic.mean(stats);
					double var = Statistic.stddev(stats);

					System.out.println(String.format("\n%s", key));
					System.out.println(m.toString());
					System.out.println(String.format("Mean: %.2f", mean));
					System.out.println(String.format("STDDEV: %.2f", var));

					for(int k = 0; k < 10; k++) { 
						Statistic s = stats[stats.length-1-k];
						System.out.println(String.format("%d %s", k, s));
					}
				}
			}
			
			
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HRRReimbursementDataset[] load(AtlasProperties props) throws InvalidFormatException, IOException { 
		Map<String,File> files = props.findHRRReimbursementFiles();
		HRRReimbursementDataset[] ds = new HRRReimbursementDataset[files.size()];
		int k = 0;
		for(String fileKey : files.keySet()) { 
			int time = Integer.parseInt(fileKey);
			XLSExtracter ext = new XLSExtracter(files.get(fileKey), 0);
			ds[k++] = new HRRReimbursementDataset(time, ext);
			System.out.println(String.format("Loaded: %s", files.get(fileKey).getName()));
		}
		return ds;
	}

	public HRRReimbursementDataset(int t, XLSExtracter extract) { 
		super(t, extract, 1, new LabeledHRRSubjectExtracter());
	}
}

class LabeledHRRSubjectExtracter implements SubjectExtracter<HRR> {

	public HRR extractSubject(Row r) {
		Cell c1 = r.getCell(0), c2 = r.getCell(1), c3 = r.getCell(2);
		Integer id = c1.getCellType() == Cell.CELL_TYPE_NUMERIC ? 
				(int)c1.getNumericCellValue() : 
				Integer.parseInt(c1.getStringCellValue());
		if(id == 999) { return null; }
		String label = c2.getStringCellValue();
		Pattern p = Pattern.compile("(\\w+)-(.*)");
		Matcher m = p.matcher(label);
		if(!m.matches()) { throw new IllegalArgumentException(label); }
		String name = m.group(2), stateName = m.group(1);
		return new HRR(id, name, new State(stateName));
	}

	public int getNumSubjectCols() {
		return 2;
	} 
	
}

