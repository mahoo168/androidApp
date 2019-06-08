package com.example.maripet;
import com.example.maripet.definition;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLEBLUETOOTH = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothSocket mBluetoothSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private final UUID UUID_SSP = UUID.fromString("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bluetoothAdapter
        //bluetooth対応デバイスか確認
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null){
            Log.d("onCreate", "Bluethooth is supported.");

            //bluetoothがonになっているか確認
            if(mBluetoothAdapter.isEnabled()){
            }else{
                Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(bluetoothIntent, REQUEST_ENABLEBLUETOOTH  );
            }
        }
        else{
            Log.d("onCreate", "Bluethooth is not supported.");
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int ResultCode, Intent date){
        switch (requestCode){
            case REQUEST_ENABLEBLUETOOTH:
            {
                if(requestCode == Activity.RESULT_CANCELED){
                    finish();
                }
            }
            break;
        }
    }

    private void getBluttoothDevice()
    {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice deviec : pairedDevices){
            if(deviec.getUuids()[0].getUuid() == UUID_SSP){
                mBluetoothDevice = deviec;
                try{
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(UUID_SSP);
                    setupBluetoothStream();
                }
                catch (IOException e){

                }
            }
        }
    }

    private void setupBluetoothStream(){
        if(mBluetoothSocket != null){
            try{
                mOutputStream = mBluetoothSocket.getOutputStream();
                mInputStream = mBluetoothSocket.getInputStream();
            }catch (IOException e){

            }
        }
    }


    //control machine
    private void go(){
        bluetoothWrite(definition.CMD_GO);
    }
    private void back(){
        bluetoothWrite(definition.CMD_BACK);
    }
    private void right(){
        bluetoothWrite(definition.CMD_RIGHT);
    }
    private void left(){
        bluetoothWrite(definition.CMD_LEFT);
    }

    private void bluetoothWrite(int cmd){
        if(mOutputStream != null){
            try{
                mOutputStream.write(cmd);
            }catch (IOException e){

            }
        }
    }


    //button
    public void onClick_Go(View v){
        go();
    }
    public void onClick_Back(View v){
        back();
    }
    public void onClick_Left(View v){
        left();
    }
    public void onClick_Right(View v){
        right();
    }
}
