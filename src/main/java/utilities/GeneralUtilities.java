package utilities;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.prefs.Preferences;

import com.ibm.icu.text.SimpleDateFormat;

import journal.AppConstants;


public class GeneralUtilities {
	
	public static String getNameOfMonth(Calendar cal) {
		return getMonthForInt(cal.get(Calendar.MONTH));
	}
	
	public static String getFullFileSavePath() {
		return getFirstPartOfSavePath(true) + getSecondPartOfSavePath(false);
	}
	
	public static String getFileName() {
		Calendar cal = Calendar.getInstance();
		return getFileName(cal);
	}
	
	public static String getFileName(Calendar cal) {
		return AppConstants.ENTRY + "-" + cal.get(Calendar.DAY_OF_MONTH) + AppConstants.FILE_EXTENTION;
	}
	
	public static String dateToString(Calendar cal) {		
		String suffix = generateSuffixString(cal);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd'" + suffix + ",' YYYY");
		return sdf.format(cal.getTime());
	}
	
	public static String majorHeader() {
		String suffix = generateSuffixString(AppConstants.cal);
		String dayName = getNameOfDay(AppConstants.cal);
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd'" + suffix + ",' YYYY '(" + dayName + ")' hh:mm a zzz");
		return "# " + sdf.format(AppConstants.cal.getTime());
	}
	
	public static String getNameOfDay(Calendar cal) {
		String result;
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		switch(dayOfWeek) {
			case Calendar.MONDAY:
				result = "Monday";
				break;
			case Calendar.TUESDAY:
				result = "Tuesday";
				break;
			case Calendar.WEDNESDAY:
				result = "Wednesday";
				break;
			case Calendar.THURSDAY:
				result = "Thursday";
				break;
			case Calendar.FRIDAY:
				result = "Friday";
				break;
			case Calendar.SATURDAY:
				result = "Saturday";
				break;
			case Calendar.SUNDAY:
				result = "Sunday";
				break;
			default:
				result = "Unknown";
		}
		
		return result;
	}
	
	public static String minorHeader(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a zzz");
		return "## " + sdf.format(cal.getTime());
	}
	
	private static String generateSuffixString(Calendar cal) {
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if(day > 3 && day < 21) {
			return "th";
		}
		int lastDigitOfDay = day%10;
		String suffix; 
		switch (lastDigitOfDay){
			case 1: 
				suffix = "st";
				break;
			case 2: 
				suffix = "nd";
				break;
			case 3: 
				suffix = "rd";
				break;
			default:
				suffix = "th";
				break;
		}
		
		return suffix;
	}
	
	public static String getFirstPartOfSavePath(boolean includEndingFileSeperator) {
		Preferences prefs = Preferences.userRoot().node(AppConstants.PREF_NODE);
		String saveLocation = prefs.get(AppConstants.PREF_SAVE_LOCATION, System.getProperty("user.home"));
		String path = saveLocation + File.separator + AppConstants.TOP_LEVEL_DIRECTORY_NAME;
		if(includEndingFileSeperator) {
			path = path + File.separator;
		}
		return path;
	}
	
	public static String getSecondPartOfSavePath(boolean includStartingFileSeperator) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date(System.currentTimeMillis()));
//		return getSecondPartOfSavePath(cal, includStartingFileSeperator);
		return getSecondPartOfSavePath(AppConstants.cal, includStartingFileSeperator);
	}
	
	public static String getSecondPartOfSavePath(Calendar cal, boolean includStartingFileSeperator) {
		String year = cal.get(Calendar.YEAR) + "";
		String month = GeneralUtilities.getNameOfMonth(cal);
		
		String path = "";
		if(includStartingFileSeperator) {
			path = File.separator;
		}

		path = path + year + File.separator + month;// + File.separator + day;
		
		return path;
	}
	
	public static String getResourceSavePath(boolean includeFinalFileSeperator) {
		String pathToEntries = getFullFileSavePath();
		String path = pathToEntries + File.separator + AppConstants.RESOURCES + File.separator + AppConstants.cal.get(Calendar.DAY_OF_MONTH);
		if(includeFinalFileSeperator) {
			return path + File.separator;
		}else {
			return path;
		}
	}

	private static String getMonthForInt(int num) {
	    String month = "wrong";
	    DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getMonths();
	    if (num >= 0 && num <= 11) {
	        month = months[num];
	    }
	    return month;
	}
}
