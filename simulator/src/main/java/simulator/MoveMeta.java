
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

// Could look for "charges for one turn" in effect text.

public class MoveMeta {

  int accuracy;
  String dmgType;
  String effect;
  String short_effect;
  int effectChance;

  int ailmentID;
  int ailment_chance;
  int moveCategoryID;
  int crit_rate;
  int drain;
  int flinch_chance;
  int healing;
  int min_hits;
  int max_hits;
  int min_turns;
  int max_turns;
  int chargeTurns;
  int rechargeTurns;
  int stat_chance;

  int power;
  int pp;
  int priority;

  ArrayList<StatChange> stat_changes;

  int targetTypeID;

  String typeName;
  int typeID;


  public MoveMeta(int acc, String dType, String eff, String sEff, int effC, int aID, int aChance, int moveCatID, int cr, int d, int fChance, int h, int minH, int maxH, int minT, int maxT, int chargeTs, int rechargeTs, int statChance, int pow, int powerPoints, int prior, ArrayList<StatChange> statsChanges, int targetTID, String tName, int tID) {

    this.accuracy = acc;
    this.dmgType = dType;
    this.effect = eff;
    this.short_effect = sEff;
    this.effectChance = effC;

    this.ailmentID = aID;
    this.ailment_chance = aChance;
    this.moveCategoryID = moveCatID;
    this.crit_rate = cr;
    this.drain = d;
    this.flinch_chance = fChance;
    this.healing = h;
    this.min_hits = minH;
    this.max_hits = maxH;
    this.min_turns = minT;
    this.max_turns = maxT;
    this.chargeTurns = chargeTs;
    this.rechargeTurns = rechargeTs;
    // this.immuneTurns;
    this.stat_chance = statChance;

    this.power = pow;
    this.pp = powerPoints;
    this.priority = prior;

    this.stat_changes = statsChanges;

    this.targetTypeID = targetTID;

    this.typeName = tName;
    this.typeID = tID;

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    stringToReturn = stringToReturn + this.typeName + "\t"
                                    + this.power + "\t"
                                    + this.accuracy + "\t"
                                    + this.chargeTurns + "\t"
                                    + this.rechargeTurns + "\t"
                                    + "\n" + this.effect + "\n";

    return stringToReturn;

  }

  public String toStringFull() {

    return null;

  }

}
