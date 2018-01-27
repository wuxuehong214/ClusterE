package com.wuxuehong.plugin;

/**
 * 
 * @author cjry
 * the interface Algorithm
 */

public interface Algorithm {
	public void mergeComplex(Complex c1, Complex c2);
    public Complex[] clustering(Network network);
    public ParameterSet getParameterSet();
}
