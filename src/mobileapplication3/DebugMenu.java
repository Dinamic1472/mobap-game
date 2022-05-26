/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mobileapplication3;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author vipaol
 */
public class DebugMenu extends GameCanvas implements Runnable {
    
    private static final int millis = 50;
    private GenericMenu menu = new GenericMenu();
    private String[] menuOpts = {"Enable debug options", "-----", "closer worldgen trigger", "show X-coordinate", "show speedometer", "cheat(*)", "music", "back"};
    private final int[] statemap = {0, -1, 0, 0, 0, 0, 0, 0};
    boolean stopped = false;
    int scW = 0;
    int scH;
    public static boolean closerWorldgen = false;
    public static boolean xCoord = false;
    public static boolean speedo = false;
    public static boolean cheat = false;
    public static boolean music = false;
    
    public DebugMenu() {
        super(true);
        setFullScreenMode(true);
        (new Thread(this, "about canvas")).start();
    }

    public void run() {
        long sleep = 0;
        long start = 0;

        while (!stopped) {
            start = System.currentTimeMillis();
            input();
            if (scW != getWidth()) {
                scW = getWidth();
                scH = getHeight();
                System.out.println(statemap != null);
                menu.loadParams(scW, scH, menuOpts, statemap);
                menu.setSpecialOption(0);
                refreshStates();
            }
            repaint();

            sleep = millis - (System.currentTimeMillis() - start);
            sleep = Math.max(sleep, 0);

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void paint(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, scW, scH);
        try {
            menu.paint(g);
            menu.tick();
        } catch (NullPointerException ex) {
            
        }
    }
    void selectPressed() {
        int selected = menu.selected;
        if (selected == 0) {
            mnCanvas.debug = !mnCanvas.debug;
            menu.setIsSpecialOptnActivated(mnCanvas.debug);
        }
        if (selected == 2) {
            closerWorldgen = !closerWorldgen;
        }
        if (selected == 3) {
            xCoord = !xCoord;
        }
        if (selected == 4) {
            speedo = !speedo;
        }
        if (selected == 5) {
            cheat = !cheat;
        }
        if (selected == 6) {
            music = !music;
        }
        if (selected == menuOpts.length - 1) {
            stopped = true;
            Main.set(new mnCanvas());
        } else {
            refreshStates();
        }
    }
    void refreshStates() {
        if (mnCanvas.debug) {
            menu.setEnabledFor(closerWorldgen, 2);
            menu.setEnabledFor(xCoord, 3);
            menu.setEnabledFor(speedo, 4);
            menu.setEnabledFor(cheat, 5);
            menu.setEnabledFor(music, 6);
        } else {
            for (int i = 2; i < menuOpts.length - 1; i++) {
                menu.setStateFor(-1, i);
            }
        }
    }
    private void input() {
        int keyStates = getKeyStates();
        if (menu.key(keyStates)) {
            selectPressed();
        }
    }
    protected void pointerPressed(int x, int y) {
        menu.setIsPressedNow(true);
        menu.pointer(x, y);
    }

    protected void pointerDragged(int x, int y) {
        menu.pointer(x, y);
    }

    protected void pointerReleased(int x, int y) {
        menu.setIsPressedNow(false);
        if (menu.pointer(x, y)) {
            selectPressed();
        }
    }
}