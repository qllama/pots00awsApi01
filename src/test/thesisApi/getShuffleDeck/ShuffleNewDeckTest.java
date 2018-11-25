package thesisApi.getShuffleDeck;

import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


public class ShuffleNewDeckTest {


    private String deckId;
    private String pileName;


    private String shuffleDeck(String deckId, int deckCount){ //Can be new String "new" or int "deck_id"

        String deckCountString;
        if(deckCount > 0){
            deckCountString = "?deck_count=" + deckCount; }
            else {deckCountString = "";}

        String response = RestAssured.given()
                .when()
                .log().all()
                .get("https://deckofcardsapi.com/api/deck/" + deckId +"/shuffle/" + deckCountString)
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("remaining", equalTo(52*deckCount))
                .body("success", equalTo(true))
                .body("shuffled", equalTo(true))
                .extract().body().jsonPath().getString("deck_id");

        return response;
    }

    private String getNewDeck(){

        String response = RestAssured.given()
                .when()
                .log().all()
                .get("https://deckofcardsapi.com/api/deck/new/")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .body("remaining", equalTo(52))
                .body("success", equalTo(true))
                .body("shuffled", equalTo(false))
                .extract().body().jsonPath().getString("deck_id");

        return response;
    }

    @Test
    public void newDecksNotEquals(){
        String deckA = getNewDeck();
        String deckB = getNewDeck();
        Assert.assertNotEquals(deckA, deckB);
    }

    @Test
    public void shuffleNewDeckTest(){
        shuffleDeck("new", 2);
    }

    @Test
    public void shuffleExistingDeckTest(){
        shuffleDeck(getNewDeck(), 1);
    }

    @Test
    public void reshuffleExistingDeckTest(){
        String deckA = getNewDeck();
        shuffleDeck(deckA, 1);
        shuffleDeck(deckA, 1);
    }


}
