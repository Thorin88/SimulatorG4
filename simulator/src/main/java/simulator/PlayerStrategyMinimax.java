import java.util.*;

import static utility.Constants.*;

public class PlayerStrategyMinimax extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  public PlayerStrategyMinimax(int number) {

    super(number);
    super.name = "Minimax";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategyMinimax(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Minimax";

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
    Tree testTree = Tree.buildRoot(currentGS, this.playerNumber);

    minimax(testTree.root,true,MIN,MAX,4);

    ArrayList<Integer> highestScoringActions = this.evaluateMinimaxTree(testTree);

    // gameTree.root.toStringBFS(1);

    // System.out.println(gameTree.root.children.get(0).currentScore);
    // System.out.println(gameTree.root.children.get(4).currentScore);
    // System.out.println(gameTree.root.children.get(8).currentScore);
    // System.out.println(gameTree.root.children.get(12).currentScore);
    if (printMode == PRINTING_ENABLED) {
      // System.out.println(gameTree.root.currentScore);
      // System.out.println(gameTree.root.childrenScores());
      // System.out.println(highestScoringActions.toString());

      // System.out.println("Minimax score: " + minimax(testTree.root,true,MIN,MAX,4) );
      System.out.println("Minimax root score: " + testTree.root.currentScore );
      System.out.println("Minimax best move indexes: " + evaluateMinimaxTree(testTree).toString() );

      for (int i = 0; i < testTree.root.children.size(); i++) {

        System.out.println(testTree.root.children.get(i).currentScore);

      }

    }

    int indexOfAction = highestScoringActions.get(UtilityFunctions.randomNumber(0,highestScoringActions.size() - 1));

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

  //  (score node)
  // Needs to always be from maxing players view, not turn player's view
  public float score(TreeNode node) {

    GameState currentGS = node.nodeGameState;

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

    int size = myTeam.size();

    for (int i = 0; i < size; i++) {

      if (myTeam.get(i).currentHealth <= 0) {

        score -= 1;

      }

      // Add percent of pman remaining health

      else {

        score += myTeam.get(i).currentHealth / (float) myTeam.get(i).stats.get(0).getStatValue();

      }

    }

    size = opTeam.size();

    for (int i = 0; i < size; i++) {

      if (opTeam.get(i).currentHealth <= 0) {

        score += 1;

      }

      // Add percent of pman remaining health

      else {

        score -= opTeam.get(i).currentHealth / (float) opTeam.get(i).stats.get(0).getStatValue();

      }

    }

    return score * node.probability;
    // return score * node.probability;

  }

  // Builds up currentNode, so later deeper searches can use and add to the tree built.
  // Tree reuseable, new scores are used on the leaves and moved up.

  public float minimax(TreeNode node, boolean maxingPlayer, float bestForMax, float bestForMin, int maxDepth) {

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
        best = Math.max(best, val);
        // Used in calls to other children. Needed since bestForMax could be
        // from outside of this node's children.
        bestForMax = Math.max(bestForMax, best);

        if (bestForMin <= bestForMax) { break; }

      }

      node.currentScore = best * node.probability;
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

        best = Math.min(best, val);
        bestForMin = Math.min(bestForMin, best);

        if (bestForMin <= bestForMax) { break; }

      }

      node.currentScore = best * node.probability;
      return node.currentScore;

    }

  }


  public static void buildMinimaxChildren(TreeNode node, int printMode) {

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

      //TODO: Rflag
      copyOfCopyGS.applyMove(copyOfCopyGS,copyOfUser,copyOfUser.moves.get(i),j,R_RANDOM,false,PRINTING_DISABLED,null,null,null);

      // Even, else odd, so ! = odd, else even
      if ( !((node.depth & 1) == 0) && node.depth > 1 ) {

        copyOfCopyGS.endTurn(copyOfCopyGS, PRINTING_DISABLED,null);
        // System.out.println(node.depth);

      }

      TreeNode newNode = new TreeNode(copyOfCopyGS, opponent);
      newNode.moveIndex = i;
      newNode.probability = moveProbabilities[j];

      node.setChild(newNode);

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
