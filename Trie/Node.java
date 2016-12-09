package Trie;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author schulace
 *  @version Dec 1, 2016
 */

public class Node
{

    private Term   data;
    private Node[] nexts;
    private boolean    words;
    private int prefixes;



    public Node(Term t)
    {
        data = t;
        nexts = new Node[26];
        words = false;
        prefixes = 0;
    }

    // ----------------------------------------------------------
    /**
     * @return the data
     */
    public Term getData()
    {
        return data;
    }

    // ----------------------------------------------------------
    /**
     * sets the weight
     *
     * @param weight
     *            the weight to set.
     */
    public void setWeight(int weight)
    {
        if (this.data != null)
        {
            this.data.setWeight(weight);
        }
    }

    // ----------------------------------------------------------
    /**
     * inserts a new node at a letter-based location in the present node.
     *
     * @param location
     *            the character index
     * @param toInsert
     *            the inserted node
     */
    public void insert(char location, Node toInsert)
    {
        if (location >= 'a' && location <= 'z')
        {
            nexts[location - 97] = toInsert;
        }
        if (location >= 'A' && location <= 'Z')
        {
            nexts[location - 10] = toInsert;
        }
    }


    // ----------------------------------------------------------
    /**
     * @param data
     *            the data to set
     */
    public void setData(Term data)
    {
        this.data = data;
    }


    // ----------------------------------------------------------
    /**
     * Create a new Node object.
     *
     * @param t
     *            the term
     */


    // ----------------------------------------------------------
    /**
     * @return the words
     */
    public boolean getWords()
    {
        return words;
    }


    // ----------------------------------------------------------
    /**
     * @param words the words to set
     */
    public void setWords(boolean words)
    {
        this.words = words;
    }


    // ----------------------------------------------------------
    /**
     * @return the prefixes
     */
    public int getPrefixes()
    {
        return prefixes;
    }


    // ----------------------------------------------------------
    /**
     * @param prefixes the prefixes to set
     */
    public void setPrefixes(int prefixes)
    {
        this.prefixes = prefixes;
    }


    // ----------------------------------------------------------
    /**
     * @param c
     *            character to access subnode
     * @return the subnode at c
     */
    public Node getNext(char c)
    {
        if (c >= 'a' && c <= 'z')
        {
            return nexts[c - 97];
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * @return a JSON representation of the node and all it's subnodes
     */
    public String altToString()
    {
        String s1 = data == null ? "" : this.data.toString();
        String s = "{ term: '" + s1 + "'";
        for (int i = 0; i < 26; i++)
        {
            if (nexts[i] != null)
            {
                s += ",";
                s += ((char)(i + 97)) + ":" + nexts[i].altToString();
            }

        }
//if (nexts[25] != null)
//{
//s += (char)(25+97) +':'+ nexts[25].toString();
//}
        s += "}";
        return s;
    }
}
