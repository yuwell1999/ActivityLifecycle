package com.example.activitylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CActivity extends AppCompatActivity {

    private Button mC_StartA;
    private Button mC_StartB;
    private Button mFinishC;
    private Button mC_Dialog;

    private TextView mCT1;
    private TextView mCT2;

    private static String mA="";
    private static String mB="";
    private static String mC="";
    private static String mStatusA="";
    private static String mStatusB="";
    private static String mStatusC="";
    private static String mStatusABC="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);

        mC_StartA=(Button)findViewById(R.id.start_A_button);
        mC_StartA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=MainActivity.newIntent(CActivity.this,false);
                startActivity(intent);
            }
        });

        mC_StartB=(Button)findViewById(R.id.start_B_button);
        mC_StartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CActivity.this,BActivity.class);
                startActivity(intent);
            }
        });

        mFinishC=(Button)findViewById(R.id.finish_C_button);
        mFinishC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatusC="Activity C.onDestroy()";
                showLifecycleMethodList(mStatusC);

                SharedPreferences.Editor editor=getSharedPreferences("Destroy",MODE_PRIVATE).edit();
                editor.putBoolean("CisDestroyed",true);
                editor.commit();

                finish();
            }
        });

        mC_Dialog=(Button)findViewById(R.id.show_dialog_button);
        mC_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CActivity.this,DialogActivity.class);
                startActivity(intent);
            }
        });

        mCT1=(TextView)findViewById(R.id.CT1);
        mCT2=(TextView)findViewById(R.id.CT2);

        mStatusC="Activity C.onCreate()";

        showLifecycleMethodList(mStatusC);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mStatusC="Activity C.onStart()";
        showLifecycleMethodList(mStatusC);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mStatusC="Activity C.onResume()";
        showLifecycleMethodList(mStatusC);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mStatusC="Activity C.onPause()";
        showLifecycleMethodList(mStatusC);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mStatusC="Activity C.onStop()";
        showLifecycleMethodList(mStatusC);
    }

    @Override
    protected void onDestroy(){
        mStatusC="Activity C.onDestroy()";
        showLifecycleMethodList(mStatusC);
        super.onDestroy();
    }

    public void showLifecycleMethodList(String ActivityStatus){
        mStatusC=ActivityStatus;
        setStatusC(mStatusC);
        mStatusABC=getLifecycleMethodList();
        mStatusABC=ActivityStatus+'\n'+mStatusABC;
        setLifecycleMethodList(mStatusABC);

        mStatusA=getStatusA();
        mStatusB=getStatusB();
        //
        if(mStatusA.equals("Activity A.onPause()")){
            mStatusA="Activity A.onStop()";
            mStatusABC=mStatusA+"\n"+mStatusABC;
        }
        if(mStatusB.equals("Activity B.onPause()")){
            mStatusB="Activity B.onStop()";
            mStatusABC=mStatusB+"\n"+mStatusABC;
        }


//        SharedPreferences pref=getSharedPreferences("Destroy",MODE_PRIVATE);
//        boolean AisDestroy=pref.getBoolean("AisDestroy",false);
//        boolean BisDestroy=pref.getBoolean("BisDestroy",false);
//        boolean CisDestroy=pref.getBoolean("CisDestroy",false);

        mA=mStatusA.replace(".on",": ").replace("()","")+"d";
        mA=mA.replace("opd","opped");
        mB=mStatusB.replace(".on",": ").replace("()","")+"d";
        mB=mB.replace("opd","opped");
        mC=mStatusC.replace(".on",": ").replace("()","")+"d";
        mC=mC.replace("opd","opped");

        //
        if(!mStatusB.equals("")){
            ActivityStatus=mA +"\n"+mB+'\n'+mC;
        }
        if(mStatusB.equals("")){
            ActivityStatus=mA +'\n'+mC;
        }

        mCT1.setText(mStatusABC);
        mCT2.setText(ActivityStatus);
    }

    public void setLifecycleMethodList(String input){
        FileOutputStream fos=null;
        BufferedWriter bw=null;
        try{
            fos=openFileOutput("LifecycleMethodList", Context.MODE_PRIVATE);
            bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(input);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            try{
                if(bw!=null){
                    bw.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public void setStatusA(String StatusA){
        FileOutputStream fos=null;
        BufferedWriter bw=null;
        try{
            fos=openFileOutput("A_Status", Context.MODE_PRIVATE);
            bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(StatusA);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            try{
                if(bw!=null){
                    bw.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public void setStatusB(String StatusB){
        FileOutputStream fos=null;
        BufferedWriter bw=null;
        try{
            fos=openFileOutput("B_Status", Context.MODE_PRIVATE);
            bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(StatusB);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            try{
                if(bw!=null){
                    bw.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public void setStatusC(String StatusC){
        FileOutputStream fos=null;
        BufferedWriter bw=null;
        try{
            fos=openFileOutput("C_Status", Context.MODE_PRIVATE);
            bw=new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(StatusC);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            try{
                if(bw!=null){
                    bw.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }

    public String getLifecycleMethodList(){
        FileInputStream fis=null;
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();

        try{
            fis=openFileInput("LifecycleMethodList");
            br=new BufferedReader(new InputStreamReader(fis));
            String rline="";
            while ((rline=br.readLine())!=null){
                sb.append(rline);
                sb.append("\n");
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public String getStatusA(){
        FileInputStream fis=null;
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();

        try{
            fis=openFileInput("A_Status");
            br=new BufferedReader(new InputStreamReader(fis));
            String rline="";
            while ((rline=br.readLine())!=null){
                sb.append(rline);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public String getStatusB(){
        FileInputStream fis=null;
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();

        try{
            fis=openFileInput("B_Status");
            br=new BufferedReader(new InputStreamReader(fis));
            String rline="";
            while ((rline=br.readLine())!=null){
                sb.append(rline);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public String getStatusC(){
        FileInputStream fis=null;
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();

        try{
            fis=openFileInput("C_Status");
            br=new BufferedReader(new InputStreamReader(fis));
            String rline="";
            while ((rline=br.readLine())!=null){
                sb.append(rline);
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }finally{
            if(br!=null){
                try{
                    br.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}