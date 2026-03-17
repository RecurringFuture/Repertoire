package com.recurringfuture.repository.data;

import java.util.List;

public class RepertoireData {

    public static List<String> getKeys() {
        return List.of("C", "C#", "Db", "D", "D#", "Eb", "E", "F", "F#", "Gb", "G" , "G#", "Ab", "A", "A#", "Bb", "B");
    }

    public static List<String> getStates() {
        return List.of("State", "ToDo", "In Progress", "Repertoire");
    }

    public static List<String> getCapoPositions() {
        return List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
    }

    public static List<String> getThresholds() {
        return List.of("1 Day", "2 Days", "3 Days", "4 Days", "5 Days", "1 Week", "2 Weeks", "3 Weeks", "1 Month", "2 Months", "3 Months");
    }
}
