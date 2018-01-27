package temp;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date：2011-6-11 上午08:45:13 
 * 
 */

public class ExcelExport {
	
	
	/**
	 * 统计ECC PCC
	 * @param filename
	 * @param pn
	 * @throws IOException
	 * @throws WriteException
	 */
	public static void exportExcel(String filename,ProteinNetwork pn) throws IOException, WriteException{
		// 打开文件
		WritableWorkbook book = Workbook.createWorkbook(new File(filename));
		// 生成名为“第一页”的工作表，参数0表示这是第一页
		WritableSheet sheet = book.createSheet("实验结果", 0);
		// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
		// 以及单元格内容为test
		WritableFont font1 = new WritableFont(WritableFont.TIMES, 16,
				WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);
		int hang = 0;
		for (int i = 0; i < 2; i++)
			sheet.setColumnView(i, 15);
		sheet.setRowView(hang, 400);
		sheet.addCell(new Label(0, hang, "ECC", format1));
		sheet.addCell(new Label(1, hang, "PCC", format1));
		
		Iterator<String> it = pn.strToEdge.keySet().iterator();
		
		while(it.hasNext()){
			
			hang++;//行数递增
			
			EdgeVo  edge = pn.strToEdge.get(it.next());
			
			sheet.addCell(new jxl.write.Number(0, hang, edge.getEcc()));
			
			sheet.addCell(new jxl.write.Number(1, hang, edge.getPcc()));
			
		}
		
		book.write();
		
		book.close();
	}
	
	/**
	 * 到处所有cluster详细信息
	 * @throws IOException 
	 * @throws WriteException 
	 */
	public static void exportClusters(List<NodeVo> seeds, Map<String,ClusterVo> seedToCluster, String filename) throws IOException, WriteException{
		// 打开文件
		WritableWorkbook book = Workbook.createWorkbook(new File(filename));
		// 生成名为“第一页”的工作表，参数0表示这是第一页
		WritableSheet sheet = book.createSheet("实验结果", 0);
		// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
		// 以及单元格内容为test
		WritableFont font1 = new WritableFont(WritableFont.TIMES, 15,
				WritableFont.BOLD);
		WritableCellFormat format1 = new WritableCellFormat(font1);
		int hang = 0;
		for (int i = 0; i < 9; i++)
			sheet.setColumnView(i, 20);
		sheet.setRowView(hang, 400);
		sheet.addCell(new Label(0, hang, "Seed", format1));
		sheet.addCell(new Label(1, hang, "Seed weight", format1));
		sheet.addCell(new Label(2, hang, "Essential", format1));
		sheet.addCell(new Label(3, hang, "Complex Size", format1));
		sheet.addCell(new Label(4, hang, "Match Value", format1));
		sheet.addCell(new Label(5, hang, "P-value", format1));
		sheet.addCell(new Label(6, hang, "Essential Num", format1));
		sheet.addCell(new Label(7, hang, "Interactions", format1));
		sheet.addCell(new Label(8, hang, "Density", format1));
		sheet.addCell(new Label(9, hang, "Ecc", format1));
		sheet.addCell(new Label(10, hang, "Degree", format1));
		
		for(int i = 0 ; i < seeds.size() ; i++){
			
			NodeVo seed = seeds.get(i);  //get the seed node
			
			ClusterVo cluster = seedToCluster.get(seed.getName());
			
			if(cluster == null) continue;
			
			hang++;//行数递增
			//种子节点名称
			sheet.addCell(new Label(0, hang, seed.getName()));
			//种子节点权值
			sheet.addCell(new jxl.write.Number(1, hang, seed.getWeight()));
			//是否关键蛋白
			sheet.addCell(new Label(2, hang, seed.isEssential()?"yes":"no"));
			//复合物规模
			sheet.addCell(new jxl.write.Number(3, hang, cluster.getProteins().size()));
			//与已知复合物匹配值
			sheet.addCell(new jxl.write.Number(4, hang, cluster.getMatchValue()));
			//Pvalue值
			sheet.addCell(new jxl.write.Number(5, hang, cluster.getPvalue()));
			//关键蛋白质个数
			sheet.addCell(new jxl.write.Number(6, hang, ((float)cluster.getEssential_count()/(float)cluster.getProteins().size())));
			//相互作用个数
			sheet.addCell(new jxl.write.Number(7, hang, cluster.getEdgeNum()));
			//复合物密度
			sheet.addCell(new jxl.write.Number(8, hang, cluster.getDensity()));
			//聚集系数
			sheet.addCell(new jxl.write.Number(9, hang, seed.getEcc()));
			//degree
			sheet.addCell(new jxl.write.Number(10, hang, seed.getNeighbours().size()));
		}
		
		book.write();
		
		book.close();
	}

}
