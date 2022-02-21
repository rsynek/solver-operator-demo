package org.synek.solver.common;

public class SolverEvent {
    private Long ProblemId;
    private SolverEventType solverEventType;

    public SolverEvent(Long problemId, SolverEventType solverEventType) {
        ProblemId = problemId;
        this.solverEventType = solverEventType;
    }

    public Long getProblemId() {
        return ProblemId;
    }

    public SolverEventType getSolverEventType() {
        return solverEventType;
    }
}
