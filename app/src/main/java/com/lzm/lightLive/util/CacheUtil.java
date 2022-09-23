package com.lzm.lightLive.util;

import android.content.Context;

public class CacheUtil {

    private static final String NO_PROMPT_CELL_DATA_DIALOG = "NO_PROMPT_CELL_DATA_DIALOG";

    public static void setNoPromptCellDataDialog(Context context, boolean noPromptCellDataDialog) {
        SpUtils.getInstance(context).put(NO_PROMPT_CELL_DATA_DIALOG, noPromptCellDataDialog);
    }

    public static boolean getNoPromptCellDataDialog(Context context) {
        return (boolean) SpUtils.getInstance(context).get(NO_PROMPT_CELL_DATA_DIALOG, false);
    }
}
