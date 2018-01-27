package ipcipg_n;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.comb 
 * @date：2011-3-21 下午02:05:50 
 * 
 * 该类用于加载蛋白质网络 存储蛋白质网络信息 以及计算蛋白质网络拓扑特性
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

import com.wuxuehong.bean.Edge;
import com.wuxuehong.interfaces.GraphInfo;

public class ProteinNetwork {
	//字符串到节点对象映射
	public Map<String,NodeVo> strToNode = new HashMap<String,NodeVo>();
	//字符串到边对象映射
	public Map<String,EdgeVo> strToEdge = new HashMap<String,EdgeVo>();

	public void initialize() throws IOException{
		System.out.print("Reading PPI network....");
		long time = System.currentTimeMillis();
		readProteins();
		System.out.println((System.currentTimeMillis()-time)+"ms");
	}
	/**
	 * read  ppi 
	 * @param filename
	 * @throws IOException
	 */
	public void readProteins() throws IOException{
		String p1,p2;
		NodeVo node1,node2;
		for(int i=0;i<GraphInfo.edgelist.size();i++){
			 Edge edge = GraphInfo.edgelist.get(i);
			 p1 = edge.getNode1().getNodeID();
			 p2 = edge.getNode2().getNodeID();
			 node1 = strToNode.get(p1);
			 if(node1==null){
				 node1 = new NodeVo(p1);
				 strToNode.put(p1, node1);
			 }
			 
			 node2 = strToNode.get(p2);
			 if(node2==null){
				 node2 = new NodeVo(p2);
				 strToNode.put(p2, node2);
			 }
			 
			 EdgeVo e = getEdge(p1, p2);
			 if(e==null){
				 e = new EdgeVo(p1,p2);
				 strToEdge.put(p1+p2, e);
				 
				 node1.getNeighbours().add(node2.getName());
				 node2.getNeighbours().add(node1.getName());
			 }
		}
	}
	
	/**
	 * 根据两节点名称获取边对象
	 * @param p1
	 * @param p2
	 * @return
	 */
	public EdgeVo getEdge(String p1,String p2){
		EdgeVo e = strToEdge.get(p1+p2);
		if(e!=null){
			return e;
		}else
			return strToEdge.get(p2+p1);
	}
	
	/**
	 * 计算两个节点之间的ecc值
	 * 两个节点共同邻居个数  除以度数-1 较小的那个
	 * @param p1
	 * @param p2
	 * @return
	 */
	public float getEcc(String p1,String p2){
		float result = 0;
		float count = 0;
		float min = 0;
		NodeVo n1 = strToNode.get(p1);
		NodeVo n2 = strToNode.get(p2);
		int num1 = n1.getNeighbours().size();
		int num2 = n2.getNeighbours().size();
		for(int i=0;i<num1;i++){
			if(n2.getNeighbours().contains(n1.getNeighbours().get(i)))
				count++;
		}
		min = num1<num2?num1:num2;
		min--;
		if(min==0) return 0;
		result = (float)(count)/min;
		return result;
	}
	
}
