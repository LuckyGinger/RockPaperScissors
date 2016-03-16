package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private final static int REQUEST_ENABLE_BT = 1;

    private Button btnHost = (Button) findViewById(R.id.hostClick);
    private Button btnJoin = (Button) findViewById(R.id.joinClick);

    private BluetoothConnectionHandler btConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btnHost.setOnClickListener(this);
//        btnJoin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        System.out.println("DeBug - button clicked");
        bluetoothConnection();
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
