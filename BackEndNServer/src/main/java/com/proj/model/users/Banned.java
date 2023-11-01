package com.proj.model.users;

import java.time.Duration;

public class Banned extends User {
    // Field
    private Duration duration; // Java time object to use with LocalDateTime for date arithmetic. Consider having an end-date for ban instead like "LocalDateTime banEnd"
    private String reason;

    // Constructor
    public Banned(){};

    // Method
    public Duration getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
