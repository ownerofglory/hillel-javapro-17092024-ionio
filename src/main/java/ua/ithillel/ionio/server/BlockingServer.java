package ua.ithillel.ionio.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class BlockingServer implements AutoCloseable {
    private final ServerSocket serverSocket;

    public BlockingServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void start() {
        while (true) {
            try (
                    Socket connection = serverSocket.accept();
                    OutputStream sos = connection.getOutputStream();

                    BufferedWriter connWriter = new BufferedWriter(new OutputStreamWriter(sos));
            ) {

                String resp = String.format("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n\r\n" +
                        """
                                                    <html>
                                                    <body>
                                                    <h1>Hello World</h1>
                                                    <h2>%s</h2>
                                                    </body>
                                                    </html>""", LocalDateTime.now());
                connWriter.write(resp);
                connWriter.flush();

            } catch (IOException e) {
                break;
            }
        }
    }


    @Override
    public void close() throws Exception {
        this.serverSocket.close();
    }
}
