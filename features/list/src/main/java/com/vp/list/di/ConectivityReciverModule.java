package com.vp.list.di;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.vp.list.viewmodel.ListViewModel;

public class ConectivityReciverModule extends BroadcastReceiver {
    private ListViewModel listViewModel;
    private String currentQuery;

    public ConectivityReciverModule(ListViewModel listViewModel, String currentQuery) {
        this.listViewModel = listViewModel;
        this.currentQuery = currentQuery;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (isConnected(context))
            {
                Toast.makeText(context, "device now is connected !", Toast.LENGTH_LONG).show();
                listViewModel.searchMoviesByTitle(currentQuery, 1);
            }
            else
            {
                Toast.makeText(context, "NO Internet connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                (
                        activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE
                );
    }
}
