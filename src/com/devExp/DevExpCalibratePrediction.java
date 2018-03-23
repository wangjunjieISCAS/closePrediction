package com.devExp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.data.TestProject;
import com.data.TestReport;

public class DevExpCalibratePrediction {
	Integer beginInsReport = 0;
	Integer endInsReport = 0;
	Integer capSize = 7;
	
	public void prepareDevExpPredictionData ( TestProject project, TreeMap<Integer, Integer> CRCResult ) {
		ArrayList<TestReport> reportList = project.getTestReportsInProj();
		//CRCResult中的endReport为真实的endReport的index（从0开始的index）-1，也就是说这个endReport为<endReport得到的
		for ( int i =0; i < reportList.size(); i++ ) {
			
		}
	}
	
	public TreeMap<Integer, Integer[]> readCRCPredictionResults ( String fileName ) {
		TreeMap<Integer, Integer[]> CRCResult = new TreeMap<Integer, Integer[]>();
		
		beginInsReport = 0;
		endInsReport = 0;
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File (fileName ) ));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(",");
				Integer reportNum = Integer.parseInt( temp[0] );
				Integer predictNum = Integer.parseInt( temp[1] );
				Integer actualNum = Integer.parseInt( temp[2] );
				Integer totalNum = Integer.parseInt( temp[3] );
				Integer[] result = new Integer[] { predictNum, actualNum, totalNum };
				
				CRCResult.put( reportNum, result );
				
				double percentage = 1.0*actualNum / totalNum;
				if ( beginInsReport == 0 && percentage >= 0.8 ) {
					beginInsReport = reportNum;
				}
				if ( endInsReport == 0 && percentage >= 1.0 ) {
					endInsReport = reportNum;
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CRCResult;
	}
	
	public HashMap<String, Integer> readDevExp ( String fileName ) {
		HashMap<String, Integer> devExpList = new HashMap<String, Integer>();
		
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File (fileName ) ));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(",");
				String userId = temp[0];
				Integer exp = Integer.parseInt( temp[1] );
				
				devExpList.put( userId, exp );
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return devExpList;
	}
}
