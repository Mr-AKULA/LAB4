package com.example.geometrycalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Spinner figureSpinner;
    private LinearLayout inputFieldsContainer;
    private TextView figureImage;
    private TextView figureDimension;
    private Button calculateButton;
    private Button aboutButton;
    private LinearLayout resultsContainer;
    private TextView errorText;

    // Массив фигур с названиями, типами, эмодзи и единицами измерения
    private String[][] figures = {
            // {тип, название, эмодзи, единицы измерения}
            {"square", "Квадрат", "⬜", "см"},
            {"rectangle", "Прямоугольник", "▭", "см"},
            {"triangle", "Треугольник", "△", "см"},
            {"parallelogram", "Параллелограмм", "▱", "см"},
            {"trapezoid", "Трапеция", "⏢", "см"},
            {"rhombus", "Ромб", "◆", "см"},
            {"circle", "Окружность", "⭕", "см"},
            {"cube", "Куб", "⬛", "см"},
            {"parallelepiped", "Параллелепипед", "▣", "см"},
            {"cone", "Конус", "🔺", "см"},
            {"pyramid", "Пирамида", "🔶", "см"},
            {"cylinder", "Цилиндр", "🛢️", "см"},
            {"sphere", "Сфера", "⚪", "см"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupSpinner();
        setupListeners();
    }

    private void initViews() {
        figureSpinner = findViewById(R.id.figureSpinner);
        inputFieldsContainer = findViewById(R.id.inputFieldsContainer);
        figureImage = findViewById(R.id.figureImage);
        figureDimension = findViewById(R.id.figureDimension);
        calculateButton = findViewById(R.id.calculateButton);
        aboutButton = findViewById(R.id.aboutButton);
        resultsContainer = findViewById(R.id.resultsContainer);
        errorText = findViewById(R.id.errorText);

        // Устанавливаем черный цвет текста
        figureImage.setTextColor(getResources().getColor(android.R.color.black));
        figureDimension.setTextColor(getResources().getColor(android.R.color.black));
        errorText.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void setupSpinner() {
        // Создаем адаптер с названиями фигур
        String[] figureNames = new String[figures.length];
        for (int i = 0; i < figures.length; i++) {
            figureNames[i] = figures[i][1];
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, figureNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        figureSpinner.setAdapter(adapter);

        figureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateInputFields(position);
                clearResults();
                errorText.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupListeners() {
        calculateButton.setOnClickListener(v -> calculate());
        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void updateInputFields(int figureIndex) {
        // Очищаем контейнер
        inputFieldsContainer.removeAllViews();

        if (figureIndex < 0 || figureIndex >= figures.length) {
            figureImage.setText("");
            figureDimension.setText("");
            calculateButton.setVisibility(View.GONE);
            return;
        }

        // Устанавливаем эмодзи и тип измерения
        figureImage.setText(figures[figureIndex][2]);
        figureDimension.setText("Единицы измерения: " + figures[figureIndex][3]);

        // Показываем кнопку расчета
        calculateButton.setVisibility(View.VISIBLE);

        // Создаем поля ввода в зависимости от выбранной фигуры
        switch (figures[figureIndex][0]) {
            case "square":
                addInputField("side", "Сторона (a)", "Введите длину стороны");
                break;

            case "rectangle":
                addInputField("length", "Длина (a)", "Введите длину");
                addInputField("width", "Ширина (b)", "Введите ширину");
                break;

            case "triangle":
                addInputField("sideA", "Сторона a", "Введите сторону a");
                addInputField("sideB", "Сторона b", "Введите сторону b");
                addInputField("sideC", "Сторона c", "Введите сторону c");
                addInputField("height", "Высота (h)", "Введите высоту");
                addNoteTextView("Примечание: Каждая сторона должна быть меньше суммы двух других");
                break;

            case "parallelogram":
                addInputField("base", "Основание (a)", "Введите основание");
                addInputField("side", "Сторона (b)", "Введите сторону");
                addInputField("height", "Высота (h)", "Введите высоту к основанию a");
                addNoteTextView("Примечание: Высота h не может быть больше боковой стороны b");
                break;

            case "trapezoid":
                addInputField("baseA", "Верхнее основание (a)", "Введите основание a");
                addInputField("baseB", "Нижнее основание (b)", "Введите основание b");
                addInputField("height", "Высота (h)", "Введите высоту");
                addInputField("sideC", "Боковая сторона (c)", "Введите боковую сторону");
                addNoteTextView("Примечание: Для равнобедренной трапеции h < c");
                break;

            case "rhombus":
                addInputField("side", "Сторона (a)", "Введите сторону");
                addInputField("diagonal1", "Диагональ d₁", "Введите диагональ 1");
                addInputField("diagonal2", "Диагональ d₂", "Введите диагональ 2");
                addInputField("height", "Высота (h)", "Введите высоту");
                addNoteTextView("Примечание: Диагонали не могут быть больше 2a, высота h < a");
                break;

            case "circle":
                addInputField("radius", "Радиус (r)", "Введите радиус");
                break;

            case "cube":
                addInputField("side", "Ребро (a)", "Введите длину ребра");
                break;

            case "parallelepiped":
                addInputField("length", "Длина (a)", "Введите длину");
                addInputField("width", "Ширина (b)", "Введите ширину");
                addInputField("height", "Высота (c)", "Введите высоту");
                break;

            case "cone":
                addInputField("radius", "Радиус основания (r)", "Введите радиус основания");
                addInputField("height", "Высота (h)", "Введите высоту");
                break;

            case "pyramid":
                addInputField("baseSide", "Сторона основания (a)", "Введите сторону основания");
                addInputField("height", "Высота (h)", "Введите высоту");
                addNoteTextView("Основание: квадрат со стороной a");
                break;

            case "cylinder":
                addInputField("radius", "Радиус основания (r)", "Введите радиус основания");
                addInputField("height", "Высота (h)", "Введите высоту");
                break;

            case "sphere":
                addInputField("radius", "Радиус (r)", "Введите радиус");
                break;
        }
    }

    private void addInputField(String id, String label, String hint) {
        // Создаем TextView для метки
        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setTextSize(14);
        labelView.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        labelParams.setMargins(0, 16, 0, 8);
        labelView.setLayoutParams(labelParams);
        inputFieldsContainer.addView(labelView);

        // Создаем EditText для ввода
        EditText input = new EditText(this);
        input.setId(View.generateViewId());
        input.setTag(id); // Сохраняем идентификатор в теге
        input.setHint(hint);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // Устанавливаем черный цвет текста
        input.setTextColor(getResources().getColor(android.R.color.black));
        input.setHintTextColor(getResources().getColor(android.R.color.darker_gray));

        // Используем стандартный фон или создайте свой drawable
        if (getResources().getIdentifier("edit_text_border", "drawable", getPackageName()) != 0) {
            input.setBackgroundResource(R.drawable.edit_text_border);
        }

        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        inputParams.setMargins(0, 0, 0, 16);
        input.setLayoutParams(inputParams);
        inputFieldsContainer.addView(input);
    }

    private void addNoteTextView(String note) {
        TextView noteView = new TextView(this);
        noteView.setText(note);
        noteView.setTextSize(12);
        noteView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        noteView.setTypeface(null, android.graphics.Typeface.ITALIC);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 16);
        noteView.setLayoutParams(params);
        inputFieldsContainer.addView(noteView);
    }

    private void calculate() {
        int figureIndex = figureSpinner.getSelectedItemPosition();

        if (figureIndex < 0 || figureIndex >= figures.length) {
            showError("⚠️ Пожалуйста, выберите фигуру");
            return;
        }

        try {
            // Собираем значения из полей ввода
            java.util.HashMap<String, Double> values = new java.util.HashMap<>();
            boolean hasError = false;

            for (int i = 0; i < inputFieldsContainer.getChildCount(); i++) {
                View child = inputFieldsContainer.getChildAt(i);
                if (child instanceof EditText) {
                    EditText input = (EditText) child;
                    String tag = (String) input.getTag();
                    String valueStr = input.getText().toString().trim();

                    if (valueStr.isEmpty()) {
                        hasError = true;
                        input.setBackgroundResource(R.drawable.edit_text_error);
                        continue;
                    }

                    try {
                        double value = Double.parseDouble(valueStr);
                        if (value <= 0) {
                            hasError = true;
                            input.setBackgroundResource(R.drawable.edit_text_error);
                        } else {
                            values.put(tag, value);
                            // Сбрасываем фон на нормальный
                            if (getResources().getIdentifier("edit_text_border", "drawable", getPackageName()) != 0) {
                                input.setBackgroundResource(R.drawable.edit_text_border);
                            }
                        }
                    } catch (NumberFormatException e) {
                        hasError = true;
                        input.setBackgroundResource(R.drawable.edit_text_error);
                    }
                }
            }

            if (hasError) {
                showError("⚠️ Пожалуйста, введите корректные положительные числа во все поля");
                return;
            }

            // Выполняем расчет в зависимости от фигуры
            String figureType = figures[figureIndex][0];
            java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();

            // Проверка валидации и расчет
            switch (figureType) {
                case "square":
                    results = calculateSquare(values);
                    break;
                case "rectangle":
                    results = calculateRectangle(values);
                    break;
                case "triangle":
                    results = calculateTriangle(values);
                    break;
                case "parallelogram":
                    results = calculateParallelogram(values);
                    break;
                case "trapezoid":
                    results = calculateTrapezoid(values);
                    break;
                case "rhombus":
                    results = calculateRhombus(values);
                    break;
                case "circle":
                    results = calculateCircle(values);
                    break;
                case "cube":
                    results = calculateCube(values);
                    break;
                case "parallelepiped":
                    results = calculateParallelepiped(values);
                    break;
                case "cone":
                    results = calculateCone(values);
                    break;
                case "pyramid":
                    results = calculatePyramid(values);
                    break;
                case "cylinder":
                    results = calculateCylinder(values);
                    break;
                case "sphere":
                    results = calculateSphere(values);
                    break;
            }

            // Показываем результаты
            showResults(results);

        } catch (IllegalArgumentException e) {
            showError("⚠️ " + e.getMessage());
        } catch (Exception e) {
            showError("⚠️ Ошибка расчета: " + e.getMessage());
        }
    }

    // Методы расчета для каждой фигуры с валидацией как в index.html
    private java.util.LinkedHashMap<String, Double> calculateSquare(java.util.HashMap<String, Double> v) {
        double side = v.get("side");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = 4a)", 4 * side);
        results.put("Площадь (S = a²)", side * side);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateRectangle(java.util.HashMap<String, Double> v) {
        double length = v.get("length");
        double width = v.get("width");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = 2(a+b))", 2 * (length + width));
        results.put("Площадь (S = a×b)", length * width);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateTriangle(java.util.HashMap<String, Double> v) {
        double sideA = v.get("sideA");
        double sideB = v.get("sideB");
        double sideC = v.get("sideC");

        // Исправление для API 23
        double height = 0.0;
        if (v.containsKey("height")) {
            height = v.get("height");
        }

        // Проверка неравенства треугольника (как в index.html)
        if (sideA >= sideB + sideC) {
            throw new IllegalArgumentException("Сторона a (" + sideA + ") должна быть меньше суммы b + c (" + (sideB + sideC) + ")");
        }
        if (sideB >= sideA + sideC) {
            throw new IllegalArgumentException("Сторона b (" + sideB + ") должна быть меньше суммы a + c (" + (sideA + sideC) + ")");
        }
        if (sideC >= sideA + sideB) {
            throw new IllegalArgumentException("Сторона c (" + sideC + ") должна быть меньше суммы a + b (" + (sideA + sideB) + ")");
        }

        // Проверка высоты
        if (height > 0) {
            if (height >= sideA || height >= sideB || height >= sideC) {
                throw new IllegalArgumentException("Высота h должна быть меньше всех сторон треугольника");
            }
        }

        double p = (sideA + sideB + sideC) / 2;
        double area = Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));

        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = a+b+c)", sideA + sideB + sideC);
        results.put("Площадь (формула Герона)", area);
        if (height > 0) {
            results.put("Высота h", height);
        }
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateParallelogram(java.util.HashMap<String, Double> v) {
        double base = v.get("base");
        double side = v.get("side");
        double height = v.get("height");

        // Проверка на наличие всех параметров
        if (!v.containsKey("base") || !v.containsKey("side") || !v.containsKey("height")) {
            throw new IllegalArgumentException("Необходимо указать все три параметра: основание (a), боковая сторона (b) и высота (h)");
        }

        // Проверка на положительность
        if (base <= 0 || side <= 0 || height <= 0) {
            throw new IllegalArgumentException("Все параметры должны быть положительными числами больше нуля");
        }

        // Проверка: высота не может быть больше стороны
        if (height > side) {
            throw new IllegalArgumentException(
                    String.format("Высота h (%.2f) не может быть больше боковой стороны b (%.2f)", height, side));
        }

        // Проверка на существование треугольника (высота, сторона, проекция)
        double sinAlpha = height / side;
        double cosAlpha = Math.sqrt(1 - sinAlpha * sinAlpha);
        double projection = side * cosAlpha;

        // Проверка на существование параллелограмма
        if (base < projection) {
            throw new IllegalArgumentException(
                    String.format("Параллелограмм с такими параметрами не существует.\n" +
                                    "Основание a=%.2f должно быть не меньше проекции боковой стороны ≈%.2f",
                            base, projection));
        }

        // ДОПОЛНИТЕЛЬНЫЕ ПРАКТИЧЕСКИЕ ПРОВЕРКИ:

        // 1. Проверка на слишком "сплющенные" фигуры (основание намного больше боковой стороны)
        if (base > 50 * side) {
            throw new IllegalArgumentException(
                    String.format("Основание (%.2f) слишком велико относительно боковой стороны (%.2f). " +
                                    "Соотношение a/b = %.1f. Рассмотрите другие значения.",
                            base, side, base/side));
        }

        // 2. Проверка на слишком "высокие" фигуры (угол близкий к 90°)
        if (height/side > 0.95) { // угол > ~72°
            throw new IllegalArgumentException(
                    String.format("Высота (%.2f) слишком близка к боковой стороне (%.2f). " +
                                    "Соотношение h/b = %.2f. Угол ≈ %.1f°",
                            height, side, height/side, Math.toDegrees(Math.asin(height/side))));
        }

        // 3. Проверка на слишком "плоские" фигуры (малый угол наклона)
        if (height/side < 0.1) { // угол < ~6°
            throw new IllegalArgumentException(
                    String.format("Параллелограмм слишком плоский. Высота (%.2f) очень мала относительно боковой стороны (%.2f). " +
                                    "Соотношение h/b = %.2f. Угол ≈ %.1f°",
                            height, side, height/side, Math.toDegrees(Math.asin(height/side))));
        }

        // 4. Проверка на разумное соотношение сторон
        double ratio = base / side;
        if (ratio < 0.1 || ratio > 10) {
            throw new IllegalArgumentException(
                    String.format("Соотношение основания к боковой стороне (a/b = %.2f) выходит за разумные пределы. " +
                                    "Рекомендуется: 0.2 ≤ a/b ≤ 5",
                            ratio));
        }

        // Вычисление результатов
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = 2(a+b))", 2 * (base + side));
        results.put("Площадь (S = a×h)", base * height);
        results.put("Высота h", height);
        results.put("Проекция боковой стороны", projection);
        results.put("Угол наклона, градусы", Math.toDegrees(Math.asin(sinAlpha)));

        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateTrapezoid(java.util.HashMap<String, Double> v) {
        double baseA = v.get("baseA");
        double baseB = v.get("baseB");
        double sideC = v.get("sideC");
        double height = v.get("height");

        // Проверки (как в index.html)
        if (baseA == baseB) {
            throw new IllegalArgumentException("Основания трапеции не должны быть равны (a = " + baseA + ", b = " + baseB + ")");
        }
        if (height >= sideC) {
            throw new IllegalArgumentException("Высота h (" + height + ") должна быть меньше боковой стороны c (" + sideC + ")");
        }

        // Дополнительная проверка как в index.html
        double baseDiff = Math.abs(baseB - baseA);
        double minSide = Math.sqrt(height * height + (baseDiff / 2) * (baseDiff / 2));
        if (sideC < minSide) {
            throw new IllegalArgumentException("При данных основаниях и высоте боковая сторона должна быть не менее " + String.format("%.2f", minSide));
        }

        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = a+b+2c)", baseA + baseB + 2 * sideC);
        results.put("Площадь (S = (a+b)/2 × h)", ((baseA + baseB) / 2) * height);
        results.put("Высота h", height);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateRhombus(java.util.HashMap<String, Double> v) {
        double side = v.get("side");
        double diagonal1 = v.get("diagonal1");
        double diagonal2 = v.get("diagonal2");

        // Исправление для API 23
        double height = 0.0;
        if (v.containsKey("height")) {
            height = v.get("height");
        }

        // Проверки (как в index.html)
        if (diagonal1 > 2 * side || diagonal2 > 2 * side) {
            throw new IllegalArgumentException("Диагонали не могут быть больше удвоенной стороны (2a = " + (2 * side) + ")");
        }

        // Проверка теоремы Пифагора
        double calculatedSide = Math.sqrt((diagonal1/2) * (diagonal1/2) + (diagonal2/2) * (diagonal2/2));
        if (Math.abs(calculatedSide - side) > 0.01) {
            throw new IllegalArgumentException("При данных диагоналях сторона должна быть " + String.format("%.2f", calculatedSide));
        }

        // Проверка высоты
        if (height > 0 && height >= side) {
            throw new IllegalArgumentException("Высота h должна быть меньше стороны a");
        }

        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Периметр (P = 4a)", 4 * side);
        results.put("Площадь (S = (d₁×d₂)/2)", (diagonal1 * diagonal2) / 2);
        if (height > 0) {
            results.put("Высота h", height);
        }
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateCircle(java.util.HashMap<String, Double> v) {
        double radius = v.get("radius");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Длина окружности (C = 2πr)", 2 * Math.PI * radius);
        results.put("Площадь круга (S = πr²)", Math.PI * radius * radius);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateCube(java.util.HashMap<String, Double> v) {
        double side = v.get("side");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь поверхности (S = 6a²)", 6 * side * side);
        results.put("Объём (V = a³)", side * side * side);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateParallelepiped(java.util.HashMap<String, Double> v) {
        double length = v.get("length");
        double width = v.get("width");
        double height = v.get("height");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь поверхности (S = 2(ab+ac+bc))", 2 * (length * width + length * height + width * height));
        results.put("Объём (V = a×b×c)", length * width * height);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateCone(java.util.HashMap<String, Double> v) {
        double radius = v.get("radius");
        double height = v.get("height");
        double l = Math.sqrt(radius * radius + height * height);
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь поверхности (S = πr(r+l))", Math.PI * radius * (radius + l));
        results.put("Объём (V = (πr²h)/3)", (1.0/3.0) * Math.PI * radius * radius * height);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculatePyramid(java.util.HashMap<String, Double> v) {
        // Проверка наличия всех параметров
        if (!v.containsKey("baseSide") || !v.containsKey("height")) {
            throw new IllegalArgumentException("Необходимо указать сторону основания (a) и высоту пирамиды (h)");
        }

        double baseSide = v.get("baseSide");
        double height = v.get("height");

        // Базовые проверки на положительность
        if (baseSide <= 0 || height <= 0) {
            throw new IllegalArgumentException("Сторона основания и высота должны быть положительными числами больше нуля");
        }

        // Проверка минимальных значений (опционально)
        if (baseSide < 0.01 || height < 0.01) {
            throw new IllegalArgumentException("Значения параметров слишком маленькие. Минимальное значение: 0.01");
        }

        // Проверка максимальных значений (опционально)
        if (baseSide > 1000 || height > 1000) {
            throw new IllegalArgumentException("Значения параметров слишком большие. Введите значения до 1000");
        }

        // Проверка на разумное соотношение высоты к стороне основания
        // Для правильной пирамиды высота не должна быть слишком маленькой или слишком большой
        // относительно стороны основания
        double heightToBaseRatio = height / baseSide;

        // Проверка на слишком "сплющенную" пирамиду (почти плоская)
        if (heightToBaseRatio < 0.1) {
            throw new IllegalArgumentException(
                    String.format("Пирамида слишком плоская. Высота (%.2f) очень мала относительно стороны основания (%.2f). " +
                                    "Соотношение h/a = %.2f. Рекомендуется: h/a ≥ 0.2",
                            height, baseSide, heightToBaseRatio));
        }

        // Проверка на слишком "высокую" пирамиду (нереалистичная форма)
        if (heightToBaseRatio > 10) {
            throw new IllegalArgumentException(
                    String.format("Пирамида слишком высокая. Высота (%.2f) слишком велика относительно стороны основания (%.2f). " +
                                    "Соотношение h/a = %.2f. Рекомендуется: h/a ≤ 5",
                            height, baseSide, heightToBaseRatio));
        }

        // Проверка существования треугольников в боковых гранях
        // Апофема образует прямоугольный треугольник с половиной стороны основания и высотой
        // По теореме Пифагора: apothem² = height² + (baseSide/2)²
        // Всегда выполнимо для положительных значений, но добавим проверку на переполнение

        // Вычисляем квадраты для проверки переполнения
        double halfBase = baseSide / 2.0;

        // Проверка на слишком большие значения, которые могут вызвать переполнение
        if (height > 1e150 || halfBase > 1e150) {
            throw new IllegalArgumentException("Значения параметров слишком большие для вычислений");
        }

        // Вычисление параметров
        double baseArea = baseSide * baseSide;
        double apothemSquared = height * height + halfBase * halfBase;

        // Проверка на корректность вычислений
        if (apothemSquared <= 0 || !Double.isFinite(apothemSquared)) {
            throw new IllegalArgumentException("Некорректные данные для вычисления апофемы");
        }

        double apothem = Math.sqrt(apothemSquared);

        // Дополнительная проверка: апофема должна быть больше половины стороны основания
        // (это всегда верно для положительной высоты, но для уверенности)
        if (apothem <= halfBase) {
            throw new IllegalArgumentException(
                    String.format("Апофема (≈%.2f) должна быть больше половины стороны основания (%.2f) " +
                            "для существования боковых граней", apothem, halfBase));
        }

        // Проверка на слишком острый угол при вершине
        // Угол наклона бокового ребра к основанию: tan(α) = height / halfBase
        double angleAtApex = Math.atan(height / halfBase) * 180.0 / Math.PI;

        // Если угол слишком острый (< 10°), пирамида будет очень "острой"
        if (angleAtApex < 10) {
            throw new IllegalArgumentException(
                    String.format("Угол при вершине боковой грани слишком острый: ≈%.1f°. " +
                            "Рекомендуется угол ≥ 15° для реалистичной пирамиды", angleAtApex));
        }

        // Если угол слишком тупой (> 80°), пирамида будет очень "пологой"
        if (angleAtApex > 80) {
            throw new IllegalArgumentException(
                    String.format("Угол при вершине боковой грани слишком тупой: ≈%.1f°. " +
                            "Рекомендуется угол ≤ 75°", angleAtApex));
        }

        // Вычисления
        double lateralArea = 2 * baseSide * apothem;
        double surfaceArea = baseArea + lateralArea;
        double volume = (1.0 / 3.0) * baseArea * height;

        // Проверка на корректность вычисленных значений
        if (!Double.isFinite(lateralArea) || !Double.isFinite(surfaceArea) || !Double.isFinite(volume)) {
            throw new IllegalArgumentException("Результаты вычислений вышли за допустимые пределы");
        }

        // Формирование результатов
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь основания (Sₒ = a²)", baseArea);
        results.put("Площадь боковой поверхности (Sᵦ = 2a×l)", lateralArea);
        results.put("Полная площадь поверхности (S = Sₒ + Sᵦ)", surfaceArea);
        results.put("Объём (V = (Sₒ×h)/3)", volume);
        results.put("Апофема (l)", apothem);
        results.put("Угол наклона боковой грани, градусы", angleAtApex);

        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateCylinder(java.util.HashMap<String, Double> v) {
        double radius = v.get("radius");
        double height = v.get("height");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь поверхности (S = 2πr(r+h))", 2 * Math.PI * radius * (radius + height));
        results.put("Объём (V = πr²h)", Math.PI * radius * radius * height);
        return results;
    }

    private java.util.LinkedHashMap<String, Double> calculateSphere(java.util.HashMap<String, Double> v) {
        double radius = v.get("radius");
        java.util.LinkedHashMap<String, Double> results = new java.util.LinkedHashMap<>();
        results.put("Площадь поверхности (S = 4πr²)", 4 * Math.PI * radius * radius);
        results.put("Объём (V = (4πr³)/3)", (4.0/3.0) * Math.PI * radius * radius * radius);
        return results;
    }

    private void showResults(java.util.LinkedHashMap<String, Double> results) {
        // Очищаем контейнер результатов
        resultsContainer.removeAllViews();
        errorText.setVisibility(View.GONE);

        int figureIndex = figureSpinner.getSelectedItemPosition();
        String figureName = figures[figureIndex][1];
        String figureEmoji = figures[figureIndex][2];
        String figureDim = figures[figureIndex][3];

        // Создаем заголовок
        TextView title = new TextView(this);
        title.setText(figureEmoji + " " + figureName + " [" + figureDim + "]");
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(android.R.color.black)); // Черный цвет

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 16);
        title.setLayoutParams(titleParams);
        resultsContainer.addView(title);

        // Добавляем результаты
        for (java.util.Map.Entry<String, Double> entry : results.entrySet()) {
            TextView resultItem = new TextView(this);
            resultItem.setText(entry.getKey() + ": " + String.format("%.2f", entry.getValue()));
            resultItem.setTextSize(16);
            resultItem.setTextColor(getResources().getColor(android.R.color.black)); // Черный цвет
            resultItem.setPadding(16, 12, 16, 12);

            if (getResources().getIdentifier("result_item_bg", "drawable", getPackageName()) != 0) {
                resultItem.setBackgroundResource(R.drawable.result_item_bg);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 8);
            resultItem.setLayoutParams(params);
            resultsContainer.addView(resultItem);
        }

        resultsContainer.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        resultsContainer.setVisibility(View.GONE);
        errorText.setText(message);
        errorText.setTextColor(getResources().getColor(android.R.color.black)); // Черный цвет
        errorText.setVisibility(View.VISIBLE);
    }

    private void clearResults() {
        resultsContainer.removeAllViews();
        resultsContainer.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);
    }
}