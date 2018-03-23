
public class Test {
	public static void main ( String[] args ) {
		Integer[] numCapInEachSample = { 9, 8, 9, 14, 8, 5, 18, 11, 4, 3, 16, 5, 2, 7, 9, 0, 4, 10 };
		int sum = 0;
		int inSum = 0;
		for ( int i =0; i < numCapInEachSample.length; i++ ) {
			inSum += numCapInEachSample[i];
			for ( int j = i+1; j < numCapInEachSample.length; j++ ) {
				sum += numCapInEachSample[i] * numCapInEachSample[j];
			}
		}
		System.out.println ( numCapInEachSample.length + " " + inSum + " " + sum );
	}
}	
