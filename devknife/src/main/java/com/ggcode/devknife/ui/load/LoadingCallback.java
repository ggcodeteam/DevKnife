package com.ggcode.devknife.ui.load;

import com.ggcode.devknife.R;
import me.passin.loadknife.callback.Callback;

/**
 * @author: zbb
 * @date: 2019/5/28 13:46
 * @desc:
 */
public class LoadingCallback extends Callback {

    @Override
    public int getLayoutId() {
        return R.layout.dk_callback_loading;
    }
}

