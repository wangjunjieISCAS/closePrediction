package com.processPrediction;

import com.prediction.MhCHCRCAlgorithm;

public class TotalBugPredictionMhCH extends TotalBugPrediction {
	
	public TotalBugPredictionMhCH ( ) {
		this.algorithm = new MhCHCRCAlgorithm ();
	}	
	
	public static void main ( String[] args ) {
		TotalBugPredictionMhCH prediction = new TotalBugPredictionMhCH();
		String folderName = "data/input/test";
		prediction.predictTotalBugForProjects(folderName, 8, "data/output/performance");
	}
}
