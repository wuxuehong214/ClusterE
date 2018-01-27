package proteinCompelex_20110416;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.wuxuehong.bean.Node;

public class Main_2 {

	private GeneExpression geneExpression ;
	private ProteinNetwork proteinNetwork;
	private DAGanalysis dagAnalysis;
	private List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	private double density_door = 0.7;
	private double avgW = 1;
	
	public Main_2() {
		// TODO Auto-generated constructor stub
		try {
			geneExpression = new GeneExpression();
			geneExpression.initialize("D:\\Program\\ProteinComplex\\GeneExpressionDatas.txt");
			
			dagAnalysis = new DAGanalysis();
			dagAnalysis.initialize("D:\\Program\\ProteinComplex\\GO_Function.txt", "D:\\Program\\ProteinComplex\\GO_Function_annotation.txt");
			
			proteinNetwork = new ProteinNetwork(geneExpression,dagAnalysis);
//			proteinNetwork.initialize("D:\\Program\\ProteinComplex\\Static_network.txt");
			proteinNetwork.initialize("e:/myPPIS.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
System.out.println("Weight..............");
		calculateNodeWeight();
		Collections.sort(proteinNetwork.nodes);
System.out.println("Expand..............");
		expandCore();
System.out.println("Filter..............");
		filter();
//filter_by_degree();
System.out.println(clusters.size()+"***********************");
		Collections.sort(clusters);
		try {
			output("D:\\Program\\ProteinComplex\\test\\mine2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void output(String filename) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		ClusterVo cluster;
		for(int i=0;i<clusters.size();i++){
			 cluster = clusters.get(i);
			 for(int j=0;j<cluster.getProteins().size();j++)
				 bw.write(cluster.getProteins().get(j)+"\t");
			 bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	

	public void calculateNodeWeight(){
		EdgeVo e = null;
		NodeVo n1,n2=null;
		int com=0;
		int min = 0;
		double ecc;
		for(int i=0;i<proteinNetwork.edges.size();i++){
			e = proteinNetwork.edges.get(i);
			if(e.getPcc()==0)continue;
			n1 = proteinNetwork.strToNode.get(e.getN1());
			n2 = proteinNetwork.strToNode.get(e.getN2());
			com=0;
			for(int j=0;j<n1.getNeighbours().size();j++)
				if(n2.getNeighbours().contains(n1.getNeighbours().get(j)))com++;
			min = (n1.getNeighbours().size()<n2.getNeighbours().size()?n1.getNeighbours().size():n2.getNeighbours().size());
			com++;
			ecc = (double)com/(double)(min);
			e.setPcc(e.getPcc()*ecc);
			n1.setWeight(n1.getWeight()+e.getPcc());
			n2.setWeight(n2.getWeight()+e.getPcc());
		}
	}
	
	public Vector<Node>[] outputCluster(){
		Vector<Node>[] v = new Vector[clusters.size()];
		ClusterVo c;
		for(int i=0;i<clusters.size();i++){
			v[i] = new Vector<Node>();
			c = clusters.get(i);
			for(int j=0;j<c.getProteins().size();j++){
				v[i].add(new Node(c.getProteins().get(j)));
			}
		}
		return v;
	}
	
	public void expandCore(){
		NodeVo node= null;
		String tNode = null;
		List<NodeVo> neighbours = null;
		ClusterVo cluster = null;
		double max=-100000;
		List<NodeVo> tempList = new ArrayList<NodeVo>();
		while(proteinNetwork.nodes.size()!=0){ // check each node 
			node = proteinNetwork.nodes.get(0);
			proteinNetwork.nodes.remove(0);
			if(node.isRemoved())continue;
			cluster = new ClusterVo(); //each node will be initialized as a cluster
			node.setRemoved(true);
			cluster.getProteins().add(node.getName());     //put the protein into the cluster
			
			tempList.clear();
			while(true){
				tNode = null;
				max = -10000;
				for(int j=0;j<cluster.getProteins().size();j++){       //check all the protein in the cluster
					node = proteinNetwork.strToNode.get(cluster.getProteins().get(j));   //get the node 
					neighbours = node.getNeighbours();                      //get the neighours of the node
					for(int i=0;i<neighbours.size();i++){
						node = neighbours.get(i);
						if(node.isRemoved()||node.getIndex()==clusters.size()){
							continue;
						}
						if(node.getWeight()>max){
							tNode = node.getName();
							max = node.getWeight();
						}
					}
				}
				if(tNode!=null){
//	System.out.println(cluster.getProteins().size());
					cluster.getProteins().add(tNode);
					proteinNetwork.strToNode.get(tNode).setRemoved(true);
					if(getDensity(cluster)<density_door){
						cluster.getProteins().remove(tNode);
						proteinNetwork.strToNode.get(tNode).setRemoved(false);
						proteinNetwork.strToNode.get(tNode).setIndex(clusters.size()); //���ýڵ���Ϊ���ڵ�
						tempList.add(proteinNetwork.strToNode.get(tNode));
					}else{   //һ�������µĽڵ�  �� �������Ľڵ� 
						 for(int i=0;i<tempList.size();i++){
							 tempList.get(i).setIndex(-1);
						 }
					}
				}else{
					if(cluster.getProteins().size()>2){
						clusters.add(cluster);
					}else{
						for(int i=0;i<cluster.getProteins().size();i++)
							proteinNetwork.strToNode.get(cluster.getProteins().get(i)).setRemoved(false);
					}
					break;
				}
			}
			
		}
	}
	
	/**
	 * get the number that node connect the cluster
	 * @param cluster
	 * @param node
	 * @return
	 */
	public int getNeiNum(ClusterVo cluster,String node){
		int total = 0;
		String n;
		for(int i=0;i<cluster.getProteins().size();i++){
			 n = cluster.getProteins().get(i);
			 if(proteinNetwork.strToEdge.get(n+node)!=null){
				 total++;
			 }
		}
		return total;
	}
	/**
	 * calculate the density of the cluster
	 * @param cluster
	 * @return
	 */
	public double getDensity(ClusterVo cluster){
		String p1,p2;
		int  v = cluster.getProteins().size();
		int e = 0;
		for(int i=0;i<cluster.getProteins().size();i++){
			p1 = cluster.getProteins().get(i);
			for(int j=i+1;j<cluster.getProteins().size();j++){
				p2 = cluster.getProteins().get(j);
				if(proteinNetwork.strToEdge.get(p1+p2)!=null)
					e++;
			}
		}
		double result = (double)(2*e)/(double)(v*(v-1)); 
		return result;
	}
	/**
	 * get avg weight of the nodes in the cluster
	 * @param cluster
	 * @return
	 */
	public double getAvgWeight(ClusterVo cluster){
		double result = 0 ;
		for(int i=0;i<cluster.getProteins().size();i++){
			result+=proteinNetwork.strToNode.get(cluster.getProteins().get(i)).getWeight();
		}
		result = result/(double)(cluster.getProteins().size());
		return result;
	}
	
	/**
	 * filter the edges in each cluster that the pcc is less than zero
	 */
    public void filter(){
    	ClusterVo cluster = null;
    	String p1,p2;
    	int count=0;
    	EdgeVo e ;
    	for(int i=0;i<clusters.size();){
    		cluster = clusters.get(i);
    		for(int j=0;j<cluster.getProteins().size();){
    			 count=0;
    			 p1 = cluster.getProteins().get(j);
    			 for(int k=0;k<cluster.getProteins().size();k++){
    				 p2 = cluster.getProteins().get(k);
    				 e = proteinNetwork.strToEdge.get(p1+p2);
    				 if(e!=null&&dagAnalysis.getSimilarityBtp(e.getN1(), e.getN2())>0)
    					  count++;
    			 }
    			 if(count==0){
    				cluster.getProteins().remove(j);
    			 }else
    				 j++;
    		}
    		if(cluster.getProteins().size()<3){
    			 clusters.remove(i);
    		}else
    			i++;
    	}
    }
    
    public void filter_by_degree(){
    	ClusterVo cluster = null;
    	String p1,p2;
    	int count=0;
    	EdgeVo e ;
    	double avg;
    	for(int i=0;i<clusters.size();){
    		cluster = clusters.get(i);
    		avg = getAvgDegree(cluster);
    		for(int j=0;j<cluster.getProteins().size();){
    			 count=0;
    			 p1 = cluster.getProteins().get(j);
    			 for(int k=0;k<cluster.getProteins().size();k++){
    				 p2 = cluster.getProteins().get(k);
    				 e = proteinNetwork.strToEdge.get(p1+p2);
    				 if(e!=null)
    					  count++;
    			 }
    			 if(count<avg){
    				cluster.getProteins().remove(j);
    			 }else
    				 j++;
    		}
    		if(cluster.getProteins().size()<3){
    			 clusters.remove(i);
    		}else
    			i++;
    	}
    }
    
    public double getAvgDegree(ClusterVo cluster){
    	int edgeCount=0;
    	String p1,p2;
    	for(int i=0;i<cluster.getProteins().size();i++){
    		p1 = cluster.getProteins().get(i);
    		for(int j=i+1;j<cluster.getProteins().size();j++){
    			p2 = cluster.getProteins().get(j);
    			if(proteinNetwork.strToEdge.get(p1+p2)!=null)
    				   edgeCount++;
    		}
    	}
    	double result = (double)(2*edgeCount)/(double)(cluster.getProteins().size());
    	return result;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main_2();
	}

}
