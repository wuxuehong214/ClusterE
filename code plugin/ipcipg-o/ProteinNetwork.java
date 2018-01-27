package ipcipg_o;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.comb 
 * @date��2011-3-21 ����02:05:50 
 * 
 * �������ڼ��ص��������� �洢������������Ϣ �Լ����㵰����������������
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
	//�ַ������ڵ����ӳ��
	public Map<String,NodeVo> strToNode = new HashMap<String,NodeVo>();
	//�ַ������߶���ӳ��
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
	 * �������ڵ����ƻ�ȡ�߶���
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
	 * ���������ڵ�֮���eccֵ
	 * �����ڵ㹲ͬ�ھӸ���  ���Զ���-1 ��С���Ǹ�
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
