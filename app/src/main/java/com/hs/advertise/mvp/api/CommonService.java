
package com.hs.advertise.mvp.api;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * ================================================
 * 存放通用的一些 API
 * <p>
 * Created by ganxinrong on 08/05/2019 12:05
 * <p>
 * ================================================
 */
public interface CommonService {

    @Headers("Content-Type:application/json")
    @POST
    Observable<Object> commonOperation(@Url String url, @Body RequestBody requestBody);

/*    @Multipart
    Observable<String> updateAvatar(@Part("memberId") String memberId, @Part("file\"; filename=\"image.png") RequestBody imgs, @HeaderMap Map<String, Object> headerParams);


    @FormUrlEncoded
    @POST
    Observable<String> commonOperation(@Url String url, @FieldMap Map<String, Object> fields, @HeaderMap Map<String, Object> headerParams);

    @DELETE
    Observable<String> commonDeteleOperation(@Url String url, @QueryMap Map<String, Object> fields, @HeaderMap Map<String, Object> headerParams);

    @GET
    Observable<String> commonGETOperation(@Url String url, @QueryMap Map<String, Object> paramas, @HeaderMap Map<String, Object> headerParams);
    //get请求不要加@FormUrlEncoded

    @GET
    Observable<String> commonGETOperation(@Url String url, @HeaderMap Map<String, Object> headerParams);

    @Headers("Content-Type:application/json")
    @Multipart
    Observable<String> uploadFile(@Part List<MultipartBody.Part> partList, @Body RequestBody requestBody);*/


}
