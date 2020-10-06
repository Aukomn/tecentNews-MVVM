package my.test.myapplication.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.test.myapplication.R;
import my.test.myapplication.adapter.VideoPlayerAdapter;
import my.test.myapplication.bean.DailyVideos;
import my.test.myapplication.utils.ScrollCalculatorHelper;
import my.test.myapplication.viewmodel.NewViewModel;

/**
 * A fragment representing a list of Items.
 */
public class VideoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private NewViewModel newViewModel;
    private VideoPlayerAdapter playerAdapter;
    private static String VIDEO_URL = "http://baobab.kaiyanapp.com/api/v2/feed";
    private boolean isfirst=true;
    SmartRefreshLayout refreshLayout;
    private String nexturl;
    private ScrollCalculatorHelper scrollCalculatorHelper;
    private LinearLayoutManager linearLayoutManager;
    private Boolean mFull=false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static VideoFragment newInstance(int columnCount) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newViewModel= ViewModelProviders.of(this).get(NewViewModel.class);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        playerAdapter=new VideoPlayerAdapter(R.layout.item_video,getActivity());
        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            refreshLayout=view.findViewById(R.id.refreshv);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listv);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(playerAdapter);
        if(isfirst){
            refreshLayout.autoRefresh();
    //        isfirst=false;
        }
        linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                GSYVideoManager.onPause();
                newViewModel.getVideos(VIDEO_URL).observe(getViewLifecycleOwner(), new Observer<DailyVideos>() {
                    @Override
                    public void onChanged(DailyVideos dailyVideos) {
                        if(dailyVideos!=null) {
                            dailyVideos.getIssueList().get(0).getItemList().remove(0);
                            playerAdapter.setNewData(dailyVideos.getIssueList().get(0).getItemList());
                            playerAdapter.notifyDataSetChanged();
                            nexturl=dailyVideos.getNextPageUrl();
                        }
                    }
                });
                refreshLayout.finishRefresh();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                GSYVideoManager.onPause();
                if(nexturl==null)return;
                newViewModel.getVideos(nexturl).observe(getViewLifecycleOwner(), new Observer<DailyVideos>() {
                    @Override
                    public void onChanged(DailyVideos DailyVideos) {
                        if(DailyVideos!=null&&DailyVideos.getIssueList().get(0).getItemList().size()>0) {
                            playerAdapter.addData(DailyVideos.getIssueList().get(0).getItemList());
                            playerAdapter.notifyDataSetChanged();
                            nexturl=DailyVideos.getNextPageUrl();
                        }
                        else refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                });
                refreshLayout.finishLoadMore();
            }
        });
        // 限定范围为屏幕一半的上下偏移180
        int playTop = CommonUtil.getScreenHeight(getContext()) / 2
                - CommonUtil.dip2px(getContext(), 180);
        int playBottom = CommonUtil.getScreenHeight(getContext()) / 2
                + CommonUtil.dip2px(getContext(), 180);
        scrollCalculatorHelper=new ScrollCalculatorHelper(R.id.video_player_item,playTop,playBottom);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(
                    @NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
              //  Log.d("--",newState+"");
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager != null) {
                    firstVisibleItem =
                            linearLayoutManager.findFirstVisibleItemPosition();
                    lastVisibleItem =
                            linearLayoutManager.findLastVisibleItemPosition();

                //  Log.d("--",firstVisibleItem+"");
                }
                // 这是滑动自动播放的代码
                if (dx>0|dy>0)
                {
                    scrollCalculatorHelper.onScroll(recyclerView,
                            firstVisibleItem,
                            lastVisibleItem,
                            lastVisibleItem - firstVisibleItem);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}