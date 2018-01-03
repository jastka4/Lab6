package tb.sockets.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	// This server can accept up to maxClientsCount clients' connections.
	private static final int maxClientsCount = 2;
	private static final ClientThread[] threads = new ClientThread[maxClientsCount];

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(6666);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new ClientThread(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxClientsCount) {
					DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
					os.writeBytes("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			if (checkIfAllPlayersConnected())
			{
				int firstPlayer = drawFirstPlayer();
				threads[firstPlayer].setFirstTurn();
				ClientThread.startGame();
			}
		}
	}

	private static boolean checkIfAllPlayersConnected() {
		return threads[maxClientsCount - 1] != null;
	}

	protected static void updateThreads(ClientThread[] clientThreads)
	{
		int i = 0;
		for(ClientThread thread: clientThreads)
		{
			threads[i] = thread;
		}
	}

	private static int drawFirstPlayer()
	{
		if(Math.random() < 0.5)
		{
			return 0;
		} else {
			return 1;
		}
	}
}
