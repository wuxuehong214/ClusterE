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
 * @date：2011-4-3 上午09:17:10 
 * 
 */

public class Main {
	
	//基因表达信息 操作对象
	private GeneExpression ge = new GeneExpression();
	//蛋白质网络信息 操作对象
	private ProteinNetwork  pn = new ProteinNetwork();
	//与已知复合物匹配 操作对象
	private MatchKnown mk = new MatchKnown();
	//关键蛋白操作对象
	EssentialProtein ep = new EssentialProtein();
	//文本输出操作对象
	TxtExport te  = new TxtExport();
	
	//所有候选节点
	private List<NodeVo> nodes = new ArrayList<NodeVo>();
	
	
	
	private float density = 0.7f;
	
	private int degree = 15; //度数阀值
	
	private float avgecc = 0.4f;//平均聚集系数
	
	private int minSize = 3;//最小规模
	
	
	
	//种子节点到其相应的复合物的映射
	private Map<String, ClusterVo> seedToCluster = new HashMap<String , ClusterVo>();
	
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	public Main(float density,int minSize) {
		
		this.density = density;
		
		this.minSize = minSize;
		
		try {
			//读取基因表达信息
			ge.initialize("D:\\Program\\ProteinComplex\\GeneExpressionDatas.txt");
			//读取PPI网络 以及蛋白质功能注释信息
			pn.initialize("e:/20110611/ppi.txt", "e:/20110611/GO_Function_annotation.txt");
//			//读取关键蛋白信息
//			ep.initialize("e:/20110611/Essential.txt");
//			//读取已知蛋白质复合物信息
//			mk.initialize("e:/20110611/complex_merge.txt");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		System.out.println("Calculating nodes' weight...");
		//计算节点的权值   以及节点聚集系数
		calculateNodeWt(); 
		
		System.out.println("Mining protein complexes...");
		//挖掘蛋白质复合物
		findComplexes();
		
//		System.out.println("Charging the essential proteins...");
//		//关键蛋白判断
//		ep.setEssentialNode(nodes);
//		ep.calculateEssentialNum(clusters);
//		
//		System.out.println("Matching with known complexes...");
//		//将各个蛋白质复合物与已知复合物比较
//		mk.calculateMatch(clusters);
//		
//		System.out.println("Calculating pvalue...");
//		//计算各个复合物的pvalue
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
	 * 判断该节点是否可以存在多个cluster中
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
	 * 根据种子节点查找complex
	 */
	public void findComplexes(){
		
		int totalCount = 0;
		
		for( int i=0; i<nodes.size(); i++){
			
			NodeVo node = nodes.get(i);
			
			if(node.isFlag())continue;//如果已经存在其他cluster中
			
			ClusterVo cluster = new ClusterVo();//初始化一个cluster

			cluster.getProteins().add(node.getName());
			
			node.setFlag(true);
			
			while(true){
				
				NodeVo kn = null;
				
				float max = -100;
				
				for(int j=0; j<node.getNeighbours().size(); j++){
						
					String nei = node.getNeighbours().get(j);
					
					NodeVo n = pn.strToNode.get(nei);
					//如果n不存在与其他cluster中，并且不是假死节点 并且 与node节点权值最大
					if(!n.isFlag() && getTotalWeight(cluster, nei) > max && getDensity(cluster,nei) >= density && isAviliable(cluster, nei)){
						
						kn = n;
						
						max = pn.getEdge(node.getName(), nei).getWeight();
						
					}
					
				}

				if(kn != null){
					
					cluster.getProteins().add(kn.getName());//将其加入cluster
						
					kn.setFlag(true);
					
				}else{
					//如果complex的规模大于2则保留
					if(cluster.getProteins().size() >= minSize){
	
						clusters.add(cluster);
			System.out.println(cluster.getProteins());
						totalCount += cluster.getProteins().size();
						//计算cluster中边的个数
//						cluster.setEdgeNum(getEdgeNum(cluster));
						//节点名称到cluster映射
						seedToCluster.put(node.getName(), cluster);
						
					}else{ //否则  将cluster中的节点归还
						
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
	 * 计算node与cluster中节点形成边的权值总和
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
	 * 判断节点node 与cluster中相连的边数 是否大于cluster中除node以外节点数的一半。
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
	 * 获取cluster密度
	 * @param cluster
	 * @return
	 */
	public float getDensity(ClusterVo cluster , String node){
		
		float result = 0;
		//节点个数
		int nodeCount = cluster.getProteins().size()+1;
		//边个数
		int edgeCount = 0;
		
		String n1 , n2;
		//计算cluster中边的个数
		for(int i=0; i< nodeCount-1; i++){
			
			n1 = cluster.getProteins().get(i);
			
			for(int j = i+1; j < nodeCount-1; j++){
				
				n2 = cluster.getProteins().get(j);
				
				if(pn.getEdge(n1, n2)!=null){
					
					edgeCount ++;
					
				}
			}
			//计算加入的节点与cluster中存在的边的个数
			if( pn.getEdge(n1, node) != null ){
				
					edgeCount++;
				
			}
			
		}
		
		result = (float)(2*edgeCount)/(float)(nodeCount*(nodeCount-1));
		
		return result;
		
	}
	
	/**
	 * 获取cluster中边的个数
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
	 * 计算边的权值  点的权值 以及点的聚集系数
	 * 边的权值为ecc*pcc
	 * 点的权值为 所有与之相连的边的权值之和
	 * 计算各个节点的聚集系数
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
			
			if(n1.getEcc()==-1){ //如果还未计算节点n1的聚集系数  则计算之
				
				n1.setEcc(getNodeEcc(n1));
				
			}
			
			n1.setWeight( n1.getWeight() + edge.getWeight());
			
			NodeVo n2 = pn.strToNode.get( edge.getN2());
			
			if(n2.getEcc() == -1){ //同上
				
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
	 * 计算节点的聚集系数
	 * 计算其邻居节点中  已有的边的个数除以可能存在的边的个数
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
