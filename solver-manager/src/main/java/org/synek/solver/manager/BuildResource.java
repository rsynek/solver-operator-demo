package org.synek.solver.manager;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.BuildConfigBuilder;
import io.fabric8.openshift.api.model.BuildRequest;
import io.fabric8.openshift.api.model.BuildRequestBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@Path("/build")
public class BuildResource {

    private static final String GIT_URL = "https://github.com/rsynek/solver-operator-demo.git";

    @Inject
    private OpenShiftClient openShiftClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public String create() {

        BuildConfig buildConfig = new BuildConfigBuilder()
                .withNewMetadata().withName("quickstart-school-timetabling").endMetadata()
                .withNewSpec()
                .withNewSource()
                .withType("Git")
                .withContextDir("solver-pod")
                .withNewGit()
                .withUri(GIT_URL)
                .endGit()
                .endSource()
                .withNewStrategy()
                .withType("Source")
                .withNewSourceStrategy()
                .withNewFrom()
                .withKind("ImageStreamTag")
                .withName("openjdk-11:latest")
                .endFrom()
                .endSourceStrategy()
                .endStrategy()
                .withNewOutput()
                .withNewTo().withKind("ImageStreamTag")
                .withName("quickstart-school-timetabling:latest") // TODO: create an image stream first.
                .endTo()
                .endOutput()
                .endSpec()
                .build();

        openShiftClient.buildConfigs().inNamespace("s2i").createOrReplace(buildConfig);

        BuildRequest buildRequest = new BuildRequestBuilder()
                .withNewMetadata()
                .withGenerateName("quickstart-school-timetabling")
                .endMetadata()
                .build();
        openShiftClient.buildConfigs().withName("quickstart-school-timetabling").instantiate(buildRequest);

        return buildConfig.getMetadata().getName();
    }
}
