package sample;

import java.util.Comparator;

public class StopSorter implements Comparator<Stop> {

    public int compare(Stop stop1, Stop stop2){
        return stop1.compareTo(stop2);
    }

}