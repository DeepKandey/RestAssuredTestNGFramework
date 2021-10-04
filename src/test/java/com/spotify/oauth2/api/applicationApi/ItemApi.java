package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.api.Route;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.api.utils.ConfigLoader;
import com.spotify.oauth2.pojo.Item;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class ItemApi {

  @Step
  public static Response post(Item requestItem) {
    return RestResource.post(
        requestItem,
        TokenManager.getToken(),
        Route.USERS + "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS);
  }

  @Step
  public static Response post(Item requestItem, String token) {
    return RestResource.post(
        requestItem,
        token,
        Route.USERS + "/" + ConfigLoader.getInstance().getUserId() + Route.PLAYLISTS);
  }

  @Step
  public static Response get(String playlistId) {
    return RestResource.get(TokenManager.getToken(), Route.PLAYLISTS + "/" + playlistId);
  }

  @Step
  public static Response update(Item requestItem, String playlistId) {
    return RestResource.update(
        requestItem, TokenManager.getToken(), Route.PLAYLISTS + "/" + playlistId);
  }
}
