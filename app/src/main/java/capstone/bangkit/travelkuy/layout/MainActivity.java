package capstone.bangkit.travelkuy.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import capstone.bangkit.travelkuy.R;
import capstone.bangkit.travelkuy.adapter.MainAdapter;
import capstone.bangkit.travelkuy.adapter.RecomAdapter;
import capstone.bangkit.travelkuy.adapter.RecomWisataAdapter;
import capstone.bangkit.travelkuy.dekorasi.LayoutMarginDecoration;
import capstone.bangkit.travelkuy.model.ModelHotel;
import capstone.bangkit.travelkuy.model.ModelMain;
import capstone.bangkit.travelkuy.model.ModelWisata;

public class MainActivity extends AppCompatActivity implements MainAdapter.onSelectData, RecomAdapter.onSelectData, RecomWisataAdapter.onSelectData {

    RecyclerView rvMainMenu;
    RecyclerView rvRecom;
    RecyclerView rvWisataa;
    LayoutMarginDecoration gridMargin;
    ModelMain mdlMainMenu;
    ProgressDialog progressDialog;
    List<ModelMain> lsMainMenu = new ArrayList<>();
    List<ModelHotel> modelHotel = new ArrayList<>();
    List<ModelWisata> modelWisata = new ArrayList<>();
    RecomAdapter recomAdapter;
    RecomWisataAdapter recomWisataAdapter;
    TextView tvToday;
    String hariIni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility
                    (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        tvToday = findViewById(R.id.tvDate);
        rvMainMenu = findViewById(R.id.rvMainMenu);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3,
                RecyclerView.VERTICAL, false);
        rvMainMenu.setLayoutManager(mLayoutManager);
        gridMargin = new LayoutMarginDecoration(2);
        rvMainMenu.addItemDecoration(gridMargin);
        rvMainMenu.setHasFixedSize(true);

        //get Time Now
        Date dateNow = Calendar.getInstance().getTime();
        hariIni = (String) DateFormat.format("EEEE", dateNow);
        getToday();
        setMenu();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data...");

        rvWisataa = findViewById(R.id.rvWisataa);
        rvWisataa.setHasFixedSize(true);
        rvWisataa.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getWisataa();

        rvRecom = findViewById(R.id.rvRecom);
        rvRecom.setHasFixedSize(true);
        rvRecom.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getRecom();
    }
    private void getRecom()
    {
        String json;
        progressDialog.show();


        try {
            progressDialog.dismiss();


            InputStream is = getAssets().open("recomHotel.json");

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i<jsonArray.length();i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                ModelHotel data = new ModelHotel();
                data.setTxtNamaHotel(obj.getString("Name"));
                data.setTxtAlamatHotel(obj.getString("Addres"));
                data.setKoordinat(obj.getString("Coordinate"));
                data.setGambarHotel(obj.getString("Images"));
                modelHotel.add(data);
                showRecom();
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
        }

    }

    private void showRecom() {
        recomAdapter = new RecomAdapter(MainActivity.this, modelHotel, this);
        rvRecom.setAdapter(recomAdapter);
    }

    private void getWisataa()
    {
        String json;
        progressDialog.show();


        try {
            progressDialog.dismiss();


            InputStream is = getAssets().open("recomWisata.json");

            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i<jsonArray.length();i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);
                ModelWisata data = new ModelWisata();
                data.setIdWisata(obj.getString("id"));
                data.setTxtNamaWisata(obj.getString("place_name"));
                data.setGambarWisata(obj.getString("image"));
                data.setKategoriWisata(obj.getString("category"));
                modelWisata.add(data);
                showWisata();
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
        }

    }

    private void showWisata() {
        recomWisataAdapter = new RecomWisataAdapter(MainActivity.this, modelWisata, this);
        rvWisataa.setAdapter(recomWisataAdapter);
    }

    private void getToday() {
        Date date = Calendar.getInstance().getTime();
        String tanggal = (String) DateFormat.format("d MMMM yyyy", date);
        String formatFix = hariIni + ", " + tanggal;
        tvToday.setText(formatFix);
    }

    private void setMenu() {
        mdlMainMenu = new ModelMain("Hotel", R.drawable.ic_hotel);
        lsMainMenu.add(mdlMainMenu);
        mdlMainMenu = new ModelMain("Kuliner", R.drawable.ic_cafe);
        lsMainMenu.add(mdlMainMenu);
        mdlMainMenu = new ModelMain("Wisata", R.drawable.ic_destination);
        lsMainMenu.add(mdlMainMenu);

        MainAdapter myAdapter = new MainAdapter(lsMainMenu, this);
        rvMainMenu.setAdapter(myAdapter);
    }

    public void onSelected(ModelHotel modelHotel) {
        Intent intent = new Intent(MainActivity.this, DetailHotelActivity.class);
        intent.putExtra("detailHotel", modelHotel);
        startActivity(intent);
    }

    //set Transparent Status bar
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onSelected(ModelMain mdlMain) {
        switch (mdlMain.getTxtName()) {
            case "Hotel":
                startActivityForResult(new Intent(MainActivity.this, HotelActivity.class), 1);
                break;
            case "Kuliner":
                startActivityForResult(new Intent(MainActivity.this, KulinerActivity.class), 1);
                break;
            case "Wisata":
                startActivityForResult(new Intent(MainActivity.this, WisataActivity.class), 1);
                break;
        }
    }

    @Override
    public void onSelected(ModelWisata modelWisata) {

    }
}
