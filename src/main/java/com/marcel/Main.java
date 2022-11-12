package com.marcel;

import com.marcel.Rendering.MainRenderController;
import com.marcel.Rendering.utils.ThemeStuff;
import com.marcel.service.ValidateStartupFileService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        ValidateStartupFileService.validateLocalResources();
        log.debug("Starting init...");
        ThemeStuff.init();
        MainRenderController.main();

        //System.out.println("TEST: " + FileStuff.ReadFile("test.txt"));

        log.debug("Done with init!");
        String baseTitleText = "Logic Gate Simulator v0.12";

        double FPS;
        int counter = 100;
        MainRenderController.allowDrawing = true;
        log.info("Running main...");
        MainRenderController.jFrame.setTitle(baseTitleText + " - " + "?" + " FPS");
        while (true)
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
    }
}
