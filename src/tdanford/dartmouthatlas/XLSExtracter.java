package tdanford.dartmouthatlas;

import java.util.*;
import java.io.*;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XLSExtracter {
	
	public static void main(String[] args) { 
		AtlasProperties atlas = new AtlasProperties();
		File dap = args.length > 0 ? new File(args[0]) : 
			new File(atlas.baseDir(), "DAP_Hosp_HRR_ST_01_05.xls");
		try {
			XLSExtracter extract = new XLSExtracter(dap, 0);

			for(int i = 0; i < extract.getNumRows(); i++) { 
				String[] row = extract.convertRow(extract.getRow(i));
				for(int j = 0; j < row.length; j++) { 
					if(j > 0) { System.out.print("\t"); }
					System.out.print(row[j]);
				}
				System.out.println();
			}
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Workbook workbook;
	private Sheet sheet;

	public XLSExtracter(File xlsFile, int sheetIdx) 
		throws IOException, InvalidFormatException { 
		
	    InputStream inp = new FileInputStream(xlsFile);
	    workbook = WorkbookFactory.create(inp);
	    sheet = workbook.getSheetAt(sheetIdx);
	}
	
	public int getMaxCols() {
		int cols = 0;
		for(int i = 0; i < getNumRows(); i++) { 
			cols = Math.max(cols, getRow(i).getPhysicalNumberOfCells());
		}
		return cols;
	}
	
	public int getNumRows() { 
		return sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	}
	
	public Row getRow(int i) { 
		return sheet.getRow(sheet.getFirstRowNum() + i);
	}
	
	public int getNumCols(int row) { 
		return getRow(row).getPhysicalNumberOfCells();
	}
	
	public Cell getCell(int i, int j) { 
		Row r = getRow(i);
		Cell c = r.getCell(j);
		return c;
	}
	
	public int getCellType(int i, int j) { 
		Cell c = getCell(i, j);
		return c.getCellType();
	}
	
	public String findHeader(int j) { 
		for(int i = 0; i < getNumRows(); i++) { 
			Row r = getRow(i);
			if(r.getPhysicalNumberOfCells() > j) { 
				Cell c = r.getCell(j);
				switch(c.getCellType()) { 
				case Cell.CELL_TYPE_BLANK: 
					break;
				case Cell.CELL_TYPE_STRING: 
					return c.getStringCellValue();
				default:
					return null;
				}
			}
		}
		return null;
	}
	
	
	public Object getCellValue(int i, int j) {
		Cell c = getCell(i, j);
		return cellValue(c);
	}
	
	private Object cellValue(Cell c) { 
		int type = c.getCellType();
		try { 
			switch(type) { 
			case Cell.CELL_TYPE_BLANK: return null;
			case Cell.CELL_TYPE_BOOLEAN: return (Boolean)c.getBooleanCellValue();
			case Cell.CELL_TYPE_NUMERIC: return (Double)c.getNumericCellValue();
			case Cell.CELL_TYPE_STRING: return c.getStringCellValue();
			default: return c.getStringCellValue();
			}
		} catch(Exception e) { 
			return null;
		}
	}
	
	public String[] convertRow(Row r) { 
		Iterator<Cell> cells = r.cellIterator();
		ArrayList<String> contents = new ArrayList<String>();
		while(cells.hasNext()) { 
			Cell cell = cells.next();
			Object cellValue = cell != null ? cellValue(cell) : "NULL";
			String value = cellValue != null ? cellValue.toString() : "NULL";
			contents.add(value);
		}
		return contents.toArray(new String[0]);
	}
}
