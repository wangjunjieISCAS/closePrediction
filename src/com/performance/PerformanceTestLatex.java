package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class PerformanceTestLatex {
	//String[] attrName = { "Trend", "Arrival", "Knee", "M0", "Mth", "MhJK", "MhCH", "MtCH"};
	String[] attrName = { "M080", "M085", "M090", "M095", "M0100"};
	
	public void generateLatexFile ( String bugFile, String reportFile, String FFile, String outFile ) {
		//Integer[] rank = { 1, 2, 3, 4, 5, 6, 7, 8};
		Integer[] rank = { 1, 2, 3, 4, 5};
		
		Double[][] bugResults = this.readHypothesisTestResults( bugFile );
		Double[][] reportResults = this.readHypothesisTestResults( reportFile );
		Double[][] FResults = this.readHypothesisTestResults( FFile );
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFile )));
			
			for ( int i =0; i < rank.length; i++ ) {
				Integer curRank = rank[i];
				String str = attrName[i];
				for ( int j =0; j < rank.length; j++ ) {
					int inCurRank = rank[j];
					if ( j < i ) {
						str += " & & & ";
					}
					if ( j > i ) {
						Double bugValue = bugResults[curRank][inCurRank];
						if ( bugValue == -1.0)
							bugValue = bugResults[inCurRank][curRank];
						String bugStr = bugValue.toString();
						//bugStr = this.transferValue( bugValue );
						//System.out.println( bugStr );
						
						while ( bugStr.length() <= 4 )
							bugStr = bugStr + "0";
						str += " & " + bugStr.substring(0, 4 );
						if ( bugValue < 0.05 )
							str += "\\tiny{*}";
						
						Double reportValue = reportResults[curRank][inCurRank];
						if ( reportValue == -1.0)
							reportValue = reportResults[inCurRank][curRank];
						String reportStr = reportValue.toString();
						//reportStr = this.transferValue( reportValue );
						
						while ( reportStr.length() <= 4 )
							reportStr = reportStr + "0";
						str += " & " + reportStr.substring(0,  4  );
						if ( reportValue < 0.05 )
							str += "\\tiny{*}";
						
						Double FValue = FResults[curRank][inCurRank];
						if ( FValue == -1.0)
							FValue = FResults[inCurRank][curRank];
						String FStr = FValue.toString();
						//FStr = this.transferValue( FValue );
						while ( FStr.length() <= 4 )
							FStr = FStr + "0";
						
						str += " & " + FStr.substring(0, 4 );
						if ( FValue < 0.05 )
							str += "\\tiny{*}";
					}
				}
				str += " \\\\";
				writer.write( str );
				writer.newLine();
				writer.write( "\\hline");
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String transferValue ( Double value ) {
		String strValue = value.toString();
		if ( value == 0.0)
			strValue = "0.000";
		if ( value == 1.0)
			strValue = "1.000";
		
		return strValue;
	}
	
	public Double[][] readHypothesisTestResults ( String fileName ) {
		Double[][] results = new Double[9][9];
		for ( int i =0; i < results.length; i++ ) {
			for ( int j=0; j < results[i].length; j++ ) {
				results[i][j] = -1.0;
			}
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( fileName )));	
			String line = "";
			int index =-1;
			while ( (line = br.readLine() ) != null ) {
				index++;
				if ( index == 0 )
					continue;
				
				String[] temp = line.split( ",");
				if ( temp.length == 0 )
					continue;
				for ( int i =1; i < temp.length; i++ ) {
					if ( temp[i].length() == 0 || temp[i] == null || temp[i].trim().length() == 0 )
						continue;
					//System.out.println( index + " " +  i + " " + "||" + temp[i] + "||");
					results[index][i] = Double.parseDouble( temp[i]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	public static void main ( String[] args ) {
		PerformanceTestLatex performance = new PerformanceTestLatex();
		String typeInFile = "M0";
		performance.generateLatexFile( "data/output/" +  typeInFile + "-bug.csv", "data/output/" + typeInFile + "-report.csv", 
				"data/output/" + typeInFile + "-F.csv",  "data/output/" + typeInFile + "-latex.txt");
	}
}
