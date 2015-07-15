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
public class TrieNode
{

    private Character gliph;
    private HashMap<Character, TrieNode> childrens;
    private HashSet<String> languages;

    /**
     * Constructor
     */
    public TrieNode(Character g)
    {
        gliph = g;
        childrens = null;
        languages = null;
    }   
    
    public void addNgram(String ngram, HashSet langs)
    {
        
        if (!ngram.isEmpty())
        {            
            //this ngram and all his posible descendants has an only characteristic language, so stop the insertion
            if(this.languages!=null && this.languages.size()==1)
            {
                return;
            }
            
            if (childrens == null)
            {
                childrens = new HashMap<>();
            }

            Character g = ngram.charAt(0);

            if (!childrens.containsKey(g))
            {
                childrens.put(g, new TrieNode(g));
            }

            childrens.get(g).addNgram(ngram.substring(1), langs);
        }
        else
        {
            if (langs != null)
                languages = new HashSet<>(langs);
        }

    }
    
    public HashSet characteristicLang(String ngram)
    {
        if(languages != null && languages.size() == 1)
        {
            //this ngram and all his posible descendants has an only characteristic language, so stop the search
            return languages;
        }
        
        if (!ngram.isEmpty())
        {
            Character g = ngram.charAt(0);
            
            if(childrens.containsKey(g))
            {
                return childrens.get(g).characteristicLang(ngram.substring(1));
            }   
            else
            {
                return null;
            }
        }
        else
        {
            //last character of ngram            
            return languages;
        }            
    }
    
    public HashSet<String> nodeNgrams(String ngram)
    {
        HashSet<String> ngrams = new HashSet<>();
        
        String nodeNgram = ngram + this.gliph;
        
        ngrams.add(nodeNgram);
        
        if (childrens != null)
        {
            for (Character ch: childrens.keySet())
            {
                ngrams.addAll(childrens.get(ch).nodeNgrams(nodeNgram));
            }
        }
        
        return ngrams;
    }
    
}
