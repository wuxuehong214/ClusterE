package com.wuxuehong.plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date£º2011-4-3 ÉÏÎç09:47:13 
 * 
 */

public class NodeVo {

	private String name;
	private Set<String> neighbours;
	private boolean flag;
	
	public NodeVo(String name){
		this.name = name;
		this.flag = false;
		neighbours = new HashSet<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(Set<String> neighbours) {
		this.neighbours = neighbours;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
