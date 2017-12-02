package leesd.crossithackathon.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.R;
import leesd.crossithackathon.model.SlideObj;


/**
 * Created by cmtyx on 2017-07-31.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<SlideObj> res;

    public MainAdapter(Context context, List<SlideObj> res){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private void ratingBarInit(RatingBar ratingBar) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(context, R.color.colorStar), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(res.get(position).getName());
        Glide.with(context).load(res.get(position).getImg()).into(holder.image);
        ratingBarInit(holder.ratingBar);
        holder.ratingBar.setRating((new Random().nextInt()*10)%5+1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.po_image) KenBurnsView image;
        @BindView(R.id.po_name) TextView name;
        @BindView(R.id.po_rating) RatingBar ratingBar;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
