package com.upc.yourlivestock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        TextView viewClientName = (TextView) findViewById(R.id.viewClientName);
        String clientName = getIntent().getExtras().getString("clientName");
        viewClientName.setText(clientName);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnWelcomeRegister:
                Intent intentRegister = new Intent(WelcomeActivity.this,LivestockActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.btnExit:
                Intent intentExit = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intentExit);
                break;
        }
    }
}
