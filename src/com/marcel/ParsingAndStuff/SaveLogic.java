package com.marcel.ParsingAndStuff;

import com.marcel.Rendering.Renderers.LogicComponent;
import com.marcel.Rendering.Utils.FileStuff;

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
                if (comp.inputGates.size() > 0)
                {
                    for (int i = 0; i < comp.inputGates.size() - 1; i++)
                    {
                        str += GetIndexOfLogicComponent(allComponents, comp.inputGates.get(i));
                        str += ", ";
                    }
                    str += GetIndexOfLogicComponent(allComponents, comp.inputGates.get(comp.inputGates.size() - 1));
                }
                str += "], ";
            }
//            {
//                str += "[";
//                if (comp.inputGates.size() > 0)
//                {
//                    for (int i = 0; i < comp.inputGates.size() - 1; i++)
//                    {
//                        if (comp.inputGates.get(i) == null)
//                            str += -1;
//                        else
//                        {
//                            str += comp.inputGates.get(i).out;
//                        }
//                        str += ", ";
//                    }
//                    if (comp.inputGates.get(comp.inputGates.size() - 1) == null)
//                        str += -1;
//                    else
//                    {
//                        str += GetIndexOfLogicComponent(allComponents, comp.inputGates.get(comp.inputGates.size() - 1));
//                    }
//                }
//                str += "], ";
//            }
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

    public static void SaveLogicComponentsToFile(List<LogicComponent> components, String filename)
    {
        System.out.println("Saving...");

        List<LogicComponent> allComponents = new ArrayList<>();
        List<String> tempData = new ArrayList<>();
        for (int i = 0; i < components.size(); i++)
        {
            LogicComponent comp = components.get(i);
            int indx = GetIndexOfLogicComponent(allComponents, comp);

            tempData.add(CompToString(allComponents, comp));
            System.out.println(GetIndexOfLogicComponent(allComponents, comp) + " - " + tempData.get(tempData.size() - 1));
        }




        String resData = "[";
        for (String str : tempData)
            resData += str + " ";
        resData += "]";

        System.out.println("Data:");
        System.out.println(resData);

        FileStuff.WriteFile("test.txt", resData);
    }
}
