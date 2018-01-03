package tb.sockets.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientThread extends Thread
{
    private BufferedReader bufferedReader = null;
    private DataOutputStream dataOutputStream = null;
    private Socket clientSocket = null;
    private static ClientThread[] threads;

    private Board board = new Board(10, 10);
    private boolean isFirstTurn;


    public ClientThread(Socket clientSocket, ClientThread[] threads)
    {
        this.clientSocket = clientSocket;
        this.threads = threads;
        this.isFirstTurn = false;
    }

    public void run() {
        ClientThread opponent = null;
        char checkWinner;

        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Connected to " + clientSocket.getRemoteSocketAddress());

            if (threads[threads.length - 1] == null && threads[threads.length - 1] != this) {
                synchronized (this) {
                    wait();
                }
            }

            dataOutputStream.writeBytes((isFirstTurn ? '1' : '0') + board.getBoardAsString() + '\n');

            for(ClientThread thread: threads)
            {
                if(thread != this)
                    opponent = thread;
            }

            String responseLine;
            while ((responseLine = bufferedReader.readLine()) != null ) {
                if(responseLine.startsWith("/quit"))
                {
                    break;
                }
                int x = Character.getNumericValue(responseLine.charAt(0));
                int y = Character.getNumericValue(responseLine.charAt(1));
                synchronized (this) {
                    String checkHit = opponent.board.getIfShipMissedHitOrSunken(x, y);
                    if(board.checkIfAllShipsSunken() == true)
                        checkWinner = 'L';
                    else if (opponent.board.checkIfAllShipsSunken() == true)
                         checkWinner = 'W';
                    else
                        checkWinner = '.';
                    dataOutputStream.writeBytes(checkWinner + checkHit + '\n');
                    opponent.dataOutputStream.writeBytes(checkWinner + checkHit + '\n');
                    System.out.println(clientSocket.getRemoteSocketAddress() + "'s ship got " +checkWinner + checkHit);
                }
            }

            System.out.println("Connection closed with " + clientSocket.getRemoteSocketAddress());
            bufferedReader.close();
            dataOutputStream.close();
            clientSocket.close();

            resetThread();
            MultiThreadServer.updateThreads(threads);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static void startGame()
    {
        for (int i = 0; i < threads.length - 1; i++)
        {
            synchronized (threads[i])
            {
                threads[i].notify();
            }
        }
    }

    private synchronized void resetThread()
    {
        for(int i = 0; i < threads.length; i++)
        {
            if(threads[i] == this)
            {
                threads[i] = null;
            }
        }
    }

    protected void setFirstTurn()
    {
        this.isFirstTurn = true;
    }
}
