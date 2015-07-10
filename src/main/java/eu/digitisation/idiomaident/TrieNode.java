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

/**
 *
 * @author Impact
 */
public class TrieNode
{

    private Character gliph;
    private HashMap<Character, TrieNode> childrens;

    /**
     * Constructor
     */
    public TrieNode(Character g)
    {
        gliph = g;
        childrens = null;
    }

    /**
     * Adds a ngram to the Trie
     *
     * @param ngram
     */
    public void addNgram(String ngram)
    {
        if (!ngram.isEmpty())
        {
            if (childrens == null)
            {
                childrens = new HashMap<>();
            }

            Character g = ngram.charAt(0);

            if (!childrens.containsKey(g))
            {
                childrens.put(g, new TrieNode(g));
            }

            childrens.get(g).addNgram(ngram.substring(1));
        }

    }
}
