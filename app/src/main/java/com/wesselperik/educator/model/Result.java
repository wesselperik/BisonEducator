package com.wesselperik.educator.model;

/**
 * Created by Wessel on 24-5-2017.
 */

public class Result {

    private String name;
    private int grade;
    private int attempt;

    public Result(String name, int grade, int attempt) {
        this.name = name;
        this.grade = grade;
        this.attempt = attempt;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public int getAttempt() {
        return attempt;
    }
}
