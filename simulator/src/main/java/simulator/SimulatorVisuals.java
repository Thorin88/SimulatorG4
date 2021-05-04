
// JFrame stuff
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// A Jframe object plus whatever extra we add
public class SimulatorVisuals extends JFrame {

  // X coords
  static int frameLength = 900;
  // Y coords
  static int frameWidth = 600;

  int moveButtonXSize = frameLength/6;
  int moveButtonYSize = moveButtonXSize/2;

  int moveButtonStartingX = frameLength/10;
  int moveButtonStartingY = frameWidth - moveButtonYSize - frameWidth/10;

  ActionButtonInner move1 = null;
  ActionButtonInner move2 = null;
  ActionButtonInner move3 = null;
  ActionButtonInner move4 = null;

  public SimulatorVisuals(String labelText, GameState intialGameState) {

    // JLabel label = new JLabel(labelText);

    // Was 200,300
    super.setTitle(labelText);
    super.setSize(frameLength, frameWidth);
    // super.setLocationRelativeTo(null);

    // Could just do this add in SV1
    // super.add(new DisplayHandler());
    // Needed to mount panel in frame
    super.add(new DisplayHandler(this.frameLength, this.frameWidth, intialGameState));
    super.setLocation(100,100);
    super.setResizable(false);
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.setVisible(true);

  }

  public void updateGameState(GameState currentGS, Boolean displayMoves) {

    this.getContentPane().removeAll();
    JPanel panelToAdd = new DisplayHandler(this.frameLength, this.frameWidth, currentGS);

    if (displayMoves) {

      displayMoveButtons(panelToAdd, currentGS);

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

    super.getContentPane().removeAll();

    Pokemon p2Pman = currentGS.player2CurrentPokemon;

    if (this.move1 == null) {

      Move move = p2Pman.moves.get(0);
      JButton moveB = makeMoveButton(move);
      moveB.setBounds(moveButtonStartingX,moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      panel.add(moveB);
      this.move1 = new ActionButtonInner();
      this.move1.setButton(moveB);
      this.move1.setActionIndex(0);

    }
    else {

      Move move = p2Pman.moves.get(0);
      JButton button = this.move1.buttonObj;
      button.setText(move.moveName);
      this.move1.setActionIndex(0);
      panel.add(button);

    }

    if (this.move2 == null) {

      Move move = p2Pman.moves.get(1);
      JButton moveB = makeMoveButton(move);
      moveB.setBounds(moveButtonStartingX + (moveButtonXSize),moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      panel.add(moveB);
      this.move2 = new ActionButtonInner();
      this.move2.setButton(moveB);
      this.move2.setActionIndex(1);

    }
    else {

      Move move = p2Pman.moves.get(1);
      JButton button = this.move2.buttonObj;
      button.setText(move.moveName);
      this.move2.setActionIndex(1);
      panel.add(button);

    }

    if (this.move3 == null) {

      Move move = p2Pman.moves.get(2);
      JButton moveB = makeMoveButton(move);
      moveB.setBounds(moveButtonStartingX + 2*(moveButtonXSize),moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      panel.add(moveB);
      this.move3 = new ActionButtonInner();
      this.move3.setButton(moveB);
      this.move3.setActionIndex(2);

    }
    else {

      Move move = p2Pman.moves.get(2);
      JButton button = this.move3.buttonObj;
      button.setText(move.moveName);
      this.move3.setActionIndex(2);
      panel.add(button);

    }

    if (this.move4 == null) {

      Move move = p2Pman.moves.get(3);
      JButton moveB = makeMoveButton(move);
      moveB.setBounds(moveButtonStartingX + 3*(moveButtonXSize),moveButtonStartingY,moveButtonXSize,moveButtonYSize);
      panel.add(moveB);
      this.move4 = new ActionButtonInner();
      this.move4.setButton(moveB);
      this.move4.setActionIndex(3);

    }
    else {

      Move move = p2Pman.moves.get(3);
      JButton button = this.move4.buttonObj;
      button.setText(move.moveName);
      this.move4.setActionIndex(3);
      panel.add(button);

    }

    super.getContentPane().add(panel);
    super.revalidate();

  }

  public JButton makeMoveButton(Move move) {

    String name = move.moveName;

    ActionButtonInner listener = new ActionButtonInner();
    JButton button = new JButton(name);
    button.addActionListener(listener);

    return button;

  }

  private class ActionButtonInner implements ActionListener {

    JButton buttonObj;
    int actionIndex;

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
    // Could call like runGame in here? Then show new game state?
    public void actionPerformed(ActionEvent e) {

      // frameWidth = frameWidth + 1;
      // repaint();

      if (e.getSource() == move1.buttonObj) {
        move1.buttonObj.setText("The button has been clicked");
      }

    }

    // @Override
    // public String toString() {
    //
    //   return "[" + this.formName + "," + this.formID +"]";
    //
    // }



  }

}
