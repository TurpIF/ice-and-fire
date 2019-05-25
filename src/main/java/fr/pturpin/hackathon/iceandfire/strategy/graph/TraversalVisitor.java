package fr.pturpin.hackathon.iceandfire.strategy.graph;

public interface TraversalVisitor<T> {

    TraversalContinuation visit(T element);

    enum TraversalContinuation {
        SKIP, CONTINUE, STOP
    }

}
