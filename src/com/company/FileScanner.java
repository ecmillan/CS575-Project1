package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static java.lang.System.exit;

/**
 * Created by Chris on 10/18/2016.
 */
public class FileScanner
{
    /**
     *  Method for getting the full contents of a file and appending the special EOF character
     *  */
    public static String GetFileStringWithEOF(String path)
    {
        try
        {
            File file = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            // append our special end of file symbol for the grammar
            builder.append("&EOF");
            reader.close();
            return builder.toString();
        }
        catch(Exception ex)
        {
            System.out.println("Error in reading file. Exiting program");
            exit(1);
            // return empty string if exit doesn't work
            return "";
        }
    }
}
