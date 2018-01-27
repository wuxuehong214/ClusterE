package temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-6-11 下午02:56:13 
 * 
 */

public class EssentialProtein {
	
	private Map< String, Integer> essentialMap = new HashMap< String, Integer>();
	
	/**
	 * 读取关键蛋白质信息
	 * @param essentialProten
	 * @throws IOException 
	 */
	public void initialize(String essentialProten) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(new File(essentialProten)));
		
		String str = br.readLine();
		
		while(str != null){
			
			essentialMap.put(str.trim().toUpperCase(), 1);
			
			str = br.readLine();
			
		}
		
		br.close();
		
	}
	
	/**
	 * 判断输入的蛋白质是否是关键蛋白
	 * @param protein
	 * @return
	 */
	public boolean isItEssential(String protein){
		
		if( essentialMap.get(protein) != null){
			
			return true;
			
		}
		
		return false;
			
	}
	
	/**
	 * 初始化设置 所有的节点  判断是否为关键节点
	 * @param nodes
	 */
	public void setEssentialNode( List<NodeVo> nodes){
		
		NodeVo node = null;
		
		for ( int i = 0 ; i < nodes.size() ; i ++){
			
			node = nodes.get(i);
			
			node.setEssential(isItEssential(node.getName()));
			
		}
		
	}
	
	/**
	 * 计算每个cluster中关键蛋白质的个数
	 * @param clusters
	 */
	public  void  calculateEssentialNum(List<ClusterVo> clusters){
		
		int count = 0;
		
		for( int i = 0 ; i < clusters.size(); i++){
			
			ClusterVo cluster = clusters.get(i);
			
			count = 0;
			
			for( int j = 0 ; j < cluster.getProteins().size() ; j++){
				
				if( isItEssential(cluster.getProteins().get(j))){  //判断是否 关键蛋白
					
					count++;
					
				}
				
			}
			
			cluster.setEssential_count(count);
			
		}
		
	}
	

}
