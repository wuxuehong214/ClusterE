package hf_measure;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.wuxuehong.bean.Edge;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.GraphInfo;

public class ProteinNetwork {
	//all of the proteins read from the ppi
	//�����������е����е�����
	public Set<String> proteins = new HashSet<String>();
	// all the clusters
	//������������clusters
	public List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	//function to the protein in current network
	//��ǰ������ ����ĳһ���ܵ����е�����
	public Map<String,Set<String>> functionToProteinMap = new HashMap<String,Set<String>>();
	
	//all the functions to the protein
//	public Map<String,Set<String>> funsToProteins = new HashMap<String,Set<String>>();
	//all the protein to the functions
	//��ǰ������������ÿ�������ʶ�Ӧ�Ĺ�����Ϣ
	public Map<String,Set<String>> proteinsToFunctionsMap = new HashMap<String,Set<String>>();
	
	//��ǰ������������ ÿ�����׵��ھӽڵ���Ϣ
	public Map<String,Set<String>> proteinNeighbours = new  HashMap<String,Set<String>>();
	
	//��¼�����������б���Ϣ
//	public Map<String,Integer> edgeMap = new HashMap<String,Integer>();
	/**
	 * ��ȡ�����ʹ���ע����Ϣ
	 * read the gene ontology file
	 * @param filename
	 * @throws IOException
	 */
	public void readGeneOntology(String filename) throws IOException{
		proteinsToFunctionsMap.clear();
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		Scanner s = null;
		String protein = null;
		while(str!=null){
			s = new Scanner(str);
			protein = s.next();
			Set<String> funs = new HashSet<String>();
			proteinsToFunctionsMap.put(protein, funs);
			while(s.hasNext()){
				String fun = s.next();
				funs.add(fun);
			}
			str = br.readLine();
		}
		br.close();
		
		functionToProteinMap.clear();
		Iterator<String> it = proteins.iterator();
		while(it.hasNext()){
			String p = it.next();
			 Set<String> funs = proteinsToFunctionsMap.get(p);// check the functions of this protein
			 if(funs!=null){         //if this protein has functions
				 Iterator<String> it2 = funs.iterator();
				 while(it2.hasNext()){
					 String fun = it2.next();   //get the function name
					 Set<String> proteins = functionToProteinMap.get(fun);  //check if the function to the protein stored
					 if(proteins==null){
						 proteins = new HashSet<String>();
						 functionToProteinMap.put(fun, proteins);
					 }
					 proteins.add(p);
				 } 
			 }
		}
	}
	/**
	 * read  ppi 
	 * @param filename
	 * @throws IOException
	 */
	public void readProteins() throws IOException{
		String p1,p2;
		for(int i=0;i<GraphInfo.edgelist.size();i++){
			Edge edge = GraphInfo.edgelist.get(i);
			p1 = edge.getNode1().getNodeID();
			p2 = edge.getNode2().getNodeID();
			proteins.add(p1);
			 proteins.add(p2);
			 
			 Set<String> neighbours = proteinNeighbours.get(p1);
			 if(neighbours==null){
				 neighbours = new HashSet<String>();
				 proteinNeighbours.put(p1, neighbours);
			 }
			 neighbours.add(p2);
			 
			 neighbours = proteinNeighbours.get(p2);
			 if(neighbours==null){
				 neighbours = new HashSet<String>();
				 proteinNeighbours.put(p2, neighbours);
			 }
			 neighbours.add(p1);
		}
	}
	
	/**
	 * read the test clusters
	 * @param filename
	 * @throws IOException
	 */
	public void readClusters(Vector<Node>[] cluster) throws IOException{
		
		for(int i=0;i<cluster.length;i++){
			Vector<Node> nodes = cluster[i];
			ClusterVo c = new ClusterVo();
			clusters.add(c);
			for(int j=0;j<nodes.size();j++)
				c.getProteins().add(nodes.get(j).getNodeID());
		}
	}
	
	/**
	 * ����ÿ��cluster��pvalueֵ
	 * @param clusters
	 */
	public void calculateTopological(DAGanalysis dag){
		Set<String> func = new HashSet<String>();   //����һ������ �洢ĳ��cluster�����������й�������
		String fun;  //��������
//		double min = 100;  //p-value��Сֵ��ʼ��
		int funnum = 0;  //ĳһ��cluster�а���ĳһ���ܵ� ����������
		int size = 0;
		double fmeasure = 0;
		double hfmeasure = 0;
		HashMap<String, Double> wmap = new HashMap<String, Double>();
		for(int i = 0 ; i < clusters.size() ; i++){
System.out.println("analyzing....cluster"+(i+1));
			ClusterVo cluster = clusters.get(i); //���λ�ȡ����cluster����
			func.clear();  //�������  ���������ڴ洢��ǰcluster���������е��׿��ܵ����й��ܡ� 
//			min = 100;
			hfmeasure = 0;
			fmeasure = 0;
			size = cluster.getProteins().size(); //��õ�ǰcluster�����е��׸���
			
			//��ȡ��ǰ����cluster�����й���
			Iterator<String> it = cluster.getProteins().iterator();   //һ�ε���ÿ��������
			while(it.hasNext()){
				String p = it.next();
				Set<String> funcs = proteinsToFunctionsMap.get(p);  //��ȡ��ǰ�����ʰ����Ĺ���
				if(funcs!=null)
					func.addAll(funcs);     //�����е��׹��ܴ洢������func��
			}
			
			//����5����ÿһ�����ܡ� 
			Iterator<String> it2 = func.iterator();      //����ÿ������
			while(it2.hasNext()){  //���α���ÿ������  ����p-valueֵ��С����һ��
				funnum = 0;
				fun = it2.next();   //��ȡ��ǰ���㹦��
				double total_wi = 0;
				it = cluster.getProteins().iterator();  //���ε���ÿ������
				int background = functionToProteinMap.get(fun).size(); //��ȡ����ǰ����fun�ĵ��׸���
				
				while(it.hasNext()){      
					double max = -10;
					String protein = it.next(); //��ȡ��ǰ���㵰��
					Set<String> funcs = proteinsToFunctionsMap.get(protein); //��ȡ�õ������й��ܼ���
					if(funcs != null){
						if(!funcs.contains(fun)) {
							Iterator<String> fit = funcs.iterator();  //���ε�����fun���ܣ������������ԡ�
							while(fit.hasNext()){
								String pf = fit.next();
								Double value = wmap.get(pf+fun);
								double wi ;
								if(value == null){
									wi = dag.getSimilarityMinusEp(pf, fun);
									wmap.put(pf+fun, wi);
									wmap.put(fun+pf, wi);
									float bili = getSimBili(cluster, protein, fun);
									wi = wi*bili;
								}else{
									wi = value.doubleValue();
									float bili = getSimBili(cluster, protein, fun);
									wi = wi*bili;
								}
								if(wi > max) max = wi;
							}
						}
					}else{  //����δ֪ �ĵ�����
						double wwi = maxWiTopology(protein, cluster, fun, dag);
						if(wwi > max)max = wwi;
						
						cluster.getUnknownProteins().add(protein);
						cluster.setCotainUnknown(true); //��cluster��������δ֪�ĵ�����
					}
					if(max != -10)
						total_wi += max;	
					
					//��ǰ�����ʸ�����Cluster�� ���иù��ܵĵ����ʸ���ͳ��
					if(funcs != null && funcs.contains(fun)) funnum++;
				}
				//f-measure
				double	f_measure = (double)(2*funnum)/(double)(size+background);
				if(f_measure>fmeasure){
					fmeasure = f_measure;
					cluster.setFmeasureFun(fun);
					cluster.setBackground(background);
					cluster.setFunnum(funnum);
				}
				//hf-measure
				double hf_measure = (double) (total_wi+2*funnum)/(double)(size+background);
//				System.out.println(fun+"\t\t"+hf_measure+"\t"+total_wi);
				if(hf_measure>hfmeasure){
					hfmeasure = hf_measure;
					cluster.setHfmeasureFun(fun);
					cluster.setHbackground(background);
					cluster.setHfunnum(funnum);
				}
				
			}
			cluster.setFmeasure(fmeasure);
			cluster.setHfmeasure(hfmeasure);
	System.out.println(cluster);
		}
	}
	
	/**
	 * �������������ʹ���
	 * ���㣺 �õ����ʸ�����Cluster�У� �ж��ٶԵ����ʺ�������������(һ�������ʺ���f1���� ����һ�������ʺ���f2����)
	 *       �ټ�����Щ�����ʶ���  ʵ�ʴ��ڹ�ϵ�ı����ı���
	 * @param cluster
	 * @param f1
	 * @param f2
	 * @return
	 */
	public float getSimBili(ClusterVo cluster, String protein, String fun){
		List<String> proteins = new ArrayList<String>(cluster.getProteins());
		proteins.remove(protein);
		float total = 0, truely = 0 ;
		Set<String> ps;
		for(int i=0;i<proteins.size();i++){
			ps = proteinsToFunctionsMap.get(proteins.get(i));
			if(ps == null) continue;
			if(ps.contains(fun)) {
				total++;
				if(cluster.getEdgeMap().get(proteins.get(i)+protein)!=null || cluster.getEdgeMap().get(protein+proteins.get(i))!=null)
					truely++;
			}
		}
//		System.out.println(total+"\t\t"+truely+"\t\t"+truely/total+"\t"+fun+"\t"+protein);
		return truely/total;
	}
	
	/**
	 * when a protein have no function cal its wi
	 * 
	 * @param protein
	 * @return
	 */
	public double maxWiTopology(String protein, ClusterVo cluster, String func,DAGanalysis dag) {
		//��������δ֪�ĵ����ʵ�ʱ��鿴���ھӵ���  
		Set<String> neighbours = proteinNeighbours.get(protein);
		//�ھӵ����ʽڵ����
		int NI = neighbours.size();
		//�ھӵ��׽ڵ㹦�ܼ���
		List<String> functions = new ArrayList<String>();
		//�ھӽڵ㵰�׹��ܼ���
		Set<String> funs = new HashSet<String>();
		//���ε���ÿ���ھӵ���
		Iterator<String> it = neighbours.iterator();
		//��ȡ���׹��ܼ���
		while (it.hasNext()) {
			Set<String> fs = proteinsToFunctionsMap.get(it.next());
			if (fs != null) {
				functions.addAll(fs);
				funs.addAll(fs);
			}
		}
		
		//���ε���ÿ�����ܵĹ���
		it = funs.iterator();
		double max = -10000;
		while (it.hasNext()) {
			String fun = it.next();
			int freq = 0;
			//�ھӵ��׹��ܼ����к��е�ǰ����������fun�� ���׽ڵ����
			for (int i = 0; i < functions.size(); i++) {
				if (fun.equals(functions.get(i)))
					freq++;
			}
			//�������˽ṹ
			float bili = getSimBili(cluster, protein, func);
			double re = ((double) freq / (double) NI)* dag.getHfSimilarity(fun, func,bili);
			if (re > max)
				max = re;
		}
		// cal w(vk)
		it = cluster.getProteins().iterator();
		double total_wi = 0;
		int ck = 0;
		while (it.hasNext()) {
			String pt = it.next();
			if (proteinsToFunctionsMap.get(pt) != null) { // if
				ck++; // known proteins ++
				double max2 = -100;
				Set<String> proteinFunc = proteinsToFunctionsMap.get(pt);
				Iterator<String> pfs = proteinFunc.iterator();
				while (pfs.hasNext()) {
					String pf = pfs.next();
					float bili = getSimBili(cluster, pt, func);
					double wi = dag.getSimilarityMinusEp(pf, func) * bili;
					if (wi > max2)
						max2 = wi;
				}
				if(max2>=0)
				total_wi += max2;
			}
		}
		total_wi = total_wi / (double) ck;
		double result = (max + total_wi) / (double) 2;
//		System.out.println(max+"^^^^^^^^^^^^^"+total_wi);
		return result;
	}
	
	
	
	
	
	
	
	/**
	 * ����ÿ��cluster��pvalueֵ
	 * @param clusters
	 */
	public void calculateUntopology(DAGanalysis dag){
		Set<String> func = new HashSet<String>();   //����һ������ �洢ĳ��cluster�����������й�������
		String fun;  //��������
		double min = 100;  //p-value��Сֵ��ʼ��
		int funnum = 0;  //ĳһ��cluster�а���ĳһ���ܵ� ����������
		int size = 0;
		double fmeasure = 0;
		double hfmeasure = 0;
		HashMap<String, Double> wmap = new HashMap<String, Double>();
		abc:
		for(int i = 0 ; i < clusters.size() ; i++){
System.out.println("analyzing....cluster"+(i+1));
			ClusterVo cluster = clusters.get(i); //���λ�ȡ����cluster����
			func.clear();  //�������  ���������ڴ洢��ǰcluster���������е��׿��ܵ����й��ܡ� 
			min = 100;
			hfmeasure = 0;
			fmeasure = 0;
			size = cluster.getProteins().size(); //��õ�ǰcluster�����е��׸���
			
			//��ȡ��ǰ����cluster�����й���
			Iterator<String> it = cluster.getProteins().iterator();   //һ�ε���ÿ��������
			while(it.hasNext()){
				String p = it.next();
				if(!proteins.contains(p)) continue abc;
				Set<String> funcs = proteinsToFunctionsMap.get(p);  //��ȡ��ǰ�����ʰ����Ĺ���
				if(funcs!=null)
					func.addAll(funcs);     //�����е��׹��ܴ洢������func��
			}
			
			//����5����ÿһ�����ܡ� 
			Iterator<String> it2 = func.iterator();      //����ÿ������
			while(it2.hasNext()){  //���α���ÿ������  ����p-valueֵ��С����һ��
				funnum = 0;
				fun = it2.next();   //��ȡ��ǰ���㹦��
				double total_wi = 0;
				it = cluster.getProteins().iterator();  //���ε���ÿ������
				int background = functionToProteinMap.get(fun).size(); //��ȡ����ǰ����fun�ĵ��׸���
//				System.out.println();
//				System.out.println("--------------------"+fun+"-------------------");
				while(it.hasNext()){      
					double max = -10;
					String protein = it.next(); //��ȡ��ǰ���㵰��
					Set<String> funcs = proteinsToFunctionsMap.get(protein); //��ȡ�õ������й��ܼ���
					if(funcs != null){
						if(!funcs.contains(fun)){
							Iterator<String> fit = funcs.iterator();  //���ε�����fun���ܣ������������ԡ�
							while(fit.hasNext()){
								String pf = fit.next();
								Double value = wmap.get(pf+fun);
								double wi ;
								if(value == null){
									wi = dag.getSimilarityMinusEp(pf, fun);
									wmap.put(pf+fun, wi);
									wmap.put(fun+pf, wi);
								}else{
									wi = value.doubleValue();
								}
								if(wi > max) max = wi;
							}
						}
					}else{  //����δ֪ �ĵ�����
						double wwi = maxWiUntopology(protein, cluster, fun, dag);
						if(wwi > max)max = wwi;
						
						cluster.getUnknownProteins().add(protein);
						cluster.setCotainUnknown(true); //��cluster��������δ֪�ĵ�����
					}
					if(max != -10)
						total_wi += max;	
					
					//��ǰ�����ʸ�����Cluster�� ���иù��ܵĵ����ʸ���ͳ��
					if(funcs != null && funcs.contains(fun)) funnum++;
				}
				
				//f-measure
				double	f_measure = (double)(2*funnum)/(double)(size+background);
				if(f_measure>fmeasure){
					fmeasure = f_measure;
					cluster.setFmeasureFun(fun);
					cluster.setBackground(background);
					cluster.setFunnum(funnum);
				}
				//hf-measure
				double hf_measure = (double) (total_wi+2*funnum)/(double)(size+background);
//				System.out.println(fun+"\t\t"+hf_measure+"\t"+total_wi);
				if(hf_measure>hfmeasure){
					hfmeasure = hf_measure;
					cluster.setHfmeasureFun(fun);
					cluster.setHbackground(background);
					cluster.setHfunnum(funnum);
				}
				
			}
			cluster.setHfmeasure2(hfmeasure);
			cluster.setFmeasure(fmeasure);
	System.out.println(cluster);
		}
	}
	
	/**
	 * when a protein have no function cal its wi
	 * 
	 * @param protein
	 * @return
	 */
	public double maxWiUntopology(String protein, ClusterVo cluster, String func,DAGanalysis dag) {
		//��������δ֪�ĵ����ʵ�ʱ��鿴���ھӵ���  
		Set<String> neighbours = proteinNeighbours.get(protein);
		//�ھӵ����ʽڵ����
		int NI = neighbours.size();
		//�ھӵ��׽ڵ㹦�ܼ���
		List<String> functions = new ArrayList<String>();
		//�ھӽڵ㵰�׹��ܼ���
		Set<String> funs = new HashSet<String>();
		//���ε���ÿ���ھӵ���
		Iterator<String> it = neighbours.iterator();
		//��ȡ���׹��ܼ���
		while (it.hasNext()) {
			Set<String> fs = proteinsToFunctionsMap.get(it.next());
			if (fs != null) {
				functions.addAll(fs);
				funs.addAll(fs);
			}
		}
		//���ε���ÿ�����ܵĹ���
		it = funs.iterator();
		double max = -10000;
		while (it.hasNext()) {
			String fun = it.next();
			int freq = 0;
			//�ھӵ��׹��ܼ����к��е�ǰ����������fun�� ���׽ڵ����
			for (int i = 0; i < functions.size(); i++) {
				if (fun.equals(functions.get(i)))
					freq++;
			}
			double re = ((double) freq / (double) NI)* dag.getHfSimilarity(fun, func,1);
			if (re > max){
				max = re;
			}
		}
		// cal w(vk)
		it = cluster.getProteins().iterator();
		double total_wi = 0;
		int ck = 0;
		while (it.hasNext()) {
			String pt = it.next();
			if (proteinsToFunctionsMap.get(pt) != null) { // if
				ck++; // known proteins ++
				double max2 = -100;
				Set<String> proteinFunc = proteinsToFunctionsMap.get(pt);
				Iterator<String> pfs = proteinFunc.iterator();
				while (pfs.hasNext()) {
					String pf = pfs.next();
					double wi = dag.getSimilarityMinusEp(pf, func);
					if (wi > max2)
						max2 = wi;
				}
				if(max2>=0)
				total_wi += max2;
			}
		}
		total_wi = total_wi / (double) ck;
		double result = (max + total_wi) / (double) 2;
		return result;
	}
	
}
