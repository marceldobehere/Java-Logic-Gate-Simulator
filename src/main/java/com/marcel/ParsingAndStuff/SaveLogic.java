package com.marcel.ParsingAndStuff;

import com.marcel.Rendering.renderers.ComponentConnection;
import com.marcel.Rendering.renderers.LogicComponent;
import com.marcel.Rendering.utils.FileStuff;

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
            String str = "";
            str += GetIndexOfLogicComponent(allComponents, comp) + ", ";
            str += "BASIC" + ", ";
            str += comp.type + ", ";
            str += comp.basicState + ", ";
            {
                str += "{";
                str += comp.pos.x + ", ";
                str += comp.pos.y;
                str += "}, ";
            }
            {
                str += "[";
                if (comp.inputs.size() > 0)
                {
                    for (int i = 0; i < comp.inputs.size() - 1; i++)
                    {
                        str += comp.inputs.get(i);
                        str += ", ";
                    }
                    str += comp.inputs.get(comp.inputs.size() - 1);
                }
                str += "], ";
            }
            {
                str += "[";
                if (comp.outputs.size() > 0)
                {
                    for (int i = 0; i < comp.outputs.size() - 1; i++)
                    {
                        str += comp.outputs.get(i);
                        str += ", ";
                    }
                    str += comp.outputs.get(comp.outputs.size() - 1);
                }
                str += "]";
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

        String resData = "[";
        for (String str : tempData)
            resData += str;
        resData += "]";

        System.out.println("Data:");
        System.out.println(resData);

        FileStuff.WriteFile("test.txt", resData);
    }
}
