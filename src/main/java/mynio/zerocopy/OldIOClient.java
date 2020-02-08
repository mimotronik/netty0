package mynio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author winterfell
 */
public class OldIOClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("127.0.0.1", 7001);

        InputStream inputStream = new FileInputStream("D:/Shadowsocks-4.1.8.0.zip");

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];

        long readCount;
        long total = 0;

        long startTime = System.currentTimeMillis();

        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送总字节数: " + total + "耗时：" + (System.currentTimeMillis() - startTime) + "ms");

    }

}
