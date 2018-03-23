package com.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PerformanceCategory123 {
	
	public void obtainPerformanceForSeparateCategory ( String fileName, String outFileName, Integer[] category ) {
		HashMap<Integer, ArrayList<Integer>> projectCatMap = this.obtainProjectCategory();
		System.out.println ( projectCatMap.get( 1) + " " + projectCatMap.get(2) + " " + projectCatMap.get( 3));
		
		HashMap<Integer, String[]> projectPerformance = new HashMap<Integer, String[]>();
		String[] title = null;
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File ( fileName ) ));
			
			String line = "";
			boolean isFirstLine = true;
			
			while ( (line = reader.readLine()) != null ) {
				String[] temp = line.split( ",");
				if ( isFirstLine == true ) {
					title = temp;
					isFirstLine = false;
					continue;
				}
				
				String[] projectInfo = temp[0].trim().split( "-");
				int projectId = Integer.parseInt( projectInfo[0]);
				
				projectPerformance.put( projectId, temp );
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			for ( int i =0; i < title.length; i++ )
				writer.write( title[i] + ",");
			writer.newLine();
			
			for ( int i =0; i < category.length; i++ ) {
				ArrayList<Integer> projectList = projectCatMap.get( category[i] );
				for ( int j =0; j < projectList.size(); j++ ) {
					Integer projectId = projectList.get( j );
					//System.out.println ( projectId );
					String[] value = projectPerformance.get( projectId );
					
					for ( int k =0; k < value.length; k++)
						writer.write( value[k] + ",");
					writer.newLine();
				}
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, ArrayList<Integer>> obtainProjectCategory ( ) {
		HashMap<Integer, ArrayList<Integer>> projectCatMap = new HashMap<Integer, ArrayList<Integer>>();
		for ( int i =1; i<=3; i++ ) {
			ArrayList<Integer> projects = new ArrayList<Integer>();
			projectCatMap.put( i, projects );
		}
		
		try {
			BufferedReader reader = new BufferedReader ( new FileReader ( new File ( "data/output/taskCategory.csv" ) ));
			
			String line = "";
			while ( (line = reader.readLine()) != null ) {
				String[] temp = line.split( ",");
				if ( temp.length >= 1) {
					Integer value = Integer.parseInt( temp[0] ) ;
					ArrayList<Integer> valueList = projectCatMap.get(  1);
					valueList.add( value );
					projectCatMap.put( 1, valueList );
				}
					
				if ( temp.length >= 2 ) {
					Integer value = Integer.parseInt( temp[1] ) ;
					ArrayList<Integer> valueList = projectCatMap.get(  2);
					valueList.add( value );
					projectCatMap.put( 2, valueList );
				}
				if ( temp.length >= 3 ) {
					Integer value = Integer.parseInt( temp[2] ) ;
					ArrayList<Integer> valueList = projectCatMap.get(  3);
					valueList.add( value );
					projectCatMap.put( 3, valueList );
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return projectCatMap;
	}
	
	public void obtainPerformanceFolder ( String folderName ) {
		Integer[] categoryIndex = {2};
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = projectFileList[i];
				
				this.obtainPerformanceForSeparateCategory( folderName+"/"+fileName, folderName+"/"+"c2"+fileName, categoryIndex );
			}
		}
	}
	
	public static void main ( String[] args ) {
		PerformanceCategory123 cat = new PerformanceCategory123();
		String[] folderList = { "performanceTrend", "performanceM0", "performanceKnee", "performanceArrival", "performanceMth", 
				"performanceMhJK", "performanceMhCH", "performanceMtCH"};
		for ( int i =0; i < folderList.length; i++ )
			cat.obtainPerformanceFolder( "data/output/" + folderList[i] );
	}
}
