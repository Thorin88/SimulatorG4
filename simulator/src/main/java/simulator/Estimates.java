import java.util.*;

public class Estimates {

  int attacker;

  int estAtk;
  int estSpAtk;

  int estDef;
  int estSpDef;

  int estSpd;

  int totalHPRemoved;

  double rLow;
  double rHigh;

  float userAtkMulitplier;
  float targetDefMulitplier;

  int userActualEffectiveAtkStat;
  int targetActualEffectiveDefStat;

  int power;
  int levelCalc;

  public Estimates() {

    this.attacker = 1;

    this.estAtk = -1;
    this.estSpAtk = -1;
    this.estDef = -1;
    this.estSpDef = -1;
    this.estSpd = -1;

    this.totalHPRemoved = -1;

    this.rLow = -1;
    this.rHigh = -1;

    this.userAtkMulitplier = -1;
    this.targetDefMulitplier = -1;

    this.userActualEffectiveAtkStat = -1;
    this.targetActualEffectiveDefStat = -1;

    this.power = -1;
    this.levelCalc = -1;

  }

  public void addDefEstimate(int est, String damageType) {

    if (damageType.equals("physical")) {
      this.estDef = est;
    }

    else {
      this.estSpDef = est;
    }

  }

  public void addAtkEstimate(int est, String damageType) {

    if (damageType.equals("physical")) {
      this.estAtk = est;
    }

    else {
      this.estSpAtk = est;
    }

  }

  public void addSpdEstimate(int est) {

    this.estSpd = est;

  }

  public void setAttacker(int player) {

    this.attacker = player;

  }

  public void updateStats(Pokemon pman, int askingPlayer) {

    // Was hit, so update opponents atk stats
    if (askingPlayer != this.attacker) {
      if (this.estAtk != -1) {
        pman.stats.get(1).setStatValue(this.estAtk);
      }
      if (this.estSpAtk != -1) {
        pman.stats.get(3).setStatValue(this.estSpAtk);
      }
    }

    // Did the hitting, so update opponents def stats
    else {
      if (this.estDef != -1) {
        pman.stats.get(2).setStatValue(this.estDef);
      }
      if (this.estSpDef != -1) {
        pman.stats.get(4).setStatValue(this.estSpDef);
      }
    }

    if (this.estSpd != -1) {
      pman.stats.get(5).setStatValue(this.estSpd);
    }

  }

  @Override
  public String toString() {

    String stringToReturn = "";

    stringToReturn += "Estimated Atk: ";
    stringToReturn += estAtk + "\n";

    stringToReturn += "Estimated SpAtk: ";
    stringToReturn += estSpAtk + "\n";

    stringToReturn += "Estimated Def: ";
    stringToReturn += estDef + "\n";

    stringToReturn += "Estimated SpDef: ";
    stringToReturn += estSpDef + "\n";

    stringToReturn += "Estimated Spd: ";
    stringToReturn += estSpd + "\n";

    return stringToReturn;

  }

}
