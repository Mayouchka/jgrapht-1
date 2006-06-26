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
/* -------------------------
 * DijkstraShortestPath.java
 * -------------------------
 * (C) Copyright 2003-2006, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   Christian Hammer
 *
 * $Id$
 *
 * Changes
 * -------
 * 02-Sep-2003 : Initial revision (JVS);
 * 29-May-2005 : Make non-static and add radius support (JVS);
 * 07-Jun-2005 : Made generic (CH);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.traverse.*;


/**
 * An implementation of <a
 * href="http://mathworld.wolfram.com/DijkstrasAlgorithm.html">Dijkstra's
 * shortest path algorithm</a> using <code>ClosestFirstIterator</code>.
 *
 * @author John V. Sichi
 * @since Sep 2, 2003
 */
public final class DijkstraShortestPath<V, E>
{

    //~ Instance fields -------------------------------------------------------

    private List<E> m_edgeList;
    private double m_pathLength;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     */
    public DijkstraShortestPath(Graph<V, E> graph,
        V startVertex,
        V endVertex)
    {
        this(graph, startVertex, endVertex, Double.POSITIVE_INFINITY);
    }

    /**
     * Creates and executes a new DijkstraShortestPath algorithm instance. An
     * instance is only good for a single search; after construction, it can be
     * accessed to retrieve information about the path found.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     * @param radius limit on path length, or Double.POSITIVE_INFINITY for
     *               unbounded search
     */
    public DijkstraShortestPath(Graph<V, E> graph,
        V startVertex,
        V endVertex,
        double radius)
    {
        ClosestFirstIterator<V, E> iter =
            new ClosestFirstIterator<V, E>(graph, startVertex, radius);

        while (iter.hasNext()) {
            V vertex = iter.next();

            if (vertex.equals(endVertex)) {
                createEdgeList(graph, iter, endVertex);
                m_pathLength = iter.getShortestPathLength(endVertex);

                return;
            }
        }

        m_edgeList = null;
        m_pathLength = Double.POSITIVE_INFINITY;
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * Return the edges making up the path found.
     *
     * @return List of Edges, or null if no path exists
     */
    public List<E> getPathEdgeList()
    {
        return m_edgeList;
    }

    /**
     * Return the length of the path found.
     *
     * @return path length, or Double.POSITIVE_INFINITY if no path exists
     */
    public double getPathLength()
    {
        return m_pathLength;
    }

    /**
     * Convenience method to find the shortest path via a single static method
     * call.  If you need a more advanced search (e.g. limited by radius, or
     * computation of the path length), use the constructor instead.
     *
     * @param graph the graph to be searched
     * @param startVertex the vertex at which the path should start
     * @param endVertex the vertex at which the path should end
     *
     * @return List of Edges, or null if no path exists
     */
    public static <V, E> List<E> findPathBetween(
        Graph<V, E> graph,
        V startVertex,
        V endVertex)
    {
        DijkstraShortestPath<V, E> alg =
            new DijkstraShortestPath<V, E>(
                graph,
                startVertex,
                endVertex);

        return alg.getPathEdgeList();
    }

    private void createEdgeList(
        Graph<V, E> graph,
        ClosestFirstIterator<V, E> iter,
        V endVertex)
    {
        m_edgeList = new ArrayList<E>();

        while (true) {
            E edge = iter.getSpanningTreeEdge(endVertex);

            if (edge == null) {
                break;
            }

            m_edgeList.add(edge);
            endVertex = Graphs.getOppositeVertex(graph, edge, endVertex);
        }

        Collections.reverse(m_edgeList);
    }
}
