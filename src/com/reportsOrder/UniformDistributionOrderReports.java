package com.reportsOrder;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import com.data.TestReport;

public class UniformDistributionOrderReports {
	/*
	 * 基于均匀分布进行排序；这里的实现有点复杂，其实就是直接将所有的报告分布到所有的桶里面
	 */
	public TreeMap<Integer, ArrayList<TestReport>> orderReportsUniformly ( TreeMap<Integer, ArrayList<TestReport>> captureHistory  ) {
		TreeMap<String, ArrayList<TestReport>> dupTagReportsList = new TreeMap<String, ArrayList<TestReport>>();
		for ( Integer cap : captureHistory.keySet() ) {
			ArrayList<TestReport> reportList = captureHistory.get( cap );
			for ( int i=0; i < reportList.size(); i++ ) {
				TestReport report = reportList.get( i );
				String dupTag = report.getDupTag();
				
				ArrayList<TestReport> dupTagReports = new ArrayList<TestReport>();
				if ( dupTagReportsList.containsKey( dupTag )) {
					dupTagReports = dupTagReportsList.get( dupTag );
				}
				dupTagReports.add( report );
				dupTagReportsList.put( dupTag, dupTagReports );
			}
		}
		
		TreeMap<Integer, ArrayList<TestReport>> newCaptureHistory = new TreeMap<Integer, ArrayList<TestReport>>();
		ArrayList<Integer> capIndexList = new ArrayList<Integer>();
		for ( Integer cap : captureHistory.keySet() ) {
			ArrayList<TestReport> reports = new ArrayList<TestReport>();
			newCaptureHistory.put( cap, reports);
			
			capIndexList.add( cap );
		}
		
		Random rand = new Random();
		for ( String dupTag : dupTagReportsList.keySet() ) {
			ArrayList<TestReport> dupTagReports = dupTagReportsList.get( dupTag );
			for ( int i = 0; i < dupTagReports.size(); i++ ) {
				int index = rand.nextInt( newCaptureHistory.size() );
				
				Integer newCap = capIndexList.get( index );    //this report should be put in this newCap key
				ArrayList<TestReport> reports = newCaptureHistory.get( newCap );
				reports.add( dupTagReports.get( i ) );
				newCaptureHistory.put( newCap, reports );
			}			
		}
		return newCaptureHistory;
	}
}
