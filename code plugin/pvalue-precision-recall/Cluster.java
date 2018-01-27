package com.wuxuehong.plugin;

import java.util.Vector;

import com.wuxuehong.bean.Node;

public class Cluster implements Comparable {

	private String ClusterID;

	private Vector<Node> proteins;

	private double precision;

	private double pvalue;

	private double recall;
	
	private double measure;

	private int funProtein; // 复合物中含有某种功能的蛋白质数量

	private int netProtein; // 蛋白质网络中含有该功能的蛋白质数量

	private String functionCode ;

	private String function = "";
	
	private int probableFunctions;
	
	public int getProbableFunctions() {
		return probableFunctions;
	}

	public void setProbableFunctions(int probableFunctions) {
		this.probableFunctions = probableFunctions;
	}

	public static String comparePara = "P-value";
	

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public int getFunProtein() {
		return funProtein;
	}

	public void setFunProtein(int funProtein) {
		this.funProtein = funProtein;
	}

	public int getNetProtein() {
		return netProtein;
	}

	public void setNetProtein(int netProtein) {
		this.netProtein = netProtein;
	}

	public Cluster() {

	}

	public String getClusterID() {
		return ClusterID;
	}

	public void setClusterID(String clusterID) {
		ClusterID = clusterID;
	}

	public Vector<Node> getProteins() {
		return proteins;
	}

	public void setProteins(Vector<Node> proteins) {
		this.proteins = proteins;
	}

	public double getPrecision() {
		return (double)funProtein/(double)proteins.size();
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getPvalue() {
		return pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}

	public double getRecall() {
		return (double)funProtein/(double)netProtein;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}
	
	public double getMeasure() {
		return 2*getRecall()*getPrecision()/(getRecall()+getPrecision());
	}

	public void setMeasure(double measure) {
		this.measure = measure;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Cluster c = (Cluster) o;
		if(comparePara.equals("P-value"))
		return new Double(getPvalue()).compareTo(new Double(c.getPvalue()));
		else if(comparePara.equals("Precision"))
			return new Double(getPrecision()).compareTo(new Double(c.getPrecision()));
		else if(comparePara.equals("Recall"))
			return new Double(getRecall()).compareTo(new Double(c.getRecall()));
		else if(comparePara.equals("f-measure"))
			return new Double(getMeasure()).compareTo(new Double(c.getMeasure()));
		return 1;
	}

}
