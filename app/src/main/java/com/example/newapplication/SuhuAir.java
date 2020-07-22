package com.example.newapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SuhuAir extends AppCompatActivity {

    TextView suhu;
    DatabaseReference dref;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu_air);

        findViewById(R.id.ket_suhu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuhuDialog();
            }
        });

        suhu = (TextView) findViewById(R.id.suhu);
        dref= FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status=dataSnapshot.child("suhu").getValue().toString();
                suhu.setText(status+" C");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Kembalisuhu (View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void finish() {
        super.finish();
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
        if(alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}