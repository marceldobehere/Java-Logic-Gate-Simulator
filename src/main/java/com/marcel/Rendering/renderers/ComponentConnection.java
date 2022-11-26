package com.marcel.Rendering.renderers;

public class ComponentConnection {

    public LogicComponent fromComponent;
    public int fromComponentIndex;

    public LogicComponent toComponent;
    public int toComponentIndex;

    public ComponentConnection(LogicComponent from, int fromIndex, LogicComponent to, int toIndex)
    {
        this.fromComponentIndex = fromIndex;
        this.fromComponent = from;
        this.toComponent = to;
        this.toComponentIndex = toIndex;
    }

}
