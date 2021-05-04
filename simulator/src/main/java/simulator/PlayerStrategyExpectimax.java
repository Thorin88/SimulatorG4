import java.util.*;

import static utility.Constants.*;

public class PlayerStrategyExpectimax extends Player {

  int opponentPlayerNumber;

  Team opponentTeam;
  Team opponentTeamAIAccess;

  int dataAccessFlag;

  public PlayerStrategyExpectimax(int number) {

    super(number);
    super.name = "Expectimax";
    this.dataAccessFlag = 0;

    if (number == 1) { this.opponentPlayerNumber = 2; }
    else { this.opponentPlayerNumber = 1; }

    opponentTeam = new Team();
    opponentTeamAIAccess = new Team();

  }

  public PlayerStrategyExpectimax(int number, int flag) {

    super(number);
    this.dataAccessFlag = flag;
    super.name = "Expectimax";

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

    Tree gameTree = Tree.buildTree(currentGS, this.playerNumber);

    ArrayList<Integer> highestScoringActions = this.evaluateTree(gameTree);

    // gameTree.root.toStringBFS(1);

    // System.out.println(gameTree.root.children.get(0).currentScore);
    // System.out.println(gameTree.root.children.get(4).currentScore);
    // System.out.println(gameTree.root.children.get(8).currentScore);
    // System.out.println(gameTree.root.children.get(12).currentScore);
    if (printMode == PRINTING_ENABLED) {
      System.out.println(gameTree.root.currentScore);
      System.out.println(gameTree.root.childrenScores());
      System.out.println(highestScoringActions.toString());
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

  public void scoreLeaves(TreeNode root) {

    Queue<TreeNode> queue = new LinkedList<TreeNode>();

    queue.clear();
    queue.add(root);

    while (!queue.isEmpty()) {

      TreeNode node = queue.remove();

      int size = node.children.size();

      if (size == 0) {

        node.currentScore = score(node.nodeGameState);

      }

      for (int i = 0; i < size; i++) {

        queue.add(node.children.get(i));

      }

    }

  }

  // Evaluate tree assuming this player is the root node
  public ArrayList<Integer> evaluateTree(Tree tree) {

    this.scoreLeaves(tree.root);

    Queue<TreeNode> queue = new LinkedList<TreeNode>();
    Stack<TreeNode> stack = new Stack<TreeNode>();

    queue.clear();
    stack.clear();
    queue.add(tree.root);

    while (!queue.isEmpty()) {

      TreeNode node = queue.remove();
      stack.push(node);

      int size = node.children.size();

      for (int i = 0; i < size; i++) {

        queue.add(node.children.get(i));

      }

    }

    // Stack contains reverse BFS ordering of nodes in tree.

    while (!stack.isEmpty()) {

      TreeNode node = stack.pop();
      // System.out.println(node.currentScore);
      updateNodeScore(node,tree.root.player);
      // System.out.println(node.currentScore);

    }

    int size = tree.root.children.size();
    ArrayList<Integer> toReturn = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (tree.root.children.get(i).currentScore == tree.root.currentScore && !toReturn.contains(tree.root.children.get(i).moveIndex)) {

        toReturn.add(tree.root.children.get(i).moveIndex);

      }

    }

    return toReturn;

  }

  // Weighted average of the scores of children of the node provided, using the probability
  // of the state in each node occurring. Grouped by move index used. Each node
  // in a group is assigned this new value. Accounts for the fact that the tree
  // may not have the same number of outcomes for each move in future versions.
  //
  // Skew
  public void prepareNodeChildren(TreeNode node, float skew) {

    int size = node.children.size();

    if (size == 0) { return; }

    float newScore = (float) 0.0;
    int currentMoveIndex = -1;
    ArrayList<TreeNode> nodesToChange = new ArrayList<TreeNode>();
    int i = 0;

    // if (node.depth == 2) { System.out.println(size); }

    while (i < size) {

      TreeNode child = node.children.get(i);

      // if (node.depth == 2) { System.out.println(child.currentScore); }

      if (child.moveIndex != currentMoveIndex) {

        // Fill out all the values

        int listSize = nodesToChange.size();
        float totalScore = (float) 0.0;

        for (int j = 0; j < listSize; j++) {

          totalScore += nodesToChange.get(j).currentScore * nodesToChange.get(j).probability;

          // if (node.depth == 2) { System.out.println(nodesToChange.get(j).currentScore); }


        }
        // if (totalScore == 0) {System.out.println("YEETUS0");}

        // if (node.depth == 1 && ) { totalScore += skew; }

        for (int j = 0; j < listSize; j++) {

          nodesToChange.get(j).currentScore = totalScore;

        }

        currentMoveIndex = child.moveIndex;
        nodesToChange = new ArrayList<TreeNode>();

      }

      nodesToChange.add(child);
      i++;

    }

    // Since last arraylist won't be processed in the loop.

    int listSize = nodesToChange.size();
    float totalScore = (float) 0.0;

    for (int j = 0; j < listSize; j++) {

      totalScore += nodesToChange.get(j).currentScore * nodesToChange.get(j).probability;

    }

    // if (totalScore == 0) {System.out.println("YEETUS1");}

    for (int j = 0; j < listSize; j++) {

      nodesToChange.get(j).currentScore = totalScore;

    }

  // if (node.depth == 2) { System.out.println(node.currentScore); /* System.exit(0); */ }

  }

  // Simply gets either the max or min value score of its children, depending on the player,
  // and sets the given node's score as this value. Assumes maximising player
  // is the player associated with the root of the tree.
  public void updateNodeScore(TreeNode node, int rootPlayer) {

    // Could skew score depending on whether the first move killed or not.

    int size = node.children.size();

    if (size == 0) { return; }

    // Skew currently unused
    prepareNodeChildren(node, (float) 0.1);

    float newScore = (float) 0.0;

    for (int i = 0; i < size; i++) {

      TreeNode child = node.children.get(i);

      if (rootPlayer == node.player) {

        float childScore = child.currentScore;

        if (i == 0) { newScore = childScore; }

        if (childScore > newScore) {

          newScore = childScore;

        }

      }

      else {

        float childScore = child.currentScore;

        if (i == 0) { newScore = childScore; }

        if (childScore < newScore) {

          newScore = childScore;

        }

      }

    }

    node.currentScore = newScore;

  }

  // Likely just need this for Expectimax
  // Currently uses non human accessible data.
  public float score(GameState currentGS) {

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

    // return UtilityFunctions.randomNumber(1,2);
    return score;

  }

  public void scoreStateFromMyView(GameState currentGS) {}

  public void scoreStateFromOpponentView(GameState currentGS) {}

  // whatItThinksTheOtherPlayerWillDo

  // whatItWantsToDo

  // some function to combine both these things

}
