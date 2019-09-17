//package com.tc.fullmediarobot.function.awake
//
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import com.iflytek.cloud.*
//import com.iflytek.cloud.util.ResourceUtil
//import com.orhanobut.logger.Logger
//import com.tc.fullmediarobot.R
//import com.tc.fullmediarobot.utils.SharedPrefUtil
//import org.json.JSONException
//import org.json.JSONObject
//
//object WakeUpHelper {
//
//    private val TAG = "WakeUpHelper"
//
//    //唤醒的阈值，就相当于门限值，当用户输入的语音的置信度大于这一个值的时候，才被认定为成功唤醒。
//    private const val WAKEUP_THRESHOLD = 1450
//    //是否持续唤醒
//    private const val keep_alive = "1"
//
//    private const val SP_KEY_IS_AWAKE = "isAwake"
//    /**
//     * 闭环优化网络模式有三种：
//     * 模式0：关闭闭环优化功能
//     *
//     *
//     *
//     * 模式1：开启闭环优化功能，允许上传优化数据。需开发者自行管理优化资源。
//     * sdk提供相应的查询和下载接口，请开发者参考API文档，具体使用请参考本示例
//     * queryResource及downloadResource方法；
//     *
//     *
//     * 模式2：开启闭环优化功能，允许上传优化数据及启动唤醒时进行资源查询下载；
//     * 本示例为方便开发者使用仅展示模式0和模式2；
//     */
//    private val ivwNetMode = "2"
//    // 语音唤醒对象
//    private var mVoiceWakeUper: VoiceWakeuper? = null
//    //存储唤醒词的ID
//    private val wordID = ""
//
//    private var mActivater: Activater? = null
//    private var mWakeupListner: MyWakeupListener? = null
//
//    fun create(context: Context) {
//        // 初始化唤醒对象
//        mVoiceWakeUper = VoiceWakeuper.createWakeuper(context) { i ->
//            Logger.wtf(i.toString() + "初始化-----")
//        }
//
//        mWakeupListner = MyWakeupListener(context)
//    }
//
//    /**
//     * 开启唤醒功能
//     */
//    fun startWakeUp(context: Context) {
//        Log.d(TAG, "开启唤醒功能")
//        //非空判断，防止因空指针使程序崩溃
//        mVoiceWakeUper = VoiceWakeuper.getWakeuper()
//        if (mVoiceWakeUper != null) {
//            // 清空参数
//            mVoiceWakeUper?.setParameter(SpeechConstant.PARAMS, null)
//            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
//            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_THRESHOLD, "0:$WAKEUP_THRESHOLD")
//            // 设置唤醒模式
//            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_SST, "wakeup")
//            // 设置持续进行唤醒
//            mVoiceWakeUper?.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive)
//            // 设置闭环优化网络模式
//            //这个Msc sdk 没有这个字段
////            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode)
//            // 设置唤醒资源路径
//            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_RES_PATH, getResource(context))
//            // 设置唤醒录音保存路径，保存最近一分钟的音频
////            mVoiceWakeUper?.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
//            mVoiceWakeUper?.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
//            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
////            mVoiceWakeUper?.setParameter(SpeechConstant.NOTIFY_RECORD_DATA, "1")
//            // 启动唤醒
//            mVoiceWakeUper?.startListening(mWakeupListner)
//        }
//    }
//
//    fun setOnActivater(activater: Activater) {
//        this.mActivater = activater
//    }
//
//    fun updateAwakeState(isAwake: Boolean) {
//        SharedPrefUtil.putBoolean(SP_KEY_IS_AWAKE, isAwake) //退出主界面关闭唤醒
//    }
//
//    fun getAwakeState(): Boolean {
//        val state =
//            SharedPrefUtil.getBoolean(SP_KEY_IS_AWAKE, false)
//        Logger.e("wakeup state = $state")
//        return state
//    }
//
//    /**
//     * 销毁唤醒功能
//     */
//    fun destroyWakeup() {
//        // 销毁合成对象
//        mVoiceWakeUper = VoiceWakeuper.getWakeuper()
//        mVoiceWakeUper?.destroy()
//    }
//
//    /**
//     * 停止唤醒
//     */
//    fun stopWakeup() {
//        if (mVoiceWakeUper?.isListening == true) {
//            mVoiceWakeUper?.stopListening()
//            Log.d(TAG, "停止唤醒")
//        }
//    }
//
//
//    /**
//     * 获取唤醒词功能
//     *
//     * @return 返回文件位置
//     */
//    private fun getResource(context: Context): String {
//        return ResourceUtil.generateResourcePath(
//            context,
//            ResourceUtil.RESOURCE_TYPE.assets,
//            "ivw/" + context.getString(R.string.app_id) + ".jet"
//        )
//    }
//
//    /**
//     * 唤醒词监听类
//     *
//     * @author Administrator
//     */
//    private class MyWakeupListener(val context: Context) : WakeuperListener {
//        //开始说话
//        override fun onBeginOfSpeech() {
//            Logger.wtf("MyWakeupListener 开始说话")
//        }
//
//        //错误码返回
//        override fun onError(arg0: SpeechError) {
//            Logger.wtf("MyWakeupListener 出错 errorCode = ${arg0.errorCode}")
//            Logger.wtf(arg0.toString())
////            stopWakeup()
////            startWakeUp(context)
//        }
//
//        override fun onEvent(arg0: Int, arg1: Int, arg2: Int, arg3: Bundle) {
//            if (SpeechEvent.EVENT_IVW_RESULT == arg0) {
//                // 当使用唤醒+识别功能时获取识别结果
//                // arg1:是否最后一个结果，1:是，0:否。
//                val result = arg3.get(SpeechEvent.KEY_EVENT_IVW_RESULT) as RecognizerResult
//                Logger.wtf(result.resultString)
//            }
//        }
//
//        /**
//         * 声音改变回调
//         */
//        override fun onVolumeChanged(i: Int) {
//
//        }
//
//        override fun onResult(result: WakeuperResult) {
//            Logger.wtf("MyWakeupListener 返回结果")
//            if (!"1".equals(keep_alive, ignoreCase = true)) {
//                //setRadioEnable(true);
//            }
//            try {
//                val text = result.resultString
//                val `object`: JSONObject
//                `object` = JSONObject(text)
//                val buffer = StringBuffer()
//                buffer.append("【RAW】 $text")
//                buffer.append("\n")
//                buffer.append("【操作类型】" + `object`.optString("sst"))
//                buffer.append("\n")
//                buffer.append("【唤醒词id】" + `object`.optString("id"))
//                buffer.append("\n")
//                buffer.append("【得分】" + `object`.optString("score"))
//                buffer.append("\n")
//                buffer.append("【前端点】" + `object`.optString("bos"))
//                buffer.append("\n")
//                buffer.append("【尾端点】" + `object`.optString("eos"))
//                Logger.wtf(buffer.toString())
//
//                if (mActivater != null) {
//                    mActivater?.onActivated()
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//        }
//    }
//
//    // 回调接口e
//
//    interface Activater {
//        fun onActivated()
//    }
//
//}