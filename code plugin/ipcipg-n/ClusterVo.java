package ipcipg_n;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-4-3 ����09:49:08 
 * 
 */

public class ClusterVo {
	
	public String toString() {
		StringBuffer sb = new StringBuffer("");
		for(int i = 0 ; i < proteins.size() ; i++){
			sb.append(proteins.get(i)+"\t");
		}
		return sb.toString();
	}
	
	private float fscore = 0;

	private int index ;  //����������
	
	private List<String> proteins;//�ø����������еĵ����ʽڵ�
	
	private float matchValue = 0;//����֪������Mathcֵ
	
	private int essential_count = 0; //�ؼ����׸���
	
	private double pvalue; //p-valueֵ
	
	private String function ;//��СP-valoueֵ����Ӧ�Ĺ�������
	
	private int edgeNum = 0;//����
	
	private int overlap; //cluster�к���ĳ���ܵĵ����ʸ���
	
	private int background;//�������������к��иù��ܵĵ����ʸ���
	
	private String detail="";
	
	public ClusterVo(){
		proteins = new ArrayList<String>();
	}
	
	public List<String> getProteins() {
		return proteins;
	}

	public void setProteins(List<String> proteins) {
		this.proteins = proteins;
	}

	public float getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(float matchValue) {
		this.matchValue = matchValue;
	}

	public int getEssential_count() {
		return essential_count;
	}

	public void setEssential_count(int essentialCount) {
		essential_count = essentialCount;
	}

	public double getPvalue() {
		return pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public int getEdgeNum() {
		return edgeNum;
	}

	public void setEdgeNum(int edgeNum) {
		this.edgeNum = edgeNum;
	}
	
	public float getDensity(){
		if(proteins.size() == 0 || edgeNum == 0 )
			return 0;
		else 
			return (float)(2*edgeNum)/(float)(proteins.size()*(proteins.size()-1));
	}

	public float getFscore() {
		return fscore;
	}

	public void setFscore(float fscore) {
		this.fscore = fscore;
	}

	public int getOverlap() {
		return overlap;
	}

	public void setOverlap(int overlap) {
		this.overlap = overlap;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
