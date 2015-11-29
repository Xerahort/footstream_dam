package com.dam.data;

public class ClassificationPosition {

    public static final String POSITION = "position";
    public static final String NAME = "teamName";
    public static final String PLAYED_GAMES = "playedGames";
    public static final String POINTS = "points";
    public static final String GOALS = "goals";
    public static final String GAME_WINS = "wins";
    public static final String GAME_DRAWS = "draws";
    public static final String GAME_LOSSES = "losses";

    private int position;
    private String name;
    private int playedGames;
    private int points;
    private int goals;
    private int wins;
    private int draws;
    private int losses;

    private Team team;

    public ClassificationPosition() { }

    public ClassificationPosition(int position, String name, int points, int goals) {
        this.position = position;
        this.name = name;
        this.points = points;
        this.goals = goals;
    }

    public int getPosition() { return position; }
    public String getName() { return name; }
    public int getPlayedGames() { return playedGames; }
    public int getPoints() { return points; }
    public int getGoals() { return goals; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public Team getTeam() { return team; }

    public void setPosition(int position) { this.position = position; }
    public void setName(String name) { this.name = name; }
    public void setPlayedGames(int playedGames) { this.playedGames = playedGames; }
    public void setPoints(int points) { this.points = points; }
    public void setGoals(int goals) { this.goals = goals; }
    public void setWins(int wins) { this.wins = wins; }
    public void setDraws(int draws) { this.draws = draws; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setTeam(Team team) { this.team = team; }

}
