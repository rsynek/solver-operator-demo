package org.synek.solver.manager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@ApplicationScoped
public class PodService {

    @Inject
    OpenShiftClient openShiftClient;

    public void startPod(String namespace, long problemId) {
        String podName = getPodName(problemId);
        EnvVar envVar = new EnvVarBuilder()
                .withName("PROBLEM_ID")
                .withValue(String.valueOf(problemId))
                .build();
        Pod pod = new PodBuilder().withNewMetadata().withName(podName).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(podName + "-container")
                .withImage("image-registry.openshift-image-registry.svc:5000/s2i/school-timetabling")
                .addNewPort()
                .withContainerPort(8080)
                .endPort()
                .addToEnv(envVar)
                .endContainer()
                .endSpec()
                .build();
        Pod createdPod = openShiftClient.pods().inNamespace(namespace).createOrReplace(pod);
    }

    public void deletePod(String namespace, long problemId) {
        String podName = getPodName(problemId);
        Boolean isDeleted = openShiftClient.pods()
                .inNamespace(namespace)
                .withName(podName)
                .delete();
        System.out.println("Deleted a pod (" + podName + ") : " + isDeleted);
    }

    private String getPodName(long problemId) {
        return "solver-pod-" + problemId;
    }
}
