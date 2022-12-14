package com.marcel;

import com.marcel.rendering.MainRenderController;
import com.marcel.rendering.utils.ThemeStuff;
import com.marcel.service.ValidateStartupFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("all")
    public static void main(String[] args)
    {
        ValidateStartupFileService.validateLocalResources();
        log.debug("Starting init...");
        ThemeStuff.init();
        MainRenderController.main();

        //System.out.println("TEST: " + FileStuff.ReadFile("test.txt"));

        log.debug("Done with init!");
        String baseTitleText = "Logic Gate Simulator v0.14";

        double FPS;
        int counter = 100;
        MainRenderController.allowDrawing = true;
        log.info("Running main...");
        MainRenderController.jFrame.setTitle(baseTitleText + " - " + "?" + " FPS");
        while (!MainRenderController.exit)
        {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < counter; i++)
            {
                MainRenderController.canvas.update();
                MainRenderController.updateSelectedComponentPosition();

                try
                {
                    Thread.sleep(10);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
            double dur = (System.currentTimeMillis() - startTime) / 1000.0;

            FPS = counter/dur;
            MainRenderController.jFrame.setTitle(baseTitleText + " - " + Math.round(FPS * 10)/10.0 + " FPS" + " - " + MainRenderController.canvas.mainWindowRenderer.logicGates.size() + " GATES");
        }

        System.out.println("Exiting...");
    }
}
