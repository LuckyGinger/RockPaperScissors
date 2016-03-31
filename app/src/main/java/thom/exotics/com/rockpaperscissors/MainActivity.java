package thom.exotics.com.rockpaperscissors;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

    private BluetoothConnectionHandler btConnection;
    private BluetoothAdapter mBluetoothAdapter;
    private ListView deviceList;
    private static final UUID MY_UUID = UUID.fromString("18c7e7e5-1223-4df0-84d1-70281b08dedb");
    private static final int SUCCESS_CONNECT = 1;
    private static final int MESSAGE_READ = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private ManageThread mainManager;
    private Button btnHost;
    private Button btnJoin;
    private Logic logic;
    Button btnRock;
    Button btnPapr;
    Button btnScis;
    ImageView image;
    TextView mainMsg;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("DeBug - Inside of Handler");
            btnHost.setEnabled(false);
            btnJoin.setEnabled(false);
            String s = "successfully connected";
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS_CONNECT:
                    mainMsg.setText("Game On!");
                    mainManager = new ManageThread((BluetoothSocket)msg.obj);
                    mainManager.start();
                    System.out.println("CONNECT");
                    mainManager.write(s.getBytes());

                    btnRock = (Button) findViewById(R.id.rockButton);
                    btnPapr = (Button) findViewById(R.id.paperButton);
                    btnScis = (Button) findViewById(R.id.scissorsButton);

                    // ROCK
                    btnRock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Rock was clicked");
                            logic.myMove = "r";
                            mainManager.write(logic.myMove.getBytes());
                            image.setBackgroundResource(R.drawable.happy_rock);
                            btnRock.setEnabled(false);
                            btnPapr.setEnabled(false);
                            btnScis.setEnabled(false);
                            logic.getWinner(logic);
                        }
                    });
                    // PAPER
                    btnPapr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Paper was clicked");
                            logic.myMove = "p";
                            mainManager.write(logic.myMove.getBytes());
                            image.setBackgroundResource(R.drawable.happy_paper);
                            btnRock.setEnabled(false);
                            btnPapr.setEnabled(false);
                            btnScis.setEnabled(false);
                            logic.getWinner(logic);
                        }
                    });
                    // SCISSORS
                    btnScis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("Scissors was clicked");
                            logic.myMove = "s";
                            mainManager.write(logic.myMove.getBytes());
                            image.setBackgroundResource(R.drawable.happy_scissors);
                            btnRock.setEnabled(false);
                            btnPapr.setEnabled(false);
                            btnScis.setEnabled(false);
                            logic.getWinner(logic);
                        }
                    });

//                    Toast.makeText(getApplicationContext(), "Successfully Connected", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_READ:
//                    byte[] readBuf = (byte[])msg.obj; // TODO: Problem is here
                    String string = (String) msg.obj;
                    // check that this isn't the initial connection
                    if (string.equals(s)) {
                        logic.theirMove = null;
                    } else {
                        logic.theirMove = string;
                    }
                    System.out.println("Read Msg: " + logic.theirMove);
                    logic.getWinner(logic);
                    break;
            }
        }
    };

    //TODO: make this better
    private AcceptThread serverCon;
    private ConnectThread clientCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        uuid = UUID.randomUUID();
//        System.out.println("DeBug - Random UUDI: " + uuid);

//        btConnection = new BluetoothConnectionHandler();
        image = (ImageView) findViewById(R.id.imageView2);
        mainMsg = (TextView) findViewById(R.id.textView);
        deviceList = (ListView) findViewById(R.id.listView);
        btnHost = (Button) findViewById(R.id.hostClick);
        btnJoin = (Button) findViewById(R.id.joinClick);

        btnHost.setOnClickListener(this);
        btnJoin.setOnClickListener(this);

        logic = new Logic();
    }

    @Override
    public void onClick(View v) {

        // Get device ready for Bluetooth connections
        btConnection = new BluetoothConnectionHandler(this);
        mBluetoothAdapter = btConnection.getBluetoothAdapter();
        // Turn on bluetooth if not already on
        btConnection.enableBluetooth();

        switch (v.getId()) {
            case R.id.hostClick:
                System.out.println("DeBug - Host was clicked");
                bobBarker();
                break;
            case R.id.joinClick:
                System.out.println("DeBug - Join was clicked");
                dutch();

                break;

        }
    }

    // Host connection
    private void bobBarker() {
        System.out.println("DeBug - Host was clicked");
        mainMsg.setText("Waiting for connection...");
        btConnection.makeDiscoverable();

        // Make device discoverable so host can be connected to

        serverCon = new AcceptThread("ServerThread");
        serverCon.start();
    }

    // Client connection
    private void dutch() {
        // TODO: create a bluetooth client connection to the server
        System.out.println("DeBug - Join was clicked");
        mainMsg.setText("Finding Devices...");
        // Discover bluetooth devices
        btConnection.discoverDevices();
        // Update the listView
        deviceList.setAdapter(btConnection.getMArrayAdapter());
        deviceList.setVisibility(View.VISIBLE);
        deviceList.setClickable(true);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainMsg.setText("Connecting...");

                if (btConnection.isDiscovering()) {
                    btConnection.cancelDiscovery();
                }
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
                System.out.println("DeBug - DOES THIS WORK1");
                clientCon = new ConnectThread(device);
                clientCon.start();

                deviceList.setVisibility(View.INVISIBLE);
//                mainManager = clientCon.getManager();
//                for(int i = 0; i < 10; i++) {
//                    System.out.print("Trying to write to server");
//                    mainManager.write(("Message from client device").getBytes());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_CANCELED) {
                System.out.println("DeBug - Accepted Bluetooth pairing");

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("DeBug - Accepted Bluetooth pairing");
                btnHost.setEnabled(true);
                btnJoin.setEnabled(true);
            }
        }
    }

    /**
     * Hold the game logic pieces
     */
    public class Logic {
        public String myMove;
        public String theirMove;

        public void getWinner(Logic leLogic){
            System.out.println("myMove:    " + logic.myMove);
            System.out.println("theirMove: " + logic.theirMove);

            if (leLogic.myMove != null && leLogic.theirMove != null) {
                // TODO: check for the winner
                if ((leLogic.myMove.equals("r") && leLogic.theirMove.equals("r")) ||
                     leLogic.myMove.equals("p") && leLogic.theirMove.equals("p") ||
                     leLogic.myMove.equals("s") && leLogic.theirMove.equals("s")){

                    System.out.println("Game is a tie!");
                    mainMsg.setText("Tie Game!");
                    clearMoves(leLogic);

                } else if (leLogic.myMove.equals("r")){
                    if (leLogic.theirMove.equals("p")) {
                        System.out.println("You lose... loser.");
                        mainMsg.setText("You Lost...");
                        image.setBackgroundResource(R.drawable.mad_rock);
                    } else if (leLogic.theirMove.equals("s")) {
                        System.out.println("Hey buddy you win!");
                        mainMsg.setText("You Win!");
                        image.setBackgroundResource(R.drawable.happy_rock);
                    }
                    clearMoves(leLogic);
                } else if (leLogic.myMove.equals("p")){
                    if (leLogic.theirMove.equals("s")) {
                        System.out.println("You lose... loser.");
                        mainMsg.setText("You Lost...");
                        image.setBackgroundResource(R.drawable.mad_paper);
                    } else if (leLogic.theirMove.equals("r")) {
                        System.out.println("Hey buddy you win!");
                        mainMsg.setText("You Win!");
                        image.setBackgroundResource(R.drawable.happy_paper);
                    }
                    clearMoves(leLogic);
                } else if (leLogic.myMove.equals("s")){
                    if (leLogic.theirMove.equals("r")) {
                        System.out.println("You lose... loser.");
                        mainMsg.setText("You Lost...");
                        image.setBackgroundResource(R.drawable.mad_scissors);
                    } else if (leLogic.theirMove.equals("p")) {
                        System.out.println("Hey buddy you win!");
                        mainMsg.setText("You Win!");
                        image.setBackgroundResource(R.drawable.happy_scissors);
                    }
                    clearMoves(leLogic);
                }
            } else if (leLogic.myMove != null && leLogic.theirMove == null) {
                // TODO: display a message saying "your move"
                System.out.println("Waiting for slow person to make a move");
                mainMsg.setText("Waiting for other player");

            } else if (leLogic.myMove == null && leLogic.theirMove != null) {
                // TODO: display a message "waiting for other player"
                System.out.println("Your move homie.");
                mainMsg.setText("Waiting for your move");

            }
        }

        private void clearMoves(Logic leLogic) {
            leLogic.myMove = null;
            leLogic.theirMove = null;
            System.out.println("Game data cleared");
//            mainMsg.setText("Game On!");
            btnRock.setEnabled(true);
            btnPapr.setEnabled(true);
            btnScis.setEnabled(true);
        }
    }

    /**
     * ConnectThread - Tries to establish a connection to the accepting device
     */
    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds of throws in exception
                mmSocket.connect();
                System.out.println("Connected Device: " + mmSocket.getRemoteDevice());

            } catch (IOException connectExeption) {
                // Unable to connect; close the socket and get to the choppa!
                try {
                    mmSocket.close();
                } catch (IOException closeException) {}
                return;
            }


//            Toast.makeText(getApplicationContext(), "Device Connected", Toast.LENGTH_SHORT).show();
            // Start managing the connection //TODO: this might need to be changed...
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {}
        }
    }

    /**
     * AcceptThread - Accepts the device trying to connect
     */
    public class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private String threadName;

        public AcceptThread (String name) {
            threadName = name;
            BluetoothServerSocket tmp = null;
            System.out.println("Creating " + threadName);
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(name, MY_UUID);
            } catch (IOException e) {}
            mmServerSocket = tmp;

        }

        public void run() {
            BluetoothSocket socket = null;
            System.out.print("Running " + threadName);
            int counter = 0;
            while (true) {
                if (counter++ < 10) {
                    System.out.print(".");
                }
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    System.out.println("Connected Device: " + socket.getRemoteDevice());
//                    Toast.makeText(getApplicationContext(), "Device Connected", Toast.LENGTH_SHORT).show();
                    // Start managing the connection //TODO: this might need to be changed...
                    mHandler.obtainMessage(SUCCESS_CONNECT, socket).sendToTarget();

                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            System.out.println("Thread " + threadName + " exiting");
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {}
        }
    }

    public class ManageThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ManageThread(BluetoothSocket socket) {
            System.out.println("DeBug - Starting ManageThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {}

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            System.out.println("DeBug - Running ManageThread");
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    System.out.println("Waiting for input from buffer...");
                    bytes = mmInStream.read(buffer);
                    String string = new String(buffer, 0, bytes);
                    buffer[bytes] = '\0';
                    System.out.println("Input Received");

                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, string)
                            .sendToTarget(); // TODO: Problem is here

                    // Clear the buffer

                } catch (IOException e) {
                    break;
                }
            }
            System.out.println("DeBug - Ending ManageThread");

        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {}
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {}
        }
    }
}
