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

        MainRenderController.allowDrawing = true;
        System.out.println("Running main...");
        while (true)
        {
            MainRenderController.canvas.update();
            MainRenderController.updateSelectedComponentPosition();

            try
            {
                Thread.sleep(25);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
                System.out.println("BRUH");
            }
        }
    }
}
