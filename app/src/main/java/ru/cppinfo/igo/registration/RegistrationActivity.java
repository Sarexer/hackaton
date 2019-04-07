package ru.cppinfo.igo.registration;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.cppinfo.igo.LogRegSingleFragmentActivity;
import ru.cppinfo.igo.start.StartActivity;

public class RegistrationActivity extends LogRegSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RegistrationFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
