package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothConnectionHandler btConnection;
    private BluetoothServerSocket mmServerSocket;
    private AcceptThread server;
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

                BluetoothAdapter btAdapter = btConnection.getBluetoothAdapter();
                String name = "bluetoothComm";

                try {
                    // Create the connection to the Client
                    final BluetoothServerSocket btServer = btAdapter.listenUsingInsecureRfcommWithServiceRecord(name, MY_UUID);
                    BluetoothSocket serverSocket = btServer.accept();

                    ConnectedThread conThred = new ConnectedThread(serverSocket, new ConnectedThread.CallBackListener(){
                        @Override
                        public void onReceived(String msg) {
                            System.out.print("The Message from Client: '" + msg + "'");
                        }
                    });

                    conThred.write(("This is words from server").getBytes());
                    conThred.cancel();

                } catch (Exception e) {
                    System.out.println("DeBug - Error making server");
                    e.printStackTrace();
                }

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
//                        acceptThread(btAdapter, deviceName);


                        try {
                            // Create the connection to the Server
                            BluetoothDevice device = btConnection.getBluetoothDevice();
                            BluetoothSocket clientSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                            clientSocket.connect();

                            ConnectedThread conThred = new ConnectedThread(clientSocket, new ConnectedThread.CallBackListener(){
                                @Override
                                public void onReceived(String msg) {
                                    System.out.print("The Message from Sever: " + msg);
                                }
                            });

                            conThred.write(("This is words from client").getBytes());
                            conThred.cancel();

                        } catch (IOException e) {
                            System.out.println("DeBug - Error making client");
                            e.printStackTrace();
                        }

                    }
                });

                break;
        }
    }
}
