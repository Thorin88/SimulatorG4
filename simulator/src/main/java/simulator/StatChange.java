
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.Iterator;
// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class StatChange {

  String name;
  int level;
  // StatInfo info;

  public StatChange(String statName, int changeValue) {

    this.name = statName;
    this.level = changeValue;

  }

  public String getStatName() {

    return this.name;

  }

  public int getStatChangeLevel() {

    return this.level;

  }

  public StatChange deepCopy() {

    return new StatChange(this.name, this.level);

  }

  @Override
  public String toString() {

    return "[" + this.name + "," + this.level +"]";

  }

}
