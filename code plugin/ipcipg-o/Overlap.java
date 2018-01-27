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
 * @date：2011-7-17 下午01:47:38 
 * 
 */

public class Overlap {
	
	private ProteinNetwork pn = new ProteinNetwork();
	private GeneExpression ge = new GeneExpression();
	private float minSize = 0;
	private List<NodeVo> nodes = new ArrayList<NodeVo>();  //所有节点
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	private float D = 0; //度 
	private float N = 0; //点聚集系数
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
			pn.initialize();//读取蛋白质网络
			ge.initialize(geneFile); //读取基因表达数据
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
	 * 计算边的权值  点的权值 以及点的聚集系数
	 * 边的权值为ecc*pcc
	 * 点的权值为 所有与之相连的边的权值之和
	 * 计算各个节点的聚集系数
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
			if(n1.getNcc()==-1){ //如果还未计算节点n1的聚集系数  则计算之
				n1.setNcc(getNcc(n1));
			}
			n1.setWeight( n1.getWeight() + edge.getWeight());
			
			NodeVo n2 = pn.strToNode.get( edge.getN2());
			if(n2.getNcc()==-1){ //如果还未计算节点n1的聚集系数  则计算之
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
		ArrayList<String> nc = new ArrayList<String>();//邻居节点集合
		ArrayList<String> candidate = new ArrayList<String>(); //候选节点集合
		clusters.clear();
		while( nodes.size() != 0 ){
			NodeVo node = nodes.remove(0);   //获取种子节点
			ClusterVo cluster = new ClusterVo(); //初始化为一个簇
			cluster.getProteins().add(node.getName()); //将其加入簇中
			//更新邻居节点集合
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
					 nc.clear();                   //候选扩充节点不存在则 将nc置为空 停止扩充
					 if(cluster.getProteins().size()>=minSize){  //如果是别的簇符合规模
						 ClusterVo c = canMergeInto(cluster, density);
						 if(c == null){ //如果cluster没有与之前的cluster相融合
								clusters.add(cluster);
						}
						 for(int i = 0 ; i < cluster.getProteins().size() ; i++){
							 NodeVo nn = pn.strToNode.get(cluster.getProteins().get(i));
							 if(nn.getNeighbours().size()<=D || nn.getNcc()>=N )
								 nn.setIsRemove(true); //移除已经成簇的节点
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
							extended = candidate.get(i);   //从候选节点中选取与簇中边权值和最大的节点
						}
					}
					cluster.getProteins().add(extended); //将符合条件的节点扩充到簇中
					//更新簇的邻居节点
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
	 * 计算节点的聚集系数
	 * 计算其邻居节点中  已有的边的个数除以可能存在的边的个数
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
	 * 计算node与cluster中节点形成边的权值总和
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
	 * 判断cluster与现有的cluster相似性值是否大于value如果大于则结合 并返回true 否则返回false
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
				max = result;      //获取最大相似值
				temp = c;  //获取要结合的cluster对象
			}
		}
		//将cluster融入 temp
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
