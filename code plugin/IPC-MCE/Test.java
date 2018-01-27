package com.wuxuehong.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;

public class Test {
	
	public List<MyNode> mynodes = new ArrayList<MyNode>();
	public List<MyEdge> myedges = new ArrayList<MyEdge>();
	
	public Map<String,MyNode> nodeMap = new HashMap<String,MyNode>();
	public Map<String,MyEdge> edgeMap = new HashMap<String,MyEdge>();
	
	public List<ArrayList<MyNode>> hubs = new ArrayList<ArrayList<MyNode>>();
	
	public Set<HashSet<MyNode>> ds = new HashSet<HashSet<MyNode>>();
	
	public Set<HashSet<MyNode>> maxclique = new HashSet<HashSet<MyNode>>();
	
	public double door ;
	
	public Test(double door){
		this.door = door;
		readFile();
		Collections.sort(mynodes);
	}
	
	public Vector[] getResult(){
		////////////////////////HUBS///////////////////////////
		getHubs();
		////////////////////////��ȡ������///////////////////////////
		Set<MyNode> temp = new HashSet<MyNode>();
		temp.addAll(mynodes);
		Bronkerboschl(new HashSet<MyNode>(), temp, new HashSet<MyNode>());
		///////////////////////////��������չ///////////////////////////
		expandClique();
		
		int a = hubs.size();
		int b = ds.size();
		int c = a+b;
		Vector<Vector<Node>> result = new  Vector<Vector<Node>>();
		for(int i=0;i<hubs.size();i++){
			if(hubs.get(i).size()>=3)
			{
				Vector<Node> aa = new Vector<Node>();
				aa.addAll(hubs.get(i));
				result.add(aa);
			}
		}
		Iterator it = ds.iterator();
		while(it.hasNext()){
			HashSet<Node> bb = (HashSet<Node>) it.next();
			if(bb.size()>=3)
			{
				Vector<Node> aa = new Vector<Node>();
				aa.addAll(bb);
				result.add(aa);
			}
		}
		Vector<Node>[] cc = new Vector[result.size()];
		for(int i=0;i<result.size();i++){
			cc[i] = new Vector<Node>();
			cc[i].addAll(result.get(i));
		}
		return cc;
	}
		
	
	//************************************��ȡPPI���� * @throws IOException ****************************************/
	public void readFile(){
		Vector<Edge> edges = GraphInfo.edgelist; 
		String n1,n2;
		MyNode tempNode,tempNode2;
		for(int i=0;i<edges.size();i++){
			n1 = edges.get(i).getNode1().getNodeID();           //��һ���ڵ�
			tempNode = nodeMap.get(n1);
			if(tempNode==null){
				tempNode = new MyNode(n1);
				mynodes.add(tempNode);
				nodeMap.put(n1, tempNode);
			}else{
				tempNode.setDegree(tempNode.getDegree()+1);
			}
			n2 = edges.get(i).getNode2().getNodeID(); 			//�ڶ����ڵ�
			tempNode2 = nodeMap.get(n2);
			if(tempNode2==null){
				tempNode2 = new MyNode(n2);
				mynodes.add(tempNode2);
				nodeMap.put(n2, tempNode2);
			}else{
				tempNode2.setDegree(tempNode2.getDegree()+1);
			}
			tempNode.getNeighbour().add(tempNode2);
			tempNode2.getNeighbour().add(tempNode);
			MyEdge myedge = new MyEdge(tempNode,tempNode2);
			edgeMap.put(n1.trim()+n2.trim(), myedge);
			edgeMap.put(n2.trim()+n1.trim(), myedge);
			myedges.add(myedge);
		}
		System.out.println("�ڵ�����"+mynodes.size()+"****************"+myedges.size());
	}
	/****************************************���HUB*********************************************/
	public void getHubs(){
		ArrayList<MyNode> temp ;
		for(int i=0;i<mynodes.size();){
			MyNode mn = mynodes.get(0); 
			if(mn.getDegree()!=1)break;
			temp = new ArrayList<MyNode>();
			
			temp.add(mn);										//����Ϊ1�Ľڵ��������
			mynodes.remove(mn);									//����Ϊ1�Ľڵ��Ƴ�
			
			MyNode node = mn.getNeighbour().get(0);			//��ȡ��Ψһ�ھӽڵ�
			temp.add(node);										//����Ψһ��һ���ڽӵ��������
			
			myedges.remove(edgeMap.get(mn.getName()+node.getName())); //�Ƴ���Ϊ1�������ڵı�
			
			if(node.getDegree()==1){
				mynodes.remove(node);
			}else{
				for(int j=0;j<node.getNeighbours().size();j++){		//ɨ����Ψһ�ڽӵ� 
					if(node.getNeighbour().get(j).getDegree()==1){
						 if(mynodes.remove(node.getNeighbours().get(j)))
						 {
							 temp.add(node.getNeighbour().get(j));          //����Ϊ1�ĵ��Ƴ�
							 myedges.remove(node.getName()+node.getNeighbour().get(j).getName()); //����Ӧ�ı��Ƴ�
						 }
					}
				}
			}
			hubs.add(temp);        //�洢��HUB��ͼ
		}
	}
	/*********************************��� Maxmium clique  ������ö��**************************/
	public void Bronkerboschl(Set<MyNode> R,Set<MyNode> P,Set<MyNode> X ){
		if(P.size()==0&&X.size()==0){
			HashSet<MyNode> temp = new HashSet<MyNode>();
			temp.addAll(R);
			maxclique.add(temp);
//			java.util.Iterator<MyNode> it = R.iterator();
//			while(it.hasNext()){
//				MyNode mn = it.next();
//				System.out.print(mn.getName()+"***");
//			}
//			System.out.println();
			return;
		}
		if(P.size()==0){
//			System.out.println("error");
			return;
		}
		Set<MyNode> RR = new HashSet<MyNode>();
		RR.addAll(R);
		
		Set<MyNode> PP = new HashSet<MyNode>();
		Set<MyNode> PP2 = new HashSet<MyNode>();
		PP.addAll(P);
		PP2.addAll(P);
		
		Set<MyNode> XX = new HashSet<MyNode>();
		XX.addAll(X);
		/***************************choose a pivot vertex u in P U X****************************/
		PP.addAll(XX);
		List<MyNode> temp = new LinkedList<MyNode>();
		temp.addAll(PP);
		Collections.sort(temp);
		MyNode u = temp.get(temp.size()-1);

		PP.clear();
		PP.addAll(P);
		/***************************for each vertex v in p\N(u)****************************/
		PP.removeAll(u.getNeighbours());
		java.util.Iterator<MyNode> it = PP.iterator();
		while(it.hasNext()){
			MyNode node = it.next();
			RR.clear();
			RR.addAll(R);
			RR.add(node);
			Bronkerboschl(RR, getUnion(PP2, node.getNeighbour()), getUnion(XX, node.getNeighbour()));
			PP2.remove(node);
			XX.add(node);
		}
	}
	public Set<MyNode> getUnion(Set<MyNode> p1 ,List<MyNode> p2){
		Set<MyNode> pp1 = new HashSet<MyNode>();
		pp1.addAll(p1);
		Set<MyNode> temp = new HashSet<MyNode>();
		temp.addAll(p1);
		
		pp1.addAll(p2);
		pp1.removeAll(temp);
		
		temp.clear();
		temp.addAll(p2);
		temp.removeAll(pp1);
		
		return temp;
	}
	/*********************************��������չ************************************/
	public void expandClique(){
		HashMap<String,String> chargeMap = new HashMap<String,String>();//���ڹ����ظ�������ͼ
		java.util.Iterator<HashSet<MyNode>> it =  maxclique.iterator();  
		Set<MyNode> neis = new HashSet<MyNode>();
		while(it.hasNext()){           		//���ζ�ÿ�������Ž�����չ
			HashSet<MyNode> hs = it.next();                // �õ�һ��������
			java.util.Iterator<MyNode> it2 = hs.iterator();
			neis.clear();
			while(it2.hasNext()){
				neis.addAll(it2.next().getNeighbour());   //��ȡ�ü����������е��ھӽڵ�
			}
			neis.removeAll(hs); //�����ŵ������ھӽڵ㲻����  �������ڽڵ�
			java.util.Iterator<MyNode> it3 = neis.iterator();
			while(it3.hasNext()){
				MyNode node = it3.next();
				if(IPEV(hs,node)>door){
					hs.add(node);
				}
			}
			String value = toStringValue(hs);
			if(chargeMap.get(value)==null)  //����ó�����ͼ��û��  
				ds.add(hs);					//����ó�����ͼ
		}
	}
	/**
	 * IP = E/V    E-����v�뼫����clique���ڵı���   V-�����Ŷ�����
	 * @param clique
	 * @param v
	 * @return
	 */
	public double IPEV(HashSet<MyNode> clique,MyNode v){
		java.util.Iterator<MyNode> it = clique.iterator();
		int edgeCount = 0;
		while(it.hasNext()){
			if(edgeMap.get(it.next().getName()+v.getName())!=null)
				edgeCount++;
		}
		return (double)edgeCount/(double)clique.size();
	}
	/**
	 * ��һ��HashSet<MyNode>  ���ݽڵ���������  Ȼ������������ϳ�һ���ִ�
	 * @param s
	 */
	public String toStringValue(HashSet<MyNode> s){
		java.util.Iterator<MyNode> it = s.iterator();
		List<String> strs = new LinkedList<String>();
		while(it.hasNext())
			strs.add(it.next().getName());
		Collections.sort(strs);
		String str="";
		for(int i=0;i<strs.size();i++)
			str+=strs.get(i);
		return str;
	}
	
	public void showClique(Set<HashSet<MyNode>> temp){
		java.util.Iterator<HashSet<MyNode>> it = temp.iterator();
		while(it.hasNext()){
			HashSet<MyNode> R = it.next();
			java.util.Iterator<MyNode> it2 = R.iterator();
			while(it2.hasNext()){
				MyNode mn = it2.next();
				System.out.print(mn.getName()+"***");
			}
			System.out.println();
		}
	}
	

}
