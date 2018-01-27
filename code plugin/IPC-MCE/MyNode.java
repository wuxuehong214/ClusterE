package com.wuxuehong.plugin;
import java.util.ArrayList;
import java.util.List;

import com.wuxuehong.bean.Node;


public class MyNode extends Node  implements Comparable{
	
	private String name;
	private int degree;
	private List<MyNode> neighbour;
	
	
	public MyNode(String name){
		super(name);
		this.name = name;
		degree = 1;
		neighbour = new ArrayList<MyNode>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public List<MyNode> getNeighbour() {
		return neighbour;
	}

	public void setNeighbours(List<MyNode> neighbours) {
		this.neighbour = neighbours;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		MyNode mn = (MyNode)o;
		return this.getDegree()-mn.getDegree();
	}
	
	

}
