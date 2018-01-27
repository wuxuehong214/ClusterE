package temp;

import java.util.List;
import java.util.Map;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date��2011-6-15 ����09:26:30 
 * ���ڼ��㵰��������ͼ��ÿ���ڵ��betweenness
 */

public class DijkstraAlgorithm {
	
	private int[] length;

	public int calculate( NodeVo node, List<NodeVo> nodes , Map<String, EdgeVo> edgemap){
		
		length = new int[nodes.size()];   //���ڴ洢node�ڵ㵽�����ڵ�ĵľ���
		
		for(int i = 0; i < nodes.size(); i++){  //��ʼ��
			
			if(edgemap.get(nodes.get(i).getName()+node.getName())!=null){
				
					length[i] = 1; 
				
			}else {
				
					length[i] = Integer.MAX_VALUE;
				
			}
			
		}
		
		node.setIsRemove(true);
		
		int betweenness = 0;
		
		int looptime = nodes.size()-1;
		
		for( int i = 0; i < looptime; i++){
			
			NodeVo n = nodes.get(i);
			
			
		}
		
		return betweenness;
		
	}
	
	/**
	 * ��ȡ��NODE�ڵ���̾���Ľڵ�����
	 * @param length
	 * @param nodes
	 * @return
	 */
	public int  getMinmumNode(int[] length , List<NodeVo> nodes){
		
		int index = -1;
		
		int min = Integer.MAX_VALUE; //��ʼ����СֵΪ���ֵ
		
		for(int i = 0 ; i < length.length ; i++ ){
			
			if(!nodes.get(i).getIsRemove()&&length[i]<min){  //����ڵ�û�б��Ƴ� ���ҵ���i��·������С��min
				
				min = length[i];          //�л���Сֵ
				
				index = i;            //��¼�ýڵ�
				
			}
			
		}
		
		return index;
		
	}
	
	
}
