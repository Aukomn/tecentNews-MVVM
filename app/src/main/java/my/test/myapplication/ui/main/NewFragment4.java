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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.test.myapplication.R;
import my.test.myapplication.adapter.NewsAdapter;
import my.test.myapplication.bean.Milite;
import my.test.myapplication.bean.SerMap;
import my.test.myapplication.constant.BaseConstant;
import my.test.myapplication.viewmodel.NewViewModel;

/**
 * A fragment representing a list of Items.
 */
public class NewFragment4 extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static ArraySet<HashMap<String, String>> list;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private HashMap<String, String> params=new HashMap<>();
    private NewViewModel newViewModel;
    private List<Milite.DataBean> data=new ArrayList<>();
    private NewsAdapter newsAdapter;
    private SmartRefreshLayout refreshLayout;
    private int page=0;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewFragment4() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewFragment4 newInstance(int columnCount, HashMap<String, String> params) {
        NewFragment4 fragment = new NewFragment4();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        SerMap serMap=new SerMap();
        serMap.setMap(params);
        args.putSerializable("map",serMap);
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
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        // Set the adapter
        newsAdapter=new NewsAdapter(R.layout.item,getActivity());
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            refreshLayout=view.findViewById(R.id.refresh);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(newsAdapter);
        final SerMap serMap1= (SerMap) getArguments().getSerializable("map");
        newViewModel.setParams(serMap1.getMap());
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    initRefresh(serMap1);
                    refreshLayout.finishRefresh();
                }
            });

          initRefresh(serMap1);
          refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
              @Override
              public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                  page++;
                          newViewModel.getNews(BaseConstant.getMiliteParams(),page).observe(getViewLifecycleOwner(), new Observer<Milite>() {
                              @Override
                              public void onChanged(Milite milite) {
                                  newsAdapter.addData(milite.getData());
                                  newsAdapter.notifyDataSetChanged();
                                  refreshLayout.finishLoadMore();
                              }
                          });
              }
          });
        return view;
    }
    private void initRefresh(SerMap serMap){
        page=0;
              newViewModel.getNews(BaseConstant.getMiliteParams(),page).observe(getViewLifecycleOwner(), new Observer<Milite>() {
                  @Override
                  public void onChanged(Milite milite) {
                      if (milite != null) {
                          newsAdapter.setNewData(milite.getData());
                          newsAdapter.notifyDataSetChanged();
                          refreshLayout.finishRefresh();
                      }
                  }
      });
    }


}