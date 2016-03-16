package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Thom on 3/14/2016.
 */
public class BluetoothConnectionHandler {
    Activity theActivity;

    ArrayAdapter mArrayAdapter;

    private final static int REQUEST_ENABLE_BT = 1;

    public BluetoothConnectionHandler() {
        // Do nothing for now
    }

    public BluetoothConnectionHandler(Activity activity) {
        theActivity = activity;

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            System.out.println("DeBug - Device does not support Bluetooth");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                theActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
//                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//                System.out.println(device.getName() + " <-> " + device.getAddress());
            }
        }

    }

    public ArrayAdapter getMArrayAdapter(){
        return mArrayAdapter;
    }

}
