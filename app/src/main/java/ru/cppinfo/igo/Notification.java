package ru.cppinfo.igo;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shaka on 20.03.2018.
 */

public class Notification {
    public static final MediaType JSON
            = MediaType.parse("application/json");
    public static void sendNotification(final String regToken) {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    json.put("body","Вам кто то ответил");
                    json.put("title","Новый ответ");
                    json.put("sound", "defaultSoundUri");
                    json.put("icon", R.drawable.ic_notification_24dp);
                    json.put("to",regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key=AAAA-EOXXoE:APA91bGhMaBkwlT81ar7zOgujKZAAd8UA4qQ8YHdz2WpGqPRtIZXE1enIxs0oKJqB-chS5ZLfY2FkAa7wuoy80w_MwpT579HsYljOAulEeLlD3gOf9bzx7OycZVl1t_oGWrrOIDujuDJWAXBUwDro3Ilq4c3zE07aA")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
