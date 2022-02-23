package org.synek.solver.manager.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.fabric8.openshift.api.model.BuildConfig;
import io.fabric8.openshift.api.model.BuildConfigBuilder;
import io.fabric8.openshift.api.model.BuildRequest;
import io.fabric8.openshift.api.model.BuildRequestBuilder;
import io.fabric8.openshift.api.model.ImageStream;
import io.fabric8.openshift.api.model.ImageStreamBuilder;
import io.fabric8.openshift.client.OpenShiftClient;

@Path("/build")
public class BuildResource {

    private static final String GIT_URL = "https://github.com/rsynek/quickstart-school-timetabling.git";
    private static final String NAME = "school-timetabling";

    @Inject
    private OpenShiftClient openShiftClient;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    public String create() {
        ImageStream imageStream = new ImageStreamBuilder()
                .withNewMetadata().withName(NAME)
                .endMetadata()
                .build();
        openShiftClient.imageStreams().createOrReplace(imageStream);

        BuildConfig buildConfig = new BuildConfigBuilder()
                .withNewMetadata().withName(NAME).endMetadata()
                .withNewSpec()
                .withNewSource()
                .withType("Git")
                //.withContextDir("solver-pod")
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
                .withName(NAME + ":latest") // TODO: create an image stream first.
                .endTo()
                .endOutput()
                .endSpec()
                .build();

        openShiftClient.buildConfigs().inNamespace("s2i").createOrReplace(buildConfig);

        BuildRequest buildRequest = new BuildRequestBuilder()
                .withNewMetadata()
                .withGenerateName(NAME)
                .endMetadata()
                .build();
        openShiftClient.buildConfigs().withName(NAME).instantiate(buildRequest);

        return buildConfig.getMetadata().getName();
    }
}
