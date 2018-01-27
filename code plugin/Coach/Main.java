package com.wuxuehong.plugin;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.wuxuehong.interfaces.GraphInfo;

public class Main {
	
	
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private Map<String,Node> nodeMap = new HashMap<String,Node>();
	private Map<String,Edge> edgeMap = new HashMap<String,Edge>();
	private List<SubGraph> cores = new ArrayList<SubGraph>();
	private float density=(float) 0.7;
	private float similarity = (float) 0.225;
	
	public Main(float density,float similarity){
		this.density = density;
		this.similarity = similarity;
		readPPI();
		getCores();
		getAttachment();
//		showCores();
	}
	
	/**
	 * get the attachment for each core 
	 * the selected attachments interact with more than half of the 
	 * proteins in the core
	 */
	public void getAttachment(){
		List<Node> attachments = new ArrayList<Node>();
		int count = 0;
		for(int i=0;i<cores.size();i++){
			SubGraph sub = cores.get(i);            //get a core
			attachments.clear();           //clear the attachments
			
			for(int j=0;j<sub.getNodes().size();j++){     //check all the node in the core
				Node node = sub.getNodes().get(j);      //get a node from the core
				for(int k=0;k<node.getNeighbours().size();k++){
					count=0;
					Node node2 = node.getNeighbours().get(k);
					if(sub.getNodes().contains(node2)||attachments.contains(node2)){         //if the node is in the core then go to next
						continue;
					}
					for(int m=0;m<sub.getNodes().size();m++){
						if(edgeMap.get(node2.getName()+sub.getNodes().get(m).getName())!=null)
							count++;
					}
					int num = sub.getNodes().size();
					if(num%2==0){
						num = num/2;
					}else
						num = num/2+1;
					if(count>num)
						attachments.add(node2);
				}
			}
			sub.getNodes().addAll(attachments);
		}
	}
	
	/**
	 * show the detail nodes for each core
	 * @throws FileNotFoundException 
	 */
//	public void showCores() throws IOException{
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("F:\\大学后做过的一些东西\\毕业设计\\动态蛋白质中利用共表达数据万挖掘功能模块\\Static_network_core.txt")));
//		int total = 0;
//		for(int i=0;i<cores.size();i++){
//			SubGraph sub = cores.get(i);
//			total+=sub.getNodes().size();
//			System.out.println("Complex:"+i+"\t"+sub.getNodes().size());
//			bw.write("Complex:"+i+"\t"+sub.getNodes().size());
//			bw.newLine();
//			for(int j=0;j<sub.getNodes().size();j++){
//				System.out.print(sub.getNodes().get(j).getName()+"--");
//				bw.write(sub.getNodes().get(j).getName());
//				bw.newLine();
//			}
//			System.out.println();
//		}
//		bw.flush();
//		bw.close();
//	System.out.println("total nodes in the cores:"+total +"But all the nodes in the network:"+nodes.size());
//	System.out.println("show cores size"+cores.size());
//	}
	
	
	/**
	 * read the ppi network
	 * @throws IOException 
	 */
	public void readPPI(){
		String ss = null;
		Node n1,n2;
		for(int i=0;i<GraphInfo.edgelist.size();i++){
			com.wuxuehong.bean.Edge edge = GraphInfo.edgelist.get(i);
			ss = edge.getNode1().getNodeID();
			n1 = nodeMap.get(ss);
			if(n1==null){
				n1 = new Node(ss);
				nodeMap.put(ss, n1);
				nodes.add(n1);
			}
			ss = edge.getNode2().getNodeID();
			n2 = nodeMap.get(ss);
			if(n2==null){
				n2 = new Node(ss);
				nodeMap.put(ss, n2);
				nodes.add(n2);
			}
			n1.getNeighbours().add(n2);
			n2.getNeighbours().add(n1);
			Edge e =new Edge(n1,n2);
			edgeMap.put(n1.getName()+n2.getName(), e);
			edgeMap.put(n2.getName()+n1.getName(), e);
			edges.add(e);
		}
	}
	
	/**
	 * get the cores from the input network
	 */
	public void getCores(){
		for(int i=0;i<nodes.size();i++){
			Node node = nodes.get(i);       //get a node
			SubGraph sub = new SubGraph(node,edgeMap); //
			sub.filter();// filter the node which has only one degree
			if(sub.isEmpty()) continue;  //if the subgraph is empty then go to next node
			sub.getCore();//get a preliminary core
if(sub.getNodes().size()<2)continue; //if there only one core node then continue
			List<Node> cgNodes = new ArrayList<Node>(); //use to store the preliminary core nodes
			cgNodes.addAll(sub.getNodes());

			List<SubGraph> subs = Core_removal(sub);  // get a set of connected componments(prilimnary cores)
			
			for(int j=0;j<subs.size();j++){
				SubGraph sg = subs.get(j);
				while(sg.getDensity()<density){      //if the sg is not dense enough
					Node w = sg.getMinmDegreeNode();   //get the minmum degree node
					sg.getNodes().remove(w);          //remove the node from sg until it is dense enough
				}
				Node w = sg.getMaxDegreeNode(cgNodes);
				while(w!=null&&sg.getDensity(w)>=density){
					sg.getNodes().add(w);    //add the node to the sg
					w = sg.getMaxDegreeNode(cgNodes);
				}
				redundancy_filtering(sg);        //filter the core 
			}
		}
		
	}
	
	
	/**
	 * calculate the similarity between subgraph sub1 and sub2
	 * NA(sub1,sub2)
	 * common count the common nodes number in sub1 and sub2;
	 * na count the number of the vertex in sub1
	 * nb count the number of the vertex in sub2
	 * then NA(sub1,sub2) = common*conmmon/na*nb
	 * @param sub1
	 * @param sub2
	 * @return
	 */
	public float NA(SubGraph sub1,SubGraph sub2){
		int common = 0;
		int na=0,nb=0;
		for(int i=0;i<sub1.getNodes().size();i++){
			Node node = sub1.getNodes().get(i);
			if(sub2.getNodes().contains(node))
				common++;
		}
		na = sub1.getNodes().size();
		nb = sub2.getNodes().size();
		return (float)(common*common)/(float)(na*nb);
	}
	
	/**
	 * get a most similar subgraph in cores
	 * @return
	 */
	public SubGraph getMostSimilarSubgraph(SubGraph c){
		float max = 0;
		SubGraph returnSub = null;
		for(int i=0;i<cores.size();i++){
			SubGraph sub = cores.get(i);
			float value = NA(sub, c);
			if(value>max){
				max = value;
				returnSub = sub;
			}
		}
		return returnSub;
	}
	
	/**
	 * 
	 * @param sub
	 */
	public void redundancy_filtering(SubGraph c){
		SubGraph b = getMostSimilarSubgraph(c); //b is c's most similar subgraph in cores
		if(b==null){
			cores.add(c);
			return;
		}
		if(NA(b,c)<similarity){
			cores.add(c);           //insert c into cores
		}else{
			if(c.getDensity()*c.getNodes().size()>=b.getDensity()*b.getNodes().size()){
				cores.remove(b);
				cores.add(c);
			}
		}
	}
	private int index = 0;
	/**
	 * 
	 * @param sub
	 */
	public List<SubGraph> Core_removal(SubGraph sub){
		List<SubGraph> subs = new ArrayList<SubGraph>();
		if(sub.getDensity()>=density){
			subs.add(sub);
		}else{
			List<Node> coreNodes = sub.getCoreNode();  //get the core node whose degree is larger than the avgdegree of sub
			sub.removeCoreNodes(coreNodes);  //remove the coreNodes from sub
			List<SubGraph> sgs = sub.getConnectedComponment();  //obtain a set of connected componments
			//to here there is no reference to  sub
			for(int i=0;i<sgs.size();i++){
				SubGraph subgraph = sgs.get(i);  //get a connectecd componment
				List<SubGraph> subs2 = Core_removal(subgraph);
				for(int j=0;j<subs2.size();j++){
					subs2.get(j).getNodes().addAll(coreNodes);  //insert corenodes into a connected componment
					subs.add(subs2.get(j)); //add the preliminary core 
				}
			}
		}
		return subs;
	}

	public void setCores(List<SubGraph> cores) {
		this.cores = cores;
	}
	public  List<SubGraph> getCore() {
		for(int i=0;i<cores.size();){
			if(cores.get(i).getNodes().size()<3){
				cores.remove(i);
			}else{
				i++;
			}
		}
		Collections.sort(cores);
		return cores;
	}
	
//	public static void main(String args[]) throws IOException{
//		new Main();
//	}

}
