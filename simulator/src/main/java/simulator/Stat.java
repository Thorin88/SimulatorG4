
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

public class Stat {

  String statName;
  int base_stat;
  // StatInfo info;
  // int stage;

  public Stat(String name, int statValue) {

    this.statName = name;
    this.base_stat = statValue;
    // this.stage = 0;

  }

  public String getStatName() {

    return this.statName;

  }

  public int getStatValue() {

    return this.base_stat;

  }

  public void setStatValue(int stat) {

    this.base_stat = stat;

  }

  public Stat deepCopy() {

    return new Stat(this.statName, this.base_stat);

  }

  @Override
  public String toString() {

    return "[" + this.statName + "," + this.base_stat + "]";

  }

}
