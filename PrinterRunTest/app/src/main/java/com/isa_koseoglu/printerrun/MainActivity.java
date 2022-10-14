package com.isa_koseoglu.printerrun;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btn_findPrint,btn_runPrint;
    EditText txt_findPrintID,txt_contentEnter;
    TextView lbl_contentPrint,lbl_controlPrint;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* class */

        /* textview to match */
        lbl_contentPrint=(TextView) findViewById(R.id.lbl_PrinterContentx);
        lbl_controlPrint=(TextView)findViewById(R.id.lbl_PrinterControl);
        /*-------------------------------------------*/
        /* edit text  to match */
        txt_findPrintID=(EditText)findViewById(R.id.txt_Print_ID);
        txt_contentEnter=(EditText) findViewById(R.id.txt_Printer_Write);
        /*---------------------------------------------*/
        /* button to match */
        btn_findPrint=(Button) findViewById(R.id.btn_Find_Printer);
        btn_runPrint=(Button) findViewById(R.id.btn_PrinterRun);
        /*----------------------------------------------*/

        /* Printer Control */

        btn_findPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_findPrintID.getText().toString().isEmpty()){
                    lbl_controlPrint.setText("please enter printer name");
                }
                else{
                    PrinterControl printerControl=new PrinterControl(txt_findPrintID.getText().toString());
                    if(printerControl.FindPrint()){
                        txt_findPrintID.setEnabled(false);
                        btn_findPrint.setEnabled(false);
                        txt_contentEnter.setEnabled(true);
                        btn_runPrint.setEnabled(true);
                        lbl_controlPrint.setText("Printer Divace ID = "+printerControl.printerDevice.toString());
                        lbl_controlPrint.setBackgroundColor(Color.GREEN);

                    }
                }
            }
        });
        /* printer run*/

        btn_runPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_contentEnter.getText().toString().isEmpty()){
                    lbl_contentPrint.setVisibility(View.VISIBLE);
                    lbl_contentPrint.setText("cannot be left blank");
                }
                else{
                    PrinterControl printerControl=new PrinterControl(txt_findPrintID.getText().toString());
                    printerControl.RunPrint(txt_contentEnter.getText().toString(), (byte) 10);
                    lbl_contentPrint.setVisibility(View.INVISIBLE);
                    txt_contentEnter.setText("");
                }
            }
        });

    }

}
 class PrinterControl {
    PrinterBluetothService print=new PrinterBluetothService();
    public static BluetoothDevice printerDevice;
    public static String _printID;
    PrinterControl(String printid){
        _printID=printid;
    }
    public boolean FindPrint(){
        print.printer_id=_printID;
        boolean find=false;
        find=print.FindPRINT();
        printerDevice=print.devicex;
        return find;
    }
    public void RunPrint(String content,byte nusha){
        print.printer_id=_printID;
        print.FindPRINT();
        print.OpenPRINT();
        print.ContentWritePRINT(content,nusha);
        print.ClosePRINT();
    }

}