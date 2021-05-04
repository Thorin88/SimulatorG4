
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

// Could look for "charges for one turn" in effect text.

public class Move {

  String moveName;
  int moveID;
  // StatInfo info;
  MoveMeta metaData;

  public Move(String name, int id) {

    this.moveName = name;
    this.moveID = id;

  }

  public Move(String name, int id, MoveMeta meta) {

    this.moveName = name;
    this.moveID = id;
    this.metaData = meta;

  }

  public String getMoveName() {

    return this.moveName;

  }

  public int getMoveID() {

    return this.moveID;

  }

  public MoveMeta getMoveMetaData() {

    return this.metaData;

  }

  public void setMetaData(MoveMeta data) {

    this.metaData = data;

  }

  // Need short effect too.
  public Object parseEffect() {

    /*
      "User is forced to use this move for X turns" where X is a word
      "Power doubles every time this move is used in succession to a maximum of Yx"
      where Y is a number. log 2 Y = number of turns this applies for.
      "If this move misses" reset doubling factor

      No moves will hit the user for the remainder of this turn

    */

    return null;

  }

  @Override
  public String toString() {

    return "[" + this.moveName + "," + this.moveID +"]";

  }

}

/*

Bans:

All moves with ailmentID = -1.

0: Causes no status problems, just damage calc and move effect

Length of status decided at move time. Decremented each time player turn passes.

1: Can cause paralysis - Pokemon could have a single status field (burn,freeze,sleep
poison,paralysis) to ensure only one at a time.

2,3,4,5: Check status field is used and set field value.

Now seperate lot of status fields, these will have to be worked out in order;
eg confusion evaluated before paralysis.

6,7,8,9: Field values set and decremented.

12: Ban torment

13: More fields, function to provide all possible options (remember struggle
possibility) doesn't include (Banned)

14: Status in the same field as confusion, evaluated at the end of the turn

15: (Maybe ban) Quick check on move evaluatino to prevent healing if move has a healing tag.

17: Another field, checked by type matchup system-eg function returns 1 instead
of 0.

18: Same as 14

19: Do not implement

20: Do not implement

21: Maybe, but probably ban.

24: Ban

Damage Types:

0: Just need the damage calc

1: No dmg calc, need to check status is free and apply if needed.

2: No dmg calc, check move data for stat changes, apply to target.

3: No dmg calc, heal amount in move effect. (Could have parsers per type,
eg a move of this type has its own parser to find health % or fraction)

4: dmg calc, then check availability of ailment.

5: Check availability of ailment, then apply stat changes to opponent

6: dmg calc, check move data for stat changes, apply to TARGET.

7: dmg calc, check move data for stat changes, apply to USER.

8: dmg calc, heal amount in move effect.

9: No dmg calc if it hits

10: Might be hard to implement these, check the turn data

11: Will likely need unique fields, eg if statements

*/
