package ru.cppinfo.igo.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import ru.cppinfo.igo.InterestActivity;
import ru.cppinfo.igo.R;

public class RegistrationFragment extends Fragment {

    public static String email;
    public static String FIO;
    public static String bdate;
    public static String pass;
    private ProgressBar progressBar;
    private EditText editTextEmail;
    private EditText editTextFIO;
    private EditText editTextBDate;
    private EditText editTextPass;
    private Button btnReg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registartion, container, false);

        editTextEmail = v.findViewById(R.id.editTxtEmail);
        editTextPass = v.findViewById(R.id.editTxtPassword);
        editTextBDate = v.findViewById(R.id.editTxtBDate);
        editTextFIO = v.findViewById(R.id.editTxtFIO);

        btnReg = v.findViewById(R.id.btnRegistr);
        progressBar = v.findViewById(R.id.registr_progressBar);
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
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence src, int start, int end, Spanned d, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (src.charAt(i) == '/') {
                        String s = src.toString();
                        s = s.replaceAll("/", "");

                        src = s.subSequence(0, s.length());

                        return src;
                    } else if (src.charAt(i) == ' ') {
                        String s = src.toString();
                        s = s.replaceAll(" ", "");

                        src = s.subSequence(0, s.length());

                        return src;
                    }
                }
                return null;
            }
        };

        editTextEmail.setFilters(new InputFilter[]{filter});
        editTextPass.setFilters(new InputFilter[]{filter});

        return v;
    }


    private void registration() {

        email = editTextEmail.getText().toString();
        FIO = editTextFIO.getText().toString();
        bdate = editTextBDate.getText().toString();
        pass = editTextPass.getText().toString();

        Intent intent = new Intent(getContext(), InterestActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("FIO", FIO);
        intent.putExtra("bdate", bdate);
        intent.putExtra("pass", pass);

        startActivity(intent);
        getActivity().finish();
        /*Context context = getContext();
        if (email.equals("") && pass.equals("")) {
            if (context != null) {
                Toasty.warning(context, "Вы не ввели логин и пароль!", Toast.LENGTH_SHORT, true).show();
            }
            return;
        } else if (pass.equals("")) {
            if (context != null) {
                Toasty.warning(context, "Вы не ввели пароль!", Toast.LENGTH_SHORT, true).show();
            }
            return;
        } else if (email.equals("")) {
            if (context != null) {
                Toasty.warning(context, "Вы не ввели логин!", Toast.LENGTH_SHORT, true).show();
            }

            return;
        }

        progressBar.setVisibility(ProgressBar.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.reg(email, FIO, bdate, pass);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (response.body() != null) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body();

                    String res = (String) map.get("res");
                    Context context1 = getContext();
                    if (res.equals("0")) {
                        if (context1 != null) {
                            Toasty.success(context1, "Успешная регистрация", Toast.LENGTH_SHORT, true).show();

                        }


                        getActivity().finish();
                        Intent intent = new Intent(getContext(), InterestActivity.class);
                        startActivity(intent);

                        *//*Intent intent = new Intent(getContext(), StartActivity.class);
                        startActivity(intent);*//*

                    } else if (res.equals("1")) {
                        if (context1 != null) {
                            Toasty.error(context1, "Такой логин уже существует!", Toast.LENGTH_SHORT, true).show();
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Window window = getActivity().getWindow();
                if(window != null){
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });*/

    }

}
