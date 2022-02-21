package org.synek.solver.manager;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;

public class ClientProducer {

    @Produces
    @Singleton
    private OpenShiftClient makeDefaultClient() {
        return new DefaultOpenShiftClient();
    }

}
