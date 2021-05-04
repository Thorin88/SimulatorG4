import java.util.*;

public abstract class Player {

  int playerNumber;
  String name;

  public Player(int number) {

    this.playerNumber = number;

  }

  public Player(int number, int flag) {

    this.playerNumber = number;

  }

  public abstract void setDataAccessFlag(int newFlag);

  public abstract Action chooseAction(GameState currentGS, ArrayList<Action> possibleActions, int printMode);

  // public abstract int chooseMove(GameState currentGS);

  public abstract Action switchOutFainted(GameState currentGS, ArrayList<Action> possibleActions, int printMode);

  // public abstract void moveSeen(Pokemon pman, int moveIndex, int printMode);
  // public abstract void moveSeen(Pokemon pman, int moveIndex, Estimates estimates, int printMode);

  public abstract void pokemonSeen(GameState currentGS, int printMode);

  public abstract void moveSeen(GameState currentGS, Pokemon user, Pokemon target, int moveIndex, Estimates estimates, int printMode);

}
