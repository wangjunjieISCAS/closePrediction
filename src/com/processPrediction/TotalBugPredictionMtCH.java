package com.processPrediction;

import com.prediction.MtCHCRCAlgorithm;

public class TotalBugPredictionMtCH extends TotalBugPrediction {
	public TotalBugPredictionMtCH ( ) {
		this.algorithm = new MtCHCRCAlgorithm();
	}
}
