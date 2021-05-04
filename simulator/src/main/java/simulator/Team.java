import java.util.*;

public class Team {

  // int playerNumber;
  ArrayList<Pokemon> listOfPman;

  public Team() {

    // this.playerNumber = 1;
    this.listOfPman = new ArrayList<Pokemon>();

  }

  public void add(Pokemon pman) {

    this.listOfPman.add(pman);

  }

  public Pokemon get(int index) {

    return this.listOfPman.get(index);

  }

  public int size() {

    return this.listOfPman.size();

  }

  public Boolean contains(Pokemon pman) {

    return this.listOfPman.contains(pman);

  }

  public int indexOf(Pokemon pman) {

    return this.listOfPman.indexOf(pman);

  }

  // false if at least one pokemon in team has > 0 health, true otherwise
  public Boolean isOutOfUseablePokemon() {

    int size = this.listOfPman.size();

    for (int i = 0; i < size; i++) {

      if (listOfPman.get(i).currentHealth > 0) { return false; }

    }

    return true;

  }

  public Team deepCopy() {

    Team teamCopy = new Team();

    int size = this.listOfPman.size();

    for (int i = 0; i < size; i++) {

      teamCopy.add( this.listOfPman.get(i).deepCopy() );

    }

    return teamCopy;

  }

}
