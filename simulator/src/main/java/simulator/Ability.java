
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.Iterator;
// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Ability {

  String abilityName;
  int abilityID;
  // StatInfo info;

  public Ability(String name, int id) {

    this.abilityName = name;
    this.abilityID = id;

  }

  public String getAbilityName() {

    return this.abilityName;

  }

  public int getAbilityID() {

    return this.abilityID;

  }

  @Override
  public String toString() {

    return "[" + this.abilityName + "," + this.abilityID +"]";

  }

}
