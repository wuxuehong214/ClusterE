package com.wuxuehong.plugin;

public class LinePoint {
	
	private int known_size;
	
	private int predit_size;
	
	
	public LinePoint(int a,int b){
		this.predit_size =a;
		this.known_size = b;
	}
	
	public LinePoint(){
		
	}

	public int getKnown_size() {
		return known_size;
	}

	public void setKnown_size(int known_size) {
		this.known_size = known_size;
	}

	public int getPredit_size() {
		return predit_size;
	}

	public void setPredit_size(int predit_size) {
		this.predit_size = predit_size;
	}
	
	

}
