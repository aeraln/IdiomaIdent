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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Impact
 */
public class DetectLangTest
{
    
    public DetectLangTest()
    {
    }

    @Test
    public void testGenerateNgrams()
    {        
        ArrayList<String> result = new ArrayList<>(Arrays.asList("h", "ho", "o", "hol", "ol", "l", "ola", "la", "a"));
        ArrayList<String> result2 = new ArrayList<>(Arrays.asList("l", "la", "a", "lal", "al", "l", "ala", "la", "a"));
        ArrayList<String> ngrams;
        
        DetectLang dtector = new DetectLang();
        
        Method method;                
        
        try
        {
            method = DetectLang.class.getDeclaredMethod("generateNgrams", String.class, int.class);
            
            method.setAccessible(true);
        
            ngrams = (ArrayList) method.invoke(dtector, "hola", 3);
                        
            Assert.assertEquals(result,ngrams);
            
            ngrams = (ArrayList) method.invoke(dtector, "lala", 3);
                        
            Assert.assertEquals(result2,ngrams);
            
        } 
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            System.out.println("Exception " + ex.toString());
            Assert.fail();
        }                                 
    }
    
    @Test
    public void testDoVoting()
    {        
        
        HashMap<String, Integer> votes;
        DetectLang dtector = new DetectLang();
        
        NgramTrie trie = mock(NgramTrie.class);
        when(trie.characteristicLang("h")).thenReturn(new HashSet<>(Arrays.asList("es", "it", "en", "ro")));
        when(trie.characteristicLang("ho")).thenReturn(new HashSet<>(Arrays.asList("es", "it", "ro")));
        when(trie.characteristicLang("hol")).thenReturn(new HashSet<>(Arrays.asList("es", "it")));
        when(trie.characteristicLang("hola")).thenReturn(new HashSet<>(Arrays.asList("es")));
        
        Method method;                
        dtector.setNgrams(trie);
        HashMap<String, Integer> result = new HashMap<String, Integer>()
        {{
            put("en",1);
            put("it",3);
            put("ro",2);
            put("es",4);
        }};
        
        try
        {
            ArrayList<String> ngrams = new ArrayList<>(Arrays.asList("h", "ho", "hol", "hola"));
            method = DetectLang.class.getDeclaredMethod("doVoting", ngrams.getClass());
            
            method.setAccessible(true);
        
            votes = (HashMap<String, Integer>) method.invoke(dtector, ngrams); 
            
            Assert.assertEquals(votes, result);
            
        } 
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            System.out.println("Exception " + ex.toString());
            Assert.fail();
        }    
        
    }
    
    @Test
    public void testMostVoted()
    {
        //HashMap<String, Integer> votes       
        DetectLang dtector = new DetectLang();                
        
        Method method;                
        
        HashMap<String, Integer> votes = new HashMap<String, Integer>()
        {{
            put("en",1);
            put("it",3);
            put("ro",2);
            put("es",4);
        }};
        
        String result = "es";
        
        try
        {
            
            method = DetectLang.class.getDeclaredMethod("mostVoted", HashMap.class);
            
            method.setAccessible(true);
        
            String lang = (String) method.invoke(dtector, votes); 
            
            Assert.assertEquals(lang, result);
            
        } 
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            System.out.println("Exception " + ex.toString());
            Assert.fail();
        }
        
    }
    
    /**
     * Test of language method, of class DetectLang.
     */
    @Test
    public void testLanguage()
    {
        String result = "es";
        DetectLang dtector = new DetectLang();
        
        NgramTrie trie = mock(NgramTrie.class);
        when(trie.characteristicLang("h")).thenReturn(new HashSet<>(Arrays.asList("es", "it", "en", "ro")));
        when(trie.characteristicLang("ho")).thenReturn(new HashSet<>(Arrays.asList("es", "it", "ro")));
        when(trie.characteristicLang("hol")).thenReturn(new HashSet<>(Arrays.asList("es", "it")));
        when(trie.characteristicLang("hola")).thenReturn(new HashSet<>(Arrays.asList("es")));
        when(trie.characteristicLang(anyString())).thenReturn(new HashSet<>(Arrays.asList("es", "it", "en", "ro")));               
                              
        dtector.setNgrams(trie);
                   
        String lang = dtector.language("hola");
        
        Assert.assertEquals(result, lang);
        
    }
    
}
