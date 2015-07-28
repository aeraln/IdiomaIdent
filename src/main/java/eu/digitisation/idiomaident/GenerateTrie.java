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
import java.util.HashSet;

/**
 *
 * @author Impact
 */
public class GenerateTrie
{
    public static void generateTrie(File input, File output, int minStat, int maxStat)
    {
        NgramTrie trie = new NgramTrie();
        FileInputStream finput = null;
        InputStreamReader ireader = null;
        BufferedReader reader = null;
        
        try
        {
            
            finput = new FileInputStream(input);
            ireader = new InputStreamReader(finput, Charset.forName("UTF8"));
            reader = new BufferedReader(ireader);
            
            String line;
            
            while ((line = reader.readLine()) != null)
            {
                String[] fields = line.split(";");
                
                HashSet<String> languages = new HashSet();
                
                String ngram = fields[0];
                
                //English - 1;Spanish - 2;French - 3;Italian - 4;Portuguese - 5;Romanian - 6
                for (int l=1; l<7; l++)
                {
                    //if stat >= minstat the ngram is characteristic in this language
                    //if stat >= maxstat the ngram is characteristic in this language and not in other
                    double stat = Double.parseDouble(fields[l]);
                    if (stat >= minStat) //add language to the hashset                        
                    {
                        languages.add(language(l));
                    }
                    //is the only characteristic language
                    if(stat >= maxStat)
                    {
                        languages.clear();
                        languages.add(language(l));
                        break;
                    }
                }
                
                trie.addNgram(ngram, languages);
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
        finally
        {
            if(ireader!=null)
            {
                try
                {
                    ireader.close();                    
                } catch (IOException ex)
                {
                    System.out.println(ex.toString());
                }
            }
        }
        
        //save ngramtrie
        trie.saveTrie(output);                
    }
    
    private static String language(int pos)
    {
        switch(pos)
        {
            //English - 1;Spanish - 2;French - 3;Italian - 4;Portuguese - 5;Romanian - 6
            case 1:return ("en");
                
            case 2:return ("es");
                
            case 3:return ("fr");
                
            case 4:return ("it");
                
            case 5:return ("pt");
                
            case 6:return ("ro");
        }
        
        return null;
    }
    
    public static void main(String args[])
    {
        if (args.length < 4)
        {
            System.err.println("Usage: GenerateTrie"
                    + "input output minStat maxStat");
        } else
        {
            File inFile = new File(args[0]);
            File outFile = new File(args[1]);
            int minStat = Integer.parseInt(args[2]);
            int maxStat = Integer.parseInt(args[3]);
           
            if(inFile.exists() && !inFile.isDirectory())
            {                                
                generateTrie(inFile, outFile, minStat, maxStat);
            }
        }
    }
}
