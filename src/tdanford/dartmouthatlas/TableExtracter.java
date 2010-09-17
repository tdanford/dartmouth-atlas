package tdanford.dartmouthatlas;

import java.io.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class TableExtracter extends XLSExtracter {
	
	private StringBuilder tableFile;
	
	public TableExtracter(File xlsFile) throws InvalidFormatException, IOException {
		this(xlsFile, 0);
	}

	public TableExtracter(File xlsFile, Integer sheetIdx) throws IOException, InvalidFormatException {
		super(xlsFile, sheetIdx);
		
		tableFile = new StringBuilder();
		for(int i = 0; i < getNumRows(); i++) { 
			String[] row = convertRow(getRow(i));
			for(int j = 0; j < row.length; j++) { 
				if(j > 0) { tableFile.append("\t"); }
				tableFile.append(row[j]);
			}
			tableFile.append("\n");
		}
	}
	
	public String asString() { 
		return tableFile.toString();
	}
	
	public void writeFile(File f) throws IOException { 
		PrintStream ps = new PrintStream(new FileOutputStream(f));
		ps.print(tableFile.toString());
		ps.close();
	}

}
