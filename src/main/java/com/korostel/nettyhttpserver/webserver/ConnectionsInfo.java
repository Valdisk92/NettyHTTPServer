package com.korostel.nettyhttpserver.webserver;

import java.util.*;

/**
 * Created by Владислав on 03.11.2014.
 */
public class ConnectionsInfo {

    private static volatile ConnectionsInfo instance;

    private List<IP> connectionsList = new ArrayList<>();
    private Set<String> uniqueIPSet = new HashSet<>();
    private Set<IP> connectionsSet = new HashSet<>();

    private Map<String, Integer> URLMap = new TreeMap<>();
    private long countConnections = 0;
    private long countActive = 0;

    private ConnectionsInfo() {
    }

    public synchronized static ConnectionsInfo getInstance() {
        ConnectionsInfo localInstance = instance;
        if (localInstance == null) {
            synchronized (ConnectionsInfo.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ConnectionsInfo();
                }
            }
        }
        return localInstance;
    }

    public synchronized String getStatus() {
        //Прошу прощения за "плохой-код". Все это наверное можно сделать через JSP, но с ними я не работал.
        //А на то, что бы выучить JSP и прикрутить их к Netty нужно побольше времени.
        //Но я старался что бы выглядело читабельно.
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("<html>");
        reportBuilder.append("<table border = 1>");
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>Total connection</th>");
                reportBuilder.append("<th>Unique connection</th>");
                reportBuilder.append("<th>Active connection</th>");
            reportBuilder.append("</tr>");
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>").append(countConnections).append("</th>");
                reportBuilder.append("<th>").append(uniqueIPSet.size()).append("</th>");
                reportBuilder.append("<th>").append(countActive).append("</th>");
            reportBuilder.append("</tr>");
        reportBuilder.append("</table>");
        reportBuilder.append("<table border = 1");
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>IP</th>");
                reportBuilder.append("<th>Count Query</th>");
                reportBuilder.append("<th>Date of last query</th>");
            reportBuilder.append("</tr>");
        for (IP ip : connectionsSet) {
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>").append(ip.getIp()).append("</th>");
                reportBuilder.append("<th>").append(ip.getCountQuery()).append("</th>");
                reportBuilder.append("<th>").append(ip.getLastQueryDate()).append("</th>");
            reportBuilder.append("</tr>");
        }
        reportBuilder.append("</table>");
        reportBuilder.append("<table border = 1>");
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>URL</th>");
                reportBuilder.append("<th>CountURL</th>");
            reportBuilder.append("</tr>");
            reportBuilder.append("<tr>");
        for(Map.Entry<String, Integer> k : URLMap.entrySet()) {
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>").append(k.getKey()).append("</th>");
                reportBuilder.append("<th>").append(k.getValue()).append("</th>");
            reportBuilder.append("</tr>");
        }
        reportBuilder.append("</table>");

        reportBuilder.append("<table border = 1>");
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>src_ip</th>");
                reportBuilder.append("<th>URI</th>");
                reportBuilder.append("<th>timestamp</th>");
                reportBuilder.append("<th>sent_bytes</th>");
                reportBuilder.append("<th>received_bytes</th>");
                reportBuilder.append("<th>speed(bytes/sec)</th>");
            reportBuilder.append("</tr>");
        for(IP ip: connectionsList) {
            reportBuilder.append("<tr>");
                reportBuilder.append("<th>").append(ip.getIp()).append("</th>");
                reportBuilder.append("<th>").append(ip.getURL()).append("</th>");
                reportBuilder.append("<th>").append(ip.getDate()).append("</th>");
                reportBuilder.append("<th>").append(ip.getSentBytes()).append("</th>");
                reportBuilder.append("<th>").append(ip.getReceivedBytes()).append("</th>");
                reportBuilder.append("<th>").append(ip.getSpeed()).append("</th>");
            reportBuilder.append("</tr>");
        }
        reportBuilder.append("</table>");
        reportBuilder.append("</html>");
        return reportBuilder.toString();
    }

    public synchronized void newActiveConnection() {
        countActive++;
    }

    public synchronized void removeActiveConnection() {
        countActive--;
    }

    public synchronized void addIP(IP ip) {
        if(connectionsList.size() > 15)
            connectionsList.remove(0);
        connectionsList.add(ip);

        connectionsSet.add(ip);

    }

    public synchronized void newConnection(IP ip) {
        countConnections++;
        for(Iterator<IP> iterator = connectionsSet.iterator(); iterator.hasNext();) {
            IP element = iterator.next();
            if (element.equals(ip)) {
                element.addNewQuery();
                element.setLastQueryDate(new Date());
            }
        }

    }

    public synchronized void putURL(String url) {
        if (URLMap.containsKey(url)) {
            URLMap.put(url, URLMap.get(url) + 1);
        } else {
            URLMap.put(url, 1);
        }
    }

    public synchronized void newUniqueConnection(String s) {
        if (!s.equals("/favicon.ico"))
            uniqueIPSet.add(s);
    }
}
