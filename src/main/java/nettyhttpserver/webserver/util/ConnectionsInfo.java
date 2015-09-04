package nettyhttpserver.webserver.util;

import nettyhttpserver.webserver.Connection;

import java.util.*;

import static io.netty.util.CharsetUtil.UTF_8;

public class ConnectionsInfo {

    private static ConnectionsInfo instance;

    private List<Connection> connectionsList = new LinkedList<>();
    private Set<String> uniqueIPSet = new HashSet<>();
    private Set<Connection> connectionsSet = new HashSet<>();

    private Map<String, Integer> URLMap = new TreeMap<>();
    private long countConnections = 0;
    private long countActive = 0;

    private ConnectionsInfo() {
    }



    public static ConnectionsInfo getInstance() {
        if (instance == null) {
            instance = new ConnectionsInfo();
        }
        return instance;
    }

    public synchronized byte[] getStatus() {

        String table1Head = "<thead><tr><td>Total connection</td><td>Unique connection</td><td>Active connection</td></tr></thead>";
        String table2Head = "<thead><tr><td>IP</td><td>Count Query</td><td>Date of last query</td></tr></thead>";
        String table3Head = "<thead><tr><td>URL</td><td>CountURL</td></tr></thead>";
        String table4Head = "<thead><tr><td>src_ip</td><td>URI</td><td>timestamp</td><td>sent_bytes</td><td>received_bytes</td><td>speed(bytes/sec)</td></tr></thead>";

        String htmlPage2 = "";
        htmlPage2 += "" +
                "<html>" +
                "<table border =1>" +
                table1Head +
                "<tr><th>" + countConnections + "</th><th>" + uniqueIPSet.size() + "</th><th>" + countActive + "</th></tr></table>" +
                "<table border = 1>" +
                table2Head;
        for (Connection connection : connectionsSet) {
            htmlPage2 += "<tr><th>" + connection.getIp() + "</th><th>" + connection.getCountQuery() + "</th><th>" + connection.getLastQueryDate() + "</th></tr>";
        }
        htmlPage2 += "</table>" +
                "<table border = 1>" +
                table3Head;
        for (Map.Entry<String, Integer> k : URLMap.entrySet()) {
            htmlPage2 += "<tr><th>" + k.getKey() + "</th><th>" + k.getValue() + "</th></tr>";
        }
        htmlPage2 += "</table>" +
                "<table border = 1>" +
                table4Head;
        for (Connection connection : connectionsList) {
            htmlPage2 += "<tr><th>" + connection.getIp() + "</th><th>" + connection.getURL() + "</th><th>" + connection.getDate() + "</th><th>" + connection.getSentBytes() + "</th><th>" + connection.getReceivedBytes() + "</th><th>" + connection.getSpeed() + "</th></tr>";
        }
        htmlPage2 += "</table></html>";
        return htmlPage2.getBytes(UTF_8);
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
