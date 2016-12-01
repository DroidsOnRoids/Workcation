package com.droidsonroids.workcation.screens.main.map;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.droidsonroids.workcation.R;
import com.droidsonroids.workcation.common.model.Place;
import com.droidsonroids.workcation.common.transitions.TransitionUtils;
import java.util.ArrayList;
import java.util.List;

public class BaliPlacesAdapter extends RecyclerView.Adapter<BaliPlacesAdapter.BaliViewHolder> {

    private final OnPlaceClickListener mListener;
    private Context mContext;
    private List<Place> mPlaceList = new ArrayList<>();

    BaliPlacesAdapter(OnPlaceClickListener listener, Context context) {
        mListener = listener;
        mContext = context;
    }

    @Override
    public BaliViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new BaliViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bali_place, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaliViewHolder holder, final int position) {
        holder.mTextViewName.setText(mPlaceList.get(position).getName());
        holder.mTextViewOpeningHours.setText(mPlaceList.get(position).getOpeningHours());
        holder.mTextViewPrice.setText(String.valueOf(mPlaceList.get(position).getPrice()));
        holder.mImageViewPlacePhoto.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.monkey_forest_1));
        holder.mRoot.setOnClickListener(view -> mListener.onPlaceClicked(holder.mRoot, TransitionUtils.getRecyclerViewTransitionName(position), position));
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    void setPlacesList(List<Place> placesList) {
        mPlaceList = placesList;
        for (int i = 0; i < mPlaceList.size(); i++) {
            notifyItemInserted(i);
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClicked(View sharedImage, String transitionName, final int position);
    }

    public class BaliViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_title) TextView mTextViewName;
        @BindView(R.id.price) TextView mTextViewPrice;
        @BindView(R.id.opening_hours) TextView mTextViewOpeningHours;
        @BindView(R.id.root) CardView mRoot;
        @BindView(R.id.image_place_details) ImageView mImageViewPlacePhoto;

        BaliViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
