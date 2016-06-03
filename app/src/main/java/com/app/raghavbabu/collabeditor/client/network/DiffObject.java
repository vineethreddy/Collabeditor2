package com.app.raghavbabu.collabeditor.client.network;

import java.io.Serializable;
import java.util.LinkedList;

public class DiffObject implements Serializable
{

    private static final long serialVersionUID = -4580184878403107896L;
    LinkedList<diff_match_patch.Diff> diffs;
    long clientOrServerVersionId;


    public LinkedList<diff_match_patch.Diff> getDiffs() {
        return diffs;
    }

    public void setDiffs(LinkedList<diff_match_patch.Diff> diffs) {
        this.diffs = diffs;
    }

    public DiffObject(LinkedList<diff_match_patch.Diff> diffQueue, long clientOrServerVersionId)
    {
        this.clientOrServerVersionId = clientOrServerVersionId;
        this.diffs = diffQueue;
    }

    public long getClientOrServerVersionId() {
        return clientOrServerVersionId;
    }

    public void setClientOrServerVersionId(long clientOrServerVersionId) {
        this.clientOrServerVersionId = clientOrServerVersionId;
    }

    @Override
    public String toString() {
        return "DiffObject [diffs=" + diffs
                + ", clientOrServerVersionId=" + clientOrServerVersionId + "]";
    }



}
