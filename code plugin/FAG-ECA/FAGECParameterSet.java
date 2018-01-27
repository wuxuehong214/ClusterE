package com.wuxuehong.plugin;

import java.util.HashMap;

/**
 * the set of all the parameters used in clustering
 */
public class FAGECParameterSet implements ParameterSet{
    private static FAGECParameterSet ourInstance = new FAGECParameterSet();
    private static HashMap currentParams = new HashMap();
    private static HashMap resultParams = new HashMap();
    
    //parameters
    //algorithm
    //used in clustering using FAG-EC
    private boolean overlapped;
    private double fThreshold;
    private int cliqueSizeThreshold;
    private int complexSizeThreshold;
    private boolean isWeak;

    /**
     * Constructor for the parameter set object. 
     */
    public FAGECParameterSet() {
        //default parameters
        setDefaultParams();
    }
    /**
     * Constructor for non-default algorithm parameters.
     * Once an alalysis is conducted, new parameters must be saved so that they can be retrieved in the result panel
     * for exploration and export purposes.
     */
    
    public FAGECParameterSet(
            double fThreshold,
            int cliqueSizeThreshold,
            int complexSizeThreshold,
            boolean isWeak,
            boolean overlapped) {
        setAllAlgorithmParams(
                fThreshold,
                cliqueSizeThreshold,
                complexSizeThreshold,
                isWeak,
                overlapped
        );
    }
    /**
     * staic method to be used with getParamsCopy(String networkID) 
     * @return ourInstance the static instance of CurrentParameter
     */
    public static FAGECParameterSet getInstance() {
        return ourInstance;
    }
    /**
     * Get a copy of the current parameters for a particular network. 
     * usage:
     * Parameters.getInstance().getParamsCopy();  
     */
    public FAGECParameterSet getParamsCopy(String networkID) {
        if (networkID != null) {
            return ((FAGECParameterSet) currentParams.get(networkID)).copy();
        } else {
            FAGECParameterSet newParams = new FAGECParameterSet();
            return newParams.copy();
        }
    }
    public ParameterSet getResultParams(String resultSet) {
        return ((ParameterSet) resultParams.get(resultSet)).copy();
    }
    public static void removeResultParams(String resultSet) {
        resultParams.remove(resultSet);
    } 
    /**
     * Current parameters can only be updated using this method.
     */
    public void setParams(FAGECParameterSet newParams, String resultTitle, String networkID) {
        //cannot simply equate the params and newParams classes since that creates a permanent reference
        //and prevents us from keeping 2 sets of the class such that the saved version is not altered
        //until this method is called
        ParameterSet currentParamSet = new FAGECParameterSet(
                newParams.getFThreshold(),
                newParams.getCliqueSizeThreshold(),
                newParams.getComplexSizeThreshold(),
                newParams.isWeak(),
                newParams.isOverlapped()
        );
        //replace with new value
        currentParams.put(networkID, currentParamSet);
        ParameterSet resultParamSet = new FAGECParameterSet(
                newParams.getFThreshold(),
                newParams.getCliqueSizeThreshold(),
                newParams.getComplexSizeThreshold(),
                newParams.isWeak(),
                newParams.isOverlapped()
        );
        resultParams.put(resultTitle, resultParamSet);
    }
    /**
     * Method for setting all parameters to their default values
     */
    public void setDefaultParams() {
        setAllAlgorithmParams(1.0, 3, 3, true, false);
    }

    /**
     * Convenience method to set all the main algorithm parameters
     * 
     * @param scope Scope of the search (equal to one of the two fields NETWORK or SELECTION)
     * @param algorithm The algorithm user choosed to cluster the network
     * @param selectedNodes Node selection for selection-based scope
     * @param includeLoops include loops or not
     * @param degreeThrshold degree threshold
     * @param kCore the value of k in K-Core
     * @param optimize Determines if parameters are customized by user/default or optimized
     * @param maxDepthFromStart max depth from seed node
     * @param nodeScoreThreshold node score threshold
     * @param fluff fluff the resulting clusters or not
     * @param haircut haircut the clusters or not
     * @param nodeDensityThreshold nodedesity thrshold
     * @param cliqueSizeThreshold1
     * @param complexSizeThreshold1
     * @param fThreshold
     * @param cliqueSizeThreshold
     * @param complexSizeThreshold
     * @param isWeak
     * @param overlapped
     */
    public void setAllAlgorithmParams(
            double fThreshold,
            int cliqueSizeThreshold,
            int complexSizeThreshold,
            boolean isWeak,
            boolean overlapped) {

        this.fThreshold=fThreshold;
        this.cliqueSizeThreshold=cliqueSizeThreshold;
        this.complexSizeThreshold=complexSizeThreshold;
        this.isWeak=isWeak;
        this.overlapped=overlapped;
    }

    /**
     * Copies a parameter set object
     *
     * @return A copy of the parameter set
     */
    public FAGECParameterSet copy() {
    	FAGECParameterSet newParam = new FAGECParameterSet();
        newParam.setCliqueSizeThreshold(this.cliqueSizeThreshold);
        newParam.setComplexSizeThreshold(this.complexSizeThreshold);
        newParam.setFThreshold(this.fThreshold);
        newParam.setWeak(this.isWeak);
        newParam.setOverlapped(this.overlapped);
        return newParam;
    }

	public int getCliqueSizeThreshold() {
		return cliqueSizeThreshold;
	}

	public void setCliqueSizeThreshold(int cliqueSizeThreshold) {
		this.cliqueSizeThreshold = cliqueSizeThreshold;
	}

	public int getComplexSizeThreshold() {
		return complexSizeThreshold;
	}

	public void setComplexSizeThreshold(int complexSizeThreshold) {
		this.complexSizeThreshold = complexSizeThreshold;
	}

	public double getFThreshold() {
		return fThreshold;
	}

	public void setFThreshold(double fThreshold) {
		this.fThreshold = fThreshold;
	}

	public boolean isWeak() {
		return isWeak;
	}

	public void setWeak(boolean isWeak) {
		this.isWeak = isWeak;
	}

    public boolean isOverlapped() {
		return overlapped;
	}

	public void setOverlapped(boolean overlapped) {
		this.overlapped = overlapped;
	}
    /**
     * Generates a summary of the parameters. Only parameters that are necessary are included.
     * For example, if fluff is not turned on, the fluff density cutoff will not be included.
     * 
     * @return Buffered string summarizing the parameters
     */
	
    public String toString() {
        String lineSep = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
    	sb.append("   Algorithm:  FAG-EC"+lineSep);
        sb.append("   Clustering:" + lineSep
                + "      DefinitionWay: " + ((isWeak)? ("Weak  In/OutThreshold: "+fThreshold ):"Strong") + lineSep
                + "      Overlapped: " + overlapped + ((overlapped)? (" CliqueSizeThreshold: "+cliqueSizeThreshold ):"")+lineSep
                + "      OutputThreshold: " + complexSizeThreshold + lineSep);  
        return sb.toString();
    }
}
