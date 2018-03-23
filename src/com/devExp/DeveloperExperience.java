package com.devExp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.data.TestProject;
import com.data.TestReport;
import com.dataProcess.TestProjectReader;

public class DeveloperExperience {
	public void obtainDeveloperExperience ( String folderName ) {
		TestProjectReader reader = new TestProjectReader();
		
		HashMap<String, Integer> devExpList = new HashMap<String, Integer>();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ){
			String[] projectFileList = projectsFolder.list();
			for ( int i = 0; i< projectFileList.length; i++ ){
				String fileName = folderName + "/" + projectFileList[i];
				System.out.println ( "================================== " + fileName );
				
				TestProject project = reader.loadTestProject( fileName );
				ArrayList<TestReport> reportList = project.getTestReportsInProj();
				for ( int j =0; j < reportList.size(); j++ ) {
					TestReport report = reportList.get( j );
					String userId = report.getUserId();
					String bugTag = report.getBugTag();
					if ( bugTag.equals( "ÉóºËÍ¨¹ý")) {
						int count =1 ;
						if ( devExpList.containsKey( userId )) {
							count += devExpList.get( userId);
						}
						devExpList.put( userId, count );
					}
				}
			}
		}
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/devExp.csv" )));
			
			for ( String userId : devExpList.keySet() ) {
				writer.write( userId.toString() + "," + devExpList.get( userId ).toString());
				writer.newLine();
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main ( String args[] ) {
		DeveloperExperience expTool = new DeveloperExperience();
		expTool.obtainDeveloperExperience( "data/input/projects");
	}
}
