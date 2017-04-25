package com.example.carrc.seniorproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    String activeUser = "";
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    String tableNumber;

    String formatedName;

    public void recreate(){
        startActivity(getIntent());
        finish();
    }

    public void sendChat(View view){

        final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);

        ParseObject message = new ParseObject("Message");

        final String messageContent = chatEditText.getText().toString();

        message.put("sender", formatedName);
        message.put("recipient", activeUser);
        message.put("message", messageContent);

        chatEditText.setText("");

        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){

                    messages.add(messageContent);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("staffName");

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        tableNumber = sharedPreferences.getString("tableNumber", "");

        formatedName = "table" + tableNumber;

        setTitle("Chat with " + activeUser);

        final ListView chatListView = (ListView) findViewById(R.id.chatListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(arrayAdapter);

        new CountDownTimer(600000, 5000) {

            public void onTick(long millisUntilFinished) {

                ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
                query1.whereEqualTo("sender", formatedName);
                query1.whereEqualTo("recipient", activeUser);

                ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
                query2.whereEqualTo("recipient", formatedName);
                query2.whereEqualTo("sender", activeUser);

                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                queries.add(query1);
                queries.add(query2);

                ParseQuery<ParseObject> query = ParseQuery.or(queries);

                query.orderByAscending("createdAt");

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        if(e == null){
                            if(objects.size() > 0){

                                messages.clear();

                                for(ParseObject message : objects){

                                    String messageContent = message.getString("message");

                                    if(!message.getString("sender").matches(formatedName)){
                                        messageContent = "> " + messageContent;
                                    }

                                    messages.add(messageContent);

                                }
                                chatListView.setSelection(chatListView.getAdapter().getCount()-1);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                        chatListView.setSelection(chatListView.getAdapter().getCount()-1);
                    }
                });

            }

            public void onFinish() {

            }
        }.start();
    }
}
