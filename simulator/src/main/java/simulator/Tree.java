
public class Tree {

  TreeNode root;

  public Tree() {

    this.root = null;

  }

  public void setRoot(TreeNode rootNode) {

    this.root = rootNode;

  }

  // Performance improvements mean that 3 turns ahead can now be used.
  public static Tree buildTree(GameState currentGS, int rootPlayer) {

    GameState copy = currentGS.deepCopy();

    Tree tree = new Tree();
    TreeNode root = new TreeNode(copy, rootPlayer);
    tree.setRoot(root);

    // Tree contains 2 turns
    for (int i = 0; i < 4; i++) {

      root.bfsWithFlag(0,0);

    }

    return tree;

  }

  // public static Tree buildRoot(GameState currentGS, int rootPlayer) {
  //
  //   GameState copy = currentGS.deepCopy();
  //
  //   Tree tree = new Tree();
  //   TreeNode root = new TreeNode(copy, rootPlayer);
  //   root.lowRoll = currentGS.deepCopy();
  //   root.highRoll = currentGS.deepCopy();
  //   root.probability = (float) 1.0;
  //   tree.setRoot(root);
  //
  //   return tree;
  //
  // }

  // Does NOT copy states.
  public static Tree buildRoot(GameState currentGS, int rootPlayer) {

    GameState copy = currentGS.deepCopy();

    Tree tree = new Tree();
    TreeNode root = new TreeNode(currentGS, currentGS, rootPlayer);
    root.nodeGameState = copy;
    // root.lowRoll = currentGS.deepCopy();
    // root.highRoll = currentGS.deepCopy();
    root.probability = (float) 1.0;
    tree.setRoot(root);

    return tree;

  }

}
