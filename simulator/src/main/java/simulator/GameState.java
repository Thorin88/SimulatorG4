
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

import java.lang.reflect.Type;

import static utility.Constants.*;

public class GameState {

  int turnCount;

  Team player1Team;
  Team player2Team;

  Pokemon player1CurrentPokemon;
  Pokemon player2CurrentPokemon;

  Field player1Field;
  Field player2Field;

  Weather weather;

  // Move player1Charging
  // Move player2Charging

  public GameState() {

    this.turnCount = 0;

    this.player1Team = new Team();
    this.player2Team = new Team();

    this.player1Field = new Field();
    this.player2Field = new Field();

    this.player1Field.initialise();
    this.player2Field.initialise();

    this.weather = new Weather();

    // this.weather.initialise();

  }

  // TODO
  // Doesn't use its own data so AI can do it on other gameStates.
  // Could use another way of taking the move, eg index. Use one of pman objects in this class as user.
  // Check move-cats it currently does not work for.

  // F = -2: Normal play
  // F = -1: Normal play + prints
  // F = 0: Move Fails
  // F = 1: Move Misses
  // F = 2: Move hits with no ailment bonus
  // F = 3: Move hits with ailment bonus
  public GameState applyMove(GameState currentGS, Pokemon user, Move selectedMove, int forceFlag, float rFlag, Boolean allowCrits, int printMode, Estimates estimates, ArrayList<Boolean> wasCrit, ArrayList<String> turnInfo) {

    if (printMode == PRINTING_ENABLED) {

      System.out.println("Chance of move succeeding: " + user.probabilityOfUsingMove());
      System.out.println("Chance of move hitting: " + currentGS.hitChance(currentGS, user, selectedMove));
      System.out.println(Arrays.toString(currentGS.chanceOfMoveOutcomes(currentGS, user, selectedMove)));

    }

    ArrayList<Flag> pStatusFlags = user.statusUnaffectedBySwitching.getStatusList();
    ArrayList<Flag> tStatusFlags = user.statusAffectedBySwitching.getStatusList();

    int randNo = 1;

    if (user.currentHealth <= 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("(User has fainted)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed, " + user.name + " has fainted.");
      }
      return currentGS;

    }

    // id = 1 but list index = 0.
    // Paralysis
    if (pStatusFlags.get(0).getTurnsRemaining() != -1) {

      // Numbers between 1 and 4
      randNo = UtilityFunctions.randomNumber(1,4);

      // Force status to trigger
      if (forceFlag == FORCE_FAIL) { return currentGS; }

      // Any flag which is not regular play will bypass the status effect
      if (randNo == 1 && forceFlag == NORMAL_PLAY) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon paralysed.");
        }
        if (turnInfo != null) {
          turnInfo.add(user.name + " is paralysed and can't move!");
        }
        return currentGS;

      }

    }

    // + 1 to allow idenfification of awoken pokemon
    // Sleep - Turns choosen when ailment applied. Does not reset when switching.
    else if (pStatusFlags.get(1).getTurnsRemaining() > -1) {

      if (pStatusFlags.get(1).getTurnsRemaining() == 0) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("X woke up.");
        }
        if (turnInfo != null) {
          turnInfo.add(user.name + " woke up.");
        }
        pStatusFlags.get(1).turnsRemaining = pStatusFlags.get(1).getTurnsRemaining() - 1;
        // Setting nightmare up;
        tStatusFlags.get(3).turnsRemaining = -1;

      } else {

        pStatusFlags.get(1).turnsRemaining = pStatusFlags.get(1).getTurnsRemaining() - 1;

        if (forceFlag == FORCE_FAIL) { return currentGS; }

        if (forceFlag == NORMAL_PLAY) {

          if (printMode == PRINTING_ENABLED) {
            System.out.println("Pokemon is asleep.");
          }
          if (turnInfo != null) {
            turnInfo.add(user.name + " is asleep.");
          }
          return currentGS;
        }

      }

    }

    // Frozen - 20% chance to unfreeze each turn
    else if (pStatusFlags.get(2).getTurnsRemaining() != -1) {

      if (forceFlag == FORCE_FAIL) { return currentGS; }

      randNo = UtilityFunctions.randomNumber(0,4);

      if (randNo != 0) {

        if (forceFlag == NORMAL_PLAY) {

          if (printMode == PRINTING_ENABLED) {
            System.out.println("Pokemon is frozen.");
          }
          if (turnInfo != null) {
            turnInfo.add(user.name + " is frozen solid and cannot move!");
          }
          return currentGS;

        }

      }

      else {

        if (forceFlag == NORMAL_PLAY) {

          if (printMode == PRINTING_ENABLED) {
            System.out.println("X thawed out.");
          }
          if (turnInfo != null) {
            turnInfo.add(user.name + " thawed out.");
          }

        }
        pStatusFlags.get(2).turnsRemaining = -1;

      }

    }

    // Burn skipped
    // Poison skipped

    // Now the lesser status effects - using ifs not else ifs since multiple status
    // conditions can apply.

    // Confusion: 33% chance
    if (tStatusFlags.get(0).getTurnsRemaining() != -1) {

      if (tStatusFlags.get(0).getTurnsRemaining() != 0) {

        if (forceFlag == FORCE_FAIL) {

          // TODO: Damage calc for this hit.
          return currentGS;

        }

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon is confused.");
        }
        if (turnInfo != null) {
          turnInfo.add(user.name + " is confused!");
        }

        randNo = UtilityFunctions.randomNumber(0,2);

        if (randNo == 0 && forceFlag == NORMAL_PLAY) {

          if (printMode == PRINTING_ENABLED) {
            System.out.println("Pokemon hurt itself in its confusion.");
          }
          if (turnInfo != null) {
            turnInfo.add(user.name + " hurt itself in its confusion!");
          }
          // TODO: Damage calc for this hit.
          tStatusFlags.get(0).turnsRemaining = tStatusFlags.get(0).getTurnsRemaining() - 1;
          return currentGS;

        }

        else {

          tStatusFlags.get(0).turnsRemaining = tStatusFlags.get(0).getTurnsRemaining() - 1;

        }

      }

      else {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon snapped out of confusion.");
        }
        if (turnInfo != null) {
          turnInfo.add(user.name + " snapped out of confusion.");
        }
        tStatusFlags.get(0).turnsRemaining = -1;

      }


    }

    // Love: Maybe do not implement this? Only goes away when opposing pokemon is switched out.
    if (tStatusFlags.get(1).getTurnsRemaining() != -1) {

      if (forceFlag == FORCE_FAIL) { return currentGS; }

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Pokemon is inlove with the foe.");
      }
      if (turnInfo != null) {
        turnInfo.add(user.name + " is inlove.");
      }

      randNo = UtilityFunctions.randomNumber(0,1);

      if (randNo == 1 && forceFlag == NORMAL_PLAY) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon is immobilized by love.");
        }
        if (turnInfo != null) {
          turnInfo.add(user.name + " is immobilized by love.");
        }
        return currentGS;

      }

    }

    // Skipped trap: Implement as 1/8 HP damage each turn. Possibly also not being
    // able to switch out.

    // Skipped nightmare: Implement as 1/4 HP damage each turn, opponent must be sleeping (!= -1)

    // Yawn occurs at the end of the turn.

    // Forsight occurs during type calculation. Could use a GS check when the matchup
    // function is used. If int = 0 against ghost, int = 1.

    // Skipped leechseed: Implement as 1/8 HP damage each turn.

    // Check for flinch

    if (tStatusFlags.get(7).turnsRemaining != -1) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("User flinched.");
      }
      if (turnInfo != null) {
        turnInfo.add(user.name + " flinched.");
      }
      // Just incase - reset at end of every tirn anyway.
      tStatusFlags.get(7).turnsRemaining = -1;

      if (forceFlag == FORCE_FAIL || forceFlag == NORMAL_PLAY) {
        return currentGS;
      }

    }

    if (turnInfo != null) {
      turnInfo.add(user.name + " used " + UtilityFunctions.toCaps(selectedMove.moveName) + ".");
    }

    MoveMeta meta = selectedMove.getMoveMetaData();

    int moveCategoryID = meta.moveCategoryID;
    String damageType = meta.dmgType;

    int accuracy = meta.accuracy;

    // Possibly use extra converter values, eg 2 == user field.
    int target = GameState.targetIDConverter(meta.targetTypeID);
    Pokemon targetPman = currentGS.getTargetPokemon(currentGS, user, target);

    // Need to check if move misses or not.
    // User acc stage - target eva stage

    // Covers status as well
    if (targetPman.currentHealth <= 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move failed (target has 0 HP)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed, target has fainted.");
      }

      return currentGS;

    }

    // Move forced to miss
    if (forceFlag == FORCE_MISS) {

      return currentGS;

    }

    if (forceFlag == NORMAL_PLAY && currentGS.didMoveMiss(user, targetPman, accuracy, currentGS, selectedMove)) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move missed.");
      }
      if (turnInfo != null) {
        turnInfo.add("Move missed.");
      }
      return currentGS;

    }

    // Field moves here

    int moveID = selectedMove.moveID;

    if (Field.isHazard(moveID)) {

      // System.out.println("Hazard");
      currentGS.applyHazard(currentGS, user, selectedMove, target, printMode, turnInfo);

    }

    else if (Field.isFieldEffect(moveID)) {

      // System.out.println("FieldEffect");
      // Safeguard applied and then ailment applied. Brings up "protected
      // by safeguard" message unnecessary.
      currentGS.applyFieldEffect(currentGS, user, selectedMove, target, printMode, turnInfo);

    }

    else if (Weather.isWeather(moveID)) {

      // System.out.println("Weather");
      currentGS.applyWeather(currentGS, user, selectedMove, printMode, turnInfo);

    }

    else if (GameState.isHealing(moveCategoryID)) {

      int baseHealingPercentage = meta.healing;

      // Weather check to decrease this base

      Weather currentWeather = currentGS.weather;
      int weatherID = currentWeather.flagID;

      // Only applies to certain moves (moonlight, morning-sun, synthesis)

      if (moveID >= 234 && moveID <= 236) {

        switch (weatherID) {

          case -1:
            // 50%
            break;
          case 2:
            baseHealingPercentage = 66;
            break;
          default:
            baseHealingPercentage = 25;

        }

      }

      int amountToHeal = (int) ( ((float) baseHealingPercentage / 100.0) * user.stats.get(0).getStatValue() );

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Amount to heal: " + amountToHeal);
      }
      if (turnInfo != null) {
        int actualHPHealed = Math.min(user.stats.get(0).getStatValue() - user.currentHealth,amountToHeal);
        float temp = (actualHPHealed / (float) user.stats.get(0).getStatValue() ) * 100;
        turnInfo.add(user.name + " gained " + Math.round(temp * 10.0) / 10.0 + "% of its HP.");
      }

      user.currentHealth = Math.min((user.currentHealth + amountToHeal),user.stats.get(0).getStatValue());

    }


    int numberOfHits = UtilityFunctions.randomNumber(meta.min_hits,meta.max_hits);

    do {

      if (GameState.isDamaging(moveCategoryID)) {

        // Making Modifier value
        float modifier = currentGS.calcMoveDamageModifier(currentGS,user,selectedMove,targetPman,rFlag,allowCrits,wasCrit,turnInfo);
        if (modifier == -1) {

          if (printMode == PRINTING_ENABLED) {
            System.out.println("Move not implemented yet.");
          }

          return currentGS;
        }

        // System.out.println(modifier);

        if (!damageType.equals("status")) {

          int damage = currentGS.damage(currentGS,user,selectedMove,damageType,targetPman,printMode);

          damage = (int) (damage * modifier);

          // Non-effective typing can lead to 0 dmg

          if (modifier == 0) {

            if (printMode == PRINTING_ENABLED) {
              System.out.println("No effect");
            }

            return currentGS;

          }

          if (damage < 1 && modifier != 0) { damage = 1; }

          if (printMode == PRINTING_ENABLED) {
            if (wasCrit != null && wasCrit.get(0)) { System.out.println("Critical Hit!"); }
            System.out.println("Damage: "+ damage + " (modifier = " + modifier + ")");
          }


          // TODO: Take away damage from pman, for use in drain moves.

          int totalHPRemoved = Math.min(damage,targetPman.currentHealth);

          if (printMode == PRINTING_ENABLED) {
            System.out.println("HP Loss: " + totalHPRemoved);
          }
          if (turnInfo != null) {
            float temp = (totalHPRemoved / (float) targetPman.stats.get(0).getStatValue() ) * 100;
            turnInfo.add(targetPman.name + " lost " + Math.round(temp * 10.0) / 10.0 + "% of its HP.");
          }

          targetPman.currentHealth -= totalHPRemoved;

          // Instead of returning the gamestate if a pman's health is 0, still
          // need to apply everything else (eg user stat raises). Even though the
          // pman has fainted, this will not affect anything.

          // Check if move is damaging and healing (drain move)

          if (meta.drain != 0) {

            // Can be negative or positive
            int healing = (int) (totalHPRemoved * ((float) (meta.drain / 100.0)));

            if (printMode == PRINTING_ENABLED) {
              System.out.println("Healing: " + healing);
            }

            // TODO: Apply healing

            if (healing >= 0) {

              // Comparing currentHealth + healing to max health of pman
              user.currentHealth = Math.min((user.currentHealth + healing),user.stats.get(0).getStatValue());

            }

            else {

              user.currentHealth = Math.max((user.currentHealth + healing),0);

            }

          }

          // Provides stat range estimate for partially observable agents
          // if (true) {
          // TODO: Undo this
          if (estimates != null) {

            // Must use totalHPRemoved - what if it is a much smaller amount
            // than calculated damage? Then only update when the user does not faint

            // Do not return valid stat update
            if (targetPman.currentHealth <= 0) {}

            else {
              // Estimate target's defence
              // Estimate user's attack

              // What a human can see
              // double percentageHPLost = (double) totalHPRemoved / targetPman.stats.get(0);

              estimates.totalHPRemoved = totalHPRemoved;

              double rLow = modifier / 0.85;
              double rHigh = modifier * 0.85;

              if (wasCrit != null && wasCrit.get(0)) {
                rLow = rLow / 2.0;
                rHigh = rHigh / 2.0;
              }

              estimates.rLow = rLow;
              estimates.rHigh = rHigh;

              // double damageDealtRLow = totalHPRemoved / rLow;
              // double damageDealtRHigh = totalHPRemoved / rHigh;

              // Calc stat from damage and modifier
              // Change these to return the multiplyer not the actual values
              estimates.userAtkMulitplier = currentGS.getAttackStatModifier(user, damageType);
              estimates.targetDefMulitplier = currentGS.getDefenseStatModifier(targetPman, damageType, currentGS);

              // Atk stat * multiplier = effective atk
              estimates.userActualEffectiveAtkStat = currentGS.getEffectiveAttackStat(user, damageType);
              estimates.targetActualEffectiveDefStat = currentGS.getEffectiveDefenseStat(targetPman, damageType, currentGS);

              // Store in array list index depending on phy or special

              estimates.power = meta.power;

              estimates.levelCalc = ((2 * user.level) / 5 + 2);

              if (user.equals(currentGS.player1CurrentPokemon)) {
                estimates.setAttacker(1);
              }
              else{
                estimates.setAttacker(2);
              }

            }

          }

        }

      }

      numberOfHits--;

    } while (numberOfHits > 0);



    // System.out.println("Target: ");
    // System.out.println(targetPman.toString());

    // Applying status chances and ailments.

    // 1. Apply stat changes to TARGET or USER - currently always applies
    // stats to target.
    // targetTypeID = where the move hits, move-cat can tell who gets the buffs
    // damage+raise = damage + stat change on user
    // damage+lower = damage + stat change on target

    Pokemon statAndAilmentTarget = currentGS.getStatChangeAndAilmentTargetPokemon(currentGS, targetPman, moveCategoryID);

    // + Chance of stat change - could use effect chance or stat chance
    // Use stat_chance - eg 188

    // 0 used to indicate it always happens if the move doesn't miss.
    // Also force a stat change if asked
    if (meta.stat_chance == 0 || forceFlag == FORCE_HIT_EFFECT) {

      statAndAilmentTarget.addToStatChangeLevels(currentGS, meta.stat_changes, turnInfo);

    }

    // Force stat change to not happen - If flag != NO_EFFECT do this
    else if (forceFlag != FORCE_HIT_NO_EFFECT) {

      randNo = UtilityFunctions.randomNumber(0,100);

      if (randNo < meta.stat_chance) {

        statAndAilmentTarget.addToStatChangeLevels(currentGS, meta.stat_changes, turnInfo);

      }

    }

    // Applying ailments

    // 0 represents 100% chance, also force effect is asked
    if (meta.ailment_chance == 0 || forceFlag == FORCE_HIT_EFFECT) {

      statAndAilmentTarget.applyAilment(currentGS, selectedMove, turnInfo);

    }

    else if (forceFlag != FORCE_HIT_NO_EFFECT) {

      randNo = UtilityFunctions.randomNumber(0,100);

      if (randNo < meta.ailment_chance) {

        statAndAilmentTarget.applyAilment(currentGS, selectedMove, turnInfo);

      }

    }

    // Apply flinch. Do not apply if asked.
    if (meta.flinch_chance != 0 && forceFlag != FORCE_HIT_NO_EFFECT) {

      randNo = UtilityFunctions.randomNumber(0,100);

      // Chance of flinch applying or force flinch if asked.
      if (randNo < meta.flinch_chance || forceFlag == FORCE_HIT_EFFECT) {

        // Apply flinch status

        statAndAilmentTarget.statusAffectedBySwitching.getStatusList().get(7).turnsRemaining = 1;

      }

    }

    if (printMode == PRINTING_ENABLED) {

      System.out.println("Move used: " + selectedMove.moveName);
      System.out.println("User: " + user.name + " (HP: " + user.currentHealth + ")");
      System.out.println("Original Target: " + targetPman.name + " (HP: " + targetPman.currentHealth + ")");
      // System.out.println("HP: " + targetPman.currentHealth);
      // System.out.println(targetPman.statChangeLevels.toString());
      // System.out.println(targetPman.statusAffectedBySwitching.getStatusList().toString());
      // System.out.println(targetPman.statusUnaffectedBySwitching.getStatusList().toString());
      System.out.println("Stat change target: " + statAndAilmentTarget.name + " (HP: " + statAndAilmentTarget.currentHealth + ")");
      // System.out.println("HP: " + statAndAilmentTarget.currentHealth);
      // System.out.println(statAndAilmentTarget.statChangeLevels.toString());
      // System.out.println(statAndAilmentTarget.statusUnaffectedBySwitching.getStatusList().toString());
      // System.out.println(statAndAilmentTarget.statusAffectedBySwitching.getStatusList().toString());

    }

    return currentGS;

  }

  public GameState endTurn(GameState currentGS, int printMode, ArrayList<String> turnInfo) {

    if (printMode == PRINTING_ENABLED) {
      System.out.println("Ending turn...");
    }

    currentGS.turnCount++;

    // TODO: Always remove flinch for both pman. (Done)

    /*

    TODO: Speed ordering

    Use fastestPman variable.

    - Decrement/remove status conditions and field effects
    - Apply damage from status and field affects (Fainting each time if needed)
      Can implement this as do stuff until the stuff is all done or pman is at
      0 HP.
    - If anything has fainted, switch pman. This happens after all decrement are
      finished, but the does pman faint as everything continues.

    */

    // Ordering

    Pokemon fastestPman = null;
    Pokemon slowerPman = null;

    int speedResult;

    // No speed bias
    speedResult = currentGS.whichPokemonIsFaster(currentGS, -1);

    if (speedResult == 1) {

      fastestPman = currentGS.player1CurrentPokemon;
      slowerPman = currentGS.player2CurrentPokemon;

    }

    else {

      fastestPman = currentGS.player2CurrentPokemon;
      slowerPman = currentGS.player1CurrentPokemon;

    }

    // Weather fading

    Weather currentWeather = currentGS.weather;

    if (currentWeather.turnsRemaining == 0) {

      // Weather fades
      if (printMode == PRINTING_ENABLED) {
        System.out.println("Weather faded.");
      }
      if (turnInfo != null) {

        int weatherID = currentWeather.flagID;

        switch (weatherID) {
          case 0:
            turnInfo.add("The sandstorm subsided.");
            break;
          case 1:
            turnInfo.add("The rain stopped.");
            break;
          case 2:
            turnInfo.add("The harsh sunlight faded.");
            break;
          case 3:
            turnInfo.add("The hail stopped.");
            break;
          default:

        }

      }

      currentWeather.flagID = -1;

    }

    if (currentWeather.turnsRemaining != -1) {

      currentWeather.turnsRemaining -= 1;

      // Weather continues to be active
      if (turnInfo != null) {

        int weatherID = currentWeather.flagID;

        switch (weatherID) {
          case 0:
            turnInfo.add("The sandstorm rages.");
            break;
          case 1:
            turnInfo.add("Rain continues to fall.");
            break;
          case 2:
            turnInfo.add("The sunlight is strong.");
            break;
          case 3:
            turnInfo.add("Hail continues to fall.");
            break;
          default:

        }

      }

    }

    // Removing flinch

    fastestPman.statusAffectedBySwitching.getStatusList().get(7).turnsRemaining = -1;
    slowerPman.statusAffectedBySwitching.getStatusList().get(7).turnsRemaining = -1;

    // Weather damage

    // fastestPman.applyWeatherDamage();

    // TODO: TurnInfo from these



    if (fastestPman.currentHealth > 0) {
      fastestPman.applyWeatherDamage(currentWeather, turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
    slowerPman.applyWeatherDamage(currentWeather, turnInfo, printMode);
    }

    // Leech seed

    if (slowerPman.currentHealth > 0 && fastestPman.currentHealth > 0) {
      slowerPman.applyLeechSeed(fastestPman, turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0 && fastestPman.currentHealth > 0) {
      fastestPman.applyLeechSeed(slowerPman, turnInfo, printMode);
    }

    // Poison

    if (fastestPman.currentHealth > 0) {
      fastestPman.applyPoisonDamage(turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
      slowerPman.applyPoisonDamage(turnInfo, printMode);
    }

    // Burn

    if (fastestPman.currentHealth > 0) {
      fastestPman.applyBurnDamage(turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
      slowerPman.applyBurnDamage(turnInfo, printMode);
    }

    // Nightmare

    if (fastestPman.currentHealth > 0) {
      fastestPman.applyNightmareDamage(turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
      slowerPman.applyNightmareDamage(turnInfo, printMode);
    }

    // Traps

    if (fastestPman.currentHealth > 0) {
      fastestPman.applyTrapDamage(turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
      slowerPman.applyTrapDamage(turnInfo, printMode);
    }

    // Yawn

    if (fastestPman.currentHealth > 0) {
      fastestPman.applyYawn(turnInfo, printMode);
    }
    if (slowerPman.currentHealth > 0) {
      slowerPman.applyYawn(turnInfo, printMode);
    }

    // Field effects (always happens)

    currentGS.player1Field.endOfTurnDecrements(1, printMode);
    currentGS.player2Field.endOfTurnDecrements(2, printMode);

    return currentGS;

  }

  public float calcMoveDamageModifier(GameState currentGS, Pokemon user, Move selectedMove, Pokemon targetPman, float r, Boolean allowCrits, ArrayList<Boolean> wasCrit, ArrayList<String> turnInfo) {

    MoveMeta meta = selectedMove.getMoveMetaData();

    int moveCategoryID = meta.moveCategoryID;
    String moveType = meta.typeName;

    // int target = targetIDConverter(meta.targetTypeID);
    // Pokemon targetPman = getTargetPokemon(currentGS, user, target);

    if (targetPman == null) {

      return -1;

    }

    int randNo = UtilityFunctions.randomNumber(0,15);
    float random = (float) ((100 - randNo)/100.0);

    // Force R value
    if (r != R_RANDOM && r >= 0.85 && r <= 1) {

      random = r;

    }

    //TODO: implement weather field (Done)
    float weather = (float) 1.0;

    // Sunny
    if (currentGS.weather.flagID == 1) {

      if (moveType.equals("fire")) { weather = (float) 0.5; }

      else if (moveType.equals("water")) { weather = (float) 1.5; }

    }

    // Raining
    else if (currentGS.weather.flagID == 2) {

      if (moveType.equals("fire")) { weather = (float) 1.5; }

      else if (moveType.equals("water")) { weather = (float) 0.5; }

    }

    int critModifier = currentGS.critModifier(currentGS, meta.crit_rate, targetPman, allowCrits, wasCrit);

    float stab = 1;

    if (user.types.toString().contains(moveType)) { stab = (float) 1.5; }

    // System.out.println("Target:");
    // System.out.println(targetPman.toString());

    float typeEffectiveness = user.multiplyer(selectedMove, targetPman);

    if (turnInfo != null) {

      if (wasCrit != null && wasCrit.get(0)) {
        turnInfo.add("Critical Hit!");
      }

      if (typeEffectiveness == 0.25 || typeEffectiveness == 0.5) {
        turnInfo.add("It was not very effective...");
      }
      else if (typeEffectiveness == 2.0 || typeEffectiveness == 4.0) {
        turnInfo.add("It was super effective!");
      }
      else if (typeEffectiveness == 0.0) {
        turnInfo.add("It doesn't affect " + targetPman.name + "...");
      }

    }

    float burn = 1;

    if (user.statusUnaffectedBySwitching.getStatusList().get(3).getTurnsRemaining() != -1) { burn = (float) 0.5; }

    // System.out.println(weather);
    // System.out.println(critModifier);
    // System.out.println(random);
    // System.out.println(stab);
    // System.out.println(typeEffectiveness);
    // System.out.println(burn);

    // Other effects

    float other = 1;

    Field targetField = currentGS.getTargetField(currentGS, targetPman);

    // Physical
    if (meta.dmgType.equals("physical")) {

      if (targetField.activeFieldEffects.get(2).turnsRemaining > -1) {

        other = other * (float) 0.5;

      }

    }

    // Special
    else if (meta.dmgType.equals("special")) {

      if (targetField.activeFieldEffects.get(1).turnsRemaining > -1) {

        other = other * (float) 0.5;

      }

    }

    // Possibly round to 2dp
    return weather * critModifier * random * stab * typeEffectiveness * burn * other;

  }

  public static Boolean isDamaging(int moveCategoryID) {

    if (moveCategoryID == 0 || moveCategoryID == 4 || moveCategoryID == 6 || moveCategoryID == 7 || moveCategoryID == 8) {
      return true;
    }

    return false;

  }

  public static Boolean isHealing(int moveCategoryID) {

    if (moveCategoryID == 3) {
      return true;
    }

    return false;

  }

  // TODO: Provide currentGS, if lucky chant active on damage target's side, return 1
  // Takes a crit chance and returns the appropriate modifier depending on whether
  // it was found to crit or not. Level increase doubles crit chance
  public int critModifier(GameState currentGS, int moveCritRateLevel, Pokemon target, Boolean allowCrits, ArrayList<Boolean> wasCrit) {

    if (!allowCrits) { return 1; }

    Field targetField = currentGS.getTargetField(currentGS, target);

    if (targetField.activeFieldEffects.get(5).turnsRemaining > -1) {

      System.out.println("Lucky Chant prevented crit.");
      return 1;

    }

    double critChance = 1.0/16;

    double critRate = critChance * (2 * moveCritRateLevel);

    if (critRate == 0) {

      critRate = 1.0/16;

    }

    // 0-100
    int randNo = UtilityFunctions.randomNumber(0,100);

    // System.out.println(critRate * 100);
    // System.out.println(randNo);

    if (randNo <= critRate * 100) {

      if (wasCrit != null) {
        wasCrit.clear();
        wasCrit.add(true);
      }
      return 2;

    }

    else {
      if (wasCrit != null) {
        wasCrit.clear();
        wasCrit.add(false);
      }
      return 1;
    }

  }

  // 0 = user, 1 = opponent
  public static int targetIDConverter(int targetTypeID) {

    // Not implemented yet or move should fail in singles.
    if (targetTypeID < 4) {

      return -1;

    }

    // Implementing.
    if (targetTypeID == 4) { return 0; }

    // Implementing.
    if (targetTypeID == 6) { return 1; }

    // Can be implemented later, possibly use for weather or not at all.
    if (targetTypeID == 12) {

      // Maybe use new value, but might cause issues in applyMove.
      return 0;

    }

    // Target becomes just the user
    if (targetTypeID == 5 || targetTypeID == 7 || targetTypeID == 13) { return 0; }

    // Target becomes just the opponent
    if (targetTypeID == 8 || targetTypeID == 9 || targetTypeID == 10 || targetTypeID == 11) { return 1; }

    // Missed cases
    return -1;

  }
  // Returns at least 1, unless special case - 0 used as error value.
  // Assume that the stat changes occur after the damage is dealt.
  public int damage(GameState currentGS, Pokemon user, Move selectedMove, String dmgType, Pokemon targetPman, int printMode) {

    MoveMeta data = selectedMove.getMoveMetaData();

    int power = data.power;

    int userEffectiveAtkStat;
    int targetEffectiveDefStat;

    int baseMoveDamage = 0;

    if (power == 0) {

      System.out.println("Move not implemented yet");
      return 0;

    }

    else {

      userEffectiveAtkStat = currentGS.getEffectiveAttackStat(user, dmgType);
      targetEffectiveDefStat = currentGS.getEffectiveDefenseStat(targetPman, dmgType, currentGS);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("(EA: " + userEffectiveAtkStat + " ED: "+ targetEffectiveDefStat + ")");
      }

      // System.out.println(userEffectiveAtkStat);
      // System.out.println(targetEffectiveDefStat);

      // Truncating div
       int levelCalc = ((2 * user.level) / 5 + 2);
       // Truncating div
       int numerator = (levelCalc * power * userEffectiveAtkStat/targetEffectiveDefStat);

       baseMoveDamage = (numerator / 50 + 2);

    }

    return baseMoveDamage;

  }

  // // Used to estimate opponent's stats
  // // Currently does not work well with multi-hit moves
  // public static float damageToAtkStat(int damageRecieved, GameState currentGS, Pokemon user, Move selectedMove, String dmgType, Pokemon targetPman, int printMode) {
  //
  //   MoveMeta meta = selectedMove.getMoveMetaData();
  //
  //   String damageType = meta.dmgType;
  //
  //   if (!damageType.equals("status")) {}
  //
  //   return 0;
  //
  // }
  //
  // public static float damageToDefStat(int damageDealt, GameState currentGS, Pokemon user, Move selectedMove, String dmgType, Pokemon targetPman, int printMode) {
  //
  //   // if (!damageType.equals("status")) {}
  //
  //   return 0;
  //
  // }

  public static float statStageToMultiplier(int stage) {

    if (stage > 6) { stage = 6; }
    if (stage < -6) { stage = -6; }

    switch (stage) {

      case 0:
        return 1;
      case 1:
        return (float) 1.5;
      case 2:
        return 2;
      case 3:
        return (float) 2.5;
      case 4:
        return 3;
      case 5:
        return (float) 3.5;
      case 6:
        return 4;
      case -1:
        return (float) 0.66;
      case -2:
        return (float) 0.5;
      case -3:
        return (float) 0.4;
      case -4:
        return (float) 0.33;
      case -5:
        return (float) 0.285;
      case -6:
        return (float) 0.25;
      default:
        return 1;

    }

  }

  // Requires u acc - t eva first
  public static float accuracyAndEvasionStageToMultiplier(int stage) {

    if (stage > 6) { stage = 6; }
    if (stage < -6) { stage = -6; }

    switch (stage) {

      case 0:
        return 1;
      case 1:
        return (float) 1.33;
      case 2:
        return (float) 1.66;
      case 3:
        return 2;
      case 4:
        return (float) 2.33;
      case 5:
        return (float) 2.66;
      case 6:
        return 3;
      case -1:
        return (float) 0.75;
      case -2:
        return (float) 0.6;
      case -3:
        return (float) 0.5;
      case -4:
        return (float) 0.42;
      case -5:
        return (float) 0.37;
      case -6:
        return (float) 0.33;
      default:
        return 1;

    }

  }

  // Returns either player1CurrentPokemon or player2's.
  public Pokemon getTargetPokemon(GameState currentGS, Pokemon user, int target) {

    Pokemon targetPman = null;

    if (target == 0) {

      if (user.equals(currentGS.player1CurrentPokemon)) {
        targetPman = currentGS.player1CurrentPokemon;
      }
      else { targetPman = currentGS.player2CurrentPokemon; }

    }

    else if (target == 1) {

      if (user.equals(currentGS.player1CurrentPokemon)) {
        targetPman = currentGS.player2CurrentPokemon;
      }
      else { targetPman = currentGS.player1CurrentPokemon; }

    }

    else {

      System.out.println("Move not implemented yet");
      return null;

    }

    return targetPman;

  }

  // Works out whether the stat changes of the move apply to the user or target.
  public Pokemon getStatChangeAndAilmentTargetPokemon(GameState currentGS, Pokemon currentTarget, int moveCategoryID) {

    // damage+raise
    if (moveCategoryID == 7) {
      // System.out.println("Here");
      if (currentTarget.equals(currentGS.player1CurrentPokemon)) {
        return currentGS.player2CurrentPokemon;
      }
      else { return currentGS.player1CurrentPokemon; }

    }

    return currentTarget;

  }

  public int getEffectiveAttackStat(Pokemon pman, String damageType) {

    int intToReturn = 1;

    int applicableAtkStat;
    int atkLevel;

    if (damageType.equals("physical")) {

      applicableAtkStat = pman.stats.get(1).getStatValue();
      atkLevel = pman.statChangeLevels.get(0).getStatChangeLevel();

    }

    else if (damageType.equals("special")) {

      applicableAtkStat = pman.stats.get(3).getStatValue();
      atkLevel = pman.statChangeLevels.get(2).getStatChangeLevel();

    }

    else {

      System.out.println("Expected physical or special move.");
      return 0;

    }

    float levelMultiplyer = GameState.statStageToMultiplier(atkLevel);

    intToReturn = (int) (applicableAtkStat * levelMultiplyer);

    if (intToReturn < 1) { intToReturn = 1; }

    return intToReturn;

  }

  public int getEffectiveDefenseStat(Pokemon pman, String damageType, GameState currentGS) {

    int intToReturn = 1;

    int applicableDefStat;
    int defLevel;

    if (damageType.equals("physical")) {

      applicableDefStat = pman.stats.get(2).getStatValue();
      defLevel = pman.statChangeLevels.get(1).getStatChangeLevel();

    }

    else if (damageType.equals("special")) {

      applicableDefStat = pman.stats.get(4).getStatValue();

      // Sandstorm
      if (currentGS.weather.flagID == 0 && pman.isType("rock")) {

        applicableDefStat = (int) (applicableDefStat * 1.5);

      }

      defLevel = pman.statChangeLevels.get(3).getStatChangeLevel();

    }

    else {

      System.out.println("Expected physical or special move.");
      return 0;

    }

    float levelMultiplyer = GameState.statStageToMultiplier(defLevel);

    intToReturn = (int) (applicableDefStat * levelMultiplyer);

    if (intToReturn < 1) { intToReturn = 1; }

    return intToReturn;

  }

  // Possibly use currentGS parameter
  public int getEffectiveSpeedStat(Pokemon pman) {

    int intToReturn = 1;

    int applicableSpeedStat;
    int spdLevel;


    applicableSpeedStat = pman.stats.get(5).getStatValue();
    spdLevel = pman.statChangeLevels.get(4).getStatChangeLevel();

    Field targetField = this.getTargetField(this, pman);

    // Tailwind
    if (targetField.activeFieldEffects.get(4).turnsRemaining != -1) {

      applicableSpeedStat = applicableSpeedStat * 2;

    }

    // Paralysis - 75% Reduction
    if (pman.statusUnaffectedBySwitching.getStatusList().get(0).turnsRemaining != -1) {
      applicableSpeedStat = (int) (applicableSpeedStat * 0.25);
    }

    float levelMultiplyer = GameState.statStageToMultiplier(spdLevel);

    intToReturn = (int) (applicableSpeedStat * levelMultiplyer);

    if (intToReturn < 1) { intToReturn = 1; }

    return intToReturn;

  }

  // Return only the modifier used
  // Not currently used by getEffectiveXStat methods

  public float getAttackStatModifier(Pokemon pman, String damageType) {

    int atkLevel;

    if (damageType.equals("physical")) {
      atkLevel = pman.statChangeLevels.get(0).getStatChangeLevel();
    }
    else if (damageType.equals("special")) {
      atkLevel = pman.statChangeLevels.get(2).getStatChangeLevel();
    }
    else {
      System.out.println("Expected physical or special move.");
      return 0;
    }

    float levelMultiplyer = GameState.statStageToMultiplier(atkLevel);
    return levelMultiplyer;

  }

  public float getDefenseStatModifier(Pokemon pman, String damageType, GameState currentGS) {

    int defLevel;
    float levelMultiplyer;

    if (damageType.equals("physical")) {
      defLevel = pman.statChangeLevels.get(1).getStatChangeLevel();
      levelMultiplyer = GameState.statStageToMultiplier(defLevel);
    }

    else if (damageType.equals("special")) {
      defLevel = pman.statChangeLevels.get(3).getStatChangeLevel();
      levelMultiplyer = GameState.statStageToMultiplier(defLevel);

      // Sandstorm
      if (currentGS.weather.flagID == 0 && pman.isType("rock")) {
        // Have to times this final value not the defLevel
        levelMultiplyer = levelMultiplyer * (float)1.5;
      }

    }

    else {
      System.out.println("Expected physical or special move.");
      return 0;
    }

    return levelMultiplyer;

  }

  public float getSpeedStatModifier(Pokemon pman) {

    int spdLevel;

    spdLevel = pman.statChangeLevels.get(4).getStatChangeLevel();
    float levelMultiplyer = GameState.statStageToMultiplier(spdLevel);

    Field targetField = this.getTargetField(this, pman);

    // Tailwind
    if (targetField.activeFieldEffects.get(4).turnsRemaining != -1) {
      levelMultiplyer = levelMultiplyer * 2;
    }

    return levelMultiplyer;

  }

  // True if miss occurred.
  // Could pass a Move object. Can make exceptions occur here.
  // Exceptions: Foresight active, target in semi-vul turn
  public Boolean didMoveMiss(Pokemon user, Pokemon targetPman, int accuracy, GameState currentGS, Move selectedMove) {

    int moveID = selectedMove.moveID;

    Weather currentWeather = currentGS.weather;

    // Sandstorm has no accuracy effects

    // Rain
    if (currentWeather.flagID == 1) {

      // Hurricane not unbanned at the moment
      if (moveID == 87 || moveID == 542) {

        accuracy = 0;

      }

    }

    // Sunny
    else if (currentWeather.flagID == 2) {

      // Hurricane not unbanned at the moment
      if (moveID == 87 || moveID == 542) {

        accuracy = 50;

      }

    }

    // Hail
    else if (currentWeather.flagID == 3) {

      if (moveID == 59) {

        accuracy = 0;

      }

    }

    // Cases where accuracy was originally null, meaning it always hits.
    if (accuracy == 0) {

      return false;

    }

    int userAccuracyStage = user.statChangeLevels.get(5).getStatChangeLevel();
    int targetEvasionStage = targetPman.statChangeLevels.get(6).getStatChangeLevel();

    int netAccEvaLevel = userAccuracyStage - targetEvasionStage;

    // Called function applies accuracy multipler caps.
    float accuracyMultiplyer = GameState.accuracyAndEvasionStageToMultiplier(netAccEvaLevel);
    int effectiveAccuracy = (int) (accuracyMultiplyer * accuracy);

    int randomNo = UtilityFunctions.randomNumber(0,100);

    // System.out.println(effectiveAccuracy);
    // System.out.println(randomNo);

    if (randomNo > effectiveAccuracy) {

      return true;

    }

    return false;

  }

  public GameState applyHazard(GameState currentGS, Pokemon user, Move moveUsed, int targetID, int printMode, ArrayList<String> turnInfo) {

    Field targetField = null;

    targetField = currentGS.getTargetField(currentGS, user, targetID);

    // Do move stuff

    int moveID = moveUsed.moveID;

    // Only stealth rock cannot be used more than once.
    if (moveID == 446 && targetField.entraceHazards.get(Field.getHazardFlagIndex(moveID)).turnsRemaining > 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move failed (already in place)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed (already in place)");
      }
      return currentGS;

    }

    if (moveID == 191 && targetField.entraceHazards.get(Field.getHazardFlagIndex(moveID)).turnsRemaining == 3) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move failed (max spikes already in place)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed (max spikes already in place)");
      }
      return currentGS;

    }

    // Unlimited poison spike use
    targetField.entraceHazards.get(Field.getHazardFlagIndex(moveID)).turnsRemaining += 1;

    return currentGS;

  }

  public GameState applyFieldEffect(GameState currentGS, Pokemon user, Move moveUsed, int targetID, int printMode, ArrayList<String> turnInfo) {

    Field targetField = null;

    targetField = currentGS.getTargetField(currentGS, user, targetID);

    // Do move stuff

    int moveID = moveUsed.moveID;

    if (targetField.activeFieldEffects.get(Field.getFieldEffectFlagIndex(moveID)).turnsRemaining > -1) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move failed (already in place)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed (already in place)");
      }
      return currentGS;

    }

    targetField.activeFieldEffects.get(Field.getFieldEffectFlagIndex(moveID)).turnsRemaining = 5;

    return currentGS;

  }

  public GameState applyWeather(GameState currentGS, Pokemon user, Move moveUsed, int printMode, ArrayList<String> turnInfo) {

    // Do move stuff

    int moveID = moveUsed.moveID;

    Weather currentWeather = currentGS.weather;

    int newWeatherID = Weather.getWeatherFlagID(moveID);

    if (currentWeather.isWeatherTypeActive(newWeatherID)) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Move failed (weather already in place)");
      }
      if (turnInfo != null) {
        turnInfo.add("Move failed, this type of weather is already active");
      }
      return currentGS;

    }

    // Purely for turn info collection
    if (turnInfo != null) {

      switch (newWeatherID) {
        case 0:
          turnInfo.add("A sandstorm brewed.");
          break;
        case 1:
          turnInfo.add("It started to rain.");
          break;
        case 2:
          turnInfo.add("The sunlight turned harsh.");
          break;
        case 3:
          turnInfo.add("It started to hail.");
          break;
        default:

      }

    }

    currentWeather.setWeather(newWeatherID, 5);

    return currentGS;

  }

  public Field getTargetField(GameState currentGS, Pokemon user, int target) {

    Field targetField = null;

    if (target == 0) {

      if (user.equals(currentGS.player1CurrentPokemon)) {
        targetField = currentGS.player1Field;
      }
      else { targetField = currentGS.player2Field; }

    }

    else if (target == 1) {

      if (user.equals(currentGS.player1CurrentPokemon)) {
        targetField = currentGS.player2Field;
      }
      else { targetField = currentGS.player1Field; }

    }

    else {

      System.out.println("Move not implemented yet (shouldn't reach this)");
      return null;

    }

    return targetField;


  }

  public Field getTargetField(GameState currentGS, Pokemon targetPman) {

    Field targetField = null;

    if (targetPman.equals(currentGS.player1CurrentPokemon)) {
      targetField = currentGS.player1Field;
    }
    else { targetField = currentGS.player2Field; }

    return targetField;


  }

  // Returns 1 if player1CurrentPokemon is faster, 2 otherwise
  public int whichPokemonIsFaster(GameState currentGS, int playerBias) {

    int player1Speed = currentGS.getEffectiveSpeedStat(currentGS.player1CurrentPokemon);
    int player2Speed = currentGS.getEffectiveSpeedStat(currentGS.player2CurrentPokemon);

    if (player1Speed > player2Speed) {

      return 1;

    }

    else if (player1Speed < player2Speed) {

      return 2;

    }

    else {

      if (playerBias == 1 || playerBias == 2) { return playerBias; }

      int decider = UtilityFunctions.randomNumber(1,2);

      return decider;

    }

  }

  public GameState switchPokemonOut(GameState currentGS, Pokemon pmanToSwitch, int printMode) {

    // TODO: Check status which may prevent this. (Would need to include
    //       charge moves if implemented)

    // TODO: reset stat changes on switch out

    // Check pman is not trapped (cannot apply if pman has fainted)
    if (pmanToSwitch.currentHealth > 0 && pmanToSwitch.statusAffectedBySwitching.getStatusList().get(2).turnsRemaining != -1) {
      if (printMode == PRINTING_ENABLED) {
        System.out.println("Pokemon cannot switch out");
      }
      return currentGS;
    }

    // TODO: Remove status which disappear on switch.

    // Reset all temp status and poison to 100 if > 1

    pmanToSwitch.statusUnaffectedBySwitching.resetPoison();
    pmanToSwitch.statusAffectedBySwitching.resetStatus();

    // Reset stat changes
    pmanToSwitch.resetStatChanges();

    if (pmanToSwitch.equals(currentGS.player1CurrentPokemon)) {
      currentGS.player1CurrentPokemon = null;
    }

    else { currentGS.player2CurrentPokemon = null; }

    return currentGS;

  }
                                  // GameState currentGS, Pokemon pmanToSwitch, int player
  public GameState switchPokemonIn(GameState currentGS, int player, int pmanIndex, ArrayList<String> turnInfo, int printMode) {

    if (pmanIndex > 5) { pmanIndex = 5; }
    if (pmanIndex < 0) { pmanIndex = 0; }

    Team team = null;
    Field field = null;

    if (player == 1) { team = currentGS.player1Team; field = currentGS.player1Field; }

    else { team = currentGS.player2Team; field = currentGS.player2Field; }

    if (pmanIndex >= team.size()) { pmanIndex = team.size() - 1; }

    Pokemon pmanToSwitchIn = team.get(pmanIndex);

    if (player == 1) { currentGS.player1CurrentPokemon = pmanToSwitchIn; }

    else { currentGS.player2CurrentPokemon = pmanToSwitchIn; }

    String pmanSwitchedInName = pmanToSwitchIn.name;

    int maxHP = pmanToSwitchIn.stats.get(0).getStatValue();
    float hpLossPercent = 0;
    int hpLoss = 1;

    // Stealth Rock
    if (field.entraceHazards.get(2).turnsRemaining > 0) {

      float vsRock = pmanToSwitchIn.multiplyer(6, pmanToSwitchIn);

      if (vsRock == 0.25) {hpLossPercent = (float) 0.03125;}
      else if (vsRock == 0.5) {hpLossPercent = (float) 0.0625;}
      else if (vsRock == 1.0) {hpLossPercent = (float) 0.125;}
      else if (vsRock == 2.0) {hpLossPercent = (float) 0.25;}
      else {hpLossPercent = (float) 0.5;}

      // System.out.println(hpLossPercent);

      hpLoss = (int) ( maxHP * hpLossPercent );

      pmanToSwitchIn.currentHealth = Math.max(0,pmanToSwitchIn.currentHealth - hpLoss);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Pointed stones dug into X's feet");
      }
      if (turnInfo != null) {
        turnInfo.add("Pointed stones dug into "+pmanSwitchedInName+".");
      }

    }

    // Spikes
    if (field.entraceHazards.get(0).turnsRemaining > 0 && !pmanToSwitchIn.isType("flying")) {

      switch (field.entraceHazards.get(0).turnsRemaining) {

        case 1:
          hpLossPercent = (float) 0.125;
          break;
        case 2:
          hpLossPercent = (float) 0.1667;
          break;
        default:
          hpLossPercent = (float) 0.25;

      }

      hpLoss = (int) ( maxHP * hpLossPercent );

      pmanToSwitchIn.currentHealth = Math.max(0,pmanToSwitchIn.currentHealth - hpLoss);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Spikes dug into X's feet");
      }
      if (turnInfo != null) {
        turnInfo.add("Spikes dug into "+pmanSwitchedInName+".");
      }

    }

    // Poison-Spikes (checks safeguard)
    if (field.entraceHazards.get(1).turnsRemaining > 0 && pmanToSwitchIn.statusUnaffectedBySwitching.isStatusAvailable() && !(pmanToSwitchIn.isType("flying") || pmanToSwitchIn.isType("poison") || pmanToSwitchIn.isType("steel")) && field.activeFieldEffects.get(3).turnsRemaining == -1) {

      if (field.entraceHazards.get(1).turnsRemaining == 1) {

        pmanToSwitchIn.statusUnaffectedBySwitching.getStatusList().get(4).turnsRemaining = 1;

        if (printMode == PRINTING_ENABLED) {
          System.out.println("X was poisoned by the toxic spikes");
        }
        if (turnInfo != null) {
          turnInfo.add(pmanSwitchedInName+" was poisoned by the toxic spikes.");
        }

      }

      else {

        pmanToSwitchIn.statusUnaffectedBySwitching.getStatusList().get(4).turnsRemaining = 100;

        if (printMode == PRINTING_ENABLED) {
          System.out.println("X was badly poisoned by the toxic spikes");
        }
        if (turnInfo != null) {
          turnInfo.add(pmanSwitchedInName+" was badly poisoned by the toxic spikes.");
        }

      }

    }

    // Do not want this happening, so return a null that cannot go unnoticed.
    if (pmanToSwitchIn == null) { return null; }

    return currentGS;

  }

  // Combines switchPokemonOut and switchPokemonIn
  // Note, pokemon switched in can faint. This will need to be checked by
  // anything that calls it.
  public GameState switchOut(GameState currentGS, int player, int pmanIndex, int printMode, ArrayList<String> turnInfo) {

    // Can check switch out was succesful by checking if currentPman == null

    Pokemon toSwitchOut = null;

    if (player == 1) { toSwitchOut = currentGS.player1CurrentPokemon; }
    else { toSwitchOut = currentGS.player2CurrentPokemon; }

    String switchedOutPmanName = toSwitchOut.name;

    currentGS.switchPokemonOut(currentGS, toSwitchOut, printMode);

    if (player == 1) {

      if (currentGS.player1CurrentPokemon != null) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon could not be switched out");
        }
        if (turnInfo != null) {
          turnInfo.add(switchedOutPmanName + " could not be switched out.");
        }
        return currentGS;

      }

    }

    else {

      if (currentGS.player2CurrentPokemon != null) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Pokemon could not be switched out");
        }
        if (turnInfo != null) {
          turnInfo.add(switchedOutPmanName + " could not be switched out.");
        }
        return currentGS;

      }

    }

    int sizeBefore = 0;
    if (turnInfo != null) {
      sizeBefore = turnInfo.size();
    }
    currentGS.switchPokemonIn(currentGS, player, pmanIndex, turnInfo, printMode);

    // System.out.println("Player " + player + " withdrew " + switchedOutPmanName + ".");

    String sentInPmanName = "";

    if (player == 1) { sentInPmanName = currentGS.player1CurrentPokemon.name; }
    else { sentInPmanName = currentGS.player2CurrentPokemon.name; }

    // System.out.println("Player " + player + " sent out " + sentInPmanName + ".");
    if (printMode == PRINTING_ENABLED) {
      System.out.println("(Player " + player + ") " + switchedOutPmanName + " --> " + sentInPmanName);
    }
    // Putting hazard messages after the switch ones with minimal
    // extra code that is run if turnInfo was null.
    if (turnInfo != null) {
      ArrayList<String> temp = new ArrayList<String>();
      while (sizeBefore < turnInfo.size()) {
        temp.add(turnInfo.remove(turnInfo.size()-1));
      }
      turnInfo.add(switchedOutPmanName + " was switched out.");
      turnInfo.add(sentInPmanName + " was switched in.");
      if (temp.size() > 0) {
        turnInfo.addAll(temp);
      }
    }

    return currentGS;

  }

  public ArrayList<Action> generateAllValidActions(GameState currentGS, int player) {

    Pokemon playerPman;
    Team playerTeam;

    ArrayList<Action> actions = new ArrayList<Action>();

    if (player == 1) {

      playerPman = currentGS.player1CurrentPokemon;
      playerTeam = currentGS.player1Team;

    }

    else {

      playerPman = currentGS.player2CurrentPokemon;
      playerTeam = currentGS.player2Team;

    }

    // Moves
    for (int i = 0; i < 4; i++) {

      // Only add moves which are not null
      if (playerPman.moves.get(i) == null) {

        continue;

      }

      actions.add(new Action("move",i, player));

    }

    int size = playerTeam.size();

    // Switches
    // TODO: do not count if user is trapped
    for (int i = 0; i < size; i++) {

      if (playerTeam.get(i).currentHealth > 0 && !playerTeam.get(i).equals(playerPman)) {

        actions.add(new Action("switch",i, player));

      }

    }

    return actions;

  }

  public ArrayList<Action> generateAllValidSwitchActions(GameState currentGS, int player) {

    Pokemon playerPman;
    Team playerTeam;

    ArrayList<Action> actions = new ArrayList<Action>();

    if (player == 1) {

      playerPman = currentGS.player1CurrentPokemon;
      playerTeam = currentGS.player1Team;

    }

    else {

      playerPman = currentGS.player2CurrentPokemon;
      playerTeam = currentGS.player2Team;

    }

    int size = playerTeam.size();

    // Switches (ignores the user being trapped)
    for (int i = 0; i < size; i++) {

      if (playerTeam.get(i).currentHealth > 0 && !playerTeam.get(i).equals(playerPman)) {

        actions.add(new Action("switch",i, player));

      }

    }

    return actions;

  }

  public ArrayList<Action> whichActionToApplyFirst(GameState currentGS, Action player1Action, Action player2Action) {

    ArrayList<Action> listToReturn = new ArrayList<Action>();

    Pokemon player1Pman = currentGS.player1CurrentPokemon;
    Pokemon player2Pman = currentGS.player2CurrentPokemon;

    int p1Speed = currentGS.getEffectiveSpeedStat(player1Pman);
    int p2Speed = currentGS.getEffectiveSpeedStat(player2Pman);

    int randNo = 1;

    if (player1Action == null || player2Action == null) {

      System.out.println("Battle should be ending");
      return null;

    }

    // Could also consider pursuit
    if (player1Action.typeOfAction.equals("switch") && player2Action.typeOfAction.equals("switch")) {

      if (p1Speed > p2Speed) {

        listToReturn.add(player1Action);
        listToReturn.add(player2Action);

      }

      else if (p2Speed > p1Speed) {

        listToReturn.add(player2Action);
        listToReturn.add(player1Action);

      }

      // Tie
      else {

        randNo = UtilityFunctions.randomNumber(1,2);

        if (randNo == 1) {

          listToReturn.add(player1Action);
          listToReturn.add(player2Action);

        }

        else {

          listToReturn.add(player2Action);
          listToReturn.add(player1Action);

        }

      }

    }

    else if (player1Action.typeOfAction.equals("switch")) {

      listToReturn.add(player1Action);
      listToReturn.add(player2Action);

    }

    else if (player2Action.typeOfAction.equals("switch")) {

      listToReturn.add(player2Action);
      listToReturn.add(player1Action);

    }

    // Both actions are moves
    else {

      Move player1Move = currentGS.player1CurrentPokemon.moves.get(player1Action.indexOfAction);
      Move player2Move = currentGS.player2CurrentPokemon.moves.get(player2Action.indexOfAction);

      // TODO: Problems if playerMeta = null;

      MoveMeta player1Meta = player1Move.metaData;
      MoveMeta player2Meta = player2Move.metaData;

      int player1Priority = player1Meta.priority;
      int player2Priority = player2Meta.priority;

      if (player1Priority > player2Priority) {

        listToReturn.add(player1Action);
        listToReturn.add(player2Action);

      }

      else if (player2Priority > player1Priority) {

        listToReturn.add(player2Action);
        listToReturn.add(player1Action);

      }

      // Priority tie
      else {

        if (p1Speed > p2Speed) {

          listToReturn.add(player1Action);
          listToReturn.add(player2Action);

        }

        else if (p2Speed > p1Speed) {

          listToReturn.add(player2Action);
          listToReturn.add(player1Action);

        }

        // Tie
        else {

          randNo = UtilityFunctions.randomNumber(1,2);

          if (randNo == 1) {

            listToReturn.add(player1Action);
            listToReturn.add(player2Action);

          }

          else {

            listToReturn.add(player2Action);
            listToReturn.add(player1Action);

          }

        }

      }

    }

    return listToReturn;

  }

  public String humanReadableStateInfo() {

    String stringToReturn = "";

    stringToReturn += "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + "\t\t\t\t\t\t\t\t\t\t\t\tWeather: " + this.weather.toString() + "\n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + "Player 1 \t\t\t\t\t\t\t\t\t\t\t\t\tPlayer 2\n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + this.player1Field.hazardsToString() + "\t\t\t\t\t\t\t\t\t\t" + this.player2Field.hazardsToString() + "\n"
                    + this.player1Field.activeEffectsToString() + "\t\t\t\t\t\t\t" + this.player2Field.activeEffectsToString() + "\n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + "Pokemon: " + player1CurrentPokemon.name + "\t\t\t\t\t\t\t\t\t\t\t\t" + "Pokemon: " + player2CurrentPokemon.name + "\n"
                    + "HP: " + this.player1CurrentPokemon.currentHealth + "/" + this.player1CurrentPokemon.stats.get(0).getStatValue() + "\t\t\t\t\t\t\t\t\t\t\t\t\t" + "HP: " + this.player2CurrentPokemon.currentHealth + "/" + this.player2CurrentPokemon.stats.get(0).getStatValue() + "\n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n"
                    + this.player1CurrentPokemon.statChangeLevels.toString() + "\t" + this.player2CurrentPokemon.statChangeLevels.toString() + "\n"
                    + this.player1CurrentPokemon.statusUnaffectedBySwitching.toString() + "\t\t\t\t\t\t\t\t\t\t" + this.player2CurrentPokemon.statusUnaffectedBySwitching.toString() + "\n"
                    + this.player1CurrentPokemon.statusAffectedBySwitching.toString() + "\t\t\t\t\t\t\t" + this.player2CurrentPokemon.statusAffectedBySwitching.toString() + "\n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";


    return stringToReturn;

  }

  // Seems to be pretty quick, 10000 takes less than half a second
  public GameState deepCopy() {

    /*
    Completely new objects for:

    Each Pokemon in both teams
    Each team
    Each field
    Weather

    */

    GameState copy = new GameState();

    copy.turnCount = this.turnCount;

    copy.player1Team = this.player1Team.deepCopy();
    copy.player2Team = this.player2Team.deepCopy();

    // So that currentPman are pointing to the same stuff as the team list.
    int indexOfP1Current = this.player1Team.indexOf(this.player1CurrentPokemon);
    int indexOfP2Current = this.player2Team.indexOf(this.player2CurrentPokemon);

    copy.player1CurrentPokemon = copy.player1Team.get(indexOfP1Current);
    copy.player2CurrentPokemon = copy.player2Team.get(indexOfP2Current);

    copy.player1Field = this.player1Field.deepCopy();
    copy.player2Field = this.player2Field.deepCopy();

    copy.weather = this.weather.deepCopy();

    return copy;

  }

  public float hitChance(GameState currentGS, Pokemon user, Move selectedMove) {

    MoveMeta meta = selectedMove.getMoveMetaData();

    int moveCategoryID = meta.moveCategoryID;
    String damageType = meta.dmgType;

    int accuracy = meta.accuracy;
    // Possibly use extra converter values, eg 2 == user field.
    int target = GameState.targetIDConverter(meta.targetTypeID);
    Pokemon targetPman = currentGS.getTargetPokemon(currentGS, user, target);

    if (user.currentHealth <= 0 || targetPman.currentHealth <= 0) {

      return 0;

    }

    // Non-effective move

    if (GameState.isDamaging(moveCategoryID)) {
      // Allowing crits does not change the meaning of this check
      float modifier = currentGS.calcMoveDamageModifier(currentGS,user,selectedMove,targetPman,R_RANDOM,true,null,null);

      if (modifier == 0) {

        return 0;

      }

    }

    int moveID = selectedMove.moveID;

    Weather currentWeather = currentGS.weather;

    // Sandstorm has no accuracy effects

    // Rain
    if (currentWeather.flagID == 1) {

      // Hurricane not unbanned at the moment
      if (moveID == 87 || moveID == 542) {

        accuracy = 0;

      }

    }

    // Sunny
    else if (currentWeather.flagID == 2) {

      // Hurricane not unbanned at the moment
      if (moveID == 87 || moveID == 542) {

        accuracy = 50;

      }

    }

    // Hail
    else if (currentWeather.flagID == 3) {

      if (moveID == 59) {

        accuracy = 0;

      }

    }

    // Cases where accuracy was originally null, meaning it always hits.
    if (accuracy == 0) {

      return 1;

    }

    int userAccuracyStage = user.statChangeLevels.get(5).getStatChangeLevel();
    int targetEvasionStage = targetPman.statChangeLevels.get(6).getStatChangeLevel();

    int netAccEvaLevel = userAccuracyStage - targetEvasionStage;

    // Called function applies accuracy multipler caps.
    float accuracyMultiplyer = GameState.accuracyAndEvasionStageToMultiplier(netAccEvaLevel);
    int effectiveAccuracy = (int) (accuracyMultiplyer * accuracy);

    return effectiveAccuracy / (float) 100.0;

  }

  // [0] = moveFailChance
  // [1] = moveMissChance
  // [2] = move with no effect proc
  // [3] = move hits and effect

  // 0 + (123) = 1
  // 1 + 2 + 3 = 1

  public float[] chanceOfMoveOutcomes(GameState currentGS, Pokemon user, Move selectedMove) {

    float moveSuccess = user.probabilityOfUsingMove();
    // float moveFailChance = 1 - moveSuccess;
    float hitChance = currentGS.hitChance(currentGS, user, selectedMove);
    float moveMissChance = 1 - hitChance;

    if (moveSuccess == 0) {

      return new float[] {1,0,0,0};

    }

    if (hitChance == 0) {

      return new float[] {0,1,0,0};

    }

    moveMissChance = moveMissChance * moveSuccess;

    float moveHitsChance = hitChance * moveSuccess;

    // Changed from ailment_chance to effect_chance
    float effectChance = selectedMove.metaData.effectChance / (float) 100.0;

    float hitNoAilment = moveHitsChance * (1 - effectChance);
    float hitAndAilment = moveHitsChance * (effectChance);

    // System.out.println(hitNoAilment + "," + hitAndAilment);

    // float total = moveFailChance + moveMissChance + moveHitsChance;

    // if (total != 1) {
    //   System.exit(0);
    // }

    // Add up values and divide by the sum

    // if ((1 - moveSuccess) + moveMissChance + hitNoAilment + hitAndAilment == 0) {
    //
    //   System.exit(0);
    //
    // }

    return new float[] {1 - moveSuccess, moveMissChance, hitNoAilment, hitAndAilment};

  }

  // F = 0: Move Fails
  // F = 1: Move Misses
  // F = 2: Move hits with no ailment bonus
  // F = 3: Move hits with ailment bonus
  // public GameState applyMoveWithFlag(GameState currentGS, Pokemon user, Move selectedMove, int forceFlag) {
  //
  //   // TODO: Check status evaluation ordering.
  //   // TODO: Moves need to fail if the TARGET has 0 HP
  //
  //   // System.out.println("Chance of move succeeding: " + user.probabilityOfUsingMove());
  //   // System.out.println("Chance of move hitting: " + currentGS.hitChance(currentGS, user, selectedMove));
  //   // System.out.println(Arrays.toString(currentGS.chanceOfMoveOutcomes(currentGS, user, selectedMove)));
  //
  //   ArrayList<Flag> pStatusFlags = user.statusUnaffectedBySwitching.getStatusList();
  //   ArrayList<Flag> tStatusFlags = user.statusAffectedBySwitching.getStatusList();
  //
  //   int randNo = 1;
  //
  //   // Do not want to force anything yet since the status effects might need to be
  //   // decremented.
  //
  //   if (user.currentHealth <= 0) {
  //
  //     // System.out.println("(User has fainted)");
  //     return currentGS;
  //
  //   }
  //
  //   // Do want move to fail now, so bypass status
  //
  //   // id = 1 but list index = 0.
  //   // Paralysis
  //   if (pStatusFlags.get(0).getTurnsRemaining() != -1) {
  //
  //     // Numbers between 1 and 4
  //     randNo = UtilityFunctions.randomNumber(1,4);
  //
  //     if (randNo == 1) {
  //
  //       // System.out.println("Pokemon paralysed.");
  //
  //        if (forceFlag == 0) { return currentGS; }
  //
  //       // System.out.println("Paralysis overturned by flag");
  //
  //     }
  //
  //   }
  //
  //   // No need to force anything here, but we do need to make sure it is bypassed
  //   // by other flags
  //
  //   // + 1 to allow idenfification of awoken pokemon
  //   // Sleep - Turns choosen when ailment applied. Does not reset when switching.
  //   else if (pStatusFlags.get(1).getTurnsRemaining() > -1) {
  //
  //     if (pStatusFlags.get(1).getTurnsRemaining() == 0) {
  //
  //       // System.out.println("X woke up.");
  //       pStatusFlags.get(1).turnsRemaining = pStatusFlags.get(1).getTurnsRemaining() - 1;
  //       // Setting nightmare up;
  //       tStatusFlags.get(3).turnsRemaining = -1;
  //
  //     } else {
  //
  //       // System.out.println("Pokemon is asleep.");
  //       pStatusFlags.get(1).turnsRemaining = pStatusFlags.get(1).getTurnsRemaining() - 1;
  //
  //       // Only applies when forced
  //       if (forceFlag == 0) { return currentGS; }
  //
  //       // System.out.println("(Flag caused sleep to be overturned)");
  //
  //     }
  //
  //   }
  //
  //   // Frozen - 20% chance to unfreeze each turn
  //   else if (pStatusFlags.get(2).getTurnsRemaining() != -1) {
  //
  //     randNo = UtilityFunctions.randomNumber(0,4);
  //
  //     if (randNo != 0) {
  //
  //       // System.out.println("Pokemon is frozen.");
  //
  //       // Only applies when forced
  //       if (forceFlag == 0) { return currentGS; }
  //
  //       // System.out.println("(Flag caused freeze to be overturned)");
  //
  //     }
  //
  //     else {
  //
  //       // System.out.println("X thawed out.");
  //       pStatusFlags.get(2).turnsRemaining = -1;
  //
  //     }
  //
  //   }
  //
  //   // Burn skipped
  //   // Poison skipped
  //
  //   // Now the lesser status effects - using ifs not else ifs since multiple status
  //   // conditions can apply.
  //
  //   // Confusion: 33% chance
  //   if (tStatusFlags.get(0).getTurnsRemaining() != -1) {
  //
  //     if (tStatusFlags.get(0).getTurnsRemaining() != 0) {
  //
  //       // System.out.println("Pokemon is confused.");
  //
  //       randNo = UtilityFunctions.randomNumber(0,2);
  //
  //       if (randNo == 0) {
  //
  //         // System.out.println("Pokemon hurt itself in its confusion.");
  //
  //         tStatusFlags.get(0).turnsRemaining = tStatusFlags.get(0).getTurnsRemaining() - 1;
  //
  //         // Only applies when forced
  //         if (forceFlag == 0) {
  //
  //           // TODO: Damage calc for this hit.
  //           return currentGS;
  //
  //         }
  //
  //         // System.out.println("(Flag caused confusion to be overturned)");
  //
  //       }
  //
  //       else {
  //
  //         tStatusFlags.get(0).turnsRemaining = tStatusFlags.get(0).getTurnsRemaining() - 1;
  //
  //       }
  //
  //     }
  //
  //     else {
  //
  //       // System.out.println("Pokemon snapped out of confusion.");
  //       tStatusFlags.get(0).turnsRemaining = -1;
  //
  //     }
  //
  //
  //   }
  //
  //   // Love: Maybe do not implement this? Only goes away when opposing pokemon is switched out.
  //   if (tStatusFlags.get(1).getTurnsRemaining() != -1) {
  //
  //     // System.out.println("Pokemon is inlove with the foe.");
  //
  //     randNo = UtilityFunctions.randomNumber(0,1);
  //
  //     if (randNo == 1) {
  //
  //       // System.out.println("Pokemon is immobilized by love.");
  //
  //       if (forceFlag == 0) { return currentGS; }
  //
  //       // System.out.println("Immobilization overturned by flag");
  //
  //     }
  //
  //   }
  //
  //   // Skipped trap: Implement as 1/8 HP damage each turn. Possibly also not being
  //   // able to switch out.
  //
  //   // Skipped nightmare: Implement as 1/4 HP damage each turn, opponent must be sleeping (!= -1)
  //
  //   // Yawn occurs at the end of the turn.
  //
  //   // Forsight occurs during type calculation. Could use a GS check when the matchup
  //   // function is used. If int = 0 against ghost, int = 1.
  //
  //   // Skipped leechseed: Implement as 1/8 HP damage each turn.
  //
  //   // Check for flinch
  //
  //   if (tStatusFlags.get(7).turnsRemaining != -1) {
  //
  //     // System.out.println("User flinched.");
  //     // Just incase - reset at end of every tirn anyway.
  //     tStatusFlags.get(7).turnsRemaining = -1;
  //
  //     if (forceFlag == 0) { return currentGS; }
  //
  //     // System.out.println("Flinch overturned by flag");
  //
  //   }
  //
  //   MoveMeta meta = selectedMove.getMoveMetaData();
  //
  //   int moveCategoryID = meta.moveCategoryID;
  //   String damageType = meta.dmgType;
  //
  //   int accuracy = meta.accuracy;
  //
  //   // Possibly use extra converter values, eg 2 == user field.
  //   int target = GameState.targetIDConverter(meta.targetTypeID);
  //   Pokemon targetPman = currentGS.getTargetPokemon(currentGS, user, target);
  //
  //   // Need to check if move misses or not.
  //   // User acc stage - target eva stage
  //
  //   // Covers status as well
  //   if (targetPman.currentHealth <= 0) {
  //
  //     // System.out.println("Move failed (target has 0 HP)");
  //     return currentGS;
  //
  //   }
  //
  //   if (forceFlag == 1) {
  //
  //     // System.out.println("Move forced to miss.");
  //     return currentGS;
  //
  //   }
  //
  //   // Field moves here
  //
  //   int moveID = selectedMove.moveID;
  //
  //   // TODO: some way to indicate that these moves shouldn't be reused while active
  //
  //   if (Field.isHazard(moveID)) {
  //
  //     // System.out.println("Hazard");
  //     currentGS.applyHazard(currentGS, user, selectedMove, target, 0);
  //
  //   }
  //
  //   else if (Field.isFieldEffect(moveID)) {
  //
  //     // System.out.println("FieldEffect");
  //     // Safeguard applied and then ailment applied. Brings up "protected
  //     // by safeguard" message unnecessary.
  //     currentGS.applyFieldEffect(currentGS, user, selectedMove, target, 0);
  //
  //   }
  //
  //   else if (Weather.isWeather(moveID)) {
  //
  //     // System.out.println("Weather");
  //     currentGS.applyWeather(currentGS, user, selectedMove, 0);
  //
  //   }
  //
  //   else if (GameState.isHealing(moveCategoryID)) {
  //
  //     int baseHealingPercentage = meta.healing;
  //
  //     // Weather check to decrease this base
  //
  //     Weather currentWeather = currentGS.weather;
  //     int weatherID = currentWeather.flagID;
  //
  //     // Only applies to certain moves (moonlight, morning-sun, synthesis)
  //
  //     if (moveID >= 234 && moveID <= 236) {
  //
  //       switch (weatherID) {
  //
  //         case -1:
  //           // 50%
  //           break;
  //         case 2:
  //           baseHealingPercentage = 66;
  //           break;
  //         default:
  //           baseHealingPercentage = 25;
  //
  //       }
  //
  //     }
  //
  //     int amountToHeal = (int) ( ((float) baseHealingPercentage / 100.0) * user.stats.get(0).getStatValue() );
  //
  //     // System.out.println("Amount to heal: " + amountToHeal);
  //
  //     user.currentHealth = Math.min((user.currentHealth + amountToHeal),user.stats.get(0).getStatValue());
  //
  //   }
  //
  //
  //   int numberOfHits = UtilityFunctions.randomNumber(meta.min_hits,meta.max_hits);
  //
  //   do {
  //
  //     if (GameState.isDamaging(moveCategoryID)) {
  //
  //       // Making Modifier value
  //       float modifier = currentGS.calcMoveDamageModifier(currentGS,user,selectedMove,targetPman);
  //       if (modifier == -1) {
  //         // System.out.println("Move not implemented yet.");
  //         return currentGS;
  //       }
  //       // System.out.println(modifier);
  //
  //       if (!damageType.equals("status")) {
  //
  //         int damage = currentGS.damage(currentGS,user,selectedMove,damageType,targetPman,0);
  //
  //         damage = (int) (damage * modifier);
  //
  //         // Non-effective typing can lead to 0 dmg
  //         if (modifier == 0) {
  //
  //           // System.out.println("No effect");
  //           return currentGS;
  //
  //         }
  //
  //         if (damage < 1 && modifier != 0) { damage = 1; }
  //
  //         // System.out.println("Damage: "+ damage + " (modifier = " + modifier + ")");
  //
  //         // TODO: Take away damage from pman, for use in drain moves.
  //
  //         int totalHPRemoved = Math.min(damage,targetPman.currentHealth);
  //
  //         // System.out.println("HP Loss: " + totalHPRemoved);
  //
  //         targetPman.currentHealth -= totalHPRemoved;
  //
  //         // Instead of returning the gamestate if a pman's health is 0, still
  //         // need to apply everything else (eg user stat raises). Even though the
  //         // pman has fainted, this will not affect anything.
  //
  //         // Check if move is damaging and healing (drain move)
  //
  //         if (meta.drain != 0) {
  //
  //           // Can be negative or positive
  //           int healing = (int) (totalHPRemoved * ((float) (meta.drain / 100.0)));
  //
  //           // System.out.println("Healing: " + healing);
  //
  //           // TODO: Apply healing
  //
  //           if (healing >= 0) {
  //
  //             // Comparing currentHealth + healing to max health of pman
  //             user.currentHealth = Math.min((user.currentHealth + healing),user.stats.get(0).getStatValue());
  //
  //           }
  //
  //           else {
  //
  //             user.currentHealth = Math.max((user.currentHealth + healing),0);
  //
  //           }
  //
  //         }
  //
  //       }
  //
  //     }
  //
  //     numberOfHits--;
  //
  //   } while (numberOfHits > 0);
  //
  //
  //
  //   // System.out.println("Target: ");
  //   // System.out.println(targetPman.toString());
  //
  //   // Applying status chances and ailments.
  //
  //   // 1. Apply stat changes to TARGET or USER - currently always applies
  //   // stats to target.
  //   // targetTypeID = where the move hits, move-cat can tell who gets the buffs
  //   // damage+raise = damage + stat change on user
  //   // damage+lower = damage + stat change on target
  //
  //   Pokemon statAndAilmentTarget = currentGS.getStatChangeAndAilmentTargetPokemon(currentGS, targetPman, moveCategoryID);
  //
  //   // + Chance of stat change - could use effect chance or stat chance
  //   // Use stat_chance - eg 188
  //
  //   // 0 used to indicate it always happens if the move doesn't miss.
  //   if (meta.stat_chance == 0) {
  //
  //     statAndAilmentTarget.addToStatChangeLevels(currentGS, meta.stat_changes);
  //
  //   }
  //
  //   else {
  //
  //     // randNo = UtilityFunctions.randomNumber(0,100);
  //
  //     if (forceFlag == 3) {
  //
  //       statAndAilmentTarget.addToStatChangeLevels(currentGS, meta.stat_changes);
  //
  //     }
  //
  //   }
  //
  //   // Applying ailments
  //
  //   if (meta.ailment_chance == 0) {
  //
  //     statAndAilmentTarget.applyAilment(currentGS, selectedMove);
  //
  //   }
  //
  //   else {
  //
  //     // randNo = UtilityFunctions.randomNumber(0,100);
  //
  //     if (forceFlag == 3) {
  //
  //       statAndAilmentTarget.applyAilment(currentGS, selectedMove);
  //
  //     }
  //
  //   }
  //
  //   if (meta.flinch_chance != 0) {
  //
  //     randNo = UtilityFunctions.randomNumber(0,100);
  //
  //     if (forceFlag == 3) {
  //
  //       // Apply flinch status
  //
  //       statAndAilmentTarget.statusAffectedBySwitching.getStatusList().get(7).turnsRemaining = 1;
  //
  //     }
  //
  //   }
  //
  //   // System.out.println("Move used: " + selectedMove.moveName);
  //   // System.out.println("User: " + user.name + " (HP: " + user.currentHealth + ")");
  //   // System.out.println("Original Target: " + targetPman.name + " (HP: " + targetPman.currentHealth + ")");
  //   // System.out.println("HP: " + targetPman.currentHealth);
  //   // System.out.println(targetPman.statChangeLevels.toString());
  //   // System.out.println(targetPman.statusAffectedBySwitching.getStatusList().toString());
  //   // System.out.println(targetPman.statusUnaffectedBySwitching.getStatusList().toString());
  //   // System.out.println("Stat change target: " + statAndAilmentTarget.name + " (HP: " + statAndAilmentTarget.currentHealth + ")");
  //   // System.out.println("HP: " + statAndAilmentTarget.currentHealth);
  //   // System.out.println(statAndAilmentTarget.statChangeLevels.toString());
  //   // System.out.println(statAndAilmentTarget.statusUnaffectedBySwitching.getStatusList().toString());
  //   // System.out.println(statAndAilmentTarget.statusAffectedBySwitching.getStatusList().toString());
  //
  //   return currentGS;
  //
  // }

  // TODO:
  // Or type name
  public int weatherModifier(GameState currentGS, int typeID) { return 1; }

  public int otherModifier(GameState currentGS, Move moveUsed) { return 1; }

  public int score() { return 0; }

}
