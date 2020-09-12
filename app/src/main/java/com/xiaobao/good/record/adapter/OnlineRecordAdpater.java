package com.xiaobao.good.record.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobao.good.AudioRecordContentActivity;
import com.xiaobao.good.R;
import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.dao.RecordHistoryDao;
import com.xiaobao.good.record.RecordDetailItem;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.RecordUploadResult;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineRecordAdpater extends BaseAdapter {
    private static final String TAG = "OnlineRecordAdpater";


    List<RecordDetailItem> list;

    Context context;

    MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    int selectItem = -1;

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    int visitId;

    public OnlineRecordAdpater(Context context, List<RecordDetailItem> list, int visitId) {
        this.list = list;
        this.context = context;
        this.visitId = visitId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        RecordDetailItem recordDetailItem = list.get(position);
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_record_detail_item, null);
            holder = new ViewHolder();

            holder.showArea = view.findViewById(R.id.ll_showArea);
            holder.text = (TextView) view.findViewById(R.id.tv_record_name);
            holder.seekbar = (SeekBar) view.findViewById(R.id.seekbar);

            holder.seekbar.setEnabled(false);

            holder.detailOrUpload = (Button) view.findViewById(R.id.bt_detail_or_upload);
            holder.play = (Button) view.findViewById(R.id.bt_play);


            holder.rl_btshow = view.findViewById(R.id.rl_btshow);
            holder.rl_seekShow = view.findViewById(R.id.rl_seekShow);

            holder.current_progress_text_view = view.findViewById(R.id.current_progress_text_view);
            holder.file_length_text_view = view.findViewById(R.id.file_length_text_view);


            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(recordDetailItem.getFilePath());
        String typeMsg = recordDetailItem.getType().equals("0") ? "详情" : "上传";
        holder.detailOrUpload.setText(typeMsg);
        holder.detailOrUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 根据type,展示详情，还是上传
                 */

                if (recordDetailItem.getType().equals("0")) {
                    /**
                     * 详情
                     */


                    Intent intent = new Intent(context, AudioRecordContentActivity.class);

                    intent.putExtra("voiceId", Integer.parseInt(recordDetailItem.getExtra()));
                    /**
                     *传参
                     */
                    context.startActivity(intent);
                } else {

                    File file = new File(recordDetailItem.getFilePath());

                    Log.i(TAG, "file :" + file);
                    Log.i(TAG, "visit_id :" + visitId);

                    RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), fileRQ);

                    RequestBody body = new MultipartBody.Builder()
                            .addFormDataPart("visit_id", visitId + "")
                            .addFormDataPart("voiceFile", file.getName(), fileRQ)
                            .build();
                    Call<RecordUploadResult> uploadCall = RetrofitUtils.getService().uploadVoice(body);
                    uploadCall.enqueue(new Callback<RecordUploadResult>() {
                        @Override
                        public void onResponse(Call<RecordUploadResult> call, Response<RecordUploadResult> response) {

                            Log.i(TAG, "recordupload :" + response.body().getCode());
                            String code = response.body().getCode();

                            if ("success".equals(code)) {
                                /**
                                 * 更新录音详情
                                 */

                                RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();
                                testDao.updateCloudStatus(1, file.getPath());


                                toast("提交成功");

                            } else {
                                toast("上传失败");
                            }

                        }

                        @Override
                        public void onFailure(Call<RecordUploadResult> call, Throwable t) {
                            toast("上传失败");

                        }
                    });
                }


            }
        });


        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mMediaPlayer = new MediaPlayer();

                try {
                    mMediaPlayer.setDataSource(recordDetailItem.getFilePath());
                    mMediaPlayer.prepare();
                    Log.i(TAG, "getDuration:" + mMediaPlayer.getDuration());
                    holder.seekbar.setMax(mMediaPlayer.getDuration());


                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mMediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "prepare() failed");
                }

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlaying(holder);
                    }
                });

                mRunnable = new MyRunnable(holder);

                mHandler.postDelayed(mRunnable, 1000);

            }
        });
        if (selectItem >= 0) {

            if (position == selectItem) {
                holder.rl_seekShow.setVisibility(View.VISIBLE);
                holder.rl_btshow.setVisibility(View.VISIBLE);
                holder.seekbar.setVisibility(View.VISIBLE);
            } else {
                holder.rl_seekShow.setVisibility(View.GONE);
                holder.rl_btshow.setVisibility(View.GONE);
                holder.seekbar.setVisibility(View.GONE);

            }
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(recordDetailItem.getFile_elpased());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(recordDetailItem.getFile_elpased())
                - TimeUnit.MINUTES.toSeconds(minutes);

        holder.file_length_text_view.setText(String.format("%02d:%02d", minutes, seconds));


        return view;

    }

    private void toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    MyRunnable mRunnable;

    class MyRunnable implements Runnable {
        ViewHolder holder;

        public MyRunnable(ViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {

            if (mMediaPlayer != null) {

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                holder.seekbar.setProgress(mCurrentPosition);

                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
                        - TimeUnit.MINUTES.toSeconds(minutes);
                holder.current_progress_text_view.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar(holder);
            }
        }
    }


    private void stopPlaying(ViewHolder holder) {
        mHandler.removeCallbacks(mRunnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;

        holder.seekbar.setProgress(holder.seekbar.getMax());


        holder.current_progress_text_view.setText(holder.file_length_text_view.getText());
    }

    private void updateSeekBar(ViewHolder holder) {
        mHandler.postDelayed(mRunnable, 1000);
    }


    static class ViewHolder {

        LinearLayout showArea;
        TextView text;
        TextView file_length_text_view;
        TextView current_progress_text_view;
        SeekBar seekbar;
        Button detailOrUpload;
        Button play;

        RelativeLayout rl_seekShow;
        RelativeLayout rl_btshow;

        String type;
        int position;


    }
}
