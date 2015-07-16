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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Impact
 */
public class NgramTrieTest
{
    
    public NgramTrieTest()
    {
    }

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();
    
    /**
     * Test of addNgram method, of class NgramTrie.
     */
    @Test
    public void testAddNgram_String()
    {   
        HashSet<String> result = new HashSet(Arrays.asList("h","ho","hol","hola","a","ad","adi","adio","adios"));
                
        
        NgramTrie instance = new NgramTrie();
        instance.addNgram("hola");
        instance.addNgram("adios");
        
        Assert.assertEquals(result, instance.treeNgrams());
    }

    /**
     * Test of addNgram method, of class NgramTrie.
     */
    @Test
    public void testAddNgram_String_HashSet()
    {        
        
        HashSet<String> result = new HashSet(Arrays.asList("h","ho","hol","hola"));
        
        NgramTrie instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en","fr")));
        instance.addNgram("hol", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hola", new HashSet<>(Arrays.asList("es")));
        
        Assert.assertEquals(result, instance.treeNgrams());  
        
        result = new HashSet(Arrays.asList("h","ho","hol"));
        
        instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hol", new HashSet<>(Arrays.asList("es")));
        
        //this ngram whould not be inserted because her father have only one characteristic language
        instance.addNgram("hola", new HashSet<>(Arrays.asList("es")));
        
        Assert.assertEquals(result, instance.treeNgrams());
    }

    /**
     * Test of characteristicLang method, of class NgramTrie.
     */
    @Test
    public void testCharacteristicLang()
    {
        HashSet<String> result = new HashSet(Arrays.asList("es","en","fr"));
        
        NgramTrie instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en","fr")));
        instance.addNgram("hol", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hola", new HashSet<>(Arrays.asList("es")));
        
        Assert.assertEquals(result, instance.characteristicLang("ho"));  
        
        result = new HashSet(Arrays.asList("es"));
        
        instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hola", new HashSet<>(Arrays.asList("es")));                
        
        Assert.assertEquals(result, instance.characteristicLang("hola"));
        
        result = new HashSet(Arrays.asList("es"));
        
        instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hol", new HashSet<>(Arrays.asList("es")));                
        
        Assert.assertEquals(result, instance.characteristicLang("hola"));
    }  
    
    @Test
    public void testSerialization() throws IOException
    {
        HashSet<String> result = new HashSet(Arrays.asList("h","ho","hol","hola"));
        
        NgramTrie instance = new NgramTrie();
        instance.addNgram("ho", new HashSet<>(Arrays.asList("es","en","fr")));
        instance.addNgram("hol", new HashSet<>(Arrays.asList("es","en")));
        instance.addNgram("hola", new HashSet<>(Arrays.asList("es")));             
        
        File out = tempFolder.newFile("trie.tr");
        instance.saveTrie(out);
        
        NgramTrie trieReaded;
        
        trieReaded = NgramTrie.readTrie(out);
        
        Assert.assertEquals(result, trieReaded.treeNgrams());
    }
    
}
