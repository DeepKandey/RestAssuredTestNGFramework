package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.utils.DataLoader;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Item;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.applicationApi.ItemApi.*;
import static com.spotify.oauth2.api.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.api.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist APIs")
public class PlaylistTests extends BaseTest{

  @Story("Create Playlist")
  @Test(description = "should be able to create a playlist")
  public void shouldBeAbleToCreateAPlaylist() {
    Item requestItem = itemBuilder(generateName(), generateDescription(), false);

    Response response = post(requestItem);
    Item responseItem = response.as(Item.class);

    assertStatusCode(response.statusCode(), StatusCode.CODE_201.code);
    assertItemEqual(requestItem, responseItem);
  }

  @Link(name = "allure", type = "myLink")
  @TmsLink("12345")
  @Issue("1234567")
  @Description("should be able to get a Playlist")
  @Story("Get Playlist")
  @Test
  public void shouldBeAbleToGetAPlaylist() {
    Item requestItem = itemBuilder("My Spotify Playlist", "My playlist description", false);

    Response response = get(DataLoader.getInstance().getGetPlaylistId());
    Item responseItem = response.as(Item.class);

    assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
    assertItemEqual(responseItem, requestItem);
  }

  @Story("Update Playlist")
  @Test
  public void shouldBeAbleToUpdateAPlaylist() {
    Item requestItem = itemBuilder(generateName(), generateDescription(), false);

    Response response = update(requestItem, DataLoader.getInstance().getUpdatePlaylistId());

    assertStatusCode(response.statusCode(), 200);
  }

  @Story("Create Playlist")
  @Test
  public void shouldNotBeAbleToCreateAPlaylistWithoutName() {
    Item requestItem = Item.builder().description(generateDescription())._public(false).build();

    Response response = post(requestItem);
    ErrorRoot responseErrorRoot = response.as(ErrorRoot.class);

    assertStatusCode(response.statusCode(), StatusCode.CODE_400.code);
    assertError(responseErrorRoot, StatusCode.CODE_400);
  }

  @Story("Create Playlist")
  @Test
  public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken() {
    String expiredToken =
        "BQC6Jbyly5mFIcuuenDXMn8YsQpPkfk_e4bkmwjhU9fd6V0SM50M7it87Qb3iUyKjRLMqKgoRBjI8qntgyQ15QjJUQ0_i4SKn-u05enFmhhPl7qiaz7Jzwt9Z4qFmsmm7Pnwky36G1NMEv4bWbkJo-UOxHHiQ7fi9KiaA7N55g1dnbsQcTGGCAXtSBYACYKgiihK5ZjxaCTgFvNqcNI8BSXQ2yQahjGp_5BJQi0ZpcHA";

    Item requestItem = itemBuilder(generateName(), generateDescription(), false);

    Response response = post(requestItem, expiredToken);
    ErrorRoot responseErrorRoot = response.as(ErrorRoot.class);

    assertStatusCode(response.statusCode(), StatusCode.CODE_401.code);
    assertError(responseErrorRoot, StatusCode.CODE_401);
  }

  @Step
  public Item itemBuilder(String name, String description, boolean _public) {
    return Item.builder().name(name).description(description)._public(_public).build();
  }

  @Step
  public void assertItemEqual(Item requestItem, Item responseItem) {
    assertThat(responseItem.getName(), equalTo(requestItem.getName()));
    assertThat(responseItem.getDescription(), equalTo(requestItem.getDescription()));
    assertThat(responseItem.get_public(), equalTo(requestItem.get_public()));
  }

  @Step
  public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
    assertThat(actualStatusCode, equalTo(expectedStatusCode));
  }

  @Step
  public void assertError(ErrorRoot responseErrorRoot, StatusCode statusCode) {
    assertThat(responseErrorRoot.getError().getMessage(), equalTo(statusCode.message));
    assertThat(responseErrorRoot.getError().getStatus(), equalTo(statusCode.code));
  }
}
