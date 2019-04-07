package ru.cppinfo.igo.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.gson.internal.LinkedTreeMap;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import data.DBHelper;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cppinfo.igo.R;
import ru.cppinfo.igo.LogRegSingleFragmentActivity;
import ru.cppinfo.igo.service.RestService;
import ru.cppinfo.igo.Token;
import ru.cppinfo.igo.start.StartActivity;


public class LoginActivity extends LogRegSingleFragmentActivity {

    ProgressBar progressBar;
    String pass = " ";
    String tokenID;


    static GoogleSignInClient apiClient;


    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.login_progressBar);
        new DBHelper(getApplicationContext()).getWritableDatabase();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        apiClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            if(email == null){
                //Toast.makeText(getApplicationContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                Toasty.error(getApplicationContext(), "Ошибка!", Toast.LENGTH_SHORT,true).show();
                return;
            }
            registration(email);

            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginActivity", "signInResult:failed code=" + e.getStatusCode());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {



        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String login = res.userId;


                if(login == null){
                    Context context = getApplicationContext();
                    if(context != null){
                        Toasty.error(context, "Ошибка!", Toast.LENGTH_SHORT,true).show();
                    }
                    return;
                }
                registration(login);
            }
            @Override
            public void onError(VKError error) {
            }
        })) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

            super.onActivityResult(requestCode, resultCode, data);
        }


    }
    private void entr(String login){
        FirebaseApp.initializeApp(getApplicationContext());
        Token token = new Token();
        token.onTokenRefresh();

        while(true){
            tokenID = token.refreshedToken;
            if(tokenID != null){
                break;
            }
        }

        final String logintext = login;
        String passtext = " ";

        progressBar.setVisibility(ProgressBar.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.entr(logintext, passtext, tokenID);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if(response.body() != null){
                    LinkedTreeMap<String, Object> map =  (LinkedTreeMap<String, Object>) response.body();

                    String res = (String) map.get("res");
                    String ID = (String) map.get("ID");

                    if (res.equals("0")) {
                        SharedPreferences settings = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt( "ID", Integer.parseInt(ID) );
                        editor.putString("login", logintext);
                        editor.apply();


                        int number = settings.getInt("number", -1);
                        if(number == -1){
                            editor.putInt("number", 4);
                            editor.apply();
                        }
                        boolean IsIntroCompleted = settings.getBoolean("intro", true);
                        if(IsIntroCompleted){
                            /*Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                            finish();
                            startActivity(intent);*/
                        }else{
                            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);*/
                        }


                    } else if(res.equals("2")){
                        Context context = getApplicationContext();
                        if(context != null){
                            Toasty.error(context, "Произошла ошибка!", Toast.LENGTH_LONG,true).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }
    private void registration(final String login){
        progressBar = findViewById(R.id.login_progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.reg(login, " ", " ", pass, "");
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if(response.body() != null){
                    LinkedTreeMap<String, Object> map =  (LinkedTreeMap<String, Object>) response.body();

                    String res = (String) map.get("res");
                    if(res.equals("0") || res.equals("1")){
                        entr(login);
                    }

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

    }

}
