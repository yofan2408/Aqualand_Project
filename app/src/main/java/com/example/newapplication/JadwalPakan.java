package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JadwalPakan extends AppCompatActivity {

    EditText txtjam, txtmenit, txtdetik, txtjam2, txtmenit2, txtdetik2;
    Button buat;
    DatabaseReference reff;
    Jadwal jadwal;

    TextView jam, menit, detik, jam2, menit2, detik2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_pakan);

        findViewById(R.id.ket_jadwal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJadwalDialog();
            }
        });

        txtjam = (EditText) findViewById(R.id.jam);
        txtmenit = (EditText) findViewById(R.id.menit);
        txtdetik = (EditText) findViewById(R.id.detik);

        txtjam2 = (EditText) findViewById(R.id.jam2);
        txtmenit2 = (EditText) findViewById(R.id.menit2);
        txtdetik2 = (EditText) findViewById(R.id.detik2);

        jam = (TextView) findViewById(R.id.setjam);
        menit = (TextView) findViewById(R.id.setmenit);
        detik = (TextView) findViewById(R.id.setdetik);

        jam2 = (TextView) findViewById(R.id.setjam2);
        menit2 = (TextView) findViewById(R.id.setmenit2);
        detik2 = (TextView) findViewById(R.id.setdetik2);

        buat = (Button) findViewById(R.id.buatjadwal);

        jadwal=new Jadwal();

        reff= FirebaseDatabase.getInstance().getReference().child("jadwal");
        buat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jadwal.setJam1(txtjam.getText().toString());
                jadwal.setMenit1(txtmenit.getText().toString());
                jadwal.setDetik1(txtdetik.getText().toString());

                jadwal.setJam2(txtjam2.getText().toString());
                jadwal.setMenit2(txtmenit2.getText().toString());
                jadwal.setDetik2(txtdetik2.getText().toString());

                reff.setValue(jadwal);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("confir");

                ref.setValue(String.valueOf(1));

            }
        });

        reff= FirebaseDatabase.getInstance().getReference().child("jadwal");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String vjam=dataSnapshot.child("jam1").getValue().toString();
                String vmenit=dataSnapshot.child("menit1").getValue().toString();
                String vdetik=dataSnapshot.child("detik1").getValue().toString();

                String vjam2=dataSnapshot.child("jam2").getValue().toString();
                String vmenit2=dataSnapshot.child("menit2").getValue().toString();
                String vdetik2=dataSnapshot.child("detik2").getValue().toString();

                jam.setText(vjam+" -");
                menit.setText(vmenit+" -");
                detik.setText(vdetik);

                jam2.setText(vjam2+" -");
                menit2.setText(vmenit2+" -");
                detik2.setText(vdetik2);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Kembalijadwal (View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showJadwalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JadwalPakan.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(JadwalPakan.this).inflate(
                R.layout.layout_jadwal_pakan_dialog,
                (ConstraintLayout) findViewById(R.id.djadwal)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.jadwal_title));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.ket_jadwal));
        ((Button) view.findViewById(R.id.btn_action_jadwal)).setText(getResources().getString(R.string.o_jadwal));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_jadwal_dialog);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btn_action_jadwal).setOnClickListener(new View.OnClickListener() {
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