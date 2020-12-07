package jkit.http_server_native;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.val;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

import jkit.core.ext.IOExt;

public interface Main {

    static void main(String[] args) throws Exception {
        run(8080, () -> "Hello Alex!!!");
    }

    static void run(
        Integer port,
        Supplier<String> sayHi
    ) throws Exception {
        val server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", getHandler(sayHi.get()));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on " + port);
    }

    static HttpHandler getHandler(String msg) {
        return t -> {
            val v = t.getProtocol();
            val body = IOExt.inputStreamToString(t.getRequestBody());
//            val body = IOExt.inputStreamToString(t.getRequestBody());
            IOExt.out(body.toString());
//            System.out.println(t.getRequestURI().getQuery());
            t.sendResponseHeaders(200, msg.length());
            val os = t.getResponseBody();
            os.write(msg.getBytes());
            os.close();
        };
    }

}
