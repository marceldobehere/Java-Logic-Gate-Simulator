package com.marcel.rendering.utils;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStuff
{
    public static String InternalFolderPath = FileUtils.getUserDirectoryPath() +
            "/.Logic Gate Simulator/internal/";
    public static String ConfigFilePath = InternalFolderPath + "config.cfg";
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

    public static void WriteFile(String pathString, String data)
    {
        Path path = Paths.get(pathString);
        try
        {
            Files.write(path, data.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException exception)
        {
            System.out.println("ERROR: " + exception);
        }
    }
}
