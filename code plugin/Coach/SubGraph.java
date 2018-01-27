package com.wuxuehong.plugin;


import java.util.*;

public class SubGraph implements Comparable{
	
	private List<Node> nodes;
	private Map<String,Edge> edgeMap;
	
	public SubGraph(Node node,Map<String,Edge> edgeMap){
		nodes = new ArrayList<Node>();
		nodes.add(node);
		nodes.addAll(node.getNeighbours());
		this.edgeMap = edgeMap;
	}
	
	public SubGraph(Map<String,Edge> edgeMap){
		nodes = new ArrayList<Node>();
		this.edgeMap = edgeMap;
	}
	
	/**
	 * get rid of the node with 1 degree
	 * @return
	 */
	public void filter(){
		int count = 0;                      //count the degree
		for(int i=0;i<nodes.size();){
			Node n1 = nodes.get(i);            //check the node
			count = 0;                         //initialize the count
			for(int j=0;j<nodes.size();j++){
				Node n2 = nodes.get(j);
				if(edgeMap.get(n1.getName()+n2.getName())!=null)count++;  //degree ++
			}
			if(count<2){                      //if the degree is less than 2 then remove it
				nodes.remove(i);  
				i=0;                        //recheck the subgraph from the first
			}else 
				i++;
		}
	}
	
	/**
	 * get core from the subgraph
	 * 
	 * for that each vertex whose degree is more than the average degree of the subgraph 
	 */
	public void getCore(){
		int count=0;			//count the degree for each vertex
		float avg = getAvgDegree();     //get average degree of the subgraph
		List<Node> nodes2 = new ArrayList<Node>();  //use to store the node whose degree is less than avgdegree
		for(int i=0;i<nodes.size();i++){
			count = 0;				//initialize for each vertex
			for(int j=0;j<nodes.size();j++){
				if(edgeMap.get(nodes.get(i).getName()+nodes.get(j).getName())!=null)count++;
			}
			if(count<avg)nodes2.add(nodes.get(i));
		}
		nodes.removeAll(nodes2);  //set the rest nodes whose degree is more than avgdegree
	}
	/**
	 * get the minimum degree node 
	 * @return
	 */
	public Node getMinmDegreeNode(){
		Node node = null;
		int mindegree = 10000;
		int count = 0;
		for(int i=0;i<nodes.size();i++){
			Node n = nodes.get(i);
			count = 0;                  //initialize the variable count for each node
			for(int j=0;j<nodes.size();j++){
				Node n2 = nodes.get(j);
				if(edgeMap.get(n.getName()+n2.getName())!=null)count++; //degree ++
			}
			if(count<mindegree){
				node = n;
				mindegree = count;
			}
		}
		return node;
	}
	
	/**
	 * get a node that have max degree with subgraph from cgnodes
	 * @param cgnodes
	 * @return
	 */
	public Node getMaxDegreeNode(List<Node> cgnodes){
		Node returnNode = null;
		int max = -1;
		int count = 0;
		for(int i=0;i<cgnodes.size();i++){   //check all the nodes in cgnodes
			Node node = cgnodes.get(i);
			if(nodes.contains(node))continue;
			count = 0;
			for(int j=0;j<nodes.size();j++){
				Node node2 = nodes.get(j);
				if(edgeMap.get(node.getName()+node2.getName())!=null)
					count++;
			}
			if(count>max){
				returnNode = node;
				max = count;
			}
		}
		if(max==0)
		return null;         //if count==0 ,then there is no node to correspond to the subgraph
		else
			return returnNode;
	}
	
	
	/**
	 * get the densit of the subgraph if there is anohter node added to it !
	 * @param addnode
	 * @return
	 */
	public float getDensity(Node addnode){
		int count = 0;//used to count the number of the edge in the subgraph
		int count2=0; //used to count the muber of the edge that the addnode corresponds to the subgraph
		for(int i=0;i<nodes.size();i++){
			Node n1 = nodes.get(i);
			for(int j=i+1;j<nodes.size();j++){
				if(edgeMap.get(n1.getName()+nodes.get(j).getName())!=null)count++;
			}
			if(edgeMap.get(n1.getName()+addnode.getName())!=null)count2++;
		}
		count+=count2;
		int size = nodes.size()+1;
		return (float)(2*(count))/(float)(size*(size-1));
	}
	
	/**
	 * get average degree
	 *
	 * total degrees divide  total nodes
	 * @return
	 */
	public float getAvgDegree(){
		int count = 0;          //count the number of the edge
		for(int i=0;i<nodes.size();i++){
			for(int j=i+1;j<nodes.size();j++){
				if(edgeMap.get(nodes.get(i).getName()+nodes.get(j).getName())!=null)count++;
			}
		}
		return (float)(2*count)/(float)nodes.size();
	}
	
	/**
	 * get density of the subgraph
	 * 2*e/v(v-1)
	 * @return
	 */
	public float getDensity(){
		int count = 0;    //count the number of the edge
		for(int i=0;i<nodes.size();i++){
			for(int j=i+1;j<nodes.size();j++){
				if(edgeMap.get(nodes.get(i).getName()+nodes.get(j).getName())!=null)count++;
			}
		}
		return (float)(2*count)/(float)(nodes.size()*(nodes.size()-1));
	}
	
	/**
	 * get core node
	 * return the nodes which  
	 * @return
	 */
	public List<Node> getCoreNode(){
		List<Node> coreNodes = new ArrayList<Node>();
		int count = 0;  //count the degree
		float avgdegree = getAvgDegree();  //get the avg degree
		for(int i=0;i<nodes.size();i++){
			count=0;              //initialize
			Node node = nodes.get(i);
			for(int j=0;j<nodes.size();j++){
				if(edgeMap.get(node.getName()+nodes.get(j).getName())!=null){
					count++;
				}
			}
			if(count>=avgdegree) coreNodes.add(node);        //if deg(v)>avgdegree then it is a core vertex
		}
		return coreNodes;
	}
	/**
	 * remove all the core nodes from the subgraph
	 * @param coreNodes
	 */
	public void removeCoreNodes(List<Node> coreNodes){
		nodes.removeAll(coreNodes);
	}
	
	/**
	 * after remove the core nodes from the subgraph 
	 * then get a set of connected componment
	 * @return
	 */
	public List<SubGraph> getConnectedComponment(){
		List<SubGraph> sgs = new ArrayList<SubGraph>();
		SubGraph sub = null;
		Set<Node> nodelist;            //used to store the nodes connected componment
		nodelist = new HashSet<Node>();   //
		while(nodes.size()!=0){            //while the nodes size in this subgraph is zero then end 
			Node node = nodes.get(0);         //get the first node
			nodelist.clear();
			nodelist.add(node);                   //add the node to the set
			nodes.remove(node);        //remove the node from the subgraph
			for(int i=0;i<nodes.size();i++){                   //check all the nodes in the rest of the subgraph
				Iterator<Node> it = nodelist.iterator();       //get the iterator of the nodelist
				while(it.hasNext()){                            
					Node node2 = it.next();               //check all the node in the nodelist too see if a node in the rest of the subgraph have an interaction with them
					if(edgeMap.get(node2.getName()+nodes.get(i).getName())!=null){
						nodelist.add(nodes.get(i));
						nodes.remove(i);
						i--;                         // if a node remove from the nodes ,then go to next but the value of i should not be changed
						break;
					}
				}
			}
			if(nodelist.size()>1){
			sub = new SubGraph(edgeMap);
			sub.getNodes().addAll(nodelist);
			sgs.add(sub);
			}
		}
		return sgs;
		
	}
	
	/**
	 * check the subgraph if there is no nodes  then it is empty
	 * @return
	 */
	public boolean isEmpty(){
		if(nodes.size()==0)return true;
		else return false;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		SubGraph s = (SubGraph)o;
		return s.getNodes().size()-this.getNodes().size();
	}
	
	

}
