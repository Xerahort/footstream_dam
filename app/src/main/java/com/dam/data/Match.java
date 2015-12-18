package com.dam.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Felix on 07.12.2015.
 */
public class Match {

    private Date date;
    private Team homeTeam;
    private Team awayTeam;
    private String status;
    private String matchday;
    private String goalsHomeTeam;
    private String goalsAwayTeam;


    public void setDate(Date date) {
        this.date = date;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMatchday(String matchday) {
        this.matchday = matchday;
    }

    public void setGoalsHomeTeam(String goalsHomeTeam) {
        this.goalsHomeTeam = goalsHomeTeam;
    }

    public void setGoalsAwayTeam(String goalsAwayTeam) {
        this.goalsAwayTeam = goalsAwayTeam;
    }


    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public String getStatus() {
        return status;
    }

    public String getMatchday() {
        return matchday;
    }

    public String getGoalsHomeTeam() {
        if(goalsHomeTeam.equals("null")) return "";
        else return goalsHomeTeam;
    }

    public String getGoalsAwayTeam() {
        if(goalsAwayTeam.equals("null")) return "";
        return goalsAwayTeam;
    }

    public String getDateText() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm E dd/MM/yyyy");
        return format.format(date);
    }

    public boolean isFinished() {
        return status.equals("FINISHED");
    }

    public Date getDate() {
        return date;
    }

    /**
     * Returns the date of the match, setting the time to 0:00. Needed for the HashMap of the calendar data.
     * @return
     */
    public Date getDateWithoutTime() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return new GregorianCalendar(c.get(GregorianCalendar.YEAR),c.get(GregorianCalendar.MONTH),c.get(GregorianCalendar.DAY_OF_MONTH)).getTime();
    }

    @Override
    public String toString() {
        return "Match{" +
                "date=" + date +
                ", homeTeam=" + homeTeam +
                ", awayTeam=" + awayTeam +
                ", status='" + status + '\'' +
                ", matchday='" + matchday + '\'' +
                ", goalsHomeTeam='" + goalsHomeTeam + '\'' +
                ", goalsAwayTeam='" + goalsAwayTeam + '\'' +
                '}';
    }
}
