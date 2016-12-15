package fr.sio.ecp.federatedbirds;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * This class represents the API service and exposes all its endpoints through simple methods.
 * The client is initialized with the Context of the application to access the shared preferences (token storage).
 */
public class ApiClient {

    // The API base URL
    private static final String API_BASE = "https://federatedbirds.appspot.com/";

    // A singleton instance
    private static ApiClient mInstance;

    // Public getter to access and initialize the singleton
    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private Context mContext;

    private ApiClient(Context context) {
        // To avoid leaking, never hold a reference to the context its self (this could be an Activity)
        // Instead, call getApplicationContext() to get a memory-safe instance (this may return the same context object or a different one)
        mContext = context.getApplicationContext();
    }

    // An internal method to execute a GET request
    private <T> T get(String path, Type type) throws IOException {
        return method("GET", path, null, type);
    }

    // An internal method to execute a POST request
    private <T> T post(String path, Object body, Type type) throws IOException {
        return method("POST", path, body, type);
    }

    /**
     * A common method to perform an HTTP request
     * @param method The HTTP method to use
     * @param path The relative path to call
     * @param body An object that will sent as JSON in the request body (must be null for a GET request)
     * @param type The type of the response, to be parsed from JSON
     * @param <T> The generic type of the response
     * @return The response to the request, parsed from JSON
     * @throws IOException is something goes wrong
     */
    private <T> T method(String method, String path, Object body, Type type) throws IOException {
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // At this point we have opened a connection to the URL, we can send headers
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            // If the user is connected, add a header to the request with the token
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        if (body != null) {
            // Write the request body with Gson library
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            try {
                new Gson().toJson(body, writer);
            } finally {
                writer.close();
            }
        }
        // Write the request body with Gson library
        Reader reader = new InputStreamReader(connection.getInputStream());
        try {
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages(Long userId) throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>() {};
        String path = userId == null ? "messages" : "users/" + userId + "/messages";
        return get(path, type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users/" + id, User.class);
    }

    public List<User> getUserFollowed(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followed", type.getType());
        //return get("users", type.getType());
    }

    public List<User> getUserFollowers(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followers", type.getType());
        //return get("users", type.getType());

    }

    public String login(String login, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    public String addUser(String login, String password, String email) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        body.addProperty("email", email);
        return post("users", body, String.class);
    }

    public Message postMessage(String text) throws IOException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }

    public User setFollowing(Long following_id, boolean follow) throws IOException {
        JsonObject body = new JsonObject();
        if (follow) {
            body.addProperty("followed", "true");
        } else {
            body.addProperty("followed", "false");
        }
        return post("users/" + following_id.toString(), body, User.class);
    }




}
