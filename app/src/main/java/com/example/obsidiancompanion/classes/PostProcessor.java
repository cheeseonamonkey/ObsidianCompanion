package com.example.obsidiancompanion.classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PostProcessor
{

    static String ReplaceVariables(String strIn)
    {
        //todo: format these
        String strOut =
                strIn.replace("[!date]", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
        .replace("[!time]", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_TIME));

        return strOut;
    }

    public static String process(String strIn)
    {
        String strOut = strIn;

String strPrepend = "";
String strAppend = "";

    strOut = strPrepend + strOut + strAppend;

    strOut = ReplaceVariables(strOut);


        return strOut;
    }

}
