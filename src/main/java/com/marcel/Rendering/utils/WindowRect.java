package com.marcel.Rendering.utils;

public class WindowRect {
    public int x1, y1, x2, y2;
    public WindowRect()
    {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }
    public int getWidth()
    {
        return (x2 - x1) + 1;
    }
    public int getHeight()
    {
        return (y2 - y1) + 1;
    }
}

