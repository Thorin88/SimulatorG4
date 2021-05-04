
// JFrame stuff
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.FontMetrics;

import java.util.ArrayList;

public class SummaryHandler extends JPanel/* implements ActionListener*/ {

  Timer timer;

  int frameLength;
  int frameWidth;

  GameState currentGS;
  String toDisplay = "";
  int charPointer = 0;

  Pokemon player2Pman;
  int p2FormID;

  int p2StatusID = 0;

  ImageIcon player2PmanImgIcon;
  Image player2PmanImg;

  // Could be a constant
  static String filePathToBaseApi = "./../";

  public SummaryHandler(int fL, int fW, GameState gameState, int targetIndex, String message) {

    // super();

    super.setDoubleBuffered(true);
    super.setLayout(null);

    // this.timer = new Timer(7,this);
    // timer.start();

    this.frameLength = fL;
    this.frameWidth = fW;

    this.currentGS = gameState;
    this.toDisplay = message;
    // // Added for extra safety around threads
    // this.currentGS = gameState.deepCopy();

    if (targetIndex == -1) {
      this.player2Pman = currentGS.player2CurrentPokemon;
      this.p2FormID = player2Pman.form.formID;
    }
    else {
      this.player2Pman = currentGS.player2Team.get(targetIndex);
      this.p2FormID = player2Pman.form.formID;
    }

    this.p2StatusID = getStatus(player2Pman);

    this.player2PmanImgIcon = new ImageIcon(filePathToBaseApi + "sprites/sprites/pokemon/" + p2FormID + ".png");
    this.player2PmanImg = player2PmanImgIcon.getImage();
    this.player2PmanImg = (new ImageIcon(player2PmanImg.getScaledInstance(frameLength/5, -1, Image.SCALE_SMOOTH))).getImage();

    // int scrollSpeed = 20;
    //
    // Timer timer = new Timer(scrollSpeed, null);
    // timer.addActionListener(new ActionListener() {
    //           @Override
    //           public void actionPerformed(ActionEvent e) {
    //
    //             if (charPointer < toDisplay.length()) {
    //               charPointer++;
    //             }
    //             else {
    //               timer.stop();
    //               return;
    //             }
    //             repaint();
    //           }
    //       });
    // timer.start();

  }

    // JPanel panel = new JPanel();

    @Override
    protected void paintComponent(Graphics g) {

      // System.out.println("******************** Being repainted ****************************");

      int leftSideYOffset = frameWidth/8;

      // Truncated div
      int upperOvalTopLeft_X = 11*(frameLength/40);
      int upperOvalTopLeft_Y = 3*(frameWidth/20);
      int upperOvalLength = frameLength/3;
      int upperOvalWidth = frameWidth/5;

      int upperOvalCentreX = upperOvalTopLeft_X + (upperOvalLength)/2;
      int upperOvalCentreY = upperOvalTopLeft_Y + (upperOvalWidth)/2;

      int lowerOvalTopLeft_X = 1*(frameLength/40);
      int lowerOvalTopLeft_Y = 9*(frameWidth/20);
      int lowerOvalLength = frameLength/3;
      int lowerOvalWidth = frameWidth/5;

      int lowerOvalCentreX = lowerOvalTopLeft_X + (lowerOvalLength)/2;
      int lowerOvalCentreY = lowerOvalTopLeft_Y + (lowerOvalWidth)/2;

      int textBoxStartX = lowerOvalTopLeft_X;
      int textBoxEndX = (upperOvalTopLeft_X+upperOvalLength)-lowerOvalTopLeft_X;
      int textBoxStartY = lowerOvalTopLeft_Y+lowerOvalWidth+frameWidth/40;
      int textBoxLength = textBoxEndX;
      int textBoxWidth = lowerOvalWidth;

      int startOfP2BoxX = lowerOvalTopLeft_X + lowerOvalLength + lowerOvalLength/20;
      int startOfP2BoxY = lowerOvalTopLeft_Y - lowerOvalWidth/4;
      int lengthOfP2Box = textBoxEndX + textBoxStartX - startOfP2BoxX;
      startOfP2BoxX = lowerOvalCentreX  - (player2PmanImg.getWidth(null))/2;
      startOfP2BoxY =  lowerOvalCentreY - (player2PmanImg.getHeight(null))/2 - (player2PmanImg.getHeight(null))/4 - frameWidth/6 + leftSideYOffset;
      // int widthOfP2Box = lowerOvalWidth;
      int widthOfP2Box = (int)(lowerOvalWidth*0.7);

      // super will be the frame the panel is mouhnted in.
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D) g;

      // Background
      g2d.setColor(new Color(186,215,228));
      g2d.fillRect(0,0,frameLength,frameWidth);

      int centreOfHPBox = startOfP2BoxX+lengthOfP2Box/2;

      int imgStartX = centreOfHPBox - ((player2PmanImg.getWidth(null))/2);
      int imgStartY = lowerOvalCentreY - (player2PmanImg.getHeight(null))/2 - (player2PmanImg.getHeight(null))/4 + leftSideYOffset;

      // Could do graphics 2d stuff
      g.drawImage(player2PmanImg, imgStartX, imgStartY, this);

      // if (player1Pman.currentHealth > 0) {
      //   g.drawImage(player1PmanImg, upperOvalCentreX - (player1PmanImg.getWidth(null))/2, upperOvalCentreY - (player1PmanImg.getHeight(null))/2 - (player1PmanImg.getHeight(null))/4, this);
      // }
      // if (player2Pman.currentHealth > 0) {
      //   g.drawImage(player2PmanImg, lowerOvalCentreX  - (player2PmanImg.getWidth(null))/2, lowerOvalCentreY - (player2PmanImg.getHeight(null))/2 - (player2PmanImg.getHeight(null))/4, this);
      // }

      // Could do inner rectangles
      g2d.setStroke(new BasicStroke(5));
      // g2d.setColor(new Color(0, 0, 0));
      // g2d.setColor(new Color(255, 0, 0));
      // g2d.fillRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);
      // g2d.setColor(new Color(255, 255, 255));
      // Text Box
      // g2d.setColor(new Color(101, 155, 155));
      // g2d.fillRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);
      // g2d.setColor(new Color(201, 70, 54));
      // g2d.drawRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);

      g2d.setStroke(new BasicStroke(1));

      int userHP = this.player2Pman.currentHealth;
      int userMaxHP = this.player2Pman.stats.get(0).getStatValue();

      // Make a method to spam repaint until hp in the current place;

      // g2d.setStroke(new BasicStroke(5));
      // g2d.setColor(new Color(0, 0, 0));
      g2d.setColor(new Color(128,128,128));
      g2d.setStroke(new BasicStroke(5));
      g2d.fillRect(startOfP2BoxX,startOfP2BoxY,lengthOfP2Box,widthOfP2Box);
      g2d.setColor(new Color(0,0,0));
      g2d.drawRect(startOfP2BoxX,startOfP2BoxY,lengthOfP2Box,widthOfP2Box);

      int p2HPStartX = startOfP2BoxX + 6*(lengthOfP2Box/20);
      int p2HPStartY = startOfP2BoxY + 6*(widthOfP2Box/10);
      int p2HPLength = 6*(lengthOfP2Box/10);
      // int p2HPWidth = 1*(widthOfP2Box/20);
      int p2HPWidth = (int) (1.5*(widthOfP2Box/20));

      g2d.setColor(new Color(0,0,0));
      g2d.fillRect(p2HPStartX,p2HPStartY,p2HPLength,p2HPWidth);

      float hpRatio = (float) userHP / userMaxHP;

      // Fill in health bar
      if (userHP > 0) {

        int p2HPToDraw = (int) (hpRatio * p2HPLength);
        // System.out.println(p2HPToDraw);

        if (p2HPToDraw <= 0) { p2HPToDraw = 1; }

        if (hpRatio > 0.5) { g2d.setColor(new Color(4,150,8)); }
        else if (hpRatio <= 0.5 && hpRatio > 0.2) { g2d.setColor(new Color(235,150,0)); }
        else { g2d.setColor(new Color(247,65,1)); }

        g2d.fillRect(p2HPStartX,p2HPStartY,p2HPToDraw,p2HPWidth);

      }

      g2d.setColor(new Color(255,255,255));
      g2d.setStroke(new BasicStroke(2));
      g2d.drawRect(p2HPStartX,p2HPStartY,p2HPLength,p2HPWidth);

      int cDigits = 0;
      int mDigits = 0;

      if (userHP <= 9) {
        cDigits = 1;
      }
      else if (userHP <= 99) {
        cDigits = 2;
      }
      else {
        cDigits = 3;
      }

      if (userMaxHP <= 9) {
        mDigits = 1;
      }
      else if (userMaxHP <= 99) {
        mDigits = 2;
      }
      else {
        mDigits = 3;
      }

      int digDifference = (mDigits - cDigits);
      String hpPadding = "";

      for (int i = 0; i < digDifference; i++) {
        hpPadding += " ";
      }

      // Shadow
      g2d.setColor(new Color(0,0,0));
      g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      // Y was 4*
      g2d.drawString(hpPadding+userHP+" / "+userMaxHP,1 + p2HPStartX + (int) ( 0.2 * p2HPLength),1+p2HPStartY + (int) (4 * (p2HPWidth)) );

      g2d.setColor(new Color(255,255,255));
      g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      g2d.drawString(hpPadding+userHP+" / "+userMaxHP,p2HPStartX + (int) ( 0.2 * p2HPLength),p2HPStartY + (int) (4 * (p2HPWidth)) );

      String p2Name = this.player2Pman.name;
      int level = this.player2Pman.level;

      drawStatus(p2StatusID,g2d,p2HPStartX-p2HPWidth*4,p2HPStartY+p2HPWidth/2,3*p2HPWidth);

      g2d.setColor(new Color(0,0,0));
      g2d.drawString("Lv" + level,p2HPStartX + (int) ( 0.7 * p2HPLength)+1,1+p2HPStartY - (int) (2 * (p2HPWidth)) );
      g2d.setColor(new Color(255,255,255));
      g2d.drawString("Lv" + level,p2HPStartX + (int) ( 0.7 * p2HPLength),p2HPStartY - (int) (2 * (p2HPWidth)) );

      g2d.setColor(new Color(0,0,0));
      g2d.drawString(p2Name,p2HPStartX+1,1+p2HPStartY - (int) (2 * (p2HPWidth)) );
      g2d.setColor(new Color(255,255,255));
      g2d.drawString(p2Name,p2HPStartX,p2HPStartY - (int) (2 * (p2HPWidth)) );

        // AnimateMessage

      // Match the textBoxVars
      // int messageX = lowerOvalTopLeft_X;
      // int messageY = lowerOvalTopLeft_Y+lowerOvalWidth+frameWidth/40;
      //
      // int xOffSet = textBoxLength/40;
      // int yOffSet = textBoxWidth/16;
      //
      // // g2d.setColor(new Color(0,0,0));
      // g2d.setColor(new Color(255,255,255));
      // g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
      // newLineDrawString(g2d,this.toDisplay.substring(0,charPointer),textBoxStartX+xOffSet,textBoxStartY+yOffSet);

      g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      for (int i = 0; i < 4; i++) {
        drawMove(i,g2d,frameLength-(frameLength/6+frameLength/4+frameLength/20),frameLength/5,frameWidth/20+i*(frameWidth/5),frameWidth/5);
      }

      drawStats(g2d,frameLength/3,frameLength/6,frameWidth/20+1*(frameWidth/5) + leftSideYOffset,frameWidth/4);

      // TODO: Y coord
      drawPmanTypes(g2d,imgStartY + player2PmanImg.getHeight(null),centreOfHPBox);

    }

    // https://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
    private void newLineDrawString(Graphics g, String text, int x, int y) {
      for (String line : text.split("\n")) {
        g.drawString(line, x, y += g.getFontMetrics().getHeight());
      }
    }

      // @Override
      // public void actionPerformed(ActionEvent a) {
      //
      //   // frameWidth = frameWidth + 1;
      //   // repaint();
      //
      // }

    public int getStatus(Pokemon pman) {

      PermanentStatusConditions status = pman.statusUnaffectedBySwitching;

      int size = status.flags.size();
      int idOfStatus = -1;

      for (int i = 0; i < size; i++) {

        if (status.flags.get(i).turnsRemaining != -1) {
          idOfStatus = status.flags.get(i).getFlagID();
        }

      }

      return idOfStatus;

    }

    // Color can be changed by this method
    public void drawStatus(int id, Graphics2D g2d, int x, int y, int r) {

      Color c = null;

      switch (id) {

        // Para
        case 1:
          c = new Color(239,173,0);
          break;
        // Sleep
        case 2:
          c = new Color(156,156,156);
          break;
        // Frozen
        case 3:
          c = new Color(74,148,255);
        break;
        // Burn
        case 4:
          c = new Color(255,66,16);
          break;
        // Poison
        case 5:
          c = new Color(222,90,214);
          break;
        default:
          break;

      }

      if (c == null) { return; }

      // https://stackoverflow.com/questions/19386951/how-to-draw-a-circle-with-given-x-and-y-coordinates-as-the-middle-spot-of-the-ci
      x = x-(r/2);
      g2d.setColor(c);
      y = y-(r/2);
      g2d.fillOval(x,y,r,r);
      g2d.setColor(new Color(255,255,255));
      g2d.setStroke(new BasicStroke(2));
      g2d.drawOval(x,y,r,r);

    }



    public void drawStats(Graphics2D g2d, int startX, int lengthX, int startY, int widthY) {

      ArrayList<Stat> stats = player2Pman.stats;
      ArrayList<StatChange> statChanges = player2Pman.statChangeLevels;

      int sectionSize = widthY/6;

      g2d.setColor(new Color(224,232,232));
      g2d.setStroke(new BasicStroke(3));
      g2d.fillRect(startX,startY,lengthX,widthY+2*sectionSize);

      g2d.setStroke(new BasicStroke(2));
      g2d.setColor(new Color(168,184,192));

      g2d.drawLine(startX,startY+sectionSize,startX+lengthX,startY+sectionSize);

      g2d.setColor(new Color(64,64,64));
      // String name = UtilityFunctions.toCaps(move.moveName);
      g2d.drawString("Statistics", startX+lengthX/20, (startY+3*sectionSize/4));

      int changeLevel = statChanges.get(0).level;
      String stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Attack: \t"+stats.get(1).base_stat+" ("+stringToAdd+")", startX+lengthX/20, (startY+sectionSize+3*sectionSize/4));
      changeLevel = statChanges.get(1).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Defense: \t"+stats.get(2).base_stat+" ("+stringToAdd+")", startX+lengthX/20, (startY+2*sectionSize+3*sectionSize/4));
      changeLevel = statChanges.get(2).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Sp. Attack: \t"+stats.get(3).base_stat+" ("+stringToAdd+")", startX+lengthX/20, (startY+3*sectionSize+3*sectionSize/4));
      changeLevel = statChanges.get(3).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Sp. Defense: \t"+stats.get(4).base_stat+" ("+stringToAdd+")", startX+lengthX/20, (startY+4*sectionSize+3*sectionSize/4));
      changeLevel = statChanges.get(4).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Speed: \t"+stats.get(5).base_stat+" ("+stringToAdd+")", startX+lengthX/20, (startY+5*sectionSize+3*sectionSize/4));

      changeLevel = statChanges.get(5).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Accuracy: \t("+stringToAdd+")", startX+lengthX/20, (startY+6*sectionSize+3*sectionSize/4));
      changeLevel = statChanges.get(6).level;
      stringToAdd = "";
      if (changeLevel >= 0) {
        stringToAdd = "+";
      }
      stringToAdd += changeLevel;
      g2d.drawString("Evasion: \t("+stringToAdd+")", startX+lengthX/20, (startY+7*sectionSize+3*sectionSize/4));


      g2d.setStroke(new BasicStroke(3));
      g2d.setColor(new Color(64,64,64));
      g2d.drawRect(startX,startY,lengthX,widthY+2*sectionSize);

    }

    public void drawMove(int index, Graphics2D g2d, int startX, int lengthX, int startY, int widthY) {

      // g2d.setFont(new Font("SansSerif", Font.BOLD, 17));

      g2d.setColor(new Color(224,232,232));

      Move move = player2Pman.moves.get(index);
      if (move == null) { return; }

      // g2d.setColor(new Color(128,128,128));
      g2d.setColor(new Color(224,232,232));
      // g2d.setColor(new Color(101, 155, 155));
      // g2d.setColor(new Color(168, 240, 216));
      g2d.setStroke(new BasicStroke(3));
      g2d.fillRect(startX,startY,lengthX,widthY);

      g2d.setStroke(new BasicStroke(2));

      int sectionSize = widthY/5;

      // g2d.setColor(new Color(168,184,192));
      // g2d.drawLine(startX,startY+sectionSize,startX+lengthX,startY+sectionSize);

      Color standardTextColour = new Color(64,64,64);
      Color typeColour = UtilityFunctions.typeToColour(move.metaData.typeID);
      // g2d.setColor(typeColour);
      g2d.setColor(standardTextColour);
      String name = UtilityFunctions.toCaps(move.moveName);

      g2d.drawString(name, startX+lengthX/20, (startY+3*sectionSize/4));

      g2d.setColor(standardTextColour);

      String moveCat = "";
      if (GameState.isDamaging(move.metaData.moveCategoryID)) {
        moveCat = UtilityFunctions.toCaps(move.metaData.dmgType);
      }
      else {
        moveCat = "Status";
      }

      g2d.drawString("Category: \t"+moveCat, startX+lengthX/20, (startY+sectionSize+3*sectionSize/4));

      // g2d.drawString("Type: \t"+UtilityFunctions.toCaps(move.metaData.typeName), startX+lengthX/20, (startY+2*sectionSize+3*sectionSize/4));
      drawStringWithColourInBoxes(g2d,"Type: ",new Color(64,64,64),UtilityFunctions.toCaps(move.metaData.typeName),typeColour, startX+lengthX/20, (startY+2*sectionSize+3*sectionSize/4));

      int pow = move.metaData.power;
      String power = "";
      if (pow == 0) {
        power = "---";
      }
      else {
        power += pow;
      }

      g2d.drawString("Power: \t"+power, startX+lengthX/20, (startY+3*sectionSize+3*sectionSize/4));
      // TODO: Use this
      // drawStringWithColour(g2d,"Power ",new Color(64,64,64),power,new Color(255,0,0), startX+lengthX/20, (startY+3*sectionSize+3*sectionSize/4));

      int acc = move.metaData.accuracy;
      String accuracy = "";
      if (acc == 0) {
        accuracy = "---";
      }
      else {
        accuracy += acc;
      }

      g2d.drawString("Accuracy: \t"+accuracy, startX+lengthX/20, (startY+4*sectionSize+3*sectionSize/4));

      int effectBoxLength = frameLength/6;
      int effectBoxDepth = widthY;

      g2d.setStroke(new BasicStroke(3));
      g2d.setColor(new Color(224,232,232));
      g2d.fillRect(startX+lengthX,startY,effectBoxLength+lengthX/20,effectBoxDepth);

      g2d.setColor(new Color(168,184,192));
      g2d.drawLine(startX,startY+sectionSize,startX+lengthX+effectBoxLength+lengthX/20,startY+sectionSize);

      // g2d.setStroke(new BasicStroke(3));
      // g2d.setColor(new Color(64,64,64));
      // g2d.drawRect(startX+lengthX,startY+sectionSize,effectBoxLength+lengthX/20,effectBoxDepth-sectionSize);

      g2d.setStroke(new BasicStroke(3));
      g2d.setColor(new Color(64,64,64));
      g2d.drawRect(startX,startY,lengthX+effectBoxLength+lengthX/20,widthY);

      g2d.setColor(new Color(64,64,64));
      // drawTextWithWrap(move.metaData.effect, g2d.getFontMetrics(), startX+lengthX+lengthX/20, (startY+3*sectionSize/4),g2d,effectBoxLength,(startY+4*sectionSize+3*sectionSize/4));
      // Inline with category not name
      g2d.drawString("Effect", startX+lengthX+lengthX/20, (startY+3*sectionSize/4));
      drawTextWithWrap(move.metaData.short_effect, g2d.getFontMetrics(), startX+lengthX+lengthX/20, (startY+sectionSize+3*sectionSize/4),g2d,effectBoxLength,(startY+4*sectionSize+3*sectionSize/4));


    }

    public void drawPmanTypes(Graphics2D g2d, int startY, int centreWith) {

      // g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      g2d.setColor(new Color(64,64,64));

      ArrayList<PokemonType> types = player2Pman.types;
      int type1 = -1;
      String type1Name = "";
      int type2 = -1;
      String type2Name = "";
      int lengthOfAllTypes = 0;

      int size = types.size();
      if (size > 0) {
        type1 = types.get(0).typeID;
        type1Name = UtilityFunctions.toCaps(types.get(0).typeName);
        lengthOfAllTypes = g2d.getFontMetrics().stringWidth(type1Name)+10;
        // Extra +10 for drawStringWithColourInBoxes borders
      }
      if (size > 1) {
        type2 = types.get(1).typeID;
        type2Name = UtilityFunctions.toCaps(types.get(1).typeName);
        lengthOfAllTypes = g2d.getFontMetrics().stringWidth(type1Name)+g2d.getFontMetrics().stringWidth(type1Name);
        // Extra +10 for drawStringWithColourInBoxes borders (only need to account
        // type1's start border and type2's end border)
      }

      int startX = centreWith - lengthOfAllTypes/2;

      if (type1 != -1) {
        drawStringWithColourInBoxes(g2d,"",new Color(64,64,64),type1Name,UtilityFunctions.typeToColour(type1), startX, startY);
      }

      if (type2 != -1) {
        // drawStringWithColourInBoxes(g2d,"",new Color(64,64,64),type2Name,UtilityFunctions.typeToColour(type2), startX, startY+g2d.getFontMetrics().getHeight());
        // drawStringWithColourInBoxes(g2d,"",new Color(64,64,64),type2Name,UtilityFunctions.typeToColour(type2), startX+(int)(1.2*g2d.getFontMetrics().stringWidth(type1Name)), startY);
        drawStringWithColourInBoxes(g2d,"",new Color(64,64,64),type2Name,UtilityFunctions.typeToColour(type2), startX+g2d.getFontMetrics().stringWidth(type1Name)+10, startY);
        // drawStringWithColourInBoxes(g2d,"",new Color(64,64,64),type2Name,UtilityFunctions.typeToColour(type2), startX+(int)(1.2*g2d.getFontMetrics().stringWidth("Psychic")), startY); // Nice spacing
      }

      g2d.setColor(new Color(64,64,64));

    }

    // https://stackoverflow.com/questions/12129633/how-do-i-render-wrapped-text-on-an-image-in-java
    public void drawTextWithWrap(String text, FontMetrics textMetrics, int startX, int startY, Graphics2D g2d, int lineLength, int absMaxDepth)
    {

      int originalStartY = startY;

      for (String newLine : text.split("\n")) {

        int lineHeight = textMetrics.getHeight();
        String textToDraw = newLine;
        String[] arr = textToDraw.split(" ");
        int nIndex = 0;
        while ( nIndex < arr.length )
        {
            String line = arr[nIndex++];
            while ( ( nIndex < arr.length ) && (textMetrics.stringWidth(line + " " + arr[nIndex]) < lineLength) )
            {
                line = line + " " + arr[nIndex];
                nIndex++;
            }
            g2d.drawString(line, startX, startY);
            startY = startY + lineHeight;
            if (startY > absMaxDepth) { /*g2d.drawString("...", startX, startY);*/ return; }
        }
      }

    }

    public void drawStringWithColour(Graphics2D g2d, String part1, Color c1, String part2,Color c2, int startX, int startY) {

      FontMetrics textMetrics = g2d.getFontMetrics();
      int part1Length = textMetrics.stringWidth(part1);

      g2d.setColor(c1);
      g2d.drawString(part1,startX,startY);
      g2d.setColor(c2);
      g2d.drawString(part2,startX+part1Length,startY);
      g2d.setColor(c1);

    }

    public void drawStringWithColourInBoxes(Graphics2D g2d, String part1, Color c1, String part2,Color c2, int startX, int startY) {

      FontMetrics textMetrics = g2d.getFontMetrics();
      int part1Length = textMetrics.stringWidth(part1);

      g2d.setColor(c2);
      // Forgive me
      g2d.fillRect(startX+part1Length-3,startY+6,textMetrics.stringWidth(part2)+6,-4-(int)(textMetrics.getHeight()*0.8));

      g2d.setColor(c1);
      g2d.drawString(part1,startX,startY);
      g2d.setColor(new Color(0,0,0));
      g2d.drawString(part2,startX+part1Length+1,startY+1);
      g2d.setColor(new Color(255,255,255));
      g2d.drawString(part2,startX+part1Length,startY);
      g2d.setColor(c1);

    }


    public JButton makeMoveButton(Move move) {

      String name = move.moveName;

      return new JButton(name);

    }

    public void forceRepaint() {

      repaint();

    }

}
