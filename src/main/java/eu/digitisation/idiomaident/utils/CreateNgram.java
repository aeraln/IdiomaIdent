/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.digitisation.idiomaident.utils;

import eu.digitisation.log.Messages;
import eu.digitisation.ngram.NgramModel;
import eu.digitisation.text.WordScanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Impact
 */
public class CreateNgram
{

    public static NgramModel createNgram(int order, String text)
    {
        NgramModel result = new NgramModel(order);
        try
        {
            WordScanner scanner = new WordScanner(text, "^\\p{Space}+");
            String word;
            while ((word = scanner.nextWord()) != null) 
            {                
                result.addWord(word);                
            }
        }
        catch (IOException ex)
        {
            Messages.info(NgramModel.class
                    .getName() + ": " + ex);
        }
        return result;
    }   
    
    public static NgramModel addToNgram(NgramModel ngram, String text)
    {        
        try
        {
            WordScanner scanner = new WordScanner(text, "^\\p{Space}+");
            String word;
            while ((word = scanner.nextWord()) != null) 
            {                
                ngram.addWord(word);                
            }
        }
        catch (IOException ex)
        {
            Messages.info(NgramModel.class
                    .getName() + ": " + ex);
        }
        return ngram;
    }
    
    public static NgramModel makeNgram(int order, File in)
    {
        NgramModel ngram = null;
        
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(in));
            String thisLine;        
            while ((thisLine = br.readLine()) != null) 
            { 
                String parseLine = StringNormalize.stringNormalize(thisLine);
                if (ngram == null)
                {
                    ngram = createNgram(order, parseLine);
                }
                else
                {
                    ngram = addToNgram(ngram, parseLine);
                }
            } 
        }
        catch(IOException ex)
        {
            Messages.info("makeNgram: " + ex.toString());
        }
        
        return ngram;
    }        
    
    public static void main(String[] args) throws FileNotFoundException 
    {
        NgramModel ngram = null;
        File fout = null;
        int order = 0;

        if (args.length == 0)
        {
            System.err.println("Usage: Ngram [-n NgramModelOrder]"
                    + " [-o OutputNgramFile]"
                    + " file1 file2 ....");
        } else
        {
            for (int k = 0; k < args.length; ++k)
            {
                String arg = args[k];

                if (arg.equals("-n"))
                {
                    order = Integer.parseInt(args[++k]);
                } else if (arg.equals("-o"))
                {
                    fout = new File(args[++k]);
                } else if (order != 0)
                {
                    File file = new File(arg);
                    ngram = makeNgram(order, file);                    
                }
            }
            
            if (fout != null)
            {
                ngram.save(fout);
            }
        }
    }
}
