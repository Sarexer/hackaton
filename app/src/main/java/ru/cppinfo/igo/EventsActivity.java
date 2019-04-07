package ru.cppinfo.igo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import es.dmoral.toasty.Toasty;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.cppinfo.igo.service.RestService;
import ru.cppinfo.igo.start.StartActivity;

public class EventsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {
    private static RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);

        toolbar = findViewById(R.id.eventsToolbar);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerview_events);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(new EventAdapter(this));

        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerView.setItemViewCacheSize(250);
        recyclerView.setDrawingCacheEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.cubs);
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.lection);
        Bitmap icon2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.master);
        Bitmap icon3 = BitmapFactory.decodeResource(getResources(),
                R.drawable.balet);
        Bitmap icon4 = BitmapFactory.decodeResource(getResources(),
                R.drawable.programming);

        icon = Bitmap.createScaledBitmap(icon, 1300, 600, false);
        icon1 = Bitmap.createScaledBitmap(icon1, 1300, 600, false);
        icon2 = Bitmap.createScaledBitmap(icon2, 1300, 600, false);
        icon3 = Bitmap.createScaledBitmap(icon3, 1300, 600, false);
        icon4 = Bitmap.createScaledBitmap(icon4, 1300, 600, false);

        bitmaps.add(icon);
        bitmaps.add(icon1);
        bitmaps.add(icon2);
        bitmaps.add(icon3);
        bitmaps.add(icon4);

        SharedPreferencesEditor editor = new SharedPreferencesEditor(this);
        String email = editor.getString("email", "");
        String FIO = editor.getString("FIO", "");

        TextView txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);
        TextView txtFIO = navigationView.getHeaderView(0).findViewById(R.id.txtFIO);

        txtEmail.setText(email);
        txtFIO.setText(FIO);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id){
            case R.id.nav_events:
                if(!menuItem.isChecked()){
                    menuItem.setChecked(true);
                    Intent intent = new Intent(this, EventsActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.nav_scan:
                break;
            case R.id.nav_magaz:
                Intent intent2 = new Intent(this, BallsActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_exit:
                SharedPreferences settings = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();

                editor.putInt("ID", -1);
                editor.putString("email", null);
                editor.apply();

                Intent intent1 = new Intent(this, StartActivity.class);
                startActivity(intent1);

                finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRefresh() {
        SharedPreferencesEditor editor = new SharedPreferencesEditor(this);
        int id = editor.getInteger("ID", -1);
        RestService gitHubService = RestService.retrofit.create(RestService.class);
        final Call<Object> call =
                gitHubService.getIvents(id+"");
        refreshLayout.setRefreshing(true);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.body() != null){
                    System.out.println("");;
                    LinkedTreeMap<String, Object> map =  (LinkedTreeMap<String, Object>) response.body();
                    ArrayList<LinkedTreeMap<String, Object>> list = (ArrayList<LinkedTreeMap<String, Object>>) map.get("res");
                    if(list != null && list.size() != 0){
                        events.clear();
                        for (LinkedTreeMap<String, Object> treeMap : list) {
                            String title = (String) treeMap.get("title");
                            String descript = (String) treeMap.get("description");

                            Event event = new Event(title, null, descript, null, null, null,null);
                            events.add(event);
                        }
                    }



                    recyclerView.getAdapter().notifyDataSetChanged();
                    refreshLayout.setRefreshing(false);



                }

                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                refreshLayout.setRefreshing(false);
            }
        });

    }
    private class EventHolder extends RecyclerView.ViewHolder {
        private CardView eventCard;
        private TextView eventTitile;
        private TextView eventDesription;
        private ImageView eventImage;
        private Button btn;


        EventHolder(View v) {
            super(v);
            eventCard = v.findViewById(R.id.card_view_event);
            eventTitile = v.findViewById(R.id.txtEventTitle);
            eventDesription = v.findViewById(R.id.txtEventsDescription);
            eventImage = v.findViewById(R.id.imageViewEvent);
            btn = v.findViewById(R.id.eventBtn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toasty.info(getApplicationContext(), "Мы оповестим вас", Toasty.LENGTH_SHORT).show();
                }
            });
        }

        public void bindEvent(String title, String description, Bitmap image) {
            eventTitile.setText(title);
            eventDesription.setText(description);
            eventImage.setImageBitmap(image);

        }

    }

    public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        Context context;

        EventAdapter(Context context) {
            this.context = context;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater
                    .inflate(R.layout.item_cardview_events, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = events.get(position);
            holder.bindEvent(event.getTitle(), event.getDescription(), bitmaps.get(position%5));

        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }


}
