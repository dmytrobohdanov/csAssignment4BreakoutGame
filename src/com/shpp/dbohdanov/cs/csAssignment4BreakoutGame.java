package com.shpp.dbohdanov.cs;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;


import java.awt.*;
import java.awt.event.MouseEvent;

public class csAssignment4BreakoutGame extends WindowProgram {
    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 40;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;

    //----------------------------------------------------------------------------------------
    // Variables:
    private int howManyBricksOntTheScreenNow; //current number of bricks on the screen
    private Color[] brickColor={Color.RED, Color.RED, Color.ORANGE, Color.ORANGE,Color.YELLOW, Color.YELLOW,
                                    Color.GREEN, Color.GREEN, Color.CYAN,Color.CYAN};

    private GRect paddle = new GRect(0, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT); // The Paddle

    private GOval ball; // The Ball
    private double dy, dx; //ball "speed"
    private double pause_time = 10; //actually speed of the ball, less=faster
    private boolean isFailed=false, win=false;
    private String failString;
    private GLabel failedLabel, scoreLabel;
    private int currentTurn;

    //----------------------------------------------------------------------------------------



    public void run() {
        drawField();
        addMouseListeners();
        currentTurn=NTURNS;

        while(currentTurn>0){
            waitForClick();
            drawBall();
            while (!isFailed) {
                ballMove();
                if(howManyBricksOntTheScreenNow<=0) {
                    youWin();
                    break;
                }
            }
            currentTurn--;
            if(howManyBricksOntTheScreenNow<=0) {
               youWin();
                break;
            }
            if(isFailed)
                prepeareForNextTurn();
        }
        if(!win)
            youLose();

    }

    private void youWin() {
        removeAll();
        GLabel youWin = new GLabel("YOU WIN! :)",getWidth()/2, getHeight()/2);
        youWin.setFont("Arial-100");
        add(youWin);
    }

    private void youLose() {
        removeAll();
        GLabel youLose = new GLabel("Looooose",getWidth()/2, getHeight()/2);
        youLose.setFont("Arial-40");
        add(youLose);
    }

    private void prepeareForNextTurn() {
        isFailed=false;
       // showTurnsLeft();

        waitForClick();
        remove(failedLabel);
    }

    private void drawField() {
        drawPaddle();
        drawBricks();
        showScore();
        //showTurnsLeft();
    }

    private void drawPaddle() {
        paddle.setFilled(true);
        paddle.setFillColor(Color.BLACK);
        add(paddle);
    }

    private void drawBall() {
        ball=new GOval(WIDTH / 2, HEIGHT / 2, BALL_RADIUS * 2, BALL_RADIUS * 2);
        ball.setFilled(true);
        ball.setFillColor(Color.BLACK);
        add(ball);
        dy = 3;
        RandomGenerator rgen = RandomGenerator.getInstance();
        dx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5))
            dx = -dx;
    }


    private void showScore() {
        if(scoreLabel!=null)
            remove(scoreLabel);
        String score="Score: "+String.valueOf(changeScore());
        scoreLabel=new GLabel(score, 20, getHeight());
        scoreLabel.setFont("Arial-20");
        add(scoreLabel);
    }

    private int changeScore() {
        return NBRICKS_PER_ROW*NBRICK_ROWS-howManyBricksOntTheScreenNow;
    }

    private void drawBricks() {
        double x=0, y=BRICK_Y_OFFSET;
        Color color;
        for (int i=0; i<NBRICK_ROWS; i++) {
            color=brickColor[i];
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
                addOneBrick(x, y, color);
                x = x+ BRICK_WIDTH+BRICK_SEP;
            }

            y=BRICK_HEIGHT*(i+1)+BRICK_SEP*(i+1)+BRICK_Y_OFFSET;
            x=0;
        }
    }

    private void addOneBrick(double x, double y, Color color) {
        GRect brick=new GRect(x,y, BRICK_WIDTH, BRICK_HEIGHT);
        brick.setFilled(true);
        brick.setFillColor(color);
        add(brick);
        howManyBricksOntTheScreenNow++;
    }


    private void ballMove() {
        changeDirectionIfHitSomething();

        ball.move(dx, dy);
        if(isFailed) {
                showFailedLabel();
            return;
        }
        pause(pause_time);
    }

    private void showFailedLabel() {
        int turnsLeft=currentTurn-1;
        if (turnsLeft==0){
            youLose();
            return;
        }
        failString="Ooops \n you have "+turnsLeft+" turns left";
        failedLabel=new GLabel(failString,getWidth()/3,getHeight()/3);
        failedLabel.setFont("Arial-20");
        add(failedLabel);
    }

    private void changeDirectionIfHitSomething() {
        if (isBallHitWall())
            dx = -dx;

        if (isBallHitUp())
            dy = -dy;

        if (isBallHitDown()) {
            isFailed=true;
            return;
        }

        GObject someobj;
        //check up-left corner
        someobj=getElementAt(ball.getX(), ball.getY());
        if ( someobj!= null) {
            remove(someobj);
            howManyBricksOntTheScreenNow--;
            println(howManyBricksOntTheScreenNow);
            showScore();
            if(howManyBricksOntTheScreenNow<=0) {
                win = true;
                return;
            }
            dy = -dy;
            return;
        }


        //check up-right corner
        someobj=getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY());
        if (someobj != null) {
            remove(someobj);
            howManyBricksOntTheScreenNow--;
            showScore();
            if(howManyBricksOntTheScreenNow<=0) {
                win = true;
                return;
            }
            dy = -dy;
            return;
        }

        //check down-left corner
        someobj = getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2);
        if (someobj != null) {
            if (getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2) == paddle) {
                dy = -dy;
                return;
            } else {
                remove(getElementAt(ball.getX(), ball.getY() + BALL_RADIUS * 2));
                howManyBricksOntTheScreenNow--;
                showScore();
                if(howManyBricksOntTheScreenNow<=0) {
                    win = true;
                    return;
                }
                dy = -dy;
                return;
            }
        }


        //check down-right corner
        someobj = getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS * 2);
        if (someobj != null) {
            if (someobj == paddle) {
                dy = -dy;
                return;
            } else {
                remove(someobj);
                howManyBricksOntTheScreenNow--;
                showScore();
                if(howManyBricksOntTheScreenNow<=0) {
                    win = true;
                    return;
                }
                dy = -dy;
                return;
            }
        }
        changeScore();
    }

    private boolean isBallHitDown() {
        boolean ballDown=ball.getY() >= getHeight()-PADDLE_Y_OFFSET;
        if(ballDown)
            ball.move(130,130);
        return ballDown;
    }

    private boolean isBallHitUp() {
        return ball.getY() <= 0;
    }


    private boolean isBallHitWall() {
        return ball.getX() <= 0 || ball.getX() + BALL_RADIUS * 2 >= WIDTH;
    }


    public void mouseMoved(MouseEvent m) {
        double x = m.getX() - PADDLE_WIDTH / 2;
        if (x > WIDTH - PADDLE_WIDTH)
            x = WIDTH - PADDLE_WIDTH;
        if (x < 0)
            x = 0;
        paddle.setLocation(x, getHeight() - PADDLE_Y_OFFSET);
    }


}