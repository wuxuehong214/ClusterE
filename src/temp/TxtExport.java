package temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-6-16 下午04:31:22 
 * 以txt文件格式导出既定的一些结果
 * 
 */

public class TxtExport {
	
	/**
	 * 输出预测出的复合物
	 * @throws IOException 
	 */
	public void exportComplexes( List<ClusterVo> clusters, String filename) throws IOException{
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		
		for( int i = 0 ; i < clusters.size() ; i ++){
			
			ClusterVo cluster = clusters.get(i);
			
			bw.write("Complex\t"+(i+1)+"\t"+cluster.getProteins().size());
			
			bw.newLine();
			
			for( int j = 0 ; j < cluster.getProteins().size() ; j ++){
				
				bw.write(cluster.getProteins().get(j));
				
				bw.newLine();
				
			}
			
		}
		
		bw.flush();
		
		bw.close();
		
	}
	
	/**
	 * 输出每个complex中边的集合
	 * 当positive为true时输出其中 已经存在的边    当positive为false时输出其中可能村子的边  假阳性边
	 * @param clusters
	 * @param filename
	 * @param pn
	 * @param positive
	 * @throws IOException 
	 */
	public void exportInteractions(List<ClusterVo> clusters, String filename, ProteinNetwork pn , boolean positive) throws IOException{
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		
		String p1, p2;
		
		for( int i = 0 ; i < clusters.size() ; i ++){
			
			ClusterVo cluster = clusters.get(i);
			
			int size = cluster.getProteins().size();
			
			if(positive)
			
				bw.write("Complex\t"+(i+1)+"\t"+cluster.getEdgeNum());
			
			else
				
				bw.write("Complex\t"+(i+1)+"\t"+(getPossibleNum(size)-cluster.getEdgeNum()));
			
			bw.newLine();
			
			for ( int m = 0 ; m < size ; m ++){
				
				p1 = cluster.getProteins().get(m);
				
				for ( int n = m+1 ; n < size ; n ++){
					
					p2 = cluster.getProteins().get(n);
					
					EdgeVo edge = pn.getEdge(p1, p2);
					
					if( edge != null && positive){        //输出cluster中已有的interactions
						
						bw.write(edge.getN1()+"\t"+edge.getN2());
						
						bw.newLine();
						
					}
					
					if( edge == null && !positive){       //输出cluster中可能存在的Interactions
						
						bw.write(p1+"\t"+p2);
						
						bw.newLine();
						
					}
					
				}
				
			}
			
		}
		
		bw.flush();
		
		bw.close();
		
	}
	
	/**
	 * 一个图中有nodeCount个节点 计算其可能有的边的总条数
	 * @param nodeCount
	 * @return
	 */
	public int getPossibleNum(int nodeCount){
		
		return nodeCount*(nodeCount-1)/2;
		
	}
	

}
