package com.marcel.Rendering.utils;

import com.marcel.Rendering.renderers.LogicComponent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ThemeStuff
{
    public static List<String> supportedThemes = new ArrayList<>();
    public static String theme = "bright";


    public static Dictionary<String, String> themeNames = new Hashtable<>();

    public static void init()
    {
        supportedThemes.add("bright");
        supportedThemes.add("dark");

        changeTheme("bright");
    }

    public static void changeTheme(String newTheme)
    {
        if (!supportedThemes.contains(newTheme))
            return;
        theme = newTheme;


        FileStuff.LogicGateImageFolderPath = FileStuff.InternalFolderPath + "images/gates/" + theme + "/";


        LogicComponent.AndImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"AND.png").getImage();
        LogicComponent.OrImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"OR.png").getImage();
        LogicComponent.NotImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"NOT.png").getImage();
        LogicComponent.SwitchImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"SWITCH.png").getImage();
        LogicComponent.ButtonImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"BUTTON.png").getImage();
        LogicComponent.LedImage = new ImageIcon(FileStuff.LogicGateImageFolderPath+"LED.png").getImage();
    }
}
