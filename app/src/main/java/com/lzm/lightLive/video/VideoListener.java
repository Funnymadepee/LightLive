package com.lzm.lightLive.video;

import android.util.Log;
import com.google.android.exoplayer2.Player;

public class VideoListener implements Player.Listener {

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        Player.Listener.super.onPlayWhenReadyChanged(playWhenReady, playbackState);
        switch (playbackState) {
            case Player.STATE_IDLE: {
                //播放器没有可播放的媒体。
                showToast("播放器没有可播放的媒体");
                break;
            }
            case Player.STATE_BUFFERING: {
                //播放器无法立即从当前位置开始播放。这种状态通常需要加载更多数据时发生。
                showToast("正在加载数据");
                break;
            }
            case Player.STATE_READY: {
                // 播放器可以立即从当前位置开始播放。如果{@link#getPlayWhenReady（）}为true，否则暂停。
                //当点击暂停或者播放时都会调用此方法
                //当跳转进度时，进度加载完成后调用此方法
                showToast("播放器可以立即从当前位置开始");
                break;
            }
            case Player.STATE_ENDED: {
                //播放器完成了播放
                showToast("视频结束了噢");
                break;
            }
        }
    }

    private void showToast(String msg) {
        Log.e("TAG", "player: " + msg );
    }

}
