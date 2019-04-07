package ru.cppinfo.igo.start;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.cppinfo.igo.EventsActivity;
import ru.cppinfo.igo.MainActivity;
import ru.cppinfo.igo.SharedPreferencesEditor;
import ru.cppinfo.igo.login.LoginActivity;
import ru.cppinfo.igo.R;
import ru.cppinfo.igo.registration.RegistrationActivity;

public class StartFragment extends Fragment{
    private Button btnEntr;
    private Button btnReg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesEditor editor = new SharedPreferencesEditor(getContext());
        int ID = editor.getInteger("ID", -1);


        if(ID != -1){

            Intent intent = new Intent(getContext(), EventsActivity.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        btnEntr = v.findViewById(R.id.btnStartEntr);
        btnReg = v.findViewById(R.id.btnStartReg);

        setupButtonListeners();


        return v;
    }

    private void setupButtonListeners(){
        btnEntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


}
