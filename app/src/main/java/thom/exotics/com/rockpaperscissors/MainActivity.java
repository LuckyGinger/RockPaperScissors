package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BluetoothConnectionHandler btConnection;
    ListView deviceList;


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
        deviceList.setAdapter(btConnection.getMArrayAdapter());

        switch (v.getId()) {
            case R.id.hostClick:
                // TODO: create a bluetooth server connection
                System.out.println("DeBug - Host was clicked");
                break;
            case R.id.joinClick:
                // TODO: create a bluetooth client connection to the server
                System.out.println("DeBug - Join was clicked");
                break;
        }
    }

}
