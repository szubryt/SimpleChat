package ps.borsuquesquad.pl.firebasechat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;


    Firebase userToFriend, friendToUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = findViewById(R.id.layout_vertical);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        // firebase references
        Firebase.setAndroidContext(this);
        userToFriend = new Firebase("https://fir-chat-4efae.firebaseio.com/messages/"
                + UserDetails.username + "_" + UserDetails.friend);
        friendToUser = new Firebase("https://fir-chat-4efae.firebaseio.com/messages/"
                + UserDetails.friend + "_" + UserDetails.username);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    userToFriend.push().setValue(map);
                    friendToUser.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });



        // event listener


        userToFriend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String user = map.get("user").toString();

                if(user.equals(UserDetails.username)){
                    addNameBox(UserDetails.username, 1);
                    addMessageBox(message, 1);

                }
                else{
                    addNameBox(UserDetails.friend, 2);
                    addMessageBox( message, 2);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }
    public void addMessageBox(String message, int type){

        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;


        if(type == 1) {

            layoutParams.gravity = Gravity.START;
            textView.setBackgroundResource(R.drawable.left);
            textView.setTextColor(getResources().getColor(R.color.chatBlack));
            textView.setPadding(20,20,20,20);
        }
        else{
            layoutParams.gravity = Gravity.END;
            textView.setBackgroundResource(R.drawable.right);
            textView.setTextColor(getResources().getColor(R.color.chatBlack));
            textView.setPadding(20,20,20,20);
        }
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }


    // who send message
    public void addNameBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;

        if(type == 1) {
            layoutParams.gravity = Gravity.START;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
        else{
            layoutParams.gravity = Gravity.END;
            textView.setPadding(20,20,20,20);
            textView.setTextColor(getResources().getColor(R.color.chatWhite));
        }
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }


}
