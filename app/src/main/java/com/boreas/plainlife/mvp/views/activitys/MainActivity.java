package com.boreas.plainlife.mvp.views.activitys;

import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.boreas.commonlib.xskinloader.SkinManager;
import com.boreas.plainlife.App;
import com.boreas.plainlife.Constant;
import com.boreas.plainlife.Location.LocationService;
import com.boreas.plainlife.ObjectPool;
import com.boreas.plainlife.R;
import com.boreas.plainlife.base.BaseActivity;
import com.boreas.plainlife.databinding.ActivityMainBinding;
import com.boreas.plainlife.internal.components.DaggerMainActivityComponent;
import com.boreas.plainlife.internal.modules.MainActivityModule;
import com.boreas.plainlife.mvp.presenters.presenterimpl.MainActivityPresenter;
import com.boreas.plainlife.mvp.views.fragments.LocationFragment;
import com.boreas.plainlife.mvp.views.fragments.PicNoteFragment;
import com.boreas.plainlife.mvp.views.viewinterfaces.MainActivityInterface;
import com.boreas.plainlife.receiver.BatteryBroadCastReceive;
import com.boreas.plainlife.utils.PreUtil;
import com.boreas.plainlife.utils.RxTimer;
import com.boreas.plainlife.utils.ToastUtil;
import com.lzf.easyfloat.EasyFloat;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainActivityInterface {

    private static final String FRAGMENT_LOCATION  = "位置";
    private static final String FRAGMENT_PIC_NOTE  = "卡片日记";
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private BatteryBroadCastReceive receiver;
    private boolean isFirstOpenApp = true;
    @Inject
    MainActivityPresenter presenter;
    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.setSwipeBackEnable(false);
        this.startService(new Intent(this,LocationService.class));
        this.fragmentManager = getSupportFragmentManager();
        this.binding.drawerLayout.setStatusBarBackground(R.color.transparent);
        initFloatBall();
    }

    private void changeSkin() {
        SkinManager.get().loadSkin(App.getInstance().skinPath);
    }

    private void restoreDefaultSkin() {
        SkinManager.get().restoreToDefaultSkin();
    }

    @Override
    protected void initListener() {
        this.binding.mainNavigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.itemLocationService:
                    this.switchFragment(FRAGMENT_LOCATION);
                    this.binding.drawerLayout.closeDrawer(this.binding.mainNavigationView);
                    break;
                case R.id.itemCardNode:
                    this.switchFragment(FRAGMENT_PIC_NOTE);
                    this.binding.drawerLayout.closeDrawer(this.binding.mainNavigationView);
                    break;
            }
            return false;
        });
    }

    private void initFloatBall() {
//        EasyFloat.with(this)
//                .setFilter(ShoppingActivity.class,
//                        SubmitApplyActivity.class, SubmitApplyOperationActivity.class)
//                // 设置浮窗xml布局文件，并可设置详细信息
//                .setLayout(R.layout.floatball_layout, view -> {
//                    //   TextView textView =  view.findViewById(R.id.tv_shopping_counts);
//                    view.findViewById(R.id.iv_sp_cart).setOnClickListener(new ClickProxy(v -> {
//                        Intent intent = new Intent(this, ShoppingActivity.class);
//                        startActivity(intent);
//                    }));
//                })
//                // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示、仅后台显示
//                .setShowPattern(ShowPattern.FOREGROUND)
//                // 设置吸附方式，共15种模式，详情参考SidePattern
//                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
//                // 设置浮窗的标签，用于区分多个浮窗
//                .setTag("floatBall")
//                // 设置浮窗是否可拖拽，默认可拖拽
//                .setDragEnable(true)
//                // 系统浮窗是否包含EditText，仅针对系统浮窗，默认不包含
//                .hasEditText(false)
//                // 设置浮窗固定坐标，ps：设置固定坐标，Gravity属性和offset属性将无效
////                .setLocation(100, 200)
//                // 设置浮窗的对齐方式和坐标偏移量
//                .setGravity(Gravity.END | Gravity.CENTER_VERTICAL, 0, 200)
//                // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
//                .setMatchParent(false, false)
//                // 设置Activity浮窗的出入动画，可自定义，实现相应接口即可（策略模式），无需动画直接设置为null
//                .setAnimator(null)
//                // 设置系统浮窗的出入动画，使用同上
//                .setAppFloatAnimator(null)
//                // 设置系统浮窗的不需要显示的页面
//                // .setFilter(MainActivity:: class.java, SecondActivity:: class.java)
//                // 设置系统浮窗的有效显示高度（不包含虚拟导航栏的高度），基本用不到，除非有虚拟导航栏适配问题
//                .setDisplayHeight(DisplayUtils.INSTANCE::rejectedNavHeight)
////    .setDisplayHeight(OnDisplayHeight {
////            context -> DisplayUtils.rejectedNavHeight(context)
////        })
//                // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
//                // ps：通过Kotlin DSL实现的回调，可以按需复写方法，用到哪个写哪个
//
//                // 创建浮窗（这是关键哦😂）
//                .show();
    }


    @Override
    protected void initComponent() {
        DaggerMainActivityComponent.builder()
                .netComponent(App.getInstance().getmNetComponent())
                .mainActivityModule(new MainActivityModule(this))
                .appComponent(App.getInstance().getmAppComponent())
                .beansComponent(App.getInstance().getmBeansComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        this.presenter.requestUserInfo();
        this.presenter.onInit();
        this.registerBatterReceiver();
    }



    private void registerBatterReceiver(){
        this.receiver = new BatteryBroadCastReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(this.receiver, intentFilter);
    }
    private void unRegisterBatterReceiver(){
        if(this.receiver != null){
            unregisterReceiver(this.receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty((String)PreUtil.get(Constant.TOKEN_KEY,""))){
            ToastUtil.show(this,"请先登陆");
            this.onReStart();
            return;
        }
        Boolean isFirstOpenApp = (Boolean) PreUtil.get("isFirstOpenApp", true);
        if (isFirstOpenApp) {
            this.binding.drawerLayout.openDrawer(this.binding.mainNavigationView);
            PreUtil.put("isFirstOpenApp", false);
        }
        if(this.isFirstOpenApp){
            this.switchFragment(FRAGMENT_PIC_NOTE);
            this.isFirstOpenApp = false;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.onDestory();
        this.unRegisterBatterReceiver();
        EasyFloat.dismissAppFloat("floatBall");
    }

    @Override
    public void onFailed(String msg) {
        ToastUtil.show(this,msg);
    }

    @Override
    public void noNetWork() {

    }

    @Override
    public void noData() {

    }
    @Override
    public void onDisLoadingDialog() {
        this.dismissLoadingDialog();
    }

    @Override
    public void onShowLoadingDialog() {
        this.showLoadingDialog();
    }

    private RxTimer startTimer = null;
    @Override
    public void onReStart() {
        if(startTimer == null){
            startTimer = new RxTimer();
        }
        startTimer.timer(1000,number -> {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            startTimer.cancel();
            startTimer = null;
            finish();
        });
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showBottom(this, getResources().getString(R.string.login_activity_exiting));
            exitTime = System.currentTimeMillis();
            return;
        } else {
            finish();
        }
        super.onBackPressed();
    }
    public void switchFragment(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name)) {
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment foundFragment = fragmentManager.findFragmentByTag(name);

        if (foundFragment == null) {
            if (name.equals(FRAGMENT_LOCATION)) {
                foundFragment = new LocationFragment();
            } else if (name.equals(FRAGMENT_PIC_NOTE)) {
                foundFragment = new PicNoteFragment();
            }
        }

        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.container, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
    }
}
