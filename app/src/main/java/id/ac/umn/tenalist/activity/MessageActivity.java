package id.ac.umn.tenalist.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.adapter.MessageAdapter;
import id.ac.umn.tenalist.model.Chat;
import id.ac.umn.tenalist.model.User;

public class MessageActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView name;
    private ImageButton btn_send;
    private EditText text_send;
    private MessageAdapter messageAdapter;
    private List<Chat> mChats;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final Context mContext = getApplicationContext();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        name = findViewById(R.id.name);
        photo = findViewById(R.id.photo);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        progressBar = findViewById(R.id.progress_bar_img);

        Intent messageIntent = getIntent();
        final String userId = messageIntent.getStringExtra("userId");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String message = text_send.getText().toString();
            if(!message.equals("")) {
                sendMessage(currentUser.getUid(), userId, message);
            } else {
                Toast.makeText(MessageActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
            }
        });

        if (UserData.user.getRole().equalsIgnoreCase("tenant")) {
            Toast.makeText(mContext, "tenant" + UserData.user.getRole(), Toast.LENGTH_SHORT).show();
            photo.setImageDrawable(this.getDrawable(R.drawable.ic_organizer));
            progressBar.setVisibility(View.GONE);
        }
        else if ((UserData.user.getRole().equalsIgnoreCase("organizer"))) {
            Toast.makeText(mContext, "organizer" + UserData.user.getRole(), Toast.LENGTH_SHORT).show();
            photo.setImageDrawable(this.getDrawable(R.drawable.ic_tenant));
            progressBar.setVisibility(View.GONE);
        }

        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());

                 //Glide.with(MessageActivity.this).load(ApiConfig.FINAL_URL + "profile/" + user.getPhoto()).into(photo);
                if(!user.getPhoto().equals("default")) {
                    Toast.makeText(mContext, " masuk glide" + user.getPhoto(), Toast.LENGTH_SHORT).show();
                    Glide.with(mContext)
                            .load(ApiConfig.FINAL_URL + "profile/" + user.getPhoto())
                            .signature(new ObjectKey(String.valueOf(UserData.picCount)))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    //Log.d("USER PHOTO", UserData.user.getRole());
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(photo);
                }

                getMessages(currentUser.getUid(), userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference("chats");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        chatsReference.push().setValue(hashMap);

        // Add sender to ChatsList
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference senderChatsList = FirebaseDatabase.getInstance().getReference("chatslist").child(currentUser.getUid()).child(receiver);
        senderChatsList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    senderChatsList.child("uid").setValue(receiver);
                    Toast.makeText(MessageActivity.this, "Chat List added", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add receiver to ChatsList
        final DatabaseReference receiverChatsList = FirebaseDatabase.getInstance().getReference("chatslist").child(receiver).child(currentUser.getUid());
        receiverChatsList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    receiverChatsList.child("uid").setValue(currentUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMessages(final String currentUserId, final String userid) {
        mChats = new ArrayList<>();
        DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference("chats");
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChats.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(currentUserId) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(currentUserId)) {
                        mChats.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChats);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
