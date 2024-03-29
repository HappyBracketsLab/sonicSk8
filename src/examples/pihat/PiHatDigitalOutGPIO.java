package examples.pihat;

import net.happybrackets.core.HBAction;
import net.happybrackets.core.HBReset;
import net.happybrackets.device.HB;
import net.happybrackets.device.sensors.gpio.GPIO;
import net.happybrackets.device.sensors.gpio.GPIODigitalOutput;

import java.lang.invoke.MethodHandles;

/**
 * These are for UNSW PI hats
 *
 * In order to use them, you need to set GPIO 28 to high - otherwise they are a tri-state
 *
 *
 * GPIO are 25, 23, 22 and 21
 * This composition will blink a digital output on GPIO_23 (pin 33 on PI header or Pin 2 on Grove)
 * See http://pi4j.com/pins/model-zero-rev1.html for pinouts
 *
 * connect cathode of LED through a resistance to earth and then connect anode to GPIO 23 output
 *
 * The state will be displayed in HB Status
 *
 *                                 ↗ ↗
 *
 *                                ┃ ╱┃
 *    _________________╱╲  ╱╲  ___┃╱ ┃_______________ GPIO_23 GPIO (Pin 2 on Grove)
 * __|__                 ╲╱  ╲╱   ┃╲ ┃
 *  ___                           ┃ ╲┃
 *   _
 *
 *******************************************************/
public class PiHatDigitalOutGPIO implements HBAction, HBReset {

    boolean exitThread = false;
    // We need to SET GPIO 28 High to enable Input or Output for GPIO on PiHat board
    private static final int GPIO_ENABLE =  28;

    final int GPIO_OUTPUT = 23;

    @Override
    public void action(HB hb) {
        /***** Type your HBAction code below this line ******/
        // remove this code if you do not want other compositions to run at the same time as this one
        hb.reset();
        hb.setStatus(this.getClass().getSimpleName() + " Loaded");

        // Reset all our GPIO - Only really necessary if the Pin has been assigned as something other than an input before
        GPIO.resetAllGPIO();

        /* Type gpioDigitalOut to create this code */
        GPIODigitalOutput outputPin = GPIODigitalOutput.getOutputPin(GPIO_OUTPUT);
        if (outputPin == null) {
            hb.setStatus("Fail GPIO Digital Out " + GPIO_OUTPUT);
        }/* End gpioDigitalOut code */


        // Create a runnable thread to turn Output on and off
        if (outputPin != null)
        {
            /* Type threadFunction to generate this code */
            Thread thread = new Thread(() -> {
                int SLEEP_TIME = 500;
                while (!exitThread) {/* write your code below this line */
                    outputPin.setState(!outputPin.getState());

                    /* write your code above this line */

                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        /*** remove the break below to just resume thread or add your own action***/
                        break;
                        /*** remove the break above to just resume thread or add your own action ***/

                    }
                }
            });

            /*** write your code you want to execute before you start the thread below this line ***/

            /*** write your code you want to execute before you start the thread above this line ***/

            thread.start();
            /****************** End threadFunction **************************/

            // Enable our GPIO on the PiHat
            GPIODigitalOutput enablePin = GPIODigitalOutput.getOutputPin(GPIO_ENABLE);
            if (enablePin == null) {
                hb.setStatus("Fail GPIO Digital Out " + GPIO_ENABLE);
            }
            else {
                enablePin.setState(true);
            }

        }
        /***** Type your HBAction code above this line ******/
    }


    /**
     * Add any code you need to have occur when a reset occurs
     */
    @Override
    public void doReset() {
        /***** Type your HBReset code below this line ******/
        // tell our thread to exit
        exitThread = true;

        // now disable our GPIO pin
        GPIO.clearPinAssignment(GPIO_OUTPUT);
        /***** Type your HBReset code above this line ******/
    }

    //<editor-fold defaultstate="collapsed" desc="Debug Start">

    /**
     * This function is used when running sketch in IntelliJ IDE for debugging or testing
     *
     * @param args standard args required
     */
    public static void main(String[] args) {

        try {
            HB.runDebug(MethodHandles.lookup().lookupClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>
}
