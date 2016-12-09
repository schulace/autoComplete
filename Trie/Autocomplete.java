package Trie;

import java.util.ArrayList;

// -------------------------------------------------------------------------
/**
 * a tree where nodes have 26 branches
 *
 * @author schulace
 * @version Nov 11, 2016
 */

public class Autocomplete
{

    // ----------------------------------------------------------
    /**
     * creates a new Autocomplete with an empty node as the head.
     */
    public Autocomplete()
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
     * adds a word to the trie, and also ensures that addTerm (private) is not
     * called with a null value for the node.
     *
     * @param strIn
     *            string to insert
     * @param weight
     *            weight of the term
     */
    public void addWord(String strIn, int weight)
    {
        if (strIn == null)
        {
            throw new NullPointerException("input can't be null");
        }
        if (getSubTrie(strIn) != null)
        {
            return;
        }
        if (strIn.length() > 0)
        {
            addTerm(
                new StringBuilder(strIn),
                root,
                new StringBuilder(strIn.length()),
                weight);
        }
    }


    // ----------------------------------------------------------
    /**
     * gets a tree with the given prefix
     *
     * @param prefix
     *            the prefix to look for.
     * @return a trie under a certain prefix
     */
    public Node getSubTrie(String prefix)
    {
        return getSubTrie(prefix, root);
    }


    // ----------------------------------------------------------
    /**
     * unused method @see Node.getNext()
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
            return location - 65;
        }
        // throw new IllegalArgumentException();
        return -1;
    }


    // ----------------------------------------------------------
    /**
     * gets a trie under a certain prefix
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
             * TODO comment above and decoment below to not return a null
             * pointer when a subNode doesn't exist for the current query.
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
    private void addTerm(
        StringBuilder s,
        Node n,
        StringBuilder currentPrefix,
        int weight)
    {
        n.setPrefixes(n.getPrefixes() + 1);
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
            n.setData(new Term(currentPrefix.toString(), weight));
            n.setWords(true);
            return;
        }
        /*
         * if the next node that this method would call is null, create a node
         * there and insert it into the Autocomplete.
         */
        if (n.getNext(s.charAt(0)) == null)
        {
            n.insert(s.charAt(0), new Node(null));
        }
        /*
         * recursively calls this method with term being 1 character shorter
         * from the front, the node being the next one according to the term's
         * current first character, and the currentPrefix being the
         * currentPrefix with the first character of term appended. (basically
         * moves a character from term's start to currentPrefix's end)
         */
        char c = s.charAt(0);
        s.delete(0, 1);
        currentPrefix.append(c);
        addTerm(s, n.getNext(c), currentPrefix, weight);
    }


    // ----------------------------------------------------------
    /**
     * @return a JSON String representation of this trie (root's toString)
     */
    public String toString()
    {
        return root.altToString();
    }


    /**
     * gets all terms with a specific query
     *
     * @param query
     *            the string to match
     * @return a stack of the possible terms in alphabetical order (i hope)
     */
    public ArrayList<Term> getSuggestions(String query)
    {
        if (query == null)
        {
            throw new NullPointerException("search term can not be null");
        }
        ArrayList<Term> s = new ArrayList<Term>();
        getTermsUnder(getSubTrie(query), s);
        return s;
    }


    /**
     * counts the number of words that start with the given prefix
     *
     * @param prefix
     *            the prefix to see starting with.
     * @return the number of words starting with prefix
     */
    public int countPrefixes(String prefix)
    {
        Node n = getSubTrie(prefix);
        if (n == null)
        {
            return 0;
        }
        return n.getPrefixes();
    }


    /**
     * gets all terms undder a node recursively
     *
     * @param n
     *            the node to look for terms under and in
     * @param s
     *            the list to add terms to
     */
    private void getTermsUnder(Node n, ArrayList<Term> s)
    {
        if (n == null)
        {
            return;
        }
        if (n.getData() != null)
        {
            if (n.getData().length() > 0)
            {
                s.add(n.getData());
            }
        }
        for (int i = 0; i < 26; i++)
        {
            getTermsUnder(n.getNext((char)(i + 97)), s);
        }
    }
}
