package com.nagarro.todo.View;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nagarro.todo.R;

public class NetworkErrorActivity extends AppCompatActivity {
    Button reloadButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);
        reloadButton = findViewById(R.id.reloadButton);
        ConnectivityManager myConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo myNetworkInfo = myConnectivityManager.getActiveNetworkInfo();
                if(myNetworkInfo !=null && myNetworkInfo.isConnected()) {
                    finish();
                }
            }
        });
    }
}
