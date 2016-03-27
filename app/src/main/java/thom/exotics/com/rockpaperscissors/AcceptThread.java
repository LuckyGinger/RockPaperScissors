package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Thom on 3/20/2016.
 */
public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private String threadName;
    private ManageThread manager;

    public AcceptThread ( String name, BluetoothAdapter mbtAdapter, UUID theUUID) {
        threadName = name;
        BluetoothServerSocket tmp = null;
        System.out.println("Creating " + threadName );
        try {
            tmp = mbtAdapter.listenUsingRfcommWithServiceRecord(name, theUUID);
        } catch (IOException e) {}
        mmServerSocket = tmp;

    }

    public void run() {
        BluetoothSocket socket = null;
        System.out.print("Running " + threadName);
        int counter = 0;
        while (true) {
            if (counter++ < 10) {
                System.out.print(".");
            }
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                System.out.println("Connected Device: " + socket.getRemoteDevice());

                // Start managing the connection //TODO: this might need to be changed...
                manager = new ManageThread(socket);
                manager.start();


                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        System.out.println("Thread " + threadName + " exiting");
    }

    public ManageThread getManager() {
        return manager;
    }

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {}
    }
}
