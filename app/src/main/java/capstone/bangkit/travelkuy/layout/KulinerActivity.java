package capstone.bangkit.travelkuy.layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import capstone.bangkit.travelkuy.R;
import capstone.bangkit.travelkuy.adapter.KulinerAdapter;
import capstone.bangkit.travelkuy.api.Api;
import capstone.bangkit.travelkuy.dekorasi.LayoutMarginDecoration;
import capstone.bangkit.travelkuy.model.ModelHotel;
import capstone.bangkit.travelkuy.model.ModelKuliner;
import capstone.bangkit.travelkuy.utils.Tools;

public class KulinerActivity extends AppCompatActivity implements KulinerAdapter.onSelectData {

    RecyclerView rvKuliner;
    LayoutMarginDecoration gridMargin;
    KulinerAdapter kulinerAdapter;
    ProgressDialog progressDialog;
    List<ModelKuliner> modelKuliner = new ArrayList<>();
    Toolbar tbKuliner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuliner);

        tbKuliner = findViewById(R.id.toolbar_kuliner);
        tbKuliner.setTitle("Daftar Kuliner");
        setSupportActionBar(tbKuliner);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data...");

        rvKuliner = findViewById(R.id.rvKuliner);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,
                2, RecyclerView.VERTICAL, false);
        rvKuliner.setLayoutManager(mLayoutManager);
        gridMargin = new LayoutMarginDecoration(2, Tools.dp2px(this, 4));
        rvKuliner.addItemDecoration(gridMargin);
        rvKuliner.setHasFixedSize(true);

        getKuliner();
    }

    private void getKuliner() {
        progressDialog.show();
        AndroidNetworking.get(Api.Kuliner)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray playerArray = response.getJSONArray("kuliner");
                            for (int i = 0; i < playerArray.length(); i++) {
                                JSONObject temp = playerArray.getJSONObject(i);
                                ModelKuliner dataApi = new ModelKuliner();
                                dataApi.setIdKuliner(temp.getString("id"));
                                dataApi.setTxtNamaKuliner(temp.getString("nama"));
                                dataApi.setTxtAlamatKuliner(temp.getString("alamat"));
                                dataApi.setTxtOpenTime(temp.getString("jam_buka_tutup"));
                                dataApi.setKoordinat(temp.getString("kordinat"));
                                dataApi.setGambarKuliner(temp.getString("gambar_url"));
                                dataApi.setKategoriKuliner(temp.getString("kategori"));
                                modelKuliner.add(dataApi);
                                showKuliner();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(KulinerActivity.this,
                                    "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(KulinerActivity.this,
                                "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showKuliner() {
        kulinerAdapter = new KulinerAdapter(KulinerActivity.this, modelKuliner, this);
        rvKuliner.setAdapter(kulinerAdapter);
    }

    @Override
    public void onSelected(ModelKuliner modelKuliner) {
        Intent intent = new Intent(KulinerActivity.this, DetailKulinerActivity.class);
        intent.putExtra("detailKuliner", modelKuliner);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
