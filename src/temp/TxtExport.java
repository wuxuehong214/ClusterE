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
 * @date��2011-6-16 ����04:31:22 
 * ��txt�ļ���ʽ�����ȶ���һЩ���
 * 
 */

public class TxtExport {
	
	/**
	 * ���Ԥ����ĸ�����
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
	 * ���ÿ��complex�бߵļ���
	 * ��positiveΪtrueʱ������� �Ѿ����ڵı�    ��positiveΪfalseʱ������п��ܴ��ӵı�  �����Ա�
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
					
					if( edge != null && positive){        //���cluster�����е�interactions
						
						bw.write(edge.getN1()+"\t"+edge.getN2());
						
						bw.newLine();
						
					}
					
					if( edge == null && !positive){       //���cluster�п��ܴ��ڵ�Interactions
						
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
	 * һ��ͼ����nodeCount���ڵ� ����������еıߵ�������
	 * @param nodeCount
	 * @return
	 */
	public int getPossibleNum(int nodeCount){
		
		return nodeCount*(nodeCount-1)/2;
		
	}
	

}
