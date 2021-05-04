import java.util.*;
import java.util.concurrent.TimeUnit;

// JFrame stuff
// import java.awt.FlowLayout;
// import javax.swing.*;
import javax.swing.JFrame;
// import java.awt.Image;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.event.ActionEvent;

import static utility.Constants.*;

// This comment was made by the Windows gang
public class SimulatorV1 {

  // String filePathToBaseApi = "./../api-data-master/data/";

  public static void main(String[] args) {

    // SimulatorV1 simulator = new SimulatorV1();

    // Requires run section in build.gradle
    // Scanner scanner = new Scanner(System.in);  // Create a Scanner object
    // System.out.println("Type to start application: ");
    //
    // String input = scanner.nextLine();  // Read user input

    System.out.println("SimulatorV1 starting (gradle)...");

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

    // GameState testGameState = gameState.deepCopy();

    // pman.storeFormImages(0);

    RandomPlayer player1 = new RandomPlayer(1);

    // RandomPlayer player2 = new RandomPlayer(2);
    // PlayerStrategyExpectimax player1 = new PlayerStrategyExpectimax(1,1);
    // PlayerStrategyMinimax player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    // PlayerStrategyMinimax_Ranges player2 = new PlayerStrategyMinimax_Ranges(2, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxII player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    // ^^ most accurate going second with no faint lookahead. 1.7 ish expected
    // PlayerStrategyExpectimaxIII player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    // PlayerStrategyMinimax_RangesII player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    // PlayerStrategyMinimaxII player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxIV player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    // PlayerStrategyMinimaxIII player2 = new PlayerStrategyMinimaxIII(2, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxV player2 = new PlayerStrategyExpectimaxV(2, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxV player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    PlayerStrategyExpectimaxVI player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    // PlayerStrategyExpectimaxVI player2 = new PlayerStrategyExpectimaxVI(2, PARTIALLY_OBSERVABLE);
    // PlayerStrategyExpectimaxIX player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);

    // frameGameState(frame, gameState);

    // frame.add(new DisplayHandler(frame.frameLength, frame.frameWidth, gameState));

    // Probably needs to be an extra param to runGame
    // SimulatorVisuals frame = new SimulatorVisuals("SimulatorV1", gameState);

    // frame.add(new DisplayHandler(this.frameLength, this.frameWidth, gameState));

    // SimulatorV1.runGame(gameState, player1, player2, -1, PRINTING_ENABLED, frame);
    SimulatorV1.runGame(gameState, player1, player2, -1, PRINTING_ENABLED, null);

    // System.out.println(testGameState.player1CurrentPokemon.currentHealth);
    // System.out.println(testGameState.player1Team.get(0).currentHealth);
    //
    // simulator.runGame(testGameState);

  }

  // Player objects passed in to help with ease testing using this function.
  // maxTurns is also helpful to control how long a game lasts during a test.
  public static GameState runGame(GameState gameState, Player pOne, Player pTwo, int maxTurns, int printMode, SimulatorVisuals frame) {

    Player player1 = pOne;
    Player player2 = pTwo;

    Action player1Action = null;
    Action player2Action = null;

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

    while ( !(gameState.player1Team.isOutOfUseablePokemon() ||  gameState.player2Team.isOutOfUseablePokemon()) && gameState.turnCount != maxTurns ) {

      // Frame updates
      if (frame != null) {
        frame.updateGameState(gameState, true);
        // frame.displayMoveButtons(gameState);
      }

      ArrayList<Integer> statEstimates = new ArrayList<Integer>();
      ArrayList<Boolean> wasItACrit = new ArrayList<Boolean>();

      player1Action = player1.chooseAction(gameState, gameState.generateAllValidActions(gameState, 1), printMode);
      player2Action = player2.chooseAction(gameState, gameState.generateAllValidActions(gameState, 2), printMode);

      // TODO: Priority based on move used to find which action to execute first.

      playerActions = gameState.whichActionToApplyFirst(gameState, player1Action, player2Action);

      if (playerActions == null) { break; }

      firstToExecute = playerActions.get(0);
      secondToExecute = playerActions.get(1);

      player1Pman = gameState.player1CurrentPokemon;
      player2Pman = gameState.player2CurrentPokemon;

      if (firstToExecute.isMove()) {

        Estimates firstEstimates = new Estimates();

        if (firstToExecute.playerMakingAction == 1) {
          gameState.applyMove(gameState, player1Pman, player1Pman.moves.get(firstToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, firstEstimates, wasItACrit, null);
          player2.moveSeen(gameState,player1Pman,player2Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
          player1.moveSeen(gameState,player1Pman,player2Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
        }
        else {
          gameState.applyMove(gameState, player2Pman, player2Pman.moves.get(firstToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, firstEstimates, wasItACrit, null);
          player1.moveSeen(gameState,player2Pman,player1Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
          player2.moveSeen(gameState,player2Pman,player1Pman,firstToExecute.indexOfAction,firstEstimates,printMode);
        }

      }
      else {

        if (firstToExecute.playerMakingAction == 1) {
          gameState.switchOut(gameState, 1, firstToExecute.indexOfAction, printMode, null);
        }
        else {
          gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode, null);
        }

      }

      if (secondToExecute.isMove()) {

        Estimates secondEstimates = new Estimates();

        if (secondToExecute.playerMakingAction == 1) {
          gameState.applyMove(gameState, player1Pman, player1Pman.moves.get(secondToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, secondEstimates, wasItACrit, null);
          player2.moveSeen(gameState,player1Pman,player2Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
          // The using player can also estimate target's def so needs another call
          player1.moveSeen(gameState,player1Pman,player2Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
        }
        else {
          gameState.applyMove(gameState, player2Pman, player2Pman.moves.get(secondToExecute.indexOfAction), NORMAL_PLAY, R_RANDOM, true, printMode, secondEstimates, wasItACrit, null);
          player1.moveSeen(gameState,player2Pman,player1Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
          // The using player can also estimate target's def so needs another call
          player2.moveSeen(gameState,player2Pman,player1Pman,secondToExecute.indexOfAction,secondEstimates,printMode);
        }

      }
      else {

        if (secondToExecute.playerMakingAction == 1) {
          gameState.switchOut(gameState, 1, secondToExecute.indexOfAction, printMode, null);
        }
        else {
          gameState.switchOut(gameState, 2, secondToExecute.indexOfAction, printMode, null);
        }

      }

      // ArrayList<String> endTInfo = new ArrayList<String>();

      gameState.endTurn(gameState, printMode, null);

      if (printMode == PRINTING_ENABLED) {
        System.out.println(gameState.humanReadableStateInfo());
      }

      // try { TimeUnit.SECONDS.sleep(2); }
      // catch (Exception e) { e.printStackTrace(); }

      if (player1Pman.currentHealth <= 0) {

        firstToExecute = player1.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,1), printMode);

        if (firstToExecute == null) { break; }

        gameState.switchOut(gameState, 1, firstToExecute.indexOfAction, printMode, null);

      }

      if (player2Pman.currentHealth <= 0) {

        firstToExecute = player2.switchOutFainted(gameState, gameState.generateAllValidSwitchActions(gameState,2), printMode);

        if (firstToExecute == null) { break; }

        gameState.switchOut(gameState, 2, firstToExecute.indexOfAction, printMode, null);

      }

      // try { TimeUnit.SECONDS.sleep(2); }
      // catch (Exception e) { e.printStackTrace(); }

      // Frame updates
      if (frame != null) {

        frame.updateGameState(gameState, true);

      }

    }

    if (printMode == PRINTING_ENABLED) {
      System.out.println("Battle finished.");
    }

    return gameState;

  }

}

// Bugs:

// Flare Blitz burns user (never actually happened)
// EffectiveAttack looking in wrong index (fixed)
// Stealth rock couldn't be laid
// non effective moves dealt 1 dmg (fixed)
// Neutral move modifier values looks suspiciusly low (think light screen was up)
