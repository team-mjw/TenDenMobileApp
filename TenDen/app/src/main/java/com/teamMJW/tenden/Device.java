package com.teamMJW.tenden;

import java.net.InetAddress;

public class Device {
    private String bulbId;
    private String currentPowerStatus;
    private String currentBrightness;
    private String currentCT;
    private InetAddress currentIP;

    public Device(String bulbId, String currentPowerStatus, String currentBrightness, String currentCT, InetAddress currentIP) {
        this.bulbId = bulbId;
        this.currentPowerStatus = currentPowerStatus;
        this.currentBrightness = currentBrightness;
        this.currentCT = currentCT;
        this.currentIP = currentIP;
    }

    public String getCurrentPowerStatus() {
        return currentPowerStatus;
    }

    public String getCurrentBrightness() {
        return currentBrightness;
    }

    public String getCurrentCT() {
        return currentCT;
    }

    public InetAddress getCurrentIP() {
        return currentIP;
    }

    public String getBulbId() {
        return bulbId;
    }
}
