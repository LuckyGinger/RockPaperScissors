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

    public ManageThread(BluetoothSocket socket, CallBackListener aListener) {
        mmSocket = socket;
        this.listener = aListener;
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
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                byte[] readBuf = (byte[]) buffer;
                String strIncom = new String(readBuf, 0 , bytes);

                if (listener != null) {
                    listener.onReceived(strIncom);
                }

            } catch (IOException e) {
                break;
            }
        }
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
