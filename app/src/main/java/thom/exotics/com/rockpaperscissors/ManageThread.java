package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Thom on 3/24/2016.
 */
public class ManageThread extends Thread {
    public static interface CallBackListener
    {
        public void onReceived(String msg);
    }

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private CallBackListener listener = null;

    public ManageThread(BluetoothSocket socket) {
        System.out.println("DeBug - Starting ManageThread");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {}

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        System.out.println("DeBug - Running ManageThread");
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                System.out.println("Waiting for input from buffer...");
                bytes = mmInStream.read(buffer);
                System.out.println("Input Received");

                byte[] readBuf = (byte[]) buffer;

                String strIncom = new String(readBuf);

                System.out.print("Message: " + strIncom);

            } catch (IOException e) {
                break;
            }
        }
        System.out.println("DeBug - Ending ManageThread");
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {}
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {}
    }
}
