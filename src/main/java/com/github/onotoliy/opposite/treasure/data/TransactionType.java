package com.github.onotoliy.opposite.treasure.data;

public enum TransactionType {

    NONE("Не выбрано"),
    COST("Расход"),
    CONTRIBUTION("Взнос"),
    WRITE_OFF("Списание с депозита"),
    PAID("Платеж"),
    EARNED("Заработано");

    TransactionType(String name) {

    }

    public String getLabel() {
        return "";
    }
}
