/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barbot;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class MotorThread extends Thread{
String motornum;
long duration;
GpioController gpio;
GpioPinDigitalOutput motor;
BarBotGuiTest myMain;

    private static final Logger LOG = Logger.getLogger(MotorThread.class.getName());

    public MotorThread(BarBotGuiTest Main, String pinNum, long runTime, GpioController Gpio, GpioPinDigitalOutput Motor) {
        //var
       motornum = pinNum;
       duration = runTime;
       gpio = Gpio;
       motor = Motor;
       myMain = Main;  
    }
     protected void finalize() {
         //var
         
     }
     public void run(){
         //code
         // create gpio controller
        //final GpioController gpio = GpioFactory.getInstance();
        
        // https://github.com/Pi4J/pi4j/issues/217
        
        // provision gpio pin #01 as an output pin and turn on
        //final GpioPinDigitalOutput motor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "motor", PinState.LOW);
        //final GpioPinDigitalOutput motor = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motor), "motor", PinState.LOW);
        // set shutdown state for this pin
        try {
            myMain.setwait(true, this);
            motor.setShutdownOptions(true, PinState.LOW);

            motor.pulse(duration); // set second argument to 'true' use a blocking call
            myMain.setwait(false, this);
        } catch (Exception e)   {
            myMain.setwait(false, this);
            e.printStackTrace();
        }
     }
}
