package org.synek.solver.manager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@ApplicationScoped
public class PodService {

    @Inject
    OpenShiftClient openShiftClient;

    public void startPod(String namespace, String podName) {
        Pod pod = new PodBuilder().withNewMetadata().withName(podName).endMetadata()
                .withNewSpec()
                .addNewContainer()
                .withName(podName + "-container")
                .withImage("image-registry.openshift-image-registry.svc:5000/s2i/quickstart-school-timetabling")
                .addNewPort()
                .withContainerPort(8080)
                .endPort()
                .endContainer()
                .endSpec()
                .build();
        Pod createdPod = openShiftClient.pods().inNamespace(namespace).createOrReplace(pod);
    }

    public void deletePod(String namespace, String podName) {
        Boolean isDeleted = openShiftClient.pods()
                .inNamespace(namespace)
                .withName(podName)
                .delete();
    }
}
