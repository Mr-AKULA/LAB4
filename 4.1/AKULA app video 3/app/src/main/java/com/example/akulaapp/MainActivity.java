package com.example.akulaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText Number_fild_1,Number_fild_2;
    private Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultTextView = findViewById(R.id.resultTextView);
        Number_fild_1 = findViewById(R.id.Number_fild_1);
        Number_fild_2 = findViewById(R.id.Number_fild_2);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float num1 = Float.parseFloat(Number_fild_1.getText().toString());
                float num2 = Float.parseFloat(Number_fild_2.getText().toString());
                float res = num1 + num2;
                resultTextView.setText(String.valueOf(res));


            }
        });
    }

    // Событие обработки при какой-либо активности в приложении
    @Override
    protected void onStart() {
        super.onStart();
    }

    //    При сворачивании приложения
    @Override
    protected void onPause() {
        super.onPause();
    }

    //    Логика выхода из приложения
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}