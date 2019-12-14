package edu.asu.jmars.layer.planning;

import edu.asu.jmars.util.DebugLog;
import edu.asu.jspice.JSpice;
import edu.asu.jspice.SpiceException;

/**
 * Time conversion routines to/from ET and UTC/SCET.
 * <p>
 * NOTE: ALL METHODS INVOLVING ET REQUIRE AN 'LS' SPICE KERNEL.  It is
 * the responsibility of the calling code to insure that the appropriate
 * kernel has been loaded for accurate time conversions.  Otherwise,
 * exceptions will be thrown.
 */
public class TimeUtil
{
    private static final DebugLog log = DebugLog.instance();
    private static final String UTC_FORMAT = "yyyy-DDD'T'HH:mm:ss.SSSSS::RND";
    private static final String SPICE_UTC_FORMAT = "YYYY-MM-DD THR:MN:SC.#####::RND";
    private static final String SPICE_UTC_FORMAT_1 = "YYYY-MM-DD THR:MN:SC.#::RND";
    private static final String SPICE_UTC_FORMAT_5 = "YYYY-MM-DD THR:MN:SC.#####";
    private static final String REPORTS_UTC_FORMAT = "YYYY-MM-DDTHR:MN:SC.###::RND";

    /**
	 * Converts from "YYYY-DOYTHH:MM:SS" format in UTC to ET.
	 * <p>
	 * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
	 */
	public static double utcToET(String utc) throws SpiceException {
		double et = 0;

		if (utc != null && !utc.trim().equals(""))
			try {
				StringBuffer buf = new StringBuffer(utc);
				double[] output = new double[1];
				output[0] = et;
				JSpice.str2etc(buf, output);
				et = output[0];
			} catch (SpiceException e) {
				log.println(e);
				throw e;
			}

		return et;
	}
	
    /**
	 * Converts from ET time string to "YYYY-DOYTHH:MM:SS.SSS" format in UTC.
	 * <p>
	 * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
	 */
    public static String etToUtcStr(String et)
	throws SpiceException
    {
	return etToUtcStr(Double.parseDouble(et));
    }

    /**
     * Converts from ET to "YYYY-MM-DD THR:MN:SC.#####::RND" format in UTC.
     * <p>
     * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
     */
    public static String etToUtcStr(double et)
	throws SpiceException
    {
	String utc = "";

	try {
	    StringBuffer output = new StringBuffer();
	    StringBuffer format = new StringBuffer(SPICE_UTC_FORMAT);
	    JSpice.timoutc(et, format, UTC_FORMAT.length(), output);
	    utc = output.toString();
	}
	catch (SpiceException e) {
	    log.println(e);
	    throw e;
	}

	return utc;
    }
    
    /**
     * Converts from ET to "YYYY-MM-DDTHR:MN:SC.###::RND" format in UTC for the OpNav Report.
     * <p>
     * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
     */
    public static String etToUtcReportStr(double et)
	throws SpiceException
    {
	String utc = "";

	try {
	    StringBuffer output = new StringBuffer();
	    StringBuffer format = new StringBuffer(REPORTS_UTC_FORMAT);
	    JSpice.timoutc(et, format, REPORTS_UTC_FORMAT.length(), output);
	    utc = output.toString();
	}
	catch (SpiceException e) {
	    log.println(e);
	    throw e;
	}

	return utc;
    }
    
    /**
     * Converts from ET to "YYYY-MM-DD THR:MN:SC.#::RND" format in UTC. 
     * <p>
     * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
     */
    public static String etToUtcStrRnd(double et)
    throws SpiceException
    {
    String utc = "";

    try {
        StringBuffer output = new StringBuffer();
        StringBuffer format = new StringBuffer(SPICE_UTC_FORMAT_1);
        JSpice.timoutc(et, format, 24, output);
        utc = output.toString();
        
    }
    catch (SpiceException e) {
        log.println(e);
        throw e;
    }

    return utc;
    }
    
    /**
     * Converts from ET to "YYYY-DOYTHH:MM:SS.SSSSS" format in UTC.
     * <p>
     * NOTE: REQUIRES THAT AN 'LS' SPICE KERNEL ALREADY BE LOADED.
     */
    public static String etToUtcStr5Dec(double et)
    throws SpiceException
    {
    String utc = "";

    try {
        StringBuffer output = new StringBuffer();
        StringBuffer format = new StringBuffer(SPICE_UTC_FORMAT_5);
        JSpice.timoutc(et, format, SPICE_UTC_FORMAT_5.length(), output);
        utc = output.toString();
    }
    catch (SpiceException e) {
        log.println(e);
        throw e;
    }

    return utc;
    }
}
