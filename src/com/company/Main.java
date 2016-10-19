package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main
{

    public static void main(String[] args)
    {

        String contents;
        // todo - replace this next block with using args passed in to find files
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the name of the file to parse (e.g. input.xml): ");
        String fileName = scan.nextLine();
        contents = FileScanner.GetFileStringWithEOF("D:\\Dropbox\\Github\\input files\\" + fileName);
        scan.close();

        // now we have file contents, time to tokenize
        Tokenizer tk = new Tokenizer();
        try
        {
            //List<Token> myTokens = tk.getTokenStream(builder.toString());
            List<Token> tokens = tk.getTokenStream(contents);
        }
        catch(Exception ex)
        {
            System.out.println("Error in tokenizing input. Exiting program");
            exit(1);
        }
        //List<Token> myTokens = new Tokenizer();(input).getTokenStream();
        //Parser parser = new Parser();
        //parser.parse(myTokens);
    }
}
