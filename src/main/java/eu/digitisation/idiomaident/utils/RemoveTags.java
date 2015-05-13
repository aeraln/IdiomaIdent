/*
 * Copyright (C) 2015 IMPACT Centre of Competence
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

/**
 *
 * @author Impact
 */
public class RemoveTags
{    
    
   public static String removeTags (String st)
   {        
        String noTags = st.replaceAll("<[^>]*>","");
        
        return noTags;
                     
   }
   
   /*
   public static String removeTags (File file) throws Exception
   {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ( (line=br.readLine()) != null) {
          sb.append(line);       
        }
        String noTags = sb.toString().replaceAll("<[^>]*>","");
        
        return noTags;
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(outfileName));
            writer.write(xml.getText(infile.getAbsolutePath()));
            writer.close();
                        
   }
   */
   
}

