package nettyhttpserver.webserver.util;

import nettyhttpserver.webserver.Connection;

import java.util.*;

public class ConnectionsInfo {

    private static volatile ConnectionsInfo instance;

    private List<Connection> connectionsList = new ArrayList<>();
    private Set<String> uniqueIPSet = new HashSet<>();
    private Set<Connection> connectionsSet = new HashSet<>();

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
        for (Connection connection : connectionsSet) {
            reportBuilder.append("<tr>");
            reportBuilder.append("<th>").append(connection.getIp()).append("</th>");
            reportBuilder.append("<th>").append(connection.getCountQuery()).append("</th>");
            reportBuilder.append("<th>").append(connection.getLastQueryDate()).append("</th>");
            reportBuilder.append("</tr>");
        }
        reportBuilder.append("</table>");
        reportBuilder.append("<table border = 1>");
        reportBuilder.append("<tr>");
        reportBuilder.append("<th>URL</th>");
        reportBuilder.append("<th>CountURL</th>");
        reportBuilder.append("</tr>");
        reportBuilder.append("<tr>");
        for (Map.Entry<String, Integer> k : URLMap.entrySet()) {
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
        for (Connection connection : connectionsList) {
            reportBuilder.append("<tr>");
            reportBuilder.append("<th>").append(connection.getIp()).append("</th>");
            reportBuilder.append("<th>").append(connection.getURL()).append("</th>");
            reportBuilder.append("<th>").append(connection.getDate()).append("</th>");
            reportBuilder.append("<th>").append(connection.getSentBytes()).append("</th>");
            reportBuilder.append("<th>").append(connection.getReceivedBytes()).append("</th>");
            reportBuilder.append("<th>").append(connection.getSpeed()).append("</th>");
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

    public synchronized void addConnection(Connection connection) {
        if (connectionsList.size() > 15)
            connectionsList.remove(0);
        connectionsList.add(connection);

        connectionsSet.add(connection);

    }

    public synchronized void newConnection(Connection connection) {
        countConnections++;
        for (Iterator<Connection> iterator = connectionsSet.iterator(); iterator.hasNext(); ) {
            Connection element = iterator.next();
            if (element.equals(connection)) {
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
