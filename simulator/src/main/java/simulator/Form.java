
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

public class Form {

  String formName;
  int formID;
  // FormURL; Can be added after this is created from GSON.

  public Form(String name, int spriteID) {

    this.formName = name;
    this.formID = spriteID;

  }

  public String getFormName() {

    return this.formName;

  }

  public int getFormID() {

    return this.formID;

  }

  @Override
  public String toString() {

    return "[" + this.formName + "," + this.formID +"]";

  }



}
