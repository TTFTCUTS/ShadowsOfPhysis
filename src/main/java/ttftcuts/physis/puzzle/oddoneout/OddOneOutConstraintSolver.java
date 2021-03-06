package ttftcuts.physis.puzzle.oddoneout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ttftcuts.physis.utils.TPair;

public class OddOneOutConstraintSolver {
	private static OddOneOutConstraintSolver instance;
	
	public static int[] symbolsPerLevel = {1,1,2,2,2,2,2,2,2,2};
	public static int[] shapesPerLevel 	= {1,2,2,2,2,3,3,3,3,3};
	public static int[] coloursPerLevel = {2,2,2,3,3,3,3,3,3,3};
	public static boolean[] negativesPerLevel = {false,false,false,false,false,true,true,true,true,true};
	
	public static List<List<OddOneOutOption>> permutations;// = new ArrayList<OddOneOutOption>();
	
	public static void makePermutationList() {
		instance = new OddOneOutConstraintSolver();
		
		permutations = new ArrayList<List<OddOneOutOption>>();
		
		for (int level = 0; level < 10; level++) {
			List<OddOneOutOption> levelpermutations = new ArrayList<OddOneOutOption>();
			
			//for (int n = OddOneOutProperty.numSymbols.min - 1; n < OddOneOutProperty.numSymbols.max; n++) {
			for (int n = 0; n < symbolsPerLevel[level]; n++) {
				makeSymbols(0, n+1, coloursPerLevel[level], shapesPerLevel[level], new ArrayList<TPair<Integer>>(), levelpermutations);
			}
			
			for(OddOneOutOption o : levelpermutations) {
				o.calculateProperties();
			}
			
			permutations.add(levelpermutations);
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
	
	public static OddOneOutPuzzle buildPuzzle(int difficulty, int seed) {
		Random rand = new Random(seed); // add a seed!
		
		List<Integer> variables = new ArrayList<Integer>();		
		Map<Integer, List<OddOneOutOption>> domains = new HashMap<Integer, List<OddOneOutOption>>();
		
		for (int i = 1; i<=6; i++) {
			List<OddOneOutOption> d = new ArrayList<OddOneOutOption>(permutations.size());
			d.addAll(permutations.get(difficulty));
			Collections.shuffle(d, rand);
			domains.put(i, d);
			variables.add(i);
		}
		
		CSP csp = instance.new CSP(variables, domains, instance.new Constraint(variables, negativesPerLevel[difficulty]));
		Map<Integer, OddOneOutOption> optmap = backtrackingSearch(csp, new HashMap<Integer,OddOneOutOption>());
		
		List<OddOneOutOption> options = new ArrayList<OddOneOutOption>();
		
		/*options.add(permutations.get(0));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));
		options.add(permutations.get(1));*/
		
		for(OddOneOutOption o : optmap.values()) {
			options.add(o);
		}
		
		return new OddOneOutPuzzle(options, csp.constraint.previousAnswer);
	}
	
	private class Constraint {
		public List<Integer> variables;
		public int previousAnswer = 0;
		public boolean allowNegatives = false;
		
		public Constraint(List<Integer> positions, boolean allowNegatives) {
			this.variables = positions;
			this.allowNegatives = allowNegatives;
		}
		
		public boolean isSatisfied(Map<Integer, OddOneOutOption> assignment) {
			if (assignment.size() != this.variables.size()) { return true; }
			
			Map<OddOneOutProperty, Map<Integer,Integer>> totals = new HashMap<OddOneOutProperty, Map<Integer,Integer>>();
			Map<OddOneOutProperty, Map<Integer,OddOneOutOption>> lastStates = new HashMap<OddOneOutProperty, Map<Integer,OddOneOutOption>>();
			for(OddOneOutProperty p : OddOneOutProperty.propertyList) {
				totals.put(p, new HashMap<Integer,Integer>());
				lastStates.put(p, new HashMap<Integer,OddOneOutOption>());
				for (int i=p.min; i<= p.max; i++) {
					totals.get(p).put(i, 0);
					lastStates.get(p).put(i, null);
				}
			}
			
			for(int key : assignment.keySet()) {
				OddOneOutOption s = assignment.get(key);
				
				for(OddOneOutProperty p : s.properties.keySet()) {
					int val = s.properties.get(p);
					
					totals.get(p).put(val, totals.get(p).get(val) + 1);
					lastStates.get(p).put(val, s);
				}
			}
			
			int odds = 0;
			
			OddOneOutOption oddstate = null;
			
			for(OddOneOutProperty p : totals.keySet()) {
				Map<Integer,Integer> data = totals.get(p);
				
				for (int number : data.keySet()) {
					int n = data.get(number);
					
					if (n==1 || (allowNegatives && n == 5)) {
						OddOneOutOption st = lastStates.get(p).get(number);
						if (oddstate != null && oddstate != st) { return false; }
						oddstate = st;
						odds++;
					}
				}
			}
			
			if (odds > 0) {
				int i = 0;
				for (OddOneOutOption s : assignment.values()) {
					if (s == oddstate) {
						this.previousAnswer = i;
						break;
					}
					i++;
				}
			}
			
			return odds > 0;
		}
	}
	
	private class CSP {
		final List<Integer> variables;
		final Map<Integer, List<OddOneOutOption>> domains;
		Constraint constraint;
		
		public CSP(List<Integer> variables, Map<Integer, List<OddOneOutOption>> domains, Constraint constraint) {
			this.variables = variables;
			this.domains = domains;
			this.constraint = constraint;
		}
	}
	
	private static Map<Integer, OddOneOutOption> backtrackingSearch(CSP csp, Map<Integer, OddOneOutOption> assignment) {
		if (assignment.size() == csp.variables.size()) { return assignment; }
		
		int variable = selectUnassignedVariable(assignment, csp);
		
		for(OddOneOutOption value : csp.domains.get(variable)) {
			Map<Integer, OddOneOutOption> oldAssignment = new HashMap<Integer, OddOneOutOption>();
			oldAssignment.putAll(assignment);
			
			if (isConsistent(variable, value, assignment, csp)) {
				assignment.put(variable, value);
				
				Map<Integer, OddOneOutOption> result = backtrackingSearch(csp, assignment);
				if (result != null) {
					return result;
				}
			}
			assignment = oldAssignment;
		}
		return null;
	}
	
	private static int selectUnassignedVariable(Map<Integer, OddOneOutOption> assignment, CSP csp) {
		int maxRemainingValues = 0;
		int maxVariable = 0;
		for(int variable : csp.variables) {
			if (!assignment.containsKey(variable)) {
				if (csp.domains.get(variable).size() > maxRemainingValues) {
					maxRemainingValues = csp.domains.get(variable).size();
					maxVariable = variable;
				}
			}
		}
		return maxVariable;
	}
	
	private static boolean isConsistent(int variable, OddOneOutOption value, Map<Integer, OddOneOutOption> assignment, CSP csp) {
		Map<Integer, OddOneOutOption> tempAssignment = new HashMap<Integer, OddOneOutOption>();
		tempAssignment.putAll(assignment);
		tempAssignment.put(variable, value);
		
		return csp.constraint.isSatisfied(tempAssignment);
	}
}
