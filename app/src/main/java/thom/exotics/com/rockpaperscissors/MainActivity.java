package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHost = (Button) findViewById(R.id.hostClick);
        Button btnJoin = (Button) findViewById(R.id.joinClick);

        btnHost.setOnClickListener(this);
        btnJoin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // check that Bluetooth is on
        bluetoothConnection();

        switch (v.getId()) {
            case R.id.hostClick:
                // TODO: create a bluetooth server connection
                System.out.println("DeBug - Host was clicked");
                break;
            case R.id.joinClick:
                // TODO: create a bluetooth client connection to the server
                System.out.println("DeBug - Join was clicked");
        }
    }

    public void bluetoothConnection() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            System.out.println("DeBug - Device does not support Bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

    }

}
