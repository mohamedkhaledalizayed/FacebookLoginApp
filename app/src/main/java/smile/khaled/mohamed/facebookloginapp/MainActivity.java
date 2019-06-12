package smile.khaled.mohamed.facebookloginapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private TextView textView;
    private LoginButton loginButton;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.txt);

        imageView = findViewById(R.id.image);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        textView.setText("Successfully logged in");
GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("LoginActivity", object.toString());

                            // Application code
                            try {
                                String name = object.getString("name");
                                Log.e("hynnn",response+"");
                                textView.setText(name);

                                Picasso.get().load("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large").into(imageView);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,link");
            request.setParameters(parameters);
            request.executeAsync();

                        Log.e("hynnn",request.getGraphObject()+"");

                    }

                    @Override
                    public void onCancel() {
                        textView.setText("Login canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null)
                    textView.setText("Logged out");
            }
        });
    }


    //https://apkpure.com/key-hash-key/notimeforunch.keyhash/download?from=details




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}