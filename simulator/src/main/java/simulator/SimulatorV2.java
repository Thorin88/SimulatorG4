
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static utility.Constants.*;

// JFrame stuff
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// A Jframe object plus whatever extra we add
public class SimulatorV2 extends JFrame {

  // X coords
  // static int frameLength = 900;
  static int frameLength = 1300;
  // Y coords
  static int frameWidth = 600;

  int moveButtonXSize = frameLength/6;
  int moveButtonYSize = moveButtonXSize/2;

  int buttonGapX = frameLength/60;
  int buttonGapY = frameWidth/40;

  // Upper oval TL + Olength
  int moveButtonStartingX = 11*(frameLength/40) + frameLength/3 + frameLength/40;
  int moveButtonStartingY = 3*(frameWidth/20);

  int switchButtonStartingX = 11*(frameLength/40) + frameLength/3 + frameLength/40;
  int switchButtonStartingY = 3*(frameWidth/40);

  ActionButtonInner move1 = null;
  ActionButtonInner move2 = null;
  ActionButtonInner move3 = null;
  ActionButtonInner move4 = null;

  ActionButtonInner switchToSwitches = null;

  ActionButtonInner switch1 = null;
  ActionButtonInner switch2 = null;
  ActionButtonInner switch3 = null;
  ActionButtonInner switch4 = null;
  ActionButtonInner switch5 = null;
  ActionButtonInner switch6 = null;

  JToggleButton observability = null;
  JButton startButton = null;

  JToggleButton showScoresButton = null;
  Boolean showScores = true;

  int prevTurnSwitch = -1;
  int prevTurnMove = -1;

  ArrayList<Action> moveScores = null;
  ArrayList<Action> switchScores = null;

  // Would be greyed out or not drawn if pman fainted.
  ActionButtonInner switchToMoves = null;

  GameState currentGS = null;
  Player player1 = null;
  Player helperAgent = null;

  public SimulatorV2(String labelText, GameState intialGameState, Player computerPlayer) {

    // JLabel label = new JLabel(labelText);

    // Was 200,300
    super.setTitle(labelText);
    super.setSize(frameLength, frameWidth);
    // super.setLocationRelativeTo(null);

    // Could just do this add in SV1
    // super.add(new DisplayHandler());
    // Needed to mount panel in frame

    // JPanel starterPanel = new DisplayHandler(this.frameLength, this.frameWidth, intialGameState);
    // displayMoveButtons(starterPanel, intialGameState);
    // super.add(starterPanel);

    startUp(intialGameState);

    // updateGameState(intialGameState,true,false,false);

    super.setLocation(50,100);
    super.setResizable(false);
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.setVisible(true);

    this.currentGS = intialGameState;
    // Just needs to be set before the match starts
    this.player1 = computerPlayer;
    // this.helperAgent = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxVII(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    this.helperAgent = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);

  }

  public void startUp(GameState currentGS) {

    this.getContentPane().removeAll();
    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads

    // Would need to set something here to indicate different repaint usable
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS.deepCopy());
    // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    // Just need to add this as global var and add listener

    // listener should immeiately remove buttons then check what
    // toggle buts are pressed, set the global player object and then
    // display the first moves.

    JToggleButton obvButton = new JToggleButton("Full");
    obvButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

              if (observability != null && e.getSource() == observability) {

                if (observability.isSelected()) {
                  observability.setText("Partial");
                  // Setting data access flag of agent
                  // Add method to set this
                  player1.setDataAccessFlag(PARTIALLY_OBSERVABLE);
                  helperAgent.setDataAccessFlag(PARTIALLY_OBSERVABLE);
                }
                else {
                  observability.setText("Full");
                  player1.setDataAccessFlag(FULLY_OBSERVABLE);
                  helperAgent.setDataAccessFlag(FULLY_OBSERVABLE);
                }

              }

            }
        });
    obvButton.setBounds(switchButtonStartingX,switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
    panelToAdd.add(obvButton);
    this.observability = obvButton;

    this.getContentPane().add(panelToAdd);
    // this.revalidate();

    JToggleButton showScoresButton = new JToggleButton("Scores: Shown");
    showScoresButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {

              if (showScoresButton != null && e.getSource() == showScoresButton) {

                if (showScoresButton.isSelected()) {
                  showScoresButton.setText("Scores: Hidden");
                  showScores = false;
                }
                else {
                  showScoresButton.setText("Scores: Shown");
                  showScores = true;
                }

              }

            }
        });
    showScoresButton.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
    panelToAdd.add(showScoresButton);
    this.showScoresButton = showScoresButton;

    this.getContentPane().add(panelToAdd);

    JButton startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
            // Shouldn't need to thread this
            @Override public void actionPerformed(ActionEvent e) {

              if (startButton != null && e.getSource() == startButton) {
                updateGameState(currentGS,true,false,false);
              }

            }
        });
    startButton.setBounds(switchButtonStartingX,switchButtonStartingY + 3*(moveButtonYSize) + 3*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
    panelToAdd.add(startButton);
    this.startButton = startButton;

    this.getContentPane().add(panelToAdd);
    this.revalidate();

  }

  public void updateGameState(GameState currentGS, Boolean displayMoves, Boolean displaySwitches, Boolean endOfTurn) {

    this.getContentPane().removeAll();
    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS.deepCopy());
    // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    if (displayMoves) {

      displayMoveButtons(panelToAdd, currentGS);

    }

    else if (displaySwitches) {

      displaySwitchButtons(panelToAdd, currentGS, endOfTurn);

    }

    this.getContentPane().add(panelToAdd);
    this.revalidate();

  }

  // How to deal with unusable moves?
  // Creates a fresh panel at the momement, could add to one
  // Could move this and globals into panel class? Avoids creating new
  // panel.
  // Could use images
  public void displayMoveButtons(JPanel panel, GameState currentGS) {

    // TODO
    // Handle null moves
    // Grey out unusable options - button.setEnabled(false);

    // super.getContentPane().removeAll();

    Pokemon p2Pman = currentGS.player2CurrentPokemon;
    ArrayList<Action> userActions = currentGS.generateAllValidActions(currentGS, 2);

    // if (showScores) {
    //   System.out.println(userActions.toString());
    // }

    int size = userActions.size();
    ArrayList<Integer> userValidMoves = new ArrayList<Integer>();
    ArrayList<Action> userValidMoveActions = new ArrayList<Action>();
    ArrayList<Action> userValidSwitchActions = new ArrayList<Action>();
    for (int i = 0; i < size; i++) {
      if (userActions.get(i).isMove()) {
        userValidMoves.add(userActions.get(i).indexOfAction);
        userValidMoveActions.add(userActions.get(i));
      }
      else if (userActions.get(i).isSwitch()) {
        userValidSwitchActions.add(userActions.get(i));
      }
    }

    if (prevTurnMove != currentGS.turnCount ) {
      prevTurnMove = currentGS.turnCount;
      helperAgent.pokemonSeen(currentGS,PRINTING_DISABLED);
      // Move actions from chooseAction
      if (showScores) {
        System.out.println("Update move + switch scores");
        helperAgent.chooseAction(currentGS, userActions, PRINTING_DISABLED);
        System.out.println(userValidMoveActions.toString());
        System.out.println(userValidSwitchActions.toString());
      }
      this.moveScores = userValidMoveActions;
      this.switchScores = userValidSwitchActions;
    }

    size = p2Pman.moves.size();

    if (size >= 1) {

      JButton moveB = makeMoveButton(p2Pman.moves.get(0));
      moveB.setBounds(moveButtonStartingX,moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      if (!userValidMoves.contains(0)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = this.moveScores.get(userValidMoves.indexOf(0)).agentScore;
        moveB.setText(moveB.getText() + " (" + scoreToAdd + ")");
      }
      panel.add(moveB);
      this.move1 = new ActionButtonInner();
      this.move1.setButton(moveB);
      this.move1.setActionIndex(0);

    }

    if (size >= 2) {

      JButton moveB = makeMoveButton(p2Pman.moves.get(1));
      moveB.setBounds(moveButtonStartingX + (moveButtonXSize) + (buttonGapX),moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      if (!userValidMoves.contains(1)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = this.moveScores.get(userValidMoves.indexOf(1)).agentScore;
        moveB.setText(moveB.getText() + " (" + scoreToAdd + ")");
      }
      panel.add(moveB);
      this.move2 = new ActionButtonInner();
      this.move2.setButton(moveB);
      this.move2.setActionIndex(1);

    }

    if (size >= 3) {

      JButton moveB = makeMoveButton(p2Pman.moves.get(2));
      moveB.setBounds(moveButtonStartingX,moveButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidMoves.contains(2)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = this.moveScores.get(userValidMoves.indexOf(2)).agentScore;
        moveB.setText(moveB.getText() + " (" + scoreToAdd + ")");
      }
      panel.add(moveB);
      this.move3 = new ActionButtonInner();
      this.move3.setButton(moveB);
      this.move3.setActionIndex(2);

    }

    if (size >= 4) {

      JButton moveB = makeMoveButton(p2Pman.moves.get(3));
      moveB.setBounds(moveButtonStartingX + (moveButtonXSize) + (buttonGapX),moveButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidMoves.contains(3)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = this.moveScores.get(userValidMoves.indexOf(3)).agentScore;
        moveB.setText(moveB.getText() + " (" + scoreToAdd + ")");
      }
      panel.add(moveB);
      this.move4 = new ActionButtonInner();
      this.move4.setButton(moveB);
      this.move4.setActionIndex(3);

    }

    JButton moveB = makeSwitchButton("Switch");
    moveB.setBounds(moveButtonStartingX,moveButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
    panel.add(moveB);
    this.switchToSwitches = new ActionButtonInner();
    this.switchToSwitches.setButton(moveB);
    this.switchToSwitches.setActionIndex(-1);

    // super.getContentPane().add(panel);
    // super.revalidate();

  }

  public void displaySwitchButtons(JPanel panel, GameState currentGS, Boolean endOfTurn) {

    // super.getContentPane().removeAll();

    Pokemon p2Pman = currentGS.player2CurrentPokemon;
    Team p2Team = currentGS.player2Team;
    ArrayList<Action> userActions = null;

    // The actions provided may differ, so done explicitly
    if (endOfTurn) {
      userActions = currentGS.generateAllValidSwitchActions(currentGS, 2);
    }
    else {
      // MoveButtons would have calculated these already
      // userActions = currentGS.generateAllValidActions(currentGS, 2);
      userActions = this.switchScores;
      System.out.println(userActions);
    }

    int size = userActions.size();
    ArrayList<Integer> userValidSwitches = new ArrayList<Integer>();
    ArrayList<Action> userValidSwitchActions = new ArrayList<Action>();
    // Not necessarily needed but left in for safety
    for (int i = 0; i < size; i++) {
      if (userActions.get(i).isSwitch()) {
        userValidSwitches.add(userActions.get(i).indexOfAction);
        userValidSwitchActions.add(userActions.get(i));
      }
    }

    // End of turn switch actions
    if (endOfTurn && showScores) {
      helperAgent.switchOutFainted(currentGS, userValidSwitchActions, PRINTING_DISABLED);
      System.out.println(userValidSwitchActions.toString());
    }
    // Update scores for switching instead of moves
    // Move scores allows updates first, does both scores

    // else if (showScores && prevTurnSwitch != currentGS.turnCount ) {
    //
    //   // prevTurnSwitch = currentGS.turnCount;
    //   // System.out.println("Update switch scores");
    //   // // To allow the PO to see the new pokemon
    //   // helperAgent.pokemonSeen(currentGS,PRINTING_DISABLED);
    //   // Switch actions from chooseAction
    //
    // }

    // TODO: Score users

    size = p2Team.size();

    if (size >= 1) {

      JButton moveB = makeSwitchButton(p2Team.get(0).name);
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY,moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(0)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(0)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(0).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      // else if (endOfTurn && showScores) {
      //   float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(0)).agentScore;
      //   moveB.setText(p2Team.get(0).name + " (" + scoreToAdd + ")");
      // }
      // else if (showScores) {
      //   float scoreToAdd = this.switchScores.get(userValidMoves.indexOf(0)).agentScore;
      //   moveB.setText(moveB.getText() + " (" + scoreToAdd + ")");
      // }
      panel.add(moveB);
      this.switch1 = new ActionButtonInner();
      this.switch1.setButton(moveB);
      this.switch1.setActionIndex(0);
      this.switch1.endOfTurn = endOfTurn;

    }

    if (size >= 2) {

      JButton moveB = makeSwitchButton(p2Team.get(1).name);
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY,moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(1)) {
        moveB.setEnabled(false);
      }
      // Now know this list contains this action
      // end of turn used since we would never be wanting
      // to cache the scores generated.
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(1)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(1).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(1).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      this.switch2 = new ActionButtonInner();
      this.switch2.setButton(moveB);
      this.switch2.setActionIndex(1);
      this.switch2.endOfTurn = endOfTurn;

    }

    if (size >= 3) {

      JButton moveB = makeSwitchButton(p2Team.get(2).name);
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(2)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(2)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(2).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(2).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      this.switch3 = new ActionButtonInner();
      this.switch3.setButton(moveB);
      this.switch3.setActionIndex(2);
      this.switch3.endOfTurn = endOfTurn;

    }

    if (size >= 4) {

      JButton moveB = makeSwitchButton(p2Team.get(3).name);
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(3)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(3)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(3).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(3).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      this.switch4 = new ActionButtonInner();
      this.switch4.setButton(moveB);
      this.switch4.setActionIndex(3);
      this.switch4.endOfTurn = endOfTurn;

    }

    if (size >= 5) {

      JButton moveB = makeSwitchButton(p2Team.get(4).name);
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(4)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(4)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(4).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(4).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      this.switch5 = new ActionButtonInner();
      this.switch5.setButton(moveB);
      this.switch5.setActionIndex(4);
      this.switch5.endOfTurn = endOfTurn;

    }

    if (size >= 6) {

      JButton moveB = makeSwitchButton(p2Team.get(5).name);
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize,moveButtonYSize);
      if (!userValidSwitches.contains(5)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        float scoreToAdd = userValidSwitchActions.get(userValidSwitches.indexOf(5)).agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          toAdd = p2Team.get(5).name + " (" + scoreToAdd + ")";
        }
        else {
          toAdd = p2Team.get(5).name + " (???)";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      this.switch6 = new ActionButtonInner();
      this.switch6.setButton(moveB);
      this.switch6.setActionIndex(5);
      this.switch6.endOfTurn = endOfTurn;

    }

    if (!endOfTurn) {
      JButton moveB = makeSwitchButton("Moves");
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + 3*(moveButtonYSize) + 3*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
      panel.add(moveB);
      this.switchToMoves = new ActionButtonInner();
      this.switchToMoves.setButton(moveB);
      this.switchToMoves.setActionIndex(-1);
    }

  }

  public JButton makeMoveButton(Move move) {

    String name = "";

    if (move == null) {
      name = "Empty Slot";
    }
    else {
      name = move.moveName;
    }

    ActionButtonInner listener = new ActionButtonInner();
    JButton button = new JButton(name);
    button.addActionListener(listener);

    return button;

  }

  public JButton makeSwitchButton(String name) {

    ActionButtonInner listener = new ActionButtonInner();
    JButton button = new JButton(name);
    button.addActionListener(listener);

    return button;

  }

  // Player objects passed in to help with ease testing using this function.
  // maxTurns is also helpful to control how long a game lasts during a test.
  public GameState runGameP2Choosen(GameState gameState, Action player2ChosenAction, int printMode) {

    Boolean battleEnded = false;

    Player player1 = this.player1;

    Action player1Action = null;
    Action player2Action = player2ChosenAction;

    Action firstToExecute = null;
    Action secondToExecute = null;

    Pokemon player1Pman = null;
    Pokemon player2Pman = null;

    ArrayList<Action> playerActions = null;

    // Should copy gameState in final method
    // Tree testTree = new Tree();
    // TreeNode root = new TreeNode(gameState, 1);
    // testTree.setRoot(root);

    // Tree testTree = Tree.buildTree(gameState, 2);

    // System.out.println(c4.parent.depth);
    // System.out.println(c4.depth);

    // testTree.root.toStringBFS(2);

    // player2.evaluateTree(testTree);

    // System.out.println( player2.evaluateTree(testTree) );

    // testTree.root.toStringBFS(1);

    // System.out.println(testTree.root.children.get(5).nodeGameState.player1CurrentPokemon.currentHealth);
    // System.out.println(testTree.root.children.get(6).nodeGameState.player1CurrentPokemon.currentHealth);

    // System.exit(0);

    // GameState testGameState = gameState.deepCopy();

    // while ( !(gameState.player1Team.isOutOfUseablePokemon() ||  gameState.player2Team.isOutOfUseablePokemon()) ) {

      // // Frame updates
      // if (frame != null) {
      this.updateGameState(gameState.deepCopy(), false, false, false);
      waitXSeconds(2);
      //   // frame.displayMoveButtons(gameState);
      // }

      ArrayList<Integer> statEstimates = new ArrayList<Integer>();
      ArrayList<Boolean> wasItACrit = new ArrayList<Boolean>();

      ArrayList<String> firstActionInfo = new ArrayList<String>();
      ArrayList<String> secondActionInfo = new ArrayList<String>();

      player1Action = player1.chooseAction(gameState, gameState.generateAllValidActions(gameState, 1), printMode);
      player2Action = player2ChosenAction;

      // For PO helper data collection
      helperAgent.pokemonSeen(currentGS,PRINTING_DISABLED);

      // TODO: Priority based on move used to find which action to execute first.

      playerActions = gameState.whichActionToApplyFirst(gameState, player1Action, player2Action);

      // TODO Might break things later, need to account for this
      // if (playerActions == null) { break; }

      firstToExecute = playerActions.get(0);
      secondToExecute = playerActions.get(1);

      player1Pman = gameState.player1CurrentPokemon;
      player2Pman = gameState.player2CurrentPokemon;

      if (firstToExecute.isMove()) {

        Estimates firstEstimates = new Estimates();

        if (firstToExecute.playerMakingAction == 1) {
          gameState.applyMove(gameState, player1Pman, player1Pman.moves.get(firstToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, firstEstimates, wasItACrit, firstActionInfo);
          helperAgent.moveSeen(gameState,player1Pman,player2Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
          player1.moveSeen(gameState,player1Pman,player2Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
        }
        else {
          gameState.applyMove(gameState, player2Pman, player2Pman.moves.get(firstToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, firstEstimates, wasItACrit, firstActionInfo);
          player1.moveSeen(gameState,player2Pman,player1Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
          helperAgent.moveSeen(gameState,player2Pman,player1Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
        }

      }
      else {

        if (firstToExecute.playerMakingAction == 1) {
          gameState.switchOut(gameState, 1, firstToExecute.indexOfAction, printMode, firstActionInfo);
        }
        else {
          gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode, firstActionInfo);
        }

      }

      updateGameState(gameState.deepCopy(), false, false, false);
      waitXSeconds(2);

      if (secondToExecute.isMove()) {

        Estimates secondEstimates = new Estimates();

        if (secondToExecute.playerMakingAction == 1) {
          gameState.applyMove(gameState, player1Pman, player1Pman.moves.get(secondToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, secondEstimates, wasItACrit, secondActionInfo);
          helperAgent.moveSeen(gameState,player1Pman,player2Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
          // The using player can also estimate target's def so needs another call
          player1.moveSeen(gameState,player1Pman,player2Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
        }
        else {
          gameState.applyMove(gameState, player2Pman, player2Pman.moves.get(secondToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, secondEstimates, wasItACrit, secondActionInfo);
          player1.moveSeen(gameState,player2Pman,player1Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
          // The using player can also estimate target's def so needs another call
          helperAgent.moveSeen(gameState,player2Pman,player1Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
        }

      }
      else {

        if (secondToExecute.playerMakingAction == 1) {
          gameState.switchOut(gameState, 1, secondToExecute.indexOfAction, printMode, secondActionInfo);
        }
        else {
          gameState.switchOut(gameState, 2, secondToExecute.indexOfAction, printMode, secondActionInfo);
        }

      }

      prevTurnSwitch = gameState.turnCount;
      prevTurnMove = gameState.turnCount;

      ArrayList<String> endTInfo = new ArrayList<String>();

      gameState.endTurn(gameState, printMode, endTInfo);

      if (printMode == PRINTING_ENABLED) {
        System.out.println(gameState.humanReadableStateInfo());
        System.out.println("A1: " + firstActionInfo.toString());
        System.out.println("A2: " + secondActionInfo.toString());
        System.out.println("ET: " + endTInfo.toString());
      }

      // try { TimeUnit.SECONDS.sleep(2); }
      // catch (Exception e) { e.printStackTrace(); }

      // TODO - Switching

      // if (player1Pman.currentHealth <= 0) {
      //
      //   firstToExecute = player1.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,1), printMode);
      //
      //   // Replace the break later
      //   if (firstToExecute == null) {
      //
      //     // System.out.println("Player 1 is out of useable Pokemon");
      //     battleEnded = true;
      //
      //   }
      //   else {
      //     gameState.switchOut(gameState, 1, firstToExecute.indexOfAction, printMode);
      //   }
      //
      // }
      //
      // if (player2Pman.currentHealth <= 0) {
      //
      //   // firstToExecute = player2.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,2), printMode);
      //
      //   // switchOutUserFainted(gameState);
      //
      //   if (firstToExecute == null) { break; }
      //
      //   gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode);
      //
      // }

      // try { TimeUnit.SECONDS.sleep(2); }
      // catch (Exception e) { e.printStackTrace(); }

      // Frame updates
      // if (frame != null) {
      //
        updateGameState(gameState.deepCopy(), false, false, false);
        waitXSeconds(2);
      //
      // }

    // }

    // if (gameState.player1Team.isOutOfUseablePokemon() || gameState.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }
    //
    // if (battleEnded && printMode == PRINTING_ENABLED) {
    //   System.out.println("Battle finished.");
    // }

    return gameState;

  }

  // This is where scores would be added to buttons
  public void switchOut(GameState currentGS) {

    // updateGameState(currentGS, false);

    Pokemon player2Pman = currentGS.player2CurrentPokemon;
    Boolean battleEnded = false;

    if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }

    if (battleEnded) {
      System.out.println("Battle finished.");
      // No buttons
      updateGameState(currentGS.deepCopy(), false, false, false);
      return;
    }

    if (currentGS.player1CurrentPokemon.currentHealth <= 0) {

      Action firstToExecute = player1.switchOutFainted(currentGS, currentGS.generateAllValidSwitchActions(currentGS,1), PRINTING_ENABLED);

      // Replace the break later
      if (firstToExecute == null) {

        // System.out.println("Player 1 is out of useable Pokemon");
        battleEnded = true;

      }
      else {
        currentGS.switchOut(currentGS, 1, firstToExecute.indexOfAction, PRINTING_ENABLED, null);
        // Could add delay here
      }

    }
    //
    if (player2Pman.currentHealth <= 0) {

      // firstToExecute = player2.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,2), printMode);

      // switchOutUserFainted(gameState);

      updateGameState(currentGS.deepCopy(), false, true, true);

      // if (firstToExecute == null) { break; }
      //
      // gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode);

    }
    else {
      updateGameState(currentGS.deepCopy(), true, false, false);
    }

    // runGameP2Choosen(this.currentGS,);
    // System.out.println("Did that");

    // if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }
    //
    // if (battleEnded) {
    //   System.out.println("Battle finished.");
    //   // No buttons
    //   updateGameState(currentGS.deepCopy(), false, false, false);
    // }

  }

  private class ActionButtonInner implements ActionListener {

    JButton buttonObj;
    int actionIndex;

    Boolean endOfTurn = false;

    public ActionButtonInner() {

      this.buttonObj = null;
      this.actionIndex = -1;

    }

    public JButton getButton() {

      return this.buttonObj;

    }

    public int getActionIndex() {

      return this.actionIndex;

    }

    public void setButton(JButton b) {

      this.buttonObj = b;

    }

    public void setActionIndex(int i) {

      this.actionIndex = i;

    }

    @Override
    // Controls the flow of the game. When moves have been entered the panels
    // displaying the results of each action will not have buttons on them.
    // To allow the game to continue, functions
    // at the end of the calls from here display a final panel with buttons to
    // be detected here.
    public void actionPerformed(ActionEvent e) {

    // Sorta thread safe because the no other button presses are allowed
    // Repaint does not alter components so having the UI un clickable
    // Means that nothing will conflict?

    // Might be races when reading from game states - maybe deepcopy them.

    // If repaint runs on a different thread to the worker thread, then having
    // this copy should prevent race conditions, as repaint only looks at the GS
    // it is given - read-only.

      // Thread for event handling separate from thread for painting
      new Thread() { public void run() {

        Action action = null;

        if (move1 != null && e.getSource() == move1.buttonObj) {
          action = new Action("move",move1.actionIndex,2);
          runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
        }
        else if (move2 != null && e.getSource() == move2.buttonObj) {
          action = new Action("move",move2.actionIndex,2);
          runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
        }
        else if (move3 != null && e.getSource() == move3.buttonObj) {
          action = new Action("move",move3.actionIndex,2);
          runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
        }
        else if (move4 != null && e.getSource() == move4.buttonObj) {
          action = new Action("move",move4.actionIndex,2);
          runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
        }

        // Does different stuff depending on whether the switch was at the
        // end of the turn or instead of a move.
        else if (switch1 != null && e.getSource() == switch1.buttonObj) {
          action = new Action("switch",switch1.actionIndex,2);
          if (switch1.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }
        else if (switch2 != null && e.getSource() == switch2.buttonObj) {
          action = new Action("switch",switch2.actionIndex,2);
          if (switch2.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }
        else if (switch3 != null && e.getSource() == switch3.buttonObj) {
          action = new Action("switch",switch3.actionIndex,2);
          if (switch3.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }
        else if (switch4 != null && e.getSource() == switch4.buttonObj) {
          action = new Action("switch",switch4.actionIndex,2);
          if (switch4.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }
        else if (switch5 != null && e.getSource() == switch5.buttonObj) {
          action = new Action("switch",switch5.actionIndex,2);
          if (switch5.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }
        else if (switch6 != null && e.getSource() == switch6.buttonObj) {
          action = new Action("switch",switch6.actionIndex,2);
          if (switch6.endOfTurn) {
            currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,null);
          }
          else {
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }
        }

        else if (switchToSwitches != null && e.getSource() == switchToSwitches.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          updateGameState(currentGS,false,true,false);
          return;
        }

        else if (switchToMoves != null && e.getSource() == switchToMoves.buttonObj) {
          // Regardless of when a move is clicked, it will be doing the same thing,
          // so no extra stuff for this.
          updateGameState(currentGS,true,false,false);
          return;
        }

        // Can add more switch buttons here

        // runGameP2Choosen(currentGS,action,PRINTING_ENABLED);

        // Checked each turn, will do nothing if nothing fainted.
        switchOut(currentGS);

      }}.start();

    }

    // @Override
    // public String toString() {
    //
    //   return "[" + this.formName + "," + this.formID +"]";
    //
    // }

  }

  public static void main(String[] args) {

    System.out.println("SimulatorV2 starting (gradle)...");

    // JFrame frame = startUpGUI("SimulatorV1");

    // int[] moves1 = {0,1,2,9};
    // int[] moves1 = {0,1,2,75}; // Overheat
    // int[] moves1 = {0,1,2,76};
    // int[] moves1 = {0,1,2,40}; // Giga Drain
    // int[] moves1 = {0,1,2,19}; // Sleep powder
    // int[] moves1 = {0,1,2,58}; // Multi hit
    // int[] moves1 = {0,1,8,40}; // double-edge + Giga Drain
    // int[] moves1 = {0,1,8,28}; // light-screen
    // int[] moves1 = {0,1,17,28}; // flamethrower (c) + light-screen (v)
    // int[] moves1 = {68,1,8,52}; // Will-o-wisp + Sunny Day
    // int[] moves1 = {68,1,17,48}; // Will-o-wisp and safeguard
    // int[] moves1 = {0,1,50,52}; // Synthesis + Sunny Day
    // int[] moves1 = {28,1,50,52}; // Synthesis + Sunny Day
    // int[] moves1 = {28,23,50,52}; // Synthesis + Toxic + Sunny Day
    // int[] moves1 = {28,18,50,52}; // Synthesis + Poison Powder + Sunny Day
    // int[] moves1 = {14,1,50,52}; // Leech seed + Cut
    // int[] moves1 = {14,1,79,52}; // Yawn (on pman 143)
    // int[] moves1 = {14,1,79,52};
    // int[] moves2 = {2,1,0,3};
    // int[] moves3 = {0,8,10,3};
    // int[] moves4 = {0,18,-1,-1};

    String[] moves1 = {"thunder","iron-tail","quick-attack","thunderbolt"};

    String[] moves2 = {"body-slam","brine","blizzard","psychic"};

    String[] moves3 = {"shadow-ball","crunch","blizzard","giga-impact"};

    String[] moves4 = {"frenzy-plant","giga-drain","sludge-bomb","sleep-powder"};
    // String[] moves4 = {"frenzy-plant","giga-drain","sludge-bomb","razor-leaf"};

    String[] moves5 = {"blast-burn","flare-blitz","air-slash","dragon-pulse"};

    String[] moves6 = {"hydro-cannon","blizzard","flash-cannon","focus-blast"};

    String[] moves7 = {"stealth-rock","stone-edge","earthquake","swords-dance"};

    // Seems to make it pick stealth rock as well
    // String[] moves7 = {"earthquake","swords-dance","stone-edge","stealth-rock"};

    // Moves after best move are getting the same score propageted up.
    // String[] moves7 = {"stealth-rock","stone-edge","swords-dance","earthquake"};

    // String[] moves7 = {"swords-dance","earthquake","stone-edge","stealth-rock"};

    String[] moves8 = {"roar-of-time","thunder-wave","sandstorm","earth-power"};

    String[] moves9 = {"toxic","recover","light-screen","aeroblast"};

    // TODO: Allow Origin Pulse?
    String[] moves10 = {"rain-dance","thunder","hydro-pump","water-spout"};

    String[] moves11 = {"spacial-rend","aura-sphere","rain-dance","thunder"};

    String[] moves12 = {"dark-void","dark-pulse","nasty-plot","thunder"};

    // Pikachu
    Pokemon pman = new Pokemon();
    // Lapras
    Pokemon pman2 = new Pokemon();
    // Snorlax
    Pokemon pman3 = new Pokemon();
    // Charizard
    Pokemon pman4 = new Pokemon();
    // Venusaur
    Pokemon pman5 = new Pokemon();
    // Blastiose
    Pokemon pman6 = new Pokemon();

    // My Team

    // Groudon
    Pokemon pman7 = new Pokemon();
    // Diagla
    Pokemon pman8 = new Pokemon();
    // Lugia
    Pokemon pman9 = new Pokemon();
    // Kyogre
    Pokemon pman10 = new Pokemon();
    // Palkia
    Pokemon pman11 = new Pokemon();
    // Darkrai
    Pokemon pman12 = new Pokemon();

    pman.buildUsingMoveNames(25,0,moves1,0,PRINTING_ENABLED);
    pman2.buildUsingMoveNames(131,0,moves2,0,PRINTING_ENABLED);
    pman3.buildUsingMoveNames(143,0,moves3,0,PRINTING_ENABLED);
    pman4.buildUsingMoveNames(3,0,moves4,0,PRINTING_ENABLED);
    pman5.buildUsingMoveNames(6,0,moves5,0,PRINTING_ENABLED);
    pman6.buildUsingMoveNames(9,0,moves6,0,PRINTING_ENABLED);
    pman7.buildUsingMoveNames(383,0,moves7,0,PRINTING_ENABLED);
    pman8.buildUsingMoveNames(483,0,moves8,0,PRINTING_ENABLED);
    pman9.buildUsingMoveNames(249,0,moves9,0,PRINTING_ENABLED);
    pman10.buildUsingMoveNames(382,0,moves10,0,PRINTING_ENABLED);
    pman11.buildUsingMoveNames(484,0,moves11,0,PRINTING_ENABLED);
    pman12.buildUsingMoveNames(491,0,moves12,0,PRINTING_ENABLED);

    // if (true) {return;}

    int[] ivs1 = {24,12,30,16,23,5};
    int[] perfectIVs = {31,31,31,31,31,31};
    int[] ivs3 = {0,0,0,0,0,0};

    int[] pBulk = {252,0,252,6,0,0};
    int[] sBulk = {252,0,6,0,252,0};
    int[] pAttacker = {6,252,0,0,0,252};
    int[] sAttacker= {6,0,0,252,0,252};

    pman.levelUpPokemon(88,perfectIVs,sAttacker);
    pman2.levelUpPokemon(80,perfectIVs,sBulk);
    pman3.levelUpPokemon(82,perfectIVs,pAttacker);
    pman4.levelUpPokemon(84,perfectIVs,sAttacker);
    pman5.levelUpPokemon(84,perfectIVs,sAttacker);
    pman6.levelUpPokemon(84,perfectIVs,sAttacker);
    pman7.levelUpPokemon(80,perfectIVs,pAttacker);
    pman8.levelUpPokemon(80,perfectIVs,sAttacker);
    pman9.levelUpPokemon(80,perfectIVs,sBulk);
    pman10.levelUpPokemon(80,perfectIVs,sAttacker);
    pman11.levelUpPokemon(80,perfectIVs,sAttacker);
    pman12.levelUpPokemon(80,perfectIVs,sAttacker);

    GameState gameState = new GameState();

    // gameState.player1Team.add(pman);
    // gameState.player1Team.add(pman2);
    // gameState.player1Team.add(pman3);
    // gameState.player1Team.add(pman4);
    // gameState.player1Team.add(pman5);
    // gameState.player1Team.add(pman6);

    // P1 Ubers
    gameState.player1Team.add(pman7.deepCopy());
    gameState.player1Team.add(pman8.deepCopy());
    gameState.player1Team.add(pman9.deepCopy());
    gameState.player1Team.add(pman10.deepCopy());
    gameState.player1Team.add(pman11.deepCopy());
    gameState.player1Team.add(pman12.deepCopy());

    gameState.player2Team.add(pman7);
    gameState.player2Team.add(pman8);
    gameState.player2Team.add(pman9);
    gameState.player2Team.add(pman10);
    gameState.player2Team.add(pman11);
    gameState.player2Team.add(pman12);

    // Team testTeam = gameState.player1Team.deepCopy();

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // gameState.player2CurrentPokemon = gameState.player2Team.get(3);

    // gameState.player2CurrentPokemon.currentHealth = 50;
    // gameState.player1Team.get(1).currentHealth = 50;
    // gameState.player2Team.get(4).stats.get(5).setStatValue(500);

    // GameState testGameState = gameState.deepCopy();

    // pman.storeFormImages(0);

    // PlayerStrategyExpectimaxVI player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVI player1 = new PlayerStrategyExpectimaxVI(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVII player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVII player1 = new PlayerStrategyExpectimaxVII(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVIII player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVIII player1 = new PlayerStrategyExpectimaxVIII(1, PARTIALLY_OBSERVABLE);
    PlayerStrategyExpectimaxIX player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxIX player1 = new PlayerStrategyExpectimaxIX(1, PARTIALLY_OBSERVABLE);




    SimulatorV2 test = new SimulatorV2("SimulatorV2", gameState, player1);

  }

  public void waitXSeconds(int x) {

    try { TimeUnit.SECONDS.sleep(x); }
    catch (Exception e) { e.printStackTrace(); }

  }

}
