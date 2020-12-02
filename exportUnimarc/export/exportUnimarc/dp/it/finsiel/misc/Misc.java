package it.finsiel.misc;

public class Misc {
        
	public static int binarySearch(String[] sorted, String key) {
	    int first = 0;
	    int upto  = sorted.length;
	    
	    while (first < upto) {
	        int mid = (first + upto) / 2;  // Compute mid point.
	        if (key.compareTo(sorted[mid]) < 0) {
	            upto = mid;       // repeat search in bottom half.
	        } else if (key.compareTo(sorted[mid]) > 0) {
	            first = mid + 1;  // Repeat search in top half.
	        } else {
	            return mid;       // Found it. return position
	        }
	    }
	    return -(first + 1);      // Failed to find key
	}
        
	public static boolean emptyString(String textLine) {

		for (int i = 0; i < textLine.length(); i++) {
			if (textLine.charAt(i) != '\n' && textLine.charAt(i) != '\r'
					&& textLine.charAt(i) != '\t' && textLine.charAt(i) != ' ')
				return false;
		}
		return true;
	}

	public static boolean emptyOrCommentedString(String textLine) {

		for (int i = 0; i < textLine.length(); i++) {
			if (textLine.charAt(i) == '#')
				return true;
			if (textLine.charAt(i) != '\n' && 
				textLine.charAt(i) != '\r' && 
				textLine.charAt(i) != '\t' && 
				textLine.charAt(i) != ' ')
				return false;
		}
		return true;
	}






} // End MiscString

