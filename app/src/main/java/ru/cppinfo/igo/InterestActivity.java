package ru.cppinfo.igo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cppinfo.igo.service.RestService;
import ru.cppinfo.igo.start.StartActivity;


public class InterestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    public static String email;
    public static String FIO;
    public static String bdate;
    public static String pass;

    private Drawable theatre;
    private Drawable fashion;
    private Drawable media;
    private Drawable music;
    private Drawable dance;
    private Drawable concert;
    private Drawable original;
    private Drawable like;

    private boolean[] interests = {false,false,false,false, false, false,false};
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_interests);
        recyclerView = findViewById(R.id.recyclerViewInterests);
        toolbar = findViewById(R.id.interest_toolbar);

        toolbar.setElevation(10);
        toolbar.setTitle("Выберите ваши интересы");


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new InterestAdapter(this));


        setSupportActionBar(toolbar);

        email = getIntent().getStringExtra("email");
        FIO = getIntent().getStringExtra("FIO");
        bdate = getIntent().getStringExtra("bdate");
        pass = getIntent().getStringExtra("pass");

        theatre = getDrawable(R.drawable.theater);
        fashion = getDrawable(R.drawable.dress);
        media = getDrawable(R.drawable.mobile_app);
        music = getDrawable(R.drawable.electric_guitar);
        dance = getDrawable(R.drawable.dance);
        concert = getDrawable(R.drawable.stage);
        original = getDrawable(R.drawable.concept);
        like = getDrawable(R.drawable.heart);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.interests_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String interest = "";
        for (boolean b : interests) {
            if(b){
                interest += "1";
            }else{
                interest += "0";
            }
        }

        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.reg(email, FIO, bdate, pass, interest);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (response.body() != null) {
                    LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) response.body();

                    String res = (String) map.get("res");

                    if (res.equals("0")) {
                        Toasty.success(getApplicationContext(), "Успешная регистрация", Toast.LENGTH_SHORT, true).show();

                        finish();

                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);

                    } else if (res.equals("1")) {
                        Toasty.error(getApplicationContext(), "Такой логин уже существует!", Toast.LENGTH_SHORT, true).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Window window = getWindow();
                if(window != null){
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                //progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
        return super.onOptionsItemSelected(item);
    }

    private class InterestHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private CardView cardView;
        private ImageView imageView;
        private TextView textView;


        InterestHolder(View v) {
            super(v);

            imageView = v.findViewById(R.id.imageViewInterest);
            textView = v.findViewById(R.id.txtViewInterestName);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!interests[getAdapterPosition()]){
                        interests[getAdapterPosition()] = true;
                    }else{
                        interests[getAdapterPosition()] = false;
                    }

                    bindInterest(getAdapterPosition(), interests[getAdapterPosition()]);

                }
            });
        }



        public void bindInterest(int position, boolean isPressed) {
            if(isPressed){
                switch (position){
                    case 0:
                        textView.setText("Театр");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 1:
                        textView.setText("Мода");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 2:
                        textView.setText("Медиа и PR");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 3:
                        textView.setText("Музыка");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 4:
                        textView.setText("Хореография");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 5:
                        textView.setText("Концертные программы");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;
                    case 6:
                        textView.setText("Оригинальный жанр");
                        imageView.setImageDrawable(like);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(100,100,100,100);
                        break;

                }

            }else{
                switch (position){
                    case 0:
                        textView.setText("Театр");
                        imageView.setImageDrawable(theatre);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_violet));
                        imageView.setPadding(20,20,20,20);
                        break;
                    case 1:
                        textView.setText("Мода");
                        imageView.setImageDrawable(fashion);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_orange));
                        imageView.setPadding(30,30,30,30);
                        break;
                    case 2:
                        textView.setText("Медиа и PR");
                        imageView.setImageDrawable(media);
                        imageView.setPadding(0,0,0,0);
                        imageView.setBackground(null);
                        break;
                    case 3:
                        textView.setText("Музыка");
                        imageView.setImageDrawable(music);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_blue));
                        imageView.setPadding(20,20,20,20);
                        break;
                    case 4:
                        textView.setText("Хореография");
                        imageView.setImageDrawable(dance);
                        imageView.setBackground(getDrawable(R.drawable.round_image_view_orange));
                        imageView.setPadding(20,20,20,20);
                        break;
                    case 5:
                        textView.setText("Концертные программы");
                        imageView.setImageDrawable(concert);
                        imageView.setPadding(0,0,0,0);
                        break;
                    case 6:
                        textView.setText("Оригинальный жанр");
                        imageView.setImageDrawable(original);
                        imageView.setPadding(0,0,0,0);
                        imageView.setBackground(null);
                        break;

                }

            }
        }

        @Override
        public void onClick(View view) {

        }
    }


    public class InterestAdapter extends RecyclerView.Adapter<InterestHolder> {
        Context context;

        InterestAdapter(Context context) {
            this.context = context;
        }

        @Override
        public InterestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater
                    .inflate(R.layout.item_interest_circle, parent, false);
            return new InterestHolder(view);
        }

        @Override
        public void onBindViewHolder(InterestHolder holder, int position) {
            holder.bindInterest(position, interests[position]);
        }

        @Override
        public int getItemCount() {
            return 7;
        }
    }

}
