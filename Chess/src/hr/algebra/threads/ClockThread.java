/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author fran
 */
public class ClockThread  implements Runnable{
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private Label lblClock;
    public ClockThread(Label label) {
        this.lblClock = label;
    }

    
    @Override
    public void run() {
        while(true) {
            Platform.runLater(() -> lblClock.setText(LocalDateTime.now().format(DATE_TIME_FORMATTER)));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClockThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
