package com.example.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    // Bluetooth Connection Attributes;
    private ListView listView;
    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    private HashMap<Integer, BluetoothDevice> deviceHashMaps = new HashMap<>();
    private InputStream inputStream;
    private OutputStream outputStream;
    private final ArrayList<String> moveForwardCommands = new ArrayList<String>(Arrays.asList("đi thẳng", "tiến lên", "go"));
    private final ArrayList<String> moveBackwardCommands = new ArrayList<String>(Arrays.asList("lùi lại", "quay lại"));
    private final ArrayList<String> stopCommands = new ArrayList<String>(Arrays.asList("dừng lại", "dừng", "stop"));
    private final ArrayList<String> turnRightCommands = new ArrayList<String>(Arrays.asList("rẽ phải", "quẹo phải", "turn right"));
    private final ArrayList<String> turnLeftCommands = new ArrayList<String>(Arrays.asList("rẽ trái", "quẹo trái", "turn left"));

    private BluetoothSocket btSock1;
    private BluetoothSocket btSock2;
    private BluetoothSocket btSock3;
    private BluetoothSocket btSock4;
    private BluetoothSocket btSock5;
    private BluetoothSocket btSock6;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Speech to Text Attributes
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Speech to Text part
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        // Bluetooth Part
        Button btn = (Button)findViewById(R.id.btnGet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bAdapter==null){
                    Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                        ArrayList list = new ArrayList();

                        if (pairedDevices.size() > 0) {
                            int count = 0;
                            for (BluetoothDevice device : pairedDevices) {
                                count++;
                                deviceHashMaps.put(count, device);
                                list.add("id"+count + " )\t" + device.getName() + " [MAC: " + device.getAddress() + "]\n");
                            }
                            listView = (ListView) findViewById(R.id.deviceList);
                            aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(aAdapter);
                        }
                    } catch (SecurityException se){
                        se.printStackTrace();
                    }
                }
            }
        });
        
        Button btn1 = (Button)findViewById(R.id.btn1);
        Button btn2 = (Button)findViewById(R.id.btn2);
        Button btn3 = (Button)findViewById(R.id.btn3);
        Button btn4 = (Button)findViewById(R.id.btn4);
        Button btn5 = (Button)findViewById(R.id.btn5);
        Button btn6 = (Button)findViewById(R.id.btn6);
        TextView errMessage = (TextView)findViewById(R.id.errMessage);
        TextView currentStatus = (TextView)findViewById(R.id.txtCurrentStatus);
        TextView deviceMessage = (TextView)findViewById(R.id.deviceMessage);
        TextView deviceMsgDisconnect = (TextView)findViewById(R.id.deviceMsgDisconnect);
        Button destroyConnectBtn = (Button)findViewById(R.id.destroy_connect_btn);

        Button btnChangeType = (Button)findViewById(R.id.btnChangeType);
        Button btnGoForward = (Button)findViewById(R.id.btnGoForward);
        Button btnGoBackward = (Button)findViewById(R.id.btnGoBackward);
        Button btnTurnLeft = (Button)findViewById(R.id.btnTurnLeft);
        Button btnTurnRight = (Button)findViewById(R.id.btnTurnRight);
        Button btnStop = (Button)findViewById(R.id.btnStop);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(1) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }

                    btSock1 = deviceHashMaps.get(1).createRfcommSocketToServiceRecord(MY_UUID);
                    btSock1.connect();
                    if (btSock1.isConnected()) {
                        inputStream = btSock1.getInputStream();
                        outputStream = btSock1.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 1 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 1");
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(2) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }
                    btSock2 = deviceHashMaps.get(2).createRfcommSocketToServiceRecord(MY_UUID);
                    if (btSock2.isConnected()) {
                        btSock2.connect();
                        inputStream = btSock2.getInputStream();
                        outputStream = btSock2.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 2 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 2");
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(3) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }
                    btSock3 = deviceHashMaps.get(3).createRfcommSocketToServiceRecord(MY_UUID);
                    btSock3.connect();
                    if (btSock3.isConnected()){
                        inputStream = btSock3.getInputStream();
                        outputStream = btSock3.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 3 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 3");
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(4) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }
                    btSock4 = deviceHashMaps.get(4).createRfcommSocketToServiceRecord(MY_UUID);
                    btSock4.connect();
                    if (btSock4.isConnected()){
                        inputStream = btSock4.getInputStream();
                        outputStream = btSock4.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 4 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 4");

                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(5) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }
                    btSock5 = deviceHashMaps.get(5).createRfcommSocketToServiceRecord(MY_UUID);
                    btSock5.connect();
                    if (btSock5.isConnected()) {
                        inputStream = btSock5.getInputStream();
                        outputStream = btSock5.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 5 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 5");
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws SecurityException{
                try {
                    if (deviceHashMaps.get(6) == null){
                        errMessage.setText("Thiết bị NULL");
                        return;
                    }
                    btSock6 = deviceHashMaps.get(6).createRfcommSocketToServiceRecord(MY_UUID);
                    btSock6.connect();
                    if (btSock6.isConnected()) {
                        inputStream = btSock6.getInputStream();
                        outputStream = btSock6.getOutputStream();
                        deviceMessage.setText("Đã kết nối thiết bị 6 !");
                        errMessage.setText("");
                        deviceMsgDisconnect.setText("");
                    } else errMessage.setText("IO Exception Connect 6");
                } catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        btnChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(currentStatus.getText().toString().contains("tay") ) {
                        currentStatus.setText("Di chuyển tự động");
                    }
                    else {
                        currentStatus.setText("Điều khiển bằng tay");
                    }
                    setOutputStream('c');

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setOutputStream('f');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnGoBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setOutputStream('b');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnTurnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setOutputStream('l');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnTurnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setOutputStream('r');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setOutputStream('s');
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        destroyConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (btSock1 != null) {
                        btSock1.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 1 !");
                    }
                    if (btSock2 != null) {
                        btSock2.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 2 !");
                    }
                    if (btSock3 != null) {
                        btSock3.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 3 !");
                    }
                    if (btSock4 != null) {
                        btSock4.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 4 !");
                    }
                    if (btSock5 != null) {
                        btSock5.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 5 !");
                    }
                    if (btSock6 != null) {
                        btSock6.close();
                        errMessage.setText("");
                        deviceMessage.setText("");
                        deviceMsgDisconnect.setText("Đã đóng kết nối thiết bị 6 !");
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void convertSpeechToCommand(String speech) throws IOException{
        String lowercaseSpeech = speech.toLowerCase();

        // TURN LEFT
        if (turnLeftCommands.contains(lowercaseSpeech)){
            outputStream.write('l');
        }

        // TURN RIGHT
        if (turnRightCommands.contains(lowercaseSpeech)){
            outputStream.write('r');
        }

        // FORWARD
        if (moveForwardCommands.contains(lowercaseSpeech)){
            outputStream.write('f');
        }

        // BACKWARD
        if (moveBackwardCommands.contains(lowercaseSpeech)){
            outputStream.write('b');
        }

        // STOP
        if (stopCommands.contains(lowercaseSpeech)){
            outputStream.write('s');
        }

    }

    private void setOutputStream(char command) throws IOException {
        outputStream.write(command);
    }

    /* Showing google speech input dialog */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /* Receiving Speech Input */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    for (String res : result){
//                        System.out.println(res);
//                    }
                    txtSpeechInput.setText(result.get(0));
                    try {
                        convertSpeechToCommand(result.get(0));
                    } catch (IOException ioException){
                        ioException.printStackTrace();
                    }
                }
                break;
            }
            default: {
                System.out.println("Error happened \n");
            }

        }
    }
}
