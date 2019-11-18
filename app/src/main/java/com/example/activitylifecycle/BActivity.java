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

public class BActivity extends AppCompatActivity {

    private Button mB_StartA;
    private Button mB_StartC;
    private Button mFinishB;
    private Button mB_Dialog;

    private TextView mBT1;
    private TextView mBT2;

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
        setContentView(R.layout.activity_b);

        mB_StartA=(Button)findViewById(R.id.start_A_button);
        mB_StartA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=MainActivity.newIntent(BActivity.this,false);
                startActivity(intent);
            }
        });

        mB_StartC=(Button)findViewById(R.id.start_C_button);
        mB_StartC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BActivity.this,CActivity.class);
                startActivity(intent);
            }
        });

        mFinishB=(Button)findViewById(R.id.finish_B_button);
        mFinishB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatusB="Activity B.onDestroy()";
                showLifecycleMethodList(mStatusB);

                SharedPreferences.Editor editor=getSharedPreferences("Destroy",MODE_PRIVATE).edit();
                editor.putBoolean("BisDestroyed",true);
                editor.commit();

                finish();
            }
        });

        mB_Dialog=(Button)findViewById(R.id.show_dialog_button);
        mB_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BActivity.this,DialogActivity.class);
                startActivity(intent);
            }
        });

        mBT1=(TextView)findViewById(R.id.BT1);
        mBT2=(TextView)findViewById(R.id.BT2);

        mStatusB="Activity B.onCreate()";

        showLifecycleMethodList(mStatusB);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mStatusB="Activity B.onStart()";
        showLifecycleMethodList(mStatusB);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mStatusB="Activity B.onResume()";
        showLifecycleMethodList(mStatusB);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mStatusB="Activity B.onPause()";
        showLifecycleMethodList(mStatusB);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mStatusB="Activity B.onStop()";
        showLifecycleMethodList(mStatusB);
    }

    @Override
    protected void onDestroy(){
        mStatusB="Activity B.onDestroy()";
        //setLifecycleMethodList(mStatusB);
        showLifecycleMethodList(mStatusB);
        super.onDestroy();

    }

    public void showLifecycleMethodList(String ActivityStatus){
        mStatusB=ActivityStatus;
        setStatusB(mStatusB);
        mStatusABC=getLifecycleMethodList();
        mStatusABC=ActivityStatus+'\n'+mStatusABC;
        setLifecycleMethodList(mStatusABC);

        mStatusA=getStatusA();
        mStatusC=getStatusC();

        //Context otherAppContext=createPackageContext("com.example.activitylifecycle",CONTEXT_IGNORE_SECURITY);
        SharedPreferences pref=getSharedPreferences("Destroy",MODE_PRIVATE);
        boolean AisDestroy=pref.getBoolean("AisDestroy",false);
        boolean BisDestroy=pref.getBoolean("BisDestroy",false);
        boolean CisDestroy=pref.getBoolean("CisDestroy",false);

        //
        if(mStatusA.equals("Activity A.onPause()")){
            mStatusA="Activity A.onStop()";
            mStatusABC=mStatusA+"\n"+mStatusABC;
        }
        if(mStatusC.equals("Activity C.onPause()")){
            mStatusC="Activity C.onStop()";
            mStatusABC=mStatusC+"\n"+mStatusABC;
        }



        mA=mStatusA.replace(".on",": ").replace("()","")+"d";
        mA=mA.replace("opd","opped");
        mB=mStatusB.replace(".on",": ").replace("()","")+"d";
        mB=mB.replace("opd","opped");
        mC=mStatusC.replace(".on",": ").replace("()","")+"d";
        mC=mC.replace("opd","opped");
        //
        if(!mStatusC.equals("")){
            ActivityStatus=mA +'\n'+mB +'\n'+mC;
        }
        if(mStatusC.equals("")){
            ActivityStatus=mA +'\n'+mB;
        }

        //ActivityStatus=getStatusA()+"\n"+getStatusB()+"\n"+getStatusC();
        mBT1.setText(mStatusABC);
        mBT2.setText(ActivityStatus);
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
