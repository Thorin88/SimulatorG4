import java.util.*;

import static utility.Constants.*;

// Thinks about switches
// Calls minimax on all moves seperately
// Uses old pruning method, which I think is more correct (val is not *prob)
public class PlayerStrategyMinimaxII extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  // Statistics

  static int minimaxCalls = 0;
  static int nodesCreated = 0;
  static int nodesPruned = 0;

  public PlayerStrategyMinimaxII(int number) {

    super(number);
    super.name = "Minimax - II";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategyMinimaxII(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Minimax - II";

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
    minimaxCalls = 0;
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

    Tree minimaxTree = null;
    ArrayList<Integer> indexesOfBest = new ArrayList<Integer>();

    int speedResult = currentGS.whichPokemonIsFaster(currentGS, this.playerNumber);

    if (speedResult == this.playerNumber) {

      minimaxTree = Tree.buildRoot(currentGS, this.playerNumber);
      // Build up first layer of children
      minimax(minimaxTree.root,true,MIN,MAX,1);

      int numberOfChildren = minimaxTree.root.children.size();

      float bestSoFar = MIN;

      float bestForMax = MIN;
      float bestForMin = MAX;

      // ArrayList<Integer> indexesOfBest = new ArrayList<Integer>();

      for (int i = 0; i < numberOfChildren; i++) {

        // Depth refers to furthest node reached in tree, so still 4 not 3
        float score = minimax(minimaxTree.root.children.get(i),false,bestForMax,bestForMin,4);
        // float score = minimax(minimaxTree.root.children.get(i),false,MIN,MAX,4);

        if (printMode == PRINTING_ENABLED) {
          System.out.println("[" + minimaxTree.root.children.get(i).moveIndex + "]: " + score);
        }

        if (score >= bestSoFar) {

          if (score > bestSoFar) {

            bestSoFar = score;
            indexesOfBest.clear();

          }

          indexesOfBest.add( minimaxTree.root.children.get(i).moveIndex );

        }

      }

    }

    else {

      minimaxTree = Tree.buildRoot(currentGS, this.opponentPlayerNumber);
      // Build up first layer of children
      minimax(minimaxTree.root,false,MIN,MAX,1);

      int numberOfChildren = minimaxTree.root.children.size();

      float bestSoFar = MAX;

      float bestForMax = MIN;
      float bestForMin = MAX;

      ArrayList<Integer> indexesOfBestChild = new ArrayList<Integer>();
      // ArrayList<Integer> indexesOfBest = new ArrayList<Integer>();

      for (int i = 0; i < numberOfChildren; i++) {

        // Depth refers to furthest node reached in tree, so still 4 not 3
        float score = minimax(minimaxTree.root.children.get(i),true,bestForMax,bestForMin,4);
        // float score = minimax(minimaxTree.root.children.get(i),false,MIN,MAX,4);

        if (printMode == PRINTING_ENABLED) {
          System.out.println("[" + minimaxTree.root.children.get(i).moveIndex + "]: " + score);
        }

        if (score <= bestSoFar) {

          if (score < bestSoFar) {

            bestSoFar = score;
            indexesOfBestChild.clear();

          }

          indexesOfBestChild.add( i );

        }

      }

      int indexOfTheirChild = indexesOfBestChild.get(0);

      // Get this move's node, find best max score for this node

      // Tree is already built, so does not matter too much if you
      // re-eval the tree.

      TreeNode theirBestMove = minimaxTree.root.children.get(indexOfTheirChild);
      numberOfChildren = theirBestMove.children.size();

      bestSoFar = MIN;

      bestForMax = MIN;
      bestForMin = MAX;

      if (printMode == PRINTING_ENABLED) {
        System.out.println("There best move was: " + theirBestMove.moveIndex);
      }
      // Opponent can win with their move.
      // TODO: Break the tie here somehow
      if (numberOfChildren == 0) {

        for (int i = 0; i < possibleActions.size(); i++) {
          if (possibleActions.get(i).isMove()) { indexesOfBest.add(possibleActions.get(i).indexOfAction); }
        }

      }

      for (int i = 0; i < numberOfChildren; i++) {

        // Depth refers to furthest node reached in tree, so still 4 not 3
        float score = minimax(theirBestMove.children.get(i),false,bestForMax,bestForMin,4);
        // float score = minimax(minimaxTree.root.children.get(i),false,MIN,MAX,4);

        if (printMode == PRINTING_ENABLED) {
          System.out.println("[" + theirBestMove.children.get(i).moveIndex + "]: " + score);
        }

        if (score >= bestSoFar) {

          if (score > bestSoFar) {

            bestSoFar = score;
            indexesOfBest.clear();

          }

          indexesOfBest.add( theirBestMove.children.get(i).moveIndex );

        }

      }

    }

    ArrayList<Integer> highestScoringActions = indexesOfBest;

    if (printMode == PRINTING_ENABLED) {

      System.out.println("Minimax best move indexes: " + highestScoringActions );

    }

    int indexOfAction = highestScoringActions.get(UtilityFunctions.randomNumber(0,highestScoringActions.size() - 1));

    if (printMode == PRINTING_ENABLED) {

      System.out.println("----------------------------");
      System.out.println("Statistics:");
      System.out.println("Minimax calls: " + minimaxCalls + " (Also counts calls used for switching choices)");
      System.out.println("Nodes created: " + nodesCreated + " (" + ((float) nodesCreated / Math.pow(4,4)) * 100 + ") ");
      System.out.println("Nodes pruned: " + nodesPruned);
      System.out.println("----------------------------");

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

      // Updated
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

        Tree minimaxTree = Tree.buildRoot(currentGSCopy, this.playerNumber);

        rootScore = minimax(minimaxTree.root,true,MIN,MAX,4);

      }

      else {

        Tree minimaxTree = Tree.buildRoot(currentGSCopy, this.opponentPlayerNumber);

        rootScore = minimax(minimaxTree.root,false,MIN,MAX,4);

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
  public ArrayList<Integer> evaluateMinimaxTree(Tree tree) {

    int size = tree.root.children.size();
    ArrayList<Integer> toReturn = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (tree.root.children.get(i).currentScore == tree.root.currentScore && !toReturn.contains(tree.root.children.get(i).moveIndex)) {

        toReturn.add(tree.root.children.get(i).moveIndex);

      }

    }

    return toReturn;

  }


  // Likely just need this for Expectimax
  // Currently uses non human accessible data.

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

        // 70% -> -0.3
        score -= 1 - ( myTeam.get(i).currentHealth / (float) myTeam.get(i).stats.get(0).getStatValue() ) ;

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

        // 70% -> +0.3
        score += 1 - ( opTeam.get(i).currentHealth / (float) opTeam.get(i).stats.get(0).getStatValue() );

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

  public float minimax(TreeNode node, boolean maxingPlayer, float bestForMax, float bestForMin, int maxDepth) {

    minimaxCalls++;

    // Leaf if someone has won -> TODO improve this for low and high
    if (node.depth > maxDepth || node.nodeGameState.player1Team.isOutOfUseablePokemon() ||  node.nodeGameState.player2Team.isOutOfUseablePokemon() ) {

      // Score node
      node.currentScore = this.score(node);
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
        best = Math.max(best, val * node.probability);
        // Used in calls to other children. Needed since bestForMax could be
        // from outside of this node's children.
        bestForMax = Math.max(bestForMax, best);

        if (bestForMin <= bestForMax) {

          // if (node.depth == 2) {
          //
          //   System.out.println("Did this: " + node.moveIndex);
          //   best = MIN;
          //
          // }
          nodesPruned++;
          break;

        }

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

        if (bestForMin <= bestForMax) {

          // if (node.depth == 2) {
          //
          //   System.out.println("Did this: " + node.moveIndex);
          //   best = MAX;
          //
          // }
          nodesPruned++;
          break;

        }

      }

      node.currentScore = best;
      return node.currentScore;

    }

  }

  // Removed static
  public void buildMinimaxChildren(TreeNode node, int printMode) {

    GameState nodeGS = node.nodeGameState;
    Pokemon originalUser = null;

    // Getting the player to move in this node.
    if (node.player == 1) { originalUser = node.nodeGameState.player1CurrentPokemon; }
    else { originalUser = node.nodeGameState.player2CurrentPokemon; }

    int originalUserMoveSetSize = originalUser.moves.size();

    // For each move
    for (int i = 0; i < originalUserMoveSetSize; i++) {

      // Remember moves entries can be null
      if (originalUser.moves.get(i) == null) { continue; }

      GameState copyGS = nodeGS.deepCopy();
      Pokemon user = null;

      if (node.player == 1) { user = copyGS.player1CurrentPokemon; }
      else { user = copyGS.player2CurrentPokemon; }

      float[] moveProbabilities = copyGS.chanceOfMoveOutcomes(copyGS, user, user.moves.get(i));

      if (printMode == PRINTING_ENABLED) {

        System.out.println(Arrays.toString(moveProbabilities));

      }

      int indexOfMostProbableOutcome = UtilityFunctions.indexOfMaxOfFloatArray(moveProbabilities);

      int j = indexOfMostProbableOutcome;

      GameState copyOfCopyGS = copyGS.deepCopy();
      Pokemon copyOfUser = null;
      int opponent = 1;

      if (node.player == 1) { copyOfUser = copyOfCopyGS.player1CurrentPokemon; opponent = 2; }
      else { copyOfUser = copyOfCopyGS.player2CurrentPokemon; opponent = 1; }

      //TODO: Rflag - Causing Differences in scores
      copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(i),j,R_RANDOM,false,PRINTING_DISABLED,null,null,null);

      // Even, else odd, so ! = odd, else even
      if ( !((node.depth & 1) == 0) && node.depth > 1 ) {

      // // Even, else odd -> so ! = odd, else even
      // if ( !(( (node.depth + 1) & 1) == 0) && node.depth > 1 ) {

        copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED,null);
        // System.out.println(node.depth);

        // Switch in new pman
        if (copyOfUser.currentHealth <= 0 ) {

          // System.out.println("Test 1");

          ArrayList<Action> switches = copyOfCopyGS.generateAllValidSwitchActions(copyOfCopyGS, node.player);

          int actionIndex = switchOutFaintedLite(copyOfCopyGS, switches, node.player, PRINTING_DISABLED);

          // This player has not lost
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

          // This player has not lost
          if (actionIndex != -1) {

            copyOfCopyGS.switchOut(copyOfCopyGS, opponent, actionIndex, PRINTING_DISABLED, null);

          }

          // else { System.out.println("Did this K"); }

        }

      }

      TreeNode newNode = new TreeNode(copyOfCopyGS, opponent);
      newNode.moveIndex = i;
      newNode.probability = moveProbabilities[j];

      node.setChild(newNode);
      nodesCreated++;

      if (printMode == PRINTING_ENABLED) {
        System.out.println("TREE NODE ADDED");
      }

    }

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

      if (speedResult == player) {

        Tree minimaxTree = Tree.buildRoot(currentGSCopy, player);

        rootScore = minimax(minimaxTree.root,true,MIN,MAX,1);

      }

      else {

        Tree minimaxTree = Tree.buildRoot(currentGSCopy, opponent);

        rootScore = minimax(minimaxTree.root,false,MIN,MAX,1);

      }

      // if (printMode == PRINTING_ENABLED) {
      //
      //   System.out.println("Switching in " + yourTeam.get( possibleActions.get(i).indexOfAction ).name + " gives a score of " + rootScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");
      //
      // }

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

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
