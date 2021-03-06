package com.example.carrc.seniorproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class EmployeeChatActivity extends AppCompatActivity {

    String activeUser = "";
    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    public void sendMessage(View view){

        final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);

        ParseObject message = new ParseObject("Message");

        final String messageContent = chatEditText.getText().toString();

        message.put("sender", ParseUser.getCurrentUser().getUsername());
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
        setContentView(R.layout.activity_employee_chat);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.carrc.seniorproject", Context.MODE_PRIVATE );
        ParseUser.logInInBackground(sharedPreferences.getString("username", ""), sharedPreferences.getString("password", ""));

        Intent intent = getIntent();
        activeUser = intent.getStringExtra("staffName");

        setTitle("Chat with " + activeUser);

        final ListView chatListView = (ListView) findViewById(R.id.chatListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(arrayAdapter);

        new CountDownTimer(600000, 5000) {

            public void onTick(long millisUntilFinished) {

                ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");
                query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
                query1.whereEqualTo("recipient", activeUser);

                ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");
                query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
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

                                    if(!message.getString("sender").matches(ParseUser.getCurrentUser().getUsername())){
                                        messageContent = "> " + messageContent;
                                    }

                                    messages.add(messageContent);

                                }
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
