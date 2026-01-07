package com.example.geometrycalculator;

public class CalculationResult {
    private Double perimeter;
    private Double area;
    private Double volume;

    public CalculationResult(Double perimeter, Double area, Double volume) {
        this.perimeter = perimeter;
        this.area = area;
        this.volume = volume;
    }

    // Геттеры
    public Double getPerimeter() { return perimeter; }
    public Double getArea() { return area; }
    public Double getVolume() { return volume; }
}