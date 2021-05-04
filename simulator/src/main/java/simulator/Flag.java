
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Flag {

  int ailmentID;
  int turnsRemaining;
  // StatInfo info;

  public Flag (int id, int turns) {

    this.ailmentID = id;
    this.turnsRemaining = turns;

  }

  public int getFlagID() {

    return this.ailmentID;

  }

  public int getTurnsRemaining() {

    return this.turnsRemaining;

  }

  public Flag deepCopy() {

    return new Flag(this.ailmentID, this.turnsRemaining);

  }

  @Override
  public String toString() {

    return "[" + this.ailmentID + "," + this.turnsRemaining +"]";

  }

}
