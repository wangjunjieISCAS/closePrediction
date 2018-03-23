package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.csvreader.CsvReader;

/*
 * 每张图包含所有的number in each capture， equal time thres，但是只包含一个attrName
 */
public class PerformanceBoxPlotOneAttribute {
	public void prepareBoxPlotData ( String folderName, String attrName, String outFileName ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( " " + "," + "  " + "," + "number");
			writer.newLine();
			
			File projectsFolder = new File ( folderName );
			if ( projectsFolder.isDirectory() ){
				String[] projectFileList = projectsFolder.list();
				for ( int i = 0; i< projectFileList.length; i++ ){
					String[] temp = projectFileList[i].split( "-");
					Integer captureSize = Integer.parseInt( temp[1] );
					Integer equalTimeThres = Integer.parseInt( temp[2] );
					
					ArrayList<Double> attrValues = this.readPerformance( folderName + "/" + projectFileList[i], attrName);
					for ( int j =0; j < attrValues.size(); j++ ) {
						writer.write( captureSize + "," + equalTimeThres + "," + attrValues.get(j).toString() );
						writer.newLine();
					}					
				}
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Double> readPerformance ( String fileName, String attrName ) {
		ArrayList<Double> attrValues = new ArrayList<Double>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
	        	Double value = Double.parseDouble( reader.get( attrName ) ) ;
	        	attrValues.add( value );	        	
	        }
			
	        reader.close();
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attrValues;
	}
	
	public static void main ( String args[] ) {
		PerformanceBoxPlotOneAttribute plot = new PerformanceBoxPlotOneAttribute();
		plot.prepareBoxPlotData( "data/output/performance", "percentReportsSubmit", "data/output/percentReports.csv");
	}
}
