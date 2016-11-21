package code;

// -------------------------------------------------------------------------
/**
 * a tree where nodes have 26 branches
 *
 * @author schulace
 * @version Nov 11, 2016
 */

public class AutoComplete
{
    // -------------------------------------------------------------------------
    /**
     * A node class for the AutoComplete (like a binary node but a 26-ary node)
     *
     * @author schulace
     * @version Nov 13, 2016
     */
    public class Node
    {

        private Term   data;
        private Node[] nexts;
        private int    prefixTo;


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
         * figures out how many words the current node is a prefix to
         */
        public void setPrefixes()
        {
            this.prefixTo = 0;
            for (int x = 0; x < 26; x++)
            {
                if (nexts[x] != null)
                {
                    this.prefixTo += nexts[x].getPrefixes();
                }
            }
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
         * gets the number of prefixes
         *
         * @return number of words prefixed by this one
         */
        public int getPrefixes()
        {
            setPrefixes();
            return this.prefixTo + (this.data != null ? 1 : 0);
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
        public Node(Term t)
        {
            data = t;
            nexts = new Node[26];
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
            if (c >= 'A' && c <= 'Z')
            {
                return nexts[c - 10];
            }
            return null;
        }


        // ----------------------------------------------------------
        /**
         * @return a JSON representation of the node and all it's subnodes
         */
        public String toString()
        {
            String s1 = data == null ? "" : this.data.toString();
            String s = "{ term: '" + s1 + "'";
            for (int i = 0; i < 26; i++)
            {
                if (nexts[i] != null)
                {
                    s += ",";
                    s += ((char)(i + 97)) + ":" + nexts[i].toString();
                }

            }
// if (nexts[25] != null)
// {
// s += (char)(25+97) +':'+ nexts[25].toString();
// }
            s += "}";
            return s;
        }
    };


    // ----------------------------------------------------------
    /**
     * creates a new AutoComplete with an empty node as the head.
     */
    public AutoComplete()
    {
        root = new Node(null);
    }

    private Node root;


    // ----------------------------------------------------------
    /**
     * @return pretty much just for tests. gets root.
     */
    public Node getRoot()
    {
        return this.root;
    }


    // ----------------------------------------------------------
    /**
     * Adds a term to the AutoComplete
     *
     * @param t
     *            the term to add
     */
    public void addTerm(Term t)
    {
        if (t == null || t.length() == 0)
        {
            return;
        }
        addTerm(t, root, "");
    }


    // ----------------------------------------------------------
    /**
     * adds a word to the trie
     *
     * @param strIn
     *            string to insert
     * @param weight
     *            weight of the term
     */
    public void addWord(String strIn, int weight)
    {
        addTerm(new Term(strIn, weight));
    }


    // ----------------------------------------------------------
    /**
     * gets a tree with the given prefix
     *
     * @param prefix
     *            unused.
     * @return unused
     */
    public Node getSubTrie(String prefix)
    {
        return getSubTrie(prefix, root);
    }


    // ----------------------------------------------------------
    /**
     * unused method
     *
     * @param location
     *            a character
     * @return the index of a character with a as 0
     */
    public int getCharIndex(char location)
    {
        if (location >= 'a' && location <= 'z')
        {
            return location - 97;
        }
        if (location >= 'A' && location <= 'Z')
        {
            return location - 10;
        }
        return -1;
    }


    // ----------------------------------------------------------
    /**
     * never gets used dont bother I wrote something different this is for the
     * api.
     *
     * @param prefix
     *            part of word already done with
     * @param n
     *            the node to insert at
     * @return the subtrie where the prefix would be placed
     */
    private Node getSubTrie(String prefix, Node n)
    {
        if (prefix.length() == 0)
        {
            return n;
        }
        if (n.getNext(prefix.charAt(0)) == null)
        {
            return null;
/*
 * TODO comment above and decoment below to not return a null pointer when a
 * subNode doesn't exist for the current query.
 */
            // n.insert(prefix.charAt(0), new Node(null));
        }
        return getSubTrie(
            prefix.substring(1, prefix.length()),
            n.getNext(prefix.charAt(0)));

    }


    // ----------------------------------------------------------
    /**
     * This method's purpose is to insert a term into the trie. It does this by
     * removing data from the term object and adding it on to the currentPrefix
     * variable, eventually returning a new Term made from the prefix variable.
     * inputs are relatively sanitized because this will never be passed a null
     * value for root and ensures that it will also never pass itself a null
     * value for root. the method that calls this also ensures that term isn't 0
     * or of length 0.
     *
     * @param s
     *            the term to be added in (should probably use a string tbh but
     *            I'm too lazy)
     * @param n
     *            the node to add the term to
     * @param currentPrefix
     *            the letters preceeding this insertion in the trie
     */
    private void addTerm(Term s, Node n, String currentPrefix)
    {
        /*
         * if the length is 0 and the currently examined node doesn't have a
         * term, then the term that was passed belongs at this node. Then
         * proceeds to create a new term from the currentPrefix (which is what
         * the term was originally) and the term's old weight. returns when done
         * because the term has been inserted.
         */
        if (s.length() == 0 /* && n.getData() == null */)
        {
            /*
             * TODO remove n.getData() == null depending on desired behavior for
             * when it encounters something there (default is to leave things be
             * and not overwrite)
             */
            n.setData(new Term(currentPrefix, s.getWeight()));
            return;
        }
        /*
         * if the next node that this method would call is null, create a node
         * there and insert it into the AutoComplete.
         */
        if (n.getNext(s.getQuery().charAt(0)) == null)
        {
            n.insert(s.getQuery().charAt(0), new Node(null));
        }
        /*
         * recursively calls this method with term being 1 character shorter
         * from the front, the node being the next one according to the term's
         * current first character, and the currentPrefix being the
         * currentPrefix with the first character of term appended. (basically
         * moves a character from term's start to currentPrefix's end)
         */
        addTerm(
            new Term(s.getQuery().substring(1), s.getWeight()),
            n.getNext(s.getQuery().charAt(0)),
            currentPrefix + s.getQuery().charAt(0));
    }


    // ----------------------------------------------------------
    /**
     * not used yet, may be useful later
     *
     * @param c
     *            character ot check
     * @return whether this will return a valid value when run through Node's
     *         getNext()
     */
    @SuppressWarnings("unused")
    private boolean isValidCharacter(char c)
    {
        return (c >= 'A' && c <= 'Z') || (c >= 'A' && c <= 'Z');
    }


    // ----------------------------------------------------------
    /**
     * @return a JSON String representation of this trie (root's toString)
     */
    public String toString()
    {
        return root.toString();
    }
}
