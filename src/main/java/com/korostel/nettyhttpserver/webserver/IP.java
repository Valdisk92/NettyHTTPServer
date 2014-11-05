package com.korostel.nettyhttpserver.webserver;

import java.util.Date;

/**
 * Created by Владислав on 03.11.2014.
 */
public class IP {
    private String ip = "";
    private String uri = "";
    private Date date = new Date();
    private Date lastQueryDate = new Date();
    private int sentBytes = 0;
    private int receivedBytes = 0;
    private double speed = 0.0;
    private int countQuery = 0;

    public IP() {
        this.date = new Date();
    }

    @Override
    public String toString() {
        return "IP{" +
                "ip='" + ip + '\'' +
                ", uri='" + uri + '\'' +
                ", date=" + date +
                ", sentBytes=" + sentBytes +
                ", receivedBytes=" + receivedBytes +
                ", speed=" + speed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IP newIP = (IP) o;
        return this.ip.equals(newIP.ip);
    }

    @Override
    public int hashCode() {
        return ip.hashCode();
    }

    public String getIp() {
        return ip;
    }

    public String getURL() {
        return uri;
    }

    public Date getDate() {
        return date;
    }

    public Date getLastQueryDate() {
        return lastQueryDate;
    }

    public int getSentBytes() {
        return sentBytes;
    }

    public int getReceivedBytes() {
        return receivedBytes;
    }

    public double getSpeed() {
        return speed;
    }

    public int getCountQuery() {
        return countQuery;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLastQueryDate(Date lastQueryDate) {
        this.lastQueryDate = lastQueryDate;
    }

    public void setSentBytes(int sentBytes) {
        this.sentBytes = sentBytes;
    }

    public void setReceivedBytes(int receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void addNewQuery() {
        this.countQuery++;
    }
}
