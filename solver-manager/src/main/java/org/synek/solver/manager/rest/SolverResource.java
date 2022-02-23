package org.synek.solver.manager.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.synek.solver.manager.PodService;

@Path("/solver")
public class SolverResource {

    @Inject
    PodService podService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{problemId}")
    public String solve(@PathParam("problemId") long problemId) {
        String podName = "solver-pod-" + problemId;
        podService.startPod("s2i", problemId);
        return "Pod " + podName + " started.";
    }
}
