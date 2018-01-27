package com.wuxuehong.plugin;

public class MyEdge {
	
	private MyNode node1;
	private MyNode node2;
	
	
	public MyEdge(MyNode node1,MyNode node2){
		this.node1 = node1;
		this.node2 = node2;
	}


	public MyNode getNode1() {
		return node1;
	}


	public void setNode1(MyNode node1) {
		this.node1 = node1;
	}


	public MyNode getNode2() {
		return node2;
	}


	public void setNode2(MyNode node2) {
		this.node2 = node2;
	}
	
	

}
