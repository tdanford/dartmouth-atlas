package tdanford.dartmouthatlas.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.regex.*;

public interface SubjectExtracter<T extends AtlasSubject> {
	public T extractSubject(Row r);
	public int getNumSubjectCols();
}

class HRRSubjectExtracter implements SubjectExtracter<HRR> {

	public HRR extractSubject(Row r) {
		Cell c1 = r.getCell(0), c2 = r.getCell(1), c3 = r.getCell(2);
		Integer id = c1.getCellType() == Cell.CELL_TYPE_NUMERIC ? 
				(int)c1.getNumericCellValue() : 
				Integer.parseInt(c1.getStringCellValue());
		String name = c2.getStringCellValue();
		String stateName = c3.getStringCellValue();
		return new HRR(id, name, new State(stateName));
	}

	public int getNumSubjectCols() {
		return 3;
	} 
	
}

