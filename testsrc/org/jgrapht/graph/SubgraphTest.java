/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ------------------------
 * SubgraphTest.java
 * ------------------------
 * (C) Copyright 2003-2006, by Michael Behrisch and Contributors.
 *
 * Original Author:  Michael Behrisch
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 21-Sep-2004 : Initial revision (MB);
 *
 */
package org.jgrapht.graph;

import java.util.*;

import junit.framework.*;

import org.jgrapht.*;


/**
 * Unit test for {@link Subgraph} class.
 *
 * @author Michael Behrisch
 * @since Sep 21, 2004
 */
public class SubgraphTest extends TestCase
{

    //~ Instance fields -------------------------------------------------------

    private String _v1 = new String("v1");
    private String _v2 = new String("v2");
    private String _v3 = new String("v3");
    private String _v4 = new String("v4");

    //~ Constructors ----------------------------------------------------------

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public SubgraphTest(String name)
    {
        super(name);
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testInducedSubgraphListener()
    {
        Graph<String, DefaultEdge> g = init(true);
        Subgraph<String, DefaultEdge> sub = new Subgraph<String, DefaultEdge>(g, null);

        assertEquals(g.vertexSet(), sub.vertexSet());
        assertEquals(g.edgeSet(), sub.edgeSet());

        g.addEdge(_v3, _v4);

        assertEquals(g.vertexSet(), sub.vertexSet());
        assertEquals(g.edgeSet(), sub.edgeSet());
    }

    /**
     * Tests Subgraph.
     */
    public void testSubgraph()
    {
        Graph<String, DefaultEdge> g = init(false);
        Subgraph<String, DefaultEdge> sub = new Subgraph<String, DefaultEdge>(g, null, null);

        assertEquals(g.vertexSet(), sub.vertexSet());
        assertEquals(g.edgeSet(), sub.edgeSet());

        Set<String> vset = new HashSet<String>(g.vertexSet());
        g.removeVertex(_v1);
        assertEquals(vset, sub.vertexSet()); // losing track

        g = init(false);
        vset = new HashSet<String>();
        vset.add(_v1);
        sub = new Subgraph<String, DefaultEdge>(g, vset, null);
        assertEquals(vset, sub.vertexSet());
        assertEquals(0, sub.degreeOf(_v1));
        assertEquals(Collections.EMPTY_SET, sub.edgeSet());

        vset.add(_v2);
        vset.add(_v3);
        sub = new Subgraph<String, DefaultEdge>(g, vset, new HashSet<DefaultEdge>(g.getAllEdges(_v1, _v2)));
        assertEquals(vset, sub.vertexSet());
        assertEquals(1, sub.edgeSet().size());
    }

    /**
     * .
     */
    public void testSubgraphListener()
    {
        Graph<String, DefaultEdge> g = init(true);
        Subgraph<String, DefaultEdge> sub = new Subgraph<String, DefaultEdge>(g, null, null);

        assertEquals(g.vertexSet(), sub.vertexSet());
        assertEquals(g.edgeSet(), sub.edgeSet());

        Set<String> vset = new HashSet<String>(g.vertexSet());
        g.removeVertex(_v1);
        vset.remove(_v1);
        assertEquals(vset, sub.vertexSet()); // not losing track
        assertEquals(g.edgeSet(), sub.edgeSet());
    }

    private Graph<String, DefaultEdge> init(boolean listenable)
    {
        Graph<String,DefaultEdge> g;

        if (listenable) {
            g = new ListenableUndirectedGraph<String, DefaultEdge>(
                DefaultEdge.class);
        } else {
            g = new SimpleGraph<String, DefaultEdge>(
                DefaultEdge.class);
        }

        g.addVertex(_v1);
        g.addVertex(_v2);
        g.addVertex(_v3);
        g.addVertex(_v4);
        g.addEdge(_v1, _v2);
        g.addEdge(_v2, _v3);
        g.addEdge(_v3, _v1);
        g.addEdge(_v1, _v4);

        return g;
    }
}
