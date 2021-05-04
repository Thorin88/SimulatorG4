import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PokemonData {

  int id;
  String name;
  ArrayList<PokemonType> types;
  ArrayList<Form> forms;
  ArrayList<Move> moves;
  ArrayList<Ability> abilities;
  ArrayList<Stat> stats;
  // TODO Make getters for each stat.

  public ArrayList<Stat> getStatList() {

    return this.stats;

  }

  public ArrayList<PokemonType> getTypeList() {

    return this.types;

  }

  public ArrayList<Ability> getAbilityList() {

    return this.abilities;

  }

  public ArrayList<Move> getMoveList() {

    return this.moves;

  }

  public ArrayList<Form> getFormList() {

    return this.forms;

  }

  public int getID() {

    return this.id;

  }

  public String getName() {

    return this.name;

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    stringToReturn = stringToReturn + "ID: " + this.id + "\n"
                                    + "Name: " + this.name + "\n"
                                    + this.stats.toString() + "\n"
                                    + this.abilities.toString() + "\n"
                                    + this.moves.toString() + "\n"
                                    + this.forms.toString() + "\n";

    return stringToReturn;

  }



}
