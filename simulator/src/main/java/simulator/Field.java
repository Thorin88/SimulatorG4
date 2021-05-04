import java.util.*;

public class Field {

  // turnsRemaining used as a measure of how many times move has been used, eg spikes
  ArrayList<Flag> entraceHazards;
  // turnsRemaining used as normal
  ArrayList<Flag> activeFieldEffects; // (reflect n stuff)

  public Field() {

    this.entraceHazards = new ArrayList<Flag>();
    this.activeFieldEffects = new ArrayList<Flag>();

  }

  public void initialise() {

    // Spikes
    this.entraceHazards.add(new Flag(0,0));
    // Toxic Spikes - Badly poisoned if 2 or more
    this.entraceHazards.add(new Flag(1,0));
    // Stealth Rock
    this.entraceHazards.add(new Flag(2,0));


    // Mist - Used in apply stat changes
    this.activeFieldEffects.add(new Flag(0,-1));

    // Light Screen - Used in damage modifier
    this.activeFieldEffects.add(new Flag(1,-1));

    // Reflect - Used in damage modifier
    this.activeFieldEffects.add(new Flag(2,-1));

    // Safeguard - Used in apply ailment
    this.activeFieldEffects.add(new Flag(3,-1));

    // Tailwind - Used in priority calculation (only 4 turns)
    this.activeFieldEffects.add(new Flag(4,-1));

    // Lucky Chant - Used in critModifier
    this.activeFieldEffects.add(new Flag(5,-1));

  }

  public static Boolean isHazard(String moveName) {

    String moveList = "spikes,toxic-spikes,stealth-rock";

    if (moveName.equals("toxic")) { return false; }

    if (moveList.contains(moveName)) { return true; }

    return false;

  }

  public static Boolean isHazard(int moveID) {

    if (moveID == 191 || moveID == 390 || moveID == 446) { return true; }

    return false;

  }

  public static Boolean isFieldEffect(String moveName) {

    String moveList = "mist,light-screen,reflect,safeguard,tailwind,lucky-chant";

    if (moveList.contains(moveName)) { return true; }

    return false;

  }

  public static Boolean isFieldEffect(int moveID) {

    if (moveID == 54 || moveID == 113 || moveID == 115 || moveID == 219 || moveID == 366 || moveID == 381) { return true; }

    return false;

  }

  public static int getHazardFlagIndex(int moveID) {

    switch (moveID) {

      case 191:
        return 0;
      case 390:
        return 1;
      case 446:
        return 2;

    }

    return 0;

  }

  public static int getFieldEffectFlagIndex(int moveID) {

    switch (moveID) {

      case 54:
        return 0;
      case 113:
        return 1;
      case 115:
        return 2;
      case 219:
        return 3;
      case 366:
        return 4;
      case 381:
        return 5;

    }

    return 0;

  }

  public void endOfTurnDecrements(int player, int printMode) {

    int size = this.activeFieldEffects.size();
    Flag currentFlag = null;

    for (int i = 0; i < size; i++) {

      currentFlag = this.activeFieldEffects.get(i);

      if (currentFlag.turnsRemaining == 0) {

        // Need index to name function
        if (printMode == 1) {
          System.out.println("Player " + player + "'s X faded.");
        }

      }

      if (currentFlag.turnsRemaining != -1) {

        currentFlag.turnsRemaining -= 1;

      }

    }

  }

  public String hazardsToString() {

    String stringToReturn = "";

    int size = this.entraceHazards.size();

    stringToReturn += "Entrace Hazards: ";

    for (int i = 0; i < size; i++) {

      stringToReturn += this.entraceHazards.get(i).toString() + " ";

    }

    return stringToReturn;

  }

  public String activeEffectsToString() {

    String stringToReturn = "";

    int size = this.activeFieldEffects.size();

    stringToReturn += "Active Effects: ";

    for (int i = 0; i < size; i++) {

      stringToReturn += this.activeFieldEffects.get(i).toString() + " ";

    }

    return stringToReturn;

  }

  public Field deepCopy() {

    Field fieldToReturn = new Field();

    int size = this.entraceHazards.size();

    for (int i = 0; i < size; i++) {

      fieldToReturn.entraceHazards.add( this.entraceHazards.get(i).deepCopy() );

    }

    size = this.activeFieldEffects.size();

    for (int i = 0; i < size; i++) {

      fieldToReturn.activeFieldEffects.add( this.activeFieldEffects.get(i).deepCopy() );

    }

    return fieldToReturn;

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    int size = this.entraceHazards.size();

    stringToReturn += "Entrace Hazards: ";

    for (int i = 0; i < size; i++) {

      stringToReturn += this.entraceHazards.get(i).toString() + " ";

    }

    size = this.activeFieldEffects.size();

    stringToReturn += "Active Effects: ";

    for (int i = 0; i < size; i++) {

      stringToReturn += this.activeFieldEffects.get(i).toString() + " ";

    }

    return stringToReturn;

  }

}
