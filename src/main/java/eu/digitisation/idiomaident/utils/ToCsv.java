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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 *
 * @author Impact
 */
public class ToCsv
{

    public static String buildString(String line)
    {
        String[] sArray =
        {
            "", "0", "0", "0", "0", "0", "0", "0"
        };

        String[] trozos = line.split("\t");

        if (trozos != null)
        {

            sArray[0] = trozos[0] != null ? trozos[0] : "";

            for (int i = 2; i < trozos.length; i++)
            {
                String[] statics = trozos[i].split(":");
                
                switch (statics[0])
                {
                    case "0": sArray[1]=statics[1];
                        break;
                        
                    case "1": sArray[2]=statics[1];
                        break;
                        
                    case "2": sArray[3]=statics[1];
                        break;
                        
                    case "3": sArray[4]=statics[1];
                        break;
                        
                    case "4": sArray[5]=statics[1];
                        break;
                        
                    case "5": sArray[6]=statics[1];
                        break;
                        
                    case "-1": sArray[7]=statics[1];
                        break;    
                }
                
            }
            
            double total = Double.parseDouble(sArray[7]);
            
            //filtro
            if ( total < 1000)
                return null;
            
            if ( sArray[0].length() > 6 )
                return null;
            
            if ( sArray[0].matches(".*[¹º›»¨°�±“³£¶�²ª¬½'‰¤®«¼™!\"#$%&()©€*+,-./:;<=>?@\\^_`{|}~]+.*"))
                return null;
            
            if ( sArray[0].matches(".*\\{Digit}+.*"))
                return null;
            
            //porcentajes
            for (int i = 1; i<7; i++)
            {
                sArray[i] = String.format(Locale.ENGLISH, "%.3g", (Double.parseDouble(sArray[i]) / total) * 100);
            }
            
            
            StringBuilder sbuild = new StringBuilder();
            
            for (String trozo : sArray)
            {                
                sbuild.append(trozo);
                sbuild.append(';');
            }
            
            sbuild.replace(sbuild.lastIndexOf(";"), sbuild.length(), "\n");
            
            return sbuild.toString();

        }
        else
        {
            return null;
        }

    }

    public static void toCsv(File in, File out)
    {
        FileReader freader = null;
        BufferedReader buffreader = null;
        FileOutputStream fwriter = null;
        OutputStreamWriter buffwriter = null;

        try
        {
            freader = new FileReader(in);
            buffreader = new BufferedReader(freader);

            fwriter = new FileOutputStream(out);
            buffwriter = new OutputStreamWriter(fwriter, Charset.forName("UTF8"));

            String line;

            //buffwriter.write("String,English,Spanish,French,Italian,Portuguese,Romanian,Total\n");
            
            while ((line = buffreader.readLine()) != null)
            {
                if (!line.contains("\""))
                {
                    String sOut = buildString(line);
                    if(sOut != null)
                        buffwriter.write(sOut);
                }
            }

        } catch (IOException ex)
        {
            System.out.println(ex.toString());
        } finally
        {
            if (freader != null)
            {
                try
                {
                    freader.close();
                } catch (IOException ex)
                {
                    System.out.println("Error finaly freader");
                    System.out.println(ex.toString());
                }
            }

            if (buffwriter != null)
            {
                try
                {
                    buffwriter.flush();
                    buffwriter.close();
                } catch (IOException ex)
                {
                    System.out.println("Error finaly buffwriter");
                    System.out.println(ex.toString());
                }
            }
            
            if (fwriter != null)
            {
                try
                {                    
                    fwriter.close();
                } catch (IOException ex)
                {
                    System.out.println("Error finaly fwriter");
                    System.out.println(ex.toString());
                }
            }                        

        }
    }

    public static void main(String args[])
    {
        if (args.length < 2)
        {
            System.err.println("Usage: ToCsv"
                    + "inFile outFile");
        } else
        {
            File inFile = new File(args[0]);
            File outFile = new File(args[1]);
           
            if(inFile.exists() && !inFile.isDirectory())
            {                                
                toCsv(inFile, outFile);
            }
        }
    }
}
