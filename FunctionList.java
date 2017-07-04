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
		list.add(new Functs.NatLog());
		list.add(new Functs.Log());
		list.add(new Functs.Sqrt());
		list.add(new Functs.Exp());
		list.add(new Functs.E());
		
		sortList();
	}
	
	// sorts the functions alphabetically by their name
	void sortList() {
		list.sort(new FuncComp());
	}
	
	// uses binary search to add the given Function to the list in its correct alphabetical location
	void add(Function f) {
		FuncComp comp = new FuncComp();
		
		int start, end;
		start = 0;
		end = list.size();
		
		while(start < end) {
			int mid = (start + end) / 2;
			int result = comp.compare(f, list.get(mid));
			
			if(result < 0) {
				end = mid;
			} else if(result > 0) {
				start = mid + 1;
			} else {
				end = mid;
				start = mid + 1;
			}
		}
		
		list.add(start, f);
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
