package com.allever.robotbestpractice.ifly;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.allever.robotbestpractice.R;
import com.allever.robotbestpractice.wakeup.IWakeupProxy;
import com.allever.robotbestpractice.wakeup.WakeupListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author allever
 */
public class IFlyWakeupProxy implements IWakeupProxy {

    private VoiceWakeuper mVoiceWakeuper;
    private WakeupListener mWakeupListener;

    @Override
    public void init(@NotNull Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID +"=" + context.getString(R.string.app_id));
        // 初始化唤醒对象
        mVoiceWakeuper = VoiceWakeuper.createWakeuper(context, new InitListener() {
            @Override
            public void onInit(int i) {
                log("初始化唤醒 = " + i);
            }
        });

        if (mVoiceWakeuper != null) {
            // 清空参数
            mVoiceWakeuper.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mVoiceWakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:1450");
            // 设置唤醒模式
            mVoiceWakeuper.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mVoiceWakeuper.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            // 设置闭环优化网络模式
            //这个Msc sdk 没有这个字段
//            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode)
            // 设置唤醒资源路径
            mVoiceWakeuper.setParameter(SpeechConstant.IVW_RES_PATH, getResource(context));
            // 设置唤醒录音保存路径，保存最近一分钟的音频
//            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mVoiceWakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
//            mVoiceWakeuper.setParameter(SpeechConstant.NOTIFY_RECORD_DATA, "1");
        }
    }

    @Override
    public void setListener(@NotNull WakeupListener wakeupListener) {
        mWakeupListener = wakeupListener;
    }

    private WakeuperListener mWakeupeListener = new WakeuperListener() {
        @Override
        public void onResult(WakeuperResult result) {
            log("返回结果");
            if (!"1".equalsIgnoreCase("1")) {
//                setRadioEnable(true);
            }
            String resultString;
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();

                mWakeupListener.onWakeup(0);
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }

            log(resultString);
        }

        @Override
        public void onError(SpeechError error) {
            log("出错 errorCode = " + error.getErrorCode());
            log("出错 errorMsg = " + error.getErrorDescription());
//            showTip(error.getPlainDescription(true));
//            setRadioEnable(true);
        }

        @Override
        public void onBeginOfSpeech() {
            log("开始说话");
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch (eventType) {
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA);
                    log("ivw audio length: " + audio.length);
                    break;
                default:
            }
        }

        @Override
        public void onVolumeChanged(int volume) {
            log("onVolumeChanged " + volume);
        }
    };

    private void log(String msg) {
        Log.d("IFlyWakeupProxy", msg);
    }

    @Override
    public void startWakeup() {
        if (mVoiceWakeuper != null) {
            mVoiceWakeuper.startListening(mWakeupeListener);
        }
    }

    @Override
    public void stopWakeup() {
        if (mVoiceWakeuper != null) {
            mVoiceWakeuper.stopListening();
        }
    }

    @Override
    public void destroy() {
        if (mVoiceWakeuper != null) {
            mVoiceWakeuper.destroy();
        }
    }

    /**
     * 获取唤醒词功能
     *
     * @return 返回文件位置
     */
    private String getResource(Context context) {
        return ResourceUtil.generateResourcePath(
                context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/" + context.getString(R.string.app_id) + ".jet"
        );
    }

}
