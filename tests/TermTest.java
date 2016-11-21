package tests;

import code.Term;
import code.AutoComplete;
import code.AutoComplete.Node;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 *
 * @author schulace
 * @version Nov 13, 2016
 */

public class TermTest
    extends student.TestCase
{

    private Term t;
    private AutoComplete tr;


    public void setUp()
    {
        t = new Term("asdf", 1000);
        tr = new AutoComplete();
    }


    // ----------------------------------------------------------
    /**
     * tests the addition of stuff to the trie
     */
    public void testThing()
    {
        Term st = new Term("asdfa", 3000);
        tr.addTerm(st);
        tr.addTerm(t);
        tr.addTerm(new Term("as", 20));
        tr.addTerm(new Term("asdf", 4));
        tr.addTerm(new Term("qqq", 2));
        assertErrorThrown(
            () -> tr.addTerm(new Term(null, 2)),
            NullPointerException.class);
        tr.addTerm(new Term("", 4));
        Node root = tr.getRoot();
        tr.addTerm(new Term("money", 3456));
        tr.addTerm(new Term("zebra", 3));
        tr.addTerm(new Term("aqwer", 4));
        System.out.println(tr.toString());
        assertEquals("zebra", tr.getSubTrie("zebra").getData().getQuery());
        assertNull(tr.getSubTrie("s"));
        assertEquals(7, root.getPrefixes());
    }

        // ----------------------------------------------------------
        /**
         * test for comparators of term
         */
        public void testComparators()
        {
            Term ts = new Term("hello", 4);
            Term tb = new Term("hell", 3);
            assertEquals(0, Term.byPrefixOrder(1).compare(ts, tb));
            assertEquals(1, Term.byPrefixOrder(4).compare(ts,tb));
            tb = new Term("no thanks", 3);
            assertEquals('h'-'n', Term.byPrefixOrder(1000).compare(ts, tb));
            assertEquals(-1, Term.byReverseWeightOrder().compare(ts,tb));
        }
    private boolean assertErrorThrown(Runnable r, Class<?> exception)
    {
        Exception ex = null;
        try
        {
            r.run();
        }
        catch (Exception e)
        {
            ex = e;
        }
        return exception.isInstance(ex);
    }
}
