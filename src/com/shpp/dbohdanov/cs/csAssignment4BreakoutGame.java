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
    private static final int PADDLE_Y_OFFSET = 30;

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
    private String scoreString="Score: ";
    private GLabel scoreLabel =new GLabel(scoreString,0,0);


    private GRect paddle = new GRect(0, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT); // The Paddle

    private GOval ball = new GOval(WIDTH / 2, HEIGHT / 2, BALL_RADIUS * 2, BALL_RADIUS * 2); // The Ball
    private double dy, dx; //ball "speed"
    private double pause_time = 10; //actually speed of the ball, less=faster
    //----------------------------------------------------------------------------------------



    public void run() {
        drawField();

        addMouseListeners();
        waitForClick();
        while (true) {
            ballMove();
        }
    }
    private void drawField() {
        drawPaddle();
        drawBall();
        drawBricks();
        showScore();
    }

    private void drawPaddle() {
        paddle.setFilled(true);
        paddle.setFillColor(Color.BLACK);
        add(paddle);
    }

    private void drawBall() {
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

    }
    private void changeScore() {

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
        pause(pause_time);
    }

    private void changeDirectionIfHitSomething() {
        if (isBallHitWall())
            dx = -dx;

        if (isBallHitUp())
            dy = -dy;

        GObject someobj;
        //check up-left corner
        someobj=getElementAt(ball.getX(), ball.getY());
        if ( someobj!= null) {
            remove(someobj);
            howManyBricksOntTheScreenNow--;
            dy = -dy;
            return;
        }


        //check up-right corner
        someobj=getElementAt(ball.getX() + BALL_RADIUS * 2, ball.getY());
        if (someobj != null) {
            remove(someobj);
            howManyBricksOntTheScreenNow--;
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
                dy = -dy;
                return;
            }
        }
        changeScore();
    }
    
    private boolean isBallHitUp() {
        if (ball.getY() <= 0/*||isBallHitPaddle()*/)
            return true;
        return false;
    }


    private boolean isBallHitWall() {
        if (ball.getX() <= 0 || ball.getX() + BALL_RADIUS * 2 >= WIDTH)
            return true;
        return false;
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