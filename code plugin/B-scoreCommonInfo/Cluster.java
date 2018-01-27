package com.wuxuehong.plugin;

public class Cluster {
	
	private String clusterID;
	
	private int nodeCount;
	
	private int edgeCount;
	
	private float avgDegree;
	
	private float density;

	public String getClusterID() {
		return clusterID;
	}

	public void setClusterID(String clusterID) {
		this.clusterID = clusterID;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	public int getEdgeCount() {
		return edgeCount;
	}

	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}

	public float getAvgDegree() {
		return avgDegree;
	}

	public void setAvgDegree(float avgDegree) {
		this.avgDegree = avgDegree;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}
	
	

}
