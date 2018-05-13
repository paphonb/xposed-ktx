package xyz.paphonb.xposedktx

import android.content.res.XModuleResources
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

/*
 * Copyright (C) 2018 paphonb@xda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

abstract class BaseHook : IXposedHookLoadPackage, IXposedHookInitPackageResources, IXposedHookZygoteInit {

    abstract val hooksList: List<Any>
    private val modRes get() = modResInternal!!

    private var modulePath = ""
    private var modResInternal: XModuleResources? = null

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hooksList.forEach { (it as? IXposedHookLoadPackage)?.handleLoadPackage(lpparam) }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {
        modResInternal = XModuleResources.createInstance(modulePath, resparam.res)

        hooksList.forEach { (it as? IXposedHookInitPackageResources)?.handleInitPackageResources(resparam) }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        modulePath = startupParam.modulePath

        hooksList.forEach { (it as? IXposedHookZygoteInit)?.initZygote(startupParam) }
    }
}