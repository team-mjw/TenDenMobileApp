package com.teamMJW.tenden;

public class Mode {
    private String name;
    private int brightness;
    private int temperature;

    public Mode(String name, int brightness, int temperature) {
        this.name = name;
        this.brightness = brightness;
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
