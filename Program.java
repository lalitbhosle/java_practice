import java.io.DataOutputStream;
import java.net.*;
import javax.sound.sampled.*;

public class Program
{
    public static void main(String[] args) throws Exception
    {
        AudioFormat af = new AudioFormat(8000.0f,8,1,true,false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
        TargetDataLine microphone = (TargetDataLine)AudioSystem.getLine(info);
        microphone.open(af);
        Socket conn = new Socket("localhost",3000);
        microphone.start();
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        int bytesRead = 0;
        byte[] soundData = new byte[1];
        Thread inThread = new Thread(new SoundReceiver(conn));
        inThread.start();
        while(bytesRead != -1)
        {
            bytesRead = microphone.read(soundData, 0, soundData.length);
            if(bytesRead >= 0)
            {
                dos.write(soundData, 0, bytesRead);
            }
        }
        System.out.println("IT IS DONE.");
    }
}
