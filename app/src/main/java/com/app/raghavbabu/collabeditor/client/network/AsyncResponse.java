package com.app.raghavbabu.collabeditor.client.network;

import com.app.raghavbabu.collabeditor.client.Objects.Operation;

/**
 * Created by raghavbabu on 4/24/16.
 */
public interface AsyncResponse<R> {
   void processFinish(R responseObject, Operation operationType);
}

