package org.example;

import org.example.exceptions.NegativeTimeSpanException;
import org.example.exceptions.IllegalInputTimeSpanException;
import org.example.exceptions.IllegalScaleFactorException;

public class TimeSpan {

    private int hours;
    private int minutes;

    // Нормалізація часу
    private void normalize() {
        hours += minutes / 60;
        minutes %= 60;
    }

    // Конструктори

    public TimeSpan() {
        this.hours = 0;
        this.minutes = 0;
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин не може бути від'ємною!"
            );
        }

        this.hours = minutes / 60;
        this.minutes = minutes % 60;
    }

    public TimeSpan(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректно введений часовий інтервал!"
            );
        }

        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException(
                    "Часовий інтервал не може бути null!"
            );
        }

        this.hours = span.hours;
        this.minutes = span.minutes;
    }

    // Геттери

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    // Додавання часу

    public void add(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректний часовий інтервал!"
            );
        }

        this.hours += hours;
        this.minutes += minutes;
        normalize();
    }

    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин не може бути від'ємною!"
            );
        }

        this.minutes += minutes;
        normalize();
    }

    public void add(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException(
                    "Часовий інтервал не може бути null!"
            );
        }

        add(span.hours, span.minutes);
    }

    // Віднімання часу

    public void subtract(int hours, int minutes) {
        if (hours < 0 || minutes < 0 || minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Некоректний часовий інтервал!"
            );
        }

        int currentTotal = getTotalMinutes();
        int subtractTotal = hours * 60 + minutes;

        if (subtractTotal > currentTotal) {
            throw new NegativeTimeSpanException(
                    "Часовий інтервал не може бути від'ємним!"
            );
        }

        int result = currentTotal - subtractTotal;

        this.hours = result / 60;
        this.minutes = result % 60;
    }

    public void subtract(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин не може бути від'ємною!"
            );
        }

        subtract(minutes / 60, minutes % 60);
    }

    public void subtract(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException(
                    "Часовий інтервал не може бути null!"
            );
        }

        subtract(span.hours, span.minutes);
    }

    // Обчислення тривалості

    public double getTotalHours() {
        return hours + minutes / 60.0;
    }

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    // Масштабування

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException(
                    "Множник повинен бути більшим за нуль!"
            );
        }

        int total = getTotalMinutes() * factor;

        this.hours = total / 60;
        this.minutes = total % 60;
    }

    @Override
    public String toString() {
        return hours + " год " + minutes + " хв";
    }
}
