package org.synek.sender;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.synek.sender.messaging.SolverEvent;
import org.synek.sender.messaging.SolverEventType;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
@ApplicationScoped
public class Main implements QuarkusApplication {

    private static final long[] PROBLEM_IDS = { 1L, 2L, 3L };

    @Channel("solver")
    Emitter<SolverEvent> solverEventEmitter;

    @Override
    public int run(String... args) {
        for (long problemId : PROBLEM_IDS) {
            solverEventEmitter.send(new SolverEvent(problemId, SolverEventType.SOLVER_REQUEST));
        }

        return 0;
    }
}