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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Impact
 */
public class DetectLang
{
    private NgramTrie ngrams;
    private final int TAMNGRAM = 5;
    
    public DetectLang()
    {
        ngrams = null;
    }
    
    public DetectLang(File trieFile)
    {
        ngrams = NgramTrie.readTrie(trieFile);
    }
    
    public void setNgrams(NgramTrie trie)
    {
        ngrams = trie;
    }
    
    public String language(String text)
    {               
        if (ngrams != null)        
        {
            ArrayList<String> textNgrams = generateNgrams(text, TAMNGRAM);

            //voting
            HashMap<String, Integer> votes = doVoting(textNgrams);

            //return the most voted

            return mostVoted(votes);        
        }
        else
        {
            return null;
        }
    }

    private ArrayList<String> generateNgrams(String text, int TAMNGRAM)
    {        
        ArrayList<String> ngrams = null;
        
        if (text.length() < 1) 
        {
            throw new IllegalArgumentException("Cannot extract n-grams from empty word");
        } 
        else 
        {   
            ngrams = new ArrayList<>();
            for (int high = 1; high <= text.length(); ++high) 
            {
                for (int low = Math.max(0, high - TAMNGRAM); low < high; ++low) 
                {
                    String s = text.substring(low, high);
                    ngrams.add(s);
                }
            }
        }
    
        return ngrams;
    }

    private HashMap<String, Integer> doVoting(ArrayList<String> textNgrams)
    {
        HashMap<String, Integer> votes = new HashMap<>();
        
        HashSet<String> langs;
        
        for (String ngram : textNgrams)
        {
            langs = ngrams.characteristicLang(ngram);
            for (String lang: langs)
            {
                if (votes.containsKey(lang))
                {
                    votes.put(lang, votes.get(lang)+1);                    
                }
                else
                {
                    votes.put(lang, 1);
                }
            }
        }
        
        return votes;
    }

    private String mostVoted(HashMap<String, Integer> votes)
    {
        String best = "";
        int nVotes = 0;
        
        for (String lang: votes.keySet())
        {
            int langVote = votes.get(lang);
            if ( langVote >= nVotes )
            {
                best = lang;
                nVotes = langVote;
            }
        }
        
        return best;
    }
    
}
