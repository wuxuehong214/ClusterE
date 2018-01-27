package com.wuxuehong.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.print.attribute.standard.Fidelity;


import com.wuxuehong.bean.Node;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-4-3 上午09:17:10 
 * 
 */

public class Main {
	
	private GeneExpression ge = new GeneExpression();
	private DAGanalysis dag = new DAGanalysis();
	private ProteinNetwork  pn = new ProteinNetwork();
	
	
	private List<ClusterVo> clusters = new LinkedList<ClusterVo>();

	public Main() throws IOException {
		// TODO Auto-generated constructor stub
		try {
			ge.initialize("ProteinComplex\\GeneExpressionDatas.txt");
			dag.initialize("ProteinComplex\\GO_Function.txt", "ProteinComplex\\GO_Function_annotation.txt");
			pn.initialize("ProteinComplex\\Static_network.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeProteinNetwork();
		findSeeds();
		System.out.println(clusters.size()+"*****");
	//	saveResults("d:/temp/complexes");
	}
	
	/**
	 * use pcc to initialize the protein network
	 * remove the false positive
	 */
	public void initializeProteinNetwork(){
		Set<String> keys = pn.strToEdge.keySet();
		Iterator<String> it = keys.iterator();
		String s = null;
		EdgeVo e = null;
System.out.println(pn.strToEdge.size()+"^^^^^^^^^^^^^^^^^^^^^");
		while(it.hasNext()){
			s = it.next();
			e = pn.strToEdge.get(s);
			double a = ge.getPCC(e.getN1(), e.getN2());
			if(Math.abs(a)<0.1&&a!=0){
				it.remove();
			}
		}
		System.out.println(pn.strToEdge.size()+"^^^^^^^^^^^^^^^##^^^^^^");
	}
	/**
	 * find seeds
	 */
	public void findSeeds(){
		long time = System.currentTimeMillis();
		Iterator<String> it = pn.strToNode.keySet().iterator();
		String protein = null;
		while(it.hasNext()){
			protein = it.next();
			if(pn.strToNode.get(protein).isFlag())   //如果已经处理过
					continue;
			ClusterVo cv = new ClusterVo();
			//将该蛋白节点与其  邻居节点作为一个种子
			cv.getProteins().add(protein);
			cv.getProteins().addAll(pn.strToNode.get(protein).getNeighbours());
			//对该种子作初始化  去除其中内度为1 并且外度大于1的节点
			initializeCluster(cv);
			if(cv.getProteins().size()>1)
				clusters.add(cv);
		}
		System.out.println("Find seeds...."+(System.currentTimeMillis()-time)+"ms");
	}
	
	public void expendSeeds(){
		
	}
	
	public Vector<Node>[] outputCluster(){
		Vector<Node>[] vns = new Vector[clusters.size()];
		for(int i=0;i<vns.length;i++)
		{
			vns[i] = new Vector<Node>();
			Iterator<String> it = clusters.get(i).getProteins().iterator();
			while(it.hasNext()){
				vns[i].add(new Node(it.next()));
			}
		}
		return vns;
	}
	
	/**
	 * save results
	 * @param filename
	 * @throws IOException
	 */
	public void saveResults(String filename) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		ClusterVo cv = null;
		Iterator<String> it = null;
		for(int i=0;i<clusters.size();i++){
			cv = clusters.get(i);
			bw.write("Complex:\t"+(i+1)+"\t"+cv.getProteins().size());
			bw.newLine();
			it = cv.getProteins().iterator();
			while(it.hasNext()){
				bw.write(it.next());
				bw.newLine();
			}
			cv = null;
		}
		bw.flush();
		bw.close();
	}
	
	/**
	 * //对该种子作初始化  去除其中内度为1 并且外度大于1的节点
	 */
	public void initializeCluster(ClusterVo cluster){
		List<String> list = new ArrayList<String>(cluster.getProteins());
		int degree=0;
		String p1,p2;
		for(int i=0;i<list.size();){
			p1 = list.get(i);
			degree = 0 ;
			for(int j=0;j<list.size();j++){
				p2 = list.get(j);
				if(pn.strToEdge.get(p1+p2)!=null)
					degree++;
			}
			if(degree<2){
				cluster.getProteins().remove(p1);
				list.remove(i);
				i=0;
			}else{
				i++;
			}
		}
		if(list.size()==0){
			return;
		}
		List<String> delNodes = new ArrayList<String>();
		list.clear();
		list.addAll(cluster.getProteins());
		
//		for(int i=0;i<list.size();i++){
//			p1 = list.get(i);
//			degree = 0;				//initialize for each vertex
//			for(int j=0;j<list.size();j++){
//				if(edgeMap.get(nodes.get(i).getName()+nodes.get(j).getName())!=null)count++;
//			}
//			if(count<avg)nodes2.add(nodes.get(i));
//		}
	}
	/**
	 * get the avg degree of a cluster
	 * @param cluster
	 * @return
	 */
	public float getAvgDegree(ClusterVo cluster){
		int degree = 0;          //count the number of the edge
		List<String> list = new ArrayList<String>(cluster.getProteins());
		int size = list.size();
		String p1,p2;
		for(int i=0;i<size;i++){
			p1 = list.get(i);
			for(int j=i+1;j<size;j++){
				p2 = list.get(j);
				if(pn.strToEdge.get(p1+p2)!=null)degree++;
			}
		}
		return (float)(2*degree)/(float)size;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new Main();
	}

}
