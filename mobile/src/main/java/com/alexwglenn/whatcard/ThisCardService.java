package com.alexwglenn.whatcard;

import com.alexwglenn.whatcard.model.Card;
import com.alexwglenn.whatcard.model.AddCardResponse;
import com.alexwglenn.whatcard.model.CardsResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by aglenn on 1/13/16.
 */
public interface ThisCardService {

//    @GET("/users/{id}")
//    Observable<User>getUser(@Path("id") String userID);

    @POST("/cards")
    Observable<Response<AddCardResponse>>addCard(@Body Card card, @Header("Session-Key") String sessionKey);

    @GET("/users/{id}/cards")
    Observable<Response<Card[]>>getUserCards(@Path("id") String userID, @Header("Session-Key") String sessionKey);

    @POST("/users/{id}/cards")
    Observable<Response<AddCardResponse>>addUserCard(@Path("id") String userID, @Body String card_id, @Header("Session-Key") String sessionKey);

    @POST("/users/{id}/cards")
    Observable<Response<AddCardResponse>>addUserCard(@Path("id") String userID, @Body Card card, @Header("Session-Key") String sessionKey);
}
