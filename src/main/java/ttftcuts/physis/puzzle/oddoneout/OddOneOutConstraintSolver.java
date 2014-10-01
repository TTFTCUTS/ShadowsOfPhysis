package ttftcuts.physis.puzzle.oddoneout;

import java.util.ArrayList;
import java.util.List;

import ttftcuts.physis.utils.TPair;

public class OddOneOutConstraintSolver {

	public static List<OddOneOutOption> permutations = new ArrayList<OddOneOutOption>();
	
	public static void makePermutationList() {
		for (int n = OddOneOutProperty.numSymbols.min - 1; n < OddOneOutProperty.numSymbols.max; n++) {
			makeSymbols(0, n+1, 3, 3, new ArrayList<TPair<Integer>>(), permutations);
		}
		
		for(OddOneOutOption o : permutations) {
			o.calculateProperties();
		}
	}
	
	private static void makeSymbols(int d, int n, int nc, int ns, List<TPair<Integer>> sym, List<OddOneOutOption> states) {
		if (d >= n) {
			OddOneOutOption state = new OddOneOutOption();
			for (int i=0; i<sym.size(); i++) {
				state.addSymbol(sym.get(i));
			}
			states.add(state);
		} else {
			for (int x = 0; x < nc; x++) {
				for (int y = 0; y < ns; y++) {
					List<TPair<Integer>> news = new ArrayList<TPair<Integer>>();
					news.addAll(sym);
					news.add(new TPair<Integer>(x,y));
					makeSymbols(d+1, n, nc, ns, news, states);
				}
			}
		}
	}
	
	public static OddOneOutPuzzle buildPuzzle(int difficulty) {

		List<OddOneOutOption> options = new ArrayList<OddOneOutOption>();
		
		options.add(permutations.get(0));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		
		return new OddOneOutPuzzle(options, 0);
	}
}
