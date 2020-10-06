package my.test.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import androidx.annotation.NonNull;
import my.test.myapplication.R;
import my.test.myapplication.activity.VideoPlayActivity;
import my.test.myapplication.bean.DailyVideos;
import my.test.myapplication.view.CoverVideoPlayerView;

public class VideoPlayerAdapter extends BaseQuickAdapter<DailyVideos.IssueListBean.ItemListBean, BaseViewHolder> {
    private Context context;
    public VideoPlayerAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.context=activity;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DailyVideos.IssueListBean.ItemListBean item) {

        ((CoverVideoPlayerView)helper.getView(R.id.video_player_item)).getBackButton().setVisibility(View.INVISIBLE);
        if(item==null|item.getType().equals("banner2"))
            return;
        helper.setText(R.id.item_number,item.getData().getTitle());
        try {
            Glide.with(mContext).load(item.getData().getAuthor().getIcon()).into((ImageView) helper.getView(R.id.imageV));
            helper.setText(R.id.content,item.getData().getAuthor().getName());
            ((CoverVideoPlayerView)helper.getView(R.id.video_player_item)).loadCoverImage(item.getData().getCover().getFeed(),0);
        }catch (NullPointerException e){

        }
        GSYVideoOptionBuilder gsyVideoOptionBuilder =
                new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder.setIsTouchWiget(false)
                .setUrl(item.getData().getPlayUrl())
              //  .setVideoTitle(item.getData().getTitle())
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag("2")
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(helper.getAdapterPosition())
                .setVideoAllCallBack(new GSYSampleCallBack(){
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        GSYVideoManager.instance().setNeedMute(true);
                    }
                }).build((StandardGSYVideoPlayer) helper.getView(R.id.video_player_item));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, VideoPlayActivity.class);
                i.putExtra("url",item.getData().getPlayUrl());
         //       i.putExtra("cover",item.getData().getCover().getFeed());
                i.putExtra("title",item.getData().getTitle());
                context.startActivity(i);
            }
        });
        ((CoverVideoPlayerView)helper.getView(R.id.video_player_item)).rvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, VideoPlayActivity.class);
                i.putExtra("url",item.getData().getPlayUrl());
        //        i.putExtra("cover",item.getData().getCover().getFeed());
                i.putExtra("title",item.getData().getTitle());
                context.startActivity(i);
            }
        });
        ((CoverVideoPlayerView)helper.getView(R.id.video_player_item)).getThumbImageViewLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, VideoPlayActivity.class);
                i.putExtra("url",item.getData().getPlayUrl());
         //       i.putExtra("cover",item.getData().getCover().getFeed());
                i.putExtra("title",item.getData().getTitle());
                context.startActivity(i);
            }
        });
    }
}
