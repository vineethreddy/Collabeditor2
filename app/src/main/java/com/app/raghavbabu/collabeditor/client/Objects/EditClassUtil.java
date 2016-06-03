package com.app.raghavbabu.collabeditor.client.Objects;

import android.util.Log;

import com.app.raghavbabu.collabeditor.client.network.DiffObject;
import com.app.raghavbabu.collabeditor.client.network.Document;
import com.app.raghavbabu.collabeditor.client.network.EditClass;
import com.app.raghavbabu.collabeditor.client.network.diff_match_patch;

import java.util.LinkedList;

/**
 * Created by raghavbabu on 4/29/16.
 */
public class EditClassUtil {


    /*The method performs diff on the server/client original copy and server/client shadow copy puts the list of Diffs produced
   in the editClasses queue along with client/server version number. It also changes the shadow copy to mirror the changes done
   on the original copy*/
    public static void addEdit(EditClass editClass, Document currentCopy, ShadowDocument shadowCopy, boolean Server )
    {
        diff_match_patch MatchandPatchObj = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> Diffs = MatchandPatchObj.diff_main(shadowCopy.getContent(), currentCopy.getContent());

        editClass.setClientN(shadowCopy.getClientN() );
        editClass.setServerM(shadowCopy.getServerM() );

        if(Server)
        {
            editClass.getDiffQueue().add(new DiffObject(Diffs, editClass.getServerM()));

            shadowCopy.updateM();// updating the server version number if the server is sending over the editClass
        }
        else
        {
            editClass.getDiffQueue().add(new DiffObject(Diffs, editClass.getClientN()));
            Log.v("diffs",Diffs+"");

            shadowCopy.updateN();// updating the client version number if the client is sending over the editClass
        }

        LinkedList<diff_match_patch.Patch> patch = MatchandPatchObj.patch_make(shadowCopy.getContent(), Diffs);
        Object[] result = MatchandPatchObj.patch_apply(patch, shadowCopy.getContent());

        shadowCopy.updateString(result[0].toString());
    }

    // Clears all the diffObjects from the queue of the editClass
    public static void ClearEdits(EditClass editClass)
    {
        while(editClass.getDiffQueue().peek() != null)
        {
            editClass.getDiffQueue().remove();
        }
    }
}
