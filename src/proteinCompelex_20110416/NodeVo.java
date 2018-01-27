package proteinCompelex_20110416;

import java.util.ArrayList;
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

public class NodeVo implements Comparable<NodeVo>{

	public String toString() {
		return name;
	}

	private String name;
	private List<NodeVo> neighbours;
	private double weight=0;
	
	private boolean isRemoved = false;
	private int index = -1;
	
	public NodeVo(String name){
		this.name = name;
		neighbours = new ArrayList<NodeVo>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public List<NodeVo> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<NodeVo> neighbours) {
		this.neighbours = neighbours;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int compareTo(NodeVo o) {
		if(weight>=o.getWeight())
			return -1;
		else
			return 1;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	
	public static void main(String args[]){
		NodeVo n1 = new NodeVo("aa");
		NodeVo n2 = new NodeVo("aa");
		System.out.println(n2.equals(n1));
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeVo other = (NodeVo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
