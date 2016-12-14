package fr.sio.ecp.federatedbirds.app;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import java.io.IOException;
import com.squareup.picasso.Picasso;
import java.util.List;
import android.support.v4.os.AsyncTaskCompat;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.model.User;
import fr.sio.ecp.federatedbirds.ApiClient;
import android.widget.Toast;

import android.util.Log;



/**
 * Created by Michaël on 24/11/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private List<User> mUsers;


    private static final String USERID_KEY = "user_id";
    private static final String USERNAME_KEY = "user_name";
    private static final String USERAVATAR_KEY = "user_avatar";

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        User user = mUsers.get(position);

        final long user_id = user.id;
        final String username = user.login;
        final String user_avatar = user.avatar;

        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);

        holder.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new SetFollowingTask(v.getContext(), user_id, true)
                );
                Toast.makeText(v.getContext(), R.string.follow_ok, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskCompat.executeParallel(
                        new SetFollowingTask(v.getContext(), user_id, false)
                );
                Toast.makeText(v.getContext(), R.string.unfollow_ok, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarView;
        private TextView mUsernameView;
        private Button mFollow;
        private Button mUnfollow;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mFollow = (Button) itemView.findViewById(R.id.follow);
            mUnfollow = (Button) itemView.findViewById(R.id.unfollow);
        }
    }

    private class SetFollowingTask extends AsyncTask<Void, Void, User> {

        private Context mContext;
        private long mFollowingId;
        private boolean mFollow;

        public SetFollowingTask(Context context, long following_id, boolean follow){
            mContext = context;
            mFollowingId = following_id;
            mFollow = follow;
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                return ApiClient.getInstance(mContext).setFollowing(mFollowingId, mFollow);
            } catch (IOException e) {
                Log.e(UsersAdapter.class.getSimpleName(), "Following failed", e);
                return null;
            }
        }
    }
}