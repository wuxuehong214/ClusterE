package com.wuxuehong.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

public class FAGECAlgorithm implements Algorithm {
	TreeMap curEdgeWeights = new TreeMap();
	Network curNetwork;
	FAGECParameterSet params;
	
	public FAGECAlgorithm(){
		//add code here
		params = new FAGECParameterSet();
	}
	

	/**
     * FAG-EC Algorithm Step 1: 
     * Calculate arc weights which is defined as 
     * ( sizOf(Ni Intersect Nj) +1 )/ min[(ki),(kj)]
     *
     * @param inputNetwork The network that will be calculated
     */    
    public void calEdgeWeight(Network inputNetwork){
        String callerID = "In Algorithm.calEdgeWeight";
        double weight;
        int degree1,degree2,min;
        ArrayList al;
    	TreeMap edgeWeightsMap=new TreeMap(new Comparator(){
    		//sort Doubles in descending order
    		public int compare(Object o1,Object o2){
    			double d1=((Double)o1).doubleValue();
    			double d2=((Double)o2).doubleValue();
    			if(d1==d2){
    				return 0;
    			}
    			else if(d1<d2){
    				return 1;
    			}
    			else return -1;
    		}
    	});  		
    	Iterator edges=inputNetwork.getAlArcs().iterator();
    	while(edges.hasNext()){	//for each edge, cal the weight
    		weight=0.0;
    		int common=0;
    		NetArc e=(NetArc)edges.next();
    		int nFrom=e.getFirstNode();
    		int nTo=e.getSecondNode();
    		
    		//cal the edge weght
    		int[] neighbors1=inputNetwork.neighborsArray(nFrom);
    		int[] neighbors2=inputNetwork.neighborsArray(nTo);
    		Arrays.sort(neighbors1);
    		for(int i=0;i<neighbors2.length;i++){
    			int key=neighbors2[i];
    			if(Arrays.binarySearch(neighbors1, key)>=0)//exist a common neighbor of both nodes
    				common++;
    		}
    		common++;
    		degree1=inputNetwork.getDegree(nFrom);
    		degree2=inputNetwork.getDegree(nTo);
    		min=degree1<degree2 ? degree1:degree2;
    		weight=(double)(common)/(double)min;
    		
    		//add to the edge weights map
    		if(edgeWeightsMap.containsKey(new Double(weight))) {
    			al=(ArrayList)edgeWeightsMap.get(new Double(weight));
    			al.add(new Integer(e.getRootGraphIndex()));
    		}else{
    			al=new ArrayList();
    			al.add(new Integer(e.getRootGraphIndex()));
    			edgeWeightsMap.put(new Double(weight), al);
    		}
    	}       	
    	curEdgeWeights=edgeWeightsMap;
    }
    
    public void mergeComplex(Complex c1, Complex c2){
    	int inDegree=c1.getInDegree();
    	int totalDegree=c1.getTotalDegree()+c2.getTotalDegree();
    	
    	ArrayList alNodes=c1.getALNodes();
    	Iterator i=c2.getALNodes().iterator();
    	while(i.hasNext()){
    		int nodeIndex=((Integer)i.next()).intValue();
    		int[] adjs=curNetwork.neighborsArray(nodeIndex);
    		for(int j=0;j<adjs.length;j++)
    			if(alNodes.contains(new Integer(adjs[j])))
    					inDegree++;
    		alNodes.add(new Integer(nodeIndex));
    		NetNode node=(NetNode)curNetwork.getAlNodes().get(nodeIndex);
    		node.setComplexID(c1.getComplexID());
    	}
    	c1.setInDegree(inDegree);
    	c1.setTotalDegree(totalDegree);
    	int outDegree=totalDegree-2*inDegree;
    	if(outDegree<0)
    		System.out.println("Error: outDegree<0! ");
    	float fModule = (float)inDegree/(float)(outDegree);
    	if( fModule>params.getFThreshold() )
    		c1.setModule(true);
    	c2.getALNodes().clear();
    }
	
	public Complex[] clustering(Network inputNetwork){
		//System.out.println("NodeNum:"+inputNetwork.getAlNodes().size());
		//System.out.println("ArcNum:"+inputNetwork.getAlArcs().size());
        String callerID = "Algorithm.FAG_ECFinder";
    	System.out.println("In "+callerID);
    	
    	curNetwork = inputNetwork;
        calEdgeWeight(curNetwork);  
        ArrayList rawComplexes = new ArrayList(curNetwork.getAlNodes().size());
        //Initialization, sort each single node into a clique
        int i=0;
        Iterator nodes = curNetwork.getAlNodes().iterator();
        while(nodes.hasNext()){
        	NetNode n=(NetNode)nodes.next();
        	n.setComplexID(i);
    		int degree=n.getDegree();
    		Complex newComplex = new Complex(i);
    		ArrayList alNodes=new ArrayList();
    		alNodes.add(new Integer(n.getRootGraphIndex()));
    		newComplex.setALNodes(alNodes);
    		newComplex.setTotalDegree(degree);
    		rawComplexes.add(newComplex);
    		i++;
        }
        /**********************************************************************************************
			Then, Operation UNION:	according to different situation, in which the two nodes consisting 
				this arc may belong to different Complexes or an identical Complex and that the 
				attributes of the Complexes varies, we take on different action 
         ***********************************************************************************************/
        ArrayList alEdgeWithSameWeight;  
        NetArc curEdge;
        Collection values = curEdgeWeights.values(); //returns a Collection sorted by key order (descending)                                                                                                                   
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            //each weight may be associated with multiple edges, iterate over these lists
        	alEdgeWithSameWeight = (ArrayList) iterator.next();
            for (int j = 0; j < alEdgeWithSameWeight.size(); j++) {//for each edge
                int edgeIndex = ((Integer) alEdgeWithSameWeight.get(j)).intValue();
                curEdge=(NetArc)curNetwork.getAlArcs().get(edgeIndex);                
        		int inFrom = curEdge.getFirstNode();
        		int inTo   = curEdge.getSecondNode();
        		int icFrom=((NetNode)curNetwork.getAlNodes().get(inFrom)).getComplexID();
        		int icTo=((NetNode)curNetwork.getAlNodes().get(inTo)).getComplexID();
        		if(icFrom != icTo)    //we have take some actions only if the two complexes are not the same
        		{
        			Complex cFrom=(Complex)rawComplexes.get(icFrom);
        			Complex cTo=(Complex)rawComplexes.get(icTo);
        			if(cFrom.isMergeable() && cTo.isMergeable())	//the two complexes are both mergeable
        				if(!cFrom.isModule() || !cTo.isModule())	//either of the two complexes are not modules yet
        					if(cFrom.getALNodes().size() >= cTo.getALNodes().size()){//merge the smaller complexe to the larger one
        						mergeComplex(cFrom, cTo);
        					}
        					else{	//merge the smaller complex to the larger one
        						mergeComplex(cTo, cFrom);
        					}
        				else	//both of the two complexes are modules
        				{
        					cFrom.setMergeable(false);
        					cTo.setMergeable(false);
        				}
        			else	//either of the two complexes is not mergeable
        			{
        				cFrom.setMergeable(false);
    					cTo.setMergeable(false);
        			}
        		}
            }
        }
        ArrayList alComplexes = new ArrayList();
        Iterator it=rawComplexes.iterator();
        while(it.hasNext()){
        	Complex complex=(Complex)it.next();
        	if(complex.getALNodes().size()>=params.getComplexSizeThreshold()){
        		ArrayList alNodes=complex.getALNodes();
        		int ind=complex.getInDegree();
        		int outd=complex.getTotalDegree()-2*ind;
        		if(ind!=0 && outd!=0)
        			complex.setModularity((double)ind/(double)outd);
        		else
        			complex.calModularity(curNetwork);
        		alComplexes.add(complex);
        	}
        }
        //Finally convert the arraylist into a fixed array
        Complex[] complexes = new Complex[alComplexes.size()];
        for (int c = 0; c < complexes.length; c++) {
        	complexes[c] = (Complex) alComplexes.get(c);
        }
        System.out.println("Complex length:"+complexes.length);
		for(i=0;i<complexes.length;i++){
			System.out.println("Complex "+i+" ");
			Complex com=complexes[i];
			ArrayList alNodes=com.getALNodes();
			for(int j=0;j<alNodes.size();j++){
				int index=((Integer)alNodes.get(j)).intValue();
				String id=((NetNode)inputNetwork.getAlNodes().get(index)).getIdentifier();
				System.out.print(id+" ");
			}
			System.out.println("");
		}
		return complexes;
		
	}
	public FAGECParameterSet getParameterSet() {
		return params;
	}
	public void setParamSet(FAGECParameterSet params) {
		this.params = params;
	}
}
