package tests;

import code.Term;
import code.AutoComplete;
import code.Node;

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

    private AutoComplete tr;

    /**
   */


    public void setUp()
    {
        tr = new AutoComplete();
    }

    // ----------------------------------------------------------
    /**
     * tests the addition of stuff to the trie
     */
    public void testThing()
    {
        tr.addWord( "as", 20);
        tr.addWord( "asdf", 4);
        tr.addWord( "qqq", 2);
        assertErrorThrown(
            () -> tr.addWord( null, 2),
            NullPointerException.class);
        tr.addWord( "", 4);
        code.Node root = tr.getRoot();
        tr.addWord( "money", 3456);
        tr.addWord( "zebra", 3);
        tr.addWord( "aqwer", 4);
        System.out.println(tr.toString());
        assertEquals("zebra", tr.getSubTrie("zebra").getData().getQuery());
        assertNull(tr.getSubTrie("s"));
        assertEquals(6, root.getPrefixes());
    }

    /**
     * tests the finder and prefix counter.
     */
    public void testFind()
    {
        tr.addWord( "as", 20);
        tr.addWord( "asdf", 4);
        tr.addWord( "qqq", 2);
        assertErrorThrown(
            () -> tr.addWord( null, 2),
            NullPointerException.class);
        tr.addWord( "", 4);
        Node root = tr.getRoot();
        tr.addWord( "money", 3456);
        tr.addWord( "zebra", 3);
        tr.addWord( "aqwer", 4);
        System.out.println(tr.getSuggestions(""));
        assertEquals(2, tr.countPrefixes("as"));
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
        assertEquals(1, Term.byPrefixOrder(4).compare(ts, tb));
        tb = new Term("no thanks", 3);
        assertEquals('h' - 'n', Term.byPrefixOrder(1000).compare(ts, tb));
        assertEquals(-1, Term.byReverseWeightOrder().compare(ts, tb));
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
