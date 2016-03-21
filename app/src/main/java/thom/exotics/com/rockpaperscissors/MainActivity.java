package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothConnectionHandler btConnection;
    private BluetoothServerSocket mmServerSocket;
    private ServerSide server;
    private ListView deviceList;
    private static final UUID MY_UUID = UUID.fromString("18c7e7e5-1223-4df0-84d1-70281b08dedb");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        uuid = UUID.randomUUID();
//        System.out.println("DeBug - Random UUDI: " + uuid);

//        btConnection = new BluetoothConnectionHandler();

        deviceList = (ListView) findViewById(R.id.listView);
        Button btnHost = (Button) findViewById(R.id.hostClick);
        Button btnJoin = (Button) findViewById(R.id.joinClick);

        btnHost.setOnClickListener(this);
        btnJoin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // check that Bluetooth is on

        btConnection = new BluetoothConnectionHandler(this);

        switch (v.getId()) {
            case R.id.hostClick:
                // TODO: create a bluetooth server connection
                System.out.println("DeBug - Host was clicked");
                // Make device discoverable so host can be connected to
                btConnection.makeDiscoverable();
                break;
            case R.id.joinClick:
                // TODO: create a bluetooth client connection to the server
                System.out.println("DeBug - Join was clicked");
                // Turn on bluetooth if not already on
                btConnection.enableBluetooth();
                // Discover bluetooth devices
                btConnection.discoverDevices();
                // Update the listView
                deviceList.setAdapter(btConnection.getMArrayAdapter());
                deviceList.setClickable(true);
                deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // get the name of the device from the string in the deviceList
                        String deviceName = ((String) deviceList.getItemAtPosition(position)).split("\n")[0];
                        // get the address of the device from the string in the deviceList
                        String address = ((String) deviceList.getItemAtPosition(position)).split("\n")[1];

                        // DeBug - Check that we are able to the the device name and address
                        System.out.println("DeBug - Device Name: " + deviceName);
                        System.out.println("DeBug - Device Addr: " + address);

                        // TODO: set up the server connection
                        BluetoothAdapter btAdapter = btConnection.getBluetoothAdapter();
                        acceptThread(btAdapter, deviceName);
                    }
                });

                break;
        }
    }

    public void acceptThread(BluetoothAdapter btAdapter, String name) {
        BluetoothServerSocket tmp = null;
        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord("MYYAPP", MY_UUID);

        } catch (IOException e) { }
        mmServerSocket = tmp;
    }
 
//    public void run() {
//        BluetoothSocket socket = null;
//        while (true) {
//            try {
//                socket = mmServerSocket.accept();
//
//            } catch (IOException e) {
//                break;
//            }
//            // If a connection was accepted
//            if (socket != null) {
//                // Do work to manage the connection(in a separate thread)
//                manageConnectedSocket(socket);
//                mmServerSocket.close();
//            }
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
