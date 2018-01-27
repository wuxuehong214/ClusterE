package proteinCompelex_20110416;

import java.util.ArrayList;
import java.util.List;

public class ClusterVo implements Comparable<ClusterVo>{
	
	private List<String> proteins;
	
	public ClusterVo(){
		proteins = new ArrayList<String>();
	}

	public List<String> getProteins() {
		return proteins;
	}

	public void setProteins(List<String> proteins) {
		this.proteins = proteins;
	}

	@Override
	public int compareTo(ClusterVo arg0) {
		// TODO Auto-generated method stub
		return arg0.getProteins().size()-proteins.size();
	}
	
	

}
