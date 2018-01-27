package ipcipg_n;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
 * @date��2011-7-17 ����01:47:46 
 * 
 */

public class NonOverlap {
	
	private ProteinNetwork pn = new ProteinNetwork();
	private GeneExpression ge = new GeneExpression();
	private float minSize = 0;
	private String outFile = null;//����ļ�����
	private List<NodeVo> nodes = new ArrayList<NodeVo>();  //���нڵ�
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	private String geneFile;
	
	public NonOverlap(String[] para)throws Exception{
		try{
		  minSize = Integer.parseInt(para[0]);
		  geneFile = para[1];
		}catch (Exception e) {
			//TODO: handle exception
			throw e;
		}
		try {
			pn.initialize(); //��ȡ����������
			ge.initialize(geneFile); //��ȡ����������
			System.out.println("Calculating seeds...");
			calculateNodeWt();
			System.out.println("Extending seeds...");
			extendSeed();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Input file error!");
			return;
		}
		System.out.println("Successfully! Total protein complexes:"+clusters.size());
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
			n1.setWeight( n1.getWeight() + edge.getWeight());
			NodeVo n2 = pn.strToNode.get( edge.getN2());
			n2.setWeight( n2.getWeight() + edge.getWeight());
		}
		it = pn.strToNode.keySet().iterator();
		while(it.hasNext()){
			NodeVo n = pn.strToNode.get(it.next());
			nodes.add(n);
		}
		Collections.sort(nodes);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("d:/fw.txt"));
			for(int i=0;i<nodes.size();i++){
				bw.write(nodes.get(i).getName()+"\t"+nodes.get(i).getWeight());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������
	 */
	public void extendSeed(){
		ArrayList<String> nc = new ArrayList<String>();//�ھӽڵ㼯��
		ArrayList<String> candidate = new ArrayList<String>(); //��ѡ�ڵ㼯��
		
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
						 clusters.add(cluster);            //���ʶ��
						 for(int i = 0 ; i < cluster.getProteins().size() ; i++){
							 NodeVo nn = pn.strToNode.get(cluster.getProteins().get(i));
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

	public List<ClusterVo> getClusters() {
		return clusters;
	}
	

}

