package com.processPerformance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;

public class PerformanceEvaluation {
	int beginReportIndex = 30;    //从第 beginReportIndex 个report
	int reportNumThres = 40;
	int whenTrueBugThres = 1;    //预测的缺陷数目和真实的缺陷数目一致，并且在whenTrueBugThres个capture中保持不变
	
	public Double obtainAverageDifference ( TreeMap<Integer, Integer[]> predictionResult ) {    //estPopsize, actualBugNum, totalBugNum
		int totalDif = 0, count = 0;
		for ( Integer endReport : predictionResult.keySet() ) {
			if ( endReport < beginReportIndex )
				continue;
			Integer[] result = predictionResult.get( endReport );
			if ( result[1] >= result[2])
				break;
			
			int dif = Math.abs( result[0] - result[2] );
			totalDif += dif;
			count++;
		}
		Double aveDif = (1.0*totalDif) / count;
		return aveDif;
	}
	
	public Double obtainSpecificDifference ( TreeMap<Integer, Integer[]> predictionResult, Double custThres ) {    //estPopSize, actualBugNum, totalBugNum
		double actualPercentage = 0.0;
		int stopIndex = 0;
		
		int count = 0;
		for ( Integer endReport : predictionResult.keySet() ) {
			count++;
			Integer[] result = predictionResult.get( endReport );
			double percentage = 1.0* result[1] / result[0];
			if ( result[0] != 0 && result[1] != 0 && percentage >= custThres && endReport > reportNumThres ) {
				stopIndex = endReport;
				actualPercentage = 1.0*result[1] / result[2];
				
				break;
			}
		}		
		
		return actualPercentage;
	}
	
	/*
	 * 提交X个，Y%的报告时，能够使得预测的缺陷数目和真实的缺陷数目一致
	 */
	public Integer whenPredictTrueBugNumber ( TreeMap<Integer, Integer[]> predictionResult, int totalReports ) {		
		Integer endReportTrueBug = 0;
		int count = 0;
		int index = -1;
		
		ArrayList<Integer> endReportList = new ArrayList<Integer>();
		for ( Integer endReport : predictionResult.keySet() ) {
			endReportList.add( endReport );
			index ++;
			
			Integer[] result = predictionResult.get( endReport );
			if ( result[0] == result[2] ){
				count ++;
			}else {
				count = 0;
			}
			
			if ( count > this.whenTrueBugThres ) {
				break;
			}
		}		
		//System.out.println( index + " " + count );
		if ( count != 0 ) {
			count = count -1;
		}		
		endReportTrueBug = endReportList.get( index-count);
		if ( count == 0 )
			endReportTrueBug = totalReports;
		
		return endReportTrueBug;
	}
	
	public Integer whenPredictTrueBugNumberRemain ( TreeMap<Integer, Integer[]> predictionResult, int totalReports ) {
		ArrayList<Integer> endReportList = new ArrayList<Integer>();
		ArrayList<Integer> estBugNum = new ArrayList<Integer>();
		Integer totalBugNum = 0;
		for ( Integer endReport : predictionResult.keySet() ) {
			endReportList.add( endReport );
			Integer[] result = predictionResult.get( endReport );
			estBugNum.add( result[0] );
			totalBugNum = result[2];
		}	
		
		int i = estBugNum.size()-1;
		for ( ; i >=0 ; i-- ) {
			if ( estBugNum.get(i) != totalBugNum ) {
				break;
			}
		}
		
		Integer endReportTrueBug = totalReports;
		if ( i < estBugNum.size() -1 ) {
			endReportTrueBug = endReportList.get( i+1 );
		}
		return endReportTrueBug;
	}
	/*
	 * 首先找出真实发现的缺陷数目为X%对应的报告的数目Y，然后找出提交了Y个报告时，预测的缺陷数目和真实的缺陷数目之间的差别
	 */
	public Integer[] predictPerformanceForSpecificBugPercentage ( TreeMap<Integer, Integer[]> predictionResult, TestProject project, Double[] markPoints , boolean isAbs) {
		int totalBugNum = 0;
		for ( Integer endReport : predictionResult.keySet() ) {
			Integer[] result = predictionResult.get( endReport );
			totalBugNum = result[2];
			break;
		}
		
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		HashSet<String> noDupBugTag = new HashSet<String>();
		int index = 0;
		Integer[] markReports = new Integer[markPoints.length];
		for ( int i =0; i < reportList.size() && index < markPoints.length; i++ ) {
			String bugTag = reportList.get(i).getBugTag();
			String dupTag = reportList.get(i).getDupTag();
			if ( bugTag.equals( "审核通过") ) {
				noDupBugTag.add( dupTag );
			}
			
			double percent = 1.0*noDupBugTag.size() / totalBugNum;
			if ( index < markPoints.length && percent >= markPoints[index] ) {
				markReports[index] = i;
				index++;
			}
		}
		System.out.println ( markReports.toString() );
		
		index = 0;
		Integer[] predictBugNum = new Integer[markReports.length];
		for ( Integer endReport : predictionResult.keySet() ) {
			Integer[] result = predictionResult.get( endReport );
			//可能一个endReport对应多个marReports的情况
			while ( index < markReports.length && endReport >= markReports[index]) {
				if ( isAbs ) {
					predictBugNum[index] = Math.abs( result[0] - result[2] ) ;
				}else {
					predictBugNum[index] = result[0] - result[2];
				}
				
				index++;
			}
		}
		return predictBugNum;
	}
	
	public Double predictPerformanceForBugPercentageVariance ( TreeMap<Integer, Integer[]> predictionResult, TestProject project, Double[] markPoints, boolean isAbs ) {
		Integer[] predictBugNum = this.predictPerformanceForSpecificBugPercentage(predictionResult, project, markPoints, isAbs);
		int sum = 0;
		for ( int i =0; i < predictBugNum.length; i++ ) {
			sum += predictBugNum[i];
		}
		double average = 1.0*sum / predictBugNum.length;
		
		double stdVar = 0.0;
		for ( int i =0; i < predictBugNum.length; i++ ) {
			stdVar += (1.0*predictBugNum[i] - average) * (1.0*predictBugNum[i] - average);
		}
		stdVar = Math.sqrt( stdVar/predictBugNum.length );
		
		return stdVar;
	}
}
