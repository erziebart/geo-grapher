package geographer;

import java.util.ArrayList;
import java.util.Comparator;

public class FunctionList {
	private ArrayList<Function> list;
	
	private static class FuncComp implements Comparator<Function> {
		@Override
		public int compare(Function f1, Function f2) {
			return f1.name.compareTo(f2.name);
		}
	}
	
	FunctionList() {
		list = new ArrayList<Function>();
	}
	
	void loadFunctionList() {
		// generic
		list.add(new Functs.NatLog());
		list.add(new Functs.Log());
		list.add(new Functs.Sqrt());
		list.add(new Functs.Exp());
		list.add(new Functs.E());
		
		// trigonometric
		list.add(new Trig.Sine());
		list.add(new Trig.Cosine());
		list.add(new Trig.Tangent());
		list.add(new Trig.Cotangent());
		list.add(new Trig.Secant());
		list.add(new Trig.Cosecant());
		list.add(new Trig.Pi());
		list.add(new Trig.Tau());
		list.add(new Trig.Arcsine());
		list.add(new Trig.Arccosine());
		list.add(new Trig.Arctangent());
		list.add(new Trig.Arccotangent());
		list.add(new Trig.Arcsecant());
		list.add(new Trig.Arccosecant());
		list.add(new Trig.ToDegree());
		list.add(new Trig.ToRadian());
		
		// hyperbolic trigonometric
		list.add(new HyperTrig.HyperSine());
		list.add(new HyperTrig.HyperCosine());
		list.add(new HyperTrig.HyperTangent());
		list.add(new HyperTrig.HyperCotangent());
		list.add(new HyperTrig.HyperSecant());
		list.add(new HyperTrig.HyperCosecant());
		list.add(new HyperTrig.HyperSineInverse());
		list.add(new HyperTrig.HyperCosineInverse());
		list.add(new HyperTrig.HyperTangentInverse());
		list.add(new HyperTrig.HyperCotangentInverse());
		list.add(new HyperTrig.HyperSecantInverse());
		list.add(new HyperTrig.HyperCosecantInverse());
		
		sortList();
	}
	
	// sorts the functions alphabetically by their name
	void sortList() {
		list.sort(new FuncComp());
	}
	
	// uses binary search to add the given Function to the list in its correct alphabetical location
	void add(Function f)  throws FunctionAlreadyInListException {
		FuncComp comp = new FuncComp();
		
		int start, end;
		start = 0;
		end = list.size();
		int result = 1;
		
		while(start < end) {
			int mid = (start + end) / 2;
			result = comp.compare(f, list.get(mid));
			
			if(result < 0) {
				end = mid;
			} else if(result > 0) {
				start = mid + 1;
			} else {
				end = mid;
				start = mid + 1;
			}
		}
		
		if(result != 0) {
			list.add(start, f);
		} else {
			throw new FunctionAlreadyInListException();
		}
		
	}
	
	// adds all the functions in the given FunctionList
	public void addAll(FunctionList fl) {
		for(int i = 0; i < fl.list.size(); i++) {
			try {
				add(fl.list.get(i));
			} catch(FunctionAlreadyInListException ex) {}
		}
	}
	
	
	// uses binary search to return whether a name is in the list
	boolean isInList(String name) {
		int start, end;
		start = 0;
		end = list.size();
		
		while(start < end) {
			int mid = (start + end) / 2;
			Function current = list.get(mid);
			int result = name.compareTo(current.name);
			
			if(result < 0) {
				end = mid;
			} else if(result > 0) {
				start = mid + 1;
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	// uses a binary search to return the Function with the requested name
	Function get(String name) {
		int start, end;
		start = 0;
		end = list.size();
		
		while(start < end) {
			int mid = (start + end) / 2;
			Function current = list.get(mid);
			int result = name.compareTo(current.name);
			
			if(result < 0) {
				end = mid;
			} else if(result > 0) {
				start = mid + 1;
			} else {
				return current;
			}
		}
		
		return null;
	}
}
