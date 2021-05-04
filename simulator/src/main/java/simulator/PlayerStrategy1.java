import java.util.*;

import static utility.Constants.*;

public class PlayerStrategy1 extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  public PlayerStrategy1(int number) {

    super(number);
    this.dataAccessFlag = 0;
    super.name = "Random (No switching)";

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategy1(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Random (No switching)";

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public void setDataAccessFlag(int newFlag) {

    this.dataAccessFlag = newFlag;

  }

  public Action chooseAction(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    Pokemon opponentPokemon = null;

    Pokemon yourPokemon = null;

    if (this.opponentPlayerNumber == 1) {

      opponentPokemon = currentGS.player1CurrentPokemon;
      yourPokemon = currentGS.player2CurrentPokemon;

    }
    else {

      opponentPokemon = currentGS.player2CurrentPokemon;
      yourPokemon = currentGS.player1CurrentPokemon;

    }

    // TODO: Make pokemon for AI access list.
    if (!this.opponentTeam.contains(opponentPokemon)) {

      // Adding pokemon that the AI won't access but will use to fill-out its own pokemon data.
      this.opponentTeam.add(opponentPokemon);

      // Full data access
      if (this.dataAccessFlag == 0) {

        this.opponentTeamAIAccess.add(opponentPokemon);

      }

      // Inferable data access
      else {

        Pokemon newAIPman = new Pokemon();
        int[] noMoves = {-1,-1,-1,-1};
        // Assuming form is always 0
        newAIPman.buildFromIndexes(opponentPokemon.id, 0, noMoves, 0);

        this.opponentTeamAIAccess.add(newAIPman);

      }

      // this.opponentTeamAIAccess.add();

    }

    // New pokemon added at this point

    // Can be used to access the relevant pman in either the full or inferable pokemon team.
    int indexOfOpponentCurrentPman = this.opponentTeam.indexOf(opponentPokemon);

    // Go through and score actions, getIndexOf highest score. That index = action to use

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

  public void moveSeen(GameState currentGS, Pokemon user, Pokemon target, int moveIndex, Estimates estimates, int printMode) {
    // Already has access to move data.
    if (this.dataAccessFlag == 0) { return; }

    // Can return -1;
    // Tells us which AI access pman to add to.
    int indexOfPman = this.opponentTeam.indexOf(user);

    // System.out.println(pman.moves.get(moveIndex).moveName);

  }

  // Likely just need this for Expectimax

  public int score(GameState currentGS) {

    return UtilityFunctions.randomNumber(-2,2);

  }

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
