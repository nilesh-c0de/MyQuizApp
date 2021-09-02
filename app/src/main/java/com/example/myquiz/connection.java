package com.example.myquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class connection extends AppCompatActivity {

    CoordinatorLayout layout;
    TextView retry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection);

        layout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        retry = (TextView) findViewById(R.id.text_retry);

        Snackbar snackbar = Snackbar.make(layout, "No internet connection", Snackbar.LENGTH_INDEFINITE);
        View snackView = snackbar.getView();
        final TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedToInternet()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }

    private boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) ||
                (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }
}
