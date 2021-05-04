
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import static utility.Constants.*;

// JFrame stuff
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.BorderFactory;
import java.awt.Insets;

import javax.swing.SwingUtilities;

import java.awt.Color;

// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// A Jframe object plus whatever extra we add
public class SimulatorV3 extends JFrame {

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

  ActionButtonInner infoCurrent = null;

  ActionButtonInner info1 = null;
  ActionButtonInner info2 = null;
  ActionButtonInner info3 = null;
  ActionButtonInner info4 = null;
  ActionButtonInner info5 = null;
  ActionButtonInner info6 = null;

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

  GameState b4T1GS = null;
  GameState t1GS = null;
  ArrayList<String> t1I = null;
  GameState b4T2GS = null;
  GameState t2GS = null;
  ArrayList<String> t2I = null;
  GameState etGS = null;
  ArrayList<String> etI = null;

  MyKeyListener currentKL = null;

  public SimulatorV3(String labelText, GameState intialGameState, Player computerPlayer) {

    // System.out.println(SwingUtilities.isEventDispatchThread());

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
    // this.helperAgent = new PlayerStrategyMinimaxIII(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxVII(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    // this.helperAgent = new PlayerStrategyExpectimaxX(2, FULLY_OBSERVABLE);
    this.helperAgent = new PlayerStrategyExpectimaxXI(2, FULLY_OBSERVABLE);

  }

  public void startUp(GameState currentGS) {

    this.getContentPane().removeAll();
    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads

    // Would need to set something here to indicate different repaint usable
    String newline = System.getProperty("line.separator");
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS.deepCopy(), "SimulatorV3: Select the obserability of the opponent agent and whether you" + newline + "want an agent to assist you. Press Start to begin the battle.");
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
                updateGameState(currentGS.deepCopy(),null,true,false,false,false);
              }

            }
        });
    startButton.setBounds(switchButtonStartingX,switchButtonStartingY + 3*(moveButtonYSize) + 3*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
    panelToAdd.add(startButton);
    this.startButton = startButton;

    this.getContentPane().add(panelToAdd);
    this.revalidate();

  }

  public void updateGameState(GameState currentGS, String message, Boolean displayMoves, Boolean displaySwitches, Boolean endOfTurn, Boolean fromSummary) {

    System.out.println(SwingUtilities.isEventDispatchThread());

    this.getContentPane().removeAll();

    // Display message will buttons are up
    String displayMsg = "";
    if (displayMoves) {
      displayMsg = "What will " + currentGS.player2CurrentPokemon.name + " do?";
    }
    else if (displaySwitches) {
      displayMsg = "Switch to which Pokemon?";
    }
    else {
      if (message != null) {
        displayMsg = message;
      }
    }

    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS.deepCopy(), displayMsg);
    // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    if (displayMoves) {

      // removing K listener
      if (this.currentKL != null) {
        this.removeKeyListener(this.currentKL);
        this.currentKL = null;
      }

      // Ensuring nothing happens after this function is called when scores
      // need to be calculated. Also relies on updateGameState(gs,msg,true,...)
      // Always having nothing after it.
      // showScores needed since if this is false, we just want to display
      // the moves and revalidate at the end of THIS function, not return
      // like in the showScore case.
      if (prevTurnMove != currentGS.turnCount && showScores) {
        displayMoveButtons(panelToAdd, currentGS);
        return;
      }
      displayMoveButtons(panelToAdd, currentGS);

    }

    else if (displaySwitches) {

      // removing K listener
      if (this.currentKL != null) {
        this.removeKeyListener(this.currentKL);
        this.currentKL = null;
      }

      // A thread will be called to do background work in this call. Again,
      // if showScores is false we want to avoid the return and revalidate
      // at the end of this function.
      if (endOfTurn && showScores && !fromSummary) {
        displaySwitchButtons(panelToAdd, currentGS, endOfTurn, false);
        return;
      }
      // fromSummary to say whether we need to recalc or not
      // (= threadRunning)
      displaySwitchButtons(panelToAdd, currentGS, endOfTurn, fromSummary);

    }

    this.getContentPane().add(panelToAdd);
    this.revalidate();

  }

  public void summary(GameState currentGS, int targetIndex, String message, Boolean switchChoices, Boolean turnEnded) {

    System.out.println(SwingUtilities.isEventDispatchThread());

    this.getContentPane().removeAll();

    // Display message will buttons are up
    String displayMsg = "";
    if (message != null) {
      displayMsg = message;
    }

    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads
    JPanel panelToAdd = new SummaryHandler(this.frameLength, this.frameWidth, currentGS.deepCopy(), targetIndex, displayMsg);
    // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    if (switchChoices) {
      JButton back = makeSwitchButton("Back to battle");
      back.setBounds(frameLength/50,frameWidth/20,moveButtonXSize,moveButtonYSize/2);
      panelToAdd.add(back);
      this.switchToSwitches = new ActionButtonInner();
      this.switchToSwitches.setButton(back);
      this.switchToSwitches.setActionIndex(-1);
      if (turnEnded) {
        this.switchToSwitches.endOfTurn = true;
        this.switchToSwitches.fromSummary = true;
      }
    }
    else {
      JButton back = makeSwitchButton("Back to battle");
      back.setBounds(frameLength/50,frameWidth/20,moveButtonXSize,moveButtonYSize/2);
      panelToAdd.add(back);
      this.switchToMoves = new ActionButtonInner();
      this.switchToMoves.setButton(back);
      this.switchToMoves.setActionIndex(-1);
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
      player1.pokemonSeen(currentGS,PRINTING_DISABLED);
      // Move actions from chooseAction
      // Only in here if we need a new set of scores
      if (showScores) {
        System.out.println("Update move + switch scores");
        updateGameState(currentGS.deepCopy(),"Generating action scores...",false,false,false,false);
        new Thread() { public void run() {
          helperAgent.chooseAction(currentGS, userActions, PRINTING_DISABLED);
          System.out.println(userValidMoveActions.toString());
          System.out.println(userValidSwitchActions.toString());
          // Now we have the scores, call displaySwitches again
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              try {
                setButtonScores(userValidMoveActions,userValidSwitchActions,true);
                getContentPane().removeAll();
                // Display the buttons on the panel ()
                displayMoveButtons(panel,currentGS);
                getContentPane().add(panel);
                revalidate();
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            }
          });
        }}.start();
        // Original call to displayMoves returns and now the thread created
        // will eventually display the moves
        return;
      }

      // Now only called when not showing scores
      this.setButtonScores(userValidMoveActions,userValidSwitchActions,true);

      // this.moveScores = userValidMoveActions;
      // this.switchScores = userValidSwitchActions;
    }

    size = p2Pman.moves.size();

    if (size >= 1) {

      JButton moveB = makeMoveButton(p2Pman.moves.get(0));
      moveB.setBounds(moveButtonStartingX,moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      if (!userValidMoves.contains(0)) {
        moveB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = this.moveScores.get(userValidMoves.indexOf(0));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        moveB.setText("<html><center>" + moveB.getText() + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>");
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
        Action thisAction = this.moveScores.get(userValidMoves.indexOf(1));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        moveB.setText("<html><center>" + moveB.getText() + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>");
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
        Action thisAction = this.moveScores.get(userValidMoves.indexOf(2));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        moveB.setText("<html><center>" + moveB.getText() + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>");
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
        Action thisAction = this.moveScores.get(userValidMoves.indexOf(3));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        moveB.setText("<html><center>" + moveB.getText() + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>");
      }
      panel.add(moveB);
      this.move4 = new ActionButtonInner();
      this.move4.setButton(moveB);
      this.move4.setActionIndex(3);

    }

    JButton moveB = makeSwitchButton("Switch");
    moveB.setBounds(moveButtonStartingX,moveButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
    // Are there best scoring switches - so the user can see this is where
    // the best action, or other best actions, are.
    if (showScores && this.areThereBestSwitches()) {
      moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
    }
    panel.add(moveB);
    this.switchToSwitches = new ActionButtonInner();
    this.switchToSwitches.setButton(moveB);
    this.switchToSwitches.setActionIndex(-1);

    JButton info = makeSwitchButton("Summary");
    int infoXSize = moveButtonXSize/2;
    int infoYSize = moveButtonYSize/2;
    info.setBounds(moveButtonStartingX+moveButtonXSize+buttonGapX/2-infoXSize/2,moveButtonStartingY - infoYSize - buttonGapY,infoXSize,infoYSize);
    panel.add(info);
    this.infoCurrent = new ActionButtonInner();
    this.infoCurrent.setButton(info);
    this.infoCurrent.setActionIndex(-1);
    this.infoCurrent.endOfTurn = false;

    // super.getContentPane().add(panel);
    // super.revalidate();

  }

  public void displaySwitchButtons(JPanel panel, GameState currentGS, Boolean endOfTurn, Boolean threadRunning) {

    System.out.println(SwingUtilities.isEventDispatchThread());

    // super.getContentPane().removeAll();

    Pokemon p2Pman = currentGS.player2CurrentPokemon;
    Team p2Team = currentGS.player2Team;
    ArrayList<Action> userActions = null;

    // Loading actions into the userActions variable. Loaded from global
    // variable for during turn switches, calculated on the spot for end
    // of turn switches.

    // The actions provided may differ, so done explicitly
    if (endOfTurn) {
      userActions = currentGS.generateAllValidSwitchActions(currentGS, 2);
      if (showScores && threadRunning) {
        userActions = this.switchScores;
      }
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

    // End of turn switch actions (if it was regular switches we would already
    // have calculated the scores in the current move screen)
    if (endOfTurn && showScores && !threadRunning) {
      System.out.println("Update switch scores");
      updateGameState(currentGS.deepCopy(),"Generating switch scores...",false,false,false,false);
      new Thread() { public void run() {
        helperAgent.switchOutFainted(currentGS, userValidSwitchActions, PRINTING_DISABLED);
        // Now we have the scores, call displaySwitches again
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            try {
              setButtonScores(null,userValidSwitchActions,true);
              getContentPane().removeAll();
              // Display the buttons on the panel ()
              displaySwitchButtons(panel,currentGS,endOfTurn,true);
              getContentPane().add(panel);
              revalidate();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });
      }}.start();
      return;

      // helperAgent.switchOutFainted(currentGS, userValidSwitchActions, PRINTING_DISABLED);
      //
      // // Adding highlight marker from best end of turn switches
      // this.setButtonScores(null,userValidSwitchActions,false);
      //
      // System.out.println(userValidSwitchActions.toString());
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

    int switchButtonXSize = (int)(moveButtonXSize*0.85);

    size = p2Team.size();

    if (size >= 1) {

      JButton moveB = makeSwitchButton(p2Team.get(0).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY,switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX+switchButtonXSize,switchButtonStartingY,moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(0)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(0));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(0).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(0).name + "<br>(???)</center></html>";
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
      panel.add(infoB);
      this.switch1 = new ActionButtonInner();
      this.switch1.setButton(moveB);
      this.switch1.setActionIndex(0);
      this.switch1.endOfTurn = endOfTurn;
      this.info1 = new ActionButtonInner();
      this.info1.setButton(infoB);
      this.info1.setActionIndex(0);
      this.info1.endOfTurn = endOfTurn;

    }

    if (size >= 2) {

      JButton moveB = makeSwitchButton(p2Team.get(1).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY,switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX) + switchButtonXSize,switchButtonStartingY,moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(1)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      // Now know this list contains this action
      // end of turn used since we would never be wanting
      // to cache the scores generated.
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(1));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(1).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(1).name + "<br>(???)</center></html>";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      panel.add(infoB);
      this.switch2 = new ActionButtonInner();
      this.switch2.setButton(moveB);
      this.switch2.setActionIndex(1);
      this.switch2.endOfTurn = endOfTurn;
      this.info2 = new ActionButtonInner();
      this.info2.setButton(infoB);
      this.info2.setActionIndex(1);
      this.info2.endOfTurn = endOfTurn;

    }

    if (size >= 3) {

      JButton moveB = makeSwitchButton(p2Team.get(2).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + (moveButtonYSize) + (buttonGapY),switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX+switchButtonXSize,switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(2)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(2));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(2).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(2).name + "<br>(???)</center></html>";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      panel.add(infoB);
      this.switch3 = new ActionButtonInner();
      this.switch3.setButton(moveB);
      this.switch3.setActionIndex(2);
      this.switch3.endOfTurn = endOfTurn;
      this.info3 = new ActionButtonInner();
      this.info3.setButton(infoB);
      this.info3.setActionIndex(2);
      this.info3.endOfTurn = endOfTurn;

    }

    if (size >= 4) {

      JButton moveB = makeSwitchButton(p2Team.get(3).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY + (moveButtonYSize) + (buttonGapY),switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX) + switchButtonXSize,switchButtonStartingY + (moveButtonYSize) + (buttonGapY),moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(3)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(3));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(3).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(3).name + "<br>(???)</center></html>";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      panel.add(infoB);
      this.switch4 = new ActionButtonInner();
      this.switch4.setButton(moveB);
      this.switch4.setActionIndex(3);
      this.switch4.endOfTurn = endOfTurn;
      this.info4 = new ActionButtonInner();
      this.info4.setButton(infoB);
      this.info4.setActionIndex(3);
      this.info4.endOfTurn = endOfTurn;

    }

    if (size >= 5) {

      JButton moveB = makeSwitchButton(p2Team.get(4).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX+switchButtonXSize,switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(4)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(4));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(4).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(4).name + "<br>(???)</center></html>";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      panel.add(infoB);
      this.switch5 = new ActionButtonInner();
      this.switch5.setButton(moveB);
      this.switch5.setActionIndex(4);
      this.switch5.endOfTurn = endOfTurn;
      this.info5 = new ActionButtonInner();
      this.info5.setButton(infoB);
      this.info5.setActionIndex(4);
      this.info5.endOfTurn = endOfTurn;

    }

    if (size >= 6) {

      JButton moveB = makeSwitchButton(p2Team.get(5).name);
      JButton infoB = makeSwitchButton("Info");
      moveB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX),switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),switchButtonXSize,moveButtonYSize);
      infoB.setBounds(switchButtonStartingX + (moveButtonXSize) + (buttonGapX) + switchButtonXSize,switchButtonStartingY + 2*(moveButtonYSize) + 2*(buttonGapY),moveButtonXSize-switchButtonXSize,moveButtonYSize);
      infoB.setMargin(new Insets(0, 0, 0, 0));
      if (!userValidSwitches.contains(5)) {
        moveB.setEnabled(false);
        // infoB.setEnabled(false);
      }
      else if (showScores) {
        Action thisAction = userValidSwitchActions.get(userValidSwitches.indexOf(5));
        // Highlighting button if its the highest scoring action
        if (thisAction.isBest) {
          moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
        }
        float scoreToAdd = thisAction.agentScore;
        // Dealing with displaying unreliable scores
        String toAdd;
        if (scoreToAdd > MIN_INVALID) {
          // toAdd = p2Team.get(0).name + " (" + scoreToAdd + ")";
          toAdd = "<html><center>" + p2Team.get(5).name + "<br>(" + String.format("%.4f", scoreToAdd) + ")</center></html>";
        }
        else {
          // toAdd = p2Team.get(0).name + " (???)";
          toAdd = "<html><center>" + p2Team.get(5).name + "<br>(???)</center></html>";
        }
        moveB.setText(toAdd);
      }
      panel.add(moveB);
      panel.add(infoB);
      this.switch6 = new ActionButtonInner();
      this.switch6.setButton(moveB);
      this.switch6.setActionIndex(5);
      this.switch6.endOfTurn = endOfTurn;
      this.info6 = new ActionButtonInner();
      this.info6.setButton(infoB);
      this.info6.setActionIndex(5);
      this.info6.endOfTurn = endOfTurn;

    }

    if (!endOfTurn) {
      JButton moveB = makeSwitchButton("Moves");
      moveB.setBounds(switchButtonStartingX,switchButtonStartingY + 3*(moveButtonYSize) + 3*(buttonGapY),moveButtonXSize*2 + buttonGapX,moveButtonYSize);
      // Are there best scoring moves - so the user can see this is where
      // the best action, or other best actions, are.
      if (showScores && this.areThereBestMoves()) {
        moveB.setBorder(BorderFactory.createLineBorder(new Color(201, 70, 54), 4));
      }
      panel.add(moveB);
      this.switchToMoves = new ActionButtonInner();
      this.switchToMoves.setButton(moveB);
      this.switchToMoves.setActionIndex(-1);
    }

    // JButton info = makeSwitchButton("Summary");
    // int infoXSize = moveButtonXSize/2;
    // int infoYSize = moveButtonYSize/2;
    // info.setBounds(moveButtonStartingX+moveButtonXSize+buttonGapX/2-infoXSize/2,moveButtonStartingY - infoYSize - buttonGapY,infoXSize,infoYSize);
    // panel.add(info);
    // this.infoCurrent = new ActionButtonInner();
    // this.infoCurrent.setButton(info);
    // this.infoCurrent.setActionIndex(-1);
    // this.infoCurrent.endOfTurn = false;

  }

  public JButton makeMoveButton(Move move) {

    String name = "";

    if (move == null) {
      name = "Empty Slot";
    }
    else {
      name = move.moveName;
    }

    name = UtilityFunctions.toCaps(name);

    ActionButtonInner listener = new ActionButtonInner();
    JButton button = new JButton(name);
    // button.setForeground(new Color(231,226,0));
    // button.setBackground(new Color(231,226,255));
    button.addActionListener(listener);

    return button;

  }

  // public String moveButtonText(Move move) {
  //
  //   String name = "";
  //
  //   if (move == null) {
  //     name = "Empty Slot";
  //   }
  //   else {
  //     name = move.moveName;
  //   }
  //
  //   name = UtilityFunctions.toCaps(name);
  //   String type = UtilityFunctions.toCaps(move.metaData.typeName);
  //   int power = move.metaData.power;
  //   int accuracy = move.metaData.accuracy;
  //
  //   String toAdd = "";
  //
  //   toAdd = "<html><center><br>"+ name +"</center>"
  //   + "<p style=\"text-align:left;\">"
  //   + "This text is left aligned"
  //   + "<span style=\"float:right;\">"
  //   + "   This text is right aligned"
  //   + "</span>"
  //   + "</p></html>";
  //
  //   return toAdd;
  //
  // }
  //
  // public String moveButtonText(Move move, float score) {
  //
  //   return null;
  //
  // }

  public JButton makeSwitchButton(String name) {

    ActionButtonInner listener = new ActionButtonInner();
    JButton button = new JButton(name);
    button.addActionListener(listener);

    return button;

  }

  // Player objects passed in to help with ease testing using this function.
  // maxTurns is also helpful to control how long a game lasts during a test.
  public GameState runGameP2Choosen(GameState gameState, Action player2ChosenAction, int printMode) {

    // System.out.println(SwingUtilities.isEventDispatchThread());

    Boolean battleEnded = false;

    Player player1 = this.player1;

    Action player1Action = null;
    Action player2Action = player2ChosenAction;

    Action firstToExecute = null;
    Action secondToExecute = null;

    Pokemon player1Pman = null;
    Pokemon player2Pman = null;

    ArrayList<Action> playerActions = null;

      // // Frame updates
      // if (frame != null) {
      // this.updateGameState(gameState.deepCopy(), false, false, false);
      // waitXSeconds(2);
      //   // frame.displayMoveButtons(gameState);
      // }

      ArrayList<Integer> statEstimates = new ArrayList<Integer>();
      ArrayList<Boolean> wasItACrit = new ArrayList<Boolean>();

      ArrayList<String> firstActionInfo = new ArrayList<String>();
      ArrayList<String> secondActionInfo = new ArrayList<String>();

      Boolean addedFaintedMsgP1 = false;
      Boolean addedFaintedMsgP2 = false;

      player1Action = player1.chooseAction(gameState, gameState.generateAllValidActions(gameState, 1), printMode);
      player2Action = player2ChosenAction;

      // For PO helper data collection
      helperAgent.pokemonSeen(currentGS,PRINTING_DISABLED);
      player1.pokemonSeen(currentGS,PRINTING_DISABLED);

      // TODO: Priority based on move used to find which action to execute first.

      playerActions = gameState.whichActionToApplyFirst(gameState, player1Action, player2Action);

      // TODO Might break things later, need to account for this
      // if (playerActions == null) { break; }

      firstToExecute = playerActions.get(0);
      secondToExecute = playerActions.get(1);

      player1Pman = gameState.player1CurrentPokemon;
      player2Pman = gameState.player2CurrentPokemon;

      GameState original = gameState.deepCopy();

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

      // updateGameState(gameState.deepCopy(), false, false, false);
      // waitXSeconds(2);

      GameState afterT1 = gameState.deepCopy();
      // Fainted message
      if (firstToExecute.playerMakingAction == 1) {
        if (original.player1CurrentPokemon.currentHealth > 0 && afterT1.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          firstActionInfo.add(afterT1.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
        if (original.player2CurrentPokemon.currentHealth > 0 && afterT1.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          firstActionInfo.add(afterT1.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
      }
      else {
        if (original.player2CurrentPokemon.currentHealth > 0 && afterT1.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          firstActionInfo.add(afterT1.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
        if (original.player1CurrentPokemon.currentHealth > 0 && afterT1.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          firstActionInfo.add(afterT1.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
      }

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

      GameState afterT2 = gameState.deepCopy();

      // Fainted message
      if (firstToExecute.playerMakingAction == 1) {
        if (afterT1.player1CurrentPokemon.currentHealth > 0 && afterT2.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          secondActionInfo.add(afterT2.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
        if (afterT1.player2CurrentPokemon.currentHealth > 0 && afterT2.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          secondActionInfo.add(afterT2.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
      }
      else {
        if (afterT1.player2CurrentPokemon.currentHealth > 0 && afterT2.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          secondActionInfo.add(afterT2.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
        if (afterT1.player1CurrentPokemon.currentHealth > 0 && afterT2.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          secondActionInfo.add(afterT2.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
      }

      prevTurnSwitch = gameState.turnCount;
      prevTurnMove = gameState.turnCount;

      ArrayList<String> endTInfo = new ArrayList<String>();

      gameState.endTurn(gameState, printMode, endTInfo);

      GameState endT = gameState.deepCopy();

      // Fainted message
      if (firstToExecute.playerMakingAction == 1) {
        if (afterT2.player1CurrentPokemon.currentHealth > 0 && endT.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          endTInfo.add(endT.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
        if (afterT2.player2CurrentPokemon.currentHealth > 0 && endT.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          endTInfo.add(endT.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
      }
      else {
        if (afterT2.player2CurrentPokemon.currentHealth > 0 && endT.player2CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP2) {
          endTInfo.add(endT.player2CurrentPokemon.name + " fainted.");
          addedFaintedMsgP2 = true;
        }
        if (afterT2.player1CurrentPokemon.currentHealth > 0 && endT.player1CurrentPokemon.currentHealth <= 0 && !addedFaintedMsgP1) {
          endTInfo.add(endT.player1CurrentPokemon.name + " fainted.");
          addedFaintedMsgP1 = true;
        }
      }

      if (printMode == PRINTING_ENABLED) {
        System.out.println(gameState.humanReadableStateInfo());
        System.out.println("A1: " + firstActionInfo.toString());
        System.out.println("A2: " + secondActionInfo.toString());
        System.out.println("ET: " + endTInfo.toString());
      }

      // if (firstToExecute.playerMakingAction == 1) {
      //
      // }

      // Thread will be executing up to this point. We then schedule the animations
      // ready to be shown on the UI.
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          try {
            animateTurn(original,afterT1,firstActionInfo,afterT2,secondActionInfo,endT,endTInfo);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      });

      // Alternatively just:
      // animateTurn(original,afterT1,firstActionInfo,afterT2,secondActionInfo,endT,endTInfo);

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
        // updateGameState(gameState.deepCopy(), false, false, false);
        // waitXSeconds(2);
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

  public void animateTurn(GameState start, GameState afterTurn1, ArrayList<String> t1Info, GameState afterTurn2, ArrayList<String> t2Info, GameState endTurn, ArrayList<String> eTInfo) {

    System.out.println("animateTurn: " + SwingUtilities.isEventDispatchThread());

    // GameState t1GS = null;
    // ArrayList<String> t1Info = null;
    // GameState t2GS = null;
    // ArrayList<String> t2Info = null;
    // GameState etGS = null;
    // ArrayList<String> etInfo = null;

    this.b4T1GS = start;
    this.t1GS = afterTurn1;
    this.t1I = t1Info;
    this.b4T2GS = afterTurn1;
    this.t2GS = afterTurn2;
    this.t2I = t2Info;
    this.etGS = endTurn;
    this.etI = eTInfo;

    // NOTE: Might run into sync issues doing this

    animate();

    // String firstMessage = "";
    //
    // if (t1I != null && t1I.size() > 0) {
    //   firstMessage = t1I.remove(0);
    // }
    //
    // this.getContentPane().removeAll();
    // // Pass params to the panel here
    // // Eg to tell paintComponent to draw a healthbar
    // // deepCopy Added for extra safety around threads
    // JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, start.deepCopy(), firstMessage);
    // // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);
    //
    // this.addKeyListener(new MyKeyListener());
    //
    // this.setFocusable(true);
    // this.requestFocus();
    //
    // // Listener:
    // // slowly empties string list, makes new panel
    //
    // this.getContentPane().add(panelToAdd);
    // this.revalidate();

  }

  public void animate() {

    System.out.println("animate: " + SwingUtilities.isEventDispatchThread());

    GameState targetGS = null;
    String toDisplay = "";

    // if (t1I == null || t1I.size() == 0) { t1GS = null; }
    // if (t2I == null || t2I.size() == 0) { t2GS = null; }
    // if (etI == null || etI.size() == 0) { etGS = null; }

    // System.out.println(this.t2GS == null);

    if (t1I != null && t1I.size() > 0) {
      if (b4T1GS != null) { targetGS = b4T1GS.deepCopy(); b4T1GS = null; }
      else { targetGS = t1GS; }
      toDisplay = t1I.remove(0);
    }
    else if (t2I != null && t2I.size() > 0) {
      if (b4T2GS != null) { targetGS = b4T2GS.deepCopy(); b4T2GS = null; }
      else { targetGS = t2GS; }
      toDisplay = t2I.remove(0);
    }
    else if (etI != null && etI.size() > 0) {
      targetGS = etGS;
      toDisplay = etI.remove(0);
    }
    else {
      // Loop back round to switch or move buttons
      System.out.println("S/M Button time");
      // currentGS = etGS;
      // switchOut(etGS);

      switchOut(currentGS);
      return;
    }

    // System.out.println(targetGS.humanReadableStateInfo());

    System.out.println(toDisplay);

    this.getContentPane().removeAll();
    // Pass params to the panel here
    // Eg to tell paintComponent to draw a healthbar
    // deepCopy Added for extra safety around threads
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, targetGS.deepCopy(), toDisplay);
    // DisplayHandler panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    this.addKeyListener(new MyKeyListener());

    this.setFocusable(true);
    this.requestFocus();

    // Listener:
    // slowly empties string list, makes new panel

    this.getContentPane().add(panelToAdd);
    this.revalidate();

  }

  // Normal move and switch scores: setToGlobal set to true. Max found among
  // move and switch scores, then the actions with this score have their
  // isBest attribute set to true.
  // End of turn switches: Since these are not stored, as the screen can only
  // appear once, setToGlobal should be set to false. moves will be null or empty
  // so the maximum score will be calcuated from switches only.
  public void setButtonScores(ArrayList<Action> moves, ArrayList<Action> switches, Boolean setToGlobal) {

    if (setToGlobal) {
      this.moveScores = moves;
      this.switchScores = switches;
    }

    int size = 0;
    float currentBest = MIN_INVALID;

    if (moves != null) {
      size = moves.size();
      for (int i = 0; i < size; i++) {
        Action tempAction = moves.get(i);

        if (currentBest < tempAction.agentScore) {
          currentBest = tempAction.agentScore;
        }
      }
    }
    if (switches != null) {
      size = switches.size();
      for (int i = 0; i < size; i++) {
        Action tempAction = switches.get(i);

        if (currentBest < tempAction.agentScore) {
          currentBest = tempAction.agentScore;
        }
      }
    }

    if (moves != null) {
      size = moves.size();
      for (int i = 0; i < size; i++) {
        Action tempAction = moves.get(i);

        if (currentBest == tempAction.agentScore) {
          tempAction.isBest = true;
        }
      }
    }
    if (switches != null) {
      size = switches.size();
      for (int i = 0; i < size; i++) {
        Action tempAction = switches.get(i);

        if (currentBest == tempAction.agentScore) {
          tempAction.isBest = true;
        }
      }
    }

  }

  public Boolean areThereBestMoves() {

    ArrayList<Action> temp = this.moveScores;

    if (temp == null) { return false; }

    int size = temp.size();

    for (int i = 0; i < size; i++) {
      if (temp.get(i).isBest) { return true; }
    }

    return false;

  }

  public Boolean areThereBestSwitches() {

    ArrayList<Action> temp = this.switchScores;

    if (temp == null) { return false; }

    int size = temp.size();

    for (int i = 0; i < size; i++) {
      if (temp.get(i).isBest) { return true; }
    }

    return false;

  }

  // This is where scores would be added to buttons
  public void switchOut(GameState currentGS) {

    // updateGameState(currentGS, false);

    Pokemon player2Pman = currentGS.player2CurrentPokemon;
    Boolean battleEnded = false;

    GameState start = currentGS.deepCopy();

    if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }

    if (battleEnded) {
      System.out.println("Battle finished.");
      // No buttons
      String message = "Battle finished.";
      if (currentGS.player1Team.isOutOfUseablePokemon() && currentGS.player2Team.isOutOfUseablePokemon()) {
        message += " It was a draw.";
      }
      else if (currentGS.player1Team.isOutOfUseablePokemon()) {
        message += " You won!";
      }
      else if (currentGS.player2Team.isOutOfUseablePokemon()) {
        message += " " + this.player1.name + " won!";
      }

      updateGameState(currentGS.deepCopy(),message, false, false, false, false);
      return;
    }

    // Will loop back round to a switchOut() call
    if (currentGS.player1CurrentPokemon.currentHealth <= 0) {

      updateGameState(currentGS.deepCopy(),"Opponent is thinking about switches...",false,false,false,false);
      new Thread() { public void run() {

        Action firstToExecute = player1.switchOutFainted(currentGS, currentGS.generateAllValidSwitchActions(currentGS,1), PRINTING_ENABLED);

        // Shouldn't occur
        // if (firstToExecute == null) {
        //
        //   // System.out.println("Player 1 is out of useable Pokemon");
        //   battleEnded = true;
        //
        // }

        ArrayList<String> firstInfo = new ArrayList<String>();
        currentGS.switchOut(currentGS, 1, firstToExecute.indexOfAction, PRINTING_ENABLED,firstInfo);
        // Animate switch - if user has fainted also, will end up looping back
        // round to do that too.
        GameState firstSwitch = currentGS.deepCopy();
        // Scheduling animateTurn since this is a background worker thread.
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            try {
              animateTurn(start,firstSwitch,firstInfo,null,null,null,null);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });

      }}.start();

      // Now just waiting on thread to finish and schedule its update
      return;

    }
    //
    if (player2Pman.currentHealth <= 0) {

      // firstToExecute = player2.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,2), printMode);

      // switchOutUserFainted(gameState);

      updateGameState(currentGS.deepCopy(), null, false, true, true, false);

      // if (firstToExecute == null) { break; }
      //
      // gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode);

    }
    // The case where the user has needed to switched out (opp may or may not have
    // fainted).
    else {
      updateGameState(currentGS.deepCopy(), null, true, false, false, false);
    }

    // GameState secondSwitch = currentGS.deepCopy();
    //
    // animateTurn(start,firstSwitch,firstInfo,secondSwitch,null,null,null);

    // runGameP2Choosen(this.currentGS,);
    // System.out.println("Did that");

    // if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }
    //
    // if (battleEnded) {
    //   System.out.println("Battle finished.");
    //   // No buttons
    //   updateGameState(currentGS.deepCopy(), false, false, false);
    // }

    // System.out.println(currentGS.humanReadableStateInfo());

  }

  // public void animateSwitchOut(GameState currentGS) {
  //
  //   // updateGameState(currentGS, false);
  //
  //   Pokemon player2Pman = currentGS.player2CurrentPokemon;
  //   Boolean battleEnded = false;
  //
  //   if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }
  //
  //   if (battleEnded) {
  //     System.out.println("Battle finished.");
  //     // No buttons
  //     updateGameState(currentGS.deepCopy(), false, false, false);
  //     return;
  //   }
  //
  //   if (currentGS.player1CurrentPokemon.currentHealth <= 0) {
  //
  //     Action firstToExecute = player1.switchOutFainted(currentGS, currentGS.generateAllValidSwitchActions(currentGS,1), PRINTING_ENABLED);
  //
  //     // Replace the break later
  //     if (firstToExecute == null) {
  //
  //       // System.out.println("Player 1 is out of useable Pokemon");
  //       battleEnded = true;
  //
  //     }
  //     else {
  //       currentGS.switchOut(currentGS, 1, firstToExecute.indexOfAction, PRINTING_ENABLED,null);
  //       // Could add delay here
  //     }
  //
  //   }
  //   //
  //   if (player2Pman.currentHealth <= 0) {
  //
  //     // firstToExecute = player2.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,2), printMode);
  //
  //     // switchOutUserFainted(gameState);
  //
  //     updateGameState(currentGS.deepCopy(), false, true, true);
  //
  //     // if (firstToExecute == null) { break; }
  //     //
  //     // gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode);
  //
  //   }
  //   else {
  //     updateGameState(currentGS.deepCopy(), true, false, false);
  //   }
  //
  //   // runGameP2Choosen(this.currentGS,);
  //   // System.out.println("Did that");
  //
  //   // if (currentGS.player1Team.isOutOfUseablePokemon() || currentGS.player2Team.isOutOfUseablePokemon()) { battleEnded = true; }
  //   //
  //   // if (battleEnded) {
  //   //   System.out.println("Battle finished.");
  //   //   // No buttons
  //   //   updateGameState(currentGS.deepCopy(), false, false, false);
  //   // }
  //
  //   // System.out.println(currentGS.humanReadableStateInfo());
  //
  // }

  private class ActionButtonInner implements ActionListener {

    JButton buttonObj;
    int actionIndex;

    Boolean endOfTurn = false;
    Boolean fromSummary = true;

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

      Action action = null;

        if (move1 != null && e.getSource() == move1.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("move",move1.actionIndex,2);
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }}.start();
        }
        else if (move2 != null && e.getSource() == move2.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("move",move2.actionIndex,2);
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }}.start();
        }
        else if (move3 != null && e.getSource() == move3.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("move",move3.actionIndex,2);
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }}.start();
        }
        else if (move4 != null && e.getSource() == move4.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("move",move4.actionIndex,2);
            runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
          }}.start();
        }

        // Does different stuff depending on whether the switch was at the
        // end of the turn or instead of a move.
        else if (switch1 != null && e.getSource() == switch1.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch1.actionIndex,2);
            if (switch1.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }
        else if (switch2 != null && e.getSource() == switch2.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch2.actionIndex,2);
            if (switch2.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
              // NOTE: In a different place to V2
              // switchOut(currentGS);
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }
        else if (switch3 != null && e.getSource() == switch3.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch3.actionIndex,2);
            if (switch3.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }
        else if (switch4 != null && e.getSource() == switch4.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch4.actionIndex,2);
            if (switch4.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }
        else if (switch5 != null && e.getSource() == switch5.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch5.actionIndex,2);
            if (switch5.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }
        else if (switch6 != null && e.getSource() == switch6.buttonObj) {
          // Remove buttons
          updateGameState(currentGS.deepCopy(),"The opponent is thinking...",false,false,false,false);
          new Thread() { public void run() {
            Action action = new Action("switch",switch6.actionIndex,2);
            if (switch6.endOfTurn) {
              // Switching out from user input while also collecting the needed
              // info to display.
              ArrayList<String> info = new ArrayList<String>();
              GameState beforeSwitch = currentGS.deepCopy();
              currentGS.switchOut(currentGS,2,action.indexOfAction,PRINTING_ENABLED,info);
              GameState afterSwitch = currentGS.deepCopy();
              // Scheduling animateTurn since this is a background worker thread.
              SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                  try {
                    animateTurn(beforeSwitch,afterSwitch,info,null,null,null,null);
                  } catch (Exception ex) {
                    ex.printStackTrace();
                  }
                }
              });
            }
            else {
              runGameP2Choosen(currentGS,action,PRINTING_ENABLED);
            }
          }}.start();
        }

        else if (switchToSwitches != null && e.getSource() == switchToSwitches.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          updateGameState(currentGS.deepCopy(),null,false,true,switchToSwitches.endOfTurn,switchToSwitches.fromSummary);
          return;
        }

        else if (switchToMoves != null && e.getSource() == switchToMoves.buttonObj) {
          // Regardless of when a move is clicked, it will be doing the same thing,
          // so no extra stuff for this.
          updateGameState(currentGS.deepCopy(),null,true,false,false,false);
          return;
        }

        else if (infoCurrent != null && e.getSource() == infoCurrent.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),-1,"Testing...",false,false);
          return;
        }

        else if (info1 != null && e.getSource() == info1.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info1.actionIndex,"Testing...",true,info1.endOfTurn);
          return;
        }

        else if (info2 != null && e.getSource() == info2.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info2.actionIndex,"Testing...",true,info2.endOfTurn);
          return;
        }

        else if (info3 != null && e.getSource() == info3.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info3.actionIndex,"Testing...",true,info3.endOfTurn);
          return;
        }

        else if (info4 != null && e.getSource() == info4.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info4.actionIndex,"Testing...",true,info4.endOfTurn);
          return;
        }

        else if (info5 != null && e.getSource() == info5.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info5.actionIndex,"Testing...",true,info5.endOfTurn);
          return;
        }

        else if (info6 != null && e.getSource() == info6.buttonObj) {
          // Display switches with endTurn set to false. Listener can now apply
          // the switch actions correctly.
          summary(currentGS.deepCopy(),info6.actionIndex,"Testing...",true,info6.endOfTurn);
          return;
        }





        // Can add more switch buttons here

        // runGameP2Choosen(currentGS,action,PRINTING_ENABLED);

        // Checked each turn, will do nothing if nothing fainted.
        // switchOut(currentGS);

      // }}.start();

    }

    // @Override
    // public String toString() {
    //
    //   return "[" + this.formName + "," + this.formID +"]";
    //
    // }

  }

  private class MyKeyListener implements KeyListener {

    JButton buttonObj;
    int actionIndex;

    Boolean endOfTurn = false;

    public MyKeyListener() {

      // Making sure there is only 1 key listener
      if (currentKL != null) {
        removeKeyListener(currentKL);
        currentKL = this;
      }
      else {
        currentKL = this;
      }

      this.buttonObj = null;
      this.actionIndex = -1;

    }

    public void keyPressed(KeyEvent e) {
        // System.out.println("keyPressed");
    }

    // NOTE: Threading could break this?
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode()== KeyEvent.VK_RIGHT) {

          System.out.println("KeyListener: " + SwingUtilities.isEventDispatchThread());

          // new Thread() { public void run() {

            if (currentKL != null) {
              removeKeyListener(currentKL);
              currentKL = null;
            }

            // draw.moveRight();
            // System.out.println("Test");
            animate();

          // }}.start();

        }

        // System.out.println("Here");

    }
    public void keyTyped(KeyEvent e) {
        // System.out.println("keyTyped");
    }

    // @Override
    // public String toString() {
    //
    //   return "[" + this.formName + "," + this.formID +"]";
    //
    // }

  }

  public static void main(String[] args) {

    System.out.println("SimulatorV3 starting (gradle)...");

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

    // *** Regular

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

    gameState.player1Team.add(pman);
    gameState.player1Team.add(pman2);
    gameState.player1Team.add(pman3);
    gameState.player1Team.add(pman4);
    gameState.player1Team.add(pman5);
    gameState.player1Team.add(pman6);

    // P1 Ubers
    // gameState.player1Team.add(pman7.deepCopy());
    // gameState.player1Team.add(pman8.deepCopy());
    // gameState.player1Team.add(pman9.deepCopy());
    // gameState.player1Team.add(pman10.deepCopy());
    // gameState.player1Team.add(pman11.deepCopy());
    // gameState.player1Team.add(pman12.deepCopy());

    gameState.player2Team.add(pman7);
    gameState.player2Team.add(pman8);
    gameState.player2Team.add(pman9);
    gameState.player2Team.add(pman10);
    gameState.player2Team.add(pman11);
    gameState.player2Team.add(pman12);

    // Team testTeam = gameState.player1Team.deepCopy();

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // gameState.player1CurrentPokemon = gameState.player1Team.get(5);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(5);

    // *** Regular ^

    // C Vs C

    // String[] moves1 = {"scratch","growl","",""};
    // String[] moves2 = {"tackle","tail-whip","",""};
    //
    // // Charmander
    // Pokemon pman = new Pokemon();
    // // Squirtle
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(4,0,moves1,0,PRINTING_DISABLED);
    // pman2.buildUsingMoveNames(7,0,moves2,0,PRINTING_DISABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // pman.levelUpPokemon(5,perfectIVs,noEVs);
    // pman2.levelUpPokemon(5,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player1Team.add(pman);
    // gameState.player2Team.add(pman2);
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // C Vs C^

    // LN

    // String[] moves1 = {"scratch","growl","",""};
    // String[] moves2 = {"tackle","tail-whip","",""};
    //
    // // Charmander
    // Pokemon pman = new Pokemon();
    // // Squirtle
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(4,0,moves1,0,PRINTING_DISABLED);
    // pman2.buildUsingMoveNames(7,0,moves2,0,PRINTING_DISABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // pman.levelUpPokemon(5,perfectIVs,noEVs);
    // pman2.levelUpPokemon(5,perfectIVs,noEVs);
    //
    // ArrayList<Stat> pmanStats = pman.stats;
    // pmanStats.get(0).base_stat = 18;
    // pman.currentHealth = 18;
    // pmanStats.get(1).base_stat = 10;
    // pmanStats.get(2).base_stat = 9;
    // pmanStats.get(3).base_stat = 10;
    // pmanStats.get(4).base_stat = 10;
    // pmanStats.get(5).base_stat = 11;
    //
    // pmanStats = pman2.stats;
    // pmanStats.get(0).base_stat = 19;
    // pman2.currentHealth = 19;
    // pmanStats.get(1).base_stat = 9;
    // pmanStats.get(2).base_stat = 11;
    // pmanStats.get(3).base_stat = 10;
    // pmanStats.get(4).base_stat = 10;
    // pmanStats.get(5).base_stat = 9;
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman);
    // gameState.player1Team.add(pman2);
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);
    //
    // // Has been buffed since gen 1
    // gameState.player2CurrentPokemon.moves.get(0).metaData.power = 35;
    // gameState.player2CurrentPokemon.moves.get(0).metaData.accuracy = 95;

    // LN^

    // *** P1

    // String[] moves1 = {"fire-blast","lava-plume","earth-power","toxic"};
    //
    // String[] moves2 = {"dragon-dance","superpower","fire-punch","earthquake"};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(485,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(149,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Heat
    // pman.levelUpPokemon(100,perfectIVs,noEVs);
    // // Drag
    // pman2.levelUpPokemon(100,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player1Team.add(pman);
    // gameState.player2Team.add(pman2);
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);
    //
    // // P2 (Heat faster)
    // gameState.player2Team.get(0).stats.get(5).setStatValue(500);

    // P1 ^

    // P3

    // String[] moves1 = {"draco-meteor","surf", "thunderbolt","psychic"};
    //
    // String[] moves2 = {"dragon-dance","superpower","fire-punch","earthquake"};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(381,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(149,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Heat
    // pman.levelUpPokemon(100,perfectIVs,noEVs);
    // // Drag
    // pman2.levelUpPokemon(100,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman);
    // gameState.player1Team.add(pman2);
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);
    //
    // String[] moves1 = {"draco-meteor","surf", "thunderbolt","psychic"};
    //
    // String[] moves2 = {"dragon-dance","superpower","fire-punch","earthquake"};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(381,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(149,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Heat
    // pman.levelUpPokemon(100,perfectIVs,noEVs);
    // // Drag
    // pman2.levelUpPokemon(100,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman);
    // gameState.player1Team.add(pman2);
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // P3 ^

    // P4

    // String[] moves1 = {"draco-meteor","surf", "thunderbolt","psychic"};
    //
    // String[] moves2 = {"dragon-dance","superpower","fire-punch","earthquake"};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(381,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(149,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Heat
    // pman.levelUpPokemon(100,perfectIVs,noEVs);
    // // Drag
    // pman2.levelUpPokemon(100,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman);
    // gameState.player1Team.add(pman2);
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // ^P4

    // P5

    // String[] moves1 = {"roar-of-time","thunder-wave","sandstorm","earth-power"};
    // String[] moves2 = {"power-gem","ancient-power","",""};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(483,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(484,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Diagla
    // pman.levelUpPokemon(80,perfectIVs,noEVs);
    // // Palkia
    // pman2.levelUpPokemon(80,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman2);
    //
    // gameState.player1Team.add(pman);
    // gameState.player1Team.add(pman2.deepCopy());
    //
    // pman.currentHealth = 10;
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // ^P5

    // P6

    // String[] moves1 = {"low-sweep","mach-punch", "swords-dance","spore"};
    //
    // String[] moves2 = {"cross-poison","super-fang","brave-bird","u-turn"};
    //
    // Pokemon pman = new Pokemon();
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(286,0,moves1,0,PRINTING_ENABLED);
    // pman2.buildUsingMoveNames(169,0,moves2,0,PRINTING_ENABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // // Heat
    // pman.levelUpPokemon(100,perfectIVs,noEVs);
    // // Drag
    // pman2.levelUpPokemon(100,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player2Team.add(pman);
    // gameState.player1Team.add(pman2);
    //
    // // Team testTeam = gameState.player1Team.deepCopy();
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // P6 ^

    // Magikarp

    // String[] moves1 = {"scratch","growl","",""};
    // String[] moves2 = {"splash","tackle","",""};
    //
    // // Charmander
    // Pokemon pman = new Pokemon();
    // // Magikarp
    // Pokemon pman2 = new Pokemon();
    //
    // pman.buildUsingMoveNames(4,0,moves1,0,PRINTING_DISABLED);
    // pman2.buildUsingMoveNames(129,0,moves2,0,PRINTING_DISABLED);
    //
    // int[] perfectIVs = {31,31,31,31,31,31};
    // int[] noEVs = {0,0,0,0,0,0};
    //
    // pman.levelUpPokemon(5,perfectIVs,noEVs);
    // pman2.levelUpPokemon(5,perfectIVs,noEVs);
    //
    // GameState gameState = new GameState();
    //
    // gameState.player1Team.add(pman);
    // gameState.player2Team.add(pman2);
    //
    // gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    // gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // Magikarp^

    // gameState.player2Team.get(0).stats.get(5).setStatValue(500);

    // gameState.player2Field.entraceHazards.get(2).turnsRemaining = 1;

    // gameState.player2CurrentPokemon = gameState.player2Team.get(3);

    // gameState.player2CurrentPokemon.currentHealth = 5;
    // gameState.player2CurrentPokemon.statusUnaffectedBySwitching.flags.get(1).turnsRemaining = 1;
    // gameState.player1CurrentPokemon.statusUnaffectedBySwitching.flags.get(2).turnsRemaining = 3;
    // gameState.player2Team.get(1).currentHealth = 5;
    // gameState.player2Team.get(4).stats.get(5).setStatValue(500);
    // gameState.player1Team.get(5).stats.get(5).setStatValue(500);
    // gameState.player2Team.get(0).statChangeLevels.get(2).level = -4;

    // GameState testGameState = gameState.deepCopy();

    // pman.storeFormImages(0);

    // PlayerStrategyExpectimaxVI player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVI player1 = new PlayerStrategyExpectimaxVI(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVII player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVII player1 = new PlayerStrategyExpectimaxVII(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVIII player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVIII player1 = new PlayerStrategyExpectimaxVIII(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxIX player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxIX player1 = new PlayerStrategyExpectimaxIX(1, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxX player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxX player1 = new PlayerStrategyExpectimaxX(1, PARTIALLY_OBSERVABLE);
    PlayerStrategyExpectimaxXI player1 = new PlayerStrategyExpectimaxXI(1, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxXI player1 = new PlayerStrategyExpectimaxXI(1, PARTIALLY_OBSERVABLE);





    // SimulatorV3 test = new SimulatorV3("SimulatorV3", gameState, player1);

    // Starting the UI stuff on the EventDispatchThread
    SwingUtilities.invokeLater(new Runnable() {
      public void run() { SimulatorV3 test = new SimulatorV3("SimulatorV3", gameState, player1); }
    });

  }

  public void waitXSeconds(int x) {

    try { TimeUnit.SECONDS.sleep(x); }
    catch (Exception e) { e.printStackTrace(); }

  }

}
