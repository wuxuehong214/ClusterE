package com.wuxuehong.bean;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * ����ͼ����ɫ �㷨��--��ɫ
 * 
 * @author Administrator
 * 
 */
public class AlgorithmColor {
	private String str;
	private RGB color;

	public AlgorithmColor(String str,RGB color){
		this.str = str;
		this.color = color;
	}
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}
}
