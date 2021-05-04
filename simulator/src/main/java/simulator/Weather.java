import java.util.*;

public class Weather {

  int flagID;
  int turnsRemaining;

  public Weather() {

    this.flagID = -1;
    this.turnsRemaining = -1;

  }

  public void initialise() {

    // // Clear
    // this.weatherFlag = new Flag(-1,-1);
    //
    // // Sandstorm
    /*
      sp def affect in getEffectiveDefenseStat
    */
    // this.weatherFlags.add(new Flag(0,-1));
    //
    // // Rain
    /*
      power affect in calcMoveDamageModifier
    */
    // this.weatherFlags.add(new Flag(1,-1));
    //
    // // Sunny
    // this.weatherFlags.add(new Flag(2,-1));
    //
    // // Hail
    // this.weatherFlags.add(new Flag(3,-1));

  }

  public void setWeather(int id, int turns) {

    if (id < 0 || id > 3) {

      this.flagID = -1;
      this.turnsRemaining = -1;
      return;

    }

    this.flagID = id;
    this.turnsRemaining = turns;

  }

  public static Boolean isWeather(String moveName) {

    String moveList = "sandstorm,rain-dance,sunny-day,hail";

    if (moveList.contains(moveName)) { return true; }

    return false;

  }

  public static Boolean isWeather(int moveID) {

    if (moveID == 201 || moveID == 240 || moveID == 241 || moveID == 258) { return true; }

    return false;

  }

  public static int getWeatherFlagID(int moveID) {

    switch (moveID) {

      case 201:
        return 0;
      case 240:
        return 1;
      case 241:
        return 2;
      case 258:
        return 2;

    }

    // Clear - but function should be used only when move is known to be weather
    return -1;

  }

  // Could make a moveID parameter variant
  public Boolean isWeatherTypeActive(int weatherID) {

    if (this.flagID == weatherID) {

      return true;

    }

    return false;

  }

  public Weather deepCopy() {

    Weather toReturn = new Weather();
    toReturn.setWeather(this.flagID, this.turnsRemaining);

    return toReturn;

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    stringToReturn += "[" + this.flagID + "," + this.turnsRemaining + "]";

    return stringToReturn;

  }

}
