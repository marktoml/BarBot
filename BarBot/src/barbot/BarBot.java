/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package barbot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Properties;


/**
 *
 * @author john
 */
public class BarBot {
 static BarBotGuiTest mainframe;
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        mainframe = new BarBotGuiTest();
        mainframe.setVisible(true);
        
    }
    
}
