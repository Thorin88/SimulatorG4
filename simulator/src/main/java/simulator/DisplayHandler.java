
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

public class DisplayHandler extends JPanel/* implements ActionListener*/ {

  Timer timer;

  int frameLength;
  int frameWidth;

  GameState currentGS;
  String toDisplay = "";
  int charPointer = 0;

  Pokemon player1Pman;
  int p1FormID;
  Pokemon player2Pman;
  int p2FormID;

  int p1StatusID = 0;
  int p2StatusID = 0;

  // TODO: Load this only once in SV3
  ImageIcon player1PmanImgIcon;
  Image player1PmanImg;

  ImageIcon player2PmanImgIcon;
  Image player2PmanImg;

  // Could be a constant
  static String filePathToBaseApi = "./../";

  // Could include "Choice" object parameter to be filled in.
  // Make this value global in this class and edit it in ActionListener
  public DisplayHandler(int fL, int fW, GameState gameState) {

    // super();

    super.setDoubleBuffered(true);
    super.setLayout(null);

    // this.timer = new Timer(7,this);
    // timer.start();

    this.frameLength = fL;
    this.frameWidth = fW;

    this.currentGS = gameState;
    // // Added for extra safety around threads
    // this.currentGS = gameState.deepCopy();

    this.player1Pman = currentGS.player1CurrentPokemon;
    this.p1FormID = player1Pman.form.formID;
    this.player2Pman = currentGS.player2CurrentPokemon;
    this.p2FormID = player2Pman.form.formID;

    this.p1StatusID = getStatus(player1Pman);
    this.p2StatusID = getStatus(player2Pman);

    // Could update to use buffered image with ImageIO
    this.player1PmanImgIcon = new ImageIcon(filePathToBaseApi + "sprites/sprites/pokemon/" + p1FormID + ".png");
    this.player1PmanImg = player1PmanImgIcon.getImage();
    this.player1PmanImg = (new ImageIcon(player1PmanImg.getScaledInstance(frameLength/6, -1, Image.SCALE_SMOOTH))).getImage();

    this.player2PmanImgIcon = new ImageIcon(filePathToBaseApi + "sprites/sprites/pokemon/back/" + p2FormID + ".png");
    this.player2PmanImg = player2PmanImgIcon.getImage();
    this.player2PmanImg = (new ImageIcon(player2PmanImg.getScaledInstance(frameLength/5, -1, Image.SCALE_SMOOTH))).getImage();

  }

  public DisplayHandler(int fL, int fW, GameState gameState, String message) {

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

    this.player1Pman = currentGS.player1CurrentPokemon;
    this.p1FormID = player1Pman.form.formID;
    this.player2Pman = currentGS.player2CurrentPokemon;
    this.p2FormID = player2Pman.form.formID;

    this.p1StatusID = getStatus(player1Pman);
    this.p2StatusID = getStatus(player2Pman);

    // Could update to use buffered image with ImageIO
    this.player1PmanImgIcon = new ImageIcon(filePathToBaseApi + "sprites/sprites/pokemon/" + p1FormID + ".png");
    this.player1PmanImg = player1PmanImgIcon.getImage();
    this.player1PmanImg = (new ImageIcon(player1PmanImg.getScaledInstance(frameLength/6, -1, Image.SCALE_SMOOTH))).getImage();

    this.player2PmanImgIcon = new ImageIcon(filePathToBaseApi + "sprites/sprites/pokemon/back/" + p2FormID + ".png");
    this.player2PmanImg = player2PmanImgIcon.getImage();
    this.player2PmanImg = (new ImageIcon(player2PmanImg.getScaledInstance(frameLength/5, -1, Image.SCALE_SMOOTH))).getImage();

    // DisplayHandler dodgy = this;
    //
    // Timer timer = new Timer(50, null);
    // timer.addActionListener(new ActionListener() {
    //           @Override
    //           public void actionPerformed(ActionEvent e) {
    //               if (currentGS.player1CurrentPokemon.currentHealth <= 0) {
    //                 timer.stop();
    //                 JButton moveB = new JButton("Test");
    //                 moveB.setBounds(100,100,100,100);
    //                 dodgy.add(moveB);
    //                 repaint();
    //                 parent.updateGameState(currentGS.deepCopy(), true, false, false);
    //                 return;
    //               }
    //               currentGS.player1CurrentPokemon.currentHealth -= 1;
    //               repaint();
    //           }
    //       });
    // timer.start();

    int scrollSpeed = 20;

    Timer timer = new Timer(scrollSpeed, null);
    timer.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {

                if (charPointer < toDisplay.length()) {
                  charPointer++;
                }
                else {
                  timer.stop();
                  return;
                }
                repaint();
              }
          });
    // timer.addKeyListener(new KeyListener() {
    //           @Override
    //           public void keyPressed(KeyEvent e) {
    //             if (e.getKeyCode()== KeyEvent.VK_RIGHT) {
    //               timer.setDelay(scrollSpeed*2);
    //             }
    //           }
    //           @Override
    //           public void keyReleased(KeyEvent e) {
    //             if (e.getKeyCode()== KeyEvent.VK_RIGHT) {
    //               timer.setDelay(scrollSpeed);
    //             }
    //           }
    //           @Override
    //           public void keyTyped(KeyEvent e) {
    //             // System.out.println("keyTyped");
    //           }
    //       });
    timer.start();

  }

  // private class MyTimerListener implements ActionListener {
  //
  //   public MyTimerListener() {
  //
  //   }
  //
  //   @Override
  //   public void actionPerformed(ActionEvent e) {
  //
  //     if (charPointer < toDisplay.length()) {
  //       charPointer++;
  //     }
  //     else {
  //       timer.stop();
  //       return;
  //     }
  //
  //     repaint();
  //
  //   }
  //
  // }

    // JPanel panel = new JPanel();

    @Override
    protected void paintComponent(Graphics g) {

      // System.out.println("******************** Being repainted ****************************");

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

      // super will be the frame the panel is mouhnted in.
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D) g;

      // Background

      int backgroundLength = upperOvalLength+upperOvalTopLeft_X+frameLength/80;
      int backgroundWidth = frameWidth;

      // g2d.setColor(new Color(80,176,16));
      // g2d.setStroke(new BasicStroke(5));
      // g2d.fillRect(0,0,backgroundLength,frameWidth);

      g2d.setStroke(new BasicStroke(5));
      g2d.setColor(new Color(215,232,240));
      g2d.fillRect(0,0,backgroundLength,backgroundWidth);

      g2d.setColor(new Color(186,215,228));
      g2d.fillRect(0,0,backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.05*backgroundWidth),backgroundLength,upperOvalWidth/15);
      g2d.fillRect(0,(int)(0.08*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.1*backgroundWidth),backgroundLength,upperOvalWidth/15);
      g2d.fillRect(0,(int)(0.12*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.15*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.18*backgroundWidth),backgroundLength,upperOvalWidth/15);
      g2d.fillRect(0,(int)(0.20*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.25*backgroundWidth),backgroundLength,upperOvalWidth/40);
      g2d.fillRect(0,(int)(0.3*backgroundWidth),backgroundLength,upperOvalWidth/30);
      g2d.fillRect(0,(int)(0.4*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.45*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.55*backgroundWidth),backgroundLength,upperOvalWidth/15);
      g2d.fillRect(0,(int)(0.60*backgroundWidth),backgroundLength,upperOvalWidth/20);
      g2d.fillRect(0,(int)(0.65*backgroundWidth),backgroundLength,upperOvalWidth/15);
      // g2d.fillRect(0,(int)(0.70*backgroundWidth),backgroundLength,upperOvalWidth/30);
      // g2d.setColor(new Color(64,128,8));
      // g2d.fillRect(0,(int)(0.97*backgroundWidth),backgroundLength,upperOvalWidth/10);

      // RHS
      // g2d.setColor(new Color(231,226,171));
      g2d.setColor(new Color(186,215,228));
      g2d.fillRect(backgroundLength,0,frameLength,frameWidth);

      // Middle border
      // g2d.fillRect(backgroundLength,0,frameLength/160,frameWidth);

      // Used to be draw
      g2d.setStroke(new BasicStroke(15));
      // g2d.setColor(new Color(26, 84, 23));
      g2d.setColor(new Color(124, 210, 117));
      g2d.drawOval(upperOvalTopLeft_X,upperOvalTopLeft_Y,upperOvalLength,upperOvalWidth);
      g2d.drawOval(lowerOvalTopLeft_X,lowerOvalTopLeft_Y,lowerOvalLength,lowerOvalWidth);
      g2d.setColor(new Color(146, 226, 97));
      g2d.fillOval(upperOvalTopLeft_X,upperOvalTopLeft_Y,upperOvalLength,upperOvalWidth);
      g2d.fillOval(lowerOvalTopLeft_X,lowerOvalTopLeft_Y,lowerOvalLength,lowerOvalWidth);

      // Could do graphics 2d stuff
      g.drawImage(player1PmanImg, upperOvalCentreX - (player1PmanImg.getWidth(null))/2, upperOvalCentreY - (player1PmanImg.getHeight(null))/2 - (player1PmanImg.getHeight(null))/4, this);
      g.drawImage(player2PmanImg, lowerOvalCentreX  - (player2PmanImg.getWidth(null))/2, lowerOvalCentreY - (player2PmanImg.getHeight(null))/2 - (player2PmanImg.getHeight(null))/4, this);

      // if (player1Pman.currentHealth > 0) {
      //   g.drawImage(player1PmanImg, upperOvalCentreX - (player1PmanImg.getWidth(null))/2, upperOvalCentreY - (player1PmanImg.getHeight(null))/2 - (player1PmanImg.getHeight(null))/4, this);
      // }
      // if (player2Pman.currentHealth > 0) {
      //   g.drawImage(player2PmanImg, lowerOvalCentreX  - (player2PmanImg.getWidth(null))/2, lowerOvalCentreY - (player2PmanImg.getHeight(null))/2 - (player2PmanImg.getHeight(null))/4, this);
      // }

      // Could do inner rectangles
      g2d.setStroke(new BasicStroke(5));
      // g2d.setColor(new Color(0, 0, 0));
      int textBoxStartX = lowerOvalTopLeft_X;
      int textBoxEndX = (upperOvalTopLeft_X+upperOvalLength)-lowerOvalTopLeft_X;
      int textBoxStartY = lowerOvalTopLeft_Y+lowerOvalWidth+frameWidth/40;
      int textBoxLength = textBoxEndX;
      int textBoxWidth = lowerOvalWidth;
      // g2d.setColor(new Color(255, 0, 0));
      // g2d.fillRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);
      // g2d.setColor(new Color(255, 255, 255));
      g2d.setColor(new Color(101, 155, 155));
      g2d.fillRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);
      g2d.setColor(new Color(201, 70, 54));
      g2d.drawRect(textBoxStartX,textBoxStartY,textBoxEndX,lowerOvalWidth);

      g2d.setStroke(new BasicStroke(1));

      int opponentHP = currentGS.player1CurrentPokemon.currentHealth;
      int opponentMaxHP = currentGS.player1CurrentPokemon.stats.get(0).getStatValue();
      int userHP = currentGS.player2CurrentPokemon.currentHealth;
      int userMaxHP = currentGS.player2CurrentPokemon.stats.get(0).getStatValue();

      // Make a method to spam repaint until hp in the current place;

      // g2d.setStroke(new BasicStroke(5));
      // g2d.setColor(new Color(0, 0, 0));
      int startOfP2BoxX = lowerOvalTopLeft_X + lowerOvalLength + lowerOvalLength/20;
      int startOfP2BoxY = lowerOvalTopLeft_Y - lowerOvalWidth/4;
      int lengthOfP2Box = textBoxEndX + textBoxStartX - startOfP2BoxX;
      // int widthOfP2Box = lowerOvalWidth;
      int widthOfP2Box = (int)(lowerOvalWidth*0.7);
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

      // Player 1's (Make these methods eventually?)
      int lengthOfP1Box = lengthOfP2Box;
      int widthOfP1Box = widthOfP2Box;
      int startOfP1BoxX = upperOvalTopLeft_X - lengthOfP1Box - upperOvalLength/20;
      int startOfP1BoxY = upperOvalTopLeft_Y - upperOvalWidth/2;
      g2d.setColor(new Color(128,128,128));
      g2d.setStroke(new BasicStroke(5));
      g2d.fillRect(startOfP1BoxX,startOfP1BoxY,lengthOfP1Box,widthOfP1Box);
      g2d.setColor(new Color(0,0,0));
      g2d.drawRect(startOfP1BoxX,startOfP1BoxY,lengthOfP1Box,widthOfP1Box);

      int p1HPStartX = startOfP1BoxX + 6*(lengthOfP1Box/20);
      int p1HPStartY = startOfP1BoxY + 6*(widthOfP1Box/10);
      int p1HPLength = 6*(lengthOfP1Box/10);
      // int p1HPWidth = 1*(widthOfP1Box/20);
      int p1HPWidth = (int) (1.5*(widthOfP1Box/20));

      g2d.setColor(new Color(0,0,0));
      g2d.fillRect(p1HPStartX,p1HPStartY,p1HPLength,p1HPWidth);

      hpRatio = (float) opponentHP / opponentMaxHP;

      // Fill in health bar
      if (opponentHP > 0) {

        int p1HPToDraw = (int) (hpRatio * p1HPLength);
        // System.out.println(p1HPToDraw);

        if (p1HPToDraw <= 0) { p1HPToDraw = 1; }

        if (hpRatio > 0.5) { g2d.setColor(new Color(4,150,8)); }
        else if (hpRatio <= 0.5 && hpRatio > 0.2) { g2d.setColor(new Color(235,150,0)); }
        else { g2d.setColor(new Color(247,65,1)); }

        g2d.fillRect(p1HPStartX,p1HPStartY,p1HPToDraw,p1HPWidth);

      }

      g2d.setColor(new Color(255,255,255));
      g2d.setStroke(new BasicStroke(2));
      g2d.drawRect(p1HPStartX,p1HPStartY,p1HPLength,p1HPWidth);

      String p1Name = this.player1Pman.name;
      level = this.player1Pman.level;

      g2d.setColor(new Color(0,0,0));
      // g2d.setFont(new Font("SansSerif", Font.BOLD, 20));
      g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      // Was *0.8
      g2d.drawString("Lv" + level,p1HPStartX + (int) ( 0.7 * p1HPLength)+1,1+p1HPStartY - (int) (2 * (p1HPWidth)) );
      g2d.setColor(new Color(255,255,255));
      g2d.drawString("Lv" + level,p1HPStartX + (int) ( 0.7 * p1HPLength),p1HPStartY - (int) (2 * (p1HPWidth)) );

      drawStatus(p1StatusID,g2d,p1HPStartX-p1HPWidth*4,p1HPStartY+p1HPWidth/2,3*p1HPWidth);

      g2d.setColor(new Color(0,0,0));
      // g2d.setFont(new Font("SansSerif", Font.PLAIN, 25));
      g2d.drawString(p1Name,p1HPStartX+1,1+p1HPStartY - (int) (2 * (p1HPWidth)) );
      g2d.setColor(new Color(255,255,255));
      g2d.drawString(p1Name,p1HPStartX,p1HPStartY - (int) (2 * (p1HPWidth)) );

      // int p1NameOffsetX = (int)(0.05*lengthOfP1Box);
      // int p1NameOffsetY = (int)(0.4*widthOfP1Box);
      //
      // g2d.setColor(new Color(0,0,0));
      // g2d.setFont(new Font("SansSerif", Font.PLAIN, 25));
      // // g2d.drawString(p1Name,startOfP1BoxX++1,1+p1HPStartY - (int) (2 * (p1HPWidth)) );
      // g2d.drawString(p1Name,startOfP1BoxX+p1NameOffsetX+1,1+p1HPStartY - p1NameOffsetY );
      // g2d.setColor(new Color(255,255,255));
      // g2d.drawString(p1Name,startOfP1BoxX+p1NameOffsetX,p1HPStartY - (int) (2 * (p1HPWidth)) );

      // System.out.println(opponentHP);
      // System.out.println(userHP);

      // System.out.println("M: " + this.toDisplay);
      //
      // if (!this.toDisplay.equals("")) {
      //
      //   System.out.println("M: " + this.toDisplay);
      //
      //   g2d.setColor(new Color(255,255,255));
      //   g2d.setFont(new Font("SansSerif", Font.BOLD, 17));
      //   g2d.drawString(this.toDisplay,100,100);
      // }

        // AnimateMessage

        // Match the textBoxVars
        int messageX = lowerOvalTopLeft_X;
        int messageY = lowerOvalTopLeft_Y+lowerOvalWidth+frameWidth/40;

        int xOffSet = textBoxLength/40;
        int yOffSet = textBoxWidth/16;

        // g2d.setColor(new Color(0,0,0));
        g2d.setColor(new Color(255,255,255));
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 20));
        newLineDrawString(g2d,this.toDisplay.substring(0,charPointer),textBoxStartX+xOffSet,textBoxStartY+yOffSet);

        // System.out.println(g2d.getFontMetrics().stringWidth(this.toDisplay));

        // newLineDrawString(g2d,this.toDisplay.substring(0,charPointer),textBoxStartX+xOffSet,textBoxStartY+yOffSet);

      // g2d.setColor(new Color(0,0,0));
      // g.setFont(new Font("SansSerif", Font.BOLD, 15));
      // g.drawString("HP",p1HPStartX - (lengthOfP1Box/10),p1HPStartY + (int) (1.5 * (p1HPWidth)) );

      // Move buttons
        // Move move1 = this.player2Pman.moves.get(0);
        // JButton move1B = this.makeMoveButton(move1);
        // move1B.setBounds(0,0,100,100);
        //
        // super.add(move1B);

      // g.drawImage(player1PmanImg, frameLength / 2, frameWidth / 2, this);
      // g.drawImage(player2PmanImg, frameLength / 2, frameWidth / 2 + 10, this);

      // g.drawImage(player1PmanImg.getScaledInstance(frameLength/4, -1, Image.SCALE_SMOOTH), frameLength / 2, frameWidth / 2, this);
      // g.drawImage(player2PmanImg.getScaledInstance(10, -1, Image.SCALE_SMOOTH), frameLength / 2, frameWidth / 2 + 10, this);

      // g.drawImage(newP1Img, frameLength / 2, frameWidth / 2, this);
      // g.drawImage(newP2Img, frameLength / 2, frameWidth / 2 + 10, this);

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

    public JButton makeMoveButton(Move move) {

      String name = move.moveName;

      return new JButton(name);

    }

    public void forceRepaint() {

      repaint();

    }

}
