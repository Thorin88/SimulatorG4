import java.util.*;

import static utility.Constants.*;

// ExpXI with only best case switch evaluation, but using a 2 turn
// lookahead after the created switch branch.
public class PlayerStrategyExpectimaxXII extends Player {

  int opponentPlayerNumber;

  // Team opponentTeam;
  ArrayList<Integer> opponentTeamIndexes;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  int numberOfOpponentPman;

  // Statistics

  static int expectimaxCalls = 0;
  static int nodesCreated = 0;
  static int nodesPruned = 0;

  public PlayerStrategyExpectimaxXII(int number) {

    super(number);
    super.name = "Expectimax XII";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    // opponentTeam = new Team();
    opponentTeamIndexes = new ArrayList<Integer>();
    opponentTeamAIAccess = new Team();

    this.numberOfOpponentPman = 0;

  }

  public PlayerStrategyExpectimaxXII(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Expectimax XII";

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    // opponentTeam = new Team();
    opponentTeamIndexes = new ArrayList<Integer>();
    opponentTeamAIAccess = new Team();

    this.numberOfOpponentPman = 0;

  }

  public void setDataAccessFlag(int newFlag) {

    this.dataAccessFlag = newFlag;

  }

  public void getMoveScores(ArrayList<Action> toUpDate, Tree treeBuilt, int wentFirst, int printMode) {

    // Get expected values of all the children of the root
    if (this.playerNumber == wentFirst) {

      int size = toUpDate.size();
      ArrayList<Action> moveActions = new ArrayList<Action>();
      for (int i = 0; i < size; i++) {
        if (toUpDate.get(i).isMove()) {
          moveActions.add(toUpDate.get(i));
        }
      }

      TreeNode node = treeBuilt.root;

      int myBestMoveTieBreaker = this.breakTies(node, this.playerNumber, null);

      // - infinity
      float best = MIN;
      ArrayList<Float> expectedValues = new ArrayList<Float>();

      for (int i = 0; i < 4; i++) {
        expectedValues.add(null);
      }

      int numberOfChildren = node.children.size();

      // TODO break ties and add to choosen move's score

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        // Checking if a value already exists for this element
        if (expectedValues.get(child.moveIndex) == null) {
          expectedValues.set(child.moveIndex, child.currentScore * child.probability);
        }
        else {
          expectedValues.set(child.moveIndex, expectedValues.get(child.moveIndex) + (child.currentScore * child.probability) );
        }

      }

      size = moveActions.size();

      for (int i = 0; i < size; i++) {

        Action action = moveActions.get(i);

        Float actionScore = expectedValues.get(action.indexOfAction);
        if (actionScore == null) { actionScore = MIN; }

        action.agentScore = actionScore;
        // Skewing move weights if there was a tie break
        if (action.indexOfAction == myBestMoveTieBreaker && node.indexesToPick.size() > 1) {
          action.agentScore += 0.1;
        }

      }

    }

    // Use best action
    else {

      // TODO: Break ties
      // ArrayList<Integer> opponentBestMoves = treeBuilt.root.indexesToPick;
      ArrayList<Integer> opponentBestMoves = new ArrayList<Integer>();
      // Only applies if size of best moves > 1
      int opBestMoveTieBreaker = this.breakTies(treeBuilt.root,this.opponentPlayerNumber,null);
      // Couldn't see opp's moves (PO not seen moves yet)
      if (opBestMoveTieBreaker == -1) {
        // Just treat it as though we went first
        // Have to pass a new tree with us going first
        Tree expectimaxTree = Tree.buildRoot(treeBuilt.root.nodeGameState.deepCopy(), this.playerNumber);
        expectimax(expectimaxTree.root,true,MIN,MAX,1,true);
        this.getMoveScores(toUpDate,expectimaxTree,this.playerNumber,printMode);
        return;
      }
      // TODO Do stuff if this result is -1 ( indexes = [] ).
      // System.out.println(opBestMove);
      opponentBestMoves.add(opBestMoveTieBreaker);
      // int size = opponentBestMoves.size();
      int size = treeBuilt.root.children.size();

      TreeNode bestOpNode = null;
      float highestProbSoFar = -1;

      // For all opp. moves
      for (int i = 0; i < size; i++) {

        TreeNode child = treeBuilt.root.children.get(i);

        // Might always return 0 if there were no good moves
        if ( !opponentBestMoves.contains(child.moveIndex) ) {
          continue;
        }

        // No good moves
        if (child.indexesToPick == null) { continue; }

        // Which resulting state (of this best move) to consider
        if (child.probability > highestProbSoFar) {
          highestProbSoFar = child.probability;
          bestOpNode = child;
        }

      }

      // Essentially evaluateExpectimaxTree up to this point to choose the
      // same opponent move as it would

      // System.out.println(bestOpNode.moveIndex);

      size = toUpDate.size();
      ArrayList<Action> moveActions = new ArrayList<Action>();
      for (int i = 0; i < size; i++) {
        if (toUpDate.get(i).isMove()) {
          moveActions.add(toUpDate.get(i));
        }
      }

      TreeNode node = bestOpNode;

      // + infinity
      float best = MAX;
      ArrayList<Float> expectedValues = new ArrayList<Float>();

      for (int i = 0; i < 4; i++) {
        expectedValues.add(null);
      }

      // The Pokemon had no moves set, intended to only occur at the
      // start of partially observable strategies.
      // TODO: Select a move differently here
      if (node == null || node.children.size() == 0) {
        // Already dealt with by tree gen
        if (printMode == PRINTING_ENABLED) {
          System.out.println("Unable to estimate good move");
        }
        return;
      }

      // Break the ties of these scores and add a little to the move
      // choosen's score
      int myBestMoveTieBreaker = -1;
      Pokemon myPman = null;

      if (this.playerNumber == 1) {
        myPman = node.nodeGameState.player1CurrentPokemon;
      }
      else {
        myPman = node.nodeGameState.player2CurrentPokemon;
      }

      // Tie break my moves (always use out p number)
      if (myPman.currentHealth > 0) {
        myBestMoveTieBreaker = this.breakTies(node, this.playerNumber,null);
      }
      else {
        myBestMoveTieBreaker = this.breakTies(treeBuilt.root, this.playerNumber, node.indexesToPick);
      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        if (expectedValues.get(child.moveIndex) == null) {
          expectedValues.set(child.moveIndex, child.currentScore * child.probability);
        }
        else {
          expectedValues.set(child.moveIndex, expectedValues.get(child.moveIndex) + (child.currentScore * child.probability) );
        }

      }

      size = moveActions.size();

      for (int i = 0; i < size; i++) {

        Action action = moveActions.get(i);

        Float actionScore = expectedValues.get(action.indexOfAction);
        if (actionScore == null) { actionScore = MIN; }

        action.agentScore = actionScore;
        // Skewing move weights if there was a tie break
        if (action.indexOfAction == myBestMoveTieBreaker && node.indexesToPick.size() > 1) {
          action.agentScore += 0.1;
        }

      }

    }

  }

  // Assumes that currentGS is already loaded with AI data if applicable
  public void switchOutAsActionScoresWC(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    // TODO: Extract only the switch actions

    if (possibleActions.size() == 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Player " + super.playerNumber + " has no more Pokemon able to battle.");
      }
      return;

    }

    Team yourTeam = null;

    // May be unneeded
    GameState currentGSCopy = currentGS.deepCopy();

    if (this.opponentPlayerNumber == 1) {

      yourTeam = currentGSCopy.player2Team;
      // opponentPokemon = currentGSCopy.player1CurrentPokemon;
      // opponentTeam = currentGSCopy.player1Team;
      // yourPokemon = currentGSCopy.player2CurrentPokemon;

    }
    else {

      yourTeam = currentGSCopy.player1Team;
      // opponentPokemon = currentGSCopy.player2CurrentPokemon;
      // opponentTeam = currentGSCopy.player2Team;
      // yourPokemon = currentGSCopy.player1CurrentPokemon;

    }

    // Might depend on speed of pman
    float bestScoreSoFar = MIN;
    float finalScore = MIN;
    ArrayList<Integer> goodSwitches = new ArrayList<Integer>();

    for (int i = 0; i < possibleActions.size(); i++) {

      if (!possibleActions.get(i).isSwitch()) { continue; }

      int actionToTry = possibleActions.get(i).indexOfAction;

      // Since being called when stuff has fainted, can assume we won't find
      // the current pman being used with health > 0.
      if (yourTeam.get( actionToTry ).currentHealth == 0) {

        continue;

      }

      GameState currentGSCopyCopy = currentGSCopy.deepCopy();

      // Switch in one of the agent's other pokemon
      currentGSCopyCopy.switchOut(currentGSCopyCopy, this.playerNumber, actionToTry, PRINTING_DISABLED, null);

      // Simulate the opponents next move
      Tree expectimaxTree = Tree.buildRoot(currentGSCopyCopy, this.opponentPlayerNumber);
      TreeNode root = expectimaxTree.root;
      // +1 to depth since we used up our move to switch something in
      root.depth = 2;

      // Always using false here since the opponent is making their
      // move. This is only adding one layer as d = 2 already
      expectimax(root,false,MIN,MAX,2,true);

      int numOfRootChildren = root.children.size();

      // Do not know any opponent moves
      if (numOfRootChildren <= 0) {
        if (printMode == PRINTING_ENABLED) {
          System.out.println("No reliable switch scores");
        }
        // Better than returning 0.0, since the switches will now match
        // move scores which were unknown. Avoids PO really liking switch outs
        for (int k = 0; k < possibleActions.size(); k++) {
          // Can use MIN since these final scores always used from maxing
          // perspective.
          if (!possibleActions.get(k).isSwitch()) { continue; }
          possibleActions.get(k).agentScore = MIN_INVALID;
          // setting to root.currentScore is not ideal because the score of
          // the current state could be better than the outcome of a move
        }
        return;
      }

      int speedResult = currentGSCopyCopy.whichPokemonIsFaster(currentGSCopyCopy, this.playerNumber);
      Boolean isMaxing = (speedResult == this.playerNumber);

      for (int j = 0; j < numOfRootChildren; j++) {

        TreeNode currentChild = root.children.get(j);
        // Set up for the next exp calls to work in the correct order
        currentChild.player = speedResult;

        // Child is at depth 3, want to get to d = 5, so pass 4

        // Now doing the next turn, so want to do it in the correct speed
        // ordering. Note that it is called from the currentChild
        // currentChild.currentScore = expectimax(currentChild,isMaxing,MIN,MAX,4,true);

      }

      // Final call from the root, stops at the children whos scores we set
      // Need to prevent it calculating score of nodes, so last parameter is false

      // ^ Not needed anymore since tree is restructured by expectimax now

      // Evaluate the tree, with the later turns reordered by the new expectimax
      finalScore = expectimax(root,false,MIN,MAX,6,true);

      if (printMode == PRINTING_ENABLED) {

        System.out.println("Switching in " + yourTeam.get( actionToTry ).name + " gives a score of " + finalScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");

      }

      // For UI
      possibleActions.get(i).agentScore = finalScore;

      // Always >= since we always do this from our perspective
      // if (finalScore >= bestScoreSoFar) {
      //
      //   if (finalScore > bestScoreSoFar) {
      //
      //     bestScoreSoFar = finalScore;
      //     goodSwitches.clear();
      //
      //   }
      //
      //   // This is meant to be i
      //   goodSwitches.add(i);
      //
      // }

    }

    // Just editing possible actions

    // if (goodSwitches.size() != 0) {
    //
    //   // Possibly have a tie breaker here
    //   return possibleActions.get( goodSwitches.get(0) );
    //
    // }
    //
    // return possibleActions.get(0);

  }

  // Assumes that currentGS is already loaded with AI data if applicable

  // If average is true, assumes another scorer added scores to the switch actions
  // and averages those scores with these scores.
  public void switchOutAsActionScores(Tree treeBuilt, GameState currentGS, ArrayList<Action> possibleActions, int printMode, Boolean average) {

    // TODO: Extract only the switch actions

    if (possibleActions.size() == 0) {

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Player " + super.playerNumber + " has no more Pokemon able to battle.");
      }
      return;

    }

    Team yourTeam = null;

    // May be unneeded
    GameState currentGSCopy = currentGS.deepCopy();

    float startScore = scoreGameState(currentGSCopy);

    if (this.opponentPlayerNumber == 1) {

      yourTeam = currentGSCopy.player2Team;
      // opponentPokemon = currentGSCopy.player1CurrentPokemon;
      // opponentTeam = currentGSCopy.player1Team;
      // yourPokemon = currentGSCopy.player2CurrentPokemon;

    }
    else {

      yourTeam = currentGSCopy.player1Team;
      // opponentPokemon = currentGSCopy.player2CurrentPokemon;
      // opponentTeam = currentGSCopy.player2Team;
      // yourPokemon = currentGSCopy.player1CurrentPokemon;

    }

    // Might depend on speed of pman
    float bestScoreSoFar = MIN;
    float finalScore = MIN;
    ArrayList<Integer> goodSwitches = new ArrayList<Integer>();

    for (int i = 0; i < possibleActions.size(); i++) {

      if (!possibleActions.get(i).isSwitch()) { continue; }

      int actionToTry = possibleActions.get(i).indexOfAction;

      // Since being called when stuff has fainted, can assume we won't find
      // the current pman being used with health > 0.
      if (yourTeam.get( actionToTry ).currentHealth == 0) {

        continue;

      }

      GameState currentGSCopyCopy = currentGSCopy.deepCopy();

      int speedResult = currentGSCopyCopy.whichPokemonIsFaster(currentGSCopyCopy, this.playerNumber);
      ArrayList<Integer> bestOpIndexes = new ArrayList<Integer>();

      // Go one layer in
      if (speedResult == this.opponentPlayerNumber) {

        // true just means look at the indexes at the first level
        bestOpIndexes = evaluateExpectimaxTree(treeBuilt, true, true);

      }
      // Could be combined but left separate to show it could be different
      else {

        bestOpIndexes = evaluateExpectimaxTree(treeBuilt, false, true);

      }

      // System.out.println("[P" + this.playerNumber + "]: " + bestOpIndexes);

      // Dealing with the case were PO cannot see op moves

      // null case is due to PO not being able to see moves
      // 0 size case is due to the opponent not being able to see
      // their best move cause our move won and we stopped generating
      // the tree.
      if (bestOpIndexes == null || bestOpIndexes.size() <= 0) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("No reliable switch scores");
        }
        // Better than returning 0.0, since the switches will now match
        // move scores which were unknown. Avoids PO really liking switch outs
        for (int k = 0; k < possibleActions.size(); k++) {
          // Can use MIN since these final scores always used from maxing
          // perspective.
          if (!possibleActions.get(k).isSwitch()) { continue; }
          possibleActions.get(k).agentScore = MIN_INVALID;
          // setting to root.currentScore is not ideal because the score of
          // the current state could be better than the outcome of a move
        }
        return;

      }

      int opMoveIndex = bestOpIndexes.get(0);

      // Switch in one of the agent's other pokemon
      currentGSCopyCopy.switchOut(currentGSCopyCopy, this.playerNumber, actionToTry, PRINTING_DISABLED, null);

      // Simulate the opponents next move
      Tree expectimaxTree = Tree.buildRoot(currentGSCopyCopy, this.opponentPlayerNumber);
      TreeNode root = expectimaxTree.root;
      // +1 to depth since we used up our move to switch something in
      root.depth = 2;

      // Find op best move

      // Always using false here since the opponent is making their
      // move. This is only adding one layer as d = 2 already
      expectimax(root,false,MIN,MAX,2,true);

      int numOfRootChildren = root.children.size();

      // Do not know any opponent moves
      if (numOfRootChildren <= 0) {
        if (printMode == PRINTING_ENABLED) {
          System.out.println("No reliable switch scores");
        }
        // Better than returning 0.0, since the switches will now match
        // move scores which were unknown. Avoids PO really liking switch outs
        for (int k = 0; k < possibleActions.size(); k++) {
          // Can use MIN since these final scores always used from maxing
          // perspective.
          if (!possibleActions.get(k).isSwitch()) { continue; }
          possibleActions.get(k).agentScore = MIN_INVALID;
          // setting to root.currentScore is not ideal because the score of
          // the current state could be better than the outcome of a move
        }
        return;
      }

      speedResult = currentGSCopyCopy.whichPokemonIsFaster(currentGSCopyCopy, this.playerNumber);
      Boolean isMaxing = (speedResult == this.playerNumber);

      ArrayList<TreeNode> toRemove = new ArrayList<TreeNode>();

      for (int j = 0; j < numOfRootChildren; j++) {

        TreeNode currentChild = root.children.get(j);

        // Only want to consider op's best move for prev pman
        if (currentChild.moveIndex != opMoveIndex) {
          toRemove.add(currentChild);
        }

      }

      root.children.removeAll(toRemove);

      numOfRootChildren = root.children.size();

      // Fix if this is somehow 0 in PO case
      if (this.dataAccessFlag != FULLY_OBSERVABLE && numOfRootChildren == 0) {
        root.children.add(toRemove.get(0));
      }

      if (printMode == PRINTING_ENABLED) {
        // System.out.println(numOfRootChildren);
        System.out.println(root.children.get(0).moveIndex);
      }

      for (int j = 0; j < numOfRootChildren; j++) {

        TreeNode currentChild = root.children.get(j);

        // Set up for the next exp calls to work in the correct order
        currentChild.player = speedResult;

        // Child is at depth 3, want to get to d = 5, so pass 4

        // Now doing the next turn, so want to do it in the correct speed
        // ordering. Note that it is called from the currentChild
        // currentChild.currentScore = expectimax(currentChild,isMaxing,MIN,MAX,4,true);

      }

      // Final call from the root, stops at the children whos scores we set
      // Need to prevent it calculating score of nodes, so last parameter is false

      // ^ Not needed anymore since tree is restructured by expectimax now

      // Evaluate the tree, with the later turns reordered by the new expectimax
      finalScore = expectimax(root,false,MIN,MAX,6,true);

      if (printMode == PRINTING_ENABLED) {

        System.out.println("Switching in " + yourTeam.get( actionToTry ).name + " gives a score of " + finalScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");

      }

      // TODO: Possibly skew switches

      // For UI
      if (!average) {
        possibleActions.get(i).agentScore = finalScore;
      }
      // Average the stored value
      else {
        possibleActions.get(i).agentScore += finalScore;
        possibleActions.get(i).agentScore = possibleActions.get(i).agentScore / 2;
      }

      // Always >= since we always do this from our perspective
      // if (finalScore >= bestScoreSoFar) {
      //
      //   if (finalScore > bestScoreSoFar) {
      //
      //     bestScoreSoFar = finalScore;
      //     goodSwitches.clear();
      //
      //   }
      //
      //   // This is meant to be i
      //   goodSwitches.add(i);
      //
      // }

    }

    // Just editing possible actions

    // if (goodSwitches.size() != 0) {
    //
    //   // Possibly have a tie breaker here
    //   return possibleActions.get( goodSwitches.get(0) );
    //
    // }
    //
    // return possibleActions.get(0);

  }

  // TODO
  // Update score of possible actions
  public Action chooseAction(GameState currentGS, ArrayList<Action> possibleActions, int printMode) {

    // Statistics
    expectimaxCalls = 0;
    nodesCreated = 0;
    nodesPruned = 0;

    Pokemon opponentPokemon = null;
    Team opponentTeam = null;
    Pokemon yourPokemon = null;

    // Load in AI access pman to currentGS copy

    // Adds Pokemon to AIAccess
    this.pokemonSeen(currentGS,PRINTING_DISABLED);

    // New pokemon added at this point
    GameState currentGSCopy = currentGS.deepCopy();

    // Loading AIAccess into currentGS

    if (this.dataAccessFlag != FULLY_OBSERVABLE) {

      if (this.opponentPlayerNumber == 1) {

        opponentPokemon = currentGSCopy.player1CurrentPokemon;
        opponentTeam = currentGSCopy.player1Team;
        yourPokemon = currentGSCopy.player2CurrentPokemon;

      }
      else {

        opponentPokemon = currentGSCopy.player2CurrentPokemon;
        opponentTeam = currentGSCopy.player2Team;
        yourPokemon = currentGSCopy.player1CurrentPokemon;

      }

      // Can be used to access the relevant pman in either the full or inferable pokemon team.
      int indexOfOpponentCurrentPman = opponentTeam.indexOf(opponentPokemon);
      indexOfOpponentCurrentPman = opponentTeamIndexes.indexOf(indexOfOpponentCurrentPman);


      // Updating changes to pman during the previous turn
      int numOfOpPmanSeen = this.opponentTeamIndexes.size();
      for (int i = 0; i < numOfOpPmanSeen; i++) {

        Pokemon tempPman = opponentTeam.get( opponentTeamIndexes.get(i) );

        ArrayList<StatChange> statChangesCopy = new ArrayList<StatChange>();

        int size = tempPman.statChangeLevels.size();
        for (int j = 0; j < size; j++) {
          statChangesCopy.add( tempPman.statChangeLevels.get(j).deepCopy() );
        }

        this.opponentTeamAIAccess.get(i).statChangeLevels = statChangesCopy;

        // PO uses HP % lost not the exact amount

        if (tempPman.currentHealth <= 0) {
          this.opponentTeamAIAccess.get(i).currentHealth = 0;
        }
        else {
          int healthUpdate = (int) ((tempPman.currentHealth / (float) tempPman.stats.get(0).getStatValue()) * this.opponentTeamAIAccess.get(i).stats.get(0).getStatValue());
          // Account for slight rounding errors
          if (healthUpdate <= 0) { healthUpdate = 1; }
          this.opponentTeamAIAccess.get(i).currentHealth = healthUpdate;
        }

        if (printMode == PRINTING_ENABLED) {
          System.out.println("[P" + this.playerNumber + "] Updated HP (" + this.opponentTeamAIAccess.get(i).name + "): " + this.opponentTeamAIAccess.get(i).currentHealth);
        }
        // this.opponentTeamAIAccess.get(i).currentHealth = tempPman.currentHealth;

        this.opponentTeamAIAccess.get(i).statusUnaffectedBySwitching = tempPman.statusUnaffectedBySwitching.deepCopy();
        this.opponentTeamAIAccess.get(i).statusAffectedBySwitching = tempPman.statusAffectedBySwitching.deepCopy();

      }

      // Loading Pokemon from AI Access into the currentGS copy
      if (this.opponentPlayerNumber == 1) {
        currentGSCopy.player1Team = this.opponentTeamAIAccess;
        currentGSCopy.player1CurrentPokemon = this.opponentTeamAIAccess.get(indexOfOpponentCurrentPman);
      }
      else {
        currentGSCopy.player2Team = this.opponentTeamAIAccess;
        currentGSCopy.player2CurrentPokemon = this.opponentTeamAIAccess.get(indexOfOpponentCurrentPman);
      }
    }

    // Minimax

    Tree expectimaxTree;

    ArrayList<Integer> highestScoringActions;

    int speedResult = currentGSCopy.whichPokemonIsFaster(currentGSCopy, this.playerNumber);

    // Will return random if same, we want it to always be us if its a tie.
    if (speedResult == this.playerNumber) {

      expectimaxTree = Tree.buildRoot(currentGSCopy, this.playerNumber);

      // If negative, could evaluate switches
      expectimax(expectimaxTree.root,true,MIN,MAX,4,true);

      highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, true, false);

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

      expectimaxTree = Tree.buildRoot(currentGSCopy, this.opponentPlayerNumber);

      expectimax(expectimaxTree.root,false,MIN,MAX,4 /* + 1 */,true);

      highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, false, false);

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

    int sizeOfPA = possibleActions.size();

    if (sizeOfPA == 0) {

      return null;

    }

    this.getMoveScores(possibleActions, expectimaxTree, speedResult, printMode);
    // this.switchOutAsActionScoresWC(currentGSCopy, possibleActions, printMode);
    this.switchOutAsActionScores(expectimaxTree, currentGSCopy, possibleActions, printMode, false);

    if (printMode == PRINTING_ENABLED) {
      System.out.println(possibleActions);
    }

    float bestScoreSoFar = MIN;
    ArrayList<Action> bestActions = new ArrayList<Action>();

    for (int i = 0; i < sizeOfPA; i++) {

      float score = possibleActions.get(i).agentScore;

      if (score >= bestScoreSoFar) {

        if (score > bestScoreSoFar) {

          bestScoreSoFar = score;
          bestActions.clear();

        }

        // This is meant to be i
        bestActions.add( possibleActions.get(i) );

      }

    }

    if (printMode == PRINTING_ENABLED) {
      System.out.println("New best actions: " + bestActions.toString());
    }

    // No 0 size check
    return bestActions.get(0);

    // Old
    // return possibleActions.get(indexOfAction);

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
    Pokemon opponentPokemon = null;
    Team opponentTeam = null;
    Pokemon yourPokemon = null;

    // Loading AIAccess into currentGS
    GameState currentGSCopy = currentGS.deepCopy();

    if (this.opponentPlayerNumber == 1) {

      yourTeam = currentGSCopy.player2Team;
      opponentPokemon = currentGSCopy.player1CurrentPokemon;
      opponentTeam = currentGSCopy.player1Team;
      yourPokemon = currentGSCopy.player2CurrentPokemon;

    }
    else {

      yourTeam = currentGSCopy.player1Team;
      opponentPokemon = currentGSCopy.player2CurrentPokemon;
      opponentTeam = currentGSCopy.player2Team;
      yourPokemon = currentGSCopy.player1CurrentPokemon;

    }

    // Loading AIAccess into currentGS

    if (this.dataAccessFlag != FULLY_OBSERVABLE) {

      // Can be used to access the relevant pman in either the full or inferable pokemon team.
      int indexOfOpponentCurrentPman = opponentTeam.indexOf(opponentPokemon);
      indexOfOpponentCurrentPman = opponentTeamIndexes.indexOf(indexOfOpponentCurrentPman);

      // Updating changes to pman during the previous turn
      int numOfOpPmanSeen = this.opponentTeamIndexes.size();
      for (int i = 0; i < numOfOpPmanSeen; i++) {

        Pokemon tempPman = opponentTeam.get( opponentTeamIndexes.get(i) );

        ArrayList<StatChange> statChangesCopy = new ArrayList<StatChange>();

        int size = tempPman.statChangeLevels.size();
        for (int j = 0; j < size; j++) {
          statChangesCopy.add( tempPman.statChangeLevels.get(j).deepCopy() );
        }

        this.opponentTeamAIAccess.get(i).statChangeLevels = statChangesCopy;

        // PO uses HP % lost not the exact amount

        if (tempPman.currentHealth <= 0) {
          this.opponentTeamAIAccess.get(i).currentHealth = 0;
        }
        else {
          int healthUpdate = (int) ((tempPman.currentHealth / (float) tempPman.stats.get(0).getStatValue()) * this.opponentTeamAIAccess.get(i).stats.get(0).getStatValue());
          // Account for slight rounding errors
          if (healthUpdate <= 0) { healthUpdate = 1; }
          this.opponentTeamAIAccess.get(i).currentHealth = healthUpdate;
        }

        if (printMode == PRINTING_ENABLED) {
          System.out.println("[P" + this.playerNumber + "] Updated HP (" + this.opponentTeamAIAccess.get(i).name + "): " + this.opponentTeamAIAccess.get(i).currentHealth);
        }

        // this.opponentTeamAIAccess.get(i).currentHealth = tempPman.currentHealth;

        this.opponentTeamAIAccess.get(i).statusUnaffectedBySwitching = tempPman.statusUnaffectedBySwitching.deepCopy();
        this.opponentTeamAIAccess.get(i).statusAffectedBySwitching = tempPman.statusAffectedBySwitching.deepCopy();

      }

      // Loading Pokemon from AI Access into the currentGS copy
      if (this.opponentPlayerNumber == 1) {
        currentGSCopy.player1Team = this.opponentTeamAIAccess;
        currentGSCopy.player1CurrentPokemon = this.opponentTeamAIAccess.get(indexOfOpponentCurrentPman);
      }
      else {
        currentGSCopy.player2Team = this.opponentTeamAIAccess;
        currentGSCopy.player2CurrentPokemon = this.opponentTeamAIAccess.get(indexOfOpponentCurrentPman);
      }
    }

    // PO has now been loaded if needed

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

      GameState currentGSCopyCopy = currentGSCopy.deepCopy();

      currentGSCopyCopy.switchOut(currentGSCopyCopy, this.playerNumber, actionToTry, PRINTING_DISABLED, null);

      // Score this switch in.

      // TODO: These scores need to be evaluated, not just root score used.
      // Same for switch-out light

      int speedResult = currentGSCopyCopy.whichPokemonIsFaster(currentGSCopyCopy, this.playerNumber);

      if (speedResult == this.playerNumber) {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopyCopy, this.playerNumber);

        rootScore = expectimax(expectimaxTree.root,true,MIN,MAX,4,true);

        // ArrayList<Integer> highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, true);
        //
        // System.out.println(highestScoringActions);

        // System.out.println(expectimaxTree.root.children.size());
        //
        // System.out.println(expectimaxTree.root.children.get(0).currentScore);
        // System.out.println(expectimaxTree.root.children.get(0).moveIndex);
        // System.out.println(expectimaxTree.root.children.get(1).currentScore);
        // System.out.println(expectimaxTree.root.children.get(1).moveIndex);
        // // Hit
        // System.out.println(expectimaxTree.root.children.get(1).nodeGameState.humanReadableStateInfo());
        // System.out.println(expectimaxTree.root.children.get(1).children.get(2).nodeGameState.humanReadableStateInfo());


      }

      else {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopyCopy, this.opponentPlayerNumber);

        rootScore = expectimax(expectimaxTree.root,false,MIN,MAX,4,true);

        // ArrayList<Integer> highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, true);
        //
        // System.out.println(highestScoringActions);

        // Need to look at these scores
        // highestScoringActions = this.evaluateExpectimaxTree(expectimaxTree, false);

      }

      if (printMode == PRINTING_ENABLED) {

        System.out.println("Switching in " + yourTeam.get( actionToTry ).name + " gives a score of " + rootScore + " (Faster than opponent? : " + (speedResult == this.playerNumber) + ") ");

      }

      // For UI
      possibleActions.get(i).agentScore = rootScore;

      // Always >= since we always do this from our perspective
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
  // Already handles PO, since just uses the GSs from the tree, which would
  // have already been loaded with PO or FO data.
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

        rootScore = expectimax(expectimaxTree.root,true,MIN,MAX,1,true);

      }

      else {

        Tree expectimaxTree = Tree.buildRoot(currentGSCopy, opponent);

        rootScore = expectimax(expectimaxTree.root,false,MIN,MAX,1,true);

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
      // System.out.println( possibleActions.get( goodSwitches.get(0) ).indexOfAction );
      return possibleActions.get( goodSwitches.get(0) ).indexOfAction;

    }

    if (possibleActions.size() == 0) {

      return -1;

    }

    // Can return null
    return possibleActions.get(0).indexOfAction;

  }

  public void pokemonSeen(GameState currentGS, int printMode) {

    if (this.dataAccessFlag == FULLY_OBSERVABLE) { return; }

    Pokemon opponentPokemon = null;
    Team opponentTeam = null;
    Pokemon yourPokemon = null;

    // Load in AI access pman to currentGS copy

    if (this.opponentPlayerNumber == 1) {

      opponentPokemon = currentGS.player1CurrentPokemon;
      opponentTeam = currentGS.player1Team;
      yourPokemon = currentGS.player2CurrentPokemon;

    }
    else {

      opponentPokemon = currentGS.player2CurrentPokemon;
      opponentTeam = currentGS.player2Team;
      yourPokemon = currentGS.player1CurrentPokemon;

    }

    int indexOfOpponentCurrentPman = opponentTeam.indexOf(opponentPokemon);
    Boolean seenPman = this.opponentTeamIndexes.contains(indexOfOpponentCurrentPman);

    // Initial recording of total op team size
    if (this.dataAccessFlag != FULLY_OBSERVABLE && this.opponentTeamIndexes.size() == 0) {

      if (this.opponentPlayerNumber == 1) {
        this.numberOfOpponentPman = currentGS.player1Team.size();
      }
      else {
        this.numberOfOpponentPman = currentGS.player2Team.size();
      }

    }

    // TODO: Make pokemon for AI access list.
    if (this.dataAccessFlag != FULLY_OBSERVABLE && !seenPman) {

      // Adding pokemon that the AI won't access but will use to fill-out its own pokemon data.
      // NOTE: Indexes do not match gamestate team indexes
      this.opponentTeamIndexes.add(indexOfOpponentCurrentPman);

      // Full data access
      if (this.dataAccessFlag == FULLY_OBSERVABLE) {

        this.opponentTeamAIAccess.add(opponentPokemon);

      }

      // Inferable data access
      else {

        Pokemon newAIPman = new Pokemon();
        String[] noMoves = {"","","",""};
        // Assuming form is always 0
        newAIPman.buildUsingMoveNames(opponentPokemon.id,0,noMoves,0,PRINTING_DISABLED);

        int[] perfectIVs = {31,31,31,31,31,31};
        int[] noEVs = {0,0,0,0,0,0};
        // Initial estimate
        newAIPman.levelUpPokemon(opponentPokemon.level,perfectIVs,noEVs);

        this.opponentTeamAIAccess.add(newAIPman);


      }

      // this.opponentTeamAIAccess.add();

    }

  }

  public void moveSeen(GameState currentGS, Pokemon user, Pokemon target, int moveIndex, Estimates estimates, int printMode) {

    // Already has access to move data.
    if (this.dataAccessFlag == FULLY_OBSERVABLE) { return; }

    Team opponentTeam = null;

    if (this.opponentPlayerNumber == 1) {

      // opponentPokemon = currentGS.player1CurrentPokemon;
      opponentTeam = currentGS.player1Team;
      // yourPokemon = currentGS.player2CurrentPokemon;

    }
    else {

      // opponentPokemon = currentGS.player2CurrentPokemon;
      opponentTeam = currentGS.player2Team;
      // yourPokemon = currentGS.player1CurrentPokemon;

    }

    Move moveSeen = user.moves.get(moveIndex);
    String damageType = moveSeen.metaData.dmgType;

    if (estimates != null && estimates.totalHPRemoved != -1) {

      // Working out estimated HP loss from HP percentageHPLost

      // What a human can see
      int totalHPRemoved = -1;

      // Use real HP if this.num != attacker, estimated otherwise

      // We attacked, get the HP estimate
      if (this.playerNumber == estimates.attacker) {

        if (printMode == PRINTING_ENABLED) {
          System.out.println("Using estimated HP Loss");
        }

        int indexOfTarget = opponentTeam.indexOf(target);
        indexOfTarget = opponentTeamIndexes.indexOf(indexOfTarget);
        // indexOfTarget = opponentTeamAIAccess.indexOf(indexOfTarget);
        // if (indexOfTarget == -1) { return; }

        int opponentMaxHP = this.opponentTeamAIAccess.get(indexOfTarget).stats.get(0).getStatValue();
        totalHPRemoved = (int) (((double) estimates.totalHPRemoved / opponentMaxHP) * opponentMaxHP);

      }
      // We took damage
      else {
        if (printMode == PRINTING_ENABLED) {
          System.out.println("Using actual HP Loss");
        }
        totalHPRemoved = estimates.totalHPRemoved;
      }

      double damageDealtRLow = totalHPRemoved / estimates.rLow;
      double damageDealtRHigh = totalHPRemoved / estimates.rHigh;

      double aDLow = ((damageDealtRLow - 2)*50) / (estimates.levelCalc*estimates.power);

      double aDHigh = ((damageDealtRHigh - 2)*50) / (estimates.levelCalc*estimates.power);

      // if (printMode == PRINTING_ENABLED) {
      //
      //   System.out.println(rLow);
      //   System.out.println(rHigh);
      //   System.out.println(totalHPRemoved);
      //   System.out.println(damageDealtRLow);
      //   System.out.println(damageDealtRHigh);
      //   System.out.println("A/Ds");
      //   System.out.println(aDLow);
      //   System.out.println(aDHigh);
      //
      //   // System.out.println(levelCalc*power);
      //   // System.out.println((damageDealtRHigh - 2)*50);
      //
      // }

      // Estimate using real user atk

      int estimatedEffectiveDefLow = (int) ( estimates.userActualEffectiveAtkStat / aDHigh);
      int estimatedEffectiveDefHigh = (int) ( estimates.userActualEffectiveAtkStat / aDLow);

      int estimatedEffectiveAtkLow = (int) ( aDLow * estimates.targetActualEffectiveDefStat);
      int estimatedEffectiveAtkHigh = (int) ( aDHigh * estimates.targetActualEffectiveDefStat);

      // Estimate using real target def

      if (printMode == PRINTING_ENABLED) {

        System.out.println("User Effective Atk estimates: ");
        System.out.println(estimatedEffectiveAtkLow);
        System.out.println(estimatedEffectiveAtkHigh);
        System.out.println("Target Effective Def estimates: ");
        System.out.println(estimatedEffectiveDefLow);
        System.out.println(estimatedEffectiveDefHigh);

      }

      int estimatedAtkStatLow = (int) (estimatedEffectiveAtkLow / estimates.userAtkMulitplier);
      int estimatedAtkStatHigh = (int) (estimatedEffectiveAtkLow / estimates.userAtkMulitplier);

      int estimatedDefStatLow = (int) (estimatedEffectiveDefLow / estimates.targetDefMulitplier);
      int estimatedDefStatHigh = (int) (estimatedEffectiveDefHigh / estimates.targetDefMulitplier);

      estimates.addAtkEstimate( ((estimatedAtkStatLow + estimatedAtkStatHigh) / 2), damageType );
      estimates.addDefEstimate( ((estimatedDefStatLow + estimatedDefStatHigh) / 2), damageType );

    }

    // Can return -1;
    // Tells us which AI access pman to add to.
    // System.out.println(this.opponentTeam.size());
    int indexOfUser = opponentTeam.indexOf(user);
    indexOfUser = opponentTeamIndexes.indexOf(indexOfUser);

    // Was a move by the agent's Pokemon - update their attack stats
    if (indexOfUser != -1) {

      // System.out.println(indexOfPman);
      // System.out.println(user.name);
      // System.out.println(this.opponentTeam.contains(user));

      // Sets everytime, could change this to reduce waste

      // Add move seen
      this.opponentTeamAIAccess.get(indexOfUser).moves.set(moveIndex,user.moves.get(moveIndex));

      estimates.updateStats(this.opponentTeamAIAccess.get(indexOfUser), this.playerNumber);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Updated Offensive Stat Estimates: ");
        System.out.println(this.opponentTeamAIAccess.get(indexOfUser).stats.toString());
      }

    }

    // Update their defense stats
    else {

      int indexOfTarget = opponentTeam.indexOf(target);
      indexOfTarget = opponentTeamIndexes.indexOf(indexOfTarget);

      estimates.updateStats(this.opponentTeamAIAccess.get(indexOfTarget), this.playerNumber);

      if (printMode == PRINTING_ENABLED) {
        System.out.println("Updated Defensive Stat Estimates: ");
        System.out.println(this.opponentTeamAIAccess.get(indexOfTarget).stats.toString());
      }

    }

    // System.out.println(estimates.toString());

    // System.out.println(user.moves.get(moveIndex).moveName);

  }

  // Evaluate tree assuming this player is the root node
  // BreakTies used to return the best move with tie breakers. maxing in the
  // tiebreaker logic refers to whether the player to tiebreak for is playing at the root
  // or not
  public ArrayList<Integer> evaluateExpectimaxTree(Tree tree, Boolean maxing, Boolean breakTies) {

    ArrayList<Integer> toReturn = new ArrayList<Integer>();

    if (maxing) {

      toReturn = tree.root.indexesToPick;

      // PO - Couldn't see OPs moves
      if (toReturn == null) {
        // Picked up by callers (eg switchOutAsActionScores)
        return toReturn;
      }

      if (breakTies && toReturn.size() > 0) {
        int bestIndex = this.breakTies(tree.root,tree.root.player,null);
        toReturn.clear();
        toReturn.add(bestIndex);
      }

      // System.out.println(toReturn.size());

    }

    else {

      ArrayList<Integer> opponentBestMoves = null;
      if (breakTies) {
        opponentBestMoves = new ArrayList<Integer>();
        // Only applies if size of best moves > 1
        int opBestMoveTieBreaker = this.breakTies(tree.root,tree.root.player,null);
        // TODO Do stuff if this result is -1 ( indexes = [] ).
        // System.out.println(opBestMove);
        opponentBestMoves.add(opBestMoveTieBreaker);
      }
      else {
        opponentBestMoves = tree.root.indexesToPick;
      }

      int size = tree.root.children.size();

      float highestProbSoFar = -1;
      TreeNode bestOpNode = null;
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
          bestOpNode = child;
        }

      }

      if (breakTies) {

        int myNum = 1;
        // We aren't always doing this from our perspective, so cannot use
        // the value: this.playerNumber
        if (tree.root.player == 1) { myNum = 2; }

        int myBestMoveTieBreaker = -1;
        if (bestOpNode != null) {
          Pokemon myPman = null;
          if (myNum == 1) {
            myPman = bestOpNode.nodeGameState.player1CurrentPokemon;
          }
          else {
            myPman = bestOpNode.nodeGameState.player2CurrentPokemon;
          }
          if (myPman.currentHealth > 0) {
            myBestMoveTieBreaker = this.breakTies(bestOpNode, myNum, null);
          }
          else {
            myBestMoveTieBreaker = this.breakTies(tree.root, myNum, bestOpNode.indexesToPick);
          }
        }
        // Unable to see op move
        else {
          // Use the root GS to get some kind of estimate
          // We aren't always doing this from our perspective, so cannot use
          // the value: this.playerNumber
          // TODO: Use pman moves to generate estimate since no indexes on any
          // of this player's nodes
          myBestMoveTieBreaker = -1;
          // myBestMoveTieBreaker = this.breakTies(tree.root, myNum);
        }
        // No best moves
        if (myBestMoveTieBreaker != -1) {
          toReturn.clear();
          toReturn.add(myBestMoveTieBreaker);
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

        // score -= 1;
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

        // score += 1;
        fainted++;

      }

      // Add percent of pman remaining health

      else {

        score -= opTeam.get(i).currentHealth / (float) opTeam.get(i).stats.get(0).getStatValue();

      }

    }

    // To allow PO to see there are more unknown Pokemon
    if (this.dataAccessFlag != FULLY_OBSERVABLE) {

      // -1 for each extra unknown Pokemon
      score -= this.numberOfOpponentPman - size;

      // Avoid the fainted==size check if we haven't seen everything
      if (size != this.numberOfOpponentPman) {
        return score;
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
  public float expectimax(TreeNode node, boolean maxingPlayer, float bestForMax, float bestForMin, int maxDepth, Boolean reScore) {

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

      // Allows eval to be done without rescoring nodes. Allows lower sections
      // of the tree (for switch action score evals) to be exp'd in a different
      // player order than the top section of the tree.
      if (reScore) {
        node.currentScore = this.scoreGameState(node.nodeGameState);
      }
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
      // The Pokemon had no moves set, intended to only occur at the
      // start of partially observable strategies.
      if (node.children.size() == 0) {
        node.currentScore = this.scoreGameState(node.nodeGameState);
        return node.currentScore;
      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        // Speed result has changed (done in bEC)
        if (node.player == child.player) {
          // System.out.println("[P" + this.playerNumber + "] " + node.depth + "," + child.depth + "[" + node.player + "---" + child.player + "]");
          // System.out.println(node.nodeGameState.humanReadableStateInfo());
          // System.out.println(child.nodeGameState.humanReadableStateInfo());
          expectimax(child, true, bestForMax, bestForMin, maxDepth, reScore);
        }
        else {
          // Original
          expectimax(child, false, bestForMax, bestForMin, maxDepth, reScore);
        }

        // Still calculating this node as normal, just changed layout of tree
        // past this node.

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

      // Does this change the meaning of the indexes stored?
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
      // The Pokemon had no moves set, intended to only occur at the
      // start of partially observable strategies.
      if (node.children.size() == 0) {
        node.currentScore = this.scoreGameState(node.nodeGameState);
        return node.currentScore;
      }

      int numberOfChildren = node.children.size();

      for (int i = 0; i < numberOfChildren; i++) {

        TreeNode child = node.children.get(i);

        // Speed result has changed (done in bEC)
        if (node.player == child.player) {
          // System.out.println(node.depth + "," + child.depth + "[" + node.player + "---" + child.player + "]");
          // System.out.println(node.nodeGameState.humanReadableStateInfo());
          // System.out.println(child.nodeGameState.humanReadableStateInfo());
          expectimax(child, false, bestForMax, bestForMin, maxDepth, reScore);
        }
        else {
          // Original
          expectimax(child, true, bestForMax, bestForMin, maxDepth, reScore);
        }

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

      // break; here previously (incorrect)
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

        // Uses the same R each time
        copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(i),j,R_MEDIUM_1,false,PRINTING_DISABLED,null,null,null);

        Boolean didASwitch = false;

        // Even, else odd, so ! = odd, else even
        // if ( !((node.depth & 1) == 0) && node.depth > 1 ) {
        if ( !(( (node.depth + 1) & 1) == 0) && node.depth > 1 ) {
        // if ( !(( (node.depth + 1) & 1) == 0) && node.depth > 1 && node.depth <= 3 ) {

          didASwitch = true;

          copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED, null);
          // System.out.println(node.depth);

          // Switch in new pman
          if (copyOfUser.currentHealth <= 0 ) {

            // System.out.println("Test 1");

            ArrayList<Action> switches = copyOfCopyGS.generateAllValidSwitchActions(copyOfCopyGS, node.player);

            int actionIndex = switchOutFaintedLite(copyOfCopyGS, switches, node.player, PRINTING_DISABLED);

            // This player has lost
            if (actionIndex != -1) {

              // didASwitch = true;
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

              // didASwitch = true;
              copyOfCopyGS.switchOut(copyOfCopyGS, opponent, actionIndex, PRINTING_DISABLED, null);
              // System.out.println("(" + node.depth + ")" + copyOfCopyGS.player1CurrentPokemon.name + " after " + copyOfUser.moves.get(i).moveName);

            }

            // else { System.out.println("Did this K"); }

          }

          // End switch checks

        }

        TreeNode newNode = new TreeNode(copyOfCopyGS, opponent);
        newNode.moveIndex = i;
        newNode.probability = moveProbabilities[j];

        // Children are now built in the order given by this result
        // (Spin trees)
        if (didASwitch) {
          int speedResult = copyOfCopyGS.whichPokemonIsFaster(copyOfCopyGS, this.playerNumber);
          newNode.player = speedResult;
          // if (speedResult != opponent) {
          //   System.out.println("Did this");
          // }
        }

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

  // public int getEstimatedHPOfPman(Pokemon pman) {
  //
  //   if (this.dataAccessFlag != FULLY_OBSERVABLE && this.opponentTeam.contains(pman)) {
  //
  //     int index = this.opponentTeam.indexOf(pman);
  //     return this.opponentTeamAIAccess.get(index).stats.get(0);
  //
  //   }
  //
  //   else {
  //     return -1;
  //   }
  //
  // }

  // Player should be the player to move
  public int breakTies(TreeNode node, int player, ArrayList<Integer> nodeIndexes) {

    // Handling passing or not passing indexes from other nodes to use
    if (nodeIndexes == null) {
      nodeIndexes = node.indexesToPick;
    }

    // Now we have a problem
    // The case where the op has no moves (PO) and we are going second
    if (nodeIndexes == null) {
      // Give root.gameState to backup
      return -1;
    }

    // Ties due to moves being bad or all winning
    // Good to break these to prevent AI opponent always choosing index 0
    // when estimating the opponents move.
    int size = nodeIndexes.size();
    if (size > 1) {

      // Min or maxing player
      float bestSoFar = MIN;
      if (player != this.playerNumber) { bestSoFar = MAX; }
      int toReturn = 0;
      for (int i = 0; i < size; i++) {

        int mIndex = nodeIndexes.get(i);
        GameState gsCopy = node.nodeGameState.deepCopy();
        Pokemon pmanMovingCopy = null;
        if (player == 1) {
          pmanMovingCopy = gsCopy.player1CurrentPokemon;
        }
        else {
          pmanMovingCopy = gsCopy.player2CurrentPokemon;
        }
        Move move = pmanMovingCopy.moves.get( mIndex );

        float hitChance = gsCopy.hitChance(gsCopy,pmanMovingCopy,move);
        // Forcing move to hit with no bonus
        gsCopy.applyMove(gsCopy,pmanMovingCopy,move,2,R_MEDIUM_1,false,PRINTING_DISABLED,null,null,null);

        // Would have selected non effective moves
        // float moveScore = move.metaData.power * move.metaData.accuracy;
        float missChance = 1 - hitChance;
        // System.out.println(hitChance);
        float moveScore = (this.scoreGameState(gsCopy) * hitChance + (this.scoreGameState(node.nodeGameState) * missChance) );

        // System.out.println(this.playerNumber + ": " + moveScore);

        if (player == this.playerNumber) {
          if (bestSoFar < moveScore) {
            bestSoFar = moveScore;
            toReturn = mIndex;
          }
        }
        else {
          if (bestSoFar > moveScore) {
            bestSoFar = moveScore;
            toReturn = mIndex;
          }
        }

      }

      // System.out.println(toReturn);

      return toReturn;

    }
    else if (size == 1) {
      return node.indexesToPick.get(0);
    }
    else {
      return -1;
    }
    // Possibly case where its 0?

  }

  // public int backUpMoveChoice(GameState currentGS) {
  //
  //   // expectimax
  //
  // }

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
