package fr.pturpin.hackathon.iceandfire.strategy.graph;

import java.util.*;

public abstract class DfsTraversal<T> {

    public void traverse(T origin, TraversalVisitor<T> visitor) {
        Queue<T> toVisit = new ArrayDeque<>();
        Set<T> visited = new HashSet<>();

        toVisit.add(origin);

        while (!toVisit.isEmpty()) {
            T current = toVisit.remove();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            TraversalVisitor.TraversalContinuation continuation = visitor.visit(current);

            if (continuation == TraversalVisitor.TraversalContinuation.SKIP) {
                continue;
            }
            if (continuation == TraversalVisitor.TraversalContinuation.STOP) {
                return;
            }

            toVisit.addAll(getNeighbors(current));
        }
    }

    protected abstract Collection<T> getNeighbors(T element);

}
