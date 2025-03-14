package com.github.onotoliy.opposite.treasure.data;

public record Debt(
        Event event,
        Deposit deposit
) {
    // Переопределение метода toString()
    @Override
    public String toString() {
        return "{\"event\": " + event + ", \"deposit\": " + deposit + "}";
    }
}
