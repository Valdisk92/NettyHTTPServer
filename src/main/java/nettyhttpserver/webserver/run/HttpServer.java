package nettyhttpserver.webserver.run;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import nettyhttpserver.webserver.HttpServerInitializer;

/**
 * Created by Владислав on 03.11.2014.
 */
public class HttpServer {
    private final int PORT;

    public HttpServer(int port) {
        this.PORT = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new HttpServerInitializer());

            bootstrap.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = (args.length > 0)? Integer.parseInt(args[0]) : 7777;
        System.out.println("Server running on port: " + port);
        new HttpServer(port).run();
    }
}
