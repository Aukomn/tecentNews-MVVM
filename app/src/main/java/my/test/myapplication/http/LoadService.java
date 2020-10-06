package my.test.myapplication.http;

import java.util.HashMap;

import my.test.myapplication.bean.DailyVideos;
import my.test.myapplication.bean.Milite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LoadService {
    @GET("irs/rcd")
    Call<Milite> getMilite(@QueryMap HashMap<String, String> hashMap, @Query("page") int page);
    @GET
    Call<DailyVideos> getVideos(@Url String url);
}
