package com.wuxuehong.plugin;

import java.util.Vector;

import com.wuxuehong.bean.Node;


/**
    This class represents a candidate for Cluster analysis. A candidate must have
    a name and two independent variables on the basis of which it is to be clustered.
    A Data Point must have two variables and a name. A Vector of  Data Point object
    is fed into the constructor of the JCA class. JCA and DataPoint are the only
    classes which may be available from other packages.
    @author Shyam Sivaraman
    @version 1.0
    @see JCA
    @see Cluster
*/

public class DataPoint extends Node{
    private int mX,mY;
    private String mObjName;
    private Cluster mCluster;
    private double mEuDt;
    private Vector<Node>[] nei;

    public DataPoint(int x, int y, String name,Vector<Node> nei) {
    	super(name);
    	//super.setNeighbours(nei);
    	this.mX = x;
    	this.mY = y;
    	this.mObjName = name;
        this.mCluster = null;
    }

    public void setCluster(Cluster cluster) {
        this.mCluster = cluster;
        calcEuclideanDistance();
    }

    public void calcEuclideanDistance() {

    //called when DP is added to a cluster or when a Centroid is recalculated.
        mEuDt = Math.sqrt(Math.pow((mX - mCluster.getCentroid().getCx()),
                                   2) + Math.pow((mY - mCluster.getCentroid().getCy()), 2));//Math.pow(double   a,   double   b) 是求a的b次方的
    }

    public double testEuclideanDistance(Centroid c) {
        return Math.sqrt(Math.pow((mX - c.getCx()), 2) + Math.pow((mY - c.getCy()), 2));
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public Cluster getCluster() {
        return mCluster;
    }

    public double getCurrentEuDt() {
        return mEuDt;
    }

    public String getObjName() {
        return mObjName;
    }

}
