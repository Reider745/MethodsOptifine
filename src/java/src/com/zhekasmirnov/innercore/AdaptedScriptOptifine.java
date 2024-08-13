package com.zhekasmirnov.innercore;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.optifine_api.*;
import org.mozilla.javascript.ScriptableObject;

import com.zhekasmirnov.innercore.api.mod.API;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.optifine_api.codegen.ClassCode;

public class AdaptedScriptOptifine extends AdaptedScriptAPI {
    @Override
    protected void injectIntoScope(ScriptableObject scope, List<String> filter) {
        super.injectIntoScope(scope, filter);

        JsLevel.inject(scope);//AdaptedScriptAPI.Level
        JsEntity.inject(scope);//AdaptedScriptAPI.Entity
        JsParticles.inject(scope);//AdaptedScriptAPI.Particles
        JsTranslation.inject(scope);//AdaptedScriptAPI.Translation
        JsUpdatable.inject(scope);//AdaptedScriptAPI.Updatable
        JsNativeBlockSource.inject(scope);//NativeBlockSource
        JsUI.inject(scope);//AdaptedScriptAPI.UI
        JsItem.inject(scope);//AdaptedScriptAPI.Item
        JsBlock.inject(scope);//AdaptedScriptAPI.Block
        JsGameController.inject(scope);//AdaptedScriptAPI.GameController
        JsSaver.inject(scope);//AdaptedScriptAPI.Saver
        JsLogger.inject(scope);//AdaptedScriptAPI.Logger
        JsPlayerActor.inject(scope);//AdaptedScriptAPI.PlayerActor
        JsItemContainer.inject(scope);//AdaptedScriptAPI.ItemContainer
    }

    public static void boot(HashMap<?, ?> args) throws IOException {
        /*ClassCode.save(AdaptedScriptAPI.Level.class);
        ClassCode.save(AdaptedScriptAPI.Entity.class);
        ClassCode.save(AdaptedScriptAPI.Translation.class);
        ClassCode.save(AdaptedScriptAPI.Updatable.class);
        ClassCode.save(AdaptedScriptAPI.Particles.class);

        ClassCode.save(AdaptedScriptAPI.GenerationUtils.class);
        ClassCode.save(AdaptedScriptAPI.Player.class);
        ClassCode.save(AdaptedScriptAPI.IDRegistry.class);
        ClassCode.save(AdaptedScriptAPI.Recipes.class);
        ClassCode.save(NativeBlockSource.class);

        ClassCode.save(AdaptedScriptAPI.UI.class);
        ClassCode.save(AdaptedScriptAPI.Block.class);
        ClassCode.save(AdaptedScriptAPI.Item.class);
        ClassCode.save(AdaptedScriptAPI.GameController.class);
        ClassCode.save(AdaptedScriptAPI.Saver.class);

        ClassCode.save(AdaptedScriptAPI.Logger.class);
        ClassCode.save(AdaptedScriptAPI.PlayerActor.class);
        ClassCode.save(AdaptedScriptAPI.ItemContainer.class);*/

        try{
            final AdaptedScriptOptifine adaptedScriptOptifine = new AdaptedScriptOptifine();

            final Field ApiListField = API.class.getDeclaredField("APIInstanceList");
            ApiListField.setAccessible(true);
            final ArrayList<API> ApiList = (ArrayList<API>) ApiListField.get(null);

            int index = -1;
            for(int i = 0;i < ApiList.size();i++)
                if(ApiList.get(i).getCurrentAPIName().equals(adaptedScriptOptifine.getCurrentAPIName())) {
                    index = i;
                    break;
                }

            if(index == -1){
                Logger.debug(LOGGER_TAG, "Error edit APIInstanceList...");
                return;
            }

            ApiList.remove(index);

            registerInstance(new AdaptedScriptOptifine());


        }catch (Exception e){
            Logger.debug(LOGGER_TAG, e.toString());
        }
    }
}
