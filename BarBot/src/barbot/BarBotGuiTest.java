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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.JOptionPane;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JOptionPane;

/**
 *
 * @author john
 */
public class BarBotGuiTest extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    
    Properties properties = new Properties();
    MotorThread[] motorThrdAry = new MotorThread[7];
    String[] motorpin = new String[7];
    String[] ingrediants = new String[7];
    String readOne;
    ArrayList<String> recipes = new ArrayList<>();
    Queue drinkQ = new LinkedList();
    String[] recipeList;
    String[] drinkSelected;
    String[] drinkParts;
    int activeIngredient = 0;
    long[] motorCalc = new long[7];
    String MotorFactor;
    
    GpioController gpio;
    GpioPinDigitalOutput[] motor_IO = new GpioPinDigitalOutput[7];
    private final Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private boolean waitCursorIsShowing;
    
    
    /**
     * Creates new form BarBotGuiTest
     */
    public BarBotGuiTest() {
        int i = 0;
        boolean get = true;
        String MotorFactor;
        this.setUndecorated(true);
        initComponents();
        GetProperties();
        
        gpio = GpioFactory.getInstance();
        motorpin[0] = properties.getProperty("motor1pin");
        if(motorpin[0] == null){
            motorpin[0] = "GPIO 21";
            properties.setProperty("motor1pin", motorpin[0]);
        }
        MotorFactor = properties.getProperty("motor1calc");
        if(MotorFactor == null)   {
            motorCalc[0] = Long.valueOf("18000");
            properties.setProperty("motor1calc", "18000");
        } else  {
            motorCalc[0] = Long.valueOf(MotorFactor);
        }
       motorpin[1] = properties.getProperty("motor2pin");
        if(motorpin[1] == null){
            motorpin[1] = "GPIO 22";
            properties.setProperty("motor2pin", motorpin[1]);
        }
        MotorFactor = properties.getProperty("motor2calc");
        if(MotorFactor == null)   {
            motorCalc[1] = Long.valueOf("18000");
            properties.setProperty("motor2calc", "18000");
        } else  {
            motorCalc[1] = Long.valueOf(MotorFactor);
        }
        motorpin[2] = properties.getProperty("motor3pin");
        if(motorpin[2] == null){
            motorpin[2] = "GPIO 23";
            properties.setProperty("motor3pin", motorpin[2]);
        }
        MotorFactor = properties.getProperty("motor3calc");
        if(MotorFactor == null)   {
            motorCalc[2] = Long.valueOf("18000");
            properties.setProperty("motor3calc", "18000");
        } else  {
            motorCalc[2] = Long.valueOf(MotorFactor);
        }
        motorpin[3] = properties.getProperty("motor4pin");
        if(motorpin[3] == null){
            motorpin[3] = "GPIO 24";
            properties.setProperty("motor4pin", motorpin[3]);
        }
        MotorFactor = properties.getProperty("motor4calc");
        if(MotorFactor == null)   {
            motorCalc[3] = Long.valueOf("18000");
            properties.setProperty("motor4calc", "18000");
        } else  {
            motorCalc[3] = Long.valueOf(MotorFactor);
        }
        motorpin[4] = properties.getProperty("motor5pin");
        if(motorpin[4] == null){
            motorpin[4] = "GPIO 25";
            properties.setProperty("motor5pin", motorpin[4]);
        }
        MotorFactor = properties.getProperty("motor5calc");
        if(MotorFactor == null)   {
            motorCalc[4] = Long.valueOf("18000");
            properties.setProperty("motor5calc", "18000");
        } else  {
            motorCalc[4] = Long.valueOf(MotorFactor);
        }
        motorpin[5] = properties.getProperty("motor6pin");
        if(motorpin[5] == null){
            motorpin[5] = "GPIO 27";
            properties.setProperty("motor6pin", motorpin[5]);
        }
        MotorFactor = properties.getProperty("motor6calc");
        if(MotorFactor == null)   {
            motorCalc[5] = Long.valueOf("18000");
            properties.setProperty("motor6calc", "18000");
        } else  {
            motorCalc[5] = Long.valueOf(MotorFactor);
        }
        motorpin[6] = properties.getProperty("motor7pin");
        if(motorpin[6] == null){
            motorpin[6] = "GPIO 29";
            properties.setProperty("motor7pin", motorpin[6]);
        }
        MotorFactor = properties.getProperty("motor7calc");
        if(MotorFactor == null)   {
            motorCalc[6] = Long.valueOf("18000");
            properties.setProperty("motor4calc", "18000");
        } else  {
            motorCalc[6] = Long.valueOf(MotorFactor);
        }
        
        ingrediants[0] = properties.getProperty("ingrediant1");
        ingrediants[1] = properties.getProperty("ingrediant2");
        ingrediants[2] = properties.getProperty("ingrediant3");
        ingrediants[3] = properties.getProperty("ingrediant4");
        ingrediants[4] = properties.getProperty("ingrediant5");
        ingrediants[5] = properties.getProperty("ingrediant6");
        ingrediants[6] = properties.getProperty("ingrediant7");
        
        
        do {
            i++;
           readOne = "recipe"+i;
           readOne = properties.getProperty(readOne);
           if (readOne != null) {
              recipes.add(readOne);
           }
           else {
               get = false;
           }
        } while (get);
        recipeList = new String[recipes.size()];
        for(i = 0; i < recipes.size(); i++){
            String[] foos = recipes.get(i).split(":");
            recipeList[i] = foos[0];            
        }
        RecipeList.setListData(recipeList);
        SaveProperties();
        
        motor_IO[0] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[0]), "motor", PinState.LOW);
        motor_IO[1] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[1]), "motor", PinState.LOW);
        motor_IO[2] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[2]), "motor", PinState.LOW);
        motor_IO[3] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[3]), "motor", PinState.LOW);
        motor_IO[4] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[4]), "motor", PinState.LOW);
        motor_IO[5] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[5]), "motor", PinState.LOW);
        motor_IO[6] = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motorpin[6]), "motor", PinState.LOW);
        
        // provision gpio pin #01 as an output pin and turn on
        //final GpioPinDigitalOutput motor = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "motor", PinState.LOW);
        //final GpioPinDigitalOutput motor = gpio.provisionDigitalOutputPin(RaspiPin.getPinByName(motor), "motor", PinState.LOW);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LogoPanel = new javax.swing.JPanel();
        CudaVersionLabel = new javax.swing.JLabel();
        CudaImageLabel = new javax.swing.JLabel();
        DrinkQueCount = new javax.swing.JLabel();
        DrinkQueueLabel = new javax.swing.JLabel();
        drinkStatus = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        RecipeList = new javax.swing.JList<>();
        PlaceOrderButton = new javax.swing.JButton();
        MakeDrinksButton = new javax.swing.JButton();
        IngrediantPanel = new javax.swing.JPanel();
        Ingrediant2 = new javax.swing.JLabel();
        IngOz2 = new javax.swing.JTextField();
        Ingrediant3 = new javax.swing.JLabel();
        IngOz3 = new javax.swing.JTextField();
        Ingrediant4 = new javax.swing.JLabel();
        Ingrediant5 = new javax.swing.JLabel();
        IngOz4 = new javax.swing.JTextField();
        IngOz5 = new javax.swing.JTextField();
        LessButton = new javax.swing.JToggleButton();
        IngrediantFrameLbl = new javax.swing.JLabel();
        IngrediantOzLbl = new javax.swing.JLabel();
        Ingrediant1 = new javax.swing.JLabel();
        MoreButton = new javax.swing.JToggleButton();
        IngOz1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMaximumSize(new java.awt.Dimension(800, 400));

        CudaVersionLabel.setText("1.0");

        CudaImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/barbot/barracuda_tiny.jpg"))); // NOI18N

        DrinkQueCount.setText("0");

        DrinkQueueLabel.setText("Drinks Queued");

        javax.swing.GroupLayout LogoPanelLayout = new javax.swing.GroupLayout(LogoPanel);
        LogoPanel.setLayout(LogoPanelLayout);
        LogoPanelLayout.setHorizontalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoPanelLayout.createSequentialGroup()
                .addComponent(DrinkQueueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(DrinkQueCount, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(CudaImageLabel)
                .addGap(60, 60, 60)
                .addComponent(CudaVersionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(drinkStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        LogoPanelLayout.setVerticalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LogoPanelLayout.createSequentialGroup()
                .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, LogoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(drinkStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(CudaImageLabel)
                    .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LogoPanelLayout.createSequentialGroup()
                            .addGap(36, 36, 36)
                            .addComponent(CudaVersionLabel))
                        .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DrinkQueueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DrinkQueCount))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CudaImageLabel.getAccessibleContext().setAccessibleName("pic");

        RecipeList.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        RecipeList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        RecipeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                RecipeListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(RecipeList);

        PlaceOrderButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        PlaceOrderButton.setText("Place Order");
        PlaceOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlaceOrderButtonActionPerformed(evt);
            }
        });

        MakeDrinksButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        MakeDrinksButton.setText("Make Drinks");
        MakeDrinksButton.setEnabled(false);
        MakeDrinksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MakeDrinksButtonActionPerformed(evt);
            }
        });

        Ingrediant2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Ingrediant2.setText("__________");

        IngOz2.setText("0");

        Ingrediant3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Ingrediant3.setText("__________");

        IngOz3.setText("0");

        Ingrediant4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Ingrediant4.setText("__________");

        Ingrediant5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Ingrediant5.setText("__________");

        IngOz4.setText("0");

        IngOz5.setText("0");
        IngOz5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IngOz5ActionPerformed(evt);
            }
        });

        LessButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        LessButton.setText("LESS");
        LessButton.setToolTipText("");
        LessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LessButtonActionPerformed(evt);
            }
        });

        IngrediantFrameLbl.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        IngrediantFrameLbl.setText("Ingrediant");

        IngrediantOzLbl.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        IngrediantOzLbl.setText("Ounces");

        Ingrediant1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Ingrediant1.setText("__________");

        MoreButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        MoreButton.setText("MORE");
        MoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoreButtonActionPerformed(evt);
            }
        });

        IngOz1.setText("0");

        javax.swing.GroupLayout IngrediantPanelLayout = new javax.swing.GroupLayout(IngrediantPanel);
        IngrediantPanel.setLayout(IngrediantPanelLayout);
        IngrediantPanelLayout.setHorizontalGroup(
            IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IngrediantPanelLayout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(IngrediantPanelLayout.createSequentialGroup()
                        .addComponent(LessButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(MoreButton))
                    .addGroup(IngrediantPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Ingrediant1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ingrediant2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ingrediant3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ingrediant4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Ingrediant5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IngOz3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IngOz2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IngOz1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IngOz4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(IngOz5, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(IngrediantPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(IngrediantFrameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IngrediantOzLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(71, 71, 71))
        );

        IngrediantPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {LessButton, MoreButton});

        IngrediantPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {IngOz1, IngOz2, IngOz3, IngOz4, IngOz5});

        IngrediantPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {Ingrediant1, Ingrediant2, Ingrediant3, Ingrediant4, Ingrediant5});

        IngrediantPanelLayout.setVerticalGroup(
            IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IngrediantPanelLayout.createSequentialGroup()
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IngrediantFrameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IngrediantOzLbl))
                .addGap(16, 16, 16)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ingrediant1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IngOz1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ingrediant2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IngOz2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ingrediant3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IngOz3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ingrediant4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IngOz4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IngOz5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ingrediant5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(IngrediantPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LessButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        IngrediantPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {IngOz1, IngOz2, IngOz3, IngOz4, IngOz5, Ingrediant1, Ingrediant2, Ingrediant3, Ingrediant4, Ingrediant5});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PlaceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MakeDrinksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(IngrediantPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PlaceOrderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MakeDrinksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(IngrediantPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 18, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LogoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LogoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlaceOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlaceOrderButtonActionPerformed
        String drinkRecipe = RecipeList.getSelectedValue() + ":" + Ingrediant1.getText() + ";" + IngOz1.getText();
        
        if (!Ingrediant2.getText().contentEquals("__________")) {
            drinkRecipe += ":" + Ingrediant2.getText() + ";" + IngOz2.getText();
        }
        if (!Ingrediant3.getText().contentEquals("__________")) {
            drinkRecipe += ":" + Ingrediant3.getText() + ";" + IngOz3.getText();
        }
        if (!Ingrediant4.getText().contentEquals("__________")) {
            drinkRecipe += ":" + Ingrediant4.getText() + ";" + IngOz4.getText();
        }
        if (!Ingrediant5.getText().contentEquals("__________")) {
            drinkRecipe += ":" + Ingrediant5.getText() + ";" + IngOz5.getText();
        }

        drinkQ.add(drinkRecipe);
        DrinkQueCount.setText(String.valueOf(drinkQ.size()));
        MakeDrinksButton.setEnabled(true);
    }

    private void GetProperties() {
        // Read properties file.
        try {
            properties.load(new FileInputStream("Barbot.properties"));
        } catch (IOException e) {
              StringWriter sw = new StringWriter();
              String       stacktrace;

              System.out.println("Can't read properties file, will create on exit. " + e.getMessage());
            //  ErrorLogger.log(Level.INFO, "Can't read properties file, will create on exit. " + e.getMessage());
            //  e.printStackTrace(new PrintWriter(sw));
              stacktrace = sw.toString();
              java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_PlaceOrderButtonActionPerformed

    private void SaveProperties()      {
          // Write properties file.
          try {
              properties.store(new FileOutputStream("Barbot.properties"), null);
          } catch (IOException e) {
                StringWriter sw = new StringWriter();
                String       stacktrace;

                System.out.println("Error saving properties file. " + e.getMessage());
              //  ErrorLogger.log(Level.SEVERE, "Error saving properties file. " + e.getMessage());
              //  e.printStackTrace(new PrintWriter(sw));
                stacktrace = sw.toString();
              java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, e);  
          }
    }

    private void MakeDrinksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MakeDrinksButtonActionPerformed
        String   curDrinkRecipe;
        String[] partsOfRecipe;
        
        try {
            PlaceOrderButton.setEnabled(false);

            // Make Orders/Drinnks
            while(drinkQ.size() > 0)    {
                curDrinkRecipe = (String) drinkQ.remove();
                partsOfRecipe = curDrinkRecipe.split(":");
                JOptionPane.showMessageDialog(null, "Prepare glass for " + partsOfRecipe[0]);
                for(int i = 1; i < partsOfRecipe.length; i++)   {
                    for(int j = 0; j < 7; j++){
                        String[] aPart = partsOfRecipe[i].split(";");
                            if(aPart[0].equalsIgnoreCase(ingrediants[j])){
                                drinkStatus.setText("Pouring " + partsOfRecipe[0]);
                                /* 
                                * MotorThread( 
                                *               this,                                       --> a copy of this current class
                                *               motorpin[j],                                --> which motor to run
                                *               (long)(Float.parseFloat(aPart[1])/.000056), --> an explicit length of time in milliseconds (see below)
                                *               gpio,                                       --> Pi4J GPIO Controller object
                                *               motor_IO[j]                                 --> which GpioPinDigitalOutput to use
                                *               );
                                *
                                */
                                motorThrdAry[i] = new MotorThread(this, motorpin[j],(long)(Float.parseFloat(aPart[1])/(double)(1.0/motorCalc[j])),gpio,motor_IO[j]);
                                motorThrdAry[i].start();
                            }
                        }
                    }
            }
            PlaceOrderButton.setEnabled(true);  
            MakeDrinksButton.setEnabled(false);
        } catch(HeadlessException | NumberFormatException e)    {
            java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, e);  
            PlaceOrderButton.setEnabled(true); 
            if(drinkQ.isEmpty())   {
                MakeDrinksButton.setEnabled(false);
            }
        }
    }//GEN-LAST:event_MakeDrinksButtonActionPerformed

    private void RecipeListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_RecipeListValueChanged
        int i = RecipeList.getSelectedIndex();
        String[] drinkSelect = recipes.get(i).split(":");
        
        for(i = 0; i <= 5; i++)   {
            if(drinkSelect.length <=i)  {
                drinkParts = drinkSelect[i].split(";");
                switch(i)    {
                    case 0:
                            Ingrediant1.setText("__________");
                            IngOz1.setText("0.0");
                            Ingrediant2.setText("__________");
                            IngOz2.setText("0.0");
                            Ingrediant3.setText("__________");
                            IngOz3.setText("0.0");
                            Ingrediant4.setText("__________");
                            IngOz4.setText("0.0");
                            Ingrediant5.setText("__________");
                            IngOz5.setText("0.0");
    //                        Ingrediant6.setText("__________");
    //                        IngOz6.setText("0.0");
    //                        Ingrediant6.setText("__________");
    //                        IngOz6.setText("0.0");
                        break;
                    case 1:
                        if((drinkParts == null) || (drinkParts[0].isEmpty())) {
                            Ingrediant1.setText("__________");
                            IngOz1.setText("0.0");
                        }   else    {
                            Ingrediant1.setText(drinkParts[0]);
                            IngOz1.setText(drinkParts[1]);
                        }
                        break;
                    case 2:
                        if((drinkParts == null) || (drinkParts[0].isEmpty())) {
                            Ingrediant2.setText("__________");
                            IngOz2.setText("0.0");
                        }   else    {
                            Ingrediant2.setText(drinkParts[0]);
                            IngOz2.setText(drinkParts[1]);
                        }
                        break;
                    case 3:
                        if((drinkParts == null) || (drinkParts[0].isEmpty())) {
                            Ingrediant3.setText("__________");
                            IngOz3.setText("0.0");
                        }   else    {
                            Ingrediant3.setText(drinkParts[0]);
                            IngOz3.setText(drinkParts[1]);
                        }
                        break;
                    case 4:
                        if((drinkParts == null) || (drinkParts[0].isEmpty())) {
                            Ingrediant4.setText("__________");
                            IngOz4.setText("0.0");
                        }   else    {
                            Ingrediant4.setText(drinkParts[0]);
                            IngOz4.setText(drinkParts[1]);
                        }
                        break;
                    case 5:
                        if((drinkParts == null) || (drinkParts[0].isEmpty())) {
                            Ingrediant5.setText("__________");
                            IngOz5.setText("0.0");
                        }   else    {
                            Ingrediant5.setText(drinkParts[0]);
                            IngOz5.setText(drinkParts[1]);
                        }
                        break;
                }
            }
        }
    }//GEN-LAST:event_RecipeListValueChanged

    private void LessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LessButtonActionPerformed
        float oz;
        
        switch(activeIngredient)    {
            case 0:
                oz = Float.parseFloat(IngOz1.getText());
                oz = oz - (float)0.5;
                IngOz1.setText(Float.toString(oz));
                IngOz1.repaint();
                break;
            case 1:
                oz = Float.parseFloat(IngOz2.getText());
                oz = oz - (float)0.5;
                IngOz2.setText(Float.toString(oz));
                IngOz2.repaint();
                break;
            case 2:
                oz = Float.parseFloat(IngOz3.getText());
                oz = oz - (float)0.5;
                IngOz3.setText(Float.toString(oz));
                IngOz3.repaint();
                break;
            case 3:
                oz = Float.parseFloat(IngOz4.getText());
                oz = oz - (float)0.5;
                IngOz4.setText(Float.toString(oz));
                IngOz4.repaint();
                break;
            case 4:
                oz = Float.parseFloat(IngOz5.getText());
                oz = oz - (float)0.5;
                IngOz5.setText(Float.toString(oz));
                IngOz5.repaint();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_LessButtonActionPerformed

    private void MoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoreButtonActionPerformed
        float oz;
        
        switch(activeIngredient)    {
            case 0:
                oz = Float.parseFloat(IngOz1.getText());
                oz = oz + (float)0.5;
                IngOz1.setText(Float.toString(oz));
                IngOz1.repaint();
                break;
            case 1:
                oz = Float.parseFloat(IngOz2.getText());
                oz = oz + (float)0.5;
                IngOz2.setText(Float.toString(oz));
                IngOz2.repaint();
                break;
            case 2:
                oz = Float.parseFloat(IngOz3.getText());
                oz = oz + (float)0.5;
                IngOz3.setText(Float.toString(oz));
                IngOz3.repaint();
                break;
            case 3:
                oz = Float.parseFloat(IngOz4.getText());
                oz = oz + (float)0.5;
                IngOz4.setText(Float.toString(oz));
                IngOz4.repaint();
                break;
            case 4:
                oz = Float.parseFloat(IngOz5.getText());
                oz = oz + (float)0.5;
                IngOz5.setText(Float.toString(oz));
                IngOz5.repaint();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_MoreButtonActionPerformed

    private void IngOz5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IngOz5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IngOz5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
             /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BarBotGuiTest().setVisible(true);
            }
            });
            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BarBotGuiTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    public void setwait(boolean wait, MotorThread child){
        if(wait){
            waitCursorIsShowing = true;
            this.setCursor(waitCursor);
            PlaceOrderButton.setEnabled(!waitCursorIsShowing);
            MakeDrinksButton.setEnabled(!waitCursorIsShowing);
        }
        else{
            for(int i =0; i < 5; i++)    {
                if((motorThrdAry[i] != null) && (motorThrdAry[i] != child)){
                    if(motorThrdAry[i].isAlive()){
                        return;
                    }
                } 
            }
            waitCursorIsShowing = false;
            drinkStatus.setText("");
            this.setCursor(defaultCursor);
            PlaceOrderButton.setEnabled(!waitCursorIsShowing);
            MakeDrinksButton.setEnabled(!waitCursorIsShowing);
        }
    }
     
      protected void finalize() {
         //var
         gpio.shutdown();
     }
          
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CudaImageLabel;
    private javax.swing.JLabel CudaVersionLabel;
    private javax.swing.JLabel DrinkQueCount;
    private javax.swing.JLabel DrinkQueueLabel;
    private javax.swing.JTextField IngOz1;
    private javax.swing.JTextField IngOz2;
    private javax.swing.JTextField IngOz3;
    private javax.swing.JTextField IngOz4;
    private javax.swing.JTextField IngOz5;
    private javax.swing.JLabel Ingrediant1;
    private javax.swing.JLabel Ingrediant2;
    private javax.swing.JLabel Ingrediant3;
    private javax.swing.JLabel Ingrediant4;
    private javax.swing.JLabel Ingrediant5;
    private javax.swing.JLabel IngrediantFrameLbl;
    private javax.swing.JLabel IngrediantOzLbl;
    private javax.swing.JPanel IngrediantPanel;
    private javax.swing.JToggleButton LessButton;
    private javax.swing.JPanel LogoPanel;
    private javax.swing.JButton MakeDrinksButton;
    private javax.swing.JToggleButton MoreButton;
    private javax.swing.JButton PlaceOrderButton;
    private javax.swing.JList<String> RecipeList;
    private javax.swing.JLabel drinkStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
