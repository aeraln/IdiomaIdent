/*
 * Copyright (C) 2015 Impact
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package eu.digitisation.idiomaident;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 *
 * @author Impact
 */
public class DoTest
{        
    public static void doTest(File trie, File samples)
    {        
        
        DetectLang detector = new DetectLang(trie);
        
        //read the samples
        FileInputStream finput = null;
        InputStreamReader ireader = null;
        BufferedReader reader = null;
        
        /*
        FileInputStream finput = null;
        InputStreamReader ireader = null;
        BufferedReader reader = null;
        
        try
        {
            
            finput = new FileInputStream(input);
            ireader = new InputStreamReader(finput, Charset.forName("UTF8"));
            reader = new BufferedReader(ireader);
        */
        
        int total = 0;
        int corrects = 0;
        
        try
        {
            finput = new FileInputStream(samples);
            ireader = new InputStreamReader(finput, Charset.forName("UTF8"));
            reader = new BufferedReader(ireader);
            
            String sample;
            
            while ( (sample = reader.readLine()) != null)
            {
                String[] splits = sample.split(";");
                String text = splits[0];
                String lang = splits[1];
                
                //System.out.println(text);
                String prediction = detector.language(text);
                
                if (prediction.equals(lang))
                {
                    corrects++;
                }
                
                total++;
            }
            
        }
        catch(IOException ex)
        {
            System.out.println(ex.toString());
        }
        finally
        {
            if (ireader != null)
            {
                try
                {
                    ireader.close();                    
                } 
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        
        float porcent = ((float)corrects/(float)total)*100;
        
        System.out.println("Corrects: " + corrects + " Samples: " + total + " " + porcent +"% of success"); 
    }
    
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("Usage: DoTest"
                    + "trie samples");
        } else
        {
            File trie = new File(args[0]);
            File samples = new File(args[1]);            
           
            if(trie.exists() && !trie.isDirectory() && samples.exists() && !samples.isDirectory())
            {                                
                doTest(trie, samples);
            }
        }
    }
    
}
