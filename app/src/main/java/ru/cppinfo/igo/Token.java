package ru.cppinfo.igo;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by shaka on 20.03.2018.
 */

public class Token extends FirebaseInstanceIdService {
    public String refreshedToken;

    public Token() {
        onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }
}
