package com.wuxuehong.plugin;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class DensityDistributionSort extends ViewerSorter {

	private int column ;
	
	public void doSort(int column){
		this.column = column;
	}
	
	public int compare(Viewer viewer, Object e1, Object e2) {
		Cluster c1 = (Cluster)e1;
		Cluster c2 = (Cluster)e2;
		switch(column){
		case 1:{
		    Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));
		    Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
		    int clusterid = str1.compareTo(str2);
		    return clusterid;
		}
		case -1:{
			Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));
			Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
		    int clusterid = str2.compareTo(str1);
		    return clusterid;
		}
		case 2:{
			Integer str1 = c1.getNodeCount();
			Integer str2 = c2.getNodeCount();
			return str1.compareTo(str2);
		}
		case -2:{
			Integer str1 = c1.getNodeCount();
			Integer str2 = c2.getNodeCount();
			return str2.compareTo(str1);
		}
		case 3:{
			Integer str1 = c1.getEdgeCount();
			Integer str2 = c2.getEdgeCount();
			return str1.compareTo(str2);
		}
		case -3:{
			Integer str1 = c1.getEdgeCount();
			Integer str2 = c2.getEdgeCount();
			return str2.compareTo(str1);
		}
		case 4:{
			Float str1 = c1.getAvgDegree();
			Float str2 = c2.getAvgDegree();
			return str1.compareTo(str2);
		}
		case -4:{
			Float str1 = c1.getAvgDegree();
			Float str2 = c2.getAvgDegree();
			return str2.compareTo(str1);
		}
		case 5:{
			Float str1 = c1.getDensity();
			Float str2 = c2.getDensity();
			return str1.compareTo(str2);
		}
		case -5:{
			Float str1 = c1.getDensity();
			Float str2 = c2.getDensity();
			return str2.compareTo(str1);
		}
		}
		return 0;
	}
}
