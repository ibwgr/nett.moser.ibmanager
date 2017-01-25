package controller;

/**
 * Beschreibt eine externe Applikation mit Name ,Nummer und aktuellem Status
 * Status: RUNNING, FINISH, ERROR, TERMINATED
 *
 * Created by Nett on 31.12.2016.
 * @author Nett
 */
public class ApplicationStatus {

    private String applName;
    private int number;
    private AppInfo actState;

    public ApplicationStatus(String applName, int number, AppInfo actState) {
        this.applName = applName;
        this.number = number;
        this.actState = actState;
    }

    public String getApplName() {
        return applName;
    }

    public int getNumber() {
        return number;
    }

    public AppInfo getActState() {
        return actState;
    }
}
