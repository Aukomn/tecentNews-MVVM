package my.test.myapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;

import androidx.annotation.NonNull;
import my.test.myapplication.R;
import my.test.myapplication.activity.NewsDetailActivity;
import my.test.myapplication.bean.Milite;
import my.test.myapplication.utils.TimeUtil;

public class NewsAdapter extends BaseQuickAdapter<Milite.DataBean, BaseViewHolder> {

    private Activity context;
    public NewsAdapter(int layoutResId, Activity activity) {
        super(layoutResId);
        this.context=activity;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, final Milite.DataBean item) {
        helper.setText(R.id.content,item.getTitle());
        Glide.with(mContext).load(item.getImg())
                .into((ImageView) helper.getView(R.id.item_number));
        helper.setText(R.id.textView,item.getSource());
        helper.setText(R.id.textView2,item.getComment_num()+"评");
        try {
            helper.setText(R.id.textView3, TimeUtil.getTimeFormatText(TimeUtil.stringToDate(item.getPublish_time(), "yyyy-MM-dd HH:mm:ss")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url",item.getUrl());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("img",item.getImg());
                context.startActivity(intent);
            }
        });
    }
}
