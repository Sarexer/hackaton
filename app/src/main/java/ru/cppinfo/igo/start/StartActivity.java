package ru.cppinfo.igo.start;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import ru.cppinfo.igo.LogRegSingleFragmentActivity;


public class StartActivity extends LogRegSingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new StartFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
