import java.util.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

// For simple JSON extractions
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

// To allow for seperate methods to be used on the two types of status lists.
public class TemporaryStatusConditions {

  ArrayList<Flag> flags;

  public TemporaryStatusConditions () {

    this.flags = new ArrayList<Flag>();

  }

  public ArrayList<Flag> getStatusList() {

    return this.flags;

  }

  public void initialise() {

    for (int i = 6; i < 10; i++) {

      this.flags.add(new Flag(i,-1));

    }

    this.flags.add(new Flag(14,-1));

    for (int i = 17; i < 19; i++) {

      this.flags.add(new Flag(i,-1));

    }

    // extra status not referenced by IDs from API

    // Flinch - Needs to be removed every turn from both pman
    this.flags.add(new Flag(19,-1));

  }

  // public void endOfTurnDecrements(int player) {
  //
  //   // Confusion - No decrement - Only decreased by succesful attacks
  //
  //   // Infatuation - No decrement
  //
  //   // Trap - Decrement by 1.
  //
  //   if (this.flags.get(2).turnsRemaining == 0) {
  //
  //     // Could have a weather.endingMessage() function
  //     System.out.println("Weather faded.");
  //     currentWeather.flagID = -1;
  //
  //   }
  //
  //   if (currentWeather.turnsRemaining != -1) {
  //
  //     currentWeather.turnsRemaining -= 1;
  //
  //   }
  //
  //
  // }

  public void resetStatus() {

    int size = this.flags.size();

    for (int i = 0; i < size; i++) {

      this.flags.get(i).turnsRemaining = -1;

    }

  }

  public TemporaryStatusConditions deepCopy() {

    TemporaryStatusConditions copy = new TemporaryStatusConditions();

    int size = this.flags.size();

    for (int i = 0; i < size; i++) {

      copy.flags.add( this.flags.get(i).deepCopy() );

    }

    return copy;

  }

  @Override
  public String toString() {

    String stringToReturn = "";
    int size = this.flags.size();

    for (int i = 0; i < size; i++) {

      stringToReturn += this.flags.get(i).toString() + " ";

    }

    return stringToReturn;

  }

}
