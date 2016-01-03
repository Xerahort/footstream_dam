package com.dam.network;

import android.os.AsyncTask;
import android.util.Log;

import com.dam.footstream.MainActivity;

import java.util.ArrayList;
import java.util.Locale;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by felix on 30.12.15.
 */
public class TwitterTask extends AsyncTask<String, Void, ArrayList<twitter4j.Status>> {

    private MainActivity activity;
    private ConfigurationBuilder cb;
    private static final int NUMBER_OF_TWEETS = 3;
    private String teamName;

    public TwitterTask(MainActivity activity, String teamName) {
        this.activity = activity;
        this.teamName = teamName;

        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("IJzDt76DQwuQdgmRIEHknnk1z")
                .setOAuthConsumerSecret("HBswREX4LlY2K5TIHC64GPnjPINVSrZeaDECx6UDufT0CywHhg")
                .setOAuthAccessToken("378827574-SOgrOlK9GGe5HWoG7DjQpAVo1PDto37qeolW2T7R")
                .setOAuthAccessTokenSecret("4dMIQSjXXB4sfusBKi8mvEo91yje5sGGmrNaYHXLA5e7r");
    }

    @Override
    protected ArrayList<twitter4j.Status> doInBackground(String... params) {
        String searchText = params[0];
        ArrayList<twitter4j.Status> tweets = new ArrayList<>();

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query("\"" + searchText + "\"");
        query.setLang(Locale.getDefault().getLanguage()); // search for tweets in the default system language
        query.setCount(NUMBER_OF_TWEETS);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        if (result != null)
            for (twitter4j.Status status : result.getTweets()) {
                Log.v("TwitterTask", "@" + status.getUser().getScreenName() + ":" + status.getText() + "\t" + status.getCreatedAt());
                tweets.add(status);
            }

        return tweets;
    }

    @Override
    protected void onPostExecute(ArrayList<twitter4j.Status> tweets) {
        super.onPostExecute(tweets);
        activity.twitterDataLoaded(tweets, teamName);
    }

}
