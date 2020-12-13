package jkit.http_client;

import jkit.core.ext.IOExt;
import jkit.core.ext.StringExt;
import lombok.val;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public interface Main {

    static void main(String[] argv) throws Exception {

        val url = new URL("http://localhost:8080/echo");

        val conn = (HttpURLConnection)url.openConnection();

        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("asd", "123");

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = StringExt.getBytes("asd");
            os.write(input, 0, input.length);
        }

        val s = StringExt.fromInputStream(conn.getInputStream());

        IOExt.out(s);

    }

}
