package com.example.sudokumaster.sudoku;

public enum CharacterSet {
    Greek(960, 945, 946, 948, 949, 952, 956, 968, 937, "Greek Letters"),
    English(65, 66, 67, 68, 69, 70, 71, 72, 73, "English Letters"),
    Number(49, 50, 51, 52, 53, 54, 55, 56, 57, "Numbers");

    private final char first;
    private final char second;
    private final char third;
    private final char fourth;
    private final char fifth;
    private final char sixth;
    private final char seventh;
    private final char eighth;
    private final char ninth;
    private final String name;

    CharacterSet(int first, int second, int third, int fourth, int fifth, int sixth, int seventh, int eighth, int ninth, String name) {
        this.first = (char)first;
        this.second = (char)second;
        this.third = (char)third;
        this.fourth = (char)fourth;
        this.fifth = (char)fifth;
        this.sixth = (char)sixth;
        this.seventh = (char)seventh;
        this.eighth = (char)eighth;
        this.ninth = (char)ninth;
        this.name = name;
    }

    public char getText(int index) {
        switch (index) {
            case 1:
                return first;
            case 2:
                return second;
            case 3:
                return third;
            case 4:
                return fourth;
            case 5:
                return fifth;
            case 6:
                return sixth;
            case 7:
                return seventh;
            case 8:
                return eighth;
            case 9:
                return ninth;
            default:
                return ' ';
        }
    }

    public String getName() {return name;}
}
