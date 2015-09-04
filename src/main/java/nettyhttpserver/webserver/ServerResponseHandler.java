package nettyhttpserver.webserver;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import nettyhttpserver.webserver.util.ConnectionsInfo;

import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

public class ServerResponseHandler {

    public FullHttpResponse checkRequest(String request) throws InterruptedException {
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
                return responseStatus();
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

    private FullHttpResponse responseStatus() {
        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(HttpServerInitializer.connectionsInfo.getStatus(), CharsetUtil.US_ASCII));
    }

    private FullHttpResponse responseNotFound() {
        String notFound = "<html>This is not the web page you are looking for.<p> 404 Not Found</html>";

        return new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.copiedBuffer(notFound, CharsetUtil.US_ASCII));
    }
}
