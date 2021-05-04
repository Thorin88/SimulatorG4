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
public class PermanentStatusConditions {

  ArrayList<Flag> flags;

  public PermanentStatusConditions () {

    this.flags = new ArrayList<Flag>();

  }

  public ArrayList<Flag> getStatusList() {

    return this.flags;

  }

  public void initialise() {

    for (int i = 1; i < 6; i++) {

      this.flags.add(new Flag(i,-1));

    }

  }

  public Boolean isStatusAvailable() {

    int size = this.flags.size();

    for (int i = 0; i < size; i++) {

      if (this.flags.get(i).turnsRemaining >= 0) {

        return false;

      }

    }

    return true;

  }

  public void endOfTurnDecrements(int player) {

    // Each time poison is applied its increased, so actually no need for
    // this function.

  }

  public void resetPoison() {

    if (this.flags.get(4).turnsRemaining > 1) {

      this.flags.get(4).turnsRemaining = 100;

    }

  }

  public PermanentStatusConditions deepCopy() {

    PermanentStatusConditions copy = new PermanentStatusConditions();

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
