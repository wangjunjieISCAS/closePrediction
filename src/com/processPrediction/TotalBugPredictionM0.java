package com.processPrediction;

public class TotalBugPredictionM0 extends TotalBugPrediction {	
	public TotalBugPredictionM0 ( ) {
		this.algorithm = new M0CRCAlgorithm();
	}
	
	public static void main ( String[] args ) {
		TotalBugPredictionM0 prediction = new TotalBugPredictionM0();
		
		String folderName = "data/input/projects";
		prediction.predictTotalBugForProjects(folderName, 7, "data/output/performance/M0");
		
	}
}
