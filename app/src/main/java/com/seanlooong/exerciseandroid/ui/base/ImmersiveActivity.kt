package com.seanlooong.exerciseandroid.ui.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.ref.WeakReference

open class ImmersiveActivity : AppCompatActivity() {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            transparentStatusBar(window)
            transparentNavigationBar(window)
        }
    }

    fun transparentStatusBar(window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility
        window.statusBarColor = Color.TRANSPARENT

        //设置状态栏文字颜色
        setStatusBarTextColor(window, true)
    }

    fun setStatusBarTextColor(window: Window, light: Boolean) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) { //白色文字
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        } else { //黑色文字
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    fun transparentNavigationBar(window: Window) {
        window.isNavigationBarContrastEnforced = false
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility =
            systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.decorView.systemUiVisibility = systemUiVisibility
        window.navigationBarColor = Color.TRANSPARENT

        //设置导航栏按钮或导航条颜色
        setNavigationBarBtnColor(window, true)
    }

    fun setNavigationBarBtnColor(window: Window, light: Boolean) {
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = if (light) { //白色按钮
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        } else { //黑色按钮
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun getStatusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resId > 0) {
            return context.resources.getDimensionPixelSize(resId)
        }
        return 96
    }

    fun fixStatusBarMargin(vararg views: View) {
        views.forEach { view ->
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let { lp ->
                lp.topMargin = lp.topMargin + getStatusBarHeight(view.context)
                view.requestLayout()
            }
        }
    }

    fun paddingByStatusBar(view: View) {
        view.setPadding(
            view.paddingLeft,
            view.paddingTop + getStatusBarHeight(view.context),
            view.paddingRight,
            view.paddingBottom
        )
    }

    private class NavigationViewInfo(
        val hostRef: WeakReference<View>,
        val viewRef: WeakReference<View>,
        val rawBottom: Int,
        val onNavHeightChangeListener: (View, Int, Int) -> Unit
    )

    private val navigationViewInfoList = mutableListOf<NavigationViewInfo>()

    private val onApplyWindowInsetsListener = View.OnApplyWindowInsetsListener { v, insets ->
        val windowInsetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets, v)
        val navHeight =
            windowInsetsCompat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
        val it = navigationViewInfoList.iterator()
        while (it.hasNext()) {
            val info = it.next()
            val host = info.hostRef.get()
            val view = info.viewRef.get()
            if (host == null || view == null) {
                it.remove()
                continue
            }

            if (host == v) {
                info.onNavHeightChangeListener(view, info.rawBottom, navHeight)
            }
        }
        insets
    }

    private val actionMarginNavigation: (View, Int, Int) -> Unit =
        { view, rawBottomMargin, navHeight ->
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                it.bottomMargin = rawBottomMargin + navHeight
                view.requestLayout()
            }
        }

    private val actionPaddingNavigation: (View, Int, Int) -> Unit =
        { view, rawBottomPadding, navHeight ->
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                rawBottomPadding + navHeight
            )
        }

    fun fixNavBarMargin(vararg views: View) {
        views.forEach {
            fixSingleNavBarMargin(it)
        }
    }

    private fun fixSingleNavBarMargin(view: View) {
        val lp = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        val rawBottomMargin = lp.bottomMargin

        val viewForCalculate = getViewForCalculate(view)

        if (viewForCalculate.isAttachedToWindow) {
            val realNavigationBarHeight = getRealNavigationBarHeight(viewForCalculate)
            lp.bottomMargin = rawBottomMargin + realNavigationBarHeight
            view.requestLayout()
        }

        //isAttachedToWindow方法并不能保证此时的WindowInsets是正确的，仍然需要添加监听
        val hostRef = WeakReference(viewForCalculate)
        val viewRef = WeakReference(view)
        val info = NavigationViewInfo(hostRef, viewRef, rawBottomMargin, actionMarginNavigation)
        navigationViewInfoList.add(info)
        viewForCalculate.setOnApplyWindowInsetsListener(onApplyWindowInsetsListener)
    }

    fun paddingByNavBar(view: View) {
        val rawBottomPadding = view.paddingBottom

        val viewForCalculate = getViewForCalculate(view)

        if (viewForCalculate.isAttachedToWindow) {
            val realNavigationBarHeight = getRealNavigationBarHeight(viewForCalculate)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                rawBottomPadding + realNavigationBarHeight
            )
        }

        //isAttachedToWindow方法并不能保证此时的WindowInsets是正确的，仍然需要添加监听
        val hostRef = WeakReference(viewForCalculate)
        val viewRef = WeakReference(view)
        val info =
            NavigationViewInfo(hostRef, viewRef, rawBottomPadding, actionPaddingNavigation)
        navigationViewInfoList.add(info)
        viewForCalculate.setOnApplyWindowInsetsListener(onApplyWindowInsetsListener)
    }

    /**
     * Dialog下的View在低版本机型中获取到的WindowInsets值有误，
     * 所以尝试去获得Activity的contentView，通过Activity的contentView获取WindowInsets
     */
    @SuppressLint("ContextCast")
    private fun getViewForCalculate(view: View): View {
        return (view.context as? ContextWrapper)?.let {
            return@let (it.baseContext as? Activity)?.findViewById<View>(android.R.id.content)?.rootView
        } ?: view.rootView
    }


    @SuppressLint("InternalInsetResource")
    fun getNavigationBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier(
            "navigation_bar_height", "dimen", "android"
        )
        if (resId > 0) {
            return context.resources.getDimensionPixelSize(resId)
        }
        return 0
    }

    private fun getRealNavigationBarHeight(view: View): Int {
        val insets = ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.navigationBars())
        return insets?.bottom ?: getNavigationBarHeight(view.context)
    }
}