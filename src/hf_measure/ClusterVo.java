package hf_measure;


import java.util.*;

public class ClusterVo implements Comparable<ClusterVo>{
	
	private Set<String> proteins = new HashSet<String>();  //the collection of the proteinss
	private String clusterid;
	private String term;  //term id
	private double fmeasure;
	
	private double hfmeasure;  //topological 
	private double hfmeasure2;  //untopological

	private String fmeasureFun;
	private int funnum;// the number of proteins who have functin term
	private int background;
	
	private String hfmeasureFun;
	private int hfunnum; 
	private int hbackground;
	
	private boolean cotainUnknown = false;   //是否包含功能未知的蛋白质
	private Set<String> probFunctions = new HashSet<String>(); //可能含有的功能
	private Set<String> unknownProteins = new HashSet<String>();
	
	
	private Map<String,Integer> edgeMap = new HashMap<String,Integer>();
	
	public String getClusterid() {
		return clusterid;
	}

	public void setClusterid(String clusterid) {
		this.clusterid = clusterid;
	}

	public Set<String> getProteins() {
		return proteins;
	}

	public void setProteins(Set<String> proteins) {
		this.proteins = proteins;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getFunnum() {
		return funnum;
	}

	public void setFunnum(int funnum) {
		this.funnum = funnum;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public String getFmeasureFun() {
		return fmeasureFun;
	}

	public void setFmeasureFun(String fmeasureFun) {
		this.fmeasureFun = fmeasureFun;
	}

	public String getHfmeasureFun() {
		return hfmeasureFun;
	}

	public void setHfmeasureFun(String hfmeasureFun) {
		this.hfmeasureFun = hfmeasureFun;
	}

	public int compareTo(ClusterVo c) {
		// TODO Auto-generated method stub
		double a = hfmeasure/fmeasure;
		double b = c.getHfmeasure()/c.getFmeasure();
		return (int) (a*100000000-b*100000000);
	}


	public int getHfunnum() {
		return hfunnum;
	}

	public void setHfunnum(int hfunnum) {
		this.hfunnum = hfunnum;
	}

	public int getHbackground() {
		return hbackground;
	}

	public void setHbackground(int hbackground) {
		this.hbackground = hbackground;
	}

	public boolean isCotainUnknown() {
		return cotainUnknown;
	}

	public void setCotainUnknown(boolean cotainUnknown) {
		this.cotainUnknown = cotainUnknown;
	}

	public Set<String> getProbFunctions() {
		return probFunctions;
	}

	public void setProbFunctions(Set<String> probFunctions) {
		this.probFunctions = probFunctions;
	}

	public Set<String> getUnknownProteins() {
		return unknownProteins;
	}

	public void setUnknownProteins(Set<String> unknownProteins) {
		this.unknownProteins = unknownProteins;
	}

	public Map<String, Integer> getEdgeMap() {
		return edgeMap;
	}

	public void setEdgeMap(Map<String, Integer> edgeMap) {
		this.edgeMap = edgeMap;
	}

	public double getFmeasure() {
		return fmeasure;
	}

	public void setFmeasure(double fmeasure) {
		this.fmeasure = fmeasure;
	}

	public double getHfmeasure() {
		return hfmeasure;
	}

	public void setHfmeasure(double hfmeasure) {
		this.hfmeasure = hfmeasure;
	}

	public double getHfmeasure2() {
		return hfmeasure2;
	}

	public void setHfmeasure2(double hfmeasure2) {
		this.hfmeasure2 = hfmeasure2;
	}
	
}
