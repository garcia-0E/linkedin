package com.grupoLinkedin.proyecto;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.linkedin.platform.LISessionManager.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button botonIniciaSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPackageHash();

        botonIniciaSesion = findViewById(R.id.boton_inicia_sesion);

    }

    private void getPackageHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.grupoLinkedin.proyecto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d(TAG, "Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    public void iniciaSesionLinkedin(View view) {
        //Primero chequea si el usuario esta autentificado
        if (!LISessionManager.getInstance(this).getSession().isValid()) {
            //Si no empieza la autentificacion
            LISessionManager.getInstance(getApplicationContext()).init(this, buildScope()//pass the build scope here
                    , new AuthListener() {
                        @Override
                        public void onAuthSuccess() {
                            // Authentication was successful. You can now do
                            // other calls with the SDK.
                            Toast.makeText(MainActivity.this, "Autentificado exitosamente con  LinkedIn.", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onAuthError(LIAuthError error) {
                            // Handle authentication errors
                            Log.e(TAG, "Auth Error :" + error.toString());
                            Toast.makeText(MainActivity.this, "Falló la autentificación con LinkedIn. Intente de nuevo por favor.", Toast.LENGTH_SHORT).show();
                        }
                    }, true);//if TRUE then it will show dialog if
            // any device has no LinkedIn app installed to download app else won't show anything
        } else {
            Toast.makeText(this, "Ya estás autentificado", Toast.LENGTH_SHORT).show();

            //if user is already authenticated fetch basic profile data for user

        }

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }
}
