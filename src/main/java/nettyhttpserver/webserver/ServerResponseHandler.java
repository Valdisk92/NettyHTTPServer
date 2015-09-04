package nettyhttpserver.webserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import nettyhttpserver.webserver.util.ConnectionsInfo;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ServerResponseHandler {

    public ServerResponseHandler() {

    }

    public FullHttpResponse checkRequest(ChannelHandlerContext ctx, String request) throws InterruptedException {
        String url = "";

        if (request.contains("?url=")) {
            url = request.substring(request.indexOf("?url=")+5, request.length());
            if (!url.contains("http://")) {
                url = "http://" + url;
            }
            request = request.substring(0, request.indexOf("?url="));
            ConnectionsInfo.getInstance().putURL(url);
        }

        switch (request) {
            case "/hello":
                return responseHelloWorld();
            case "/status":
                return responseStatus(ctx);
            case "/redirect":
                return responseRedirect(url);
            default:
                return responseNotFound();
        }
    }

    private FullHttpResponse responseHelloWorld() throws InterruptedException {
        String hello = "Hello world!";
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(hello, CharsetUtil.US_ASCII));
        TimeUnit.SECONDS.sleep(10);

        return response;
    }

    private FullHttpResponse responseRedirect(String url) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, url);

        return response;
    }

    private FullHttpResponse responseStatus(ChannelHandlerContext ctx) {
        ByteBuf content = ctx.alloc().buffer(ConnectionsInfo.getInstance().getStatus().length);
        content.writeBytes(ConnectionsInfo.getInstance().getStatus());
        return new DefaultFullHttpResponse(HTTP_1_1, OK, content);
    }

    private FullHttpResponse responseNotFound() {
        String notFound = "<html>This is not the web page you are looking for.<p> 404 Not Found</html>";

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(notFound, CharsetUtil.US_ASCII));
    }
}
