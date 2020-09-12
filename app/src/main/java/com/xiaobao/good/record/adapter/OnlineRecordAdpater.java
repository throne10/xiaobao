package com.xiaobao.good.record.adapter;

import android.content.Context;
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

import com.xiaobao.good.R;
import com.xiaobao.good.record.RecordDetailItem;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnlineRecordAdpater extends BaseAdapter {
    private static final String TAG = "OnlineRecordAdpater";


    List<RecordDetailItem> list;

    Context context;

    MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    int selectItem = -1;


    public OnlineRecordAdpater(Context context, List<RecordDetailItem> list) {
        this.list = list;
        this.context = context;
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

        ViewHolder holder;
        RecordDetailItem recordDetailItem = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_record_detail_item, null);
            holder = new ViewHolder();

            holder.showArea = convertView.findViewById(R.id.ll_showArea);
            holder.text = (TextView) convertView.findViewById(R.id.tv_record_name);
            holder.seekbar = (SeekBar) convertView.findViewById(R.id.seekbar);

            holder.detailOrUpload = (Button) convertView.findViewById(R.id.bt_detail_or_upload);
            holder.play = (Button) convertView.findViewById(R.id.bt_play);


            holder.rl_btshow = convertView.findViewById(R.id.rl_btshow);
            holder.rl_seekShow = convertView.findViewById(R.id.rl_seekShow);

            holder.current_progress_text_view = convertView.findViewById(R.id.current_progress_text_view);
            holder.file_length_text_view = convertView.findViewById(R.id.file_length_text_view);


//            holder.seekbar.setEnabled(false);


            holder.text.setText(recordDetailItem.getFilePath());
            String typeMsg = recordDetailItem.getType().equals("0") ? "详情" : "上传";
            holder.detailOrUpload.setText(typeMsg);


            holder.showArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectItem = position;
                    notifyDataSetChanged(); //必须
                }
            });

            holder.detailOrUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     * 根据type,展示详情，还是上传
                     */

//                    File file = new File(recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3");
//
//                    RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                    MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), fileRQ);
//
//                    RequestBody body = new MultipartBody.Builder()
//                            .addFormDataPart("visit_id", visitId + "")
//                            .addFormDataPart("voiceFile", file.getName(), fileRQ)
//                            .build();
//                    Call<RecordUploadResult> uploadCall = RetrofitUtils.getService().uploadVoice(body);
//                    uploadCall.enqueue(new Callback<RecordUploadResult>() {
//                        @Override
//                        public void onResponse(Call<RecordUploadResult> call, Response<RecordUploadResult> response) {
//
//                            Log.i(TAG, "recordupload :" + response.body().getCode());
//                            String code = response.body().getCode();
//
//                            if ("success".equals(code)) {
//                                /**
//                                 * 更新录音详情
//                                 */
//
//                                RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();
//                                testDao.updateCloudStatus(1, file.getPath());
//
//
//                                toast("提交成功");
//
//                            } else {
//                                toast("上传失败");
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<RecordUploadResult> call, Throwable t) {
//                            toast("上传失败");
//
//                        }
//                    });


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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


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


        return convertView;

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
