package com.feezrook.Paracart.Pets;

import java.util.ArrayList;

public class SettingLevel {
    private int col;
    private int row;
    private int show;
    private int step;
    private int time;

    public SettingLevel(int col, int row, int show, int step, int time) {
        this.col = col;
        this.row = row;
        this.show = show;
        this.step = step;
        this.time = time*1000;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getShow() {
        return show;
    }

    public int getStep() {
        return step;
    }

    public int getTime() {
        return time;
    }

    public static ArrayList<SettingLevel> getArrayLevels() {
        ArrayList<SettingLevel> arrayLevels = new ArrayList<SettingLevel>(12);
        arrayLevels.add(new SettingLevel(2,3,1,999999,9999999));
        arrayLevels.add(new SettingLevel(2,3,1,6,9999999));
        arrayLevels.add(new SettingLevel(2,3,1,9999999,8));
        arrayLevels.add(new SettingLevel(2,3,1,7,10));
        arrayLevels.add(new SettingLevel(4,6,2,9999999,9999999));
        arrayLevels.add(new SettingLevel(4,6,2,30,9999999));
        arrayLevels.add(new SettingLevel(4,6,2,9999999,60));
        arrayLevels.add(new SettingLevel(4,6,2,35,70));
        arrayLevels.add(new SettingLevel(5,8,4,9999999,9999999));
        arrayLevels.add(new SettingLevel(5,8,4,65,9999999));
        arrayLevels.add(new SettingLevel(5,8,4,9999999,120));
        arrayLevels.add(new SettingLevel(5,8,4,70,130));
        return arrayLevels;
    }
}
