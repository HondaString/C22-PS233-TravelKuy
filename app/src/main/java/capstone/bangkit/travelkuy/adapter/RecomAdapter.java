package capstone.bangkit.travelkuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import capstone.bangkit.travelkuy.R;
import capstone.bangkit.travelkuy.model.ModelHotel;


public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.ViewHolder> {

    private final List<ModelHotel> items;
    private final RecomAdapter.onSelectData onSelectData;
    private final Context mContext;

    public interface onSelectData {
        void onSelected(ModelHotel modelNews);
    }

    public RecomAdapter(Context context, List<ModelHotel> items, RecomAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_hotel, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelHotel data = items.get(position);

        //Get Image
        Glide.with(mContext)
                .load(data.getGambarHotel())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgHotel);

        holder.tvNamaHotel.setText(data.getTxtNamaHotel());
        holder.rlListHotel.setOnClickListener(v -> onSelectData.onSelected(data));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaHotel;
        public RelativeLayout rlListHotel;
        public ImageView imgHotel;

        public ViewHolder(View itemView) {
            super(itemView);
            rlListHotel = itemView.findViewById(R.id.rlListHotel);
            tvNamaHotel = itemView.findViewById(R.id.tvNamaHotel);
            imgHotel = itemView.findViewById(R.id.imgHotel);
        }
    }
}
