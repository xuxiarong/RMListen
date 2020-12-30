package com.rm.business_lib.db.converter;

import com.rm.business_lib.db.download.DownloadAudio;
import com.rm.business_lib.db.download.DownloadChapter;
import com.rm.business_lib.db.listen.ListenAudioEntity;
import com.rm.business_lib.db.listen.ListenChapterEntity;

/**
 * desc   :
 * date   : 2020/11/11
 * version: 1.0
 */
public class BusinessConvert {

    public static DownloadAudio convertToDownloadAudio(ListenAudioEntity listenAudio) {
        DownloadAudio downloadAudio = new DownloadAudio();
        downloadAudio.setAudio_id(listenAudio.getAudio_id());
        downloadAudio.setAudio_type(listenAudio.getAudio_type());
        downloadAudio.setAudio_name(listenAudio.getAudio_name());
        downloadAudio.setOriginal_name(listenAudio.getOriginal_name());
        downloadAudio.setStatus(listenAudio.getStatus());
        downloadAudio.setAuthor_intro(listenAudio.getAuthor_intro());
        downloadAudio.setAnchor_id(listenAudio.getAnchor_id());
        downloadAudio.setShort_intro(listenAudio.getShort_intro());
        downloadAudio.setAudio_intro(listenAudio.getAudio_intro());
        downloadAudio.setAudio_cover(listenAudio.getAudio_cover());
        downloadAudio.setCover_url(listenAudio.getCover_url());
        downloadAudio.setAudio_label(listenAudio.getAudio_label());
        downloadAudio.setQuality(listenAudio.getQuality());
        downloadAudio.setProgress(listenAudio.getProgress());
        downloadAudio.setPlay_count(listenAudio.getPlay_count());
        downloadAudio.setCreated_at(listenAudio.getCreated_at());
        downloadAudio.setChapter_updated_at(listenAudio.getChapter_updated_at());
        downloadAudio.setAnchor_name(listenAudio.getAnchor_name());
        downloadAudio.setUpdateMillis(listenAudio.getUpdateMillis());
        downloadAudio.setSortType(listenAudio.getSortType());
        downloadAudio.setListenChapterId(listenAudio.getListenChapterId());
        downloadAudio.setAuthor(listenAudio.getAuthor());
        downloadAudio.setMember_id(listenAudio.getMember_id());
        downloadAudio.setNickname(listenAudio.getNickname());
        downloadAudio.setSubscription_count(listenAudio.getSubscription_count());
        downloadAudio.setLast_sequence(listenAudio.getLast_sequence());
        downloadAudio.setAudio_cover_url(listenAudio.getAudio_cover_url());
        downloadAudio.setAnchor(listenAudio.getAnchor());
        downloadAudio.setTags(listenAudio.getTags());
        downloadAudio.setIs_subscribe(listenAudio.getIs_subscribe());
        downloadAudio.setIs_fav(listenAudio.getIs_fav());
        downloadAudio.setDownload_num(listenAudio.getDownload_num());
        downloadAudio.setDown_size(listenAudio.getDown_size());
        downloadAudio.setEdit_select(listenAudio.getEdit_select());
        downloadAudio.setListen_finish(listenAudio.getListen_finish());
        return downloadAudio;
    }

    public static ListenAudioEntity convertToListenAudio(DownloadAudio downloadAudio) {
        ListenAudioEntity listenAudioEntity = new ListenAudioEntity();
        listenAudioEntity.setAudio_id(downloadAudio.getAudio_id());
        listenAudioEntity.setAudio_type(downloadAudio.getAudio_type());
        listenAudioEntity.setAudio_name(downloadAudio.getAudio_name());
        listenAudioEntity.setOriginal_name(downloadAudio.getOriginal_name());
        listenAudioEntity.setStatus(downloadAudio.getStatus());
        listenAudioEntity.setAuthor_intro(downloadAudio.getAuthor_intro());
        listenAudioEntity.setAnchor_id(downloadAudio.getAnchor_id());
        listenAudioEntity.setShort_intro(downloadAudio.getShort_intro());
        listenAudioEntity.setAudio_intro(downloadAudio.getAudio_intro());
        listenAudioEntity.setAudio_cover(downloadAudio.getAudio_cover());
        listenAudioEntity.setCover_url(downloadAudio.getCover_url());
        listenAudioEntity.setAudio_label(downloadAudio.getAudio_label());
        listenAudioEntity.setQuality(downloadAudio.getQuality());
        listenAudioEntity.setProgress(downloadAudio.getProgress());
        listenAudioEntity.setPlay_count(downloadAudio.getPlay_count());
        listenAudioEntity.setCreated_at(downloadAudio.getCreated_at());
        listenAudioEntity.setChapter_updated_at(downloadAudio.getChapter_updated_at());
        listenAudioEntity.setAnchor_name(downloadAudio.getAnchor_name());
        listenAudioEntity.setUpdateMillis(downloadAudio.getUpdateMillis());
        listenAudioEntity.setSortType(downloadAudio.getSortType());
        listenAudioEntity.setListenChapterId(downloadAudio.getListenChapterId());
        listenAudioEntity.setAuthor(downloadAudio.getAuthor());
        listenAudioEntity.setMember_id(downloadAudio.getMember_id());
        listenAudioEntity.setNickname(downloadAudio.getNickname());
        listenAudioEntity.setSubscription_count(downloadAudio.getSubscription_count());
        listenAudioEntity.setLast_sequence(downloadAudio.getLast_sequence());
        listenAudioEntity.setAudio_cover_url(downloadAudio.getAudio_cover_url());
        listenAudioEntity.setAnchor(downloadAudio.getAnchor());
        listenAudioEntity.setTags(downloadAudio.getTags());
        listenAudioEntity.setIs_subscribe(downloadAudio.getIs_subscribe());
        listenAudioEntity.setIs_fav(downloadAudio.getIs_fav());
        listenAudioEntity.setDownload_num(downloadAudio.getDownload_num());
        listenAudioEntity.setDown_size(downloadAudio.getDown_size());
        listenAudioEntity.setEdit_select(downloadAudio.getEdit_select());
        listenAudioEntity.setListen_finish(downloadAudio.getListen_finish());
        return listenAudioEntity;
    }

    public static DownloadChapter convertToDownloadChapter(ListenChapterEntity listenChapter) {
        return new DownloadChapter(
                listenChapter.getChapter_id(),
                listenChapter.getAudio_id(),
                listenChapter.getAudio_name(),
                listenChapter.getSequence(),
                listenChapter.getChapter_name(),
                listenChapter.getSize(),
                listenChapter.getDown_speed(),
                listenChapter.getCurrent_offset(),
                listenChapter.getDuration(),
                listenChapter.getRealDuration(),
                listenChapter.getNeed_pay(),
                listenChapter.getAmount(),
                listenChapter.getPlay_count(),
                listenChapter.getCreated_at(),
                listenChapter.getFile_path(),
                listenChapter.getPath_url(),
                listenChapter.getDown_status(),
                listenChapter.getListen_duration(),
                listenChapter.getUpdateMillis(),
                listenChapter.getChapter_edit_select(),
                listenChapter.getDown_edit_select()
        );
    }

    public static ListenChapterEntity convertToListenChapter(DownloadChapter downloadChapter) {
        return new ListenChapterEntity(
                downloadChapter.getChapter_id(),
                downloadChapter.getAudio_id(),
                downloadChapter.getAudio_name(),
                downloadChapter.getSequence(),
                downloadChapter.getChapter_name(),
                downloadChapter.getSize(),
                downloadChapter.getDown_speed(),
                downloadChapter.getCurrent_offset(),
                downloadChapter.getDuration(),
                downloadChapter.getRealDuration(),
                downloadChapter.getNeed_pay(),
                downloadChapter.getAmount(),
                downloadChapter.getPlay_count(),
                downloadChapter.getCreated_at(),
                downloadChapter.getFile_path(),
                downloadChapter.getPath_url(),
                downloadChapter.getDown_status(),
                downloadChapter.getListen_duration(),
                downloadChapter.getUpdateMillis(),
                downloadChapter.getChapter_edit_select(),
                downloadChapter.getDown_edit_select()
        );
    }


}
