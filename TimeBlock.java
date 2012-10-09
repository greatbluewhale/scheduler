/**
 * Name:    Amuthan Narthana and Nicholas Dyszel
 * Section: 2
 * Program: Scheduler Project
 * Date:    10/8/12
 */

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * TimeBlock class stores a begin and end time in hr:min
 * 
 * @author Nicholas Dyszel
 * @version 1.0, 8 Oct 2012
 */
public class TimeBlock {
    public static final int MAX_HOUR = 23;   // the maximum value of an int representing an hour
    public static final int MAX_MINUTE = 59; // the maximum value of an int representing a minute
    
    private int startHour;      // the start hour of the time block (0-23)
    private int startMinute;    // the start minute of the time block (0-59)
    private int endHour;        // the end hour of the time block (0-23)
    private int endMinute;      // the end minute of the time block (0-59)
    
    /**
     * Init constructor of a time block
     * 
     * @param startHour, the hour of the start time
     * @param startMinute, the minute of the start time
     * @param endHour, the hour of the end time
     * @param endMinute, the minute of the end time
     * @throws Exception if the arguments do not yield a valid time block
     */
    public TimeBlock(int startHour, int startMinute, int endHour, int endMinute) throws Exception {
        // PRE: 0 <= startHour <= endHour < 24, 0 <= startMinute, endMinute < 60
        // POST: A time block is created with the given parameters
        if (startHour >= 0 && startMinute >= 0 && endHour <= MAX_HOUR && 
            endMinute <= MAX_MINUTE && isLessEq(startHour, startMinute, endHour, endMinute)){
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
        } else {
            throw new Exception("Invalid time inputs in TimeBlock constructor");
        }
    }
    
    /**
     * Getter for the start hour
     * @return  the start hour of the time block
     */
    public int getStartHour(){
        return startHour;
    }
    
    /**
     * Getter for the start minute
     * @return  the start minute of the time block
     */
    public int getStartMinute(){
        return startMinute;
    }
    
    /**
     * Getter for the end hour
     * @return  the end hour of the time block
     */
    public int getEndHour(){
        return endHour;
    }
    
    /**
     * Getter for the end minute
     * @return  the end minute of the time block
     */
    public int getEndMinute(){
        return endMinute;
    }
    
    /**
     * Returns true if hour1:min1 is at or before hour2:min2
     * 
     * @param hour1, hour of first time
     * @param min1, minute of first time
     * @param hour2, hour of second time
     * @param min2, minute of second time
     * @return true if hour1:min1 <= hour2:min2
     */
    public static boolean isLessEq(int hour1, int min1, int hour2, int min2) {
        return ((hour1 < hour2) || ((hour1 == hour2) && (min1 <= min2)));
    }
        
    /**
     * Computes the intersection of two sets of time blocks
     * e.g. If the first set contains { 10:00 - 11:30, 14:00 - 17:00 } and the second set contains
     * { 9:30 - 11:00, 13:00 - 14:00, 14:30 - 15:30, 16:30 - 18:00}, the intersection contains
     * { 10:00 - 11:00, 14:30 - 15:30, 16:30 - 17:00 }
     * 
     * @param timeBlocks1   the first set of time blocks
     * @param timeBlocks2   the second set of time blocks
     * @return              the intersection of block1 and block2
     * @throws Exception    if the ArrayList of TimeBlocks aren't sorted, it may lead to an Exception
     */
    public static ArrayList<TimeBlock> intersect(ArrayList<TimeBlock> timeBlocks1, ArrayList<TimeBlock> timeBlocks2) throws Exception {
        // PRE: block1 and block2 are both strictly sorted
        // POST: intersection contains time blocks in the intersection of block1 and block2 and is strictly sorted
        ArrayList<TimeBlock> intersection = new ArrayList<TimeBlock>();
        ListIterator<TimeBlock> it1 = timeBlocks1.listIterator();
        ListIterator<TimeBlock> it2 = timeBlocks2.listIterator();
        TimeBlock block1;   // current time block in block1
        TimeBlock block2;   // current time block in block2
        
        while (it1.hasNext() && it2.hasNext()) {
            // Compare it1 and it2
            // If it1.start < it2.start < it1.end, add it2.start - min{it1.end, it2.end}
            block1 = it1.next();
            block2 = it2.next();
            
            if (isLessEq(block1.startHour, block1.startMinute, 
                         block2.startHour, block2.startMinute) &&
                isLessEq(block2.startHour, block2.startMinute,
                         block1.endHour, block1.endMinute)) {
                if (isLessEq(block1.endHour, block1.endMinute, 
                             block2.endHour, block2.endMinute)) {
                    intersection.add(new TimeBlock(block2.startHour, block2.startMinute,
                                                   block1.endHour, block1.endMinute));
                } else {
                    intersection.add(block2);
                }
            } else if (isLessEq(block2.startHour, block2.startMinute,
                                block1.startHour, block1.startMinute) &&
                       isLessEq(block1.startHour, block1.startMinute,
                                block2.endHour, block2.endMinute)) {
                if (isLessEq(block1.endHour, block1.endMinute,
                             block2.endHour, block2.endMinute)) {
                    intersection.add(block1);
                } else {
                    intersection.add(new TimeBlock(block1.startHour, block1.startMinute,
                                                   block2.endHour, block2.endMinute));
                }
            }
            
            if (isLessEq(block1.endHour, block1.endMinute,
                         block2.endHour, block2.endMinute)) {
                it2.previous();
            } else {
                it1.previous();
            }
        }
        
        // Remove time blocks that have same start and end times
        for (TimeBlock block : intersection) {
            if ((block.startHour == block.endHour) && (block.startMinute == block.endMinute)) {
                intersection.remove(block);
            }
        }
        
        return intersection;
    }
    
    /**
     * Computes the difference between two sets
     * e.g. If minuend is { 8:00 - 20:00 } and subtrahend is { 10:00 - 12:00, 13:30 - 15:00 },
     *      then the difference is { 8:00 - 10:00, 12:00 - 13:30, 15:00 - 20:00}.
     * 
     * @param minuend       the set to subtract from
     * @param subtrahend    the subtracted set
     * @return              the difference of minuend - subtrahend
     * @throws Exception    if the ArrayList of TimeBlocks aren't sorted, it may lead to an Exception
     */
    public static ArrayList<TimeBlock> difference(ArrayList<TimeBlock> minuend, ArrayList<TimeBlock> subtrahend) throws Exception {
        return intersect(minuend, complement(subtrahend));
    }
    
    /**
     * Computes the complement of a set
     * e.g. If the input set is { 6:00 - 8:00, 14:00 - 17:00, 22:00 - 24:00 }, its complement is
     * { 0:00 - 6:00, 8:00 - 14:00, 17:00 - 22:00 }
     * 
     * @param set   the input set
     * @return      the complement of set
     * @throws Exception if the ArrayList of TimeBlocks aren't sorted, it may lead to an Exception
     */
    public static ArrayList<TimeBlock> complement(ArrayList<TimeBlock> set) throws Exception {
        // PRE: set is strictly sorted
        // POST: setComplement is strictly sorted and contains the time blocks opposite of set
        ArrayList<TimeBlock> setComplement = new ArrayList<TimeBlock>();
        TimeBlock previousBlock = new TimeBlock(0, 0, 0, 0);
        
        for (TimeBlock block : set) {
            setComplement.add(new TimeBlock(previousBlock.endHour, previousBlock.endMinute, block.startHour, block.startMinute));
            previousBlock = block;
        }
        setComplement.add(new TimeBlock(previousBlock.endHour, previousBlock.endMinute, MAX_HOUR, MAX_MINUTE));
        
        // Remove time blocks that have same start and end time
        for (TimeBlock block : setComplement) {
            if ((block.startHour == block.endHour) && (block.startMinute == block.endMinute)) {
                setComplement.remove(block);
            }
        }
        
        return setComplement;
    }
    
    /**
     * This method overrides the toString method of Object, and returns the 
     * time block represented as a String (e.g. 12:30-17:30).
     */
    @Override
    public String toString(){
        String startMin = ((startMinute < 10) ? "0" : "") + startMinute;
        String endMin = ((endMinute < 10) ? "0" : "") + endMinute;
        return startHour + ":" + startMin + "-" + endHour + ":" + endMin;
    }
}
