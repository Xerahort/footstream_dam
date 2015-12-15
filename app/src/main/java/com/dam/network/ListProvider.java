package com.dam.network;

/**
 * Created by JRB on 01/12/2015.
 */

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.dam.data.ClassificationPosition;
import com.dam.footstream.ClasificacionActivity;
import com.dam.footstream.R;

import java.util.ArrayList;
import java.util.List;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class ListProvider implements RemoteViewsFactory {
    private List<ClassificationPosition> listItemList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;

    private ClassificationPosition[] data;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        //populateListItem();

        DownloadClassification downloadTask = new DownloadClassification(AppWidgetManager.getInstance(context),this,context);
        downloadTask.execute(ClasificacionActivity.LEAGUE_ID);

    }

    public void dataLoaded(List<ClassificationPosition> classification) {
        data = new ClassificationPosition[classification.size()];
        listItemList = classification;
    }

    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            ClassificationPosition item = new ClassificationPosition(i, "Team ", "id " + i, i, i);
            //ClassificationPosition item = new ClassificationPosition(ClasificacionActivity.data[i].getPosition(), ClasificacionActivity.data[i].getName(), ClasificacionActivity.data[i].getPoints(), ClasificacionActivity.data[i].getGoals());
            listItemList.add(item);
        }

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.position_classification_slim);
        ClassificationPosition item = listItemList.get(position);
        remoteView.setTextViewText(R.id.classification_position_slim, String.valueOf(item.getPosition()));
        remoteView.setTextViewText(R.id.classification_points_slim, String.valueOf(item.getPoints()));
        remoteView.setTextViewText(R.id.classification_goals_slim, String.valueOf(item.getGoals()));
        remoteView.setTextViewText(R.id.classification_name_slim, item.getName());

        //Intent fillInIntent = new Intent(context, ClasificacionActivity.class);
        //remoteView.setOnClickFillInIntent(R.id.classification_name, fillInIntent);

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

//    private class ListItem {
//        public String pos,points,goals,team;
//
//    }
}