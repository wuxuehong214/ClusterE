package hf_measure;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-3-21 下午02:45:22 
 * 
 */

public class DAGanalysis {
	
	//store the id of the term to the instance of a term
	public Map<String,TermVo> strToTermMap = new HashMap<String,TermVo>();
	// lastRank denote the last deap of the DAG
	public int largestRank = 0 ; 
	private String largestTerm = "";
	private String ROOTID = null;
	
	//Read the annotations from the Molecular Function of GO
	//then build the DAG
	public void buildDAGwithMF(String filename) throws IOException{
		strToTermMap.clear();
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = null;
		Scanner s = null;
		str = br.readLine();
		String goid = null;  //GO id
		String parent = null;  //parent id
		while(str!=null){
			
			s = new Scanner(str);
			goid = s.next();         //get the term id first
			TermVo tv = strToTermMap.get(goid);  //check if this term built
			if(tv==null){               //if not build it and add it to the map
				tv = new TermVo();
				strToTermMap.put(goid, tv);
			}
			tv.setId(goid);   //get the GO ID of the term
			
			while(s.hasNext()){
				parent = s.next();    //get the parent id of this term
				tv.getParents().add(parent); //add the parent id to this term
				//consider that this id is the child id of the parent id
				//we should add the child id to the parent term
				TermVo tv2 = strToTermMap.get(parent);  //check the map to see if the parent term built
				if(tv2==null){
					tv2 = new TermVo();           //if not build it
					strToTermMap.put(parent, tv2);  //put the new Term to the map
				}
				tv2.getChildren().add(goid);  //add the this term's id to the parent as a child id
			}
			str = br.readLine();
			tv.setName(str);  //get the name of the Term
			str = br.readLine();
		}
	}
	
	/**
	 * While we get the DAG,we should rank the DAG for each layer.
	 * start with the root get 0.
	 * 
	 */
	public void rankDAG(String rootId){
		largestRank = 0;
		largestTerm = "";
		ROOTID = rootId; //The id of "molecular_function" the root id.it gets the value 0.
		TermVo rootTerm = strToTermMap.get(ROOTID);
		deapRank(rootTerm, 0);
		
		System.out.println("largest Rank of DAG:"+largestRank);
		System.out.println("largest Term of DAG:"+largestTerm);
	}
	//deap rank the child of each term from the root
	public void deapRank(TermVo rootTerm,int rank){
		if(rank>largestRank){
			largestRank = rank;
			largestTerm = rootTerm.getId();
		}
//		if(rank==9){
//			System.out.println(rootTerm.getId());
//			System.out.println(proteinNetork.functionToProteinMap.get(rootTerm.getId()));
//		}
		if(rank>rootTerm.getRank())  //get the larger rank
			rootTerm.setRank(rank);
		if(rank<rootTerm.getLength()) //get the shorter length
			rootTerm.setLength(rank);
		Set<String> children = rootTerm.getChildren();
		Iterator<String> it = children.iterator();
		while(it.hasNext()){
			String goid = it.next();
			TermVo tv = strToTermMap.get(goid);
			deapRank(tv, rank+1);
		}
	}
	
	//#############################get Ancestor begin...########################################################################
	/**
	 * given two term ,then get the ancestor which has the maximum rank value.
	 * @return
	 */
	public String getAncestor(String termi,String termj){
		TermVo ti = strToTermMap.get(termi);
		TermVo tj = strToTermMap.get(termj);
		if(ti==null||tj==null){
			System.out.println("lost......");
			return null;
		}
//System.out.println(termi+"\t"+ti+"\t"+tj);
		if(ti.getRank()>tj.getRank()){
			Set<String> tempSet = new HashSet<String>();
			Set<String> results = new HashSet<String>();   //store the terms in the rank tj.getRank
			getParentsToRank(tempSet, tj.getRank(), ti, results);
			
			tempSet.clear();
			tempSet.add(tj.getId());
			return getAncestor(tempSet, results);
		}else if(ti.getRank()<tj.getRank()){
			Set<String> tempSet = new HashSet<String>();
			Set<String> results = new HashSet<String>();   //store the term in the rank ti.getRank
			getParentsToRank(tempSet, ti.getRank(), tj, results);
			
			tempSet.clear();
			tempSet.add(ti.getId());
			return getAncestor(tempSet, results);
		}else{
			Set<String> tempSet = new HashSet<String>();
			Set<String> results = new HashSet<String>();
			
			tempSet.add(ti.getId());
			results.add(tj.getId());
			return getAncestor(tempSet, results);
		}
	}
	// given the term with a deaper rank. we want to get terms which have the shallow rank.
	public void getParentsToRank(Set<String> tempSet,int rank,TermVo tv, Set<String> results){
		if(tv.getRank()<=rank){
			results.add(tv.getId());
		}
		if(tv.getRank()<rank)
			return;
		tempSet = new HashSet<String>();
		tempSet.addAll(tv.getParents());
		Iterator<String> it = tempSet.iterator();
		while(it.hasNext()){
			tv = strToTermMap.get(it.next());
			getParentsToRank(tempSet, rank, tv, results);
		}
	}
	//given two sets of terms,among which they all have the same rank
	//get the ancestor
	public String getAncestor(Set<String> terms1,Set<String> terms2){
		if(terms1.size()==0||terms2.size()==0) return null;
		Iterator<String> it = terms1.iterator();
		int rank = -1;  //get the base rank
		while(it.hasNext()){
			String tempstr = it.next();
			if(rank==-1)
			rank = strToTermMap.get(tempstr).getRank();
			if(terms2.contains(tempstr))
				return tempstr;
		}
		it = terms1.iterator();
		Set<String> terms11 = new HashSet<String>();
		while(it.hasNext()){
			String tempstr = it.next();
			TermVo tv = strToTermMap.get(tempstr);
			terms11.addAll(tv.getParents());
		}
		it = terms2.iterator();
		Set<String> terms22 = new HashSet<String>();
		while(it.hasNext()){
			String tempstr = it.next();
			TermVo tv = strToTermMap.get(tempstr);
			if(tv.getRank()<=rank)
				terms22.add(tv.getId());
			terms22.addAll(tv.getParents());
		}
		return getAncestor(terms11, terms22);
	}
	//############################get Ancestor end...########################################################################
	
	private Map<String,Double> antoChild = new HashMap<String,Double>();
	//############################Calculate similarity begin...##############################################################
	public double getSimilarityMinusEp(String termi, String termj){
		String ancestor = getAncestor(termi, termj);    //get the ancestor first
		if(ancestor==null) return 0;
		TermVo ta = strToTermMap.get(ancestor);
		TermVo ti = strToTermMap.get(termi);
		TermVo tj = strToTermMap.get(termj);
		double dij = 0;
		Double ddih = antoChild.get(termi+termj);
		if(ddih==null)
			ddih = antoChild.get(termj+termi);
		if(ddih==null){
			dij = getDistance(ancestor, termi)+getDistance(ancestor, termj); //get the distance between termi and termj
			antoChild.put(termi+termj,dij);
			antoChild.put(termj+termi,dij);
		}else{
			dij = ddih.doubleValue();
		}
		
		double similarity = Math.pow(ta.getLength(), 2)/(Math.pow(ta.getLength(), 2)+largestRank*dij);
		
		double ep=0;
//		double result = dij/(double)((ta.getLength()+dij)*largestRank);
		double result = 0;
		if(tj.getLength() != 0)
		  result = (largestRank*dij)/((Math.pow(ta.getLength(), 2)+largestRank*dij)*tj.getLength());
		if(termi.equals(termj))
		  ep=0;
		else if(!termi.equals(termj)&&ancestor.equals(termj)){
			ep = -result;
		}else
			ep = result;
		if(similarity-ep < 0)
			return 0;
		else
			return similarity-ep;
	}
	
	public double getHfSimilarity(String termi, String termj,float bili){
		String ancestor = getAncestor(termi, termj);    //get the ancestor first
		if(ancestor==null)return 0;
		TermVo ti = strToTermMap.get(termi);
		TermVo tj = strToTermMap.get(termj);
		TermVo ta = strToTermMap.get(ancestor);
//		double dij = ti.getLength()+tj.getLength()-2*ta.getLength(); //get the distance between termi and termj
		double dij = getDistance(ancestor, termi)+getDistance(ancestor, termj);
	//	System.out.println(ta.getLength()+"**"+dij);
		double similarity = Math.pow(ta.getLength(), 2)/(Math.pow(ta.getLength(), 2)+largestRank*dij);
		return similarity*bili;
	}
	//############################Calculate similarity end...###############################################################
	//get the distance between ancestor and child
	public double getDistance(String ancestor,String child){
		LinkedList<String> queue = new LinkedList<String>();
		queue.add(ancestor);
		queue.add("myflag");
		int deap=0;
		while(queue.size()!=0){
			String func = queue.removeFirst();
			if(func.equals("myflag")){
				deap++;
				queue.add("myflag");
				continue;
			}
			
			if(func.equals(child)){
				return deap;
			}else{
				TermVo tv = strToTermMap.get(func);
				queue.addAll(tv.getChildren());
			}
		}
		return deap;
	}
	
	//############################Calculate ep begin...##############################################################
	public double getEp(String termi, String termj){
		if(termi.equals(termj)){
			return 0;
		}
		String ancestor = getAncestor(termi, termj);    //get the ancestor first
		TermVo ta = strToTermMap.get(ancestor);
		double dij = getDistance(ancestor, termi)+getDistance(ancestor, termj);
		double ep=0;
		double result = dij/(double)(ta.getLength()+dij+largestRank);
		if(termi.equals(termj))
		  ep=0;
		else if(!termi.equals(termj)&&ancestor.equals(termj)){
			ep = -result;
		}else
			ep = result;
		return ep;
	}
	//############################Calculate ep end...###############################################################
	
	public static void main(String args[]) throws IOException{
//		DAGanalysis main = new DAGanalysis(null);
//		main.buildDAGwithMF("D:\\Program\\ForPvalue\\最新GO数据\\GO_Function.txt");
//		main.rankDAG();
//		
//		System.out.println(main.getAncestor("GO:0016740", "GO:0042800"));
//		System.out.println(main.getHfSimilarity("GO:0016740", "GO:0042800"));
//		System.out.println(main.getEp("GO:0016740", "GO:0042800"));
//		System.out.println(main.largestRank);
		//System.out.println(main.getDistance("GO:0004558", "GO:0004575"));
	}
}
