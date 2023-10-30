package com.proj.users;

import java.time.Duration;

public class Banned extends User {
    // Fields
    private Duration duration; // Java time object to use with LocalDateTime for date arithmetic. Consider having an end-date for ban instead like "LocalDateTime banEnd"
    private String reason;

    // Getters

    public Duration getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    //Setters

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
