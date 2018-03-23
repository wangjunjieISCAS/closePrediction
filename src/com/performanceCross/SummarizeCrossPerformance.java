package com.performanceCross;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.CsvReader;

public class SummarizeCrossPerformance {
	String[] attrName = { "percentBugsDetected", "percentSavedEffort", "F1"};
	String[] outputAttrName = { "%bug", "%reducedCost", "F1"};
	
	String[] categoryRank = { "Trend", "Arrival", "Knee", "M0", "Mth", "MhJK", "MhCH", "MtCH"};
	//String[] categoryRank = { "M0-80", "M0-85", "M0-90", "M0-95", "M0-100"};
	/*
	 * 每个文件夹（例如crossPerformanceM0）中存储了100次随机试验的结果，每个文件中存储的是218个项目的一次随机的结果；这里将每个文件中218个项目的performance取中值，然后将100次的中值汇总到一个文件中
	 */
	
	public void summarizeMedianPerformanceForAllMethods ( String[] folderNameList , String outFileName ) {
		HashMap<String, HashMap<Integer, Double[]>> sumMedianPerf = new HashMap<String, HashMap<Integer, Double[]>>();
		
		for ( int i =0; i < folderNameList.length; i++ ) {
			String folderName = folderNameList[i];
			HashMap<Integer, Double[]> medianPerfMethod = this.summarizeMedianPerformanceForOneMethod( folderName );
			
			String displayName = folderName.substring( new String("data/output/crossPerformance").length() );
			sumMedianPerf.put( displayName, medianPerfMethod );
		}

		this.generateDataPerCategoryAttr( "data/output/M0-test.csv", sumMedianPerf);
		this.generatePlotDataFile( "data/output/M0.csv", sumMedianPerf);	
		
		this.otainRandNumOfMedian(sumMedianPerf);
	}
	
	public void summarizeStatisPerformanceForAllMethods ( String[] folderNameList  ) {
		for ( int i =0; i < folderNameList.length; i++ ) {
			String folderName = folderNameList[i];
			TreeMap<String, Double[][]> statisPerfMethod = this.summarizeStatisPerformanceForOneMethod(folderName);
			
			String displayName = folderName.substring( new String("data/output/crossPerformance").length() );
			
			try {
				BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( "data/output/crossPerformanceStatis/" + displayName + ".csv" )));
								
				for ( String project : statisPerfMethod.keySet() ) {
					writer.write( project + ",");
					Double[][] perf = statisPerfMethod.get( project );
					for ( int j =0; j < perf.length; j++ ) {
						for( int k =0; k < perf[j].length; k++ ) {
							writer.write( perf[j][k] + ",");
						}
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
	}
		
	//对于每种方法，得到crossEffectiveness-test中F1的中值对应的random number
	public void otainRandNumOfMedian ( HashMap<String, HashMap<Integer, Double[]>> sumMedianPerf) {
		for ( String method : sumMedianPerf.keySet() ) {
			HashMap<Integer, Double[]> perf = sumMedianPerf.get( method );
			
			List<HashMap.Entry<Integer, Double[]>> newPerf = new ArrayList<HashMap.Entry<Integer, Double[]>>( perf.entrySet());

			Collections.sort( newPerf, new Comparator<HashMap.Entry<Integer, Double[]>>() {   
				public int compare(HashMap.Entry<Integer, Double[]> o1, HashMap.Entry<Integer, Double[]> o2) {      
				        //return (o2.getValue() - o1.getValue()); 
				        return o2.getValue()[2].compareTo(o1.getValue()[2] ) ;
				    }
				}); 
			
			int index = newPerf.size() /2;
			int randNum = newPerf.get( index).getKey();
			
			System.out.println( "================ " + method + " " + randNum );
		}
	}
	
	public void generatePlotDataFile ( String outFileName, HashMap<String, HashMap<Integer, Double[]>> dataset ) {
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( " " + "," + "  " + "," + "number");
			writer.newLine();
			
			for ( int k =0; k < categoryRank.length; k++) {
				String categoryName = categoryRank[k];
				
				if ( !dataset.containsKey( categoryName))
					continue;
				
			//for ( String categoryName : dataset.keySet() ) {
				HashMap<Integer, Double[]> values = dataset.get( categoryName);
				for ( Integer i : values.keySet() ) {
					for ( int j =0; j < values.get(i).length; j++ ) {
						
						writer.write( categoryName + "," + outputAttrName[j] + "," + values.get(i)[j]);
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
	
	public void generateDataPerCategoryAttr ( String outFileName, HashMap<String, HashMap<Integer, Double[]>> dataset ) {
		
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter ( new File ( outFileName )));
			writer.write( "randNum" + ",");
			for ( int k  =0; k < categoryRank.length; k++ ) {
				String categoryName = categoryRank[k];
				
				if ( !dataset.containsKey( categoryName ))
					continue;
				
				for ( int i =0; i < attrName.length; i++ ) {
					writer.write( categoryName + "-" + outputAttrName[i] + ",");
				}
			}
			writer.newLine();
			
			HashMap<Integer, Double[]> values = dataset.get( categoryRank[0]);
			for ( Integer i : values.keySet() ) {
				writer.write( i + ",");
				
				for ( int j=0; j < categoryRank.length; j++ ) {
					String categoryName = categoryRank[j];
					if ( !dataset.containsKey( categoryName ))
						continue;
					HashMap<Integer, Double[]> curValues = dataset.get( categoryName );
					
					Double[] rowValues = curValues.get( i );
					for ( int k =0; k < rowValues.length; k++ ) {
						writer.write( rowValues[k].toString() + ",");
					}
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
	
	public HashMap<Integer, Double[]> summarizeMedianPerformanceForOneMethod ( String folderName ) {
		Pattern pattern = Pattern.compile("[0-9]*");
		
		HashMap<Integer, Double[]> totalMedianPerf = new HashMap<Integer, Double[]>();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ) {
			String[] projectList = projectsFolder.list();
			for ( int i =0; i < projectList.length; i++ ) {
				String fileName = projectList[i];
				
				Matcher isNum = pattern.matcher( fileName.charAt(0)+"");
				if ( !isNum.matches() )
					continue;
				Double[] medianPerformance = this.obtainMedianPerformanceForOneValidation( folderName + "/" + fileName );
				
				String[] temp = fileName.split( "-");
				int index = Integer.parseInt( temp[0] );
				totalMedianPerf.put( index, medianPerformance );
			}
		}
		return totalMedianPerf;
	}
	
	/*
	 * 得到每种方法，在每个项目上的最大值，最小值，四分之一，四分之三，中值
	 */
	public TreeMap<String, Double[][]> summarizeStatisPerformanceForOneMethod ( String folderName ) {
		Pattern pattern = Pattern.compile("[0-9]*");
		
		TreeMap<String, ArrayList<Double[]>> totalCrossPerf = new TreeMap<String, ArrayList<Double[]>>();
		
		File projectsFolder = new File ( folderName );
		if ( projectsFolder.isDirectory() ) {
			String[] projectList = projectsFolder.list();
			for ( int i =0; i < projectList.length; i++ ) {
				String fileName = projectList[i];
				
				Matcher isNum = pattern.matcher( fileName.charAt(0)+"");
				if ( !isNum.matches() )
					continue;
				TreeMap<String, Double[]> crossPerf = this.obtainTotalPerformanceForOneValidation( folderName + "/" + fileName );
				
				for ( String project : crossPerf.keySet() ) {
					ArrayList<Double[]> values = new ArrayList<Double[]>();
					if ( totalCrossPerf.containsKey( project )) {
						values = totalCrossPerf.get( project );
					}
					values.add( crossPerf.get( project ));
					totalCrossPerf.put( project, values );
				}
			}
		}
		
		TreeMap<String, Double[][]> statisPerf = new TreeMap<String, Double[][]>();
		for ( String project : totalCrossPerf.keySet() ) {
			ArrayList<Double[]> perf = totalCrossPerf.get( project );
			ArrayList<Double> bug = new ArrayList<Double>();
			ArrayList<Double> cost = new ArrayList<Double>();
			ArrayList<Double> F1 = new ArrayList<Double>();
			for ( int i =0; i < perf.size(); i++ ) {
				Double[] value = perf.get( i );
				bug.add( value[0] );
				cost.add( value[1] );
				F1.add( value[2] );
			}
			
			Double[] bugStatis = this.obtainStatis( bug );
			Double[] costStatis = this.obtainStatis( cost );
			Double[] F1Statis = this.obtainStatis( F1 );
			Double[][] statis = new Double[3][bugStatis.length];
			statis[0] = bugStatis;
			statis[1] = costStatis;
			statis[2] = F1Statis;
			
			statisPerf.put( project, statis );
		}
		return statisPerf;		
	}
	
	public TreeMap<String, Double[]> obtainTotalPerformanceForOneValidation ( String inFileName ) {
		TreeMap<String, Double[]> totalPerf = new TreeMap<String, Double[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( inFileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
	        	String project = reader.get( "project");
	        	
        		Double temp = Double.parseDouble( reader.get( attrName[0] ) ) ; 
        		Double temp2 = Double.parseDouble( reader.get( attrName[1] ) ) ; 
        		Double temp3 = Double.parseDouble( reader.get( attrName[2] ) ) ; 
        		
        		Double[] value = { temp, temp2, temp3};
        		totalPerf.put( project , value);
	        }
			
	        reader.close();
			br.close();
			
			return totalPerf;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Double[] obtainMedianPerformanceForOneValidation ( String inFileName ) {
		ArrayList<Double> bugs = new ArrayList<Double>();
		ArrayList<Double> reports = new ArrayList<Double>();
		ArrayList<Double> F1 = new ArrayList<Double>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader( new File ( inFileName )));	
			CsvReader reader = new CsvReader( br, ',');
			
			reader.readHeaders(); 
	        while ( reader.readRecord() ){
        		Double temp = Double.parseDouble( reader.get( attrName[0] ) ) ; 
        		bugs.add( temp );
        		
        		temp = Double.parseDouble( reader.get( attrName[1] ) ) ; 
        		reports.add( temp );
        		
        		temp = Double.parseDouble( reader.get( attrName[2] ) ) ; 
        		F1.add( temp );
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
		
		Double medianBug = this.obtainMedian( bugs );
		Double medianReport = this.obtainMedian( reports );
		Double medianF1 = this.obtainMedian( F1);
		
		Double[] result = { medianBug, medianReport, medianF1};
		return result;
	}
	
	public Double[] obtainStatis ( ArrayList<Double> value ) {
		Collections.sort( value );
		
		Double median = -1.0;
		
		int size = value.size();
		if ( size % 2 == 0 ) {
			median = ( value.get( size / 2) + value.get( size/2-1)) / 2;
		}else {
			median = value.get( size /2 );
		}
		
		Double min = value.get(0);
		Double max = value.get( value.size()-1 );
		Double oneQuart = value.get( value.size() / 4 );
		Double threeQuart = value.get( value.size()-value.size()/4 );
		
		Double[] perf = {min, oneQuart, median, threeQuart, max };
		return perf;
	}
	public Double obtainMedian ( ArrayList<Double> value ) {
		Collections.sort( value );
		
		Double median = -1.0;
		
		int size = value.size();
		if ( size % 2 == 0 ) {
			median = ( value.get( size / 2) + value.get( size/2-1)) / 2;
		}else {
			median = value.get( size /2 );
		}
		return median;
	}
	
	public static void main ( String args[] ) {		
		String[] typeList = { "Trend", "M0", "Knee", "Arrival", "Mth", "MhJK", "MhCH", "MtCH"} ;  //MhJK, MtCH, MhCH, Trend
		//String[] typeList = { "M0-80", "M0-85", "M0-90", "M0-95", "M0-100"} ;  //MhJK, MtCH, MhCH, Trend
		String[] folderNameList = new String[typeList.length];
		for ( int i =0; i < folderNameList.length; i++ )
			folderNameList[i] = "data/output/crossPerformance" + typeList[i];
		
		SummarizeCrossPerformance sum = new SummarizeCrossPerformance();
		sum.summarizeStatisPerformanceForAllMethods( folderNameList );
		
		//String outFile =  "data/output/crossSum-test.csv";
		//sum.summarizeMedianPerformanceForAllMethods(folderNameList, outFile );
	}
}
