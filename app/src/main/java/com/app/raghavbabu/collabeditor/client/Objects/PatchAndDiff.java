package com.app.raghavbabu.collabeditor.client.Objects;

import android.util.Log;

import com.app.raghavbabu.collabeditor.client.network.DiffObject;
import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.EditClass;
import com.app.raghavbabu.collabeditor.client.network.diff_match_patch;

import java.util.Iterator;
import java.util.LinkedList;

/* Serializable
 Handles all patching and diff operations

/*
        String patch(Diff , String) //returns patched strings handles exception ;)
*/

public class PatchAndDiff {

    // Method which returns a LinkedList of diffs to convert the old document to the updated document
    public static LinkedList<diff_match_patch.Diff> diff(Document old, Document updated)
    {
        diff_match_patch MatchandPatchObj = new diff_match_patch();
        return MatchandPatchObj.diff_main(old.getContent(), updated.getContent());
    }


    // Method to patch the shadowCopy and server/client copy with the incoming edits from server/client side
    public static void patchIt(EditClass editsReceived, Document serverCopy, ShadowDocument shadowCopy, boolean Server)
    {
        diff_match_patch MatchandPatchObj = new diff_match_patch();

        Iterator<DiffObject> editsIterator = editsReceived.getDiffQueue().iterator();

        while(editsIterator.hasNext())
        {
            DiffObject patcher = editsIterator.next();

            if( patcher.getClientOrServerVersionId() >= shadowCopy.getServerM())
            {
                Log.v("ServerVersion: ", String.valueOf(shadowCopy.getServerM()));
                Log.v(": ", String.valueOf(shadowCopy.getServerM()));
                LinkedList<diff_match_patch.Patch> patchShadow=MatchandPatchObj.patch_make(shadowCopy.getContent(), patcher.getDiffs());
                Object[] resultShadow = MatchandPatchObj.patch_apply(patchShadow, shadowCopy.getContent());
                shadowCopy.updateString(resultShadow[0].toString());
                LinkedList<diff_match_patch.Patch> patchServer;
                try
                {
                    patchServer=MatchandPatchObj.patch_make(serverCopy.getContent(), patcher.getDiffs());
                }
                catch(StringIndexOutOfBoundsException e)
                {
                    continue;
                }
                Object[] resultServer = MatchandPatchObj.patch_apply(patchServer, serverCopy.getContent());
                serverCopy.setContent(resultServer[0].toString());
            }
            if(!editsIterator.hasNext())
            {
                if(Server)
                {
                    shadowCopy.updateN(patcher.getClientOrServerVersionId());

                }else
                {
                    shadowCopy.updateM(patcher.getClientOrServerVersionId());
                    //EditClassUtil.ClearEdits(editsReceived);
                }
            }
        }

    }



}
