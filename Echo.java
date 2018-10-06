import java.net.*;
import java.io.*;
import java.util.*;

public class Echo
{
    public static void main(String[] args) throws Exception
    {
        ServerSocket serverSocket = new ServerSocket(3000);
        while(true){Thread echoThread = new Thread(new EchoThread(serverSocket.accept()));
                    echoThread.start();}
    }
}

class EchoThread implements Runnable
{
    public static Collection<Socket> sockets = new ArrayList<Socket>();
    Socket connection = null;
    DataInputStream dataIn = null;
    DataOutputStream dataOut = null;

    public EchoThread(Socket conn) throws Exception
    {
        connection = conn;
        dataIn = new DataInputStream(connection.getInputStream());
        dataOut = new DataOutputStream(connection.getOutputStream());
        sockets.add(connection);
    }

    public void run()
    {
        int bytesRead = 0;
        byte[] inBytes = new byte[1];
        while(bytesRead != -1)
        {
            try{bytesRead = dataIn.read(inBytes, 0, inBytes.length);}catch (IOException e){}
            if(bytesRead >= 0)
            {
                sendToAll(inBytes, bytesRead);
            }
        }
        sockets.remove(connection);
    }

    public static void sendToAll(byte[] byteArray, int q)
    {
        Iterator<Socket> sockIt = sockets.iterator();
        while(sockIt.hasNext())
        {
            Socket temp = sockIt.next();
            DataOutputStream tempOut = null;
            try
            {
                tempOut = new DataOutputStream(temp.getOutputStream());
            } catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try{tempOut.write(byteArray, 0, q);}catch (IOException e){}
        }
    }
}
