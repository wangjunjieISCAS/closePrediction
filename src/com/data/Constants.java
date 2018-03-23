package com.data;

import java.util.ArrayList;

public interface Constants {
	
	final static Integer MAX_IMAGE_NUM = 2;
	
	final static String INPUT_FILE_STOP_WORD = "data/input/stopWordListBrief.txt";
	final static boolean IS_NORMALIZE_TFIDF = true;
	
	final static Integer TOP_X_DUPLICATE_REPORT = 10;
	
	final static Integer FIELD_INDEX_TEST_CASE_ID = 0;
	final static Integer FIELD_INDEX_USER_ID = 1;
	final static Integer FIELD_INDEX_TEST_CASE_NAME	 = 2;
	final static Integer FIELD_INDEX_BUG_DETAIL = 9;
	final static Integer FIELD_INDEX_REPRO_STEPS = 10;
	final static Integer FIELD_INDEX_SUBMIT_TIME  = 15;
	final static Integer FIELD_BUG_TAG = 14;
	final static Integer FIELD_DUP_TAG  = 17;    //17 corresponds to R column, 18 corresponds to S column
	
	final static double IMAGE_THRES = 0.95;
	
	//capture recapture
	final static Integer INSTANCE_NUMBER_EACH_CAPTURE = 15;
	//final static Integer CAPTURE_NUMBER = 6;
	final static Integer EQUAL_TIME_THRES = 3;
	
	final static Integer PROJECT_SIZE  = 218;
	
}
