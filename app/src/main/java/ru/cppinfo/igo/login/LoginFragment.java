package ru.cppinfo.igo.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.gson.internal.LinkedTreeMap;
import com.vk.sdk.VKSdk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cppinfo.igo.EventsActivity;
import ru.cppinfo.igo.MainActivity;
import ru.cppinfo.igo.R;
import ru.cppinfo.igo.SharedPreferencesEditor;
import ru.cppinfo.igo.Token;
import ru.cppinfo.igo.service.RestService;

public class LoginFragment extends Fragment{
    private ProgressBar progressBar;
    private EditText editTextLogin;
    private EditText editTextPass;
    private Button btnEntr;
    private ImageButton signInButton;
    private ImageButton signInButtonGoogle;
    private TextView txtViewPrivacy;

    private String logintext;
    private String passtext;

    private String tokenID;
    private int request = 5;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_test, container, false);


        editTextLogin = v.findViewById(R.id.editTxtEntryLogin);
        editTextPass = v.findViewById(R.id.editTxtEntryPass);
        btnEntr = v.findViewById(R.id.btnEntr);
        progressBar = v.findViewById(R.id.login_progressBar);
        signInButton =  v.findViewById(R.id.sign_in_button);
        signInButtonGoogle = v.findViewById(R.id.sign_in_button_google);
        txtViewPrivacy = v.findViewById(R.id.txtViewPrivacy);

        signInButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                if(activity != null){
                    VKSdk.login(activity, "email");
                }
            }
        });

        txtViewPrivacy.setText(Html.fromHtml(getString(R.string.privacy_policy)));
        txtViewPrivacy.setClickable(true);
        txtViewPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://sarexer.github.io/Terms-of-use-and-Privacy-policy/"));
                startActivity(intent);
            }
        });
        Toolbar toolbar = v.findViewById(R.id.chat1Toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_colorprimary_24dp);

        btnEntr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                entr();

            }
        });
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned d, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (src.charAt(i) == '/') {
                        String s = src.toString();
                        s = s.replaceAll("/","");

                        src = s.subSequence(0, s.length());

                        return src;
                    }else if(src.charAt(i) == ' '){
                        String s = src.toString();
                        s = s.replaceAll(" ","");

                        src = s.subSequence(0, s.length());

                        return src;
                    }
                }
                return null;
            }
        };
        editTextLogin.setFilters(new InputFilter[]{filter});
        editTextPass.setFilters(new InputFilter[]{filter});


        return v;
    }

    public void signIn() {
        Intent signInIntent = LoginActivity.apiClient.getSignInIntent();
        startActivityForResult(signInIntent, request);
    }



    private void entr(){
        FirebaseApp.initializeApp(getContext());
        Token token = new Token();
        token.onTokenRefresh();
        tokenID = token.refreshedToken;

        logintext = editTextLogin.getText().toString();
        passtext = editTextPass.getText().toString();
        final Context context = getContext();
        if(logintext.equals("") && passtext.equals("")){
            if(context != null){
                Toasty.warning(context, "Введите логин и пароль!", Toast.LENGTH_SHORT,true).show();
            }
            return;
        }else if(passtext.equals("")){
            if(context != null){
                Toasty.warning(context, "Введите пароль!", Toast.LENGTH_SHORT,true).show();
            }
            return;
        }else if(logintext.equals("")){
            if(context != null){
                Toasty.warning(context, "Введите логин!", Toast.LENGTH_SHORT,true).show();
            }
            return;
        }
        progressBar.setVisibility(ProgressBar.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.entr(logintext, passtext, tokenID);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                try {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if(response.body() != null){
                    LinkedTreeMap<String, Object> map =  (LinkedTreeMap<String, Object>) response.body();

                    String res = (String) map.get("res");
                    String ID = (String) map.get("ID");
                    String FIO = (String) map.get("FIO");

                    if (res.equals("0")) {
                        SharedPreferences settings = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt( "ID", Integer.parseInt(ID) );
                        editor.putString("login", logintext);
                        editor.apply();

                        SharedPreferencesEditor editor1 = new SharedPreferencesEditor(getContext());
                        editor1.putString("email", logintext);
                        editor1.putString("FIO", FIO);
                        Intent intent = new Intent(getContext(), EventsActivity.class);

                        startActivity(intent);
                    } else if (res.equals("1")) {
                        if(context != null){
                            Toasty.error(context, "Не правильный логин или пароль!", Toast.LENGTH_SHORT,true).show();
                        }
                    }else  if(res.equals("2")){
                        if(context != null){
                            Toasty.error(context, "Произошла ошибка!", Toast.LENGTH_LONG,true).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                try {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

}
