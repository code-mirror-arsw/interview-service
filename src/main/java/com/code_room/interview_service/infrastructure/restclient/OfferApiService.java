package com.code_room.interview_service.infrastructure.restclient;


import com.code_room.interview_service.infrastructure.restclient.dto.LangageDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OfferApiService {

    @GET("offers/language")
    Call<LangageDto> getLanguage(@Header("Authorization") String authHeader, @Query("offerId") String id);



}
