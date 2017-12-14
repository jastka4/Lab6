package tb.sockets.client;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Konsola {

	private static FilterOutputStream so;
	private static Socket sock;

	public static void main(String[] args) throws Exception {
		try {
			sock = new Socket("10.104.35.168", 6666);
			DataOutputStream so = new DataOutputStream(sock.getOutputStream());
			so.writeChars("wysyłam tekst\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			so.close();
			sock.close();
		
		}
	}

}
