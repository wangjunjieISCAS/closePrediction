package com.reportsOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import com.data.TestReport;

public class RandomOrderReports {
	public TreeMap<Integer, ArrayList<TestReport>> orderReportsRandomly ( TreeMap<Integer, ArrayList<TestReport>> captureHistory ) {
		TreeMap<Integer, Integer> captureSizeList = new TreeMap<Integer, Integer>();     //let the reordered reports have the same size with the original captureHistory
		ArrayList<TestReport> reportsList = new ArrayList<TestReport>();
		
		for ( Integer cap : captureHistory.keySet() ) {
			ArrayList<TestReport> reports = captureHistory.get( cap );
			captureSizeList.put( cap, reports.size() );
			
			reportsList.addAll( reports );
		}
		
		Collections.shuffle( reportsList );
		
		int index = 0;
		TreeMap<Integer, ArrayList<TestReport>> newCaptureHistory = new TreeMap<Integer, ArrayList<TestReport>>();
		for ( Integer cap : captureSizeList.keySet() ) {
			Integer capSize = captureSizeList.get( cap );
			
			ArrayList<TestReport> reports = new ArrayList<TestReport>();
			for ( int i = index ; i < reportsList.size() && i < index+capSize; i++ ) {
				reports.add( reportsList.get( i ));
			}
			index = index+ capSize;
			newCaptureHistory.put( cap, reports);
		}
		return newCaptureHistory;
	}
}
