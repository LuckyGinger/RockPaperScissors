package thom.exotics.com.rockpaperscissors;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothConnectionHandler btConnection;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView deviceList;
    private static final UUID MY_UUID = UUID.fromString("18c7e7e5-1223-4df0-84d1-70281b08dedb");
    private int REQUEST_ENABLE_BT = 1;

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

        // Get device ready for Bluetooth connections
        btConnection = new BluetoothConnectionHandler(this);
        mBluetoothAdapter = btConnection.getBluetoothAdapter();

        // Make sure bluetooth is doing it's thang
        if (mBluetoothAdapter == null) {
            String DNSB = "System doesn't support Bluetooth";
            System.out.print("DeBug - " + DNSB);
            Toast.makeText(this.getApplicationContext(), DNSB, Toast.LENGTH_SHORT);
            // don't continue
            return;
//        } else if (!mBluetoothAdapter.isEnabled()) {
//            // check that Bluetooth is on
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
        } else {
            System.out.println("Bananas");
            Toast.makeText(this.getApplicationContext(), "Bananas", Toast.LENGTH_SHORT);
        }

        switch (v.getId()) {
            case R.id.hostClick:
                bobBarker();

                break;
            case R.id.joinClick:
                dutch();

                break;
        }
    }

    // Host connection
    private void bobBarker() {
        System.out.println("DeBug - Host was clicked");
        // Make device discoverable so host can be connected to
        btConnection.makeDiscoverable();

        AcceptThread serverCon = new AcceptThread("ServerThread", mBluetoothAdapter, MY_UUID);
        serverCon.start();
    }

    // Client connection
    private void dutch() {
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
//                        acceptThread(mBluetoothAdapter, deviceName);

                BluetoothDevice device = btConnection.getBluetoothDevice();
                ConnectThread clientCon = new ConnectThread("ClientThread", mBluetoothAdapter, device,  MY_UUID);
                clientCon.start();

            }
        });
    }
}
