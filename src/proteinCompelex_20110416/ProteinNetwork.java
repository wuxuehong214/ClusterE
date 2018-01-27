package proteinCompelex_20110416;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date£º2011-3-21 ÏÂÎç02:05:50 
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ProteinNetwork {
	
	public Map<String,NodeVo> strToNode = new HashMap<String,NodeVo>();
	public Map<String,EdgeVo> strToEdge = new HashMap<String,EdgeVo>();
	public List<EdgeVo> edges = new ArrayList<EdgeVo>();
	public List<NodeVo> nodes = new ArrayList<NodeVo>();
	
	private GeneExpression ge ;
	private DAGanalysis dag;
	
	public ProteinNetwork(GeneExpression ge,DAGanalysis dag){
		this.ge = ge;
		this.dag = dag;
	}
	
	public void initialize(String filename) throws IOException{
		System.out.print("Reading PPI ....");
		long time = System.currentTimeMillis();
		readProteins(filename);
		System.out.println((System.currentTimeMillis()-time)+"ms");
	}
	/**
	 * read  ppi 
	 * @param filename
	 * @throws IOException
	 */
	public void readProteins(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		Scanner s = null;
		String p1,p2,w;
		double r = 0;
		NodeVo node1,node2;
		while(str!=null){
			try{
			 s = new Scanner(str);
			 p1 = s.next().toUpperCase();      //get the first protein
			 node1 = strToNode.get(p1);
			 if(node1==null){
				 node1 = new NodeVo(p1);
				 strToNode.put(p1, node1);
				 nodes.add(node1);
			 }
			 
			 p2 = s.next().toUpperCase();
			 node2 = strToNode.get(p2);
			 if(node2==null){
				 node2 = new NodeVo(p2);
				 strToNode.put(p2, node2);
				 nodes.add(node2);
			 }
			 
			 EdgeVo e = strToEdge.get(p1+p2);
			 r=0;
			 if(s.hasNext()){
				 w = s.next();
				 r = Double.parseDouble(w);
			 }
			 if(e==null){
				 e = new EdgeVo(p1,p2);
				 edges.add(e);
				 strToEdge.put(p1+p2, e);
				 strToEdge.put(p2+p1, e);
				 
				 double pcc = ge.getPCC(p1, p2);
				 e.setPcc(pcc);
				 node1.getNeighbours().add(node2);
				 node2.getNeighbours().add(node1);
//				 node1.setWeight(node1.getWeight()+pcc);
//				 node2.setWeight(node2.getWeight()+pcc);
			 }
			}catch(Exception e){
				System.out.println("Exception happens.....");
				continue;
			}
			 str = br.readLine();
		}
		br.close();
	}
	//############################################end################################################
	
	public static void main(String args[]) throws IOException{
//		ProteinNetwork pnt = new ProteinNetwork();
	}

}
