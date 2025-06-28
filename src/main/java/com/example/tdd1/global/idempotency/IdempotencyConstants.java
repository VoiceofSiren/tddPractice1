package com.example.tdd1.global.idempotency;

import java.time.Duration;

public class IdempotencyConstants {

    private IdempotencyConstants() {};

    public static final String IDEMPOTENCY_KEY_HEADER_NAME = "idempotencyKey";

    public static final int IDEMPOTENCY_KEY_DURATION_OF_SECONDS = 30;
    public static final int IDEMPOTENCY_KEY_DURATION_OF_MINUTES = 5;
    public static final int IDEMPOTENCY_KEY_DURATION_OF_HOURS = 1;
}
