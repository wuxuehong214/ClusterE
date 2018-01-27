package com.wuxuehong.plugin;

import java.util.*;
/**
 * a node represent a protein
 * @author Administrator
 *
 */
public class Node {
	
	private String name ;  //protein name
	private List<Node> neighbours;// which connect to the node directly
	
	
	public Node(String name){
		this.name = name;
		neighbours = new ArrayList<Node>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Node> getNeighbours() {
		return neighbours;
	}


	public void setNeighbours(List<Node> neighbours) {
		this.neighbours = neighbours;
	}
	
	
	

}
