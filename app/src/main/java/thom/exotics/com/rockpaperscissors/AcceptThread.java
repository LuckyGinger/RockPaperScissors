package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Thom on 3/20/2016.
 */
public class AcceptThread extends Thread{
    private BluetoothServerSocket mmServerSocket;



    public void AcceptThread(BluetoothAdapter btAdapter, String name, UUID uuid){
        BluetoothServerSocket tmp = null;
        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {}
        mmServerSocket = tmp;
    }
}
