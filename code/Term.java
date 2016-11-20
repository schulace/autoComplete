package code;

import java.util.Comparator;

public class Term
    implements Comparable<Term>
{
    private String query;
    private int  weight;


    // ----------------------------------------------------------
    /**
     * Create a new Term object.
     *
     * @param query
     *            the queried string
     * @param weight
     *            the weight of this particular search
     */
    public Term(String query, int weight)
    {
        if (query == null)
        {
            throw new NullPointerException(
                "can't create a Term with a null query");
        }
        this.query = query;
        this.weight = weight;
    }


    // ----------------------------------------------------------
    /**
     * @return the query
     */
    public String getQuery()
    {
        return query;
    }


    // ----------------------------------------------------------
    /**
     * @param query
     *            the query to set
     */
    public void setQuery(String query)
    {
        this.query = query;
    }


    // ----------------------------------------------------------
    /**
     * @return the weight
     */
    public int getWeight()
    {
        return weight;
    }


    // ----------------------------------------------------------
    /**
     * @param weight
     *            the weight to set
     */
    public void setWeight(int weight)
    {
        this.weight = weight;
    }


    // ----------------------------------------------------------
    /**
     * gest the length of the query's string
     *
     * @return the lengths of the query string
     */
    public int length()
    {
        return query.length();
    }


    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     *
     * @return a comparator for these things.
     */
    public static Comparator<Term> byReverseWeightOrder()
    {
        return new Comparator<Term>() {

            @Override
            public int compare(Term o1, Term o2)
            {
                return o1.getWeight() - o2.getWeight();
            }

        };
    }


    // Compares the two terms in lexicographic order but using only the first r
    // characters of each query.
    // ----------------------------------------------------------
    /**
     * compares alphabetically
     *
     * @param r
     *            length to check equivalence to
     * @return a comparator to sort by prefix order
     */
    public static Comparator<Term> byPrefixOrder(int r)
    {
        if (r < 0)
        {
            throw new IllegalArgumentException("r can't be negative");
        }
        return new Comparator<Term>() {
            public int compare(Term o1, Term o2)
            {
                if (o1 == null && o2 == null)
                {
                    return 0;
                }
                if(o1 == null)
                {
                    return -1;
                }
                if(o2 == null)
                {
                    return 1;
                }

                for (int x = 0; x < Math
                    .max(o1.length(), Math.max(r, o2.length())); x++)
                {
                    if (o1.getQuery().charAt(x) != o2.getQuery().charAt(x))
                    {
                        return o1.getQuery().charAt(x)
                            - o2.getQuery().charAt(x);
                    }
                }
                return o1.length() - o2.length();
            }
        };
    }


    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that)
    {
        return Term.byPrefixOrder(Math.max(that.length(), this.length()))
            .compare(this, that);
    }


    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString()
    {
        return this.getWeight() + "\t" + this.getQuery();
    }
}
