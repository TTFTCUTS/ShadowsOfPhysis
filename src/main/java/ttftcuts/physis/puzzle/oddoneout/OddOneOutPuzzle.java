package ttftcuts.physis.puzzle.oddoneout;

import java.util.List;

public class OddOneOutPuzzle {
	public int solution;
	public List<OddOneOutOption> options;
	
	public OddOneOutPuzzle(List<OddOneOutOption> options, int solution) {
		this.options = options;
		this.solution = solution;
	}
	
	@Override
	public String toString() {
		String s = "Puzzle( ";
		
		for(int i=0; i<options.size(); i++) {
			s += options.get(i);
			if (solution == i) {
				s +="(answer)";
			}
			if (i < options.size()-1) {
				s+=", ";
			}
		}
		s+= " )";
		return s;
	}
}
