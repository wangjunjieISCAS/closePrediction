package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import com.csvreader.CsvReader;

public class PerformanceMannWhitneyUTest {
	//String[] categoryRank = { "Trend", "Arrival", "Knee", "M0", "Mth", "MhJK", "MhCH", "MtCH"};
	String[] attrName = { "%bug", "%reducedCost", "F1"};
	String[] categoryRank = { "M080", "M085", "M090", "M095", "M0100"};
	
	public void conductMannWhitneyTest ( String inFileName, String typeInFile ) {
		TreeMap<String, double[]> dataset = this.readPerformanceData( inFileName );
		MannWhitneyUTest test = new MannWhitneyUTest();
		
		double[][] bugTest = new double[categoryRank.length][categoryRank.length];
		double[][] reportTest = new double[categoryRank.length][categoryRank.length];
		double[][] F1Test = new double[categoryRank.length][categoryRank.length];
		for ( int i =0; i < bugTest.length; i++ ) {
			for ( int j =0; j < bugTest[0].length; j++) {
				bugTest[i][j] = -1.0;
				reportTest[i][j] = -1.0;
				F1Test[i][j] = -1.0;
			}
		}
		
		for ( int j =0; j < categoryRank.length; j++ ) {	
			for ( int k =j+1; k < categoryRank.length; k++ ) {   //test between j and k 
				for ( int i =0; i < attrName.length; i++ ) {
					double[] x = dataset.get( categoryRank[j] + "-" + attrName[i] );
					double[] y = dataset.get( categoryRank[k] + "-" + attrName[i] );
					
					double pValue = test.mannWhitneyUTest(x, y);
					
					if ( i == 0 )
						bugTest[j][k] = pValue;
					else if ( i == 1)
						reportTest[j][k] = pValue;
					else
						F1Test[j][k] = pValue;
				}
			}
		}
		
		this.generateTestFile( "data/output/" + typeInFile + "-bug.csv", bugTest );
		this.generateTestFile( "data/output/" + typeInFile + "-report.csv", reportTest );
		this.generateTestFile( "data/output/" + typeInFile + "-F.csv", F1Test);
	}
	
	public void generateTestFile ( String outFileName, double[][] testResult ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			
			writer.write ( "" + ",");
			for ( int i =0; i < categoryRank.length; i++ ) {
				writer.write( i+1 + ",");
			}
			writer.newLine();
			
			for ( int j =0; j < categoryRank.length; j++ ) {	
				writer.write( j+1 + ",");
				for ( int k =0; k < categoryRank.length; k++ ) {  
					double temp = testResult[j][k];
					if ( j ==0 && k ==0 )
						writer.write( "0" + ",");
					else if ( temp < 0.0 )
						writer.write( " " + ",");
					else if ( temp < 0.0009 )
						writer.write( "0.000" + ",");
					else
						writer.write( temp + ",");
				}
				writer.newLine();
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public TreeMap<String, double[]> readPerformanceData ( String inFileName ) {
		ArrayList<String> categoryAttrList = new ArrayList<String>();
		TreeMap<String, double[]> dataset = new TreeMap<String, double[]>();
		for ( int i =0; i < categoryRank.length; i++ ) {
			for ( int j = 0; j < attrName.length; j++ ) {
				String name = categoryRank[i] + "-" + attrName[j];
				categoryAttrList.add( name );
				
				double[] value = new double[218];
				dataset.put( name, value);
			}
		}			
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( inFileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
			int index = 0;
	        while ( reader.readRecord() ){
	        	for ( int i =0; i < categoryAttrList.size(); i++ ) {
	        		String name = categoryAttrList.get( i );
	        		System.out.println ( name );
	        		double[] value = dataset.get( name);
	        		
	        		Double temp = Double.parseDouble( reader.get( name ) ) ;        
	        		value[index] = temp;
	        		dataset.put( name, value);
	        	}
        		index++;
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
		return dataset;
	}
	
	public static void main ( String args[] ) {
		PerformanceMannWhitneyUTest test = new PerformanceMannWhitneyUTest();
		String typeInFile = "M0";
		test.conductMannWhitneyTest( "data/output/" + typeInFile + "-test.csv", typeInFile );
	}
}
