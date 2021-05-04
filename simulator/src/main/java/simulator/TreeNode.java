import java.util.*;

import static utility.Constants.*;

// Trees currently only buildable from the root downwards.
public class TreeNode {

  int depth;
  GameState nodeGameState;

  GameState lowRoll;
  GameState highRoll;

  float probability;
  // move index which got here
  int moveIndex;
  ArrayList<Integer> indexesToPick;
  int player;
  float currentScore;

  ArrayList<TreeNode> children;
  // Might be useful
  TreeNode parent;

  public TreeNode(int player) {

    this.nodeGameState = null;
    this.children = new ArrayList<TreeNode>();
    this.depth = 1;
    this.probability = 0;
    this.moveIndex = 0;
    this.indexesToPick = null;
    this.player = player;
    this.currentScore = 0;

  }

  public TreeNode(GameState gameState, int player) {

    this.nodeGameState = gameState;
    this.children = new ArrayList<TreeNode>();
    this.depth = 1;
    this.probability = 0;
    this.moveIndex = 0;
    this.indexesToPick = null;
    this.player = player;
    this.currentScore = 0;

  }

  public TreeNode(GameState gameStateLow, GameState gameStateHigh, int player) {

    this.lowRoll = gameStateLow;
    this.highRoll = gameStateHigh;
    this.children = new ArrayList<TreeNode>();
    this.depth = 1;
    this.probability = 0;
    this.moveIndex = 0;
    this.indexesToPick = null;
    this.player = player;
    this.currentScore = 0;

  }

  // No dupe checks
  public void setChild(TreeNode childNode) {

    childNode.setParent(this);
    childNode.setDepth();
    this.children.add(childNode);

  }

  // Doesn't check or set the node as a child
  public void setParent(TreeNode parentNode) {

    this.parent = parentNode;

  }

  public void setDepth() {

    this.depth = this.parent.depth + 1;

  }

  public ArrayList<TreeNode> getChildren() {

    return this.children;

  }

  public String childrenScores() {

    String stringToReturn = "[";

    int size = this.children.size();

    for (int i = 0; i < size; i++) {

      // if (!stringToReturn.contains("[" + this.children.get(i).currentScore + "]")) {
      //
      //   stringToReturn += "[" + this.children.get(i).currentScore + "]";
      //
      // }

      stringToReturn += "[" + this.children.get(i).moveIndex + "," + this.children.get(i).currentScore + "]";

    }

    stringToReturn += "]";

    return stringToReturn;

  }

  public String toStringDFS() {

    String stringToReturn = "";

    // stringToReturn += this.nodeGameState.humanReadableStateInfo() + "\n";
    stringToReturn += this.depth;

    if (this.parent == null || this.parent.children.size() <= 1 || this.parent.children.get(this.parent.children.size() - 1).equals(this)) {

      stringToReturn += "\n";

    }

    int size = this.children.size();

    for (int i = 0; i < size; i++) {

      stringToReturn += this.children.get(i).toStringDFS();

    }

    return stringToReturn;

  }

  public void toStringBFS() {

    // https://stackoverflow.com/questions/5262308/how-do-implement-a-breadth-first-traversal

    Queue<TreeNode> queue = new LinkedList<TreeNode>();

    queue.clear();
    queue.add(this);

    while (!queue.isEmpty()) {

      TreeNode node = queue.remove();

      System.out.print(node.depth + " ");

      int size = node.children.size();

      for (int i = 0; i < size; i++) {

        queue.add(node.children.get(i));

      }

    }

  }

  // Flag = 0: fill out children of all childless nodes
  public void bfsWithFlag(int flag, int printMode) {

    Queue<TreeNode> queue = new LinkedList<TreeNode>();

    queue.clear();
    queue.add(this);

    while (!queue.isEmpty()) {

      TreeNode node = queue.remove();

      int size = node.children.size();

      if (flag == 0) {

        if (size == 0) {

          // if node is of an even depth, children need to have end turn applied

          TreeNode.fillOutChildren(node,printMode);
          if (printMode == PRINTING_ENABLED) {
            System.out.println(node.depth);
          }

        }

      }

      // if (flag == 1) {
      //
      //   if (size == 0) {
      //
      //     node.currentScore = score(node.nodeGameState);
      //
      //   }
      //
      // }

      for (int i = 0; i < size; i++) {

        queue.add(node.children.get(i));

      }

    }

  }

  public void toStringBFS(int flag) {

    // https://stackoverflow.com/questions/5262308/how-do-implement-a-breadth-first-traversal

    Queue<TreeNode> queue = new LinkedList<TreeNode>();

    queue.clear();
    queue.add(this);

    while (!queue.isEmpty()) {

      TreeNode node = queue.remove();

      if (flag == 0) { System.out.print(node.depth + " "); }

      if (flag == 1) { System.out.print(node.currentScore + " "); }

      if (flag == 2) { System.out.print(node.player + " "); }
      if (flag == 3) { System.out.print(node.moveIndex + " "); }

      int size = node.children.size();

      for (int i = 0; i < size; i++) {

        queue.add(node.children.get(i));

      }

    }

    System.out.println("");

  }

  // Builds minimax nodes from moves of the player mentioned in this node,
  // creates max 16 children, 4 per move with a state representing different
  // possible outcomes of a move. Note that "junk" states can occur. Eg
  // a move which would never fail in this state will still work, but this
  // state's probability would be 0, so it would not affect anything. Can clear
  // these nodes out if needed, but included for this first version.

  // printMode = 1: prints stuff
  // Uses R_RANDOM
  public static void fillOutChildren(TreeNode node, int printMode) {

    /*
    For all moves of playerXCurrentPokemon,
      Get probs of move
      Make a GameState copy of one in node
      generate 4 children of move using applyMoveWithFlag
        attach probability that makes flag to that node
    */

    GameState nodeGS = node.nodeGameState;
    Pokemon originalUser = null;

    if (node.player == 1) { originalUser = node.nodeGameState.player1CurrentPokemon; }
    else { originalUser = node.nodeGameState.player2CurrentPokemon; }

    int originalUserMoveSetSize = originalUser.moves.size();

    for (int i = 0; i < originalUserMoveSetSize; i++) {

      if (originalUser.moves.get(i) == null) { break; }

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
        if (moveProbabilities[j] == 0) { continue; }

        GameState copyOfCopyGS = copyGS.deepCopy();
        Pokemon copyOfUser = null;
        int opponent = 1;

        if (node.player == 1) { copyOfUser = copyOfCopyGS.player1CurrentPokemon; opponent = 2; }
        else { copyOfUser = copyOfCopyGS.player2CurrentPokemon; opponent = 1; }

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

        // Could evaluate score here
        // if (newNode.depth == 4) {}

        if (printMode == PRINTING_ENABLED) {
          System.out.println("TREE NODE ADDED");
        }

      }

    }

  }



}
