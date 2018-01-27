package com.wuxuehong.plugin;

import java.util.ArrayList;
import java.util.Iterator;

public class Complex {
    private int complexID;		//the ID of the complex
    private ArrayList alNodes = null;
    private Integer seedNode;

    private int inDegree;		//the indegree of the complex
    private int totalDegree;	//the total degree of the complex
    private boolean mergeable;	//the flag showing if the complex is mergeable
    private boolean module;		//the flag showing if this complex can be defined as a module
    private double modularity;

	public Complex(){
		this.inDegree=0;
		this.totalDegree=0;
	}
	public Complex(int ID){
		alNodes=new ArrayList();
		this.complexID=ID;
		this.inDegree=0;
		this.totalDegree=0;
		this.mergeable=true;
		this.module=false;
		this.modularity=0.0;
	}
    public int getComplexID() {
		return complexID;
	}
	public void setComplexID(int complexID) {
		this.complexID = complexID;
	}
    public ArrayList getALNodes() {
        return alNodes;
    }
    public void setALNodes(ArrayList alNodes) {
        this.alNodes = alNodes;
    }
    public Integer getSeedNode() {
        return seedNode;
    }
    public void setSeedNode(Integer seedNode) {
        this.seedNode = seedNode;
    }
	public int getInDegree() {
		return inDegree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public boolean isMergeable() {
		return mergeable;
	}
	public void setMergeable(boolean mergeable) {
		this.mergeable = mergeable;
	}
	public boolean isModule() {
		return module;
	}
	public void setModule(boolean module) {
		this.module = module;
	}
	public int getTotalDegree() {
		return totalDegree;
	}
	public void setTotalDegree(int totalDegree) {
		this.totalDegree = totalDegree;
	}
	public double getModularity() {
		return modularity;
	}
	public void setModularity(double modularity) {
		this.modularity = modularity;
	}
	public void calModularity(Network currentNetwork){
    	int inDegree=0;
    	int totalDegree=0;
    	ArrayList nodes=this.getALNodes();
    	for(Iterator it=nodes.iterator();it.hasNext();){//for each node in merged C1
    		int node=((Integer)it.next()).intValue();
    		totalDegree+=currentNetwork.getDegree(node);//can this be useful?
    		int[] neighbors=currentNetwork.neighborsArray(node);
    		for(int i=0;i<neighbors.length;i++)
    			if(nodes.contains(new Integer(neighbors[i])))
    				inDegree++;
    	}
    	int outDegree=totalDegree-inDegree;
    	inDegree=inDegree/2;
    	this.setInDegree(inDegree);
    	this.setTotalDegree(totalDegree);
    	double fModule=0;
    	if(inDegree!=0)
    		fModule = (double)inDegree/(double)outDegree;
    	else	fModule=0;
    	setModularity(fModule);
	}
}
