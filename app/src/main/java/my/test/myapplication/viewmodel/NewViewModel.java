package my.test.myapplication.viewmodel;

import android.util.Log;

import java.util.HashMap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import my.test.myapplication.bean.DailyVideos;
import my.test.myapplication.bean.Milite;
import my.test.myapplication.http.LoadUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, String>> params = new MutableLiveData<>();

    public MutableLiveData<HashMap<String, String>> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> param) {
        params.setValue(param);
    }

    public MutableLiveData<Milite>getNews(HashMap<String,String>params, int page){
        final MutableLiveData<Milite>data=new MutableLiveData<>();
        LoadUtil.getService().getMilite(params,page)
                .enqueue(new Callback<Milite>() {
                    @Override
                    public void onResponse(Call<Milite> call, Response<Milite> response) {
                        if(response.isSuccessful())
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Milite> call, Throwable t) {
                        data.setValue(null);
                    }
                });
          return data;
    }
    public MutableLiveData<DailyVideos>getVideos(String url){
        final MutableLiveData<DailyVideos>data=new MutableLiveData<>();
        LoadUtil.getService().getVideos(url)
                .enqueue(new Callback<DailyVideos>() {
                    @Override
                    public void onResponse(Call<DailyVideos> call, Response<DailyVideos> response) {
                        if (response.isSuccessful()) {
                            data.setValue(response.body());
                            Log.d("--------","!!!!");
                        }
                        else  Log.e("-------",1111111111+"");
                    }
                    @Override
                    public void onFailure(Call<DailyVideos> call, Throwable t) {
                            Log.e("-------",t.getMessage());
                    }
                });
        return data;
    }
}
