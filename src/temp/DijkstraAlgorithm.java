package temp;

import java.util.List;
import java.util.Map;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-6-15 上午09:26:30 
 * 用于计算蛋白质网络图中每个节点的betweenness
 */

public class DijkstraAlgorithm {
	
	private int[] length;

	public int calculate( NodeVo node, List<NodeVo> nodes , Map<String, EdgeVo> edgemap){
		
		length = new int[nodes.size()];   //用于存储node节点到其他节点的的距离
		
		for(int i = 0; i < nodes.size(); i++){  //初始化
			
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
	 * 获取到NODE节点最短距离的节点索引
	 * @param length
	 * @param nodes
	 * @return
	 */
	public int  getMinmumNode(int[] length , List<NodeVo> nodes){
		
		int index = -1;
		
		int min = Integer.MAX_VALUE; //初始化最小值为最大值
		
		for(int i = 0 ; i < length.length ; i++ ){
			
			if(!nodes.get(i).getIsRemove()&&length[i]<min){  //如果节点没有被移除 并且到点i的路劲距离小于min
				
				min = length[i];          //切换最小值
				
				index = i;            //记录该节点
				
			}
			
		}
		
		return index;
		
	}
	
	
}
