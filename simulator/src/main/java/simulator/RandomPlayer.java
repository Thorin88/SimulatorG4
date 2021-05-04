import java.util.*;

import static utility.Constants.*;

public class RandomPlayer extends Player {

  public RandomPlayer(int number) {

    super(number);
    super.name = "Random";

  }

  public void setDataAccessFlag(int newFlag) {

    return;

  }

  public Action chooseAction(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    if (possibleActions.size() == 0) {

      return null;

    }

    int noOfMoves = 0;

    for (int i = 0; i < possibleActions.size(); i++) {

      if (possibleActions.get(i).isMove()) { noOfMoves++; }

    }

    return possibleActions.get(UtilityFunctions.randomNumber(0, noOfMoves - 1) );
    // return possibleActions.get(UtilityFunctions.randomNumber(0,(possibleActions.size() - 1) ) );

  }

  // public int chooseMove(GameState currentGS) {
  //
  //   return 0;
  //
  // }

  public Action switchOutFainted(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    if (printMode == PRINTING_ENABLED) {
      System.out.println("Player " + super.playerNumber + "'s Pokemon fainted.");
    }

    if (possibleActions.size() == 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Player " + super.playerNumber + " has no more Pokemon able to battle.");
      }
      return null;

    }

    return possibleActions.get(0);

  }

  // TODO
  public void pokemonSeen(GameState currentGS, int printMode) {

    return;

  }

  public void moveSeen(GameState currentGS, Pokemon user, Pokemon target, int moveIndex, Estimates estimates, int printMode) {}

}
