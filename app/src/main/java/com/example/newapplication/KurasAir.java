package com.example.newapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KurasAir extends AppCompatActivity {

    TextView t1, t2;
    Button b1;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuras_air);

        findViewById(R.id.ket_kuras).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKurasDialog();
            }
        });

        t1 = findViewById(R.id.countdown_txt);
        t2 = findViewById(R.id.countdown2_txt);
        b1 = findViewById(R.id.button);

        countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                t1.setText(millisUntilFinished/1000 + " Detik");
            }

            @Override
            public void onFinish() {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("m_kuras");
//                myRef.setValue(String.valueOf(0));
//
//                t2.setText("Katup Tertutup");
//                t1.setText("0 Detik");
            }
        };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("m_kuras");
                myRef.setValue(String.valueOf(1));
                countDownTimer.start();
                t2.setText("Katup Terbuka");
            }
        });


    }

    public void Kembalikuras (View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showKurasDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(KurasAir.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(KurasAir.this).inflate(
                R.layout.layout_kuras_dialog,
                (ConstraintLayout) findViewById(R.id.dkuras)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText(getResources().getString(R.string.kuras_title));
        ((TextView) view.findViewById(R.id.textMessage)).setText(getResources().getString(R.string.ket_kuras));
        ((Button) view.findViewById(R.id.btn_action_kuras)).setText(getResources().getString(R.string.o_kuras));
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_kuras_dialog);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btn_action_kuras).setOnClickListener(new View.OnClickListener() {
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