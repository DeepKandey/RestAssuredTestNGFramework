package com.spotify.oauth2.api;

import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

  public static Response post(Object requestItem, String accessToken, String path) {
    return given(getRequestSpec())
        .body(requestItem)
        .auth()
        .oauth2(accessToken)
        .when()
        .post(path)
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static Response postAccount(HashMap<String, String> formParams) {
    return given(getAccountRequestSpec())
        .formParams(formParams)
        .when()
        .post(Route.API + Route.TOKEN)
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static Response get(String accessToken, String path) {
    return given(getRequestSpec())
        .auth()
        .oauth2(accessToken)
        .when()
        .get(path)
        .then()
        .spec(getResponseSpec())
        .extract()
        .response();
  }

  public static Response update(Object requestItem, String accessToken, String path) {
    return given(getRequestSpec())
        .body(requestItem)
        .auth()
        .oauth2(accessToken)
        .when()
        .put(path)
        .then()
        .extract()
        .response();
  }
}
