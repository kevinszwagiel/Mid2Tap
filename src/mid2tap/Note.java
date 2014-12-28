package mid2tap;

/**
 *
 * Holds all the information for each note. Mutators and Accessors
 * are available for all the variables.
 * 
 * @author Kevin
 * 
 */
public class Note {
    private double realTime; // seconds
    private double quarterTime; // seconds
    private char startEnd; // 0 = start, 1 = end
    private int noteValue;
    
    public void setRealTime(double inRealTime) {
        realTime = inRealTime;
    }
    
    public double getRealTime() {
        return realTime;
    }
    
    public void setQuarterTime(double inQuarterTime) {
        quarterTime = inQuarterTime;
    }
    
    public double getQuarterTime() {
        return quarterTime;
    }
    
    public void setStartEnd(char inStartEnd) {
        startEnd = inStartEnd;
    }
    
    public char getStartEnd() {
        return startEnd;
    }
    
    public void setNoteValue(int inNoteValue) {
        noteValue = inNoteValue;
    }
    
    public int getNoteValue() {
        return noteValue;
    }
}
