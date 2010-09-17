package tdanford.dartmouthatlas;

import java.util.*;
import java.util.regex.*;
import java.io.*;

import tdanford.PropertiesParser;

public class AtlasProperties extends PropertiesParser {
	
	public AtlasProperties() { 
		this("dirs");
	}

	public AtlasProperties(String qualifiedName) {
		super(String.format("tdanford.dartmouthatlas.%s", qualifiedName));
	}

	public File baseDir() { 
		return new File(getStringValue("base"));
	}
	
	public File getDAPFile() { 
		return new File(baseDir(), getStringValue("dap"));
	}
	
	public File[] subDirs() { 
		return baseDir().listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isDirectory();
			} 
		});
	}
	
	public Map<String,File> findHRRReimbursementFiles() { 
		return findFiles("hrr_reimbursements");
	}
	
	public Map<String,File> findFiles(String tag) { 
		String patternString = getStringValue(tag);
		Pattern p = Pattern.compile(patternString);
		NamePatternFilter filter = new NamePatternFilter(p);
		File[] fs = baseDir().listFiles(filter);
		Map<String,File> filemap = new TreeMap<String,File>();
		for(int i = 0; i < fs.length; i++) { 
			filemap.put(filter.getTag(fs[i]), fs[i]);
		}
		return filemap;
	}
	
	private static class NamePatternFilter implements FileFilter {
		
		private Pattern pattern;
		
		public NamePatternFilter(Pattern p) { 
			pattern = p;
		}

		public boolean accept(File f) {
			Matcher m = pattern.matcher(f.getName());
			return m.matches();
		} 
		
		public String getTag(File f) {
			Matcher m = pattern.matcher(f.getName());
			if(m.matches()) { 
				return m.group(1);
			} else { 
				return f.getName();
			}
		}
		
	}
}
