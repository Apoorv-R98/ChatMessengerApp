package com.apoorv.socialwhatsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsappChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView chatListView;
    private ArrayAdapter adapter;
    private String selecteduser;
    private ArrayList<String> chatsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_chat);

        selecteduser = getIntent().getStringExtra("selectedUser");
        FancyToast.makeText(this,"Chat with " + selecteduser + "Now!!!", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

        findViewById(R.id.btnSend).setOnClickListener(this);

        chatListView = findViewById(R.id.chatListView);
        chatsList= new ArrayList();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatsList);
        chatListView.setAdapter(adapter);

        try {

            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selecteduser);

            secondUserChatQuery.whereEqualTo("waSender", selecteduser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            //here or is used to add all the queries to myquery

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selecteduser)) {
                                waMessage = selecteduser + ": " + waMessage;
                            }

                            chatsList.add(waMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        final EditText edtMessage = findViewById(R.id.edtSend);

        ParseObject chat = new ParseObject("Chat");
        chat.put("waSender", ParseUser.getCurrentUser().getUsername());
        chat.put("waTargetRecipient",selecteduser);
        chat.put("waMessage",edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null)
                {
                    FancyToast.makeText(WhatsappChatActivity.this,"Message from "+ ParseUser.getCurrentUser().getUsername() + " sent to " + selecteduser,Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                    chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });
    }
}
