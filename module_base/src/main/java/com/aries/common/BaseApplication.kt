package com.aries.common

import android.content.Context
import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.MavericksViewModelConfigFactory
import com.airbnb.mvrx.asContextElement
import com.aries.common.config.ModuleConfig
import com.aries.common.impl.IBaseApplication
import com.aries.mock.BuildConfig
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseApplication : MultiDexApplication(), IBaseApplication {
    companion object {
        private var mContext: Context? = null
        fun getContext() = mContext!!
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initComponent()

//        val threadPolicy = StrictMode.ThreadPolicy.Builder()
//            .detectNetwork()
//            .penaltyDialog()
//            .build()

//        val viewModelConfigFactory = MavericksViewModelConfigFactory(this,
//            storeContextOverride = if (BuildConfig.DEBUG) threadPolicy.asContextElement() else EmptyCoroutineContext
//        )

        val viewModelConfigFactory = MavericksViewModelConfigFactory(this,
            storeContextOverride = EmptyCoroutineContext
        )

        Mavericks.initialize(this, viewModelConfigFactory)
    }

    private fun initComponent() {
        for (module in ModuleConfig.modules) {
            try {
                val clazz = Class.forName(module)
                val baseApplication: IBaseApplication = clazz.newInstance() as IBaseApplication
                baseApplication.init()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }
}