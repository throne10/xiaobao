package com.xiaobao.good.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.xiaobao.good.R;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.WechatRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WechatRecordActivity extends Activity {
    private static final String TAG = "X_WechatRecordActivity";
    @BindView(R.id.bt_add_img)
    Button btAddImg;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.lv_chats)
    ListView lvChats;

    @BindView(R.id.bt_commit)
    Button btCommit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_record);
        context = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<String> result) {
        //接收以及处理数据
        Log.i(TAG, "result>>>" + result);
        if (result.isEmpty()) {
            Toast.makeText(this, "请求失败，请检查网络后重试。", Toast.LENGTH_LONG).show();
        } else {
            lvChats.setAdapter(new ChatListAdapter(result));
        }
    }

    @OnClick({R.id.bt_add_img, R.id.bt_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_img:
                Crop.pickImage(this);
                break;
            case R.id.bt_commit:
                commitWechatRecords();
                break;
        }
    }

    private void commitWechatRecords() {
        Call<ResponseBody> responseBodyCall = RetrofitUtils.getService().uploadWechat(1, "visit_time");
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtil.i(TAG, "commitWechatRecords response>>>" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "上传失败，请检查网络后重试。", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
            Log.i(TAG, Crop.getOutput(result) + "");
            File file = null;
            try {
                file = new File(new URI(Crop.getOutput(result).toString()));
                saveBitmap(file);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            LogUtil.i(TAG, file + "");
            getChatString(file);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmap(File file) {
        try {
            File file2 = new File(Environment.getExternalStorageDirectory() + "/visit_time.jpg");
            FileOutputStream out = new FileOutputStream(file2);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getChatString(File file) {
        new Thread(() -> {
            try {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("imgFile", file.getName(), requestBody);
                MultipartBody body = builder.build();//调用即可```
                Call<WechatRecord> wechatRecord = RetrofitUtils.getService().getWechatRecord(body);
                Response<WechatRecord> clientsResponse = wechatRecord.execute();
                LogUtil.i(TAG, clientsResponse.body().getWords_result() + "");
                List<String> lsChat = new ArrayList<>();
                if (clientsResponse.isSuccessful()) {
                    for (WechatRecord.WordsResultBean wordsResultBean : clientsResponse.body().getWords_result()) {
                        lsChat.add(wordsResultBean.getWords());
                    }
                }
                EventBus.getDefault().post(lsChat);
            } catch (Exception e) {
                e.printStackTrace();
                EventBus.getDefault().post(new ArrayList<>());
            }
        }).start();
    }
}
