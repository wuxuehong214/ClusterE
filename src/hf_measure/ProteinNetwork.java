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
	//蛋白质网络中的所有蛋白质
	public Set<String> proteins = new HashSet<String>();
	// all the clusters
	//待分析的所有clusters
	public List<ClusterVo> clusters = new ArrayList<ClusterVo>();
	
	//function to the protein in current network
	//当前网络下 含有某一功能的所有蛋白质
	public Map<String,Set<String>> functionToProteinMap = new HashMap<String,Set<String>>();
	
	//all the functions to the protein
//	public Map<String,Set<String>> funsToProteins = new HashMap<String,Set<String>>();
	//all the protein to the functions
	//当前蛋白质网络下每个蛋白质对应的功能信息
	public Map<String,Set<String>> proteinsToFunctionsMap = new HashMap<String,Set<String>>();
	
	//当前蛋白质网络中 每个蛋白的邻居节点信息
	public Map<String,Set<String>> proteinNeighbours = new  HashMap<String,Set<String>>();
	
	//记录蛋白质网络中边信息
//	public Map<String,Integer> edgeMap = new HashMap<String,Integer>();
	/**
	 * 读取蛋白质功能注释信息
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
	 * 计算每个cluster的pvalue值
	 * @param clusters
	 */
	public void calculateTopological(DAGanalysis dag){
		Set<String> func = new HashSet<String>();   //定义一个容器 存储某个cluster所包含的所有功能名称
		String fun;  //功能名称
//		double min = 100;  //p-value最小值初始化
		int funnum = 0;  //某一个cluster中包含某一功能的 蛋白质数量
		int size = 0;
		double fmeasure = 0;
		double hfmeasure = 0;
		HashMap<String, Double> wmap = new HashMap<String, Double>();
		for(int i = 0 ; i < clusters.size() ; i++){
System.out.println("analyzing....cluster"+(i+1));
			ClusterVo cluster = clusters.get(i); //依次获取各个cluster对象
			func.clear();  //清空容器  该容器用于存储当前cluster对象中所有蛋白可能的所有功能。 
//			min = 100;
			hfmeasure = 0;
			fmeasure = 0;
			size = cluster.getProteins().size(); //获得当前cluster对象中蛋白个数
			
			//获取当前分析cluster中所有功能
			Iterator<String> it = cluster.getProteins().iterator();   //一次迭代每个蛋白质
			while(it.hasNext()){
				String p = it.next();
				Set<String> funcs = proteinsToFunctionsMap.get(p);  //获取当前蛋白质包含的功能
				if(funcs!=null)
					func.addAll(funcs);     //将所有蛋白功能存储到容器func中
			}
			
			//依次5分析每一个功能。 
			Iterator<String> it2 = func.iterator();      //迭代每个功能
			while(it2.hasNext()){  //依次遍历每个功能  计算p-value值最小的那一个
				funnum = 0;
				fun = it2.next();   //获取当前计算功能
				double total_wi = 0;
				it = cluster.getProteins().iterator();  //依次迭代每个蛋白
				int background = functionToProteinMap.get(fun).size(); //获取含当前功能fun的蛋白个数
				
				while(it.hasNext()){      
					double max = -10;
					String protein = it.next(); //获取当前计算蛋白
					Set<String> funcs = proteinsToFunctionsMap.get(protein); //获取该蛋白所有功能集合
					if(funcs != null){
						if(!funcs.contains(fun)) {
							Iterator<String> fit = funcs.iterator();  //依次迭代非fun功能，计算其相似性。
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
					}else{  //功能未知 的蛋白质
						double wwi = maxWiTopology(protein, cluster, fun, dag);
						if(wwi > max)max = wwi;
						
						cluster.getUnknownProteins().add(protein);
						cluster.setCotainUnknown(true); //该cluster包含功能未知的蛋白质
					}
					if(max != -10)
						total_wi += max;	
					
					//当前蛋白质复合物Cluster中 含有该功能的蛋白质个数统计
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
	 * 给定两个蛋白质功能
	 * 计算： 该蛋白质复合物Cluster中， 有多少对蛋白质含有这两个功能(一个蛋白质含有f1功能 ，另一个蛋白质含有f2功能)
	 *       再计算这些蛋白质对中  实际存在关系的边数的比例
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
		//分析功能未知的蛋白质的时候查看其邻居蛋白  
		Set<String> neighbours = proteinNeighbours.get(protein);
		//邻居蛋白质节点个数
		int NI = neighbours.size();
		//邻居蛋白节点功能集合
		List<String> functions = new ArrayList<String>();
		//邻居节点蛋白功能集合
		Set<String> funs = new HashSet<String>();
		//依次迭代每个邻居蛋白
		Iterator<String> it = neighbours.iterator();
		//获取蛋白功能集合
		while (it.hasNext()) {
			Set<String> fs = proteinsToFunctionsMap.get(it.next());
			if (fs != null) {
				functions.addAll(fs);
				funs.addAll(fs);
			}
		}
		
		//依次迭代每个可能的功能
		it = funs.iterator();
		double max = -10000;
		while (it.hasNext()) {
			String fun = it.next();
			int freq = 0;
			//邻居蛋白功能集合中含有当前待分析功能fun的 蛋白节点个数
			for (int i = 0; i < functions.size(); i++) {
				if (fun.equals(functions.get(i)))
					freq++;
			}
			//考虑拓扑结构
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
	 * 计算每个cluster的pvalue值
	 * @param clusters
	 */
	public void calculateUntopology(DAGanalysis dag){
		Set<String> func = new HashSet<String>();   //定义一个容器 存储某个cluster所包含的所有功能名称
		String fun;  //功能名称
		double min = 100;  //p-value最小值初始化
		int funnum = 0;  //某一个cluster中包含某一功能的 蛋白质数量
		int size = 0;
		double fmeasure = 0;
		double hfmeasure = 0;
		HashMap<String, Double> wmap = new HashMap<String, Double>();
		abc:
		for(int i = 0 ; i < clusters.size() ; i++){
System.out.println("analyzing....cluster"+(i+1));
			ClusterVo cluster = clusters.get(i); //依次获取各个cluster对象
			func.clear();  //清空容器  该容器用于存储当前cluster对象中所有蛋白可能的所有功能。 
			min = 100;
			hfmeasure = 0;
			fmeasure = 0;
			size = cluster.getProteins().size(); //获得当前cluster对象中蛋白个数
			
			//获取当前分析cluster中所有功能
			Iterator<String> it = cluster.getProteins().iterator();   //一次迭代每个蛋白质
			while(it.hasNext()){
				String p = it.next();
				if(!proteins.contains(p)) continue abc;
				Set<String> funcs = proteinsToFunctionsMap.get(p);  //获取当前蛋白质包含的功能
				if(funcs!=null)
					func.addAll(funcs);     //将所有蛋白功能存储到容器func中
			}
			
			//依次5分析每一个功能。 
			Iterator<String> it2 = func.iterator();      //迭代每个功能
			while(it2.hasNext()){  //依次遍历每个功能  计算p-value值最小的那一个
				funnum = 0;
				fun = it2.next();   //获取当前计算功能
				double total_wi = 0;
				it = cluster.getProteins().iterator();  //依次迭代每个蛋白
				int background = functionToProteinMap.get(fun).size(); //获取含当前功能fun的蛋白个数
//				System.out.println();
//				System.out.println("--------------------"+fun+"-------------------");
				while(it.hasNext()){      
					double max = -10;
					String protein = it.next(); //获取当前计算蛋白
					Set<String> funcs = proteinsToFunctionsMap.get(protein); //获取该蛋白所有功能集合
					if(funcs != null){
						if(!funcs.contains(fun)){
							Iterator<String> fit = funcs.iterator();  //依次迭代非fun功能，计算其相似性。
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
					}else{  //功能未知 的蛋白质
						double wwi = maxWiUntopology(protein, cluster, fun, dag);
						if(wwi > max)max = wwi;
						
						cluster.getUnknownProteins().add(protein);
						cluster.setCotainUnknown(true); //该cluster包含功能未知的蛋白质
					}
					if(max != -10)
						total_wi += max;	
					
					//当前蛋白质复合物Cluster中 含有该功能的蛋白质个数统计
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
		//分析功能未知的蛋白质的时候查看其邻居蛋白  
		Set<String> neighbours = proteinNeighbours.get(protein);
		//邻居蛋白质节点个数
		int NI = neighbours.size();
		//邻居蛋白节点功能集合
		List<String> functions = new ArrayList<String>();
		//邻居节点蛋白功能集合
		Set<String> funs = new HashSet<String>();
		//依次迭代每个邻居蛋白
		Iterator<String> it = neighbours.iterator();
		//获取蛋白功能集合
		while (it.hasNext()) {
			Set<String> fs = proteinsToFunctionsMap.get(it.next());
			if (fs != null) {
				functions.addAll(fs);
				funs.addAll(fs);
			}
		}
		//依次迭代每个可能的功能
		it = funs.iterator();
		double max = -10000;
		while (it.hasNext()) {
			String fun = it.next();
			int freq = 0;
			//邻居蛋白功能集合中含有当前待分析功能fun的 蛋白节点个数
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
