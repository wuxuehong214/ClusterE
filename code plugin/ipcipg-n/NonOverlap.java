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
 * @date：2011-7-17 下午01:47:46 
 * 
 */

public class NonOverlap {
	
	private ProteinNetwork pn = new ProteinNetwork();
	private GeneExpression ge = new GeneExpression();
	private float minSize = 0;
	private String outFile = null;//输出文件名称
	private List<NodeVo> nodes = new ArrayList<NodeVo>();  //所有节点
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
			pn.initialize(); //读取蛋白质网络
			ge.initialize(geneFile); //读取基因表达数据
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
	 * 扩充种子
	 */
	public void extendSeed(){
		ArrayList<String> nc = new ArrayList<String>();//邻居节点集合
		ArrayList<String> candidate = new ArrayList<String>(); //候选节点集合
		
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
						 clusters.add(cluster);            //完成识别
						 for(int i = 0 ; i < cluster.getProteins().size() ; i++){
							 NodeVo nn = pn.strToNode.get(cluster.getProteins().get(i));
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

	public List<ClusterVo> getClusters() {
		return clusters;
	}
	

}

