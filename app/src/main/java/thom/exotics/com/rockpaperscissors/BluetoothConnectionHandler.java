package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.util.Set;

/**
 * Created by Thom on 3/14/2016.
 */
public class BluetoothConnectionHandler {
    Activity theActivity;

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bBluetoothAdapter = null;
    private ArrayAdapter<String> bArrayAdapter;
    private IntentFilter filter;
    private BluetoothDevice device;

    public BluetoothConnectionHandler() {
        // Do nothing for now
    }


    public BluetoothConnectionHandler(Activity activity) {
        theActivity = activity;

        bArrayAdapter = new ArrayAdapter<String>(theActivity, R.layout.listitem, R.id.textview);

        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        theActivity.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        theActivity.registerReceiver(mReceiver, filter);
        bBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    public ArrayAdapter getMArrayAdapter(){
        return bArrayAdapter;
    }
    public BluetoothAdapter getBluetoothAdapter() {
        return bBluetoothAdapter;
    }
    public BluetoothDevice getBluetoothDevice() {
        return device;
    }

    public void makeDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 200);
        theActivity.startActivity(discoverableIntent);
    }

    public void enableBluetooth(){
        System.out.println("DeBug - enabling bluetooth");
        bBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bBluetoothAdapter == null) {
            // Device does not support Bluetooth
            System.out.println("DeBug - device does not support Bluetooth");
        } else {
            if (!bBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                theActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

    }

    public void discoverDevices() {
        System.out.println("DeBug - discovering devices");
        bBluetoothAdapter.cancelDiscovery();
        bBluetoothAdapter.startDiscovery();
    }

    public void cancelDiscovery() {
        bBluetoothAdapter.cancelDiscovery();
    }

    public Boolean isDiscovering() {
        return bBluetoothAdapter.isDiscovering();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("DeBug - inside BroadcastReceiver");

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent

                System.out.println("DeBug - broadcastReciver for ACTION_FOUND");
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    System.out.println("DeBug - " + device.getName() + " <-> " + device.getAddress());
                    bArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    bArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

}