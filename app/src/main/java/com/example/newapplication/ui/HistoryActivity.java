package com.example.newapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.newapplication.R;
import com.example.newapplication.adapter.HistoryAdapter;
import com.example.newapplication.model.History;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    FloatingActionButton fab;

    public static ArrayList<History> historyList = new ArrayList<>();
    public static HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        RecyclerView rvHistory = findViewById(R.id.rv_history);
        fab = findViewById(R.id.fab_delete);


        rvHistory.setHasFixedSize(true);

        adapter = new HistoryAdapter(historyList);
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab_delete) {
                    historyList.clear();
                }
            }
        });

    }
}
