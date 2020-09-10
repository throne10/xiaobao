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

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.xiaobao.good.R;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.WechatRecord;
import com.xiaobao.good.sign.Visit;
import com.xiaobao.good.sign.VisitResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }


    @BindView(R.id.lv_chats)
    ListView lvChats;

    @BindView(R.id.bt_commit)
    Button btCommit;
    private Context context;

    private int visitId;
    private StringBuffer b = null;
    private StringBuffer bTemp = null;
    private List<String> allChat = new ArrayList<>();
    private List<String> tempChat = new ArrayList<>();
    private int giveVisitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_record);
        context = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        giveVisitId = getIntent().getIntExtra("visitId", 0);
        LogUtil.i(TAG, "giveVisitId>>>>" + giveVisitId);
        if (giveVisitId != 0) {
            btCommit.setText("追加");
            Call<WeChatResult> bodyCall = RetrofitUtils.getService().getWechats(giveVisitId);
            bodyCall.enqueue(new Callback<WeChatResult>() {
                @Override
                public void onResponse(Call<WeChatResult> call, Response<WeChatResult> response) {
                    if (response.isSuccessful()) {
                        LogUtil.i(TAG, "Wechats>>>>" + response.body().getData());
                        String[] s = response.body().getData().split("<br>");
                        List<String> strings = Arrays.asList(s);
                        EventBus.getDefault().post(strings);
                    } else {
                        Toast.makeText(context, "请求微信聊天记录失败。", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<WeChatResult> call, Throwable t) {
                    Toast.makeText(context, "请求微信聊天记录失败。", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(List<String> result) {
        //接收以及处理数据
        Log.i(TAG, "result>>>" + result);
        if (result.isEmpty()) {
            Toast.makeText(this, "请求失败，请检查网络后重试。", Toast.LENGTH_LONG).show();
        } else {
            allChat.addAll(result);
            lvChats.setAdapter(new ChatListAdapter(allChat));
            b = new StringBuffer();
            for (String s : allChat) {
                b.append(s).append("<br>");
            }
            if (giveVisitId != 0) {
                if(!tempChat.isEmpty()) {
                    bTemp = new StringBuffer();
                    for (String s : tempChat) {
                        bTemp.append(s).append("<br>");
                    }
                }
            }
        }
    }

    @OnClick({R.id.bt_add_img, R.id.bt_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add_img:
                Crop.pickImage(this);
                break;
            case R.id.bt_commit:

                if (giveVisitId != 0) {
                    if (bTemp != null) {
                        append();
                    } else {
                        Toast.makeText(context, "请先追加微信聊天内容后提交。", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (b != null) {
                        visit();
                    } else {
                        Toast.makeText(context, "请先添加微信聊天内容后提交。", Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    private void append() {
        Call<ResponseBody> weChatResultCall = RetrofitUtils.getService().appendWechats(giveVisitId, bTemp.toString());
        weChatResultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        LogUtil.i(TAG, "appendWechats>>" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "追加微信聊天记录成功。", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "追加微信聊天记录失败。", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "追加微信聊天记录失败!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void visit() {
        if (visitId == 0) {
            Visit visit = new Visit();
            visit.setSign_address("");
            visit.setClient_id(38);
            visit.setEmployee_id(4);
            visit.setPurpose("微信聊天");
            visit.setWechat_content(b.toString());
            String s = new Gson().toJson(visit);
            LogUtil.i(TAG, "toJson>>>" + s);
            Call<VisitResult> visitResultCall = RetrofitUtils.getService().visit(visit);
            visitResultCall.enqueue(new Callback<VisitResult>() {
                @Override
                public void onResponse(Call<VisitResult> call, Response<VisitResult> response) {
                    if (response.isSuccessful()) {
                        visitId = response.body().getData();
                        LogUtil.i(TAG, response.body().getData() + "");
                        Toast.makeText(context, "添加微信聊天记录成功。", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "添加微信聊天记录失败。", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<VisitResult> call, Throwable t) {
                    Toast.makeText(context, "添加微信聊天记录失败。", Toast.LENGTH_LONG).show();
                }
            });
        }
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
                tempChat.addAll(lsChat);
                EventBus.getDefault().post(lsChat);
            } catch (Exception e) {
                e.printStackTrace();
                EventBus.getDefault().post(new ArrayList<>());
            }
        }).start();
    }
}
