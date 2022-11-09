package com.marcel;

import com.marcel.Rendering.MainRenderController;
import com.marcel.Rendering.Utils.FileStuff;
import com.marcel.Rendering.Utils.ThemeStuff;

public class Main {

    public static void main(String[] args)
    {
        System.out.println("Starting init...");
        ThemeStuff.init();
        MainRenderController.main();


        //System.out.println("TEST: " + FileStuff.ReadFile("test.txt"));



        System.out.println("Done with init!");
        String baseTitleText = "Logic Gate Simulator v0.12";
        double FPS = 1;
        int counter = 100;


        MainRenderController.allowDrawing = true;
        System.out.println("Running main...");
        MainRenderController.jFrame.setTitle(baseTitleText + " - " + "?" + " FPS");
        while (true)
        {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < counter; i++)
            {
                MainRenderController.canvas.update();
                MainRenderController.updateSelectedComponentPosition();

//                try
//                {
//                    Thread.sleep(25);
//                }
//                catch(InterruptedException ex)
//                {
//                    Thread.currentThread().interrupt();
//                    System.out.println("BRUH");
//                }
            }
            double dur = (System.currentTimeMillis() - startTime) / 1000.0;

            FPS = counter/dur;
            MainRenderController.jFrame.setTitle(baseTitleText + " - " + Math.round(FPS * 10)/10.0 + " FPS" + " - " + MainRenderController.canvas.mainWindowRenderer.logicGates.size() + " GATES");
        }
    }
}
