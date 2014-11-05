package com.korostel.nettyhttpserver.webserver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Created by Владислав on 03.11.2014.
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public static ConnectionsInfo connectionsInfo = ConnectionsInfo.getInstance();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        String ip = socketChannel.remoteAddress().getHostString();

        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("handler", new HttpServerHandler(ip));
    }
}
