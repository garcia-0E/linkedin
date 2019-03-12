package com.sk.linkedinlogin;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class HomePage extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    private ProgressDialog progress;
    private TextView user_name, user_email;
    private ImageView profile_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        progress = new ProgressDialog(this);
        progress.setMessage("Actualizando la data");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        linkededinApiHelper();
    }

    /*
      Realiza una solicitud HTTP GET a la API REST de LinkedIn utilizando las credenciales del usuario autenticado actualmente.
      Si tiene éxito, se devolverá un objeto de ApiResponse de LinkedIn que contiene todos los aspectos relevantes de la respuesta del servidor.
     */

    public void linkededinApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(HomePage.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    setprofile(result.getResponseDataAsJson());
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }


    public  void  setprofile(JSONObject response){
        try {
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());

            Picasso.with(this).load(response.getString("pictureUrl")).into(profile_picture);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
