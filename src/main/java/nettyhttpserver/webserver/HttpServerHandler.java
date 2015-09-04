package nettyhttpserver.webserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private String uri;
    private int sentBytes;
    private int receivedBytes;
    private long startConnectionTime;
    private Connection currentConnection;

    public HttpServerHandler(String ip) {
        currentConnection = new Connection(ip);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        startConnectionTime = System.nanoTime();

        HttpServerInitializer.connectionsInfo.newConnection(currentConnection);
        HttpServerInitializer.connectionsInfo.newActiveConnection();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        receivedBytes += msg.toString().length();
        if (!(msg instanceof HttpRequest))
            return;

        uri = ((HttpRequest)msg).getUri();
        FullHttpResponse response = new ServerResponseHandler().checkRequest(uri);

        HttpServerInitializer.connectionsInfo.newUniqueConnection(uri);

        if (response != null) {
            this.sentBytes = response.content().writerIndex();
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        double connectionTimeLength = (System.nanoTime() - startConnectionTime) / 1000000000.0;
        double speed = (receivedBytes + sentBytes) / connectionTimeLength;

        speed = new BigDecimal(speed).setScale(2, RoundingMode.UP).doubleValue();

        currentConnection.setUri(uri);
        currentConnection.setSentBytes(sentBytes);
        currentConnection.setReceivedBytes(receivedBytes);
        currentConnection.setSpeed(speed);

        HttpServerInitializer.connectionsInfo.addConnection(currentConnection);
        HttpServerInitializer.connectionsInfo.removeActiveConnection();
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
