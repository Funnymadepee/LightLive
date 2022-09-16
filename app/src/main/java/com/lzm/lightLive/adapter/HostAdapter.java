package com.lzm.lightLive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.lzm.lightLive.R;
import com.lzm.lightLive.act.MainActivity;
import com.lzm.lightLive.http.BaseResult;
import com.lzm.lightLive.http.RetrofitManager;
import com.lzm.lightLive.http.bean.Room;
import com.lzm.lightLive.http.bean.dy.DyStream;
import com.lzm.lightLive.http.request.dy.DyStreamHttpRequest;
import com.lzm.lightLive.util.CommonUtil;
import com.lzm.lightLive.util.UiTools;
import com.lzm.lightLive.view.RoundImageView;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HostAdapter extends RecyclerView.Adapter<HostAdapter.HostViewHolder> {

    private static final String TAG = "hostAda";
    private List<Room> roomList;
    private final Activity activity;

    public void setDataList(List<Room> dyRooms) {
        int oldSize = roomList.size();
        roomList = dyRooms;
        notifyItemRangeChanged(oldSize, roomList.size());
    }

    public HostAdapter(Activity activity) {
        this.activity = activity;
        roomList = new ArrayList<>();
    }

    public void addData(Room room) {
        roomList.add(room);
        notifyItemChanged(roomList.size());
    }

    @NonNull
    @Override
    public HostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_host, parent, false);
        return new HostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HostViewHolder holder, int position) {
        Room room = roomList.get(position);
        //todo add setting screen round corner
//        int screenCorner = UiTools.getScreenAverageCorner((Activity) holder.itemView.getContext());
//        if(screenCorner > 0) {
//            holder.cvRoot.setRadius(screenCorner);
//        }
        if(null == room)
            return;

        holder.ivPlat.setImageDrawable(holder.itemView.getContext().getDrawable(
                CommonUtil.generatePlatformDrawable(room.getPlatform())
        ));

        holder.tvLiveCate.setText(room.getCateName());
        Glide.with(holder.itemView.getContext())
                .load(room.getThumbUrl())
                .error(R.drawable.ic_live_no)
                .into(holder.ivThumb);
        holder.tvHostName.setText(room.getHostName());
        holder.tvRoomName.setText(room.getRoomName());
        if(room.getStreamStatus() == Room.LIVE_STATUS_ON) {
            UiTools.setViewSaturation(holder.itemView, 1);
            holder.tvRoomStatus.setText("直播中");
            holder.tvRoomStatus.setActivated(true);
        }else {
            holder.tvRoomStatus.setText("未直播");
            UiTools.setViewGray(holder.itemView);
            holder.tvRoomStatus.setActivated(false);
        }
        holder.tvRoomNum.setText(room.getRoomId());

        if(room.getHeatNum() > 0) {
            holder.tvLiveHeat.setText(CommonUtil.convertInt2K(room.getHeatNum()));
            holder.tvLiveHeat.setVisibility(View.VISIBLE);
        }else
            holder.tvLiveHeat.setVisibility(View.GONE);

        Glide.with(holder.itemView.getContext())
                .load(room.getHostAvatar())
                .error(R.drawable.ic_host)
                .into(holder.ivAvatar);
        holder.itemView.setOnClickListener(v -> {
            if (room.getPlatform() == Room.LIVE_PLAT_DY) {
                String time = String.valueOf(System.currentTimeMillis());
                String sign = CommonUtil.encrypt2ToMD5(room.getRoomId() + time);
                Log.e(TAG, "requestRoomStreamInfo: " + room.getRoomId() + "\t" + time + "\t" + sign );
                DyStreamHttpRequest mDyStreamCall = RetrofitManager.getDYStreamRetrofit().create(DyStreamHttpRequest.class);
                mDyStreamCall.queryRoomStreamInfo(room.getRoomId(), time, sign, room.getRoomId(), room.getRoomId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<BaseResult<DyStream>>() {
                            @Override
                            public void onSubscribe(Disposable d) { }

                            @Override
                            public void onNext(BaseResult<DyStream> stringBaseResult) {
                                DyStream data = stringBaseResult.getData();
                                Log.e("TAG", "onNext: " + data.toString());
                                String live = data.getRtmpLive().split("_")[0];
                                String high = "http://hw-tct.douyucdn.cn/live/" + live + ".flv?uuid=";
                                String low = "http://hw-tct.douyucdn.cn/live/" + live + "_900.flv?uuid=";
                                room.setLiveStreamUri(high);
                                startPlay(holder.itemView.getContext(), room);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage() );
                            }

                            @Override
                            public void onComplete() { }
                        });
                    /*holder.ivThumb.setTransitionName("transition_ivThumb");
                    holder.ivAvatar.setTransitionName("transition_ivAvatar");

                    Pair<View, String> pair_thumb = new Pair<>(holder.ivThumb, "transition_ivThumb");
                    Pair<View, String> pair_avatar = new Pair<>(holder.ivAvatar, "transition_ivAvatar");
                    intent.putExtra("CUS_TRANSITION_NAME_1", "transition_ivThumb");
                    intent.putExtra("CUS_TRANSITION_NAME_2", "transition_ivAvatar");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                            pair_thumb, pair_avatar);
                    holder.itemView.getContext().startActivity(intent,
                            options.toBundle());*/
            }else {
                startPlay(holder.itemView.getContext(), room);
            }

        });

    }

    private void startPlay(Context context, Room room) {
        if(room.getStreamStatus() != Room.LIVE_STATUS_ON) {
            String msg = room.getHostName() + "暂时未开播噢~";
            TipDialog.show(msg, WaitDialog.TYPE.WARNING, 1500);
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("room_info", room);
            intent.putExtra("bundle", bundle);
            context.startActivity(intent);
        }
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
        private final ImageView ivPlat;
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
            ivPlat = itemView.findViewById(R.id.iv_plat);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }


}
