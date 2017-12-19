package tb.sockets.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Konsola {

    private static DataOutputStream so;
    private static DataInputStream si;
    private static Socket sock;

    public static void main(String[] args) throws Exception {
        try {
            sock = new Socket("192.168.1.16", 6666);
            so = new DataOutputStream(sock.getOutputStream());
            si = new DataInputStream(sock.getInputStream());

            String responseLine = si.readLine();
            System.out.println("Read from the socket: " + responseLine);

            while (responseLine != null) {
                if (responseLine.indexOf("busy") != -1) {
                    System.out.println("Disconnected");
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            so.close();
            si.close();
            sock.close();
        }
    }

}
