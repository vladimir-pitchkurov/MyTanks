package my.tanks.display;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public abstract class Display {
    private static boolean created = false;
    private static JFrame window;
    private static Canvas content;

    private static BufferedImage buffer;
    private static int[] bufferData;
    private static Graphics bufferGraphics;
    private static int clearColor;

    private static BufferStrategy bufferStrategy;

    //
    private static float delta = 0;
    //


    public static void create(int width, int height, String title, int _clearColor, int numBuffers){
        if(created)return;
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        content = new Canvas();

        Dimension size = new Dimension(width, height);
        content.setPreferredSize(size);


        window.setResizable(false);
        window.getContentPane().add(content);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
        bufferGraphics = buffer.getGraphics();

        clearColor = _clearColor;

        content.createBufferStrategy(numBuffers);
        bufferStrategy = content.getBufferStrategy();



        created = true;
    }

    public static void clear(){
        Arrays.fill(bufferData, clearColor);
    }

    public static void render(){
        bufferGraphics.setColor(new Color(0xff0000ff));
        bufferGraphics.fillOval((int)( 350 + (Math.sin(delta) * 200)), 250, 100, 100);  //for demo of running..
        delta += 0.02f;  /*движение круга*/

        /*
        Сглаживание: круг №2 размытый по краям. после отрисовки сглаживание отключается чтобы не распространялось на другие объекты
        */

        ((Graphics2D)bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bufferGraphics.fillOval((int)( 550 + (Math.sin(delta) * 200)), 250, 100, 100);
        ((Graphics2D)bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); /*off*/

    }

    public static void swapBuffers(){
        Graphics g = bufferStrategy.getDrawGraphics();
        g.drawImage(buffer, 0, 0, null);
        bufferStrategy.show();

    }

}