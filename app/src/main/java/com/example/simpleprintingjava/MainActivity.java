package com.example.simpleprintingjava;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PrintingCallback {
   public EditText name,regi_num,mobile,mail,pass,c_pass;
   Button print;
   Printing printer=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Printooth.INSTANCE.hasPairedPrinter())
            printer = Printooth.INSTANCE.printer();
        init();

    }

    private void init(){
        name=findViewById(R.id.name);
        regi_num=findViewById(R.id.regi);
        mobile=findViewById(R.id.mobile_no);
        mail=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        c_pass=findViewById(R.id.c_password);
        print=findViewById(R.id.print);


        if(printer!=null){
            printer.setPrintingCallback(this);
        }
       print.setOnClickListener(view -> {
         //  Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
           if(!Printooth.INSTANCE.hasPairedPrinter()){
               startActivityForResult(new Intent(this,ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
              Toast.makeText(this,System.getProperty("os.arch"),Toast.LENGTH_LONG).show();
           }
           else{
               printtext();
           }
       });

    }

    private void printtext() {
        ArrayList<Printable> printbles=new ArrayList<>();
        printbles.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());

        printbles.add(new TextPrintable.Builder().setText("Test").setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252()).setNewLinesAfter(1).build());
        printer.print(printbles);


    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(this,"Connecting With Printer..",Toast.LENGTH_LONG).show();
    }

    @Override
    public void connectionFailed(String s) {
        Toast.makeText(this,"Failed.. "+s,Toast.LENGTH_LONG).show();


    }

    @Override
    public void disconnected() {

    }

    @Override
    public void onError(String s) {
        Toast.makeText(this,"Failed.. "+s,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onMessage(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();


    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(this,"order sent to printer",Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== ScanningActivity.SCANNING_FOR_PRINTER && resultCode== Activity.RESULT_OK){
            initprinting();
        }
    }
    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        initprinting();
                        // ToDo : Do your stuff...
                    } else if(result.getResultCode() == 321) {
                        // ToDo : Do your stuff...
                    }
                }
            });

    private void initprinting() {
        if(!Printooth.INSTANCE.hasPairedPrinter()){
            printer=Printooth.INSTANCE.printer();
        }
        if(printer!=null){
            printer.setPrintingCallback(this);

        }
    }
}