package com.prediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;
import com.performance.PerformanceEvaluation;

public class ClosePredictionTrend extends ClosePrediction {
	/*
	 * 基于N个连续不变判断结束
	 */
	@Override
	public Double[] predictCloseTime(TestProject project, Integer[] thresList) {
		// TODO Auto-generated method stub
		Integer stableNumThres = thresList[0];
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		
		PerformanceEvaluation evaluation = new PerformanceEvaluation( "data/input/bugCurveStatistics.csv");
		
		int stableNum = 0;
		HashSet<String> noDupTagList = new HashSet<String>();
		int endReport = reportList.size();
		for ( int i =0; i < reportList.size(); i++ ) {
			TestReport report = reportList.get(i);
			String tag = report.getBugTag();
			String dupTag = report.getDupTag();
			if ( tag.equals( "审核通过") ) {
				if ( noDupTagList.contains( dupTag )) {
					stableNum++;
				}else {
					noDupTagList.add( dupTag );
					stableNum = 0;
				}
			}else {
				stableNum++;
			}
			
			if ( stableNum >= stableNumThres && noDupTagList.size() > 0 ) {
				endReport = i;
				break;
			}
		}
		int bugNum = noDupTagList.size();
		
		Integer[] results = { bugNum, endReport };
		Double[] performance = evaluation.evaluatePerformance ( results, project.getProjectName() );
		return performance;
	}


	@Override
	public void predictCloseTimeForProjects(String folderName, String performanceFile, Integer[] thresList) {
		// TODO Auto-generated method stub
		super.predictCloseTimeForProjects(folderName, performanceFile, thresList);
	}
	
	public static void main ( String args[] ) {
		ClosePredictionTrend prediction = new ClosePredictionTrend();
		
		int beginIndex = 13, endIndex = 42;
		for ( int i = beginIndex; i <= endIndex; i++ ) {
			Integer[] thresList = {  i };
			String tag = thresList[0].toString() + "-1";
			prediction.predictCloseTimeForProjects( "data/input/projects", "data/output/performanceTrend/performanceTrend-" + tag + ".csv", thresList );
		}
		
		/*
		int beginIndex = 13, endIndex = 42;
		Integer[] candPara = new Integer[endIndex-beginIndex+1];
		for ( int i = beginIndex; i <= endIndex; i++ ) {
			candPara[i-beginIndex] = i;
		}
		prediction.crossValidationForProjects( "data/input/projects", candPara, "data/output/crossPerformanceTrend", 100 );
		*/
	}
}
