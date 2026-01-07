package com.example.geometrycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutDescription = findViewById(R.id.aboutDescription);
        Button backButton = findViewById(R.id.backButton);

        // Устанавливаем черный цвет текста
        aboutDescription.setTextColor(getResources().getColor(android.R.color.black));
        backButton.setTextColor(getResources().getColor(android.R.color.black));

        // Устанавливаем текст с информацией об авторе
        String authorName = getString(R.string.author_name);
        String authorGroup = getString(R.string.author_group);
        String description = getString(R.string.about_description, authorName, authorGroup);
        aboutDescription.setText(description);

        // Кнопка "Назад"
        backButton.setOnClickListener(v -> finish());
    }
}