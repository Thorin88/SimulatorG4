import java.util.*;

import static utility.Constants.*;

// Switches in pman if they faint during tree creation
public class PlayerStrategyExpectimaxIII extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  // Statistics

  static int expectimaxCalls = 0;
  static int nodesCreated = 0;
  static int nodesPruned = 0;

  public PlayerStrategyExpectimaxIII(int number) {

    super(number);
    super.name = "Expectimax III";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategyExpectimaxIII(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Expectimax III";

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public void setDataAccessFlag(int newFlag) {

    this.dataAccessFlag = newFlag;

  }

  public Action chooseAction(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    // Statistics
    expectimaxCalls = 0;
    nodesCreated = 0;
    nodesPruned = 0;

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

    Tree expectimaxTree;

    ArrayList<Integer> highestScoringActions;

    int speedResult = currentGS.whichPokemonIsFaster(currentGS, this.playerNumber);

    // Will return random if same, we want it to always be us if its a tie.
    if (speedResult == this.playerNumber) {

      expectimaxTree = Tree.buildRoot(currentGS, this.playerNumber);

      // If negative, could evaluate switches
      expectimax(expectimaxTree.root,true,MIN,MAX,4);

      highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, true);

      if (printMode == PRINTING_ENABLED) {
        // System.out.println(gameTree.root.currentScore);
        // System.out.println(gameTree.root.childrenScores());
        // System.out.println(highestScoringActions.toString());

        // System.out.println("Minimax score: " + minimax(testTree.root,true,MIN,MAX,4) );
        System.out.println("Expectimax root score: " + expectimaxTree.root.currentScore );
        System.out.println("Expectimax best move indexes: " + highestScoringActions.toString() );

        // for (int i = 0; i < minimaxTree.root.children.size(); i++) {
        //
        //   System.out.println(minimaxTree.root.children.get(i).currentScore);
        //
        // }

      }

    }

    else {

      expectimaxTree = Tree.buildRoot(currentGS, this.opponentPlayerNumber);

      expectimax(expectimaxTree.root,false,MIN,MAX,4 /* + 1 */);

      highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, false);

      if (printMode == PRINTING_ENABLED) {
        // System.out.println(gameTree.root.currentScore);
        // System.out.println(gameTree.root.childrenScores());
        // System.out.println(highestScoringActions.toString());

        // System.out.println("Minimax score: " + minimax(testTree.root,true,MIN,MAX,4) );
        System.out.println("Expectimax root score (Opponent): " + expectimaxTree.root.currentScore );
        System.out.println("Expectimax best move indexes: " + highestScoringActions.toString() );

        // for (int i = 0; i < minimaxTree.root.children.size(); i++) {
        //
        //   System.out.println(minimaxTree.root.children.get(i).currentScore);
        //
        // }

      }

    }

    int indexOfAction;

    if (printMode == PRINTING_ENABLED) {

      System.out.println("----------------------------");
      System.out.println("Statistics:");
      System.out.println("Expectimax calls: " + expectimaxCalls);
      System.out.println("Nodes created: " + nodesCreated + " (" + ((float) nodesCreated / Math.pow(16,4)) * 100 + ") ");
      System.out.println("Nodes pruned: " + nodesPruned);
      System.out.println("----------------------------");

    }

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

    // Pokemon opponentPokemon = null;
    // // Eventually add in
    // // Team opponentTeam = null;
    //
    // Pokemon yourPokemon = null;
    Team yourTeam = null;

    if (this.opponentPlayerNumber == 1) {

      yourTeam = currentGS.player2Team;

    }
    else {

      yourTeam = currentGS.player1Team;

    }

    // Might depend on speed of pman
    float bestScoreSoFar = MIN;
    float rootScore = MIN;
    ArrayList<Integer> goodSwitches = new ArrayList<Integer>();

    for (int i = 0; i < possibleActions.size(); i++) {

      int actionToTry = possibleActions.get(i).indexOfAction;

      // Since being called when stuff has fainted, can assume we won't find
      // the current pman being used with health > 0.
      if (yourTeam.get( actionToTry ).currentHealth == 0) {

        continue;

      }

      GameState currentGSCopy = currentGS.deepCopy();

      currentGSCopy.switchOut(currentGSCopy, this.playerNumber, actionToTry, PRINTING_DISABLED, null);

      // Score this switch in.

      int speedResult = currentGSCopy.whichPokemonIsFaster(currentGSCopy, this.playerNumber);

      if (speedResult == this.playerNumber) {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopy, this.playerNumber);

        rootScore = expectimax(expectimaxTree.root,true,MIN,MAX,4);

      }

      else {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopy, this.opponentPlayerNumber);

        rootScore = expectimax(expectimaxTree.root,false,MIN,MAX,4);

      }

      if (printMode == PRINTING_ENABLED) {

        System.out.println("Switching in " + yourTeam.get( actionToTry ).name + " gives a score of " + rootScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");

      }

      // For UI
      // possibleActions.get(i).agentScore = rootScore;

      if (rootScore >= bestScoreSoFar) {

        if (rootScore > bestScoreSoFar) {

          bestScoreSoFar = rootScore;
          goodSwitches.clear();

        }

        // This is meant to be i
        goodSwitches.add(i);

      }

    }

    if (goodSwitches.size() != 0) {

      // Possibly have a tie breaker here
      return possibleActions.get( goodSwitches.get(0) );

    }

    return possibleActions.get(0);

  }

  // Given a player, work out scores of switches. Scoring from this.playerNumbers perspective.
  public int switchOutFaintedLite(GameState currentGS, ArrayList<Action> possibleActions, int player, int printMode) {

    // Pokemon opponentPokemon = null;
    // // Eventually add in
    // // Team opponentTeam = null;
    //
    // Pokemon yourPokemon = null;
    int opponent = 1;
    Team yourTeam = null;

    if (player == 1) {

      yourTeam = currentGS.player1Team;
      opponent = 2;

    }
    else {

      yourTeam = currentGS.player2Team;
      opponent = 1;

    }

    // Might depend on speed of pman
    float bestScoreSoFar = MIN;
    float rootScore = MIN;
    ArrayList<Integer> goodSwitches = new ArrayList<Integer>();

    for (int i = 0; i < possibleActions.size(); i++) {

      // Updated
      int actionToTry = possibleActions.get(i).indexOfAction;

      // Since being called when stuff has fainted, can assume we won't find
      // the current pman being used with health > 0.
      if (yourTeam.get( actionToTry ).currentHealth == 0) {

        continue;

      }

      GameState currentGSCopy = currentGS.deepCopy();

      currentGSCopy.switchOut(currentGSCopy, player, actionToTry, PRINTING_DISABLED, null);

      // Score this switch in.

      int speedResult = currentGSCopy.whichPokemonIsFaster(currentGSCopy, player);
      // Check if maxing player is correct here. Eg expectimax will its score
      // a gameState from this.playerNumber's view.
      // INCORRECT - build trees as normal
      // Boolean maxingPlayer = (player == this.playerNumber);

      if (speedResult == player) {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopy, player);

        rootScore = expectimax(expectimaxTree.root,true,MIN,MAX,1);

      }

      else {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopy, opponent);

        rootScore = expectimax(expectimaxTree.root,false,MIN,MAX,1);

      }

      // if (printMode == PRINTING_ENABLED) {
      //
      //   System.out.println("Switching in " + yourTeam.get( possibleActions.get(i).indexOfAction ).name + " gives a score of " + rootScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");
      //
      // }

      // This should change depending on whether the opponent is deciding
      // this or us.

      if (player == this.playerNumber) {

        if (rootScore >= bestScoreSoFar) {

          if (rootScore > bestScoreSoFar) {

            bestScoreSoFar = rootScore;
            goodSwitches.clear();

          }

          // This is meant to be i
          goodSwitches.add(i);

        }

      }

      else {

        if (rootScore <= bestScoreSoFar) {

          if (rootScore < bestScoreSoFar) {

            bestScoreSoFar = rootScore;
            goodSwitches.clear();

          }

          // This is meant to be i
          goodSwitches.add(i);

        }

      }

    }

    if (goodSwitches.size() != 0) {

      // Possibly have a tie breaker here
      return possibleActions.get( goodSwitches.get(0) ).indexOfAction;

    }

    if (possibleActions.size() == 0) {

      return -1;

    }

    // Can return null
    return possibleActions.get(0).indexOfAction;

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
  public ArrayList<Integer> evaluateExpectimaxTree(Tree tree, Boolean maxing) {

    ArrayList<Integer> toReturn = new ArrayList<Integer>();

    if (maxing) {

      toReturn = tree.root.indexesToPick;

      // System.out.println(toReturn.size());

    }

    else {

      ArrayList<Integer> opponentBestMoves = tree.root.indexesToPick;
      // int size = opponentBestMoves.size();
      int size = tree.root.children.size();

      float highestProbSoFar = -1;

      // For all opp. moves
      for (int i = 0; i < size; i++) {

        TreeNode child = tree.root.children.get(i);

        // Might always return 0 if there were no good moves
        if ( !opponentBestMoves.contains(child.moveIndex) ) {

          continue;

        }

        // No good moves
        if (child.indexesToPick == null) { continue; }

        // This needs additional stuff
        // Could pick the most likely instance of the move and pick from that.
        // Eg for dark void as opp best move
        // + [1] for move missing
        // + [0,1,2,3] for move hitting (cause nothing does anything)
        // We end up with [1,0,1,2,3], always picking the first one
        // Stone edge, could end up with like [1] + [2], so can't just
        // get rid of dupes

        // Want to go with the most probable instance of the opp move and
        // find our best moves from that node only.

        // For debug to see what moves were in the other nodes for a move
        // System.out.println(child.indexesToPick);

        // > Means that DVs 50 50 is avoided
        // Could improve this, but picking between outcomes with same probs
        // does not come up often. Balance between this and prefering outcomes
        // which are worse or better for the AI. This seems the most sensible
        // in the most situations.

        // This means that when multiple moves for the opp and just as could,
        // we pick the move with the most likely outcome
        if (child.probability > highestProbSoFar) {
          highestProbSoFar = child.probability;
          toReturn.clear();
          toReturn.addAll(child.indexesToPick);
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

    return this.scoreGameState(node.nodeGameState) * node.probability;

  }

  // Builds up currentNode, so later deeper searches can use and add to the tree built.
  // Tree reuseable, new scores are used on the leaves and moved up.

  // Extendable to expectimax
  public float expectimax(TreeNode node, boolean maxingPlayer, float bestForMax, float bestForMin, int maxDepth) {

    // Leaf if someone has won -> TODO improve this for low and high
    // if (node.depth > maxDepth ||
    //      (
    //        (node.lowRoll.player1Team.isOutOfUseablePokemon() || node.lowRoll.player2Team.isOutOfUseablePokemon())
    //         &&
    //        (node.highRoll.player1Team.isOutOfUseablePokemon() || node.highRoll.player2Team.isOutOfUseablePokemon())
    //      )
    //    ) {

    expectimaxCalls++;

    // Normal line
    // if (node.depth > maxDepth || node.nodeGameState.player1CurrentPokemon.currentHealth == 0 || node.nodeGameState.player2CurrentPokemon.currentHealth == 0) {
    // Line when replacing fainted pman in the search
    if (node.depth > maxDepth || node.nodeGameState.player1Team.isOutOfUseablePokemon() || node.nodeGameState.player2Team.isOutOfUseablePokemon()) {

      // Score node - does not involve probability of node
      node.currentScore = this.scoreGameState(node.nodeGameState);
      return node.currentScore;

    }

    // Max
    if (maxingPlayer) {

      // - infinity
      float best = MIN;
      // float expectedValue = (float) 0.0;
      // float[] expectedValues = {(float) 0.0,(float) 0.0,(float) 0.0,(float) 0.0};
      ArrayList<Float> expectedValues = new ArrayList<Float>();

      for (int i = 0; i < 4; i++) {

        expectedValues.add(null);

      }

      // Are children created?
      if (node.children.size() == 0) {

        // Fill out node's children
        buildExpectimaxChildren(node, PRINTING_DISABLED);

      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        expectimax(child, false, bestForMax, bestForMin, maxDepth);

        // expectedValues[child.moveIndex] += child.currentScore * child.probability;

        // Checking if a value already exists for this element
        if (expectedValues.get(child.moveIndex) == null) {

          expectedValues.set(child.moveIndex, child.currentScore * child.probability);

        }

        else {

          expectedValues.set(child.moveIndex, expectedValues.get(child.moveIndex) + (child.currentScore * child.probability) );

        }

        // Works out the best minimax score seen in children
        // Want to max with * prob since thats what will get propageted up

        // best = Math.max(best, val * node.probability);

        // Used in calls to other children. Needed since bestForMax could be
        // from outside of this node's children.

        // bestForMax = Math.max(bestForMax, best);

        // if (bestForMin <= bestForMax) { System.out.println("i: " + i + ", depth: " + node.depth); break; }

        // if (bestForMin <= bestForMax) { break; }

      }

      // expectedValues.removeIf( x -> x == null );

      // Can be -1 in some situations. Node must have no children
      int indexOfBestValuedMove = UtilityFunctions.indexOfMaxOfFloatArrayList(expectedValues);
      ArrayList<Integer> indexesWithBestValue = UtilityFunctions.indexesOfMaxOfFloatArrayList(expectedValues);

      node.currentScore = expectedValues.get(indexOfBestValuedMove);
      node.indexesToPick = indexesWithBestValue;

      return node.currentScore;

    }

    // Min
    else {

      // + infinity
      float best = MAX;
      // float expectedValue = (float) 0.0;
      // float[] expectedValues = {(float) 0.0,(float) 0.0,(float) 0.0,(float) 0.0};
      ArrayList<Float> expectedValues = new ArrayList<Float>();

      for (int i = 0; i < 4; i++) {

        expectedValues.add(null);

      }

      // Are children created?
      if (node.children.size() == 0) {

        // Fill out node's children
        buildExpectimaxChildren(node, PRINTING_DISABLED);

      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        expectimax(child, true, bestForMax, bestForMin, maxDepth);

        // expectedValue += child.currentScore * child.probability;
        // expectedValues[child.moveIndex] += child.currentScore * child.probability;

        // Checking if a value already exists for this element
        if (expectedValues.get(child.moveIndex) == null) {

          expectedValues.set(child.moveIndex, child.currentScore * child.probability);

        }

        else {

          expectedValues.set(child.moveIndex, expectedValues.get(child.moveIndex) + (child.currentScore * child.probability) );

        }

        // best = Math.min(best, val * node.probability);
        // bestForMin = Math.min(bestForMin, best);

        // if (bestForMin <= bestForMax) { System.out.println("(Min) i: " + i + ", depth: " + node.depth); break; }
        // if (bestForMin <= bestForMax) { break; }

      }

      // expectedValues.removeIf( x -> x == null);

      // Remove * prob seems to improve Performance
      int indexOfBestValuedMove = UtilityFunctions.indexOfMinOfFloatArrayList(expectedValues);
      ArrayList<Integer> indexesWithBestValue = UtilityFunctions.indexesOfMinOfFloatArrayList(expectedValues);

      node.currentScore = expectedValues.get(indexOfBestValuedMove);
      node.indexesToPick = indexesWithBestValue;

      return node.currentScore;

    }

  }

  // public static void buildMinimaxChildren(TreeNode node, int printMode) {
  //
  //   // GameState nodeGS = node.nodeGameState;
  //
  //   GameState setUpGS = node.lowRoll.deepCopy();
  //   // GameState highRoll = node.highRoll.deepCopy();
  //
  //   // Pokemon originalUser = null;
  //   Pokemon user = null;
  //   // Pokemon highRollUser = null;
  //
  //   int opponent = 1;
  //
  //   // Getting the player to move in this node.
  //   if (node.player == 1) { user = setUpGS.player1CurrentPokemon; opponent = 2; }
  //   else { user = setUpGS.player2CurrentPokemon; opponent = 1; }
  //
  //   // Could be either
  //   int originalUserMoveSetSize = user.moves.size();
  //
  //   // For each move
  //   for (int i = 0; i < originalUserMoveSetSize; i++) {
  //
  //     // Need a copy again in here
  //     GameState lowRoll = node.lowRoll.deepCopy();
  //     GameState highRoll = node.highRoll.deepCopy();
  //
  //     Pokemon lowRollUser = null;
  //     Pokemon highRollUser = null;
  //
  //     if (node.player == 1) { lowRollUser = lowRoll.player1CurrentPokemon; highRollUser = highRoll.player1CurrentPokemon; }
  //     else { lowRollUser = lowRoll.player2CurrentPokemon; highRollUser = highRoll.player2CurrentPokemon; }
  //
  //     // Remember moves entries can be null - Could use low or high, does not matter
  //     if (lowRollUser.moves.get(i) == null) { break; }
  //
  //     float[] moveProbabilitiesL = lowRoll.chanceOfMoveOutcomes(lowRoll, lowRollUser, lowRollUser.moves.get(i));
  //     float[] moveProbabilitiesH = highRoll.chanceOfMoveOutcomes(highRoll, highRollUser, highRollUser.moves.get(i));
  //
  //     if (printMode == PRINTING_ENABLED) {
  //
  //       System.out.println(Arrays.toString(moveProbabilitiesL));
  //       System.out.println(Arrays.toString(moveProbabilitiesH));
  //
  //     }
  //
  //     int indexOfMostProbableOutcomeL = UtilityFunctions.indexOfMaxOfFloatArray(moveProbabilitiesL);
  //     int indexOfMostProbableOutcomeH = UtilityFunctions.indexOfMaxOfFloatArray(moveProbabilitiesH);
  //
  //     // Can do stuff with [3]. If != 0 could represent this fact somehow.
  //
  //     // Check if anything is dead, skip applying this for the appropriate state
  //
  //     // Applying low roll
  //     lowRoll.applyMove(lowRoll,lowRollUser,lowRollUser.moves.get(i),indexOfMostProbableOutcomeL,R_LOW,PRINTING_DISABLED);
  //
  //     // Applying high roll
  //     highRoll.applyMove(highRoll,highRollUser,highRollUser.moves.get(i),indexOfMostProbableOutcomeH,R_HIGH,PRINTING_DISABLED);
  //     // highRoll.applyMove(highRoll,highRollUser,highRollUser.moves.get(i),3,R_HIGH,PRINTING_DISABLED);
  //
  //     // Even, else odd, so ! = odd, else even
  //     if ( !((node.depth & 1) == 0) && node.depth > 1 ) {
  //
  //       lowRoll.endTurn(lowRoll, PRINTING_DISABLED);
  //       highRoll.endTurn(highRoll, PRINTING_DISABLED);
  //       // System.out.println(node.depth);
  //
  //     }
  //
  //     TreeNode newNode = new TreeNode(lowRoll, highRoll, opponent);
  //     newNode.moveIndex = i;
  //     newNode.probability = (moveProbabilitiesL[indexOfMostProbableOutcomeL] + moveProbabilitiesH[indexOfMostProbableOutcomeH]) / (float) 2.0;
  //
  //     node.setChild(newNode);
  //
  //     if (printMode == PRINTING_ENABLED) {
  //       System.out.println("TREE NODE ADDED");
  //     }
  //
  //   }
  //
  // }

  // Made non-static. Allows switchOutFaintedLite to be called, which will
  // use expectimax.
  public void buildExpectimaxChildren(TreeNode node, int printMode) {

    GameState nodeGS = node.nodeGameState;
    Pokemon originalUser = null;

    if (node.player == 1) { originalUser = nodeGS.player1CurrentPokemon; }
    else { originalUser = nodeGS.player2CurrentPokemon; }

    int originalUserMoveSetSize = originalUser.moves.size();

    for (int i = 0; i < originalUserMoveSetSize; i++) {

      if (originalUser.moves.get(i) == null) { continue; }

      // Remember moves entries can be null

      GameState copyGS = nodeGS.deepCopy();
      Pokemon user = null;

      if (node.player == 1) { user = copyGS.player1CurrentPokemon; }
      else { user = copyGS.player2CurrentPokemon; }

      float[] moveProbabilities = copyGS.chanceOfMoveOutcomes(copyGS, user, user.moves.get(i));

      if (printMode == PRINTING_ENABLED) {

        System.out.println(Arrays.toString(moveProbabilities));

      }

      for (int j = 0; j < 4; j++) {

        // Pruning nodes with 0 chance of occurring
        if (moveProbabilities[j] == 0) { nodesPruned++; continue; }

        GameState copyOfCopyGS = copyGS.deepCopy();
        Pokemon copyOfUser = null;
        int opponent = 1;

        if (node.player == 1) { copyOfUser = copyOfCopyGS.player1CurrentPokemon; opponent = 2; }
        else { copyOfUser = copyOfCopyGS.player2CurrentPokemon; opponent = 1; }

        // Could use the same R each time
        copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(i),j,R_RANDOM,false,PRINTING_DISABLED,null,null,null);

        // Even, else odd, so ! = odd, else even
        if ( !((node.depth & 1) == 0) && node.depth > 1 ) {

          copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED,null);
          // System.out.println(node.depth);

          // Switch in new pman
          if (copyOfUser.currentHealth <= 0 ) {

            // System.out.println("Test 1");

            ArrayList<Action> switches = copyOfCopyGS.generateAllValidSwitchActions(copyOfCopyGS, node.player);

            int actionIndex = switchOutFaintedLite(copyOfCopyGS, switches, node.player, PRINTING_DISABLED);

            // This player has lost
            if (actionIndex != -1) {

              copyOfCopyGS.switchOut(copyOfCopyGS, node.player, actionIndex, PRINTING_DISABLED, null);

            }

            // else { System.out.println("Did this L"); }

          }

          Pokemon copyOfOpponent = null;

          if (node.player == 2) { copyOfOpponent = copyOfCopyGS.player1CurrentPokemon; }
          else { copyOfOpponent = copyOfCopyGS.player2CurrentPokemon; }

          if (copyOfOpponent.currentHealth <= 0 ) {

            // System.out.println("Test 2");

            ArrayList<Action> switches = copyOfCopyGS.generateAllValidSwitchActions(copyOfCopyGS, opponent);

            int actionIndex = switchOutFaintedLite(copyOfCopyGS, switches, opponent, PRINTING_DISABLED);

            // This player has lost
            if (actionIndex != -1) {

              copyOfCopyGS.switchOut(copyOfCopyGS, opponent, actionIndex, PRINTING_DISABLED, null);

            }

            // else { System.out.println("Did this K"); }

          }

          // End switch checks

        }

        TreeNode newNode = new TreeNode(copyOfCopyGS, opponent);
        newNode.moveIndex = i;
        newNode.probability = moveProbabilities[j];

        node.setChild(newNode);
        nodesCreated++;

        // Could evaluate score here
        // if (newNode.depth == 4) {}

        if (printMode == PRINTING_ENABLED) {
          System.out.println("TREE NODE ADDED");
        }

      }

    }

  }

  // Depth for end of turn
  // public static void expectedValue(GameState currentGS, int moveToBeUsed, int depth, int nodePlayer, int printMode) {
  //
  //   // GameState nodeGS = node.nodeGameState;
  //   GameState copyGS = currentGS.deepCopy();
  //   Pokemon user = null;
  //
  //   if (nodePlayer == 1) { user = copyGS.player1CurrentPokemon; }
  //   else { user = copyGS.player2CurrentPokemon; }
  //
  //   float[] moveProbabilities = copyGS.chanceOfMoveOutcomes(copyGS, user, user.moves.get(moveToBeUsed));
  //
  //   if (printMode == PRINTING_ENABLED) {
  //
  //     System.out.println(Arrays.toString(moveProbabilities));
  //
  //   }
  //
  //   for (int j = 0; j < 4; j++) {
  //
  //     // Pruning states with 0 chance of occurring
  //     if (moveProbabilities[j] == 0) { continue; }
  //
  //     GameState copyOfCopyGS = copyGS.deepCopy();
  //     Pokemon copyOfUser = null;
  //     int opponent = 1;
  //
  //     if (nodePlayer == 1) { copyOfUser = copyOfCopyGS.player1CurrentPokemon; opponent = 2; }
  //     else { copyOfUser = copyOfCopyGS.player2CurrentPokemon; opponent = 1; }
  //
  //     copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(moveToBeUsed),j,R_RANDOM,PRINTING_DISABLED);
  //
  //     // Even, else odd, so ! = odd, else even
  //     if ( !((depth & 1) == 0) && depth > 1 ) {
  //
  //       copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED);
  //       // System.out.println(node.depth);
  //
  //     }
  //
  //     // newNode.moveIndex = moveToBeUsed;
  //     // newNode.probability = moveProbabilities[j];
  //
  //     // node.setChild(newNode);
  //
  //     // Could evaluate score here
  //     // if (newNode.depth == 4) {}
  //
  //     if (printMode == PRINTING_ENABLED) {
  //       System.out.println("TREE NODE ADDED");
  //     }
  //
  //   }
  //
  // }

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
