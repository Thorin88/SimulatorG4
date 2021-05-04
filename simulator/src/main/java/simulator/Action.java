
import java.util.*;

public class Action {

  String typeOfAction;
  int indexOfAction;
  int playerMakingAction;

  Boolean isBest = false;

  float agentScore;

  public Action(String type, int index, int player) {

    this.typeOfAction = type;
    this.indexOfAction = index;
    this.playerMakingAction = player;

    this.agentScore = 0;

  }

  // Does not check if pman fainted or not.
  public Boolean isActionValid() {

    if (!this.typeOfAction.equals("switch") && !this.typeOfAction.equals("move")) {

      return false;

    }

    if ( (this.indexOfAction < 0 || this.indexOfAction > 3) && this.typeOfAction.equals("move") ) {

      return false;

    }

    if ( (this.indexOfAction < 0 || this.indexOfAction > 5) && this.typeOfAction.equals("switch") ) {

      return false;

    }

    return true;

  }

  public Boolean isMove() {

    if (this.typeOfAction.equals("move")) { return true; }

    return false;

  }

  public Boolean isSwitch() {

    if (this.typeOfAction.equals("switch")) { return true; }

    return false;

  }

  @Override
  public String toString() {

    return "[" + this.typeOfAction + "," + this.indexOfAction + "," + this.playerMakingAction + "," + this.agentScore + "]";

  }

}
