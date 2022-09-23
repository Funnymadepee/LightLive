package com.lzm.lightLive.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.lzm.lightLive.R;
import com.lzm.lightLive.util.CacheUtil;
import com.lzm.lightLive.util.SpUtils;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Element author = new Element();
        author.setTitle("By LZM");
        author.setOnClickListener(v -> {
            SpUtils.getInstance(getContext()).clear();
        });

        return new AboutPage(getContext())
                .setDescription(getString(R.string.app_name))
                .addEmail("liangzemin1997@gmail.com", "v我50")
                .addItem(author)
                .create();

    }
}
