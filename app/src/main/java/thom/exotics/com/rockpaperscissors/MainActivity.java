package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

import java.lang.reflect.Array;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothConnectionHandler btConnection;
    private ListView deviceList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                        System.out.println("DeBug - Test Clicked -START-------------------------");
                        System.out.println("DeBug - Device Name: " + deviceName);
                        System.out.println("DeBug - Device Addr: " + address);
                        System.out.println("DeBug - Test Clicked --END--------------------------");


                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
