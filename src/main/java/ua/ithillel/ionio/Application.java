package ua.ithillel.ionio;


import ua.ithillel.ionio.adapter.info.EUInfoSystem;
import ua.ithillel.ionio.adapter.info.InfoSystem;
import ua.ithillel.ionio.adapter.info.InfoSystemUSAAdapter;
import ua.ithillel.ionio.adapter.info.Information;
import ua.ithillel.ionio.adapter.usainfo.USADefaultInfoSystem;
import ua.ithillel.ionio.decorator.hnotifier.EmailNotifier;
import ua.ithillel.ionio.decorator.hnotifier.SMSNotifier;
import ua.ithillel.ionio.decorator.notifier.DefaultNotifier;
import ua.ithillel.ionio.decorator.notifier.Notifier;
import ua.ithillel.ionio.nio.FileReadCompletionHandler;
import ua.ithillel.ionio.server.BlockingServer;
import ua.ithillel.ionio.server.NonBlockingServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Application {

    private static Notifier notifier;
    private static InfoSystem infoSystem;

    public static void main(String[] args)  {
        try (NonBlockingServer nonBlockingServer = new NonBlockingServer(8000)) {
            nonBlockingServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        Path path = Path.of("files/text1.txt");
        boolean exists = Files.exists(path);

        try (
                AsynchronousFileChannel asyncFileCh = AsynchronousFileChannel
                        .open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);

                FileChannel fileCh = FileChannel.open(path,
                StandardOpenOption.READ, StandardOpenOption.WRITE);) {


            ByteBuffer asyncBuffer = ByteBuffer.allocate(16);
            asyncFileCh.read(asyncBuffer, 0, null, new FileReadCompletionHandler());




            String messgage = "Hello from Hillel";
            byte[] data = messgage.getBytes();


            ByteBuffer buffer = ByteBuffer.wrap(data);

            int written = fileCh.write(buffer);



            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            // capacity: 16
            // position: 0

            StringBuilder stringBuilder = new StringBuilder();
            int read; // amount of read bytes
            while((read = fileCh.read(byteBuffer)) != -1) {
                // position = read
                // limit = read

                byteBuffer.flip();
                // position: 0

//                Charset.defaultCharset()
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                stringBuilder.append(charBuffer);
                // position = limit

                byteBuffer.clear();
                // position: 0
                // limit: capacity
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //
        try (BlockingServer blockingServer = new BlockingServer(8000)) {
            blockingServer.start();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        try (
                ServerSocket serverSocket = new ServerSocket(8000);
                Socket connection = serverSocket.accept();

                InputStream sin = connection.getInputStream();
                InputStreamReader inputStreamReader =  new InputStreamReader(sin);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                OutputStream sos = connection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(sos);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                ) {

//            String socketMessage = readText(bufferedReader);

            writeText(bufferedWriter, "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n\r\n" +
                    """
                                                <html>
                                                <body>
                                                <h1>Hello World</h1>
                                                </body>
                                                </html>""");

            System.out.println();


        } catch (IOException e) {
            System.out.println("Port is in use");
        }


        // -rw-r--r-- read, write
        // -r--r--r-- read
        // - --- --- ---
        // d rwx rwx rwx

       // script.sh -rw-r--r--
        // 110 100 100
        // 544
        // 111 100 100
        // 644

        try {
            File file = new File("./files/text2.txt");
            if (file.exists()) {
                boolean b = file.canRead();
                boolean b1 = file.canWrite();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }






        try (
                Reader freader = new FileReader("files/declaration.txt");
                BufferedReader br = new BufferedReader(freader); // decortator

                Writer fwriter = new FileWriter("files/text-output.txt");
                BufferedWriter bw = new BufferedWriter(fwriter); // decorator
                ) {

            String text = readText(br);

            writeText(bw, "Hellpo from writer");


            System.out.println();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (
//                InputStream fin = new FileInputStream("files/text1.txt");
                InputStream fin = new FileInputStream("files/declaration.txt");
                BufferedInputStream bin = new BufferedInputStream(fin);

//                ServerSocket serverSocket = new ServerSocket(8000);
//                Socket connection = serverSocket.accept();
//                InputStream sin = connection.getInputStream();
//                BufferedInputStream binSin = new BufferedInputStream(sin);
//                OutputStream sos = connection.getOutputStream();


                OutputStream fos = new FileOutputStream("files/text1.txt", true);
                BufferedOutputStream bout = new BufferedOutputStream(fos);
                ) {



            String text = readText(bin);
            String text2 = readText(bin);
//            String textFromConnection = readText(binSin);

//            writeText(sos, """
//                    HTTP/1.1 200 OK
//                    Content-Type: text/html
//
//                    <html>
//                    <body>
//                    <h1>Hello World</h1>
//                    </body>
//                    </html>
//                    """);
            writeText(bout, "Hello World!");

            System.out.println();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        String region = System.getenv("REGION");
        if (region == "EU") {
            infoSystem = new EUInfoSystem();
        } else  {
            infoSystem = new InfoSystemUSAAdapter(new USADefaultInfoSystem());
        }


        Information ivanPetrenko = infoSystem.getInformation("Ivan Petrenko");




        notifier = new EmailNotifier(
                new SMSNotifier(new DefaultNotifier(), "+380501212350"), "test@test.com");

        notifier.doNotify("Super cool news");

        System.getenv("API_KEY");
        System.out.println(Arrays.toString(args));
    }

    private static void writeText(BufferedOutputStream out, String text) throws IOException {
        out.write(text.getBytes());
        out.flush();
    }

    private static void writeText(BufferedWriter out, String text) throws IOException {
        out.write(text);
        out.flush();
    }


    private static String readText(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();

//        int read;
//        while ((read = reader.read()) != -1) {
//            sb.append((char) read);
//        }
        char[] buf = new char[24];
        int read;
        while((read = reader.read(buf)) != -1) {
            sb.append(buf, 0, read);
        }

        return sb.toString();
    }



    private static String readText(BufferedInputStream inputStream) throws IOException {
        StringBuilder text = new StringBuilder();
//        int read;
//        while ((read = inputStream.read()) != -1) {
//            text.append((char) read);
//        }

        byte[] buffer = new byte[24];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            text.append(new String(buffer, 0, read));
        }


        return text.toString();
    }
}
