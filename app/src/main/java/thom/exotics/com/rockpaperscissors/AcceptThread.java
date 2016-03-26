package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Thom on 3/20/2016.
 */
public class AcceptThread extends Thread {
    private Thread t;
    private String threadName;
    AcceptThread (String name) {
        threadName = name;
        System.out.println("Creating " + threadName );
    }

    public void run() {
        System.out.println("Running " + threadName );
        try {
            for (int i = 4; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread: " + threadName + " interrupted");
        }
        System.out.println("Thread " + threadName + " exiting");
    }

//    public void start() {
//        System.out.println("Starting " + threadName);
//        if (t == null) {
//            t = new Thread (this, threadName);
//            t.start();
//        }
//    }
//  private BluetoothServerSocket mmServerSocket;
//
//
//
//    public void AcceptThread(BluetoothAdapter btAdapter, String name, UUID uuid){
//        BluetoothServerSocket tmp = null;
//        try {
//            tmp = btAdapter.listenUsingRfcommWithServiceRecord(name, uuid);
//        } catch (IOException e) {}
//        mmServerSocket = tmp;
//    }
}
