import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
// import org.junit.Order;
import static org.junit.Assert.*;

import static utility.Constants.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class CharmanderVsCharmanderTest {

  GameState gameState = null;

  String storeIn = "CharmanderVsCharmanderTest";

  // Link to online test report.
  // file:///C:/Users/Toby/Documents/3rdYearProject/3rdYearProjectCode/simulator/build/reports/tests/test/index.html
  @Before
  public void setupTests() {

    String[] moves2 = {"scratch","growl","",""};

    // Charmander
    Pokemon pman = new Pokemon();
    // Charmander
    Pokemon pman2 = new Pokemon();

    pman.buildUsingMoveNames(4,0,moves2,0,PRINTING_DISABLED);
    pman2.buildUsingMoveNames(4,0,moves2,0,PRINTING_DISABLED);

    int[] perfectIVs = {31,31,31,31,31,31};
    int[] noEVs = {0,0,0,0,0,0};

    pman.levelUpPokemon(5,perfectIVs,noEVs);
    pman2.levelUpPokemon(5,perfectIVs,noEVs);

    gameState = new GameState();

    gameState.player1Team.add(pman);
    gameState.player2Team.add(pman2);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

  }

  @Test @Ignore // (timeout=10000)
  public void Random() {

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    int gamesToPlay = 100000;

    String testName = "Random";

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    // System.out.println(gameState.player1CurrentPokemon.name);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void MinIII() {

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int gamesToPlay = 1000;

    String testName = "MinIII";

    // MinimaxIII

    player1 = new PlayerStrategyMinimaxIII(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyMinimaxIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyMinimaxIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxVIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyMinimaxIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyMinimaxIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpIV() {

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int gamesToPlay = 1000;

    String testName = "ExpIV";

    // ExpectimaxIV

    player1 = new PlayerStrategyExpectimaxIV(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIV(1, p1Obs);
    player2 = new PlayerStrategyMinimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIV(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxVIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIV(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIV(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpVI() {

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    int gamesToPlay = 1000;

    String testName = "ExpVI";

    // ExpectimaxVI (NEW)

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyMinimax(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyExpectimax(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyMinimax_RangesII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyMinimaxII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVI(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpVII() {

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    int gamesToPlay = 1000;

    String testName = "ExpVII";

    // ExpectimaxVII

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyMinimax(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimax(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyMinimax_RangesII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyMinimaxII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxVI(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpVIII() {

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int gamesToPlay = 1000;

    String testName = "ExpVIII";

    // ExpectimaxVIII

    player1 = new PlayerStrategyExpectimaxVIII(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVIII(1, p1Obs);
    player2 = new PlayerStrategyMinimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxVIII(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpIX() {

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int gamesToPlay = 1000;

    String testName = "ExpIX";

    // ExpectimaxIX

    player1 = new PlayerStrategyExpectimaxIX(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIX(1, p1Obs);
    player2 = new PlayerStrategyMinimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxVIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxIX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ExpX() {

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    int p1Obs = FULLY_OBSERVABLE;
    int p2Obs = FULLY_OBSERVABLE;

    int gamesToPlay = 1000;

    String testName = "ExpX";

    // ExpectimaxX

    player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    // player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    // player2 = new PlayerStrategyExpectimaxII(2, p2Obs);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    player2 = new PlayerStrategyMinimaxIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIV(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxVIII(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    player1 = new PlayerStrategyExpectimaxX(1, p1Obs);
    player2 = new PlayerStrategyExpectimaxIX(2, p2Obs);

    playGamesAndReturnWinLossOfPlayer1(gameState, gamesToPlay, player1, player2, testName);

    assertTrue(true);

  }

  public int[] playGamesAndReturnWinLossOfPlayer1 (GameState gameState, int games, Player player1, Player player2, String fileName) {

    int winCounter = 0;

    for (int i = 0; i < games; i++) {

      GameState gameStateCopy = gameState.deepCopy();

      long startTime = System.nanoTime();
      SimulatorV1.runGame(gameStateCopy, player1, player2, -1, PRINTING_DISABLED, null);
      long endTime = System.nanoTime();
      long duration = (endTime - startTime) / 1000000000;  //divide by 1000000 to get milliseconds.

      // System.out.println("Game " + (i+1) + ": " + duration);

      if (gameStateCopy.player2Team.isOutOfUseablePokemon()) { winCounter++; }

    }

    // print counter with result
    // Could also print obserability
    System.out.println(player1.name + " Vs " + player2.name + " -> Wins: " + winCounter + ", Losses: " + (games - winCounter));

    // Writing to results file
    String filePathToBaseApi = "./../testResults/";

    File targetDir = new File(filePathToBaseApi + this.storeIn);
    if (!targetDir.exists()) {
        System.out.println("Created " + filePathToBaseApi + this.storeIn);
        targetDir.mkdirs();
    }

    String createdFileName = filePathToBaseApi + this.storeIn + "/" + fileName + ".txt";

    try {
      File file = new File(createdFileName);
      // Delete old file
      // if (file.exists()) {
      //   if (file.delete()) {
      //     System.out.println("Deleted the file: " + file.getName());
      //   } else {
      //     System.out.println("Failed to delete the file.");
      //   }
      // }
      // Create file
      if (file.createNewFile()) {
        System.out.println("File created: " + file.getName());
      } else {
          // System.out.println("File already exists.");
      }
    } catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    try (FileWriter fw = new FileWriter(createdFileName, true);
    BufferedWriter bw = new BufferedWriter(fw);
    PrintWriter out = new PrintWriter(bw)) {

      out.println(player1.name + "," + player2.name + "," + winCounter + "," + (games - winCounter));

    } catch (Exception e) {
      e.printStackTrace();
    }

    return new int[] {winCounter, games-winCounter, games};

  }

}
