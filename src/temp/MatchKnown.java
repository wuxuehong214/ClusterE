package temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class MatchKnown {
	
	
	//已知蛋白质信息complex2008.txt   complex2008_(2).txt
	private List<ClusterVo> knowns = new LinkedList<ClusterVo>();
	
	/**
	 * 读取已知蛋白质复合物信息
	 * @param filename
	 * @throws IOException 
	 */
	public void initialize(String filename) throws IOException{
		readKnwnComplex(filename,knowns);
	}
	
	/**
	 * 计算clusters中 每个CluserVo 与 已知复合物中匹配的最大值
	 * @param clusters
	 */
	public void calculateMatch(List<ClusterVo> clusters){
		
		for(int i = 0 ; i < clusters.size(); i++){
			
			ClusterVo cluster = clusters.get(i);
			
			float max = -1;
			
			for(int j = 0; j < knowns.size() ;j++){
				
				ClusterVo know = knowns.get(j);
				
				float[] result = getOS(cluster, know);
				
				if(result[0] > max){
					
					max = result[0];
					
				}
				
			}
			
			cluster.setMatchValue(max);
			
		}
	}
	
	/**
	 * 判断c1是不是被c2包含
	 * @param c1
	 * @param c2
	 * @return
	 */
	boolean isInit(ClusterVo c1,ClusterVo c2){
		for(int i=0;i<c1.getProteins().size();i++){
			if(!c2.getProteins().contains(c1.getProteins().get(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * 读取已知蛋白质信息
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public void readKnwnComplex(String filename,List<ClusterVo> knowns) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String str = br.readLine();
		ClusterVo c = null;
		Scanner s = null;
		while(str!=null){
			if(str.startsWith("Complex")||str.startsWith("complex")){
				c  = new ClusterVo();
				s = new Scanner(str);
				s.next();
				knowns.add(c);
			}else
			c.getProteins().add(str);
			str = br.readLine();
		}
	}
	
	/**
	 * 
	 * @param filename      保存文件路径
	 * @param knowns       已知蛋白质复合物信息
	 * @param complexes    预测蛋白质复合物信息
	 * @throws IOException
	 */
	public void getResults(String filename,List<ClusterVo> knowns,List<ClusterVo> complexes) throws IOException{
		

		int[] kdata = new int[10];
		int[] pdata = new int[10];
		double[] sn  = new double[10];
		double[] sp = new double[10];
		double[] f = new double[10];
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
		bw.write("PComplex\tPSize\tKComplex\tKSize\tOverlap\tOS");
		bw.newLine();
		int overlap=0;
		float max = 0;
		
		for(int i=0;i<complexes.size();i++){
			max = 0.01f;
			ClusterVo c1 = complexes.get(i);
			for(int j=0;j<knowns.size();j++){
				ClusterVo c2 = knowns.get(j);
				float[] result = getOS(c1, c2);
				if(result[0]>=0.1){
					overlap = (int)result[1];
					bw.write("Complex "+c1.getIndex()+"\t"+c1.getProteins().size()+"\tComplex "+c2.getIndex()+"\t"+c2.getProteins().size()+"\t"+overlap+"\t"+result[0]);
					bw.newLine();
					if(result[0]>max)max=result[0];
				}
			}
			if(max>=1){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;pdata[5]++;pdata[6]++;pdata[7]++;pdata[8]++;pdata[9]++;
			}else if(max>=0.9){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;pdata[5]++;pdata[6]++;pdata[7]++;pdata[8]++;
			}else if(max>=0.8){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;pdata[5]++;pdata[6]++;pdata[7]++;
			}else if(max>=0.7){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;pdata[5]++;pdata[6]++;
			}else if(max>=0.6){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;pdata[5]++;
			}else if(max>=0.5){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;pdata[4]++;
			}else if(max>=0.4){
				pdata[0]++;pdata[1]++;pdata[2]++;pdata[3]++;
			}else if(max>=0.3){
				pdata[0]++;pdata[1]++;pdata[2]++;
			}else if(max>=0.2){
				pdata[0]++;pdata[1]++;
			}else if(max>=0.1){
				pdata[0]++;
			}
		}
		
		for(int i=0;i<knowns.size();i++){
			max = 0.01f;
			ClusterVo c1 = knowns.get(i);
			for(int j=0;j<complexes.size();j++){
				ClusterVo c2 = complexes.get(j);
				float[] result = getOS(c1, c2);
				if(result[0]>max)
					max = result[0];
			}
			if(max>=1){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;kdata[5]++;kdata[6]++;kdata[7]++;kdata[8]++;kdata[9]++;
			}else if(max>=0.9){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;kdata[5]++;kdata[6]++;kdata[7]++;kdata[8]++;
			}else if(max>=0.8){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;kdata[5]++;kdata[6]++;kdata[7]++;
			}else if(max>=0.7){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;kdata[5]++;kdata[6]++;
			}else if(max>=0.6){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;kdata[5]++;
			}else if(max>=0.5){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;kdata[4]++;
			}else if(max>=0.4){
				kdata[0]++;kdata[1]++;kdata[2]++;kdata[3]++;
			}else if(max>=0.3){
				kdata[0]++;kdata[1]++;kdata[2]++;
			}else if(max>=0.2){
				kdata[0]++;kdata[1]++;
			}else if(max>=0.1){
				kdata[0]++;
			}
		}
		
		for(int i=0;i<10;i++){
			sn[i] = (double)pdata[i]/((double)pdata[i]+(double)knowns.size()-kdata[i]);
			sp[i] = (double)pdata[i]/(double)complexes.size();
			f[i] = 2*sn[i]*sp[i]/(sn[i]+sp[i]);
		}
		
		bw.newLine();
		bw.newLine();
		
		bw.write("OS\tPc\tKc\tMPc\tMKc\tSn\t\t\tSp\t\t\tF");
		bw.newLine();
		for(int i=0;i<10;i++){
			if(i!=9)
			bw.write("0."+(i+1)+"\t"+complexes.size()+"\t"+knowns.size()+"\t"+pdata[i]+"\t"+kdata[i]+"\t"+sn[i]+"\t"+sp[i]+"\t"+f[i]);
			else
				bw.write("1"+"\t"+complexes.size()+"\t"+knowns.size()+"\t"+pdata[i]+"\t"+kdata[i]+"\t"+sn[i]+"\t"+sp[i]+"\t"+f[i]);
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
	}
	
	/**
	 * 计算两个Complex之间的OS
	 * @param c1
	 * @param c2
	 * @return
	 */
	public float[] getOS(ClusterVo c1,ClusterVo c2){
		int a = c1.getProteins().size();
		int b = c2.getProteins().size();
		int c = 0;
		for(int i=0;i<c1.getProteins().size();i++){
			if(c2.getProteins().contains(c1.getProteins().get(i)))
					c++;
		}
		float[] result = new float[2];
		result[0] = (float)(c*c)/(float)(a*b);
		result[1] = c;
		return result;
		
	}
	
	
	public static void main(String args[]) throws IOException{
		new MatchKnown();
	}

}
