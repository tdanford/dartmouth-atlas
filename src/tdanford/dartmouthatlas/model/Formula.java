package tdanford.dartmouthatlas.model;

public interface Formula<T extends AtlasSubject> {
	
	public Measurement predicts();
	public Measurement[] requires();
	public Double estimate(Statistic<T>[] stats);
}

class SumFormula<T extends AtlasSubject> implements Formula<T> { 
	
	private Measurement[] args;
	private Measurement value;
	
	public SumFormula(Measurement v, Measurement... arg) { 
		if(arg.length == 0) { 
			throw new IllegalArgumentException(String.format("arg.length=%d", arg.length));
		}
		value = v;
		args = arg.clone();
	}

	public Double estimate(Statistic<T>[] stats) {
		if(stats.length != args.length) { 
			throw new IllegalArgumentException(String.format("stats.length=%d (!= %d)",
					stats.length, args.length));
		}
		double sum = 0.0;
		
		for(int i = 0; i < stats.length; i++) { 
			if(!stats[i].measure.equals(args[i])) { 
				throw new IllegalArgumentException(String.format("%s != %s",
						stats[i].measure.toString(), args[i].toString()));
			}
			
			sum += stats[i].value;
		}
		return sum;
	}

	public Measurement predicts() {
		return value;
	}

	public Measurement[] requires() {
		return args;
	}
}