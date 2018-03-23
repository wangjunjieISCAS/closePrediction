package com.reportsOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import com.data.TestReport;

public class RuleOrderReports {
	
	public TreeMap<Integer, ArrayList<TestReport>> orderReportsBasedRule ( TreeMap<Integer, ArrayList<TestReport>> captureHistory  ) {
		HashMap<String, ArrayList<TestReport>> dupTagReportsList = new HashMap<String, ArrayList<TestReport>>();
		ArrayList<TestReport> totalReportList = new ArrayList<TestReport>();
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
				
				totalReportList.add( report );
			}
		}
		
		
		//按照dupTagReports的个数排序
		List<HashMap.Entry<String, ArrayList<TestReport>>> newDupTagList = new ArrayList<HashMap.Entry<String, ArrayList<TestReport>>>(dupTagReportsList.entrySet());
		Collections.sort( newDupTagList, new Comparator<HashMap.Entry<String, ArrayList<TestReport>>>() {   
			public int compare( HashMap.Entry<String, ArrayList<TestReport>> o1, HashMap.Entry<String, ArrayList<TestReport>> o2) {      
			        //return (o2.getValue() - o1.getValue()); 
				if ( o2.getValue().size() > o1.getValue().size() )
					return 1;
				else if ( o2.getValue().size() < o1.getValue().size() )
					return -1;
				else return 0;
			    }
			}); 
			
		double fracStep = 1.0*newDupTagList.size() / totalReportList.size();
		int fracFreq = 0;
		while ( true ) {
			double cond = fracFreq * fracStep;
			if  ( cond >= 1.0 )
				break;
			fracFreq++;
		}
		fracFreq--;
		
		ArrayList<String> avaReportList = new ArrayList<String>();    //dupTag - order 表示
		HashMap<Integer, String> reportNewOrder = new HashMap<Integer, String>();
		
		//put the first report
		int index = 0, stepIndex = 0;
		Random rand = new Random();
		for ( int i =0; i < newDupTagList.size(); i++ ) {
			String dupTag = newDupTagList.get(i).getKey();
			ArrayList<TestReport> reports = newDupTagList.get(i).getValue();
			
			String newTag = dupTag + ":" + "0";
			reportNewOrder.put( index++,  newTag );
			stepIndex += fracFreq;
			
			for ( int j = 1; j < reports.size(); j++ ) {
				avaReportList.add( dupTag + ":" + j );
			}
			
			for ( ; index < stepIndex && avaReportList.size() > 0; index ++ ) {
				int choice = 0;
				if ( avaReportList.size() > 0 ) {
					choice = rand.nextInt( avaReportList.size() );
				}
				
				reportNewOrder.put( index, avaReportList.get( choice) );
				
				avaReportList.remove( choice );
			}
		}
		//the remaining one
		for ( ; avaReportList.size() > 0 ; index ++ ) {
			int choice = 0;
			if ( avaReportList.size() > 0 ) {
				choice = rand.nextInt( avaReportList.size() );
			}
			
			reportNewOrder.put( index, avaReportList.get( choice) );
			
			avaReportList.remove( choice );
		}
		
		TreeMap<Integer, ArrayList<TestReport>> newCaptureHistory = new TreeMap<Integer, ArrayList<TestReport>>();
		for ( Integer cap : captureHistory.keySet() ) {
			ArrayList<TestReport> reports = new ArrayList<TestReport>();
			newCaptureHistory.put( cap, reports);
		}
		
		int newIndex = 0;
		for ( Integer cap : captureHistory.keySet() ) {
			int size = captureHistory.get( cap).size();
			
			for ( int i =0; i < size; i++ ) {
				String newTag = reportNewOrder.get( newIndex++ );
				String[] temp = newTag.split( ":");
				//System.out.println( newTag );
				
				ArrayList<TestReport> reports = dupTagReportsList.get( temp[0]);
				TestReport report = reports.get( Integer.parseInt( temp[1]));
				
				ArrayList<TestReport> newReports = newCaptureHistory.get( cap );
				newReports.add( report );
				newCaptureHistory.put( cap, newReports );
			}
		}
		
		return newCaptureHistory;
	}
}
