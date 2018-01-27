package ipcipg_o;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;

import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

/**
 * IPCIPG_o
 * @author wuxuehong
 * 2013-4-18
 */
public class Ipcipg_o extends Plugin implements NewAlgorithm {

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return "IPCIPG_o";
	}

	@Override
	public Vector<Node>[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		if(para == null || para.length != 5) return null;
		try {
			Overlap main = new Overlap(para);
			List<ClusterVo> clusters = main.getClusters();
			Vector[] v = new Vector[clusters.size()];
			for(int i=0;i<v.length;i++){
				v[i] = new Vector();
				ClusterVo c = clusters.get(i);
				for(int j=0;j<c.getProteins().size();j++){
					v[i].add(new Node(c.getProteins().get(j)));
				}
			}
			return v;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return new String[]{"Density","D","N","minSize","gene expression data"}; 
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return new String[]{"0.7","15","0.5","2","gene.txt"};
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Algorithm IPCIPG is a local search method based on seed-extension model. IPCIPG-o used for generating overlapping clusters!";
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return NewAlgorithm.Algorithm;
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub

	}

	@Override
	public void variableInit() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub

	}

}
