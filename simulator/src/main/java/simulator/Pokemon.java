import java.util.*;
import java.lang.Math;

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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.io.File;

import static utility.Constants.*;

public class Pokemon {

  int id;
  String name;
  int level;

  ArrayList<PokemonType> types; // Retreived in PokemonData
  // GSON sets it as 0 since it cannot map anything to it.
  int currentHealth;
  // TODO Types
  Form form;
  ArrayList<Move> moves;
  Ability ability;
  ArrayList<Stat> stats;
  // Atk, Def, spAtk, spDef, spd, acc, eva
  ArrayList<StatChange> statChangeLevels;
  // NEW
  ArrayList<Float> typeMatchUps;
  // TODO Make getters for each stat
  // TODO stat changes
  String filePathToBaseApi = "./../";

  PermanentStatusConditions statusUnaffectedBySwitching;
  TemporaryStatusConditions statusAffectedBySwitching;

  // TODO Make copy methods for all the objects to allow for a deepCopyPokemon() method to exist.

  public Pokemon() {

    // At the stage where data about the Pokedex ID has been gathered.

  }

  public Pokemon(int pokedexID, String pName, Form chosenForm, ArrayList<Move> moveSet, Ability chosenAbility, ArrayList<Stat> baseStats) {

    this.id = pokedexID;
    this.name = pName;
    this.form = chosenForm;
    this.moves = moveSet;
    this.ability = chosenAbility;
    this.stats = baseStats;

  }

  public void buildFromIndexes(int id, int formIndex, int[] moveIndexes, int abilityIndex) {

      PokemonData pData = pokedexIDToPokemonData(id);

      // Gathered data about the presented PokedexID.

      // System.out.println(pData.toString());

      Form formSelected = pData.getFormList().get(formIndex);
      Ability abilitySelected = pData.getAbilityList().get(abilityIndex);
      ArrayList<PokemonType> typeList = pData.getTypeList();
      int lengthOfTypeList = typeList.size();

      this.id = pData.getID();
      this.name = pData.getName();
      this.form = new Form(formSelected.getFormName(), formSelected.getFormID());
      this.ability = new Ability(abilitySelected.getAbilityName(), abilitySelected.getAbilityID());

      ArrayList<PokemonType> copiedTypeList = new ArrayList<PokemonType>();
      ArrayList<Move> moveSet = new ArrayList<Move>();
      ArrayList<Stat> baseStats = new ArrayList<Stat>();
      ArrayList<StatChange> freshStatModifierLevels = new ArrayList<StatChange>();
      int lengthOfAllValidMovesList = pData.getMoveList().size();

      for (int i = 0; i < lengthOfTypeList; i++) {

        copiedTypeList.add(new PokemonType(typeList.get(i).getTypeName(), typeList.get(i).getTypeID()));

      }

      this.types = copiedTypeList;

      for (int i = 0; i < 4; i++) {

        int indexRequested = moveIndexes[i];

        if (indexRequested < 0) {

          moveSet.add(null);

        }

        else if (indexRequested >= lengthOfAllValidMovesList ) {

          System.out.println("Move index out of range. (5)");
          moveSet.add(null);

        }

        else {

          Move tempMove = pData.getMoveList().get(indexRequested);
          // Setting meta data
          MoveMeta tempMeta = createMoveMetaData(tempMove.getMoveID());

          moveSet.add(new Move(tempMove.getMoveName(), tempMove.getMoveID(), tempMeta));

        }

      }

      this.moves = moveSet;

      for (int i = 0; i < 6; i++) {

        Stat tempStat = pData.getStatList().get(i);

        baseStats.add(new Stat(tempStat.getStatName(), tempStat.getStatValue()));

      }

      this.stats = baseStats;

      this.currentHealth = baseStats.get(0).getStatValue();

      this.statusUnaffectedBySwitching = new PermanentStatusConditions();
      this.statusUnaffectedBySwitching.initialise();

      this.statusAffectedBySwitching = new TemporaryStatusConditions();
      this.statusAffectedBySwitching.initialise();

      // freshStatModifierLevels.add(new StatChange("hp",0));
      freshStatModifierLevels.add(new StatChange("attack",0));
      freshStatModifierLevels.add(new StatChange("defense",0));
      freshStatModifierLevels.add(new StatChange("special-attack",0));
      freshStatModifierLevels.add(new StatChange("special-defense",0));
      freshStatModifierLevels.add(new StatChange("speed",0));
      freshStatModifierLevels.add(new StatChange("accuracy",0));
      freshStatModifierLevels.add(new StatChange("evasion",0));

      this.statChangeLevels = freshStatModifierLevels;

      // Set type match ups
      this.setTypeMatchUps();

  }

  public void buildUsingMoveNames(int id, int formIndex, String[] moveNames, int abilityIndex, int printMode) {

    PokemonData pData = pokedexIDToPokemonData(id);

    // Gathered data about the presented PokedexID.

    // System.out.println(pData.toString());

    Form formSelected = pData.getFormList().get(formIndex);
    Ability abilitySelected = pData.getAbilityList().get(abilityIndex);
    ArrayList<PokemonType> typeList = pData.getTypeList();
    int lengthOfTypeList = typeList.size();

    this.id = pData.getID();
    this.name = pData.getName();
    this.name = UtilityFunctions.toCaps(this.name);
    this.form = new Form(formSelected.getFormName(), formSelected.getFormID());
    this.ability = new Ability(abilitySelected.getAbilityName(), abilitySelected.getAbilityID());

    ArrayList<PokemonType> copiedTypeList = new ArrayList<PokemonType>();
    ArrayList<Move> moveSet = new ArrayList<Move>();
    ArrayList<Stat> baseStats = new ArrayList<Stat>();
    ArrayList<StatChange> freshStatModifierLevels = new ArrayList<StatChange>();
    int lengthOfAllValidMovesList = pData.getMoveList().size();

    for (int i = 0; i < lengthOfTypeList; i++) {

      copiedTypeList.add(new PokemonType(typeList.get(i).getTypeName(), typeList.get(i).getTypeID()));

    }

    this.types = copiedTypeList;

    String moveRequested = "";
    ArrayList<Move> pmanMovePool = pData.getMoveList();
    int poolSize = pmanMovePool.size();
    Move tempMove = null;

    for (int i = 0; i < 4; i++) {

      moveRequested = moveNames[i];

      tempMove = null;

      for (int j = 0; j < poolSize; j++) {

        if (pmanMovePool.get(j).getMoveName().equals(moveRequested)) {

          tempMove = pmanMovePool.get(j);
          break;

        }

      }

      if (tempMove == null) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Move not available");
        }
        moveSet.add(null);

      }

      else {

        // Setting meta data
        MoveMeta tempMeta = createMoveMetaData(tempMove.getMoveID());
        moveSet.add(new Move(tempMove.getMoveName(), tempMove.getMoveID(), tempMeta));

      }

    }

    this.moves = moveSet;

    if (printMode == PRINTING_ENABLED) {
      System.out.println(moveSet.toString());
    }

    for (int i = 0; i < 6; i++) {

      Stat tempStat = pData.getStatList().get(i);

      baseStats.add(new Stat(tempStat.getStatName(), tempStat.getStatValue()));

    }

    this.stats = baseStats;

    this.currentHealth = baseStats.get(0).getStatValue();

    this.statusUnaffectedBySwitching = new PermanentStatusConditions();
    this.statusUnaffectedBySwitching.initialise();

    this.statusAffectedBySwitching = new TemporaryStatusConditions();
    this.statusAffectedBySwitching.initialise();

    // freshStatModifierLevels.add(new StatChange("hp",0));
    freshStatModifierLevels.add(new StatChange("attack",0));
    freshStatModifierLevels.add(new StatChange("defense",0));
    freshStatModifierLevels.add(new StatChange("special-attack",0));
    freshStatModifierLevels.add(new StatChange("special-defense",0));
    freshStatModifierLevels.add(new StatChange("speed",0));
    freshStatModifierLevels.add(new StatChange("accuracy",0));
    freshStatModifierLevels.add(new StatChange("evasion",0));

    this.statChangeLevels = freshStatModifierLevels;

    // Set type match ups
    this.setTypeMatchUps();

  }

  // TODO - Build from IDs, maybe also names.
  public Pokemon buildFromIDs() {

    return null;

  }

  // TODO - Asks the user to select stuff.
  public Pokemon buildFancy() {

    return null;

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    stringToReturn = stringToReturn + "--------------------------------------\n"
                                    + "PokedexID: " + this.id + "\n"
                                    + "Name: " + this.name + "\n"
                                    + "Level: " + this.level + "\n"
                                    + "--------------------------------------\n"
                                    + "Form: " + this.form.getFormName() + "\n"
                                    + "FormID: " + this.form.getFormID() + "\n"
                                    + "--------------------------------------\n"
                                    + "Type 1: " + this.types.get(0).getTypeName() + "\n";

    if (this.types.size() == 2) {

      stringToReturn += "Type 2: " + this.types.get(1).getTypeName() + "\n";

    }

    stringToReturn += "--------------------------------------\n";


    for (int i = 0; i < 4; i++) {

      if (this.moves.get(i) == null) {

        stringToReturn += "Move " + (i + 1) + ": ---\n";

      }

      else {

        stringToReturn += "Move " + (i + 1) + ": " + this.moves.get(i).getMoveName() + " (ID: " + this.moves.get(i).getMoveID() + ")\n"
                       + this.moves.get(i).getMoveMetaData().toString();

      }

    }

    stringToReturn = stringToReturn + "--------------------------------------\n"
                                    + "Ability: " + this.ability.getAbilityName() + "\n"
                                    + "AbilityID: " + this.ability.getAbilityID() + "\n"
                                    + "--------------------------------------\n";

    stringToReturn += "Current HP: " + this.currentHealth + "\n"
                    + "--------------------------------------\n";

    for (int i = 0; i < 6; i++) {

      stringToReturn += this.stats.get(i).getStatName() + ": " + this.stats.get(i).getStatValue() + "\n";

    }

    stringToReturn += "--------------------------------------\n";

    return stringToReturn;

  }

  // Base stats are not added too if the function fails.
  public void levelUpPokemon(int level, int[] ivs, int[] evs) {
    // 510 max total, 255 max per stat
    if (sumOfArray(evs) > 510) {

      System.out.println("EV spread exceeds 510.");

      return;

    }

    else if (containsLargerThanX(evs,252)) {

      System.out.println("An EV is greater than 252.");

      return;

    }

    else if (containsLargerThanX(ivs,31)) {

      System.out.println("An IV is greater than 31.");

      return;

    }

    else if (level > 100 || level < 1) {

      System.out.println("Invalid level. Level set to 100.");

      level = 100;

    }

    // Level now certain.

    this.level = level;

    this.stats.get(0).setStatValue(calculateHPStat(level, this.stats.get(0).getStatValue(), ivs[0], evs[0]));
    this.currentHealth = this.stats.get(0).getStatValue();

    for (int i = 1; i < 6; i++) {

      this.stats.get(i).setStatValue(calculateNonHPStat(level, this.stats.get(i).getStatValue(), ivs[i], evs[i]));

    }


  }

  public int calculateHPStat(int level, int base, int iv, int ev) {

    int evOver4 = (int) Math.round(Math.floor(ev/4));

    int numerator = (2 * base + iv + evOver4) * level;

    int fraction = (int) Math.round(Math.floor(numerator/100));

    int stat = fraction + level + 10;

    // System.out.println(stat);

    return stat;

  }

  public int calculateNonHPStat(int level, int base, int iv, int ev) {

    int evOver4 = (int) Math.round(Math.floor(ev/4));

    int numerator = (2 * base + iv + evOver4) * level;

    int fraction = ((int) Math.round(Math.floor(numerator/100))) + 5;

    int stat = (int) Math.round(Math.floor(fraction * 1));

    // System.out.println(stat);

    return stat;

  }

  public int sumOfArray(int[] array) {

    int sum = 0;

    for (int i = 0; i < array.length; i++) { sum += array[i]; }

    return sum;

  }

  public Boolean containsLargerThanX(int[] array, int x) {

    for (int i = 0; i < array.length; i++) {

      if (array[i] > x) {

        return true;

      }

    }

    return false;

  }

  public MoveMeta createMoveMetaData(int id) {

    BufferedReader bufferedReader = null;

    // Gson gson = new Gson();

    // String stringToFile = "test.json";
    String stringToFile = filePathToBaseApi + "api-data/data/api/v2/move/" + id + "/index.json";

    try {

      bufferedReader = new BufferedReader(new FileReader(stringToFile));

    } catch (Exception e) {

      System.out.println("Error opening buffered reader (6)");
      e.printStackTrace();

    }

      GsonBuilder gsonBuilder = new GsonBuilder();


      JsonDeserializer<StatChange> deserializerForStatChange = new JsonDeserializer<StatChange>() {

        @Override
        public StatChange deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

          JsonObject jobject = json.getAsJsonObject();

          JsonObject innerStatNameObj = jobject.getAsJsonObject("stat");

          return new StatChange(
            innerStatNameObj.get("name").getAsString(),
            jobject.get("change").getAsInt());

        }

      };

      JsonDeserializer<MoveMeta> deserializerForMoveMeta = new JsonDeserializer<MoveMeta>() {

        @Override
        public MoveMeta deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

          ArrayList<StatChange> listToAdd = new ArrayList<StatChange>();

          JsonObject jobject = json.getAsJsonObject();

          JsonArray arrayOfStatChanges = jobject.getAsJsonArray("stat_changes");


          int accuracy;

          if (jobject.get("accuracy").isJsonNull()) {
            // Needs to remain 0 vs 100 since it bypasses the accuracy check.
            accuracy = 0;
          }
          else {
            accuracy = jobject.get("accuracy").getAsInt();
          }

          String dmgType = jobject.getAsJsonObject("damage_class").get("name").getAsString();

          // Could replace "$...%" with effect chance

          int effectChance;

          if (jobject.get("effect_chance").isJsonNull()) {
            effectChance = 0;
          }
          else {
            effectChance = jobject.get("effect_chance").getAsInt();
          }

          String effect = jobject.getAsJsonArray("effect_entries").get(0).getAsJsonObject().get("effect").getAsString();

          effect = effect.replace("$effect_chance%",effectChance + "%");

          String shortEffect = jobject.getAsJsonArray("effect_entries").get(0).getAsJsonObject().get("short_effect").getAsString();

          shortEffect = shortEffect.replace("$effect_chance%",effectChance + "%");

          int chargeTurns;
          int rechargeTurns;

          if (effect.contains("charges for one turn") || effect.contains("hits on the second turn")) { chargeTurns = 1; }
          else { chargeTurns = 0; }

          if (effect.contains("\"recharge\"")) { rechargeTurns = 1; }
          else { rechargeTurns = 0; }

          JsonObject meta = jobject.getAsJsonObject("meta");

          int ailmentID = UtilityFunctions.urlToID(meta.getAsJsonObject("ailment").get("url").getAsString());

          int ailmentChance = meta.get("ailment_chance").getAsInt();

          int moveCatID = UtilityFunctions.urlToID(meta.getAsJsonObject("category").get("url").getAsString());

          int minHits;
          int maxHits;
          int minTurns;
          int maxTurns;

          if (meta.get("min_hits").isJsonNull()) { minHits = 1; }
          else { minHits = meta.get("min_hits").getAsInt(); }
          if (meta.get("max_hits").isJsonNull()) { maxHits = 1; }
          else { maxHits = meta.get("max_hits").getAsInt(); }
          if (meta.get("min_turns").isJsonNull()) { minTurns = -1; }
          else { minTurns = meta.get("min_turns").getAsInt(); }
          if (meta.get("max_turns").isJsonNull()) { maxTurns = -1; }
          else { maxTurns = meta.get("max_turns").getAsInt(); }

          int power;

          if (jobject.get("power").isJsonNull()) {
            power = 0;
          }
          else {
            power = jobject.get("power").getAsInt();
          }

          int sizeOfArray = arrayOfStatChanges.size();

          for (int i = 0; i < sizeOfArray; i++) {

            StatChange temp = context.deserialize(arrayOfStatChanges.get(i), StatChange.class);

            listToAdd.add(temp);

          }

          int targetTypeID = UtilityFunctions.urlToID(jobject.getAsJsonObject("target").get("url").getAsString());

          String typeName = jobject.getAsJsonObject("type").get("name").getAsString();
          int typeID = UtilityFunctions.urlToID(jobject.getAsJsonObject("type").get("url").getAsString());

          return new MoveMeta(accuracy,
                              dmgType,
                              effect,
                              shortEffect,
                              effectChance,
                              ailmentID,
                              ailmentChance,
                              moveCatID,
                              meta.get("crit_rate").getAsInt(),
                              meta.get("drain").getAsInt(),
                              meta.get("flinch_chance").getAsInt(),
                              meta.get("healing").getAsInt(),
                              minHits,
                              maxHits,
                              minTurns,
                              maxTurns,
                              chargeTurns,
                              rechargeTurns,
                              meta.get("stat_chance").getAsInt(),
                              power,
                              jobject.get("pp").getAsInt(),
                              jobject.get("priority").getAsInt(),
                              listToAdd,
                              targetTypeID,
                              typeName,
                              typeID);

        }

      };

      gsonBuilder.registerTypeAdapter(StatChange.class, deserializerForStatChange);
      gsonBuilder.registerTypeAdapter(MoveMeta.class, deserializerForMoveMeta);


      Gson gson = gsonBuilder.create();

      MoveMeta move = gson.fromJson(bufferedReader, MoveMeta.class);

    return move;

  }

  public PokemonData pokedexIDToPokemonData(int id) {

    BufferedReader bufferedReader = null;

    // Gson gson = new Gson();

    // String stringToFile = "test.json";
    String stringToFile = filePathToBaseApi + "api-data/data/api/v2/pokemon/" + id + "/index.json";

    try {

      bufferedReader = new BufferedReader(new FileReader(stringToFile));

    } catch (Exception e) {

      System.out.println("Error opening buffered reader (3)");
      e.printStackTrace();

    }

    // Custom instance of gson
    GsonBuilder gsonBuilder = new GsonBuilder();

    // Creating a new deserializer for <Stat>
    JsonDeserializer<Stat> deserializerForStat = new JsonDeserializer<Stat>() {

      @Override
      public Stat deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        JsonObject innerStatNameObj = jobject.getAsJsonObject("stat");

        // context.deserialize(jobject, Ability.class);

        return new Stat(
          innerStatNameObj.get("name").getAsString(),
          jobject.get("base_stat").getAsInt());

      }

    };

    JsonDeserializer<Ability> deserializerForAbility = new JsonDeserializer<Ability>() {

      @Override
      public Ability deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        JsonObject innerStatNameObj = jobject.getAsJsonObject("ability");

        String url = innerStatNameObj.get("url").getAsString();

        return new Ability(
          innerStatNameObj.get("name").getAsString(),
          UtilityFunctions.urlToID(url));

      }

    };

    JsonDeserializer<Move> deserializerForMove = new JsonDeserializer<Move>() {

      @Override
      public Move deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        JsonObject innerStatNameObj = jobject.getAsJsonObject("move");

        String url = innerStatNameObj.get("url").getAsString();

        // Inserting a null value if the move is not allowed, this is removed
        // at the end of this function.
        if (isMoveNameAllowed(innerStatNameObj.get("name").getAsString())) {

        return new Move(
          innerStatNameObj.get("name").getAsString(),
          UtilityFunctions.urlToID(url));

        }

        else { return null; }

      }

    };

    JsonDeserializer<Form> deserializerForForm = new JsonDeserializer<Form>() {

      @Override
      public Form deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        String url = jobject.get("url").getAsString();

        return new Form(
          jobject.get("name").getAsString(),
          UtilityFunctions.urlToID(url));

      }

    };

    JsonDeserializer<PokemonType> deserializerForType = new JsonDeserializer<PokemonType>() {

      @Override
      public PokemonType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        JsonObject innerStatNameObj = jobject.getAsJsonObject("type");

        String url = innerStatNameObj.get("url").getAsString();

        return new PokemonType(
          innerStatNameObj.get("name").getAsString(),
          UtilityFunctions.urlToID(url));

      }

    };

    // First arguement = class this will be used for, second is the actual deserializer.
    gsonBuilder.registerTypeAdapter(Stat.class, deserializerForStat);
    gsonBuilder.registerTypeAdapter(Ability.class, deserializerForAbility);
    gsonBuilder.registerTypeAdapter(Move.class, deserializerForMove);
    gsonBuilder.registerTypeAdapter(Form.class, deserializerForForm);
    gsonBuilder.registerTypeAdapter(PokemonType.class, deserializerForType);

    Gson gson = gsonBuilder.create();

    PokemonData pman = gson.fromJson(bufferedReader, PokemonData.class);

    // https://stackoverflow.com/questions/4819635/how-to-remove-all-null-elements-from-a-arraylist-or-string-array
    // Nice way to get rid of nulls from the GSON deserializer, so that moves can
    // be removed before they are choosen from to form the moveset.

    pman.getMoveList().removeAll(Collections.singleton(null));

    return pman;

  }


  public TypeMatchUp getTypeMatchUpData(int typeID) {

    BufferedReader bufferedReader = null;

    String stringToFile = filePathToBaseApi + "api-data/data/api/v2/type/" + typeID + "/index.json";

    try {

      bufferedReader = new BufferedReader(new FileReader(stringToFile));

    } catch (Exception e) {

      System.out.println("Error opening buffered reader.");
      e.printStackTrace();

    }

    GsonBuilder gsonBuilder = new GsonBuilder();

    JsonDeserializer<TypeMatchUp> deserializerForTypeMatchUp = new JsonDeserializer<TypeMatchUp>() {

      @Override
      public TypeMatchUp deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = json.getAsJsonObject();

        JsonObject innerObj = jobject.getAsJsonObject("damage_relations");

        Type pokemonTypeListType = new TypeToken<ArrayList<PokemonType>>(){}.getType();

        // ArrayList<PokemonType> temp = context.deserialize(innerObj.getAsJsonArray("double_damage_from"), pokemonTypeListType);

        context.deserialize(innerObj.getAsJsonArray("double_damage_from"), pokemonTypeListType);


        return new TypeMatchUp(
          context.deserialize(innerObj.getAsJsonArray("double_damage_from"), pokemonTypeListType),
          context.deserialize(innerObj.getAsJsonArray("double_damage_to"), pokemonTypeListType),
          context.deserialize(innerObj.getAsJsonArray("half_damage_from"), pokemonTypeListType),
          context.deserialize(innerObj.getAsJsonArray("half_damage_to"), pokemonTypeListType),
          context.deserialize(innerObj.getAsJsonArray("no_damage_from"), pokemonTypeListType),
          context.deserialize(innerObj.getAsJsonArray("no_damage_to"), pokemonTypeListType));



      }



    };

    JsonDeserializer<PokemonType> deserializerForType = new JsonDeserializer<PokemonType>() {

      @Override
      public PokemonType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jObject = json.getAsJsonObject();

        // JsonObject inner = jObject.getAsJsonObject("damage_relations");

        String url =jObject.get("url").getAsString();

        return new PokemonType(
          jObject.get("name").getAsString(),
          UtilityFunctions.urlToID(url));

      }

    };



    gsonBuilder.registerTypeAdapter(PokemonType.class, deserializerForType);
    gsonBuilder.registerTypeAdapter(TypeMatchUp.class, deserializerForTypeMatchUp);


    Gson gson = gsonBuilder.create();

    TypeMatchUp data = gson.fromJson(bufferedReader, TypeMatchUp.class);

    return data;

  }

  public static float multiplyer(Move movedUsed, Pokemon target) {

    MoveMeta moveUsedData = movedUsed.getMoveMetaData();

    int typeIDOfMove = moveUsedData.typeID;

    return target.typeMatchUps.get(typeIDOfMove - 1);

  }

  public static float multiplyer(int typeID, Pokemon target) {

    return target.typeMatchUps.get(typeID - 1);

  }

  // Use index.json in /move
  public String getAllMovesAndIDs() {

    String stringToReturn = "";
    String stringToFile = "";
    FileReader reader;
    Object obj = null;
    JSONObject jObject;


    JSONParser parser = new JSONParser();

    for (int i = 1; i < 729; i++) {

      stringToFile = filePathToBaseApi + "api-data/data/api/v2/move/" + i + "/index.json";

      try {
        reader = new FileReader(stringToFile);
        obj = parser.parse(reader);
      } catch (Exception e) { e.printStackTrace(); }

      jObject = (JSONObject) obj;

      if (!isMoveNameAllowed((String) jObject.get("name"))) {

        continue;

      }

      else {

        stringToReturn += i + ": ";



        stringToReturn += (String) jObject.get("name") + "\n";

        stringToReturn += createMoveMetaData(i).effect + "\n";

      }

    }

    return stringToReturn;

  }

  // TODO: Check HP is not used and it gets to the end of the list
  public void addToStatChangeLevels(GameState currentGS, ArrayList<StatChange> newChanges, ArrayList<String> turnInfo) {

    int sizeOfChangeList = newChanges.size();
    String tempStatName = "";

    Field targetField = currentGS.getTargetField(currentGS, this);

    for (int i = 0; i < sizeOfChangeList; i++) {

      // Mist check
      if (targetField.activeFieldEffects.get(0).turnsRemaining > -1 && newChanges.get(i).getStatChangeLevel() < 0) {

        // System.out.println("Stat lower prevented by mist.");
        if (turnInfo != null) {
          turnInfo.add(this.name + "'s " + newChanges.get(i).getStatName() + " did not lower due to the mist.");
        }
        continue;

      }

      tempStatName = newChanges.get(i).getStatName();

      for (int j = 0; j < 7; j++) {

        if (this.statChangeLevels.get(j).getStatName().equals(tempStatName)) {

          this.statChangeLevels.get(j).level += newChanges.get(i).getStatChangeLevel();

          // Purely for turn info collection
          if (turnInfo != null) {

            int change = newChanges.get(i).getStatChangeLevel();

            if (change < 0) {
              turnInfo.add(this.name + "'s " + newChanges.get(i).getStatName() + " decreased by " + Math.abs(change) + " levels.");
            }
            else if (change > 0) {
              turnInfo.add(this.name + "'s " + newChanges.get(i).getStatName() + " increased by " + change + " levels.");
            }

          }

        }

      }

    }

  }

  // public void applyAilment(GameState currentGS, int ailmentID)

  // Currently missing:
  // Foresight, Type bases status immunities, applying flinch
  public void applyAilment(GameState currentGS, Move move, ArrayList<String> turnInfo) {

    MoveMeta meta = move.metaData;

    int ailmentID = meta.ailmentID;

    // Skip if ailment == none
    if (ailmentID == 0) { return; }

    int randNo = 1;

    PermanentStatusConditions oneOfStatuses = this.statusUnaffectedBySwitching;
    TemporaryStatusConditions regularStatuses = this.statusAffectedBySwitching;

    // Check no perm status is already applied

    Field targetField = currentGS.getTargetField(currentGS, this);

    if (targetField.activeFieldEffects.get(3).turnsRemaining > -1) {

      // System.out.println("Protected by safeguard.");
      if (turnInfo != null) {
        turnInfo.add(this.name + " was protected by safeguard.");
      }
      return;

    }

    if (oneOfStatuses.isStatusAvailable()) {

      if (ailmentID == 1 || ailmentID == 3 || ailmentID == 4) {

        oneOfStatuses.getStatusList().get(ailmentID - 1).turnsRemaining = 1;

      }

      else if (ailmentID == 5) {

        int moveID = move.moveID;

        // Badly poisoned
        if (moveID == 92 || moveID == 305) {

          oneOfStatuses.getStatusList().get(ailmentID - 1).turnsRemaining = 100;
          if (turnInfo != null) {
            turnInfo.add(this.name + " was badly poisoned.");
          }

        }

        // Regular poison
        else {

          oneOfStatuses.getStatusList().get(ailmentID - 1).turnsRemaining = 1;
          if (turnInfo != null) {
            turnInfo.add(this.name + " was poisoned.");
          }

        }

      }

      else if (ailmentID == 2) {

        if (meta.min_turns == -1 || meta.min_turns == -1) {
          randNo = UtilityFunctions.randomNumber(1, 3);
        }
        else {
          // Sleep turns - using turn range found in move data.
          randNo = UtilityFunctions.randomNumber(meta.min_turns, meta.max_turns);
        }

        oneOfStatuses.getStatusList().get(ailmentID - 1).turnsRemaining = randNo;

        if (turnInfo != null) {
          turnInfo.add(this.name + " fell asleep.");
        }

      }

    }

    // TemporaryStatusConditions

    if (ailmentID == 6) {

      if (regularStatuses.getStatusList().get(0).turnsRemaining > -1) {
        // System.out.println("Target already confused.");
        if (turnInfo != null) {
          turnInfo.add(this.name + " is already confused.");
        }
      }

      else {

        if (meta.min_turns == -1 || meta.min_turns == -1) {

          randNo = UtilityFunctions.randomNumber(1, 3);

        }

        else {

          // Confusion turns - using turn range found in move data.
          randNo = UtilityFunctions.randomNumber(meta.min_turns, meta.max_turns);

        }

        if (turnInfo != null) {
          turnInfo.add(this.name + " became confused.");
        }
        regularStatuses.getStatusList().get(0).turnsRemaining = randNo;

      }

    }

    if (ailmentID == 7) {

      if (regularStatuses.getStatusList().get(1).turnsRemaining > -1) {
        // System.out.println("Target already inlove.");
        if (turnInfo != null) {
          turnInfo.add(this.name + " is already in love.");
        }
      }

      else {
        regularStatuses.getStatusList().get(1).turnsRemaining = 1;
        if (turnInfo != null) {
          turnInfo.add(this.name + " fell in love.");
        }
      }

    }

    // Possibly use data from moves, eg max turns
    if (ailmentID == 8) {

      if (regularStatuses.getStatusList().get(2).turnsRemaining > -1) {
        // System.out.println("Target already trapped.");
      }

      else {

        if (meta.min_turns == -1 || meta.min_turns == -1) {

          randNo = UtilityFunctions.randomNumber(5, 6);

        }

        else {

          // Confusion turns - using turn range found in move data.
          randNo = UtilityFunctions.randomNumber(meta.min_turns, meta.max_turns);

        }

        if (turnInfo != null) {
          turnInfo.add(this.name + " is trapped.");
        }
        regularStatuses.getStatusList().get(2).turnsRemaining = randNo;

      }

    }

    if (ailmentID == 9 && oneOfStatuses.getStatusList().get(1).turnsRemaining > -1) {

      if (regularStatuses.getStatusList().get(3).turnsRemaining > -1) {
        // System.out.println("Target already having a nightmare.");
      }

      else {

        if (meta.min_turns == -1 || meta.min_turns == -1) {

          randNo = UtilityFunctions.randomNumber(1, 3);

        }

        else {

          // Confusion turns - using turn range found in move data.
          randNo = UtilityFunctions.randomNumber(meta.min_turns, meta.max_turns);

        }

        regularStatuses.getStatusList().get(3).turnsRemaining = randNo;
        if (turnInfo != null) {
          turnInfo.add(this.name + " started getting nightmares.");
        }

      }

    }

    // Move fails
    else if (ailmentID == 9) {
      // System.out.println("Target is not asleep");
      if (turnInfo != null) {
        turnInfo.add(this.name + " was unaffected.");
      }
    }

    if (ailmentID == 14 && oneOfStatuses.getStatusList().get(1).turnsRemaining == -1) {

      if (regularStatuses.getStatusList().get(4).turnsRemaining > -1) {
        // System.out.println("Target already drowsy.");
        if (turnInfo != null) {
          turnInfo.add(this.name + " is already drowsy.");
        }
      }

      else {
        regularStatuses.getStatusList().get(4).turnsRemaining = 1;
        if (turnInfo != null) {
          turnInfo.add(this.name + " became drowsy.");
        }
      }

    }

    // Move fails
    else if (ailmentID == 14) {
      // System.out.println("Target is already asleep");
      if (turnInfo != null) {
        turnInfo.add(this.name + " was unaffacted.");
      }
    }

    if (ailmentID == 17) {

      System.out.println("Foresight not implemented yet.");

    }

    if (ailmentID == 18) {

      if (regularStatuses.getStatusList().get(6).turnsRemaining > -1) {
        // System.out.println("Target already seeded.");
        if (turnInfo != null) {
          turnInfo.add(this.name + " is already seeded.");
        }
      }

      else {
        regularStatuses.getStatusList().get(6).turnsRemaining = 1;
        if (turnInfo != null) {
          turnInfo.add(this.name + " was seeded.");
        }
      }

    }


  }

  public Boolean isType(int id) {

    int size = this.types.size();

    for (int i = 0; i < size; i++) {

      if (this.types.get(i).getTypeID() == id) { return true; }

    }

    return false;

  }

  public Boolean isType(String name) {

    int size = this.types.size();

    for (int i = 0; i < size; i++) {

      if (this.types.get(i).getTypeName().equals(name)) { return true; }

    }

    return false;

  }

  public void applyWeatherDamage(Weather weather, ArrayList<String> turnInfo, int printMode) {

    int maxHP = this.stats.get(0).getStatValue();

    if (weather.flagID == 0 && weather.turnsRemaining != -1) {

      if (this.isType("rock") || this.isType("ground") || this.isType("steel")) {

        return;

      }

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Buffered by the sandstorm.");
      }
      if (turnInfo != null) {
        turnInfo.add(this.name + " is buffeted by the sandstorm.");
      }

      int hpLoss = (int) ( maxHP / 16.0 );

      this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

    }

    else if (weather.flagID == 3 && weather.turnsRemaining != -1) {

      if (this.isType("ice")) {

        return;

      }

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Buffered by the hail.");
      }
      if (turnInfo != null) {
        turnInfo.add(this.name + " is buffeted by the hail.");
      }

      int hpLoss = (int) ( maxHP / 16.0 );

      this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

    }

  }

  public void applyPoisonDamage(ArrayList<String> turnInfo, int printMode) {

    Flag poisonStatus = this.statusUnaffectedBySwitching.getStatusList().get(4);

    if (poisonStatus.turnsRemaining == -1) {

      return;

    }

    int maxHP = this.stats.get(0).getStatValue();

    int hpLoss = (int) ( maxHP / 16.0 );

    // Badly poisoned

    if (poisonStatus.turnsRemaining > 1) {

      poisonStatus.turnsRemaining -= 1;

      // Counting turns since poisoned
      hpLoss = hpLoss * (100 - poisonStatus.turnsRemaining);

    }

    // dmg and no decrement.
    if (printMode == PRINTING_ENABLED) {
      System.out.println("X is hurt by poison.");
    }
    if (turnInfo != null) {
      turnInfo.add(this.name + " is hurt by poison.");
    }
    this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

  }

  public void applyBurnDamage(ArrayList<String> turnInfo, int printMode) {

    Flag burnStatus = this.statusUnaffectedBySwitching.getStatusList().get(3);

    if (burnStatus.turnsRemaining == -1) {

      return;

    }

    int maxHP = this.stats.get(0).getStatValue();

    int hpLoss = (int) ( maxHP / 16.0 );

    // dmg and no decrement.

    if (printMode == PRINTING_ENABLED) {
      System.out.println("X is hurt by burn.");
    }
    if (turnInfo != null) {
      turnInfo.add(this.name + " is hurt by its burn.");
    }

    this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

  }

  // Sleep decrement in applyMove means that there is never nightmare without
  // sleep, so we do not need to check for that.
  public void applyNightmareDamage(ArrayList<String> turnInfo, int printMode) {

    Flag nightmareStatus = this.statusAffectedBySwitching.getStatusList().get(3);

    if (nightmareStatus.turnsRemaining == -1) {

      return;

    }

    int maxHP = this.stats.get(0).getStatValue();

    int hpLoss = (int) ( maxHP / 4.0 );

    if (printMode == PRINTING_ENABLED) {
      System.out.println("X is tormented by nightmare.");
    }
    if (turnInfo != null) {
      turnInfo.add(this.name + " is tormented by nightmare.");
    }

    this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

    // Decrement
    this.statusAffectedBySwitching.getStatusList().get(3).turnsRemaining -= 1;

  }

  public void applyTrapDamage(ArrayList<String> turnInfo, int printMode) {

    Flag trapStatus = this.statusAffectedBySwitching.getStatusList().get(2);

    if (trapStatus.turnsRemaining == -1) {

      return;

    }

    int maxHP = this.stats.get(0).getStatValue();

    int hpLoss = (int) ( maxHP / 16.0 );

    if (printMode == PRINTING_ENABLED) {
      System.out.println("X is hurt by the trap.");
    }
    if (turnInfo != null) {
      turnInfo.add(this.name + " hurt by the trap.");
    }

    this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

    // Decrement
    this.statusAffectedBySwitching.getStatusList().get(2).turnsRemaining -= 1;

  }

  public void applyLeechSeed(Pokemon toHeal, ArrayList<String> turnInfo, int printMode) {

    Flag seedStatus = this.statusAffectedBySwitching.getStatusList().get(6);

    if (seedStatus.turnsRemaining == -1) {

      return;

    }

    int maxHP = this.stats.get(0).getStatValue();

    int hpLoss = (int) ( maxHP / 16.0 );

    int actualHealthLost = Math.min(this.currentHealth,hpLoss);

    if (printMode == PRINTING_ENABLED) {
      System.out.println("X is drained by leech seed.");
    }
    if (turnInfo != null) {
      turnInfo.add(this.name + "'s health was drained by leech seed.");
    }

    this.currentHealth = Math.max(0,this.currentHealth - hpLoss);

    toHeal.currentHealth += actualHealthLost;

    // No decrement

  }

  public void applyYawn(ArrayList<String> turnInfo, int printMode) {

    Flag yawnStatus = this.statusAffectedBySwitching.getStatusList().get(4);
    Flag sleepStatus = this.statusUnaffectedBySwitching.getStatusList().get(1);

    if (yawnStatus.turnsRemaining == -1) {

      return;

    }

    if (yawnStatus.turnsRemaining == 0) {

      if (this.statusUnaffectedBySwitching.isStatusAvailable()) {

        int randNo = UtilityFunctions.randomNumber(1, 3);
        sleepStatus.turnsRemaining = randNo;

        if (printMode == PRINTING_ENABLED) {
          System.out.println("X feel asleep");
        }
        if (turnInfo != null) {
          turnInfo.add(this.name + " feel asleep.");
        }


      }

    }

    yawnStatus.turnsRemaining -= 1;

  }

  public void resetStatChanges() {

    int size = this.statChangeLevels.size();

    for (int i = 0; i < size; i++) {

      this.statChangeLevels.get(i).level = 0;

    }

  }

  // Does not deep copy:
  // types, form, moves, ability
  public Pokemon deepCopy() {

    Pokemon pmanToReturn = new Pokemon();

    pmanToReturn.id = this.id;
    pmanToReturn.name = this.name;
    pmanToReturn.level = this.level;
    pmanToReturn.types = this.types;
    pmanToReturn.currentHealth = this.currentHealth;
    pmanToReturn.form = this.form;
    pmanToReturn.moves = this.moves;
    pmanToReturn.ability = this.ability;

    ArrayList<Stat> statsCopy = new ArrayList<Stat>();
    ArrayList<StatChange> statChangesCopy = new ArrayList<StatChange>();

    int size = this.stats.size();

    for (int i = 0; i < size; i++) {

      statsCopy.add( this.stats.get(i).deepCopy() );

    }

    pmanToReturn.stats = statsCopy;

    size = this.statChangeLevels.size();

    for (int i = 0; i < size; i++) {

      statChangesCopy.add( this.statChangeLevels.get(i).deepCopy() );

    }

    pmanToReturn.statChangeLevels = statChangesCopy;

    pmanToReturn.statusUnaffectedBySwitching = this.statusUnaffectedBySwitching.deepCopy();
    pmanToReturn.statusAffectedBySwitching = this.statusAffectedBySwitching.deepCopy();

    pmanToReturn.typeMatchUps = this.typeMatchUps;

    return pmanToReturn;

  }

  public float probabilityOfUsingMove() {

    // Get chance of at least one status currently active preventing the move being used.

    // p(moveWorking) * p(moveWorking) *

    if (this.currentHealth <= 0) {

      return 0;

    }

    ArrayList<Flag> pStatusList = this.statusUnaffectedBySwitching.getStatusList();
    ArrayList<Flag> tStatusList = this.statusAffectedBySwitching.getStatusList();

    float prob = 1;

    // Paralysis
    if (pStatusList.get(0).turnsRemaining > -1) {

      prob = prob * (float) 0.75;

    }
    // Sleep
    else if (pStatusList.get(1).turnsRemaining > 0) {

      prob = prob * (float) 0;

    }
    // Frozen
    else if (pStatusList.get(2).turnsRemaining > -1) {

      prob = prob * (float) 0.2;

    }

    // Confusion
    if (tStatusList.get(0).turnsRemaining > 0) {

      prob = prob * (float) 0.66;

    }
    // Love
    if (tStatusList.get(1).turnsRemaining > -1) {

      prob = prob * (float) 0.5;

    }
    // Flinch
    if (tStatusList.get(7).turnsRemaining > -1) {

      prob = prob * (float) 0;

    }

    return prob;

  }

  public void setTypeMatchUps() {

    ArrayList<Float> matchUps = new ArrayList<Float>();

    ArrayList<PokemonType> defensiveTypes = this.types;
    int lengthOfList = defensiveTypes.size();

    for (int j = 1; j <= 18; j++) {

      float floatToAdd = 1;
      TypeMatchUp data = this.getTypeMatchUpData(j);

      for (int i = 0; i < lengthOfList; i++) {

        if (data.double_damage_to.toString().contains(defensiveTypes.get(i).getTypeName() + "")) {

          floatToAdd = floatToAdd * 2;

        }

        else if (data.half_damage_to.toString().contains(defensiveTypes.get(i).getTypeName() + "")) {

          floatToAdd = floatToAdd / 2;

        }

        else if (data.no_damage_to.toString().contains(defensiveTypes.get(i).getTypeName() + "")) {

          floatToAdd = floatToAdd * 0;

        }

      }

      matchUps.add(floatToAdd);

    }

    this.typeMatchUps = matchUps;

  }

  // Assumes FormID will always match url links
  // To be called on a team when created? Could just save id.png and
  // look for those at display time.
  // public void storeFormImages(int index) {
  //
  //   int formID = this.form.formID;
  //
  //   String urlB = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/" + formID + ".png";
  //   String urlF = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/front/" + formID + ".png";
  //
  //   System.out.println(urlB);
  //   System.out.println(urlF);
  //
  //   File file = new File("./sprites/" + formID);
  //   Boolean exists = file.exists();
  //
  //   if (exists) {
  //     System.out.println("Sprite image found");
  //     return;
  //   }
  //
  //   System.out.println("Sprite image not found, downloading new images...");
  //
  //   return;
  //
  // }

  public Boolean isMoveNameAllowed(String name) {

    String allowedGen1Moves = "absorb,acid,acid-armor,agility,amnesia,aurora-beam,barrage,barrier,bide,bind,bite,blizzard,body-slam,bone-club,bonemerang,bubble,bubble-beam,clamp,comet-punch,confuse-ray,confusion,constrict,conversion,counter,crabhammer,cut,defense-curl,dig,disable,dizzy-punch,double-kick,double-slap,double-team,double-edge,dragon-rage,dream-eater,drill-peck,earthquake,egg-bomb,ember,explosion,fire-blast,fire-punch,fire-spin,fissure,flamethrower,flash,fly,focus-energy,fury-attack,fury-swipes,glare,growl,growth,guillotine,gust,harden,haze,headbutt,high-jump-kick,horn-attack,horn-drill,hydro-pump,hyper-beam,hyper-fang,hypnosis,ice-beam,ice-punch,jump-kick,karate-chop,kinesis,leech-life,leech-seed,leer,lick,light-screen,lovely-kiss,low-kick,meditate,mega-drain,mega-kick,mega-punch,metronome,mimic,minimize,mirror-move,mist,night-shade,pay-day,peck,petal-dance,pin-missile,poison-gas,poison-powder,poison-sting,pound,psybeam,psychic,psywave,quick-attack,rage,razor-leaf,razor-wind,recover,reflect,rest,roar,rock-slide,rock-throw,rolling-kick,sand-attack,scratch,screech,seismic-toss,self-destruct,sharpen,sing,skull-bash,sky-attack,slam,slash,sleep-powder,sludge,smog,smokescreen,soft-boiled,solar-beam,sonic-boom,spike-cannon,splash,spore,stomp,strength,string-shot,struggle,stun-spore,submission,substitute,super-fang,supersonic,surf,swift,swords-dance,tackle,tail-whip,take-down,teleport,thrash,thunder,thunder-punch,thunder-shock,thunder-wave,thunderbolt,toxic,transform,tri-attack,twineedle,vine-whip,vise-grip,water-gun,waterfall,whirlwind,wing-attack,withdraw,wrap,";
    String allowedGen2Moves = "aeroblast,ancient-power,attract,baton-pass,beat-up,belly-drum,bone-rush,charm,conversion-2,cotton-spore,cross-chop,crunch,curse,destiny-bond,detect,dragon-breath,dynamic-punch,encore,endure,extreme-speed,false-swipe,feint-attack,flail,flame-wheel,foresight,frustration,fury-cutter,future-sight,giga-drain,heal-bell,hidden-power,icy-wind,iron-tail,lock-on,mach-punch,magnitude,mean-look,megahorn,metal-claw,milk-drink,mind-reader,mirror-coat,moonlight,morning-sun,mud-slap,nightmare,octazooka,outrage,pain-split,perish-song,powder-snow,present,protect,psych-up,pursuit,rain-dance,rapid-spin,return,reversal,rock-smash,rollout,sacred-fire,safeguard,sandstorm,scary-face,shadow-ball,sketch,sleep-talk,sludge-bomb,snore,spark,spider-web,spikes,spite,steel-wing,sunny-day,swagger,sweet-kiss,sweet-scent,synthesis,thief,triple-kick,twister,vital-throw,whirlpool,zap-cannon,";
    String allowedGen3Moves = "aerial-ace,air-cutter,arm-thrust,aromatherapy,assist,astonish,blast-burn,blaze-kick,block,bounce,brick-break,bulk-up,bullet-seed,calm-mind,camouflage,charge,cosmic-power,covet,crush-claw,dive,doom-desire,dragon-claw,dragon-dance,endeavor,eruption,extrasensory,facade,fake-out,fake-tears,feather-dance,flatter,focus-punch,follow-me,frenzy-plant,grass-whistle,grudge,hail,heat-wave,helping-hand,howl,hydro-cannon,hyper-voice,ice-ball,icicle-spear,imprison,ingrain,iron-defense,knock-off,leaf-blade,luster-purge,magic-coat,magical-leaf,memento,metal-sound,meteor-mash,mist-ball,mud-shot,mud-sport,muddy-water,nature-power,needle-arm,odor-sleuth,overheat,poison-fang,poison-tail,psycho-boost,recycle,refresh,revenge,rock-blast,rock-tomb,role-play,sand-tomb,secret-power,shadow-punch,shadow-wave,sheer-cold,shock-wave,signal-beam,silver-wind,skill-swap,sky-uppercut,slack-off,smelling-salts,snatch,spit-up,stockpile,superpower,swallow,tail-glow,taunt,teeter-dance,tickle,torment,trick,uproar,volt-tackle,water-pulse,water-sport,water-spout,weather-ball,will-o-wisp,wish,yawn,";
    String allowedGen4Moves = "acupressure,air-slash,aqua-jet,aqua-ring,aqua-tail,assurance,attack-order,aura-sphere,avalanche,brave-bird,brine,bug-bite,bug-buzz,bullet-punch,captivate,charge-beam,chatter,close-combat,copycat,cross-poison,crush-grip,dark-pulse,dark-void,defend-order,defog,discharge,double-hit,draco-meteor,dragon-pulse,dragon-rush,drain-punch,earth-power,embargo,energy-ball,feint,fire-fang,flare-blitz,flash-cannon,fling,focus-blast,force-palm,gastro-acid,giga-impact,grass-knot,gravity,guard-swap,gunk-shot,gyro-ball,hammer-arm,head-smash,heal-block,heal-order,healing-wish,heart-swap,ice-fang,ice-shard,iron-head,judgment,last-resort,lava-plume,leaf-storm,lucky-chant,lunar-dance,magma-storm,magnet-bomb,magnet-rise,me-first,metal-burst,miracle-eye,mirror-shot,mud-bomb,nasty-plot,natural-gift,night-slash,ominous-wind,payback,pluck,poison-jab,power-gem,power-swap,power-trick,power-whip,psycho-cut,psycho-shift,punishment,roar-of-time,rock-climb,rock-polish,rock-wrecker,roost,seed-bomb,seed-flare,shadow-claw,shadow-force,shadow-sneak,spacial-rend,stealth-rock,stone-edge,sucker-punch,switcheroo,tailwind,thunder-fang,toxic-spikes,trick-room,trump-card,u-turn,vacuum-wave,wake-up-slap,wood-hammer,worry-seed,wring-out,x-scissor,zen-headbutt,";

    String specialCases = "perish-song,bide,rage";

    if (allowedGen1Moves.contains(name) || allowedGen2Moves.contains(name) || allowedGen3Moves.contains(name) || allowedGen4Moves.contains(name) || specialCases.contains(name)) {

      // Needs to be uncommented to actually ban, left in so moveID comments remain accurate.
      // if (specialCases.contains(name)) { return false; }

      return true;

    }

    return false;

  }

}
