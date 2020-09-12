package com.xiaobao.good.record.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

            holder.seekbar.setEnabled(false);

            RecordDetailItem recordDetailItem = list.get(position);
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


                }
            });


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mMediaPlayer = new MediaPlayer();

                    try {
                        mMediaPlayer.setDataSource(recordDetailItem.getFilePath());
                        mMediaPlayer.prepare();
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
                            stopPlaying();
                        }
                    });

                    updateSeekBar();

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


        return convertView;

    }

    //updating mSeekBar
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {

                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
//                mSeekBar.setProgress(mCurrentPosition);
//
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition);
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition)
//                        - TimeUnit.MINUTES.toSeconds(minutes);
//                mCurrentProgressTextView.setText(String.format("%02d:%02d", minutes, seconds));

                updateSeekBar();
            }
        }
    };


    private void stopPlaying() {
//        mHandler.removeCallbacks(mRunnable);
//        mMediaPlayer.stop();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
//
//        mSeekBar.setProgress(mSeekBar.getMax());
//        isPlaying = !isPlaying;
//
//        mCurrentProgressTextView.setText(mFileLengthTextView.getText());
//        mSeekBar.setProgress(mSeekBar.getMax());
//
//        //allow the screen to turn off again once audio is finished playing
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void updateSeekBar() {
        mHandler.postDelayed(mRunnable, 1000);
    }


    static class ViewHolder {

        LinearLayout showArea;
        TextView text;
        SeekBar seekbar;
        Button detailOrUpload;
        Button play;

        RelativeLayout rl_seekShow;
        RelativeLayout rl_btshow;

        String type;
        int position;


    }
}
