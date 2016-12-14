package fr.sio.ecp.federatedbirds.app;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.ApiClient;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 30/11/2015.
 */
public class FollowedLoader extends UsersLoader {

    private String mTypeFollow;

    public FollowedLoader(Context context, Long userId, String typeFollow) {
        super(context, userId);
        mTypeFollow = typeFollow;
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException {
        if (mTypeFollow.equals("followed")) {
            return ApiClient.getInstance(getContext()).getUserFollowed(userId);
        }
        else if (mTypeFollow.equals("followers")) {
            return ApiClient.getInstance(getContext()).getUserFollowers(userId);
        }
        else return null;
    }










}