package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long last_frame_time;
    private static Image background;
    private static Image game_over;
    private static Image drop;
    private static Image startButton;
    private static Image title;
    private static Image retry;
    private static float drop_left = 200;
    private static float drop_top = - 100;
    private static float drop_v = 200;
    private static int score;
    private static GameField game_field;
    private static boolean isStart = false;
    private static boolean isGameOver = false;

    public static void main(String[] args) throws IOException{
        initialization();

        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
            g.drawImage(background, 0, 0, null);
        if (isGameOver) {
            gameOver(g);
            return;
        }
        if (isStart) {
            long current_time = System.nanoTime();
            float delta_time = (current_time - last_frame_time) * 0.000000001f;
            last_frame_time = current_time;
            drop_top = drop_top + drop_v * delta_time;
            g.drawImage(drop, (int) drop_left, (int) drop_top, null);
            if (drop_top > game_window.getHeight()) gameOver(g);
        } else {
            g.drawImage(startButton, 350, 180, null);
            g.drawImage(title, 160, 50, null);

        }
    }

    private static void initialization() throws IOException{
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        startButton = ImageIO.read(GameWindow.class.getResourceAsStream("start.png"));
        title = ImageIO.read(GameWindow.class.getResourceAsStream("title.png"));
        retry = ImageIO.read(GameWindow.class.getResourceAsStream("retry.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200, 100);
        game_window.setSize(906, 478);
        game_window.setResizable(false);

        game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                boolean is_start = x >= 355 && x <= 560 && y >= 190 && y <= 270;
                boolean is_retry = (x >= 380 && x <= 580 && y >= 180 && y <= 270) && isGameOver;
                if (is_drop){
                    drop_top = -100;
                    drop_left =(float) Math.random() * (game_field.getWidth() - drop.getWidth(null));
                    drop_v = drop_v + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }

                if (is_start){
                    isStart = true;
                    last_frame_time = System.nanoTime();
                }

                if (is_retry){
                    isGameOver = false;
                    isStart = false;
                    drop_top = - 100;
                    drop_v = 200;
                    score = 0;
                    game_window.setTitle("Score: " + score);
                }

            }
        });
    }

    private static void gameOver(Graphics g){
        isGameOver = true;
        g.drawImage(game_over, 180, 80, null);
        g.drawImage(retry, 180, 180, null);

    }

    private static class GameField extends JPanel{
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }

}
