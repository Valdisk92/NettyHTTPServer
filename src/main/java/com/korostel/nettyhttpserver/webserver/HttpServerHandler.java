package com.korostel.nettyhttpserver.webserver;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Владислав on 03.11.2014.
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private String uri;
    private String ip;
    private int sentBytes;
    private int receivedBytes;
    private double speed;
    private IP currentIP;

    public HttpServerHandler(String ip) {
        this.ip = ip;
        currentIP = new IP();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        speed = System.nanoTime();

        currentIP.setIp(ip);
        HttpServerInitializer.connectionsInfo.newConnection(currentIP);
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
        speed = (System.nanoTime() - speed) / 1000000000.0;
        System.out.println("speed first = " + speed);
        speed = (receivedBytes + sentBytes) / speed;
        System.out.println("received bytes = " + receivedBytes);
        System.out.println("sent bytes = " + sentBytes);
        System.out.println("speed = " + speed);

        speed = new BigDecimal(speed).setScale(2, RoundingMode.UP).doubleValue();

        System.out.println("speed rounded = " + speed);

        currentIP.setUri(uri);
        currentIP.setSentBytes(sentBytes);
        currentIP.setReceivedBytes(receivedBytes);
        currentIP.setSpeed(speed);

        HttpServerInitializer.connectionsInfo.addIP(currentIP);
        HttpServerInitializer.connectionsInfo.removeActiveConnection();
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
