package ua.ithillel.ionio.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class FileReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();

        CharBuffer chars = StandardCharsets.UTF_8.decode(attachment);
        System.out.println(String.valueOf(chars));

        attachment.clear();
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("Error occurred" + exc);
    }
}
