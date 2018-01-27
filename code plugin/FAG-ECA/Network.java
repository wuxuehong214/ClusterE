package com.wuxuehong.plugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;

public class Network {
	private ArrayList alNodes;
	private ArrayList alArcs;
	
	public Network(){
		alNodes = null;
		alArcs = null;
	}
	public String getNodeIdentifier(int index){
		String identifier;
		identifier = (String)(((NetNode)alNodes.get(index)).getIdentifier());
		return identifier;
	}
	public int getDegree(int index){
		NetNode node = (NetNode)alNodes.get(index);
		int degree = node.getDegree();
		return degree;
	}
	public int[] neighborsArray(int index){
		NetNode node = (NetNode)alNodes.get(index);
		ArrayList alNeighbors = node.getAlNeighbors();
		int[] neighbors = new int[alNeighbors.size()];
        int j = 0;
        for (Iterator i = alNeighbors.iterator(); i.hasNext(); j++) {
        	neighbors[j] = ((Integer) i.next()).intValue();
        }
		return neighbors;
	}
	public ArrayList getAlArcs() {
		return alArcs;
	}
	public void setAlArcs(ArrayList alArcs) {
		this.alArcs = alArcs;
	}
	public ArrayList getAlNodes() {
		return alNodes;
	}
	public void setAlNodes(ArrayList alNodes) {
		this.alNodes = alNodes;
	}	
}

class NetNode extends Node{
	private String identifier;
	private int rootIndex;
	private ArrayList alNeighbors;
	private int degree;
	//private Vector neighbours;
	private int complexID;
	
	public NetNode(String identifier){
		super(identifier);
		//super.setNeighbours(neighbours);
		this.identifier = identifier;
		alNeighbors = new ArrayList();
		degree = 0;
	}

	public ArrayList getAlNeighbors() {
		return alNeighbors;
	}

	public void setAlNeighbors(ArrayList alNeighbors) {
		this.alNeighbors = alNeighbors;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getRootGraphIndex() {
		return rootIndex;
	}

	public void setRootGraphIndex(int rootIndex) {
		this.rootIndex = rootIndex;
	}

	public int getComplexID() {
		return complexID;
	}

	public void setComplexID(int complexID) {
		this.complexID = complexID;
	}
}

class NetArc{
	private int firstNode;
	private int secondNode;	
	private int rootIndex;
	
	public NetArc(int index1, int index2){
		setFirstNode(index1);
		setSecondNode(index2);
	}
	//getters and setters
	public int getFirstNode() {
		return firstNode;
	}
	public void setFirstNode(int firstNode) {
		this.firstNode = firstNode;
	}
	public int getSecondNode() {
		return secondNode;
	}
	public void setSecondNode(int secondode) {
		this.secondNode = secondode;
	}
	public int getRootGraphIndex() {
		return rootIndex;
	}
	public void setRootGraphIndex(int rootIndex) {
		this.rootIndex = rootIndex;
	}		
}
