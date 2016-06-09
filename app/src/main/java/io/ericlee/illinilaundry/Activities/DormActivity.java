package io.ericlee.illinilaundry.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.ericlee.illinilaundry.R;

public class DormActivity extends AppCompatActivity {

    private String name;
    private int wash;
    private int dry;
    private int inWash;
    private int inDry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dorm);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        wash = intent.getIntExtra("Wash", 99);
        dry = intent.getIntExtra("Dry", 99);
        inWash = intent.getIntExtra("InWash", 99);
        inDry = intent.getIntExtra("InDry", 99);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_dorm);
        toolbar.setTitle(name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
