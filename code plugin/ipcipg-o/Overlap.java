package ipcipg_o;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-7-17 ����01:47:38 
 * 
 */

public class Overlap {
	
	private ProteinNetwork pn = new ProteinNetwork();
	private GeneExpression ge = new GeneExpression();
	private float minSize = 0;
	private List<NodeVo> nodes = new ArrayList<NodeVo>();  //���нڵ�
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	private float D = 0; //�� 
	private float N = 0; //��ۼ�ϵ��
	private float density = 0.5f;
	private String geneFile = "gene.txt";
	
	public Overlap(String[] para)throws Exception{
		try{
			density = Float.parseFloat(para[0]);
			D = Float.parseFloat(para[1]);
			N = Float.parseFloat(para[2]);
			minSize = Integer.parseInt(para[3]);
			geneFile = para[4];
		}catch (Exception e) {
			throw e;
		}
		   
		try {
			pn.initialize();//��ȡ����������
			ge.initialize(geneFile); //��ȡ����������
			System.out.println("Calculating seeds...");
			calculateNodeWt();
			System.out.println("Extending seeds...");
			extendSeed();
			System.out.println("Successfully! Total protein complexes:"+clusters.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	}
	
	/**
	 * ����ߵ�Ȩֵ  ���Ȩֵ �Լ���ľۼ�ϵ��
	 * �ߵ�ȨֵΪecc*pcc
	 * ���ȨֵΪ ������֮�����ıߵ�Ȩֵ֮��
	 * ��������ڵ�ľۼ�ϵ��
	 */
	public void calculateNodeWt(){
		Set<String> keys = pn.strToEdge.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			EdgeVo edge = pn.strToEdge.get(it.next()); 
			float ecc = pn.getEcc( edge.getN1(), edge.getN2());
			float pcc = ge.getPCC( edge.getN1(), edge.getN2());
			edge.setWeight( ecc*pcc);
			edge.setEcc(ecc);
			edge.setPcc(pcc);
			NodeVo n1 = pn.strToNode.get( edge.getN1());
			if(n1.getNcc()==-1){ //�����δ����ڵ�n1�ľۼ�ϵ��  �����֮
				n1.setNcc(getNcc(n1));
			}
			n1.setWeight( n1.getWeight() + edge.getWeight());
			
			NodeVo n2 = pn.strToNode.get( edge.getN2());
			if(n2.getNcc()==-1){ //�����δ����ڵ�n1�ľۼ�ϵ��  �����֮
				n2.setNcc(getNcc(n2));
			}
			n2.setWeight( n2.getWeight() + edge.getWeight());
		}
		it = pn.strToNode.keySet().iterator();
		while(it.hasNext()){
			NodeVo n = pn.strToNode.get(it.next());
			nodes.add(n);
		}
		Collections.sort(nodes);
	}
	
	public void extendSeed(){
		ArrayList<String> nc = new ArrayList<String>();//�ھӽڵ㼯��
		ArrayList<String> candidate = new ArrayList<String>(); //��ѡ�ڵ㼯��
		clusters.clear();
		while( nodes.size() != 0 ){
			NodeVo node = nodes.remove(0);   //��ȡ���ӽڵ�
			ClusterVo cluster = new ClusterVo(); //��ʼ��Ϊһ����
			cluster.getProteins().add(node.getName()); //����������
			//�����ھӽڵ㼯��
			nc.clear();
			nc.addAll(node.getNeighbours());
			
			while( nc.size()!=0 ){
				candidate.clear();
				for(int i = 0 ; i < nc.size() ; i ++){
					String nei = nc.get(i);
					if(!pn.strToNode.get(nei).getIsRemove() && isAviliable(cluster, nei)){
						candidate.add(nei);
					}
				}
				if(candidate.size()==0){
					 nc.clear();                   //��ѡ����ڵ㲻������ ��nc��Ϊ�� ֹͣ����
					 if(cluster.getProteins().size()>=minSize){  //����Ǳ�Ĵط��Ϲ�ģ
						 ClusterVo c = canMergeInto(cluster, density);
						 if(c == null){ //���clusterû����֮ǰ��cluster���ں�
								clusters.add(cluster);
						}
						 for(int i = 0 ; i < cluster.getProteins().size() ; i++){
							 NodeVo nn = pn.strToNode.get(cluster.getProteins().get(i));
							 if(nn.getNeighbours().size()<=D || nn.getNcc()>=N )
								 nn.setIsRemove(true); //�Ƴ��Ѿ��ɴصĽڵ�
							 nodes.remove(nn);
						 }
					 }
				}else{
					float max = -Float.MAX_VALUE;
					String extended = null;
					for(int i = 0 ; i < candidate.size() ; i ++){
						float a = getTotalWeight(cluster, candidate.get(i));
						if(a>max){
							max = a ;               
							extended = candidate.get(i);   //�Ӻ�ѡ�ڵ���ѡȡ����б�Ȩֵ�����Ľڵ�
						}
					}
					cluster.getProteins().add(extended); //�����������Ľڵ����䵽����
					//���´ص��ھӽڵ�
					nc.clear();
					for(int i = 0 ; i < cluster.getProteins().size() ; i++){
						NodeVo n = pn.strToNode.get(cluster.getProteins().get(i));
						for(int j = 0 ; j < n.getNeighbours().size() ; j++){
							String temp = n.getNeighbours().get(j);
							if(!cluster.getProteins().contains(temp)&&!nc.contains(temp)){
								nc.add(temp);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * ����ڵ�ľۼ�ϵ��
	 * �������ھӽڵ���  ���еıߵĸ������Կ��ܴ��ڵıߵĸ���
	 * @param node
	 * @return
	 */
	public float getNcc(NodeVo node){
		float result = 0;
		int deg = node.getNeighbours().size();
		int edgeCount = 0 ;
		for( int i = 0 ;i < deg ; i ++){
			for( int j = i+1 ; j < deg ; j ++){
				if(pn.getEdge(node.getNeighbours().get(i),node.getNeighbours().get(j)) != null){
					edgeCount ++;
				}
			}
		}
		if(edgeCount == 0){
			return 0;
		}
		result = (float)(2*edgeCount)/(float)(deg*(deg-1));
		return result;
	}
	/**
	 * ����node��cluster�нڵ��γɱߵ�Ȩֵ�ܺ�
	 * @param cluster
	 * @param node
	 */
	public float getTotalWeight(ClusterVo cluster, String node){
		float total = 0;
		for( int i = 0 ; i < cluster.getProteins().size() ; i++){
			EdgeVo edge = pn.getEdge(node,cluster.getProteins().get(i));
			if(edge != null)
				total += edge.getWeight();
		}
		return total;
	}
	/**
	 * �жϽڵ�node ��cluster�������ı��� �Ƿ����cluster�г�node����ڵ�����һ�롣
	 * @param cluster
	 * @param node
	 * @return
	 */
	public boolean isAviliable(ClusterVo cluster, String node){
		int degree = 0;
		for(int i = 0 ; i < cluster.getProteins().size(); i++){
			if(pn.getEdge(node, cluster.getProteins().get(i)) != null){
				degree ++;
			}
		}
		if(degree > (cluster.getProteins().size())/2)return true;
		return false;
	}
	
	/**
	 * �ж�cluster�����е�cluster������ֵ�Ƿ����value����������� ������true ���򷵻�false
	 * @param cluster
	 * @param value
	 * @return
	 */
	public ClusterVo canMergeInto(ClusterVo cluster, float value){
		float result = 0 ;
		float max = -1;
		ClusterVo temp = null;
		for(int i = 0 ; i < clusters.size() ; i ++){
			ClusterVo c = clusters.get(i);
			int count = 0 ;
			for(int j = 0 ; j < c.getProteins().size() ; j ++){
				if(cluster.getProteins().contains(c.getProteins().get(j))) count++;
			}
			result =  (float)(count*count)/(float)(cluster.getProteins().size()*c.getProteins().size());
			if(result>max) {
				max = result;      //��ȡ�������ֵ
				temp = c;  //��ȡҪ��ϵ�cluster����
			}
		}
		//��cluster���� temp
		if(max>=value){
			for(int i = 0 ; i < cluster.getProteins().size() ; i ++){
				if(!temp.getProteins().contains(cluster.getProteins().get(i)))
					temp.getProteins().add(cluster.getProteins().get(i));
			}
			return temp;
		}
		return null;
	}

	public List<ClusterVo> getClusters() {
		return clusters;
	}

}
