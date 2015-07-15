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

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Impact
 */
public class NgramTrie
{

    HashMap<Character, TrieNode> childrens;

    public NgramTrie()
    {
        childrens = null;
    }

    public void addNgram(String ngram)
    {
        this.addNgram(ngram, null);                     
    }
    
    public void addNgram(String ngram, HashSet langs)
    {
        if(!ngram.isEmpty())
        {
            if (childrens == null)
            {
                childrens = new HashMap<>();
            }
            
            Character gliph = ngram.charAt(0);
            
            if(!childrens.containsKey(gliph))
            {
                childrens.put(gliph, new TrieNode(gliph));                
            }            
            
            childrens.get(gliph).addNgram(ngram.substring(1), langs);
        }                        
    }
    
    public HashSet characteristicLang(String ngram)
    {
        if (!ngram.isEmpty())
        {
            Character gliph = ngram.charAt(0);
            
            if(childrens.containsKey(gliph))
            {
                return childrens.get(gliph).characteristicLang(ngram.substring(1));
            }   
            else
            {
                return null;
            }
        }
        else
            return null;
    }
    
    public HashSet<String> treeNgrams()
    {
        HashSet<String> ngrams = new HashSet<>();
        
        for (Character ch: childrens.keySet())
        {
            ngrams.addAll(childrens.get(ch).nodeNgrams(""));
        }
        
        return ngrams;
            
    }

}
