package hf_measure;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.java.plugin.Plugin;
import com.wuxuehong.bean.Node;
import com.wuxuehong.interfaces.NewAlgorithm;

public class Hfmeasure extends Plugin implements NewAlgorithm {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Two type of measurements to evaluate identified clusters";
	}

	@Override
	public int getStyle() {
		// TODO Auto-generated method stub
		return Evaluation;
	}
	
	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
	}
	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
	}


	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Node>[] getClusters(String[] para) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getParaValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEvaluateNames() {
		// TODO Auto-generated method stub
		return "hF-measure";
	}
	
	private MainComposite mc;

	@Override
	public void drawCharts(String[] algorithm,
			HashMap<String, Vector<Node>[]> resultList, Composite composite,
			HashMap<String, RGB> colorlist) {
		// TODO Auto-generated method stub
		composite.setLayout(new FillLayout());
		if(mc == null || mc.isDisposed())
			mc = new MainComposite(composite, SWT.NONE);
		composite.layout();
		if(algorithm.length != 0){
			mc.setData(algorithm, resultList, colorlist);
		}
	}

	@Override
	public void variableInit() {
		// TODO Auto-generated method stub
		
	}

}
