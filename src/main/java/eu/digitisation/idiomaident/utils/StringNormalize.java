/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.digitisation.idiomaident.utils;

import eu.digitisation.text.StringNormalizer;
import java.text.Normalizer;

/**
 *
 * @author Impact
 */
public class StringNormalize
{
    public static String stringNormalize(String st)
    {
        String result;
        
        //remove tags
        result = RemoveTags.removeTags(st);
        
        //Normalize separated chars
        result = Normalizer.normalize(result, Normalizer.Form.NFC);
        
        //Remove upper-case letters
        //result = result.toLowerCase();
        
        //Remove punctuation
        //result = StringNormalizer.removePunctuation(result);
        
        //Remove special characters and numbers
        result = result.replaceAll("[\\[\\]/\"\\p{N},;)(-]", "");
        
        return result;
        
    }
}
