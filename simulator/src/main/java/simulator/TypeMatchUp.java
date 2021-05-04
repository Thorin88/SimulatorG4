import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TypeMatchUp {

  ArrayList<PokemonType> double_damage_from;
  ArrayList<PokemonType> double_damage_to;
  ArrayList<PokemonType> half_damage_from;
  ArrayList<PokemonType> half_damage_to;
  ArrayList<PokemonType> no_damage_from;
  ArrayList<PokemonType> no_damage_to;

  public TypeMatchUp() {

  }

  public TypeMatchUp(ArrayList<PokemonType> ddf, ArrayList<PokemonType> ddt, ArrayList<PokemonType> hdf, ArrayList<PokemonType> hdt, ArrayList<PokemonType> ndf, ArrayList<PokemonType> ndt) {

    this.double_damage_from = ddf;
    this.double_damage_to = ddt;
    this.half_damage_from = hdf;
    this.half_damage_to = hdt;
    this.no_damage_from = ndf;
    this.no_damage_to = ndt;

  }

  @Override
  public String toString() {

    return "";

  }



}
