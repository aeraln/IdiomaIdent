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
package eu.digitisation.idiomaident.utils;

import eu.digitisation.text.WordScanner;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Impact
 */
public class CorpusFilter
{

    private static HashMap<String, File> folderList;
    private static HashMap<String, String> actualFile;

    private static HashMap<String, File> getFolderList(File parent)
    {
        File[] folders = parent.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String name)
            {
                return file.isDirectory();
            }
        });

        HashMap<String, File> map = new HashMap<>();

        for (File folder : folders)
        {
            map.put(folder.getName(), folder);
        }

        return map;
    }

    private static HashSet getPosibleNames(String text)
    {
        HashSet<String> posibleNames = new HashSet();
        
        try
        {
            WordScanner scanner = new WordScanner(text);
            
            String word = "";
            
            while ( (word = scanner.nextWord()) != null )
            {
                //check the first letter
                if(word.matches("\\p{Upper}+.*"))
                {
                    posibleNames.add(word);
                }
            }
            
            return posibleNames;
        } 
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }                
        
        return null;
    }
    
    public static void filter(File in, File out, String refLanguage)
    {
        actualFile = new HashMap<>();

        //first obtain the list of language folders
        folderList = getFolderList(in);

        //foreach file in the folder of the reference language        
        //Last, we search for this word in the others language and if occurs in almost all
        //remove all occurrences (personal names).
        File refLangFolder = folderList.get(refLanguage);

        File[] filesInFolder = refLangFolder.listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File file, String name)
            {
                return name.endsWith(".txt");                
            }
        });

        int num = 0;
        for (File textFile : filesInFolder)
        {
            String fileName = textFile.getName();
            actualFile.clear();
            num++;
            System.out.println("Processing " + num +" of " + filesInFolder.length);

            try
            {
                //First we populate the actualFile hashmap with the same file
                //of the rest of languages, normalizing the text and removing the
                //special characters and the xml tags.

                for (String language : folderList.keySet())
                {
                    File languageFile = new File(folderList.get(language).getAbsolutePath() + "/" + fileName);

                    if (languageFile.exists())
                    {
                        String text = FileUtils.readFileToString(languageFile, Charset.forName("UTF-8"));
                        text = StringNormalize.stringNormalize(text);

                        actualFile.put(language, text);
                        
                    }

                }

            } catch (IOException ex)
            {
                System.err.println("Error: " + ex.toString());
            }
            
            //Then we search in the text of the reference language for words with the 
            //first caracter in uppercase.
            HashSet<String> posibleNames;
            
            posibleNames = getPosibleNames(actualFile.get(refLanguage));

            //System.out.println(posibleNames.toString());
            
            eliminateNames(posibleNames, actualFile);
            
            for (String lang : actualFile.keySet())
            {
                try
                {
                    //Copy the filter file in out folder
                    FileUtils.writeStringToFile(new File(out.getAbsolutePath() + "/" + lang + "/" + fileName), actualFile.get(lang));
                } 
                catch (IOException ex)
                {
                    System.err.println("Error: " + ex.toString());
                }
            }
            
        }
    }

    private static void eliminateNames(HashSet<String> posibleNames, HashMap<String, String> actualFile)
    {
        for (String name : posibleNames)
        {
            //Check if the name is in allmost the others languages
            double totalLenguages = actualFile.size();
            
            int goal = (int) (totalLenguages * 0.8); //80% of the lenguages
            
            int contains = 0;
            
            for (String lang : actualFile.keySet())
            {
                if(actualFile.get(lang).contains(name))
                {
                    contains++;
                }
            }
            
            if (contains >= goal) //Eliminate the name
            {
                //System.out.println(name);
                
                for (String lang : actualFile.keySet())
                {
                    String text = actualFile.get(lang);
                    
                    String replacement = "(\\p{Space}|\\p{Punct})+" + name + "(\\p{Space}|\\p{Punct})+";
                    
                    text = text.replaceAll(replacement, " ");
                    
                    actualFile.put(lang, text);
                }
            }
            
            
        }
    }
    
    public static void main(String args[])
    {
        if (args.length < 2)
        {
            System.err.println("Usage: CorpusFilter"
                    + "inFolder outFolder");
        } else
        {
            File inFile = new File(args[0]);
            File outFile = new File(args[1]);
           
            if(inFile.exists() && inFile.isDirectory())
            {
                if (!outFile.exists())
                {
                    outFile.mkdir();
                }
                
                CorpusFilter.filter(inFile, outFile, "es");
            }
        }
    }

    

    

}
