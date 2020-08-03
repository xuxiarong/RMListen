package com.music.player.lib.listener;

import com.music.player.lib.bean.BaseAudioInfo;

/**
 * TinyHung@Outlook.com
 * 2019/3/5.
 * MusicPlayer Event Listener
 */

public interface MusicPlayerEventListener {

    /**
     * 播放器所有状态回调
     * @param playerState 播放器内部状态
     */
    void onMusicPlayerState(int playerState,String message);

    /**
     * 播放器准备好了
     * @param totalDurtion 总时长
     */
    void onPrepared(long totalDurtion);

    /**
     * 缓冲百分比
     * @param percent 百分比
     * 此方法已被废弃，缓冲进度播放器内部使用变量储存，每隔500mm连同播放进度回调至组件
     * 合并至onTaskRuntime方法
     */
    @Deprecated
    void onBufferingUpdate(int percent);

    /**
     * 播放器反馈信息
     * @param event 事件
     * @param extra
     */
    void onInfo(int event, int extra);

    /**
     * 当前正在播放的任务
     * @param musicInfo 正在播放的对象
     * @param position 当前正在播放的位置
     */

    void onPlayMusiconInfo(BaseAudioInfo musicInfo, int position);

    /**
     * 音频地址无效,组件可处理付费购买等逻辑
     * @param musicInfo 播放对象
     * @param position 索引
     */
    void onMusicPathInvalid(BaseAudioInfo musicInfo,int position);

    /**
     * @param totalDurtion 音频总时间
     * @param currentDurtion 当前播放的位置
     * @param alarmResidueDurtion 闹钟剩余时长
     * @param bufferProgress 当前缓冲进度
     */
    void onTaskRuntime(long totalDurtion, long currentDurtion, long alarmResidueDurtion, int bufferProgress);

    /**
     * @param playModel 播放模式
     * @param alarmModel 闹钟模式
     * @param isToast 是否吐司提示
     */
    void onPlayerConfig(int playModel, int alarmModel,boolean isToast);
}
