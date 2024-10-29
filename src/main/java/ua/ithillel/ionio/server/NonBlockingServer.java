package ua.ithillel.ionio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingServer implements AutoCloseable {
    private ServerSocketChannel serverSocketCh;
    private Selector selector;

    public NonBlockingServer(int port) throws IOException {
        this.serverSocketCh = ServerSocketChannel.open();
        this.serverSocketCh.bind(new InetSocketAddress(port));
        this.selector = Selector.open();
    }

    public void start() {
        try {
            serverSocketCh.configureBlocking(false);
            serverSocketCh.register(selector, serverSocketCh.validOps());


            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // hasNext(), next();

                while (iterator.hasNext()) {
                    SelectionKey signal = iterator.next();

                    // ready to connect
                    if (signal.isAcceptable()) {
                        SocketChannel connection = serverSocketCh.accept();
                        if (connection != null) {
                            connection.configureBlocking(false);
                            connection.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        }
                    }

                    // ready to read
                    if (signal.isReadable()) {
                        SocketChannel channel = (SocketChannel) signal.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);

                        byteBuffer.flip();

                        String request = StandardCharsets.UTF_8.decode(byteBuffer).toString();


                        System.out.println("Request: " + request);
                    }

                    // ready to write
                    if (signal.isWritable()) {
                        SocketChannel channel = (SocketChannel) signal.channel();

                        String resp = String.format("HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/html\r\n\r\n" +
                                """
                                                            <html>
                                                            <body>
                                                            <h1>Hello From Non Blocking server</h1>
                                                            <h2>%s</h2>
                                                            </body>
                                                            </html>""", LocalDateTime.now());

                        ByteBuffer writeBuffer = ByteBuffer.wrap(resp.getBytes(StandardCharsets.UTF_8));
                        channel.write(writeBuffer);

                        channel.close();
                    }

                    iterator.remove();
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.serverSocketCh.close();
        this.selector.close();
    }
}
