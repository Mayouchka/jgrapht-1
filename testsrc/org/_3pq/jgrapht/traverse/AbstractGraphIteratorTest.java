/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* ------------------------------
 * AbstractGraphIteratorTest.java
 * ------------------------------
 * (C) Copyright 2003, by Liviu Rau and Contributors.
 *
 * Original Author:  Liviu Rau
 * Contributor(s):   Barak Naveh
 *
 * $Id$
 *
 * Changes
 * -------
 * 30-Jul-2003 : Initial revision (LR);
 * 06-Aug-2003 : Test traversal listener & extract a shared superclass (BN);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EnhancedTestCase;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;

/**
 * A basis for testing {@link org._3pq.jgrapht.traverse.BreadthFirstIterator}
 * and {@link org._3pq.jgrapht.traverse.DepthFirstIterator} classes.
 *
 * @author Liviu Rau
 *
 * @since Jul 30, 2003
 */
public abstract class AbstractGraphIteratorTest extends EnhancedTestCase {
    StringBuffer m_result = new StringBuffer(  );

    /**
     * .
     */
    public void testDirectedGraph(  ) {
        DirectedGraph graph = new DefaultDirectedGraph(  );

        //
        String v1 = "1";
        String v2 = "2";
        String v3 = "3";
        String v4 = "4";
        String v5 = "5";
        String v6 = "6";
        String v7 = "7";
        String v8 = "8";
        String v9 = "9";

        graph.addVertex( v1 );
        graph.addVertex( v2 );
        graph.addVertex( "3" );
        graph.addVertex( "4" );
        graph.addVertex( "5" );
        graph.addVertex( "6" );
        graph.addVertex( "7" );
        graph.addVertex( "8" );
        graph.addVertex( "9" );

        graph.addVertex( "orphan" );

        graph.addEdge( v1, v2 );
        graph.addEdge( v1, v3 );
        graph.addEdge( v2, v4 );
        graph.addEdge( v3, v5 );
        graph.addEdge( v3, v6 );
        graph.addEdge( v5, v6 );
        graph.addEdge( v5, v7 );
        graph.addEdge( v6, v1 );
        graph.addEdge( v7, v8 );
        graph.addEdge( v7, v9 );
        graph.addEdge( v8, v2 );
        graph.addEdge( v9, v4 );

        AbstractGraphIterator iterator = createIterator( graph, v1, true );
        iterator.addTraversalListener( new MyTraversalListener(  ) );

        while( iterator.hasNext(  ) ) {
            m_result.append( (String) iterator.next(  ) );

            if( iterator.hasNext(  ) ) {
                m_result.append( ',' );
            }
        }

        assertEquals( getExpectedStr2(  ), m_result.toString(  ) );
    }


    abstract String getExpectedStr1(  );


    abstract String getExpectedStr2(  );


    abstract AbstractGraphIterator createIterator( Graph g, Object vertex,
        boolean crossComponent );

    /**
     * Internal traversal listener.
     *
     * @author Barak Naveh
     */
    private class MyTraversalListener implements TraversalListener {
        private int m_componentNumber      = 0;
        private int m_numComponentVertices = 0;

        /**
         * @see TraversalListener#connectedComponentFinished()
         */
        public void connectedComponentFinished(  ) {
            switch( m_componentNumber ) {
                case 1:
                    assertEquals( getExpectedStr1(  ), m_result.toString(  ) );
                    assertEquals( 9, m_numComponentVertices );

                    break;

                case 2:
                    assertEquals( getExpectedStr2(  ), m_result.toString(  ) );
                    assertEquals( 1, m_numComponentVertices );

                    break;

                default:
                    assertFalse(  );

                    break;
            }

            m_numComponentVertices = 0;
        }


        /**
         * @see TraversalListener#connectedComponentStarted()
         */
        public void connectedComponentStarted(  ) {
            m_componentNumber++;
        }


        /**
         * @see TraversalListener#edgeVisited(Edge)
         */
        public void edgeVisited( Edge edge ) {
            // to be tested...
        }


        /**
         * @see TraversalListener#vertexVisited(Object)
         */
        public void vertexVisited( Object vertex ) {
            m_numComponentVertices++;
        }
    }
}
