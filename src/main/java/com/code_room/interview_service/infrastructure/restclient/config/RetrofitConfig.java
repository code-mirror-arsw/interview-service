package com.code_room.interview_service.infrastructure.restclient.config;


import com.code_room.interview_service.infrastructure.restclient.OfferApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {

  @Value("${spring.application.restclient.offer.url}")
  private String OfferUrl;


  private static final long TIMEOUT_SECONDS = 60;

  @Bean
  @Qualifier("offerRetrofit")
  public Retrofit offerRetrofit() {
    return new Retrofit.Builder()
            .baseUrl(OfferUrl)
            .client(new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build())
            .addConverterFactory(JacksonConverterFactory.create(
                    new ObjectMapper()
                            .findAndRegisterModules()
            ))

            .build();
  }

  @Bean
  public static OfferApiService getUserApiService(@Qualifier("offerRetrofit")Retrofit offerRetrofit) {
    return offerRetrofit.create(OfferApiService.class);
  }

}
