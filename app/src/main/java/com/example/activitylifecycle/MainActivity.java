package com.example.activitylifecycle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button mA_StartB;
    private Button mA_StartC;
    private Button mFinishA;
    private Button mA_Dialog;

    private TextView mAT1;
    private TextView mAT2;

    private static String mA="";
    private static String mB="";
    private static String mC="";
    private static String mStatusA="";
    private static String mStatusB="";
    private static String mStatusC="";
    private static String mStatusABC="";

    public static boolean AisDestroy=false;
    public static boolean BisDestroy=false;
    public static boolean CisDestroy=false;


    private boolean A_firstCreated=true;
    public static final String EXTRA_A_IS_FIRST_CREATED
            ="com.bignerdranch.android.activitylifecycle.extra.a.is.first.created";

    public static Intent newIntent(Context packageContext,boolean A_firstCreated){
        Intent intent=new Intent(packageContext,MainActivity.class);
        intent.putExtra(EXTRA_A_IS_FIRST_CREATED,A_firstCreated);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        A_firstCreated=getIntent().getBooleanExtra(EXTRA_A_IS_FIRST_CREATED,true);

        mA_StartB=(Button)findViewById(R.id.start_B_button);
        mA_StartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BActivity.class);
                startActivity(intent);
            }
        });

        mA_StartC=(Button)findViewById(R.id.start_C_button);
        mA_StartC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CActivity.class);
                startActivity(intent);
            }
        });

        mFinishA=(Button)findViewById(R.id.finish_A_button);
        mFinishA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatusA="Activity A.onDestroy()";
                showLifecycleMethodList(mStatusA);

                SharedPreferences.Editor editor=MainActivity.this.getSharedPreferences("Destroy",MODE_PRIVATE).edit();
                editor.putBoolean("AisDestroyed",true);
                editor.commit();

                finish();
            }
        });

        mA_Dialog=(Button)findViewById(R.id.show_dialog_button);
        mA_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DialogActivity.class);
                startActivity(intent);
                //showDialog();
            }
        });

        mAT1=(TextView)findViewById(R.id.AT1);
        mAT2=(TextView)findViewById(R.id.AT2);

        mStatusA="Activity A.onCreate()";

        if(A_firstCreated){
            init_data();
            A_firstCreated=false;
        }

        showLifecycleMethodList(mStatusA);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mStatusA="Activity A.onStart()";
        mA="Activity A: Started";
        showLifecycleMethodList(mStatusA);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mStatusA="Activity A.onResume()";
        mA="Activity A: Resumed";
        showLifecycleMethodList(mStatusA);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mStatusA="Activity A.onPause()";
        mA="Activity A: Paused";
        showLifecycleMethodList(mStatusA);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mStatusA="Activity A.onStop()";
        showLifecycleMethodList(mStatusA);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mStatusA="Activity A.onDestroy()";
        showLifecycleMethodList(mStatusA);


    }

    public void showLifecycleMethodList(String ActivityStatus){
        mStatusA=ActivityStatus;

        setStatusA(mStatusA);
        mStatusABC=getLifecycleMethodList();
        mStatusABC=ActivityStatus+'\n'+mStatusABC;
        setLifecycleMethodList(mStatusABC);

        mStatusB=getStatusB();
        mStatusC=getStatusC();

        //Context otherAppContext=createPackageContext("com.example.activitylifecycle",Context.CONTEXT_IGNORE_SECURITY);

        SharedPreferences pref=getSharedPreferences("Destroy",MODE_PRIVATE);
        boolean AisDestroy=pref.getBoolean("AisDestroy",false);
        boolean BisDestroy=pref.getBoolean("BisDestroy",false);
        boolean CisDestroy=pref.getBoolean("CisDestroy",false);

        //A运行的时候，B或C为Paused状态就要变为Stopped状态
        if(mStatusB.equals("Activity B.onPause()")){
            mStatusB="Activity B.onStop()";
            mB="Activity B: Stopped";
            mStatusABC=mStatusB+"\n"+mStatusABC;
        }
        if(mStatusC.equals("Activity C.onPause()")){
            mStatusC="Activity C.onStop()";
            mC="Activity C: Stopped";
            mStatusABC=mStatusC+"\n"+mStatusABC;
        }


        mA=mStatusA.replace(".on",": ").replace("()","")+"d";
        mA=mA.replace("opd","opped");
        mB=mStatusB.replace(".on",": ").replace("()","")+"d";
        mB=mB.replace("opd","opped");
        mC=mStatusC.replace(".on",": ").replace("()","")+"d";
        mC=mC.replace("opd","opped");

        //
        if(!mStatusB.equals("")&&!mStatusC.equals("")){
            ActivityStatus=mA +'\n'+mB +'\n'+mC;
        }
        if(!mStatusB.equals("")&&mStatusC.equals("")){
            ActivityStatus=mA +'\n'+mB;
        }
        if(mStatusB.equals("")&&!mStatusC.equals("")){
            ActivityStatus=mA +'\n'+mC;
        }
        if(mStatusB.equals("")&&mStatusC.equals("")){
            ActivityStatus=mA;
        }


        mAT1.setText(mStatusABC);
        mAT2.setText(ActivityStatus);
    }

    public void init_data(){
        setLifecycleMethodList("");
        setStatusA("");
        setStatusB("");
        setStatusC("");
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

    public void showDialog(){

        mStatusABC=getLifecycleMethodList();
        mStatusABC="Activity A.onPause()"+'\n'+mStatusABC;
        //setLifecycleMethodList(mStatusABC);
        showLifecycleMethodList(mStatusABC);

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog=builder.create();
        View DialogView =View.inflate(MainActivity.this,R.layout.dialog,null);
        dialog.setView(DialogView);
        dialog.show();

        final Button mDialogButton=findViewById(R.id.close_dialog_button);
        mDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}