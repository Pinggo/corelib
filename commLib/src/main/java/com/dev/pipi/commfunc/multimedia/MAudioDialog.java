package com.dev.pipi.commfunc.multimedia;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncodeUtils;
import com.dev.pipi.commlib.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by pwj on 2017/9/20.
 */

public class MAudioDialog extends Dialog implements View.OnClickListener {

    private TextView mTvTime, mTvNotice;
    // private TextView mTvTimeLengh;//录音长度
    private ImageView mIvRecord;
    // private ImageView mIvVoiceAnim;
    private LinearLayout mSoundLengthLayout;
    // private RelativeLayout mRlVoiceLayout;
    // 语音相关
    private ScaleAnimation mScaleBigAnimation;
    private ScaleAnimation mScaleLittleAnimation;
    private String mSoundData = "";      // 语音数据路径
    private String dataPath;
    private boolean isStop;               // 录音是否结束的标志 超过两分钟停止
    private boolean isCanceled = false;   // 是否取消录音
    private float downY;
    private MediaRecorder mRecorder;
    private long mStartTime;
    private long mEndTime;
    private int mTime;
    private String mVoiceData;
    private AnimationDrawable mImageAnim;
    private Context ctx;
    private View view;

    public MAudioDialog(Context context) {
        super(context, R.style.Screen_style);
        this.setCanceledOnTouchOutside(true);//
        this.ctx = context;
        view = LayoutInflater.from(ctx).inflate(R.layout.layout_audio, null);
        setContentView(view);
        initView(view);
    }

    public MAudioDialog(Context context, int theme) {
        super(context, theme);

    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    /**
     * 设置大小
     */
    private void setSize() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        DisplayMetrics d = ctx.getResources().getDisplayMetrics(); //
        // 获取屏幕宽、高用
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); // 就能够水平占满了
        lp.width = (int) (d.widthPixels * 1.0);
        lp.height = (int) (d.heightPixels * 0.35);
        dialogWindow.setAttributes(lp);
    }

    private void initView(View view) {
        setSize();
        initSoundData();
        mTvTime = (TextView) view.findViewById(R.id.chat_tv_sound_length);
        mTvNotice = (TextView) view.findViewById(R.id.chat_tv_sound_notice);
        mIvRecord = (ImageView) view.findViewById(R.id.chat_record);
        mSoundLengthLayout = (LinearLayout) view.findViewById(R.id.chat_tv_sound_length_layout);
        mIvRecord.setOnTouchListener(new VoiceTouch(view));
    }

    @Override
    public void onClick(View arg0) {

    }

    /**
     * 录音存放路径
     */
    public void initSoundData() {
        dataPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator+ "fs"+ File.separator+"multimedia"+ File.separator;
        File folder = new File(dataPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    /**
     * 录音的触摸监听
     */
    class VoiceTouch implements View.OnTouchListener {
        View activity;

        public VoiceTouch(View activity) {
            super();
            this.activity = activity;
        }
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = motionEvent.getY();
                    mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record_pressed));
                    mTvNotice.setText("向上滑动取消发送");
                    mSoundData = dataPath + getRandomFileName() + ".mp4";

                    // 防止开权限后崩溃
                    if (mRecorder != null) {
                        mRecorder.reset();
                    } else {
                        mRecorder = new MediaRecorder();
                    }
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setOutputFile(mSoundData);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mRecorder.start();
                        mStartTime = System.currentTimeMillis();
                        mSoundLengthLayout.setVisibility(View.VISIBLE);
                        mTvTime.setText("0" + '"');
                        // 开启定时器
                        mHandler.postDelayed(runnable, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    initScaleAnim();
                    // 录音过程重复动画
                    mScaleBigAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mScaleLittleAnimation != null) {
                                mIvRecord.startAnimation(mScaleLittleAnimation);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mScaleLittleAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (mScaleBigAnimation != null) {
                                mIvRecord.startAnimation(mScaleBigAnimation);
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mIvRecord.startAnimation(mScaleBigAnimation);

                    break;
                case MotionEvent.ACTION_UP:
                    if (!isStop) {
                        mEndTime = System.currentTimeMillis();
                        mTime = (int) ((mEndTime - mStartTime) / 1000);
                        stopRecord();
                        mIvRecord.setVisibility(View.VISIBLE);
                        try {
                            mVoiceData = EncodeUtils.base64Encode2String(mSoundData.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (isCanceled) {
//                        deleteSoundFileUnSend();
                            mTvTime.setText("0" + '"');
                            mTvNotice.setText("录音取消");
                            // mRlVoiceLayout.setVisibility(View.GONE);
                        } else {
                            mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record));
                            // mRlVoiceLayout.setVisibility(View.VISIBLE);
                            // mTvTimeLengh.setText(mTime + "" + '"');
                        }
                    } else {
                        mTvTime.setText("0");
                        mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record));
                        mTvNotice.setText("重新录音");
                    }
                    break;
                case MotionEvent.ACTION_CANCEL: // 首次开权限时会走这里，录音取消
                    // 权限影响录音录音
                    mHandler.removeCallbacks(runnable);
                    mSoundLengthLayout.setVisibility(View.GONE);

                    // 这里一定注意，先release，还要置为null，否则录音会发生错误，还有可能崩溃
                    if (mRecorder != null) {
                        mRecorder.release();
                        mRecorder = null;
                        System.gc();
                    }
                    mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record));
                    mIvRecord.clearAnimation();
                    mTvNotice.setText("按住说话");
                    isCanceled = true;
                    mScaleBigAnimation = null;
                    mScaleLittleAnimation = null;

                    break;

                case MotionEvent.ACTION_MOVE: // 滑动手指
                    float moveY = motionEvent.getY();
                    if (downY - moveY > 100) {
                        isCanceled = true;
                        mTvNotice.setText("松开手指可取消录音");
                        mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record));
                    }
                    if (downY - moveY < 20) {
                        isCanceled = false;
                        mIvRecord.setImageDrawable(activity.getResources().getDrawable(R.drawable.record_pressed));
                        mTvNotice.setText("向上滑动可取消录音");
                    }
                    break;

            }
            return true;
        }
    }

    /**
     * 结束录音
     */
    public void stopRecord() {
        mIvRecord.clearAnimation();
        mScaleBigAnimation = null;
        mScaleLittleAnimation = null;
        if (mTime < 1) {
            deleteSoundFileUnSend();
            isCanceled = true;
            Toast.makeText(ctx, "录音时间太短", Toast.LENGTH_SHORT).show();
        } else {
            if (!isCanceled) {
                mTvNotice.setText("录制音频");
                mTvTime.setText(mTime + "" + '"');
                // 录音成功
            }
        }
        try {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
        } catch (Exception e) {
            isCanceled = true;
            mIvRecord.setVisibility(View.VISIBLE);
            mTvTime.setText("");
            Toast.makeText(ctx, "录音发生错误,请重新录音", Toast.LENGTH_LONG).show();
            // 录音发生错误
        }
        mHandler.removeCallbacks(runnable);
        if (mRecorder != null) {
            mRecorder = null;
            System.gc();
        }
        if (mSoundData != null && !"".equals(mSoundData.trim())) {
            if (audio != null) {
                audio.setAudioData(mSoundData, mTime + "");
            }
            dismiss();
        }

    }

    // 定时器
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                long endTime = System.currentTimeMillis();
                int time = (int) ((endTime - mStartTime) / 1000);
                // mRlSoundLengthLayout.setVisibility(View.VISIBLE);
                mTvTime.setText(time + "" + '"');
                // 限制录音时间不长于两分钟
                if (time > 119) {
                    isStop = true;
                    mTime = time;
                    stopRecord();
                    // Toast.makeText(activity.this, "时间过长", Toast.LENGTH_SHORT).show();
                } else {
                    mHandler.postDelayed(this, 1000);
                    isStop = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 生成一个随机的文件名
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public String getRandomFileName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel + new Random().nextInt(1000);
        return rel;
    }

    /**
     * 初始化录音动画
     */
    public void initScaleAnim() {

        // 放大
        mScaleBigAnimation = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleBigAnimation.setDuration(700);

        // 缩小
        mScaleLittleAnimation = new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleLittleAnimation.setDuration(700);
    }

    /**
     * 录音完毕后，若不发送，则删除文件
     */
    public void deleteSoundFileUnSend() {
        mTime = 0;
        if (!"".equals(mSoundData)) {
            try {
                File file = new File(mSoundData);
                file.delete();
                mSoundData = "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface InterfaceAudio {
        public void setAudioData(String path, String audioLength);
    }

    private InterfaceAudio audio;

    public void setInterFace(InterfaceAudio audio) {
        this.audio = audio;
    }
}

