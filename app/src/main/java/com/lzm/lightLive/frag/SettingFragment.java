package com.lzm.lightLive.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.lzm.lightLive.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");
        Element describe = new Element();
        describe.setTitle("这是一个简单的直播聚合软件");

        return new AboutPage(getContext())
                .isRTL(false)
                .setDescription(getString(R.string.app_name))
                .addItem(versionElement)
                .addItem(describe)
                .addEmail("liangzemin1997@gmail.com", "Gmail")
                .addGitHub("FunnyMadePee", "GitHub")
                .create();

    }
}
