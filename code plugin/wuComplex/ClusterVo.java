package com.wuxuehong.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 *
 * @author: wuxuehong
 * @E-mail: wuxuehong214@163.com 
 * @date£º2011-4-3 ÉÏÎç09:49:08 
 * 
 */

public class ClusterVo {
	
	private Set<String> proteins;
	
	public ClusterVo(){
		proteins = new HashSet<String>();
	}
	
	public Set<String> getProteins() {
		return proteins;
	}

	public void setProteins(Set<String> proteins) {
		this.proteins = proteins;
	}
	
	

}
