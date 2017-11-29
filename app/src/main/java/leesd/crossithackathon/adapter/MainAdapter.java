package leesd.crossithackathon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import leesd.crossithackathon.R;


/**
 * Created by cmtyx on 2017-07-31.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private List<String> res;

    public MainAdapter(Context context, List<String> res){
        this.mInflater = LayoutInflater.from(context);
        this.res = res;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view = mInflater.inflate(R.layout.item_recycler_rank,parent,false);
      //  ViewHolder viewHolder = new ViewHolder(view);
        return null;
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

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_image)
        ImageView mainImage;
        private ViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);

        }
    }
}
