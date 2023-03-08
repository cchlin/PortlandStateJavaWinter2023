package edu.pdx.cs410J.chlin;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String addFlightMessage(String airlineName, String flightNumber, String src, String depart, String dest, String arrive )
    {
        return String.format( "%s added a new flight %s from %s at %s to %s at %s", airlineName,
                flightNumber, src, depart, dest, arrive );
    }

    public static String allAirlinesDeleted() {
        return "All airlines have been deleted";
    }

}
