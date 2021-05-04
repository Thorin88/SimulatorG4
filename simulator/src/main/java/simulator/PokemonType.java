
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

public class PokemonType {

  String typeName;
  int typeID;

  public PokemonType(String name, int typeID) {

    this.typeName = name;
    this.typeID = typeID;

  }

  public String getTypeName() {

    return this.typeName;

  }

  public int getTypeID() {

    return this.typeID;

  }

  @Override
  public String toString() {

    return "[" + this.typeName + "," + this.typeID +"]";

  }



}
