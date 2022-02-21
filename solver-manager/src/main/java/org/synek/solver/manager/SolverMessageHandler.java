package org.synek.solver.manager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.synek.solver.common.SolverEvent;
import org.synek.solver.common.SolverEventType;

@ApplicationScoped
public class SolverMessageHandler {

    private static final String NAMESPACE = "s2i";

    @Inject
    PodService podService;

    @Incoming("solver")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public CompletionStage<Void> handleMessage(SolverEvent solverEvent) {
        if (solverEvent.getSolverEventType() == SolverEventType.SOLVER_REQUEST) {
            System.out.println("Solver pod request (" + solverEvent.getProblemId() + ")");
            return CompletableFuture.runAsync(() -> podService.startPod(NAMESPACE, getPodName(solverEvent)));
        } else if (solverEvent.getSolverEventType() == SolverEventType.SOLVER_FINISHED) {
            System.out.println("Solver pod finished (" + solverEvent.getProblemId() + ")");
            return CompletableFuture.runAsync(() -> podService.deletePod(NAMESPACE, getPodName(solverEvent)));
        } else {
            throw new IllegalStateException("Unsupported event type " + solverEvent.getSolverEventType());
        }
    }

    private String getPodName(SolverEvent solverEvent) {
        return "solver-" + solverEvent.getProblemId();
    }
}
