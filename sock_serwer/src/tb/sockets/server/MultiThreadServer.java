package tb.sockets.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer implements Runnable
{
	// This server can accept up to maxClientsCount clients' connections.
	private static final int maxClientsCount = 2;
	private static final ClientThread[] threads = new ClientThread[maxClientsCount];

	private boolean started;
	private boolean running;
	private ServerSocket serverSocket;
	private Thread serverThread;

	public MultiThreadServer()
	{
		this.running = false;
		this.serverSocket = null;
		this.serverThread = null;
		this.started = false;
	}

	public static void main(String[] args)
	{
		MultiThreadServer server = new MultiThreadServer();
		server.start();
	}

	public void start()
	{
		if(!started)
		{
			started = true;
			try {
				serverSocket = new ServerSocket(6666);
				running = true;

				serverThread = new Thread(this);
				serverThread.start();

				System.out.println("Server started!\n");
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public void stop()
	{
		running = false;
		started = false;

		if(serverThread != null)
			serverThread.interrupt();
		serverThread = null;
	}

	@Override
	public void run()
	{
		try
		{
			while (running)
			{
				try {
					Socket clientSocket = serverSocket.accept();

					int i;
					for (i = 0; i < maxClientsCount; i++)
					{
						if (threads[i] == null) {
							(threads[i] = new ClientThread(clientSocket, threads)).start();
							break;
						}
					}
					if (i == maxClientsCount)
					{
						DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
						os.writeBytes("Server too busy. Try later.");
						os.close();
						clientSocket.close();
					}

				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (checkIfAllPlayersConnected())
			{
				int firstPlayer = (Math.random() < 0.5) ? 0 : 1;
				threads[firstPlayer].setFirstTurn();
				ClientThread.startGame();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			stop();
		}
	}

	private static boolean checkIfAllPlayersConnected()
	{
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
}
