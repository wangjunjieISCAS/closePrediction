package com.processPrediction;

import com.prediction.MthCRCAlgorithm;

public class TotalBugPredictionMth extends TotalBugPrediction{
	public TotalBugPredictionMth ( ) {
		this.algorithm = new MthCRCAlgorithm();
	}
	
	public static void main ( String[] args ) {
		TotalBugPredictionMth prediction = new TotalBugPredictionMth();
		
		String folderName = "data/input/test";
		prediction.predictTotalBugForProjects(folderName, 8, "data/output/performance");
	}
}
