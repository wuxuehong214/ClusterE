package temp;
/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
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

public class ProteinNetwork {
	//�ַ������ڵ����ӳ��
	public Map<String,NodeVo> strToNode = new HashMap<String,NodeVo>();
	//�ַ������߶���ӳ��
	public Map<String,EdgeVo> strToEdge = new HashMap<String,EdgeVo>();
	//�������书�����Ƽ��ϵ�ӳ��
	public Map<String,Set<String>> proteinToFunctions = new HashMap<String,Set<String>>();
	//�������Ƶ������ʼ���ӳ��
	public Map<String,Set<String>> funToProteins = new HashMap<String,Set<String>>();

	public void initialize(String ppi,String geneontology) throws IOException{
		System.out.print("Reading PPI and GeneOntoloty....");
		long time = System.currentTimeMillis();
		readProteins(ppi);
		readGeneOntology(geneontology);
		System.out.println((System.currentTimeMillis()-time)+"ms");
	}
	/**
	 * read the gene ontology file
	 * @param filename
	 * @throws IOException
	 */
	public void readGeneOntology(String filename) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		Scanner s = null;
		String protein = null;
		while(str!=null){
			s = new Scanner(str);
			protein = s.next();
			Set<String> funs = new HashSet<String>();
			proteinToFunctions.put(protein, funs);
			while(s.hasNext()){
				String fun = s.next();
				funs.add(fun);
				Set<String> proteins = funToProteins.get(fun);
				if(proteins==null){
					proteins = new HashSet<String>();
					funToProteins.put(fun, proteins);
				}
				proteins.add(protein);
			}
			str = br.readLine();
		}
		br.close();
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
		String p1,p2;
		NodeVo node1,node2;
		while(str!=null){
			try{
			 s = new Scanner(str);
			 p1 = s.next().toUpperCase();      //get the first protein
			 node1 = strToNode.get(p1);
			 if(node1==null){
				 node1 = new NodeVo(p1);
				 strToNode.put(p1, node1);
			 }
			 
			 p2 = s.next().toUpperCase();
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
			}catch(Exception e){
				System.out.println("Exception happens.....");
				continue;
			}
			 str = br.readLine();
		}
		br.close();
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
	 * �����ڵ㹲ͬ�ھӸ���+1�����ھӸ�����С���Ǹ���
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
		result = (float)(count+1)/min;
		return result;
	}
	
	/**
	 * ����ÿ��cluster��pvalueֵ
	 * @param clusters
	 */
	public void calculatePvalue(List<ClusterVo> clusters, int total_size ){
		
		Set<String> func = new HashSet<String>();   //����һ������ �洢ĳ��cluster�����������й�������
		
		String fun;  //��������
		
		double min = 100;  //p-value��Сֵ��ʼ��
		
		int funnum = 0;  //ĳһ��cluster�а���ĳһ���ܵ� ����������
		
		int size = 0 ;
		
		for(int i = 0 ; i < clusters.size() ; i++){
			
			ClusterVo cluster = clusters.get(i);
			
			func.clear();  //�������
			
			min = 100 ;
			
			size = cluster.getProteins().size();
			
			for(int j = 0 ; j < size ; j++){//��ȡ��cluster�����п��ܵĹ���
				
				Set<String> funcs = proteinToFunctions.get(cluster.getProteins().get(j));
				
				if(funcs != null)
						
					func.addAll(funcs);
				
			}
			
			Iterator<String> it = func.iterator();
			
			while(it.hasNext()){  //���α���ÿ������  ����p-valueֵ��С����һ��
				
				funnum = 0;
				
				fun = it.next();
				
				for(int k = 0 ; k < size ; k++){
					
					Set<String> funcs = proteinToFunctions.get(cluster.getProteins().get(k));
					
 					if(funcs != null && funcs.contains(fun)) funnum++;
					
				}
				
				double value = Pvalue.CalPvalue(total_size, cluster.getProteins().size(), funnum, funToProteins.get(fun).size());
				
				if(value < min ){
					
					min  = value;
					
					cluster.setFunction(fun);
					
				}
				
			}
			
			cluster.setPvalue(min);
			
		}
	}
	//############################################end################################################
	
	public static void main(String args[]) throws IOException{
//		ProteinNetwork pnt = new ProteinNetwork();
	}

}
