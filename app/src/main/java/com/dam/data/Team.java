package com.dam.data;

import java.util.ArrayList;

/**
 * Created by Felix on 17.11.2015.
 */
public class Team {

    public static final String NAME = "name";
    public static final String SHORT_NAME = "shortName";
    public static final String MARKET_VALUE = "squadMarketValue";
    public static final String EMBLEM_URL = "crestUrl";

    private String id;  //id de la API football-data
    private String name;
    private String short_name;
    private String market_value;
    private String emblem_url;
    private ArrayList<Match> matches = new ArrayList<>();

    public Team() {

    }

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setMarket_value(String market_value) {
        this.market_value = market_value;
    }

    public void setEmblem_url(String emblem_url) {
        this.emblem_url = emblem_url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }

    public String getMarket_value() {
        return market_value;
    }

    public String getEmblem_url() {
        return emblem_url;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }


}
