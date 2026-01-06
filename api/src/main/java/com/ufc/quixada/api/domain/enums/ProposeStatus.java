package com.ufc.quixada.api.domain.enums;

public enum ProposeStatus {
    ACCEPTED,
    REJECTED,
    WAITING_ANALYSIS,
    CANCELED;

    public boolean isFinalized() {
        return this == ACCEPTED || this == REJECTED || this == CANCELED;
    }
}
