package com.vanard.muze.model.museum;

public class MuseumCheckIn {
    private Long countCheckIn;
    private String museumName;

    public MuseumCheckIn() {
    }

    public MuseumCheckIn(Long countCheckIn, String museumName) {
        this.countCheckIn = countCheckIn;
        this.museumName = museumName;
    }

    public MuseumCheckIn(Long countCheckIn) {
        this.countCheckIn = countCheckIn;
    }

    public Long getCountCheckIn() {
        return countCheckIn;
    }

    public void setCountCheckIn(Long countCheckIn) {
        this.countCheckIn = countCheckIn;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }
}
