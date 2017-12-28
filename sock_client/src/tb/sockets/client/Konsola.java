package tb.sockets.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Konsola {

    private static DataOutputStream so;
    private static BufferedReader br;
    private static Socket sock;

    public static void main(String[] args) throws Exception {
        try {
            sock = new Socket("192.168.1.16", 6666);
            so = new DataOutputStream(sock.getOutputStream());
            br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            System.out.println("Connected to: " + sock.getRemoteSocketAddress());

            String responseLine = br.readLine();
            System.out.println(responseLine); // game board with battleships - bytes

            int i = 0;
            while (responseLine != null && i < 10)
            {
                if (responseLine.indexOf("busy") != -1) {
                    System.out.println("Disconnected");
                    break;
                }
                else
                {
                    String coordinates = i + "" + i +"\n";
                    so.writeBytes(coordinates);
                    responseLine = br.readLine();
                    System.out.println(responseLine);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            so.close();
            br.close();
            sock.close();
        }
    }
}
