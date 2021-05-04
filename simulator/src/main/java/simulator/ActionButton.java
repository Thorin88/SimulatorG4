
import javax.swing.JButton;

// import java.awt.event.ActionListener;
// import java.awt.event.ActionEvent;

public class ActionButton {

  JButton buttonObj;
  int actionIndex;

  public ActionButton() {

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

  // @Override
  // public void actionPerformed(ActionEvent a) {
  //
  //   // frameWidth = frameWidth + 1;
  //   // repaint();
  //
  // }

  // @Override
  // public String toString() {
  //
  //   return "[" + this.formName + "," + this.formID +"]";
  //
  // }



}
