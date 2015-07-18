package com.shpp.dbohdanov.cs;

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

    private GRect paddle=new GRect(0, getHeight() - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
    private void drawPaddle() {
        paddle.setFilled(true);
        paddle.setFillColor(Color.BLACK);
        add(paddle);
    }

    private GOval ball= new GOval(WIDTH/2,HEIGHT/2, BALL_RADIUS, BALL_RADIUS);
    private void drawBall(){
        ball.setFilled(true);
        ball.setFillColor(Color.BLACK);
        add(ball);
    }

    public void run() {
        drawPaddle();
        drawBall();
        addMouseListeners();
        while (true){
            ballMove();
        }
    }

    private void ballMove() {
        double dy=3;

        RandomGenerator rgen = RandomGenerator.getInstance();
        double dx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5))
            dx = -dx;
        ball.move(dx,dy);
        pause(500);
    }


    public void mouseMoved(MouseEvent m) {
        double x=m.getX()-PADDLE_WIDTH/2;
        if(x>WIDTH-PADDLE_WIDTH)
            x=WIDTH-PADDLE_WIDTH;
        if(x<0)
            x=0;
        paddle.setLocation(x, getHeight() - PADDLE_Y_OFFSET);
    }





}