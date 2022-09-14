package com.lzm.lightLive.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lzm.lightLive.R;
import com.lzm.lightLive.act.MainActivity;
import com.lzm.lightLive.bean.douyu.DyRoom;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.dy.DyHttpRequest;
import com.lzm.lightLive.http.dy.DyStreamHttpRequest;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.util.UiTools;
import com.lzm.lightLive.view.RoundImageView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.HostViewHolder> {

    private List<Pair<String, String>> roomList;
    private DyHttpRequest mCall;
    private final Activity activity;

    public void setDataList(List<Pair<String, String>> dyRooms) {
        int oldSize = roomList.size();
        roomList = dyRooms;
        notifyItemRangeChanged(oldSize, roomList.size());
    }

    public HostAdapter(Activity activity, List<Pair<String, String>> roomList) {
        mCall = RetrofitManager.getDYRetrofit().create(DyHttpRequest.class);
        this.activity = activity;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public HostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_host, parent, false);
        return new HostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostViewHolder holder, int position) {
        Pair<String, String> roomPair = roomList.get(position);
        //todo add setting screen round corner
        int screenCorner = UiTools.getScreenAverageCorner((Activity) holder.itemView.getContext());
        if(screenCorner > 0) {
            holder.cvRoot.setRadius(screenCorner);
        }

        if(TextUtils.isEmpty(roomPair.second))
            return;
        mCall.queryRoomInfo(roomPair.second)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<DyRoom>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(BaseResult<DyRoom> dyRoomBaseResult) {
                        DyRoom room = dyRoomBaseResult.getData();
                        Glide.with(holder.itemView.getContext())
                                .load(room.getRoomThumb())
                                .into(holder.ivThumb);
                        holder.tvHostName.setText(room.getOwnerName());
                        holder.tvRoomName.setText(room.getRoomName());
                        if(room.getRoomStatus() == 1) {
                            UiTools.setViewSaturation(holder.itemView, 1);
                            holder.tvRoomStatus.setText("直播中");
                            holder.tvRoomStatus.setActivated(true);
                        }else {
                            holder.tvRoomStatus.setText("未直播");
                            UiTools.setViewGray(holder.itemView);
                            holder.tvRoomStatus.setActivated(false);
                        }
                        holder.tvLiveCate.setText(room.getCateName());
                        holder.tvRoomNum.setText(room.getRoomId());

                        long online = room.getOnline();
                        if(room.getHn() > 0) {
                            holder.tvLiveHeat.setText(CommonUtil.convertInt2K(room.getHn()));
                            holder.tvLiveHeat.setVisibility(View.VISIBLE);
                        }else
                            holder.tvLiveHeat.setVisibility(View.GONE);

                        Glide.with(holder.itemView.getContext())
                                .load(room.getAvatar())
                                .into(holder.ivAvatar);
                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
                            room.setStreamUri(roomPair.first);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("room_info", room);
                            intent.putExtra("bundle", bundle);
                            holder.itemView.getContext().startActivity(intent);

//                            holder.ivThumb.setTransitionName("transition_ivThumb");
//                            holder.ivAvatar.setTransitionName("transition_ivAvatar");
//
//                            Pair<View, String> pair_thumb = new Pair<>(holder.ivThumb, "transition_ivThumb");
//                            Pair<View, String> pair_avatar = new Pair<>(holder.ivAvatar, "transition_ivAvatar");
//                            intent.putExtra("CUS_TRANSITION_NAME_1", "transition_ivThumb");
//                            intent.putExtra("CUS_TRANSITION_NAME_2", "transition_ivAvatar");
//                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
//                                    pair_thumb, pair_avatar);
//                            holder.itemView.getContext().startActivity(intent,
//                                    options.toBundle());
                        });
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });


    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    static class HostViewHolder extends RecyclerView.ViewHolder {
        private final CardView cvRoot;
        private final TextView tvHostName;
        private final TextView tvRoomName;
        private final TextView tvRoomStatus;
        private final TextView tvLiveHeat;
        private final TextView tvLiveCate;
        private final TextView tvRoomNum;
        private final ImageView ivThumb;
        private final RoundImageView ivAvatar;

        HostViewHolder(@NonNull View itemView) {
            super(itemView);
            cvRoot = itemView.findViewById(R.id.cv_item_root);
            tvHostName = itemView.findViewById(R.id.tv_host_name);
            tvRoomName = itemView.findViewById(R.id.tv_room_name);
            tvRoomStatus = itemView.findViewById(R.id.tv_room_status);
            tvLiveHeat = itemView.findViewById(R.id.tv_live_heat);
            tvLiveCate = itemView.findViewById(R.id.tv_live_cate);
            tvRoomNum = itemView.findViewById(R.id.tv_room_num);
            ivThumb = itemView.findViewById(R.id.iv_thumb);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }


}
