package com.xiaobao.good.sign;

import android.app.Activity;
import android.os.Bundle;
import com.soundcloud.android.crop.Crop;
import com.xiaobao.good.R;

public class SignInActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Crop.pickImage(this);
    }
}
