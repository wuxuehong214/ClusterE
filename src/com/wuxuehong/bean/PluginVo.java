package com.wuxuehong.bean;

import org.java.plugin.registry.PluginDescriptor;

import com.wuxuehong.interfaces.NewAlgorithm;

public class PluginVo {
	
	private PluginDescriptor p = null;
	
	private NewAlgorithm section = null;
	
	public PluginDescriptor getP() {
		return p;
	}

	public void setP(PluginDescriptor p) {
		this.p = p;
	}

	public NewAlgorithm getSection() {
		return section;
	}

	public void setSection(NewAlgorithm section) {
		this.section = section;
	}

	public PluginVo(PluginDescriptor p,NewAlgorithm section){
		this.p = p;
		this.section = section;
	}

	public String getPluginName(){
		String str = p.getLocation().toString();
		int a = str.indexOf('!');
		int b = str.lastIndexOf("plugins/");
		String s = str.substring(b+8, a);
		return s;
	}
	
	
}
