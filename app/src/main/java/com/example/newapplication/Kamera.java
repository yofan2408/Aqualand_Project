package com.example.newapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Kamera extends AppCompatActivity {

    Button b2;

    private TextView txt;
    private ImageView img;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference reference=firebaseDatabase.getReference();
    private DatabaseReference childernreff=reference.child("esp32-cam");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamera);

        b2 = findViewById(R.id.button2);

        findViewById(R.id.ket_kamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKameraDialog();
            }
        });

        txt=findViewById(R.id.txtkamera);
        img=findViewById(R.id.imgkamera);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("cap");
                myRef.setValue(String.valueOf(1));

                childernreff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String message=snapshot.child("photo").getValue(String.class);
                        txt.setText(message);

                        if(!message.isEmpty()) {
                            Picasso.get().load(message).into(img);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    public void Kembalikamera (View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showKameraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Kamera.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Kamera.this).inflate(
                R.layout.layout_kamera_dialog,
                (ConstraintLayout) findViewById(R.id.dkamera)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.kamera_title));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.ket_kamera));
        ((Button) view.findViewById(R.id.btn_action_kamera)).setText(getResources().getString(R.string.o_kamera));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_kamera_dialog);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btn_action_kamera).setOnClickListener(new View.OnClickListener() {
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
}
