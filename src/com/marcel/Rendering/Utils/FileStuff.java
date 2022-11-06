package com.marcel.Rendering.Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStuff
{
    public static final String InternalFolderPath = "internal/";
    public static final String ConfigFilePath = InternalFolderPath + "config.cfg";
    public static String LogicGateImageFolderPath = null;


    public static String ReadFile(String pathString)
    {
        Path path = Paths.get(pathString);
        try
        {
            byte[] data = Files.readAllBytes(path);
            String str = new String(data, StandardCharsets.UTF_8);
            return str;
        }
        catch (IOException exception)
        {
            System.out.println("ERROR: " + exception);
            return null;
        }
    }
}
