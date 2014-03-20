package CredibilityGame;

import java.text.DecimalFormat;

public class Constraints {

	public static final String LOGGER_INITIALIZED = "Logger initialied";
	public static final String OPENING_BRACKET = "[";
	public static final String CLOSING_BRACKET = "]";
	public static final String CONSIDERING_RUNNING_AWAY = 
			"The HYIP is considering running away";
	public static final String CALCULATE_ROIS_EXECUTED = 
			"calculateRois() executed";
	public static final DecimalFormat DECIMAL_SHORT = new DecimalFormat(
			"#.######");
	public static final String COMMA = ",";
	public static final String SEPERATOR = "|";
	public static final double MUTATE_FACTOR = 0.05;
	public static final int MUTATE_CHANCE = 5;
	public static final String RESET_ALL_HYIPS_MESSAGE = 
			"resetAllHyips() starts work. Choosing all producers (HYIPs).";
	public static final String RESET_ALL_INVESTORS_MESSAGE = 
			"resetAllInvestors() starts work. Choosing all consumers (Investors).";
	public static final String CALCULATE_ROIS_MESSAGE = 
			"Let's calculate some ROI's";
}
