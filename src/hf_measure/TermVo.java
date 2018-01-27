package hf_measure;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date£º2011-3-20 ÏÂÎç02:30:58 
 * 
 */

public class TermVo {
	
	private String id;
	private String name;
	private Set<String> parents = null;
	private Set<String> children = null;
	//Use the parameter rank to find out the ancestor
	private int rank=-1;  //the rank of this term in the DAG   rank the longer the better
	//use length to calculate the shortest distance
	private int length= 10000;    //length the shorter the better
	
	public TermVo(){
		parents = new HashSet<String>();
		children = new HashSet<String>();
	}
	
	public TermVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getParents() {
		return parents;
	}

	public void setParents(Set<String> parents) {
		this.parents = parents;
	}

	public Set<String> getChildren() {
		return children;
	}

	public void setChildren(Set<String> children) {
		this.children = children;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String showChildren(){
		return children.toString();
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	

}
