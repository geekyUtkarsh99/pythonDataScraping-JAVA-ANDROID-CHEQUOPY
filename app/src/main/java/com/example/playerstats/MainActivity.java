package com.example.playerstats;
/*
 * Copyright 2021 Utkarsh Choudhary
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private ListView list;
    private Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            list = (ListView)findViewById(R.id.list);
            if(!Python.isStarted())
                Python.start(new AndroidPlatform(this));

            Python p = Python.getInstance();
            PyObject py = p.getModule("pyscript");
            PyObject pi = py.callAttr("return_player1");
            PyObject pi1 = py.callAttr("return_player2");
            PyObject pi2 = py.callAttr("return_player3");

            String[] rank = new String[]{
                    "1.","2.","3."
            };

            String [] player = new String[3];
            player[0] = pi.toString();
            player[1] = pi1.toString();
            player[2] = pi2.toString();

            PyObject q1 = py.callAttr("qbr1");
            PyObject q2 = py.callAttr("qbr2");
            PyObject q3 = py.callAttr("qbr3");


            String[] qbr = new String [3];
            qbr[0] = q1.toString();
            qbr[1] = q2.toString();
            qbr[2] = q3.toString();

      /*  String[] paa = {
                "paa","paa","paa"
        };

        String[] plays = {
                "plays","plays","plays"

        };
        */

            ArrayList<HashMap<String,String>> listv = new ArrayList<>();

            for(int i = 0;i<3;i++){
                HashMap<String,String> hash = new HashMap<>();
                hash.put("rank",rank[i]);
                hash.put("player",player[i]);
                hash.put("qbr","QBR : "+ qbr[i]);
                listv.add(hash);
            }

            String[]key = {"rank","player","qbr"};
            int[] id = {R.id.Rank,R.id.player,R.id.qbr};

            SimpleAdapter smp = new SimpleAdapter(this,listv,R.layout.statistics,key,id);
            list.setAdapter(smp);





            if(user ==  null){

                startActivity(new Intent(MainActivity.this,login.class));
                finish();

            }


            b = (Button)findViewById(R.id.logout);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,login.class));
                    finish();
                }
            });


        } else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton(0,"try again", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                Log.d("", "Show Dialog: " + e.getMessage());
            }
        }



        }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}
