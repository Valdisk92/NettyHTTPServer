package nettyhttpserver.webserver;

import java.util.Date;

public class Connection {
    private String ip;
    private String uri;
    private Date date;
    private Date lastQueryDate;
    private int sentBytes;
    private int receivedBytes;
    private double speed;
    private int countQuery;

    public Connection(String ip) {
        this.ip = ip;
        this.date = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection newConnection = (Connection) o;
        return this.ip.equals(newConnection.ip);
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
