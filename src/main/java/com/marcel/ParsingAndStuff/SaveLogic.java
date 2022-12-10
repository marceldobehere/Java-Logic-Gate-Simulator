package com.marcel.ParsingAndStuff;

import com.marcel.rendering.renderers.ComponentConnection;
import com.marcel.rendering.renderers.LogicComponent;
import com.marcel.rendering.utils.FileStuff;

import java.util.ArrayList;
import java.util.List;

public class SaveLogic {

    public static int GetIndexOfLogicComponent(List<LogicComponent> comps, LogicComponent comp)
    {
        if (comp == null)
            return -1;

        int index = comps.indexOf(comp);
        if (index != -1)
            return index;

        index = comps.size();
        comps.add(comp);
        return index;
    }

    public static String CompToString(List<LogicComponent> allComponents, LogicComponent comp)
    {
        if (comp.isBasic)
        {
            StringBuilder str = new StringBuilder();
            str.append(GetIndexOfLogicComponent(allComponents, comp)).append(", ");
            str.append("BASIC" + ", ");
            str.append(comp.type).append(", ");
            str.append(comp.basicState).append(", ");
            {
                str.append("{");
                str.append(comp.pos.x).append(", ");
                str.append(comp.pos.y);
                str.append("}, ");
            }
            {
                str.append("[");
                if (comp.inputs.size() > 0)
                {
                    for (int i = 0; i < comp.inputs.size() - 1; i++)
                    {
                        str.append(comp.inputs.get(i));
                        str.append(", ");
                    }
                    str.append(comp.inputs.get(comp.inputs.size() - 1));
                }
                str.append("], ");
            }
            {
                str.append("[");
                if (comp.outputs.size() > 0)
                {
                    for (int i = 0; i < comp.outputs.size() - 1; i++)
                    {
                        str.append(comp.outputs.get(i));
                        str.append(", ");
                    }
                    str.append(comp.outputs.get(comp.outputs.size() - 1));
                }
                str.append("]");
            }


            return "{"+str+"}";
        }
        else
        {
            return "{IDK}";
        }
    }

    public static String ConnToString(List<LogicComponent> allComponents, ComponentConnection conn)
    {
        String str = "";

        str += GetIndexOfLogicComponent(allComponents, conn.fromComponent) + ", ";
        str += conn.fromComponentIndex + ", ";
        str += GetIndexOfLogicComponent(allComponents, conn.toComponent) + ", ";
        str += conn.toComponentIndex ;

        return "{"+str+"}";
    }

    public static void SaveLogicComponentsToFile(List<LogicComponent> components, List<ComponentConnection> conns, String filename)
    {
        System.out.println("Saving...");

        List<LogicComponent> allComponents = new ArrayList<>();
        List<String> tempData = new ArrayList<>();

        System.out.println("GATES:");
        tempData.add("[");
        for (int i = 0; i < components.size(); i++)
        {
            LogicComponent comp = components.get(i);
            GetIndexOfLogicComponent(allComponents, comp);

            tempData.add(CompToString(allComponents, comp));
            System.out.println(GetIndexOfLogicComponent(allComponents, comp) + " - " + tempData.get(tempData.size() - 1));

            if (i != components.size() - 1)
                tempData.add(", ");
        }
        tempData.add("], ");

        System.out.println("CONNECTIONS:");

        tempData.add("[");
        for (int i = 0; i < conns.size(); i++)
        {
            ComponentConnection conn = conns.get(i);

            tempData.add(ConnToString(allComponents, conn));
            System.out.println(" - " + tempData.get(tempData.size() - 1));

            if (i != conns.size() - 1)
                tempData.add(", ");
        }
        tempData.add("]");

        StringBuilder resData = new StringBuilder("[");
        for (String str : tempData)
            resData.append(str);
        resData.append("]");

        System.out.println("Data:");
        System.out.println(resData);

        FileStuff.WriteFile(filename, resData.toString());
    }
}
