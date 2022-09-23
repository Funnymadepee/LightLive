package com.lzm.lightLive.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
import com.lzm.lightLive.util.DialogUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HostAdapter extends BaseQuickAdapter<Room, BaseViewHolder> {

    private static final String TAG = "HostAdapter";

    public int PAGE_MAX_ITEM = 10;

    private final List<Room> cacheList = new ArrayList<>();

    public HostAdapter(int layoutResId, List<Room> data) {
        super(layoutResId, data);
    }

    public void setMassiveData(Collection<Room> collection) {
        if(collection.size() > PAGE_MAX_ITEM) {
            int cachedSize = cacheList.size();
            cacheList.addAll(cachedSize, collection);
            addData(cacheList.subList(cachedSize, cachedSize + PAGE_MAX_ITEM));
        }else {
            addData(collection);
        }
    }

    public List<Room> getCacheList() {
        return cacheList;
    }

    @Override
    protected void convert(BaseViewHolder helper, Room item) {
        Context context = helper.itemView.getContext();
        Glide.with(context)
                .load(item.getThumbUrl())
                .error(R.drawable.ic_live_no)
                .into((ImageView) helper.getView(R.id.iv_thumb));
        Glide.with(context)
                .load(item.getHostAvatar())
                .error(R.drawable.ic_host)
                .into((ImageView) helper.getView(R.id.iv_avatar));

        helper.itemView.setOnClickListener(v->{
            jumpToRoom(context, item);
        });
        helper.setImageDrawable(R.id.iv_plat,
                ActivityCompat.getDrawable(context, CommonUtil.generatePlatformDrawable(item.getPlatform()))
        );
        helper.setText(R.id.tv_live_cate, item.getCateName())
                .setText(R.id.tv_host_name, item.getHostName())
                .setText(R.id.tv_room_name, item.getRoomName())
                .setText(R.id.tv_room_num, item.getRoomId());

        if(item.getHeatNum() > 0) {
            helper.getView(R.id.tv_live_heat).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_live_heat, CommonUtil.convertInt2K(item.getHeatNum()));
        }else {
            helper.getView(R.id.tv_live_heat).setVisibility(View.GONE);
        }

        if (item.getStreamStatus() == Room.LIVE_STATUS_ON) {
            helper.setText(R.id.tv_room_status, R.string.live_status_on);
            helper.getView(R.id.tv_room_status).setActivated(true);
        }else {
            helper.setText(R.id.tv_room_status,  R.string.live_status_off);
            helper.getView(R.id.tv_room_status).setActivated(false);
        }

        helper.getView(R.id.iv_avatar).setOnLongClickListener(v -> {
            DialogUtil.showHostInfoDialog(item);
            return false;
        });

    }

    private void jumpToRoom(Context context, Room room) {
        if (room.getPlatform() == Room.LIVE_PLAT_DY) {
            WaitDialog.show(R.string.requesting);
            String time = String.valueOf(System.currentTimeMillis());
            String sign = CommonUtil.encrypt2ToMD5(room.getRoomId() + time);
            DyStreamHttpRequest mDyStreamCall = RetrofitManager.getDyStreamRetrofit().create(DyStreamHttpRequest.class);
            mDyStreamCall.queryRoomStreamInfo(room.getRoomId(), time, sign, room.getRoomId(), room.getRoomId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseResult<DyStream>>() {
                        @Override
                        public void onSubscribe(Disposable d) { }

                        @Override
                        public void onNext(BaseResult<DyStream> stringBaseResult) {
                            DyStream data = stringBaseResult.getData();
                            String live = data.getRtmpLive().split("_")[0];
                            String high = "http://hw-tct.douyucdn.cn/live/" + live + ".flv?uuid=";
                            String low = "http://hw-tct.douyucdn.cn/live/" + live + "_900.flv?uuid=";
                            room.setLiveStreamUriHigh(high);
                            room.setLiveStreamUriLow(low);
                            WaitDialog.dismiss();
                            startPlay(context, room);
                        }

                        @Override
                        public void onError(Throwable e) {
                            TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.ERROR,500);
                            Log.e(TAG, "onError: " + e.getMessage() );
                        }

                        @Override
                        public void onComplete() { }
                    });
        }else {
            startPlay(context, room);
        }
    }

    private void startPlay(Context context, Room room) {
        if(room.getStreamStatus() == Room.LIVE_STATUS_ON) {
            Intent intent = new Intent(context, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("room_info", room);
            intent.putExtra("bundle", bundle);
            context.startActivity(intent);
        }else {
            TipDialog.show(R.string.no_stream_hint, WaitDialog.TYPE.WARNING, 1500);
        }
    }

    public int indexOfRoomId(String roomId) {
        if (getData().size() == 0) return -1;
        for (int i = 0; i < getData().size(); i++) {
            if (roomId.equals(getData().get(i).getRoomId())) {
                return i;
            }
        }
        return -1;
    }

    public int getPageMaxItem() {
        return PAGE_MAX_ITEM;
    }

    public void setPageMaxItem(int pageMaxItem) {
        this.PAGE_MAX_ITEM = pageMaxItem;
    }
}
