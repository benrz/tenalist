package id.ac.umn.tenalist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

import id.ac.umn.tenalist.ApiConfig;
import id.ac.umn.tenalist.R;
import id.ac.umn.tenalist.UserData;
import id.ac.umn.tenalist.activity.MessageActivity;
import id.ac.umn.tenalist.model.BazaarDetail;
import id.ac.umn.tenalist.model.Chat;
import id.ac.umn.tenalist.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    String isLastMessage;
    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;

    public UserAdapter(Context mContext, List<User> mUsers, Boolean isChat) {
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.isChat = isChat; // User is being chat
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.name.setText(user.getName());

        if (user.getPhoto().equals("default")) {
            holder.progressBar.setVisibility(View.GONE);
            if (user.getRole().equalsIgnoreCase("tenant"))
                holder.photo.setImageDrawable(mContext.getDrawable(R.drawable.ic_tenant));
            else if ((user.getRole().equalsIgnoreCase("organizer")))
                holder.photo.setImageDrawable(mContext.getDrawable(R.drawable.ic_organizer));
        } else {
            Glide.with(mContext)
                    .load(ApiConfig.FINAL_URL + "profile/" + user.getPhoto())
                    .signature(new ObjectKey(String.valueOf(UserData.picCount)))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            if (user.getRole().equalsIgnoreCase("tenant"))
                                holder.photo.setImageDrawable(mContext.getDrawable(R.drawable.ic_tenant));
                            else if ((user.getRole().equalsIgnoreCase("organizer")))
                                holder.photo.setImageDrawable(mContext.getDrawable(R.drawable.ic_organizer));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.photo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(mContext, MessageActivity.class);
                messageIntent.putExtra("userId", user.getUid());
                mContext.startActivity(messageIntent);
            }
        });

        if (isChat) {
            getLastMessage(user.getUid(), holder.lastMessage);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference chatList = FirebaseDatabase.getInstance().getReference("chatslist");
                    chatList.child(currentUser.getUid()).child(user.getUid()).removeValue();
                    chatList.child(user.getUid()).child(currentUser.getUid()).removeValue();

                    DatabaseReference chats = FirebaseDatabase.getInstance().getReference("chats");
                    chats.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Chat chat = snapshot.getValue(Chat.class);
                                if ((chat.getSender().equals(currentUser.getUid()) && chat.getReceiver().equals(user.getUid())) ||
                                        (chat.getSender().equals(user.getUid()) && chat.getReceiver().equals(currentUser.getUid()))) {
                                    snapshot.getRef().removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    notifyDataSetChanged();
                    return false;
                }
            });
        } else {
            holder.lastMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    private void getLastMessage(final String userId, final TextView lastMessage) {
        isLastMessage = "default";
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference chatsReference = FirebaseDatabase.getInstance().getReference("chats");

        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(currentUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(currentUser.getUid())) {
                        isLastMessage = chat.getMessage();
                    }
                }

                switch (isLastMessage) {
                    case "default":
                        lastMessage.setText("No Message");
                    default:
                        lastMessage.setText(isLastMessage);
                        break;
                }

                isLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView photo;
        private TextView lastMessage;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
            progressBar = itemView.findViewById(R.id.progress_bar_img);
            lastMessage = itemView.findViewById(R.id.last_message);
        }
    }
}

