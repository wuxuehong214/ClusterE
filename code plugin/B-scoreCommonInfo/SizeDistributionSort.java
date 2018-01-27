package com.wuxuehong.plugin;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class SizeDistributionSort extends ViewerSorter {

	private int column;
	
	public void doSort(int column){
		this.column = column;
	}
	
	
	public int compare(Viewer viewer, Object e1, Object e2) {
		Clum c1 = (Clum)e1;
		Clum c2 = (Clum)e2;
		switch(column){
		case 1:{
			Integer str1 = c1.size;
			Integer str2 = c2.size;
			return str1.compareTo(str2);
		}
		case -1:{
			Integer str1 = c1.size;
			Integer str2 = c2.size;
			return str2.compareTo(str1);
		}
		case 2:{
			Integer str1 = c1.value;
			Integer str2 = c2.value;
			return str1.compareTo(str2);
		}
		case -2:{
			Integer str1 = c1.value;
			Integer str2 = c2.value;
			return str2.compareTo(str1);
		}
		}
		return 0;
	}

}
