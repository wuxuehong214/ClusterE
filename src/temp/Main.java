package temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.print.attribute.standard.Fidelity;

import com.wuxuehong.bean.Node;

import jxl.write.WriteException;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-4-3 ����09:17:10 
 * 
 */

public class Main {
	
	//��������Ϣ ��������
	private GeneExpression ge = new GeneExpression();
	//������������Ϣ ��������
	private ProteinNetwork  pn = new ProteinNetwork();
	//����֪������ƥ�� ��������
	private MatchKnown mk = new MatchKnown();
	//�ؼ����ײ�������
	EssentialProtein ep = new EssentialProtein();
	//�ı������������
	TxtExport te  = new TxtExport();
	
	//���к�ѡ�ڵ�
	private List<NodeVo> nodes = new ArrayList<NodeVo>();
	
	
	
	private float density = 0.7f;
	
	private int degree = 15; //������ֵ
	
	private float avgecc = 0.4f;//ƽ���ۼ�ϵ��
	
	private int minSize = 3;//��С��ģ
	
	
	
	//���ӽڵ㵽����Ӧ�ĸ������ӳ��
	private Map<String, ClusterVo> seedToCluster = new HashMap<String , ClusterVo>();
	
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	public Main(float density,int minSize) {
		
		this.density = density;
		
		this.minSize = minSize;
		
		try {
			//��ȡ��������Ϣ
			ge.initialize("D:\\Program\\ProteinComplex\\GeneExpressionDatas.txt");
			//��ȡPPI���� �Լ������ʹ���ע����Ϣ
			pn.initialize("e:/20110611/ppi.txt", "e:/20110611/GO_Function_annotation.txt");
//			//��ȡ�ؼ�������Ϣ
//			ep.initialize("e:/20110611/Essential.txt");
//			//��ȡ��֪�����ʸ�������Ϣ
//			mk.initialize("e:/20110611/complex_merge.txt");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		System.out.println("Calculating nodes' weight...");
		//����ڵ��Ȩֵ   �Լ��ڵ�ۼ�ϵ��
		calculateNodeWt(); 
		
		System.out.println("Mining protein complexes...");
		//�ھ򵰰��ʸ�����
		findComplexes();
		
//		System.out.println("Charging the essential proteins...");
//		//�ؼ������ж�
//		ep.setEssentialNode(nodes);
//		ep.calculateEssentialNum(clusters);
//		
//		System.out.println("Matching with known complexes...");
//		//�����������ʸ���������֪������Ƚ�
//		mk.calculateMatch(clusters);
//		
//		System.out.println("Calculating pvalue...");
//		//��������������pvalue
//		pn.calculatePvalue(clusters, nodes.size());
		
//		try {
//			
//			ExcelExport.exportClusters(nodes, seedToCluster, "e:/20110623/result20110623MF_"+density+"_min"+minSize+"_overlap.xls");
//			
//		} catch (WriteException e1) {
//			
//			e1.printStackTrace();
//			
//		} catch (IOException e1) {
//			
//			e1.printStackTrace();
//			
//		}
//		
//		try {
//			
//			te.exportComplexes(clusters, "e://20110623/complexes_"+density+"_min"+minSize+"_overlap.txt");
//			
//			te.exportInteractions(clusters, "e://20110623//positives_"+density+"_min"+minSize+"_overlap.txt", pn, true);
//			
//			te.exportInteractions(clusters, "e://20110623//negatives_"+density+"_min"+minSize+"_overlap.txt", pn, false);
//			
//		} catch (IOException e) {
//			
//			e.printStackTrace();
//			
//		}
		
		System.out.println("Total cluster size:"+clusters.size());
	}
	
	
	public Vector<Node>[] outputCluster(){
		Vector<Node>[] v = new Vector[clusters.size()];
		ClusterVo c;
		Map<String,Node> tempMap = new HashMap<String,Node>();
		for(int i=0;i<clusters.size();i++){
			v[i] = new Vector<Node>();
			c = clusters.get(i);
			for(int j=0;j<c.getProteins().size();j++){
				Node n = tempMap.get(c.getProteins().get(j));
				if(n==null)
					v[i].add(new Node(c.getProteins().get(j)));
				else{
					tempMap.put(c.getProteins().get(j),n);
					v[i].add(n);
				}
			}
		}
		return v;
	}
	
	/**
	 * �жϸýڵ��Ƿ���Դ��ڶ��cluster��
	 * @param degree
	 * @param avgecc
	 * @return
	 */
	public boolean canOverlap(NodeVo node){
		
		int deg = node.getNeighbours().size();
		
		float ae = node.getEcc();
		
		if(deg > degree && ae < avgecc)
			
			return true;
			
			return false;
		
	}
	
	/**
	 * �������ӽڵ����complex
	 */
	public void findComplexes(){
		
		int totalCount = 0;
		
		for( int i=0; i<nodes.size(); i++){
			
			NodeVo node = nodes.get(i);
			
			if(node.isFlag())continue;//����Ѿ���������cluster��
			
			ClusterVo cluster = new ClusterVo();//��ʼ��һ��cluster

			cluster.getProteins().add(node.getName());
			
			node.setFlag(true);
			
			while(true){
				
				NodeVo kn = null;
				
				float max = -100;
				
				for(int j=0; j<node.getNeighbours().size(); j++){
						
					String nei = node.getNeighbours().get(j);
					
					NodeVo n = pn.strToNode.get(nei);
					//���n������������cluster�У����Ҳ��Ǽ����ڵ� ���� ��node�ڵ�Ȩֵ���
					if(!n.isFlag() && getTotalWeight(cluster, nei) > max && getDensity(cluster,nei) >= density && isAviliable(cluster, nei)){
						
						kn = n;
						
						max = pn.getEdge(node.getName(), nei).getWeight();
						
					}
					
				}

				if(kn != null){
					
					cluster.getProteins().add(kn.getName());//�������cluster
						
					kn.setFlag(true);
					
				}else{
					//���complex�Ĺ�ģ����2����
					if(cluster.getProteins().size() >= minSize){
	
						clusters.add(cluster);
			System.out.println(cluster.getProteins());
						totalCount += cluster.getProteins().size();
						//����cluster�бߵĸ���
//						cluster.setEdgeNum(getEdgeNum(cluster));
						//�ڵ����Ƶ�clusterӳ��
						seedToCluster.put(node.getName(), cluster);
						
					}else{ //����  ��cluster�еĽڵ�黹
						
						for(int k = 0; k < cluster.getProteins().size(); k++){
							
							pn.strToNode.get(cluster.getProteins().get(k)).setFlag(false); 
							
						}

					}
					
					break;
				}
			}
		}
		
		System.out.println("Complex proteins/Total proteins :\t"+totalCount+"/"+nodes.size()+"\tdensity"+density);
		
	}
	
	
	/**
	 * ����node��cluster�нڵ��γɱߵ�Ȩֵ�ܺ�
	 * @param cluster
	 * @param node
	 */
	public float getTotalWeight(ClusterVo cluster, String node){
		
		float total = 0;
		
		for( int i = 0 ; i < cluster.getProteins().size() ; i++){
			
			EdgeVo edge = pn.strToEdge.get(node+cluster.getProteins().get(i));
			
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
	 * ��ȡcluster�ܶ�
	 * @param cluster
	 * @return
	 */
	public float getDensity(ClusterVo cluster , String node){
		
		float result = 0;
		//�ڵ����
		int nodeCount = cluster.getProteins().size()+1;
		//�߸���
		int edgeCount = 0;
		
		String n1 , n2;
		//����cluster�бߵĸ���
		for(int i=0; i< nodeCount-1; i++){
			
			n1 = cluster.getProteins().get(i);
			
			for(int j = i+1; j < nodeCount-1; j++){
				
				n2 = cluster.getProteins().get(j);
				
				if(pn.getEdge(n1, n2)!=null){
					
					edgeCount ++;
					
				}
			}
			//�������Ľڵ���cluster�д��ڵıߵĸ���
			if( pn.getEdge(n1, node) != null ){
				
					edgeCount++;
				
			}
			
		}
		
		result = (float)(2*edgeCount)/(float)(nodeCount*(nodeCount-1));
		
		return result;
		
	}
	
	/**
	 * ��ȡcluster�бߵĸ���
	 * @param cluster
	 * @return
	 */
	public int getEdgeNum(ClusterVo cluster){
		
		String n1,n2;
		
		int edgeCount = 0;
		
		for(int i=0; i<cluster.getProteins().size(); i++){
			
			n1 = cluster.getProteins().get(i);
			
			for(int j = i+1; j < cluster.getProteins().size(); j++){
				
				n2 = cluster.getProteins().get(j);
				
				if(pn.getEdge(n1, n2)!=null){
					
					edgeCount ++;
					
				}
			}
			
		}
		
		return edgeCount;
	}
	/**
	 * ����ߵ�Ȩֵ  ���Ȩֵ �Լ���ľۼ�ϵ��
	 * �ߵ�ȨֵΪecc*pcc
	 * ���ȨֵΪ ������֮�����ıߵ�Ȩֵ֮��
	 * ��������ڵ�ľۼ�ϵ��
	 */
	public void calculateNodeWt(){
		
		int negativeCount = 0;
		
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
			
			if(n1.getEcc()==-1){ //�����δ����ڵ�n1�ľۼ�ϵ��  �����֮
				
				n1.setEcc(getNodeEcc(n1));
				
			}
			
			n1.setWeight( n1.getWeight() + edge.getWeight());
			
			NodeVo n2 = pn.strToNode.get( edge.getN2());
			
			if(n2.getEcc() == -1){ //ͬ��
				
				n2.setEcc(getNodeEcc(n2));
				
			}
			
			n2.setWeight( n2.getWeight() + edge.getWeight());
			
		}
		
		it = pn.strToNode.keySet().iterator();
		
		while(it.hasNext()){
			
			NodeVo n = pn.strToNode.get(it.next());
			
			if(n.getWeight()<0) negativeCount++;
			
			nodes.add(n);
		}
		
		Collections.sort(nodes);
		
		System.out.println("Positive nodes :"+(nodes.size()-negativeCount)+"\t\tNegative nodes size: "+negativeCount);
	}
	
	
	/**
	 * ����ڵ�ľۼ�ϵ��
	 * �������ھӽڵ���  ���еıߵĸ������Կ��ܴ��ڵıߵĸ���
	 * @param node
	 * @return
	 */
	public float getNodeEcc(NodeVo node){
		
		float result = 0;
		
		int deg = node.getNeighbours().size();
		
		int edgeCount = 0 ;
		
		for( int i = 0 ; i < deg ; i ++){
			
			for( int j = i+1 ; j < deg ; j ++){
				
				if(pn.strToEdge.get(node.getNeighbours().get(i)+node.getNeighbours().get(j)) != null){
					
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
	

}
