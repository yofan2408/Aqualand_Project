package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newapplication.formatter.MyValueFormatter;
import com.example.newapplication.formatter.MyXAxisFormatter;
import com.example.newapplication.model.History;
import com.example.newapplication.ui.HistoryActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SuhuAir extends AppCompatActivity {


    TextView suhu;
    DatabaseReference dref;
    String status;
    Button btnRiwayat, btnHapus;


    LineChart lineChart;
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> iLineDataSets;
    LineData lineData;
    SimpleDateFormat simpleDateFormat;


    static Float degree = 0f;
    static String date = "";
    public static String degreePattern = (char) (+0x00B0) + "C";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu_air);

        // find id
        btnRiwayat = findViewById(R.id.btn_history);
        btnHapus = findViewById(R.id.btn_delete);
        suhu = findViewById(R.id.tv_suhu);
        lineChart = findViewById(R.id.line_chart);

        // instance date
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

        // instance dataSet
        lineDataSet = new LineDataSet(null, null);
        iLineDataSets = new ArrayList<>();
        lineData = new LineData();

        // instance formatter
        lineChart.getAxisLeft().setValueFormatter(new MyValueFormatter());
        lineDataSet.setValueFormatter(new MyValueFormatter());
        lineChart.getXAxis().setValueFormatter(new MyXAxisFormatter());

        // set chart Style
        lineChart.setNoDataTextColor(Color.WHITE);
        lineChart.setDrawBorders(true);
        lineChart.setScaleYEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setHighlightLineWidth(1f);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleHoleColor(Color.BLACK);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.WHITE);


        dref = FirebaseDatabase.getInstance().getReference("suhu");


        findViewById(R.id.ket_suhu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuhuDialog();
            }
        });


        // history
        btnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_history) {

                    dref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot myDataSnapshot : snapshot.getChildren()) {
                                degree = myDataSnapshot.getValue(Float.class);
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                            String currentDate = sdf.format(new Date());
                            date = currentDate;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    History history = createHistory(degree, date);
                    HistoryActivity.historyList.add(history);

                    Intent intent = new Intent(SuhuAir.this, HistoryActivity.class);
                    startActivity(intent);
                }
            }
        });


        // hapus data
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_delete) {
                    dref.removeValue();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Entry> dataVals = new ArrayList<>();
                int xAxis = 0;

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                        float dataPoint = myDataSnapshot.getValue(Float.class);
                        float yVal = (float) dataPoint;


                        dataVals.add(new Entry((float) xAxis, yVal));


                        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                String detail = "Suhu " + e.getY() + degreePattern;
                                Toast.makeText(SuhuAir.this, detail, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNothingSelected() {
                            }
                        });

                        xAxis++;
                    }

                    showChart(dataVals);
                } else {
                    lineChart.clear();
                    dataSnapshot.getRef().removeValue();
                    lineChart.invalidate();
                }


//                status = dataSnapshot.child("suhu").getValue().toString();
//                suhu.setText(status + " C");
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                lineChart.clear();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        dref.removeValue();
    }


    private void showChart(ArrayList<Entry> dataVals) {
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Data Suhu");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


    private History createHistory(Float suhu, String waktu) {
        History his = new History(waktu, suhu);
        return his;
    }


    public void Kembalisuhu(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void showSuhuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SuhuAir.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(SuhuAir.this).inflate(
                R.layout.layout_suhu_dialog,
                (ConstraintLayout) findViewById(R.id.dsuhu)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.suhu_title));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.ket_suhu));
        ((Button) view.findViewById(R.id.btn_action_suhu)).setText(getResources().getString(R.string.o_suhu));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_suhu_dialog);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btn_action_suhu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}
