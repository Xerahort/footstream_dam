package com.dam.data;

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
}
