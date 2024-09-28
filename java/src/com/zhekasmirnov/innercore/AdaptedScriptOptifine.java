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

        JsAdaptedScriptAPILevel.inject(scope);//AdaptedScriptAPI.Level
        JsAdaptedScriptAPIEntity.inject(scope);//AdaptedScriptAPI.Entity
        JsAdaptedScriptAPIParticles.inject(scope);//AdaptedScriptAPI.Particles
        JsAdaptedScriptAPITranslation.inject(scope);//AdaptedScriptAPI.Translation
        JsAdaptedScriptAPIUpdatable.inject(scope);//AdaptedScriptAPI.Updatable
        JsNativeBlockSource.inject(scope);//NativeBlockSource
        JsAdaptedScriptAPIUI.inject(scope);//AdaptedScriptAPI.UI
        JsAdaptedScriptAPIItem.inject(scope);//AdaptedScriptAPI.Item
        JsAdaptedScriptAPIBlock.inject(scope);//AdaptedScriptAPI.Block
        JsAdaptedScriptAPIGameController.inject(scope);//AdaptedScriptAPI.GameController
        JsAdaptedScriptAPISaver.inject(scope);//AdaptedScriptAPI.Saver
        JsAdaptedScriptAPILogger.inject(scope);//AdaptedScriptAPI.Logger
        JsAdaptedScriptAPIPlayerActor.inject(scope);//AdaptedScriptAPI.PlayerActor
        JsAdaptedScriptAPIItemContainer.inject(scope);//AdaptedScriptAPI.ItemContainer
    }

    public static void boot(HashMap<?, ?> args) throws IOException {
        //ClassCode.genApi();
        InitClasses.init();

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
