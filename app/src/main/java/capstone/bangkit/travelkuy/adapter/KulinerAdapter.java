package capstone.bangkit.travelkuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import capstone.bangkit.travelkuy.R;
import capstone.bangkit.travelkuy.model.ModelKuliner;


public class KulinerAdapter extends RecyclerView.Adapter<KulinerAdapter.ViewHolder> {

    private final List<ModelKuliner> items;
    private final KulinerAdapter.onSelectData onSelectData;
    private final Context mContext;

    public interface onSelectData {
        void onSelected(ModelKuliner modelKuliner);
    }

    public KulinerAdapter(Context context, List<ModelKuliner> items, KulinerAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_kuliner, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelKuliner data = items.get(position);

        //Get Image
        Glide.with(mContext)
                .load(data.getGambarKuliner())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgKuliner);

        holder.tvKategori.setText(data.getKategoriKuliner());
        holder.tvKuliner.setText(data.getTxtNamaKuliner());
        holder.cvKuliner.setOnClickListener( v -> onSelectData.onSelected(data) );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvKuliner;
        public TextView tvKategori;
        public CardView cvKuliner;
        public ImageView imgKuliner;

        public ViewHolder(View itemView) {
            super(itemView);
            cvKuliner = itemView.findViewById(R.id.cvKuliner);
            tvKuliner = itemView.findViewById(R.id.tvKuliner);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            imgKuliner = itemView.findViewById(R.id.imgKuliner);
        }
    }

}
