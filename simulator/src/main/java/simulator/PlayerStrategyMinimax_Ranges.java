import java.util.*;

import static utility.Constants.*;

public class PlayerStrategyMinimax_Ranges extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  public PlayerStrategyMinimax_Ranges(int number) {

    super(number);
    super.name = "Minimax - Ranges";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategyMinimax_Ranges(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Minimax - Ranges";

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
      if (this.dataAccessFlag == FULLY_OBSERVABLE) {

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

    // Minimax

    Tree minimaxTree;

    ArrayList<Integer> highestScoringActions;

    int speedResult = currentGS.whichPokemonIsFaster(currentGS, this.playerNumber);

    // Will return random if same, we want it to always be us if its a tie.
    if (speedResult == this.playerNumber) {
    // if (true) {

      minimaxTree = Tree.buildRoot(currentGS, this.playerNumber);

      minimax(minimaxTree.root,true,MIN,MAX,4);

      highestScoringActions = this.evaluateMinimaxTree(minimaxTree, true);

      if (printMode == PRINTING_ENABLED) {
        // System.out.println(gameTree.root.currentScore);
        // System.out.println(gameTree.root.childrenScores());
        // System.out.println(highestScoringActions.toString());

        // System.out.println("Minimax score: " + minimax(testTree.root,true,MIN,MAX,4) );
        System.out.println("Minimax root score: " + minimaxTree.root.currentScore );
        System.out.println("Minimax best move indexes: " + evaluateMinimaxTree(minimaxTree, true).toString() );

        // for (int i = 0; i < minimaxTree.root.children.size(); i++) {
        //
        //   System.out.println(minimaxTree.root.children.get(i).currentScore);
        //
        // }

      }

    }

    else {

      minimaxTree = Tree.buildRoot(currentGS, this.opponentPlayerNumber);

      minimax(minimaxTree.root,false,MIN,MAX,4 /* + 1 */);

      highestScoringActions = this.evaluateMinimaxTree(minimaxTree, false);

      if (printMode == PRINTING_ENABLED) {
        // System.out.println(gameTree.root.currentScore);
        // System.out.println(gameTree.root.childrenScores());
        // System.out.println(highestScoringActions.toString());

        // System.out.println("Minimax score: " + minimax(testTree.root,true,MIN,MAX,4) );
        System.out.println("Minimax root score: " + minimaxTree.root.currentScore );
        System.out.println("Minimax best move indexes: " + highestScoringActions.toString() );

        // for (int i = 0; i < minimaxTree.root.children.size(); i++) {
        //
        //   System.out.println(minimaxTree.root.children.get(i).currentScore);
        //
        // }

      }

    }

    int indexOfAction;

    if (highestScoringActions.size() != 0) {

      indexOfAction = highestScoringActions.get(UtilityFunctions.randomNumber(0,highestScoringActions.size() - 1));

    }

    // Opponent can win in won move, and is moving first, so tree has not been
    // evaluated past this point. Therefore there is no move to extract for us.

    // Also happens when opponent does not need to eval a first move any further,
    // eg it just faints us.

    // To avoid choosing a trash move, can later be changed to using a quickly
    // analysed best-action-even-though-we-expect-to-lose action.
    else {

      int noOfMoves = 0;

      for (int i = 0; i < possibleActions.size(); i++) {

        if (possibleActions.get(i).isMove()) { noOfMoves++; }

      }

      if (noOfMoves == 0) { indexOfAction = 0; }
      else { indexOfAction = UtilityFunctions.randomNumber(0, noOfMoves - 1); }

    }

    // System.exit(0);

    if (possibleActions.size() == 0) {

      return null;

    }

    return possibleActions.get(indexOfAction);
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
    if (this.dataAccessFlag == FULLY_OBSERVABLE) { return; }

    // Can return -1;
    // Tells us which AI access pman to add to.
    int indexOfPman = this.opponentTeam.indexOf(user);

    // System.out.println(pman.moves.get(moveIndex).moveName);

  }

  // Evaluate tree assuming this player is the root node
  public ArrayList<Integer> evaluateMinimaxTree(Tree tree, Boolean maxing) {

    ArrayList<Integer> toReturn = new ArrayList<Integer>();

    if (maxing) {

      int size = tree.root.children.size();

      for (int i = 0; i < size; i++) {

        // if (tree.root.children.get(i).currentScore == tree.root.currentScore && !toReturn.contains(tree.root.children.get(i).moveIndex)) {
        // Moves seen multiple times would be better.
        if (tree.root.children.get(i).currentScore == tree.root.currentScore) {

          toReturn.add(tree.root.children.get(i).moveIndex);

        }

      }

    }

    else {

      int size = tree.root.children.size();
      ArrayList<Integer> opponentBestMoves = new ArrayList<Integer>();
      float highestValue = MIN;

      // For all opp. moves
      for (int i = 0; i < size; i++) {

        // Only look at the ones with the minimising value
        if (tree.root.children.get(i).currentScore == tree.root.currentScore) {

          int sizeNext = tree.root.children.get(i).children.size();

          // Opponent can win on the next move, so tree has not been extended
          // past this point
          if (sizeNext == 0) {

            return toReturn;

          }

          // Look at the moves we would use in response
          for (int j = 0; j < sizeNext; j++) {

            float ourMovesScore = tree.root.children.get(i).children.get(j).currentScore;

            // if (printMode == PRINTING_ENABLED) {
            //
              // System.out.println("My move (" + j + ") scores:" + ourMovesScore);
            //
            // }

            // Add the moves index we used if its the highest value we've seen
            if (highestValue <= ourMovesScore) {

              // Reset list of good moves if we reach a strictly better score
              if (highestValue < ourMovesScore) {
                toReturn.clear();
              }

              highestValue = ourMovesScore;

              // Moves which appear a few times are more likely to be chosen
              toReturn.add(tree.root.children.get(i).children.get(j).moveIndex);

            }

          }

        }

      }

    }

    return toReturn;

  }


  // Likely just need this for Expectimax
  // Currently uses non human accessible data.

  //  (score node)
  // Needs to always be from maxing players view, not turn player's view

  public float scoreGameState(GameState currentGS) {

    Team myTeam = null;
    Team opTeam = null;
    Pokemon myPman = null;
    Pokemon opPman = null;
    Field myField = null;
    Field opField = null;

    int maxingPlayer = this.playerNumber;

    if (maxingPlayer == 1) {

      myTeam = currentGS.player1Team;
      myPman = currentGS.player1CurrentPokemon;
      opTeam = currentGS.player2Team;
      opPman = currentGS.player2CurrentPokemon;

    }

    else {

      myTeam = currentGS.player2Team;
      myPman = currentGS.player2CurrentPokemon;
      opTeam = currentGS.player1Team;
      opPman = currentGS.player1CurrentPokemon;

    }

    // Needs to be a float
    float score = 0;
    int fainted = 0;

    int size = myTeam.size();

    for (int i = 0; i < size; i++) {

      if (myTeam.get(i).currentHealth <= 0) {

        score -= 1;
        fainted ++;

      }

      // Add percent of pman remaining health

      else {

        score += myTeam.get(i).currentHealth / (float) myTeam.get(i).stats.get(0).getStatValue();

      }

    }

    if (fainted == size) {

      return MIN;

    }

    size = opTeam.size();
    fainted = 0;

    for (int i = 0; i < size; i++) {

      if (opTeam.get(i).currentHealth <= 0) {

        score += 1;
        fainted++;

      }

      // Add percent of pman remaining health

      else {

        score -= opTeam.get(i).currentHealth / (float) opTeam.get(i).stats.get(0).getStatValue();

      }

    }

    // Causes Alpha/Beta to priotise the earlier win
    if (fainted == size) {

      return MAX;

    }

    return score;

  }

  public float score(TreeNode node) {

    float scoreOfLow = scoreGameState(node.lowRoll);
    float scoreOfHigh = scoreGameState(node.highRoll);

    return ( (scoreOfLow + scoreOfHigh) / (float) 2.0  ) * node.probability;
    // return score * node.probability;

  }

  // Builds up currentNode, so later deeper searches can use and add to the tree built.
  // Tree reuseable, new scores are used on the leaves and moved up.

  // Extendable to expectimax
  public float minimax(TreeNode node, boolean maxingPlayer, float bestForMax, float bestForMin, int maxDepth) {

    // Leaf if someone has won -> TODO improve this for low and high
    // if (node.depth > maxDepth ||
    //      (
    //        (node.lowRoll.player1Team.isOutOfUseablePokemon() || node.lowRoll.player2Team.isOutOfUseablePokemon())
    //         &&
    //        (node.highRoll.player1Team.isOutOfUseablePokemon() || node.highRoll.player2Team.isOutOfUseablePokemon())
    //      )
    //    ) {

    if (node.depth > maxDepth || node.lowRoll.player1CurrentPokemon.currentHealth == 0 || node.lowRoll.player2CurrentPokemon.currentHealth == 0
      || node.highRoll.player1CurrentPokemon.currentHealth == 0 || node.highRoll.player2CurrentPokemon.currentHealth == 0) {

      // Score node - involves probability of node
      node.currentScore = this.score(node);
      // Expectimax would only return score
      return node.currentScore;

    }

    // Max
    if (maxingPlayer) {

      // - infinity
      float best = MIN;

      // Are children created?
      if (node.children.size() == 0) {

        // Fill out node's children
        buildMinimaxChildren(node, PRINTING_DISABLED);

      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        minimax(node.children.get(i), false, bestForMax, bestForMin, maxDepth);

        float val = node.children.get(i).currentScore;

        // Works out the best minimax score seen in children
        // Want to max with * prob since thats what will get propageted up
        best = Math.max(best, val * node.probability);
        // Used in calls to other children. Needed since bestForMax could be
        // from outside of this node's children.
        bestForMax = Math.max(bestForMax, best);

        // Think this pruning is fine, but needs to mention the value this was
        // done for
        // if (bestForMin <= bestForMax) { System.out.println("i: " + i + ", depth: " + node.depth); break; }

        if (bestForMin <= bestForMax) { break; }

      }

      node.currentScore = best;
      return node.currentScore;

    }

    // Min
    else {

      // + infinity
      float best = MAX;

      // Are children created?
      if (node.children.size() == 0) {

        // Fill out node's children
        buildMinimaxChildren(node, PRINTING_DISABLED);

      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        minimax(node.children.get(i), true, bestForMax, bestForMin, maxDepth);

        float val = node.children.get(i).currentScore;

        best = Math.min(best, val * node.probability);
        bestForMin = Math.min(bestForMin, best);

        // if (bestForMin <= bestForMax) { System.out.println("(Min) i: " + i + ", depth: " + node.depth); break; }
        if (bestForMin <= bestForMax) { break; }

      }

      // Remove * prob seems to improve Performance
      node.currentScore = best;
      return node.currentScore;

    }

  }

  public static void buildMinimaxChildren(TreeNode node, int printMode) {

    // GameState nodeGS = node.nodeGameState;

    GameState setUpGS = node.lowRoll.deepCopy();
    // GameState highRoll = node.highRoll.deepCopy();

    // Pokemon originalUser = null;
    Pokemon user = null;
    // Pokemon highRollUser = null;

    int opponent = 1;

    // Getting the player to move in this node.
    if (node.player == 1) { user = setUpGS.player1CurrentPokemon; opponent = 2; }
    else { user = setUpGS.player2CurrentPokemon; opponent = 1; }

    // Could be either
    int originalUserMoveSetSize = user.moves.size();

    // For each move
    for (int i = 0; i < originalUserMoveSetSize; i++) {

      // Need a copy again in here
      GameState lowRoll = node.lowRoll.deepCopy();
      GameState highRoll = node.highRoll.deepCopy();

      Pokemon lowRollUser = null;
      Pokemon highRollUser = null;

      if (node.player == 1) { lowRollUser = lowRoll.player1CurrentPokemon; highRollUser = highRoll.player1CurrentPokemon; }
      else { lowRollUser = lowRoll.player2CurrentPokemon; highRollUser = highRoll.player2CurrentPokemon; }

      // Remember moves entries can be null - Could use low or high, does not matter
      if (lowRollUser.moves.get(i) == null) { continue; }

      float[] moveProbabilitiesL = lowRoll.chanceOfMoveOutcomes(lowRoll, lowRollUser, lowRollUser.moves.get(i));
      float[] moveProbabilitiesH = highRoll.chanceOfMoveOutcomes(highRoll, highRollUser, highRollUser.moves.get(i));

      if (printMode == PRINTING_ENABLED) {

        System.out.println(Arrays.toString(moveProbabilitiesL));
        System.out.println(Arrays.toString(moveProbabilitiesH));

      }

      int indexOfMostProbableOutcomeL = UtilityFunctions.indexOfMaxOfFloatArray(moveProbabilitiesL);
      int indexOfMostProbableOutcomeH = UtilityFunctions.indexOfMaxOfFloatArray(moveProbabilitiesH);

      // Can do stuff with [3]. If != 0 could represent this fact somehow.

      // Check if anything is dead, skip applying this for the appropriate state

      // Applying low roll
      lowRoll.applyMove(lowRoll,lowRollUser,lowRollUser.moves.get(i),indexOfMostProbableOutcomeL,R_LOW,false,PRINTING_DISABLED,null,null,null);

      // Applying high roll
      highRoll.applyMove(highRoll,highRollUser,highRollUser.moves.get(i),indexOfMostProbableOutcomeH,R_HIGH,false,PRINTING_DISABLED,null,null,null);
      // highRoll.applyMove(highRoll,highRollUser,highRollUser.moves.get(i),3,R_HIGH,PRINTING_DISABLED);

      // Even, else odd, so ! = odd, else even
      if ( !((node.depth & 1) == 0) && node.depth > 1 ) {

        lowRoll.endTurn(lowRoll, PRINTING_DISABLED,null);
        highRoll.endTurn(highRoll, PRINTING_DISABLED,null);
        // System.out.println(node.depth);

      }

      TreeNode newNode = new TreeNode(lowRoll, highRoll, opponent);
      newNode.moveIndex = i;
      newNode.probability = (moveProbabilitiesL[indexOfMostProbableOutcomeL] + moveProbabilitiesH[indexOfMostProbableOutcomeH]) / (float) 2.0;

      node.setChild(newNode);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("TREE NODE ADDED");
      }

    }

  }

  // Depth for end of turn
  public static void expectedValue(GameState currentGS, int moveToBeUsed, int depth, int nodePlayer, int printMode) {

    // GameState nodeGS = node.nodeGameState;
    GameState copyGS = currentGS.deepCopy();
    Pokemon user = null;

    if (nodePlayer == 1) { user = copyGS.player1CurrentPokemon; }
    else { user = copyGS.player2CurrentPokemon; }

    float[] moveProbabilities = copyGS.chanceOfMoveOutcomes(copyGS, user, user.moves.get(moveToBeUsed));

    if (printMode == PRINTING_ENABLED) {

      System.out.println(Arrays.toString(moveProbabilities));

    }

    for (int j = 0; j < 4; j++) {

      // Pruning states with 0 chance of occurring
      if (moveProbabilities[j] == 0) { continue; }

      GameState copyOfCopyGS = copyGS.deepCopy();
      Pokemon copyOfUser = null;
      int opponent = 1;

      if (nodePlayer == 1) { copyOfUser = copyOfCopyGS.player1CurrentPokemon; opponent = 2; }
      else { copyOfUser = copyOfCopyGS.player2CurrentPokemon; opponent = 1; }

      copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(moveToBeUsed),j,R_RANDOM,false,PRINTING_DISABLED,null,null,null);

      // Even, else odd, so ! = odd, else even
      if ( !((depth & 1) == 0) && depth > 1 ) {

        copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED,null);
        // System.out.println(node.depth);

      }

      // newNode.moveIndex = moveToBeUsed;
      // newNode.probability = moveProbabilities[j];

      // node.setChild(newNode);

      // Could evaluate score here
      // if (newNode.depth == 4) {}

      if (printMode == PRINTING_ENABLED) {
        System.out.println("TREE NODE ADDED");
      }

    }

  }

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
