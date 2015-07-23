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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.james.mime4j.dom.datetime.DateTime;

/**
 *
 * @author Impact
 */
public class ExtractTestSamples
{
    
    private static void extractSamples(int numSamples, File inFolder, File outFile)
    {       
        FileOutputStream fileOS = null;
        OutputStreamWriter outSWriter = null;
        
        try
        {
            fileOS = new FileOutputStream(outFile);
            outSWriter = new OutputStreamWriter(fileOS, Charset.forName("UTF8"));
            
            int num = 0;
            while (num < numSamples)            
            {
                String sample = nextSample(inFolder);
                
                if (sample == null)
                    continue;
                
                outSWriter.write(sample);
                num++;
                System.out.println("Generating " + num + " of " + numSamples + " samples");
            }                        
            
            outSWriter.flush();
            
        }
        catch(IOException ex)
        {
            System.out.println(ex.toString());
        }
        finally
        {
            try
            {
                if(outSWriter!=null)
                {
                    outSWriter.close();
                }
            }
            catch(IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
                
    }
    
    private static String nextSample(File inFolder)
    {        
        File[] langFolders = inFolder.listFiles();
        
        Date now = new Date();
        Random rand = new Random(now.getTime());
        
        //get a ramdom language       
        int numLangs = langFolders.length;        
        int chosenLang = rand.nextInt(numLangs);
        
        String lang = langFolders[chosenLang].getName();
        
        //get a ramdom file from the folder        
        File[] langFiles = langFolders[chosenLang].listFiles();
        int numFiles = langFiles.length;        
        int chosenFile = rand.nextInt(numFiles);
        
        File langFile = langFiles[chosenFile];
        
        //open and read the file
        try
        {
            String text = FileUtils.readFileToString(langFile, Charset.forName("UTF-8"));
            
            text = StringNormalize.stringNormalize(text);
            
            text = text.replaceAll("[\\n.,:;]", " ");
            text = text.trim();
            
            if (text.replaceAll("[\\p{Space}]+","").length() < 20)
            {
                return null;
            }
            
            //split the text in words
            String[] words = text.split("[\\p{Space}]+");
            
            boolean correct = false;
            StringBuilder sampleBuild = null;
            
            while(!correct)
            {
                //chosen the initial position to read the words
                int actualWord = rand.nextInt(words.length);
                correct = true;
                
                sampleBuild = new StringBuilder();                
                while (sampleBuild.length() < 20)
                {
                    if(actualWord < words.length)
                    {
                        sampleBuild.append(words[actualWord]);
                        sampleBuild.append(" ");
                    }
                    else
                    {
                        correct = false;
                        break;
                    }
                    
                    actualWord++;
                }                                            
            }
            
            //complete the sample
            sampleBuild.insert(sampleBuild.length()-1,";");
            sampleBuild.append(lang);
            sampleBuild.append("\n");
            
            return sampleBuild.toString();
            
        }
        catch(IOException ex)
        {
            System.out.println(ex.toString());
        }
        
        return null;
    }

    
    
    public static void main(String args[])
    {
        if (args.length < 3)
        {
            System.err.println("Usage: ExtractTextSamples "
                    + "numSamples inFolder outCsvText");
        } else
        {
            int numSamples = Integer.parseInt(args[0]);
            File inFolder = new File(args[1]);
            File outFile = new File(args[2]);
           
            if(inFolder.exists() && inFolder.isDirectory())
            {   
                extractSamples(numSamples, inFolder, outFile);
            }
        }
    }

    
    
    
}
