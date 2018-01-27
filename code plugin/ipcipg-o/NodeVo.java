package ipcipg_o;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-4-3 ����09:47:13 
 * 
 */

public class NodeVo implements Comparable<NodeVo>{

	private String name;    //�ڵ�����
	private List<String> neighbours; //�ھӽڵ�
	private float weight = 0; //Ȩֵ
	private boolean isEssential = false;//�Ƿ��ǹؼ�����
	
	private int betweenness = 0;
	private boolean isRemove = false; //�ڼ������·��ʱʹ��
	private float ncc = -1;//��ۼ�ϵ��
	
	
	public NodeVo(String name){
		this.name = name;
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

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int compareTo(NodeVo node) {
		if(weight>node.getWeight())
			return -1;
		else if(weight==node.getWeight()){
			return name.compareTo(node.getName());
		}else
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

	public float getNcc() {
		return ncc;
	}

	public void setNcc(float ncc) {
		this.ncc = ncc;
	}

	public void setRemove(boolean isRemove) {
		this.isRemove = isRemove;
	}
	
}
