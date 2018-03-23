package com.processPrediction;

import com.prediction.MhJKCRCAlgorithm;

public class TotalBugPredictionMhJK extends TotalBugPrediction {
	public TotalBugPredictionMhJK ( ) {
		this.algorithm = new MhJKCRCAlgorithm();
	}
	
	public static void main ( String[] args ) {
		TotalBugPredictionMhJK prediction = new TotalBugPredictionMhJK();
		
		String folderName = "data/input/projects";
		prediction.predictTotalBugForProjects(folderName, 7, "data/output/performance/MhJK");
	} 
}
