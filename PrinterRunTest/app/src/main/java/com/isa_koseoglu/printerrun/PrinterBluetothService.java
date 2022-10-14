package com.isa_koseoglu.printerrun;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class PrinterBluetothService extends AppCompatActivity {

    LOGDESING _log = new LOGDESING(new ILogger[]{new CsystemPrint()});

    Activity activity;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;


    //    byte[] readBuffer;
//    int readBufferPosition;
//    volatile boolean stopWorker;
    byte FONT_TYPE;
    public static String printer_id;
    public static BluetoothDevice devicex;
    public PrinterBluetothService() {
    }


    public boolean FindPRINT() {
        boolean find=false;
        try {

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Device Bluetooth tidak Tersedia", Toast.LENGTH_SHORT).show();
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                     find=false;
                }
                startActivityForResult(enableBluetooth, 0);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(printer_id)) {
                        mmDevice = device;
                        devicex=mmDevice;
                        find=true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
             find =false;
        }
        return find;
    }

    public void OpenPRINT() {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            _log.LogYaz("openprint girdi");


            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            _log.LogYaz("openprint çıktı");
        }catch (Exception e){e.printStackTrace();_log.LogYaz("open print hata");}
    }
    public void ClosePRINT(){
        try {
            _log.LogYaz("closeprint giriş");
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            _log.LogYaz("closeprint çıkış");
        } catch (Exception e) {
            e.printStackTrace();
            _log.LogYaz("closprint hata");
        }
    }
    public void ContentWritePRINT(String content,byte nusha){
        try {
            _log.LogYaz("contentwriteprint giriş");
            if(!content.isEmpty()&&nusha!=0){
                for (int i=0 ;i<nusha ;i++){
                    mmOutputStream.write((content+"\n").getBytes(StandardCharsets.UTF_8));
                }
            }
            else{
                mmOutputStream.write("içerik ve nusha sayısı boş geçilemez".getBytes(StandardCharsets.UTF_8));
            }

            mmOutputStream.write("\n\n".getBytes());
            _log.LogYaz("contentwriteprint çıkış");
        }catch (Exception e){_log.LogYaz("YAZAMA HATASI");e.printStackTrace();}
    }

}
