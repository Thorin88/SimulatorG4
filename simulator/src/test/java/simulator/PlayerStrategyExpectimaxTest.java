import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
// import org.junit.Order;
import static org.junit.Assert.*;

import static utility.Constants.*;

public class PlayerStrategyExpectimaxTest {

  // Link to online test report.
  // file:///C:/Users/Toby/Documents/3rdYearProject/3rdYearProjectCode/simulator/build/reports/tests/test/index.html

  // @Before
  // public void setupTests() {
  //
  // }

  // Best Move puzzles

  @Test // (timeout=10000)
  public void mulitpleWinningMovesOneMoreLikely_1() {

    String[] moves1 = {"thunder","iron-tail","quick-attack","thunderbolt"};

    String[] moves7 = {"stealth-rock","stone-edge","earthquake","swords-dance"};

    // Pikachu
    Pokemon pman = new Pokemon();

    // Groudon
    Pokemon pman7 = new Pokemon();

    pman.buildUsingMoveNames(25,0,moves1,0,PRINTING_DISABLED);
    pman7.buildUsingMoveNames(383,0,moves7,0,PRINTING_DISABLED);

    int[] perfectIVs = {31,31,31,31,31,31};

    int[] pAttacker = {6,252,0,0,0,252};
    int[] sAttacker= {6,0,0,252,0,252};

    pman.levelUpPokemon(88,perfectIVs,sAttacker);
    pman7.levelUpPokemon(80,perfectIVs,pAttacker);

    GameState gameState = new GameState();

    gameState.player1Team.add(pman7);

    gameState.player2Team.add(pman);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // GameState testGameState = gameState.deepCopy(gameState);

    PlayerStrategyMinimaxII player1 = new PlayerStrategyMinimaxII(1,FULLY_OBSERVABLE);
    RandomPlayer player2 = new RandomPlayer(2);

    Action player1Action = player1.chooseAction(gameState, gameState.generateAllValidActions(gameState, 1), PRINTING_DISABLED);

    // testSimulator.runGame(gameState, player1, player2, -1, PRINTING_DISABLED);

    // (player1Action.isMove() && player1Action.indexOfAction == 2) {
    //
    //
    //
    // }


    assertTrue("Groudon did not pick the best move, Earthquake. Instead picked: " + player1Action.indexOfAction, player1Action.isMove() && player1Action.indexOfAction == 2);

  }

  // Small scale battles

  // Large scale battles

  @Test @Ignore// (timeout=10000)
  public void ubersVsRed() {

    String[] moves1 = {"thunder","iron-tail","quick-attack","thunderbolt"};

    String[] moves2 = {"body-slam","brine","blizzard","psychic"};

    String[] moves3 = {"shadow-ball","crunch","blizzard","giga-impact"};

    String[] moves4 = {"frenzy-plant","giga-drain","sludge-bomb","sleep-powder"};

    String[] moves5 = {"blast-burn","flare-blitz","air-slash","dragon-pulse"};

    String[] moves6 = {"hydro-cannon","blizzard","flash-cannon","focus-blast"};

    String[] moves7 = {"stealth-rock","stone-edge","earthquake","swords-dance"};

    String[] moves8 = {"roar-of-time","thunder-wave","sandstorm","earth-power"};

    String[] moves9 = {"toxic","recover","light-screen","aeroblast"};

    String[] moves10 = {"rain-dance","thunder","hydro-pump","water-spout"};

    String[] moves11 = {"spacial-rend","aura-sphere","rain-dance","thunder"};

    String[] moves12 = {"dark-void","dark-pulse","nasty-plot","thunder"};

    // Red's Team

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

    // Uber Team

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

    pman.buildUsingMoveNames(25,0,moves1,0,PRINTING_DISABLED);
    pman2.buildUsingMoveNames(131,0,moves2,0,PRINTING_DISABLED);
    pman3.buildUsingMoveNames(143,0,moves3,0,PRINTING_DISABLED);
    pman4.buildUsingMoveNames(3,0,moves4,0,PRINTING_DISABLED);
    pman5.buildUsingMoveNames(6,0,moves5,0,PRINTING_DISABLED);
    pman6.buildUsingMoveNames(9,0,moves6,0,PRINTING_DISABLED);
    pman7.buildUsingMoveNames(383,0,moves7,0,PRINTING_DISABLED);
    pman8.buildUsingMoveNames(483,0,moves8,0,PRINTING_DISABLED);
    pman9.buildUsingMoveNames(249,0,moves9,0,PRINTING_DISABLED);
    pman10.buildUsingMoveNames(382,0,moves10,0,PRINTING_DISABLED);
    pman11.buildUsingMoveNames(484,0,moves11,0,PRINTING_DISABLED);
    pman12.buildUsingMoveNames(491,0,moves12,0,PRINTING_DISABLED);

    int[] perfectIVs = {31,31,31,31,31,31};

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

    gameState.player2Team.add(pman);
    gameState.player2Team.add(pman2);
    gameState.player2Team.add(pman3);
    gameState.player2Team.add(pman4);
    gameState.player2Team.add(pman5);
    gameState.player2Team.add(pman6);

    gameState.player1Team.add(pman7);
    gameState.player1Team.add(pman8);
    gameState.player1Team.add(pman9);
    gameState.player1Team.add(pman10);
    gameState.player1Team.add(pman11);
    gameState.player1Team.add(pman12);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);


    // RandomPlayer player2 = new RandomPlayer(2);
    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 100, player1, player2);

    // player1 = new PlayerStrategyMinimax_RangesII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // Expectimax
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxII
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIII
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIV
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // MinimaxII
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxV
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    assertTrue(true);

  }

  @Test @Ignore // (timeout=10000)
  public void ubersVsUbers() {

    String[] moves1 = {"thunder","iron-tail","quick-attack","thunderbolt"};

    String[] moves2 = {"body-slam","brine","blizzard","psychic"};

    String[] moves3 = {"shadow-ball","crunch","blizzard","giga-impact"};

    String[] moves4 = {"frenzy-plant","giga-drain","sludge-bomb","sleep-powder"};

    String[] moves5 = {"blast-burn","flare-blitz","air-slash","dragon-pulse"};

    String[] moves6 = {"hydro-cannon","blizzard","flash-cannon","focus-blast"};

    String[] moves7 = {"stealth-rock","stone-edge","earthquake","swords-dance"};

    String[] moves8 = {"roar-of-time","thunder-wave","sandstorm","earth-power"};

    // String[] moves9 = {"toxic","recover","light-screen","aeroblast"};
    // To shorten games (recover is the offender)
    String[] moves9 = {"toxic","gust","light-screen","aeroblast"};

    String[] moves10 = {"rain-dance","thunder","hydro-pump","water-spout"};

    String[] moves11 = {"spacial-rend","aura-sphere","rain-dance","thunder"};

    String[] moves12 = {"dark-void","dark-pulse","nasty-plot","thunder"};

    // Red's Team

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

    // Uber Team

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

    pman.buildUsingMoveNames(383,0,moves7,0,PRINTING_DISABLED);
    pman2.buildUsingMoveNames(483,0,moves8,0,PRINTING_DISABLED);
    pman3.buildUsingMoveNames(249,0,moves9,0,PRINTING_DISABLED);
    pman4.buildUsingMoveNames(382,0,moves10,0,PRINTING_DISABLED);
    pman5.buildUsingMoveNames(484,0,moves11,0,PRINTING_DISABLED);
    pman6.buildUsingMoveNames(491,0,moves12,0,PRINTING_DISABLED);
    pman7.buildUsingMoveNames(383,0,moves7,0,PRINTING_DISABLED);
    pman8.buildUsingMoveNames(483,0,moves8,0,PRINTING_DISABLED);
    pman9.buildUsingMoveNames(249,0,moves9,0,PRINTING_DISABLED);
    pman10.buildUsingMoveNames(382,0,moves10,0,PRINTING_DISABLED);
    pman11.buildUsingMoveNames(484,0,moves11,0,PRINTING_DISABLED);
    pman12.buildUsingMoveNames(491,0,moves12,0,PRINTING_DISABLED);

    int[] perfectIVs = {31,31,31,31,31,31};

    int[] pBulk = {252,0,252,6,0,0};
    int[] sBulk = {252,0,6,0,252,0};
    int[] pAttacker = {6,252,0,0,0,252};
    int[] sAttacker= {6,0,0,252,0,252};

    pman.levelUpPokemon(80,perfectIVs,pAttacker);
    pman2.levelUpPokemon(80,perfectIVs,sAttacker);
    pman3.levelUpPokemon(80,perfectIVs,sBulk);
    pman4.levelUpPokemon(80,perfectIVs,sAttacker);
    pman5.levelUpPokemon(80,perfectIVs,sAttacker);
    pman6.levelUpPokemon(80,perfectIVs,sAttacker);
    pman7.levelUpPokemon(80,perfectIVs,pAttacker);
    pman8.levelUpPokemon(80,perfectIVs,sAttacker);
    pman9.levelUpPokemon(80,perfectIVs,sBulk);
    pman10.levelUpPokemon(80,perfectIVs,sAttacker);
    pman11.levelUpPokemon(80,perfectIVs,sAttacker);
    pman12.levelUpPokemon(80,perfectIVs,sAttacker);

    GameState gameState = new GameState();

    gameState.player2Team.add(pman);
    gameState.player2Team.add(pman2);
    gameState.player2Team.add(pman3);
    gameState.player2Team.add(pman4);
    gameState.player2Team.add(pman5);
    gameState.player2Team.add(pman6);

    gameState.player1Team.add(pman7);
    gameState.player1Team.add(pman8);
    gameState.player1Team.add(pman9);
    gameState.player1Team.add(pman10);
    gameState.player1Team.add(pman11);
    gameState.player1Team.add(pman12);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    // Confirm that this is the right state
    // System.out.println(gameState.humanReadableStateInfo());

    // RandomPlayer player2 = new RandomPlayer(2);
    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);

    // player1 = new PlayerStrategyMinimax_RangesII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);


    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // Expectimax
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxII
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIII
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIV
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // MinimaxII
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxV
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // ExpectimaxVI (NEW)

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // // ExpectimaxVII
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxVIII
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIX
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxX
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // Small tests

    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVI(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);

    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxVIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 20, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 20, player1, player2);
    //

    // Temp Testing

    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxX(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 10, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxVII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxX(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    //

    player1 = new PlayerStrategyExpectimaxVIII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 500, player1, player2);

    assertTrue(true);

  }

  @Test @Ignore// (timeout=10000)
  public void starter_scenario_gen1() {

    // Player 2 has the Charmander

    String[] moves1 = {"tackle","growl","",""};

    String[] moves2 = {"scratch","growl","",""};

    // Bulbasaur
    Pokemon pman = new Pokemon();

    // Charmander
    Pokemon pman2 = new Pokemon();


    pman.buildUsingMoveNames(1,0,moves1,0,PRINTING_DISABLED);
    pman2.buildUsingMoveNames(4,0,moves2,0,PRINTING_DISABLED);

    int[] perfectIVs = {31,31,31,31,31,31};

    int[] noEVs = {0,0,0,0,0,0};

    pman.levelUpPokemon(5,perfectIVs,noEVs);
    pman2.levelUpPokemon(5,perfectIVs,noEVs);

    GameState gameState = new GameState();

    gameState.player1Team.add(pman);

    gameState.player2Team.add(pman2);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    Player player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    Player player2 = new RandomPlayer(2);

    player1 = new PlayerStrategyMinimax_RangesII(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // Expectimax

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // ExpectimaxII

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // ExpectimaxIII

    player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // ExpectimaxIV

    player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // MinimaxII

    player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    // ExpectimaxV

    player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    player2 = new RandomPlayer(2);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    assertTrue(true);

  }

  @Test  // (timeout=10000)
  public void starter_scenario_gen1_samePman() {

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

    GameState gameState = new GameState();

    gameState.player1Team.add(pman);

    gameState.player2Team.add(pman2);

    gameState.player1CurrentPokemon = gameState.player1Team.get(0);
    gameState.player2CurrentPokemon = gameState.player2Team.get(0);

    Player player1 = new RandomPlayer(1);
    Player player2 = new RandomPlayer(2);

    // playGamesAndReturnWinLossOfPlayer1(gameState, 100, player1, player2);


    // Player player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // Player player2 = new RandomPlayer(2);

    // player1 = new PlayerStrategyMinimax_RangesII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimax(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // Expectimax
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimax(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxII
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIII
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxIV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxIV
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimaxII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxIV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // MinimaxII
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyMinimaxII(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyExpectimaxV(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // // ExpectimaxV
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new RandomPlayer(2);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);
    //
    // player1 = new PlayerStrategyExpectimaxV(1, FULLY_OBSERVABLE);
    // player2 = new PlayerStrategyMinimax_RangesII(2, FULLY_OBSERVABLE);
    //
    // playGamesAndReturnWinLossOfPlayer1(gameState, 50, player1, player2);

    player1 = new PlayerStrategyExpectimaxVI(1, FULLY_OBSERVABLE);
    player2 = new PlayerStrategyExpectimaxX(2, FULLY_OBSERVABLE);

    playGamesAndReturnWinLossOfPlayer1(gameState, 500, player1, player2);

    assertTrue(true);

  }


  public int[] playGamesAndReturnWinLossOfPlayer1 (GameState gameState, int games, Player player1, Player player2) {

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

    return new int[] {winCounter, games-winCounter, games};

  }

}
