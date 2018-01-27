package com.wuxuehong.plugin;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class PvalueSort extends ViewerSorter {


	private int column;
	private String para;
	
	public PvalueSort(String para){
		this.para = para;
	}
	
	public void doSort(int column){
		this.column = column;
	}
	
	public int compare(Viewer viewer, Object e1, Object e2) {
		Cluster c1 = (Cluster)e1;
		Cluster c2 = (Cluster)e2;
		if(para.equals("Pvalue")){
		switch(column){
		case 1:{
			Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));   //cluster id
			Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
			return str1.compareTo(str2);
	     	}
		case -1:{
			Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));
			Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
			return str2.compareTo(str1);
	     	}
		case 2:{
			Integer str1 = c1.getProteins().size();                //一个cluster中节点个数
			Integer str2 = c2.getProteins().size();
			return str1.compareTo(str2);
	     	}
		case -2:{
			Integer str1 = c1.getProteins().size();
			Integer str2 = c2.getProteins().size();
			return str2.compareTo(str1);
	     	}
		case 3:{
			Integer str1 = c1.getNetProtein();               //整个网络中含有某种功能的蛋白质数量
			Integer str2 = c2.getNetProtein();
			return str1.compareTo(str2);
	     	}
		case -3:{
			Integer str1 = c1.getNetProtein();
			Integer str2 = c2.getNetProtein();
			return str2.compareTo(str1);
	     	}
		case 4:{
			Integer str1 = c1.getFunProtein();          //当前蛋白中含有某种功能的蛋白质数量
			Integer str2 = c2.getFunProtein();
			return str1.compareTo(str2);
	     	}
		case -4:{
			Integer str1 = c1.getFunProtein();
			Integer str2 = c2.getFunProtein();
			return str2.compareTo(str1);
	     	}
		case 5:{
			Double str1 = c1.getPvalue();                        //pvalue
			Double str2 = c2.getPvalue();
			return str1.compareTo(str2);
	     	}
		case -5:{
			Double str1 = c1.getPvalue();
			Double str2 = c2.getPvalue();
			return str2.compareTo(str1);
	     	}
		case 6:{
			String str1 = c1.getFunctionCode();                 //function code
			String str2 = c2.getFunctionCode();
			return str1.compareTo(str2);
	     	}
		case -6:{
			String str1 = c1.getFunctionCode();
			String str2 = c2.getFunctionCode();
			return str2.compareTo(str1);
	     	}
		case 7:{
			String str1 = c1.getFunction();                           //function
			String str2 = c2.getFunction();
			return str1.compareTo(str2);
	     	}
		case -7:{
			String str1 = c1.getFunction();
			String str2 = c2.getFunction();
			return str2.compareTo(str1);
	     	}
		case 8:{
			Integer str1 = c1.getProbableFunctions();               //probable functions
			Integer str2 = c2.getProbableFunctions();
			return str1.compareTo(str2);
	     	}
		case -8:{
			Integer str1 = c1.getProbableFunctions();
			Integer str2 = c2.getProbableFunctions();
			return str2.compareTo(str1);
	     	}
		}
		}
		else {
			switch(column){
			case 1:{
				Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));   //cluster id
				Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
				return str1.compareTo(str2);
		     	}
			case -1:{
				Integer str1 = Integer.parseInt(c1.getClusterID().substring(7));
				Integer str2 = Integer.parseInt(c2.getClusterID().substring(7));
				return str2.compareTo(str1);
		     	}
			case 2:{
				Integer str1 = c1.getProteins().size();                //一个cluster中节点个数
				Integer str2 = c2.getProteins().size();
				return str1.compareTo(str2);
		     	}
			case -2:{
				Integer str1 = c1.getProteins().size();
				Integer str2 = c2.getProteins().size();
				return str2.compareTo(str1);
		     	}
			case 3:{
				Integer str1 = c1.getNetProtein();               //整个网络中含有某种功能的蛋白质数量
				Integer str2 = c2.getNetProtein();
				return str1.compareTo(str2);
		     	}
			case -3:{
				Integer str1 = c1.getNetProtein();
				Integer str2 = c2.getNetProtein();
				return str2.compareTo(str1);
		     	}
			case 4:{
				Integer str1 = c1.getFunProtein();          //当前蛋白中含有某种功能的蛋白质数量
				Integer str2 = c2.getFunProtein();
				return str1.compareTo(str2);
		     	}
			case -4:{
				Integer str1 = c1.getFunProtein();
				Integer str2 = c2.getFunProtein();
				return str2.compareTo(str1);
		     	}
			case 5:{
				Double str1 = null;
				Double str2 = null;
				if(para.equals("Precision")){
				str1 = c1.getPrecision();          //当前蛋白中含有某种功能的蛋白质数量
				str2 = c2.getPrecision();
				}else if(para.equals("Recall")){
					str1 = c1.getRecall();
					str2 = c2.getRecall();
				}else if(para.equals("f-measure")){
					str1 = c1.getMeasure();
					str2 = c2.getMeasure();
				}
				return str1.compareTo(str2);
		     	}
			case -5:{
				Double str1 = null;
				Double str2 = null;
				if(para.equals("Precision")){
				str1 = c1.getPrecision();          //当前蛋白中含有某种功能的蛋白质数量
				str2 = c2.getPrecision();
				}else if(para.equals("Recall")){
					str1 = c1.getRecall();
					str2 = c2.getRecall();
				}else if(para.equals("f-measure")){
					str1 = c1.getMeasure();
					str2 = c2.getMeasure();
				}
				return str2.compareTo(str1);
		     	}
			}
		}
		return 0;
	}
	
}
