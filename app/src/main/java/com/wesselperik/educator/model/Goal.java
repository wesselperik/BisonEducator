package com.wesselperik.educator.model;

/**
 * Created by Wessel on 24-5-2017.
 */

public class Goal {

    private String name;
    private int progress;
    private String achieved;

    public Goal(String name, int progress, String achieved) {
        this.name = name;
        this.progress = progress;
        this.achieved = achieved;
    }

    public String getName() {
        return name;
    }

    public int getProgress() {
        return progress;
    }

    public String getAchieved() {
        return achieved;
    }
}
