import java.awt.*;
import java.math.*;
import java.util.Random;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

public class TileGame extends JPanel{
  private static int [][] a;
  private static int [] b;
  private static int [] c;
  private static double score = 1;
  private static int sx = 2;
  private static int sy = 2;
  private	static String points = "";
  private static int [][]flag = new int [8][8];
  private static double prescore = 0;
  private static int tens = 0;
  private static int numflagged = 0;
  private static int numzeroes = 0;
  private static int numones = 0;

  public static void main(String[] args) {

    //set up the game board/frame
    JFrame frame = new JFrame("Tile Flipper");
    TileGame tile = new TileGame();
    tile.setBackground(new Color (0,0,0));
    frame.getContentPane().add(tile);
    frame.setSize(1000, 1000);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    if(score == 1) {
      points = score + " Point";
    }
    else {
      points = score + " Points";
    }
    JLabel label = new JLabel(points);
    JLabel info = new JLabel("Press i for info.             ");
    info.setFont(new Font("Comic Sans MS",Font.PLAIN,30));
    label.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
    label.setForeground(new Color(160, 245, 195));
    info.setForeground(new Color(160, 245, 255));


    JPanel panel = new JPanel();

    panel.add(info);
    panel.add(label, BorderLayout.EAST);
    panel.setBackground(new Color(0,0,0));
    frame.add(panel, BorderLayout.PAGE_START);
    frame.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
        {
          changeSelection(frame,-2);
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
        {
          changeSelection(frame,2);
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
        {
          changeSelection(frame,1);
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
        {
          changeSelection(frame,-1);
        }
        if(e.getKeyCode() == KeyEvent.VK_I)
        {
          JOptionPane.showMessageDialog(frame, "       Each tile has a value from 0 to 3 assigned to it at random.  When a tile is flipped over, your score is multiplied by that value.\nIf you flip over a zero, the game ends, and your score is reduced to single digits.  If you find all the tiles that can possibly add to your score, the game will end, and your score is the same.\n                             Use WASD or the arrow keys to move selection, and use ENTER to pick a tile.\n                                                                                        Good Luck!");
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
          //changes score
          prescore = score;
          score *= a[sy-2][sx-2];

          //flags tiles as checked
          if(a[sy-2][sx-2]==0 && flag[sy-2][sx-2] == 0) {
            flag[sy-2][sx-2]=10;
            frame.repaint();
            numflagged++;
          }
          else if(a[sy-2][sx-2]==1 && flag[sy-2][sx-2] == 0) {
            flag[sy-2][sx-2]=11;
            frame.repaint();
            numflagged++;
          }
          else if(a[sy-2][sx-2]==2 && flag[sy-2][sx-2] == 0) {
            flag[sy-2][sx-2]=12;
            frame.repaint();
            numflagged++;
          }
          else if(a[sy-2][sx-2]==3 && flag[sy-2][sx-2] == 0) {
            flag[sy-2][sx-2]=13;
            frame.repaint();
            numflagged++;
          }
          //makes infinite points impossible
          a[sy-2][sx-2] = 1;

          //points display
          String points;
          if(score == 1) {
            points = "1 Point";
          }
          else if(score < 10 && tens==0){
            points = score + " Points";
          }
          else {
            while(score>10) {
              tens++;
              score/=10;
            }
            points = score +"e"+tens+ " Points";

          }
          label.setText("");
          label.setText(points);
          frame.remove(panel);
          label.setFont(new Font("Comic Sans MS",Font.PLAIN,20));
          label.setForeground(new Color(160, 245, 195));
          panel.remove(label);
          panel.add(label, BorderLayout.EAST);
          frame.add(panel, BorderLayout.PAGE_START);
          frame.repaint();

          System.out.println(score);
        }

      }
      @Override
      public void keyTyped(KeyEvent e) {
      }
      @Override
      public void keyReleased(KeyEvent e) {
      }
    });

    //compiles frame
    frame.setLocation(450, 30);
    frame.setVisible(true);
    //tile print
    a = placeTiles();
    b = colSum(a);
    c = rowSum(a);
    for(int i = 0; i<8; i++) {
      for(int i2 = 0; i2<8; i2++){
        System.out.print(a[i][i2] + " ");
      }
      System.out.println();
    }
    for(int i = 0; i<8; i++) {
      System.out.print(b[i] + " ");
    }
    System.out.println();
    for(int i = 0; i<8; i++) {
      System.out.print(c[i] + " ");
    }
    System.out.println();
    //zeros printing
    for(int i = 0; i < 8; i++) {
      System.out.println("Row " + (i+1) + " has " + a[8][i] + " zeroes, and column " + (i+1) + " has " + a[i][8] + " zeroes.");
    }

  }
  public int[] calcCenter(int xPrevious, int yPrevious, int size){
    int [] centers = {xPrevious,size+yPrevious};
    return centers;
  }
  @Override
  public void paintComponent(Graphics gr) {
    //paints the board onto the frame
    super.paintComponent(gr);
    for(int i = 2; i < 10; i++) {
      for(int z = 2; z < 10; z++) {
        Reptangle(gr, i*80, z*80, 50, 50);
        if(c[z-2] == 0 ) {
          draw0(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 1 ) {
          draw1(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 2 ) {
          draw2(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 3 ) {
          draw3(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 4 ) {
          draw4(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 5 ) {
          draw5(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 6 ) {
          draw6(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 7 ) {
          draw7(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 8 ) {
          draw8(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 9 ) {
          draw9(gr, 810, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 10 ) {
          draw1(gr, 810, z*80+16);
          draw0(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 11 ) {
          draw1(gr, 810, z*80+16);
          draw1(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 12 ) {
          draw1(gr, 810, z*80+16);
          draw2(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 13 ) {
          draw1(gr, 810, z*80+16);
          draw3(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 14 ) {
          draw1(gr, 810, z*80+16);
          draw4(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 15 ) {
          draw1(gr, 810, z*80+16);
          draw5(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 16 ) {
          draw1(gr, 810, z*80+16);
          draw6(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 17 ) {
          draw1(gr, 810, z*80+16);
          draw7(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);

        }
        else if(c[z-2] == 18 ) {
          draw1(gr, 810, z*80+16);
          draw8(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 19 ) {
          draw1(gr, 810, z*80+16);
          draw9(gr, 820, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 20 ) {
          draw2(gr, 810, z*80+16);
          draw0(gr, 825, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 21 ) {
          draw2(gr, 810, z*80+16);
          draw1(gr, 830, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 22 ) {
          draw2(gr, 810, z*80+16);
          draw2(gr, 825, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 23 ) {
          draw2(gr, 810, z*80+16);
          draw3(gr, 825, z*80+16);
          drawfslash(gr,835,z*80+16);
        }
        else if(c[z-2] == 24 ) {
          draw2(gr, 810, z*80+16);
          draw4(gr, 825, z*80+16);
          drawfslash(gr,835,z*80+16);
        }

        //zeros
        if(a[z-2][8]==0) {
          draw0(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,0);
        }
        else if(a[z-2][8]==1) {
          draw1(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,1);
        }
        else if(a[z-2][8]==2) {
          draw2(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,2);
        }
        else if(a[z-2][8]==3) {
          draw3(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,3);
        }
        else if(a[z-2][8]==4) {
          draw4(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,4);
        }
        else if(a[z-2][8]==5) {
          draw5(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,5);
        }
        else if(a[z-2][8]==6) {
          draw6(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,6);
        }
        else if(a[z-2][8]==7) {
          draw7(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,7);
        }
        else if(a[z-2][8]==8) {
          draw8(gr,855,z*80+16);
          writezeroes(gr,870,z*80+16,8);
        }
      }
      if(b[i-2] == 0 ) {
        draw0(gr, i*80+10, 810);
      }
      else if(b[i-2] == 1 ) {
        draw1(gr, i*80+16, 810);
      }
      else if(b[i-2] == 2 ) {
        draw2(gr, i*80+16, 810);
      }
      else if(b[i-2] == 3 ) {
        draw3(gr, i*80+16, 810);
      }
      else if(b[i-2] == 4 ) {
        draw4(gr, i*80+16, 810);
      }
      else if(b[i-2] == 5 ) {
        draw5(gr, i*80+16, 810);
      }
      else if(b[i-2] == 6 ) {
        draw6(gr, i*80+16, 810);
      }
      else if(b[i-2] == 7 ) {
        draw7(gr, i*80+16, 810);
      }
      else if(b[i-2] == 8 ) {
        draw8(gr, i*80+16, 810);
      }
      else if(b[i-2] == 9 ) {
        draw9(gr, i*80+16, 810);
      }
      else if(b[i-2] == 10 ) {
        draw1(gr, i*80+16, 810);
        draw0(gr, i*80+26, 810);
      }
      else if(b[i-2] == 11 ) {
        draw1(gr, i*80+16, 810);
        draw1(gr, i*80+26, 810);
      }
      else if(b[i-2] == 12 ) {
        draw1(gr, i*80+16, 810);
        draw2(gr, i*80+26, 810);
      }
      else if(b[i-2] == 13 ) {
        draw1(gr, i*80+16, 810);
        draw3(gr, i*80+26, 810);
      }
      else if(b[i-2] == 14 ) {
        draw1(gr, i*80+16, 810);
        draw4(gr, i*80+26, 810);
      }
      else if(b[i-2] == 15 ) {
        draw1(gr, i*80+16, 810);
        draw5(gr, i*80+26, 810);
      }
      else if(b[i-2] == 16 ) {
        draw1(gr, i*80+16, 810);
        draw6(gr, i*80+26, 810);
      }
      else if(b[i-2] == 17 ) {
        draw1(gr, i*80+16, 810);
        draw7(gr, i*80+26, 810);
      }
      else if(b[i-2] == 18 ) {
        draw1(gr, i*80+16, 810);
        draw8(gr, i*80+26, 810);
      }
      else if(b[i-2] == 19 ) {
        draw1(gr, i*80+16, 810);
        draw9(gr, i*80+26, 810);
      }
      else if(b[i-2] == 20 ) {
        draw2(gr, i*80+16, 810);
        draw0(gr, i*80+26, 810);
      }
      else if(b[i-2] == 21 ) {
        draw2(gr, i*80+16, 810);
        draw1(gr, i*80+31, 810);
      }
      else if(b[i-2] == 22 ) {
        draw2(gr, i*80+16, 810);
        draw2(gr, i*80+26, 810);
      }
      else if(b[i-2] == 23 ) {
        draw2(gr, i*80+16, 810);
        draw3(gr, i*80+26, 810);
      }
      else if(b[i-2] == 24 ) {
        draw2(gr, i*80+16, 810);
        draw4(gr, i*80+26, 810);
      }


      //zeros
      if(a[8][i-2]==0) {
        draw0(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,0);
      }
      else if(a[8][i-2]==1) {
        draw1(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,1);
      }
      else if(a[8][i-2]==2) {
        draw2(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,2);
      }
      else if(a[8][i-2]==3) {
        draw3(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,3);
      }
      else if(a[8][i-2]==4) {
        draw4(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,4);
      }
      else if(a[8][i-2]==5) {
        draw5(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,5);
      }
      else if(a[8][i-2]==6) {
        draw6(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,6);
      }
      else if(a[8][i-2]==7) {
        draw7(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,7);
      }
      else if(a[8][i-2]==8) {
        draw8(gr, i*80+10, 840);
        writezeroes(gr, i*80+25, 840,8);
      }

    }
    gr.setColor(new Color(0,0,0));
    for(int i = 2; i < 10; i++) {
      for(int z = 2; z < 10; z++) {
        if(flag[i-2][z-2]==10) {
          draw0(gr,z*80+20,i*80+15);
        }
        if(flag[i-2][z-2]==11) {
          draw1(gr,z*80+25,i*80+15);
        }
        if(flag[i-2][z-2]==12) {
          draw2(gr,z*80+20,i*80+15);

        }
        if(flag[i-2][z-2]==13) {
          draw3(gr,z*80+20,i*80+15);
        }
      }
    }
    drawHighlighted(gr,sx*80,sy*80,50);
    if(numzeroes+numones+numflagged >=64)
    {
      levelEnd(gr);
    }
    if(score == 0) {
      gameOver(gr);
    }
  }
  public void paintMikesFavoriteRectangle(Graphics gr, int xCenter, int yCenter, int size){
    int [] leftPoint = {xCenter-size,Math.abs(yCenter-size)};
    int [] rightPoint = {xCenter+size,yCenter-size};
    gr.setColor(new Color(255*leftPoint[1]/1000,255*Math.abs((leftPoint[0]-rightPoint[1]))/1000,255*rightPoint[0]/1000));
    gr.drawRect(xCenter, yCenter, size, size);
  }
  public void Reptangle(Graphics gr, int xCenter, int yCenter, int size, int depth) {
    if(depth != 0) {

      Reptangle(gr,xCenter,yCenter,(int)size-1, depth-1);

    }
    paintMikesFavoriteRectangle(gr,xCenter,yCenter,size);
  }
  //add bias towards 1s
  public static int[][] placeTiles() {
    int [][] grid = new int[9][9];
    Random x = new Random();
    int y;
    for(int i = 0; i<8; i++) {
      for(int i2 = 0; i2<8; i2++){
        y = x.nextInt(4);
        if(i%3 == 0 && i2%2 == 1 && y == 3) {
          y = 2;
        }
        else if(i%2 == 0 && i2%3 == 1 && y == 1) {
          y = 0;
        }
        else if(i%4 == 0 && i2%2 == 1 && y == 3) {
          y = 1;
        }
        else if(i%4 == 1 && y == 3) {
          y = 1;
        }
        else if(i%4 == 0 && i2%3 == 1 && y == 2) {
          y = 1;
        }

        grid[i][i2] = y; 
        //zerocounter
        if(y == 0) {
          grid[i][8]++;
          grid[8][i2]++;
          numzeroes++;
        }
        if(y == 1) {
          numones++;
        }
      }
    }
    return grid;
  }
  public static int[] colSum(int [][] a) {
    int x = a.length-1;
    int[] s = new int [x];
    for(int p = 0; p < x; p++) {
      for(int i = 0; i < x; i++) {
        s[p] += a[i][p];
      }
    }
    return s;
  }
  public static int[] rowSum(int [][] a) {
    int x = a[0].length-1;
    int[] s = new int [x];
    for(int p = 0; p < x; p++) {
      for(int i = 0; i < x; i++) {
        s[p] += a[p][i];
      }
    }
    return s;
  }
  public void draw0(Graphics gr, int x, int y) {
    gr.drawOval(x, y, 10, 20);
    gr.drawOval(x, y, 10, 19);
    gr.drawOval(x, y, 9, 19);
    gr.drawOval(x, y, 9, 20);
    gr.drawLine(x, y, x+10, y+20);

  }
  public void draw1(Graphics gr, int x, int y) {
    gr.drawLine(x, y, x, y+20);
    gr.drawLine(x+1, y, x+1, y+20);
    gr.drawLine(x-1, y+2, x-1, y+20);
    gr.drawLine(x-5, y+5, x, y);
    gr.drawLine(x-4, y+4, x, y);
    gr.drawLine(x-3, y+3, x, y);
    gr.drawLine(x-2, y+2, x, y);
    gr.drawLine(x-3, y+19, x+2, y+19);
    gr.drawLine(x-3, y+20, x+2, y+20);

  }
  public void draw2(Graphics gr, int x, int y) {
    gr.drawArc(x, y, 10, 15, 260, 270);
    gr.drawArc(x, y, 9, 15, 260, 270);
    gr.drawArc(x, y, 10, 14, 260, 270);
    gr.drawLine(x, y+1, x, y+5);
    gr.drawArc(x+2, y+15, 6, 5, -270, 180);
    gr.drawArc(x+2, y+15, 5, 4, -270, 180);
    gr.drawLine(x+3, y+20, x+11, y+20);
    gr.drawLine(x+2, y+19, x+11, y+19);

  }
  public void draw3(Graphics gr, int x, int y) {
    gr.drawArc(x, y, 10, 10, 270, 270);
    gr.drawArc(x, y+10, 10, 10, 240, 200);
    gr.drawArc(x, y, 9, 10, 270, 270);
    gr.drawArc(x, y+10, 9, 10, 240, 200);
    gr.drawArc(x, y, 10, 9, 270, 270);
    gr.drawArc(x, y+10, 10, 9, 240, 200);
    gr.drawLine(x, y, x, y+7);
    gr.drawLine(x+1, y, x+1, y+2);
  }
  public void draw4(Graphics gr, int x, int y) {
    gr.drawLine(x+8, y, x+8, y+20);
    gr.drawLine(x+7, y, x+7, y+20);
    gr.drawLine(x+7, y, x, y+12);
    gr.drawLine(x+6, y, x, y+11);
    gr.drawLine(x, y+11, x+9, y+11);
    gr.drawLine(x, y+12, x+10, y+12);


  }
  public void draw5(Graphics gr, int x, int y) {
    gr.drawLine(x, y, x+10, y);
    gr.drawLine(x, y+1, x+10, y+1);
    gr.drawLine(x, y, x, y+10);
    gr.drawArc(x-1, y+10, 9, 10, 225, 270);
    gr.drawArc(x-1, y+10, 10, 9, 225, 270);
  }
  public void draw6(Graphics gr, int x, int y) {
    gr.drawArc(x,y,8,13,45,175);
    gr.drawArc(x,y,7,13,45,175);
    gr.drawLine(x,y+5,x,y+15);
    gr.drawLine(x+1,y+9,x+1,y+14);
    gr.drawLine(x+2,y+10,x+2,y+13);
    gr.drawOval(x, y+10, 10, 10);
    gr.drawOval(x+1, y+10, 9, 10);
    gr.drawOval(x, y+11, 9, 9);
    gr.drawOval(x, y+11, 10, 9);
  }
  public void draw7(Graphics gr, int x, int y) {
    gr.drawLine(x, y, x+10, y);
    gr.drawLine(x, y+1, x+10, y+1);
    gr.drawLine(x, y, x, y+3);
    gr.drawLine(x+1, y, x+1, y+3);
    gr.drawLine(x, y+20, x+10, y);
    gr.drawLine(x, y+20, x+9, y);
    gr.drawLine(x, y+20, x+8, y);


  }
  public void draw8(Graphics gr, int x, int y) {
    gr.drawOval(x, y+10, 10, 10);
    gr.drawOval(x+1, y+10, 9, 10);
    gr.drawOval(x, y+11, 9, 9);
    gr.drawOval(x, y+11, 10, 9);
    gr.drawOval(x, y, 10, 10);
    gr.drawOval(x+1, y, 9, 10);
    gr.drawOval(x, y+1, 9, 9);
    gr.drawOval(x, y+1, 10, 9);
  }
  public void draw9(Graphics gr, int x, int y) {
    gr.drawOval(x, y, 10, 10);
    gr.drawOval(x+1, y, 9, 10);
    gr.drawOval(x, y+1, 9, 9);
    gr.drawOval(x, y+1, 10, 9);
    gr.drawLine(x+10, y+8, x+3, y+20);
    gr.drawLine(x+9, y+8, x+3, y+20);

  }
  public void drawfslash(Graphics gr, int x, int y) {
    gr.drawLine(x+10, y, x+3, y+20);
    gr.drawLine(x+10, y, x+2, y+20);
    gr.drawLine(x+11, y, x+4, y+20);

  }
  public void writezeroes(Graphics gr, int x, int y, int count) {
    gr.drawLine(x, y+5, x+10, y+5);
    gr.drawLine(x, y+20, x+10, y+5);
    gr.drawLine(x, y+20, x+10, y+20);

    gr.drawArc(x+10, y+10, 6, 10, 0, 180);
    gr.drawLine(x+10, y+15, x+16, y+15);
    gr.drawArc(x+10, y+10, 6, 10, 180, 120);

    gr.drawLine(x+20, y+10, x+20, y+20);
    gr.drawArc(x+20, y+10, 5, 10, 15, 130);

    gr.drawOval(x+27, y+10, 6, 10);

    if(count != 1) {
      gr.drawArc(x+35, y+10, 6, 10, 0, 180);
      gr.drawLine(x+35, y+15, x+40, y+15);
      gr.drawArc(x+35, y+10, 6, 10, 180, 120);

      gr.drawArc(x+43, y+10, 6, 7, 15, 155);
      gr.drawLine(x+43, y+13, x+47, y+14);
      gr.drawArc(x+43, y+14, 6, 8, 185, 260);

    }


  }
  public void drawHighlighted(Graphics gr, int xCenter, int yCenter, int size) {
    int [] leftPoint = {xCenter-size,yCenter-size};
    int [] rightPoint = {xCenter+size,yCenter-size};
    gr.setColor(new Color(255*Math.abs((leftPoint[0]-rightPoint[1]))/1000,140,255*rightPoint[0]/1000));

    gr.drawRect(xCenter-2, yCenter-2, size+6, size+6);
    gr.drawRect(xCenter-2, yCenter-2, size+7, size+7);
    gr.drawRect(xCenter-2, yCenter-2, size+8, size+8);
    gr.drawRect(xCenter-2, yCenter-2, size+9, size+9);
    gr.drawRect(xCenter-2, yCenter-2, size+10, size+10);


  }
  public static void changeSelection(JFrame frame, int dir) {
    if(dir == 1&&sx<9) {//right
      sx+=1;
    }
    if(dir == 2&&sy<9) {//up
      sy+=1;
    }
    if(dir == -1&&sx>2) {//left
      sx-=1;
    }
    if(dir == -2&&sy>2) {//down
      sy-=1;
    }
    frame.repaint();
  }
  public void gameOver(Graphics gr) {

    //print GAME OVER across screen in red
    gr.setColor(new Color(0,0,0));
    gr.fillRect(0, 0, 1000,1000);
    gr.setColor(new Color(255,0,0));

    //G
    gr.drawArc(80, 210, 40, 50, 70, 270);
    gr.drawArc(80, 210, 39, 50, 70, 270);
    gr.drawArc(80, 210, 39, 49, 70, 270);
    gr.drawArc(80, 210, 40, 49, 70, 270);
    gr.drawLine(100,240,120,240);
    gr.drawLine(100,241,120,241);
    gr.drawLine(100,242,120,242);

    //A
    gr.drawLine(130, 260, 140, 210);
    gr.drawLine(150, 260, 140, 210);
    gr.drawLine(131, 260, 140, 210);
    gr.drawLine(151, 260, 140, 210);
    gr.drawLine(131, 260, 141, 210);
    gr.drawLine(151, 260, 141, 210);
    gr.drawLine(136, 230, 144, 230);
    gr.drawLine(135, 231, 145, 231);


    //M
    gr.drawLine(160, 260, 170, 210);
    gr.drawLine(180, 260, 170, 210);
    gr.drawLine(161, 260, 171, 210);
    gr.drawLine(181, 260, 171, 210);
    gr.drawLine(161, 260, 170, 210);
    gr.drawLine(181, 260, 170, 210);

    gr.drawLine(180, 260, 190, 210);
    gr.drawLine(200, 260, 190, 210);
    gr.drawLine(181, 260, 191, 210);
    gr.drawLine(201, 260, 191, 210);
    gr.drawLine(181, 260, 190, 210);
    gr.drawLine(201, 260, 190, 210);

    //E
    gr.drawLine(219, 210, 230, 210);
    gr.drawLine(219, 225, 230, 225);
    gr.drawLine(219, 259, 230, 259);
    gr.drawLine(219, 260, 230, 260);
    gr.drawLine(216, 210, 216, 260);
    //	gr.drawLine(217, 210, 217, 260);
    gr.drawLine(220, 210, 220, 260);
    gr.drawLine(219, 210, 219, 260);

    //O
    gr.drawOval(290, 203, 50, 60);
    gr.drawOval(290, 203, 49, 60);
    gr.drawOval(290, 203, 50, 59);
    gr.drawOval(290, 203, 49, 59);
    gr.drawOval(291, 203, 50, 60);
    gr.drawOval(291, 203, 49, 60);
    gr.drawOval(291, 203, 50, 59);
    gr.drawOval(291, 203, 49, 59);

    //V
    gr.drawLine(345, 210, 355, 260);
    gr.drawLine(365, 210, 355, 260);
    gr.drawLine(346, 210, 356, 260);
    gr.drawLine(366, 210, 356, 260);
    gr.drawLine(346, 210, 355, 260);
    gr.drawLine(366, 210, 355, 260);

    //E
    gr.drawLine(384, 210, 395, 210);
    gr.drawLine(384, 225, 395, 225);
    gr.drawLine(384, 259, 395, 259);
    gr.drawLine(384, 260, 395, 260);
    gr.drawLine(381, 210, 381, 260);
    //	gr.drawLine(382, 210, 382, 260);
    gr.drawLine(385, 210, 385, 260);
    gr.drawLine(384, 210, 384, 260);

    //R
    gr.drawArc(408, 210, 19, 20, 270, 180);
    gr.drawArc(408, 210, 18, 19, 270, 180);
    gr.drawArc(408, 210, 18, 20, 270, 180);
    gr.drawArc(408, 210, 19, 19, 270, 180);
    gr.drawLine(410, 210, 410, 260);
    gr.drawLine(414, 210, 414, 260);
    gr.drawLine(415, 210, 415, 260);
    gr.drawLine(416, 210, 416, 260);
    gr.drawLine(415, 230, 440, 260);
    gr.drawLine(416, 231, 440, 260);
    gr.drawLine(416, 232, 440, 260);
    gr.drawLine(416, 233, 440, 260);
    gr.drawLine(416, 234, 440, 260);


    //:(
    gr.setColor(new Color(255,0,0));
    gr.fillOval(430, 280, 80, 100);
    gr.fillOval(530, 280, 80, 100);
    gr.fillOval(420, 410, 210, 60);

    gr.setColor(new Color(0,0,0));
    gr.fillOval(430, 250, 80, 100);
    gr.fillOval(530, 250, 80, 100);
    gr.fillOval(420, 430, 240, 60);



    //last score display
    System.out.println((int)prescore%10);
    System.out.println(Math.rint(10*(prescore%1)));
    gr.setColor(new Color(255,0,0));
    //f
    gr.drawArc(575, 620, 5, 7, 0, 180);
    gr.drawLine(575, 625, 575, 632);
    gr.drawLine(573, 628, 578, 628);
    //i
    gr.fillOval(582, 623, 2, 2);
    gr.drawLine(583, 626, 583, 632);
    gr.drawLine(581, 632, 584, 632);
    //n
    gr.drawArc(588, 626, 5, 9, 0, 135);
    gr.drawLine(588, 625, 588, 632);
    gr.drawLine(593, 629, 593, 632);
    //a
    gr.drawOval(597, 626, 5, 6);
    gr.setColor(new Color(0,0,0));
    gr.drawLine(597, 626, 607, 626);
    gr.setColor(new Color(255,0,0));
    gr.drawLine(598, 627, 601, 627);
    gr.drawLine(602, 626, 602, 632);
    //l
    gr.drawLine(606, 620, 606, 632);

    //s
    gr.drawArc(618, 620, 6, 7, 15, 155);
    gr.drawLine(618, 623, 622, 624);
    gr.drawArc(618, 624, 6, 8, 185, 260);
    //c
    gr.drawArc(628, 626, 6, 6, 45, 270);
    //o
    gr.drawOval(637, 626, 6, 6);
    //r
    gr.drawArc(647, 626, 5, 9, 15, 120);
    gr.drawLine(647, 625, 647, 632);
    //e
    gr.drawArc(656, 626, 6, 6, 0, 180);
    gr.drawLine(656, 629, 662, 629);
    gr.drawArc(656, 626, 6, 7, 180, 120);
    //:
    gr.fillOval(665, 627, 3, 3);
    gr.fillOval(665, 631, 3, 3);

    //numbers
    if(((int)prescore%10) == 0) {
      draw0(gr,580,660);
    }
    if(((int)prescore%10) == 1) {
      draw1(gr,580,660);
    }
    if(((int)prescore%10) == 2) {
      draw2(gr,580,660);
    }
    if(((int)prescore%10) == 3) {
      draw3(gr,580,660);
    }
    if(((int)prescore%10) == 4) {
      draw4(gr,580,660);
    }
    if(((int)prescore%10) == 5) {
      draw5(gr,580,660);
    }
    if(((int)prescore%10) == 6) {
      draw6(gr,580,660);
    }
    if(((int)prescore%10) == 7) {
      draw7(gr,580,660);
    }
    if(((int)prescore%10) == 8) {
      draw8(gr,580,660);
    }
    if(((int)prescore%10) == 9) {
      draw9(gr,580,660);
    }

    gr.fillOval(593, 678, 5, 5);

    if(((int)Math.rint(10*(prescore%1))) == 0) {
      draw0(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 1) {
      draw1(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 2) {
      draw2(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 3) {
      draw3(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 4) {
      draw4(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 5) {
      draw5(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 6) {
      draw6(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 7) {
      draw7(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 8) {
      draw8(gr,603,660);
    }
    if(((int)Math.rint(10*(prescore%1))) == 9) {
      draw9(gr,603,660);
    }

  }
  public void levelEnd(Graphics gr) {
    //Print "you won" in teal
    Reptangle(gr,0,0,1000,720);
    Reptangle(gr,0,0,280,280);
    gr.setColor(new Color(255,255,255));
    gr.setColor(new Color(0,255,255));
    //Y
    gr.drawLine(435, 150, 445, 180);
    gr.drawLine(455, 150, 445, 180);
    gr.drawLine(436, 150, 446, 180);
    gr.drawLine(456, 150, 446, 180);
    gr.drawLine(436, 150, 445, 180);
    gr.drawLine(456, 150, 445, 180);
    gr.drawLine(445, 180, 445, 200);
    //O
    gr.drawOval(458, 150, 30, 50);
    gr.drawOval(457, 150, 30, 49);
    gr.drawOval(458, 150, 29, 50);
    //U
    gr.drawArc(498, 160, 30, 40, 0, -180);
    gr.drawLine(498, 180, 498, 150);
    gr.drawLine(528, 180, 528, 150);

    //W
    gr.drawLine(445, 210, 455, 260);
    gr.drawLine(465, 210, 455, 260);
    gr.drawLine(446, 210, 456, 260);
    gr.drawLine(466, 210, 456, 260);
    gr.drawLine(446, 210, 455, 260);
    gr.drawLine(466, 210, 455, 260);
    gr.drawLine(466, 210, 476, 260);
    gr.drawLine(486, 210, 476, 260);
    gr.drawLine(467, 210, 477, 260);
    gr.drawLine(487, 210, 477, 260);
    gr.drawLine(467, 210, 476, 260);
    gr.drawLine(487, 210, 476, 260);
    //O
    gr.drawOval(490, 210, 30, 50);
    gr.drawOval(489, 210, 30, 49);
    gr.drawOval(490, 210, 29, 50);
    //N
    gr.drawLine(533, 210, 533, 260);
    gr.drawLine(534, 211, 534, 260);
    gr.drawLine(535, 213, 535, 260);
    gr.drawLine(533, 210, 553, 260);
    gr.drawLine(533, 209, 553, 259);
    gr.drawLine(553, 210, 553, 260);
    gr.drawLine(554, 210, 554, 260);


    sx=-6;
    sy=-6;


    //s
    gr.drawArc(518, 620, 6, 7, 15, 155);
    gr.drawLine(518, 623, 522, 624);
    gr.drawArc(518, 624, 6, 8, 185, 260);
    //c
    gr.drawArc(528, 626, 6, 6, 45, 270);
    //o
    gr.drawOval(537, 626, 6, 6);
    //r
    gr.drawArc(547, 626, 5, 9, 15, 120);
    gr.drawLine(547, 625, 547, 632);
    //e
    gr.drawArc(556, 626, 6, 6, 0, 180);
    gr.drawLine(556, 629, 562, 629);
    gr.drawArc(556, 626, 6, 7, 180, 120);
    //:
    gr.fillOval(565, 627, 3, 3);
    gr.fillOval(565, 631, 3, 3);

    //numbers
    if(((int)score%10) == 0) {
      //draw0(gr,530,650);
    }
    if(((int)score%10) == 1) {
      draw1(gr,530,650);
    }
    if(((int)score%10) == 2) {
      draw2(gr,530,650);
    }
    if(((int)score%10) == 3) {
      draw3(gr,530,650);
    }
    if(((int)score%10) == 4) {
      draw4(gr,530,650);
    }
    if(((int)score%10) == 5) {
      draw5(gr,530,650);
    }
    if(((int)score%10) == 6) {
      draw6(gr,530,650);
    }
    if(((int)score%10) == 7) {
      draw7(gr,530,650);
    }
    if(((int)score%10) == 8) {
      draw8(gr,530,650);
    }
    if(((int)score%10) == 9) {
      draw9(gr,530,650);
    }

    //gr.fillOval(543, 668, 5, 5);

    if(((int)Math.rint(10*(score%1))) == 0) {
      draw0(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 1) {
      draw1(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 2) {
      draw2(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 3) {
      draw3(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 4) {
      draw4(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 5) {
      draw5(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 6) {
      draw6(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 7) {
      draw7(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 8) {
      draw8(gr,548,650);
    }
    if(((int)Math.rint(10*(score%1))) == 9) {
      draw9(gr,548,650);
    }
    if(tens > 0) {
      /*gr.drawArc(556, 650, 6, 6, 0, 180);
      gr.drawLine(556, 650, 562, 650);
      gr.drawArc(556, 650, 6, 7, 180, 120);*/
      for(int i = 1; i< tens; i++) {
        draw0(gr,548+(13*i),650);
      }
    }

  }
}
