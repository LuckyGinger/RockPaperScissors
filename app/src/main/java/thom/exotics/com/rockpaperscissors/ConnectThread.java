package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Thom on 3/26/2016.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter btAddapter;
    private ManageThread manager;

    public ConnectThread(String name, BluetoothAdapter mbtAddapter, BluetoothDevice device, UUID theUUID) {
        BluetoothSocket tmp = null;
        btAddapter = mbtAddapter;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(theUUID);
        } catch (IOException e) {
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down connection
        btAddapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds of throws in exception
            mmSocket.connect();
            System.out.println("Connected Device: " + mmSocket.getRemoteDevice());

        } catch (IOException connectExeption) {
            // Unable to connect; close the socket and get to the choppa!
            try {
                mmSocket.close();
            } catch (IOException closeException) {}
            return;
        }

        // Start managing the connection //TODO: this might need to be changed...
        manager = new ManageThread(mmSocket);
        manager.start();

    }

    public ManageThread getManager() {
        return manager;
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {}
    }
}
