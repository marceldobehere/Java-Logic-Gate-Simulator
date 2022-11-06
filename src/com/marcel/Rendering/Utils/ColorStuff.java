package com.marcel.Rendering.Utils;

import java.awt.*;

public class ColorStuff
{
    public static Color mix(Color a, Color b, double percent)
    {
        percent = 1 - percent;
        return new Color((int) (a.getRed() * percent + b.getRed() * (1.0 - percent)),
                (int) (a.getGreen() * percent + b.getGreen() * (1.0 - percent)),
                (int) (a.getBlue() * percent + b.getBlue() * (1.0 - percent)));
    }
}
