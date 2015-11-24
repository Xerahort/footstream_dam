package com.dam.network;


import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Felix on 17.11.2015.
 */
public abstract class NetworkTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {

    static final String HTTP = "http://";
    static final String HOST = "api.football-data.org";
    static final String PATH = "/v1/";
    static final String FOOTBALL_DATA_HEADER_FIELD = "X-Auth-Token";
    static final String FOOTBALL_DATA_API_TOKEN = "9f90b4e7721b41c58a1f77a4cfa20e0a";

    /**
     * Sends a request to the given URL and returns the network-connection from which the result can be fetched out.
     *
     * @param url the URL that should be contacted
     * @return the connection containing the response of the request
     * @throws IOException if there is a problem with the connection, there is thrown a IOException
     */
    public HttpURLConnection sendRequest(URL url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (url.toString().contains("football-data"))
                // contacting to football-data => put API-token into HTTP header
                connection.setRequestProperty(FOOTBALL_DATA_HEADER_FIELD, FOOTBALL_DATA_API_TOKEN);

        } catch (IOException e) {
            // throw Exception
            throw e;
        } finally {
            if (connection != null)
                // disconnect
                connection.disconnect();
        }
        return connection;
    }

    /**
     * Reads the response from the given connection and returns the response as a String
     *
     * @param connection the HTTP connection to the server
     * @return a String containing the response of the server
     * @throws IOException
     */
    public String readResponse(HttpURLConnection connection) throws IOException {
        String response;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            response = in.readLine();
            in.close();
        } catch (IOException e) {
            // throw Exception
            throw e;
        } finally {
            if (connection != null)
                // disconnect
                connection.disconnect();
        }
        return response;
    }

}
