package temp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-4-3 上午09:47:13 
 * 
 */

public class NodeVo implements Comparable<NodeVo>{

	private String name;    //节点名称
	private List<String> neighbours; //邻居节点
	private boolean flag = false; //控制标志
	private float weight = 0; //权值
	private boolean isEssential = false;//是否是关键蛋白
	
	private int betweenness = 0;
	private boolean isRemove = false; //在计算最短路径时使用
	private float ecc = -1;//点聚集系数
	
	
	public NodeVo(String name){
		this.name = name;
		this.flag = false;
		neighbours = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<String> neighbours) {
		this.neighbours = neighbours;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int compareTo(NodeVo node) {
		if(weight>=node.getWeight())
			return -1;
		else 
			return 1;
	}

	public boolean isEssential() {
		return isEssential;
	}

	public void setEssential(boolean isEssential) {
		this.isEssential = isEssential;
	}

	public int getBetweenness() {
		return betweenness;
	}

	public void setBetweenness(int betweenness) {
		this.betweenness = betweenness;
	}

	public boolean getIsRemove() {
		return isRemove;
	}

	public void setIsRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}

	public float getEcc() {
		return ecc;
	}

	public void setEcc(float ecc) {
		this.ecc = ecc;
	}

	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}
	
}
