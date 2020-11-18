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

//    public static DownloadAudio convertToDownloadAudio(ListenAudioEntity listenAudio) {
//        return new DownloadAudio(
//                listenAudio.getAudio_id(),
//                listenAudio.getAudio_type(),
//                listenAudio.getAudio_name(),
//                listenAudio.getOriginal_name(),
//                listenAudio.getStatus(),
//                listenAudio.getAuthor_intro(),
//                listenAudio.getAnchor_id(),
//                listenAudio.getShort_intro(),
//                listenAudio.getAudio_intro(),
//                listenAudio.getAudio_cover(),
//                listenAudio.getCover_url(),
//                listenAudio.getAudio_label(),
//                listenAudio.getQuality(),
//                listenAudio.getProgress(),
//                listenAudio.getPlay_count(),
//                listenAudio.getCreated_at(),
//                listenAudio.getChapter_updated_at(),
//                listenAudio.getAnchor_name(),
//                listenAudio.getUpdateMillis(),
//                listenAudio.getAuthor(),
//                listenAudio.getMember_id(),
//                listenAudio.getNickname(),
//                listenAudio.getSubscription_count(),
//                listenAudio.getLast_sequence(),
//                listenAudio.getAudio_cover_url(),
//                listenAudio.getAnchor(),
//                listenAudio.getTags(),
//                listenAudio.getIs_subscribe(),
//                listenAudio.getIs_fav(),
//                listenAudio.getDownload_num(),
//                listenAudio.getDown_size(),
//                listenAudio.getEdit_select(),
//                listenAudio.getListen_finish()
//        );
//    }

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
