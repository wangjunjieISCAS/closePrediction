package com.findings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.data.Constants;

public class ProjectSelection {
	int reportNumThres = 100;
	
	public void selectProject ( String sourceFolderName, String targetFolderName ) {
		HashMap<String, Double> insRateList = new HashMap<String, Double>();
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File ( "data/output/bugCurveStatistics-total.csv")));
			String line = "";
			boolean isHeader = true;
			while ( (line=reader.readLine() ) != null ) {
				if ( isHeader == true ) {
					isHeader = false;
					continue;
				}
				String[] temp = line.split( ",");
				String project = temp[0].trim();
				Integer insNum = Integer.parseInt( temp[2] );
				Integer reportNum = Integer.parseInt( temp[3] );
				Double insRate = (1.0*insNum) / reportNum;
				
				insRateList.put( project, insRate );
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File projectsFolder = new File ( sourceFolderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String[] temp = projectFileList[i].split( "-");
				int reportNum = Integer.parseInt( temp[1] ) ;
				
				Double insRate = insRateList.get( projectFileList[i] );
				if ( reportNum > reportNumThres && insRate < 0.9 ) {
					
					Object[] result = this.readReportList( sourceFolderName + "/" + projectFileList[i] );
					ArrayList<String[]>  reportList = this.rankReportsBySubmitTime( (ArrayList<String[]>)result[1] );
					String[] header = (String[])result[0];
					
					this.storeReportList( targetFolderName + "/" + projectFileList[i], header, reportList);
				}
			}
		}
	}
	
	/*
	 * 将报告按照时间和是否为缺陷排序，并进行存储，后面就不需要进行排序了。
	 * 如果两个报告的提交时间完全相同，则将是缺陷的报告排在前面
	 */
	public ArrayList<String[]> rankReportsBySubmitTime ( ArrayList<String[]> reportList ) {
		SimpleDateFormat formatLine = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
		SimpleDateFormat formatCon = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		
		Comparator compBugTag = new Comparator<String[]>() {  
			@Override
			public int compare(String[] o1, String[] o2) {
				// TODO Auto-generated method stub
				Date submitTime1  = null;
				Date submitTime2 = null;
				String time1 = o1[Constants.FIELD_INDEX_SUBMIT_TIME];
				try {
					if ( time1.contains( "-")) {
						submitTime1 = formatCon.parse( time1 );
		        	}
					else {
		        		submitTime1 = formatLine.parse( time1 );
		        	}
					String time2 = o2[Constants.FIELD_INDEX_SUBMIT_TIME];
		        	if ( time2.contains( "-")) {
		        		submitTime2 = formatCon.parse( time2 );
		        	}
		        	else {
		        		submitTime2 = formatLine.parse( time2 );
		        	}
		        	
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if ( submitTime1.compareTo( submitTime2 ) == 0 ) {
					if ( o1[Constants.FIELD_BUG_TAG].equals( "审核通过"))
						return -1;
					if ( o1[Constants.FIELD_BUG_TAG].equals( "审核通过"))
						return 1;
				}
				return submitTime1.compareTo( submitTime2 );
			}  
        };        
        Collections.sort( reportList, compBugTag );
        
        return reportList;
	}
	
	public Object[] readReportList ( String fileName ) {
		ArrayList<String[]> valueList = new ArrayList<String[]>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));
			
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders();
			String[] header = reader.getHeaders();
			
			//逐行读入除表头的数据      
			int index = 0;
	        while ( reader.readRecord() ){
	        	String[] temp = reader.getValues();
	        	valueList.add( temp );
	        }
			
	        reader.close();
			br.close();
			
			Object[] result = new Object[2];
			result[0] = header;
			result[1] = valueList;
			
			return result;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public void storeReportList ( String fileName , String[] header, ArrayList<String[]> reportList) {
		try {
			CsvWriter writer = new CsvWriter ( fileName, ',', Charset.forName("GBK"));
			writer.writeRecord( header );
			
			for ( int i =0; i < reportList.size(); i++ ) {
				String[] report = reportList.get( i);
				writer.writeRecord( report );
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main ( String args[] ) {
		ProjectSelection selection = new ProjectSelection();
		
		String sourceFolderName = "G:\\eclipse-workspace\\CrowdWorkerSelection\\data\\input\\experimental dataset";
		String targetFolderName = "data/input/projects";
		selection.selectProject(sourceFolderName, targetFolderName);
	}
}
