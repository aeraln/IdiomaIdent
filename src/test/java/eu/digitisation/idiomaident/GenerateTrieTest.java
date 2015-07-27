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
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author Impact
 */
public class GenerateTrieTest
{       

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();
    
    /**
     * Test of generateTrie method, of class GenerateTrie.
     */
    @Test
    public void testGenerateTrie()
    {
        try
        {
            ClassLoader classLoader = getClass().getClassLoader();
            File input = new File(classLoader.getResource("sample.csv").getFile());
            File output = tempFolder.newFile("trie.tr");
            
            GenerateTrie.generateTrie(input, output, 10, 80);
            
            NgramTrie trie = NgramTrie.readTrie(output);                        
            
            //English - 1;Spanish - 2;French - 3;Italian - 4;Portuguese - 5;Romanian - 6
            /*
            salva;0,652;27,5;0,162;36,4;32,1;3,21;23459
            salvo;0,0434;52,6;0,00;23,6;23,7;0,00;2304
             salva;0,659;27,3;0,164;36,8;31,9;3,24;23223
            salvar;0,00;81,2;0,00;10,7;9,0;0,00;4741
            salvat;15,00;0,00;15,50;0,00;0,00;60,50;1039
            */
            
            HashSet<String> result = new HashSet(Arrays.asList("es","it","pt"));
            
            Assert.assertEquals(result, trie.characteristicLang("salva"));                        
            
            Assert.assertEquals(result, trie.characteristicLang("salvo"));                        
            
            Assert.assertEquals(result, trie.characteristicLang(" salva"));
            
            result = new HashSet(Arrays.asList("es"));
            
            Assert.assertEquals(result, trie.characteristicLang("salvar"));
            
            result = new HashSet(Arrays.asList("en","fr","ro"));
            
            Assert.assertEquals(result, trie.characteristicLang("salvat"));
            
            Assert.assertEquals(null, trie.characteristicLang("salvi"));
            
            
        } catch (IOException ex)
        {
            System.out.println(ex.toString());
            Assert.fail();
        }
    }
    
}
