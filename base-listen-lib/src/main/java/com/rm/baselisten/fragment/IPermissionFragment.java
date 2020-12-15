package com.rm.baselisten.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @Author ：HeXinGen
 * @Date : 2019-12-05
 * @Description : 用于请求的权限的Fragment
 */
public class IPermissionFragment extends Fragment implements EasyPermissions.RationaleCallbacks {
    private static final String permission_tag = IPermissionFragment.class.getSimpleName();
    private PermissionRequest request = new PermissionRequest(this);

    public static IPermissionRequest with(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        IPermissionFragment fragment = (IPermissionFragment) fragmentManager.findFragmentByTag(permission_tag);
        if (fragment == null) {
            fragment = new IPermissionFragment();
            fragmentManager.beginTransaction().add(fragment, permission_tag).
                    commitAllowingStateLoss();
        }
        return fragment.init();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.request.statRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.request.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.request.handlePermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.request.handleActivityResult(requestCode);
    }

    private IPermissionRequest init() {
        this.request.reset();
        return this.request;
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    /**
     * 请求权限，二次提醒弹窗的取消按钮
     *
     * @param requestCode
     */
    @Override
    public void onRationaleDenied(int requestCode) {
        request.handleRationaleRefuse(requestCode);
    }

    public interface IPermissionRequest {
        /**
         * 添加需要请求的权限
         *
         * @param permissions
         * @return
         */
        IPermissionRequest addPermission(String... permissions);

        /**
         * 当权限被拒绝后，下次氢气权限的提示语
         *
         * @param tip
         * @return
         */
        IPermissionRequest setRationale(String tip);

        /**
         * 权限拒绝的回调器
         *
         * @param refuseListener
         * @return
         */
        IPermissionRequest setRefuseListener(RefuseListener refuseListener);

        /**
         * 权限同意的回调器
         *
         * @param grantListener
         * @return
         */
        IPermissionRequest setGrantListener(GrantListener grantListener);

        /**
         * 跳转设置的弹窗显示参数
         *
         * @param message
         * @return
         */
        IPermissionRequest setOpenSetArg(OpenSetMessage message);

        /**
         * 开始执行请求权限
         */
        void start();
    }

    private static class PermissionRequest implements EasyPermissions.PermissionCallbacks, IPermissionRequest {
        private List<String> permissionList = new ArrayList<>();
        private int request_permission_code = 1111;
        public static int request_set_code = 1112;
        private GrantListener grantListener;
        private RefuseListener refuseListener;
        private AtomicBoolean done = new AtomicBoolean(false);
        private Fragment fragment;
        private String tip = "App need Use Permissions";
        private OpenSetMessage openSetMessage;
        private Handler handler = new Handler(Looper.getMainLooper());

        public PermissionRequest(Fragment fragment) {
            this.fragment = fragment;
        }

        private void reset() {
            this.permissionList.clear();
            this.grantListener = null;
            this.refuseListener = null;
            this.openSetMessage = null;
            this.done.set(false);
        }

        @Override
        public IPermissionRequest setRationale(String tip) {
            this.tip = tip;
            return this;
        }

        @Override
        public IPermissionRequest addPermission(String... permissions) {
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    this.permissionList.add(permission);
                }
            }
            return this;
        }

        @Override
        public IPermissionRequest setRefuseListener(RefuseListener refuseListener) {
            this.refuseListener = refuseListener;
            return this;
        }

        @Override
        public IPermissionRequest setOpenSetArg(OpenSetMessage message) {
            this.openSetMessage = message;
            return this;
        }

        @Override
        public IPermissionRequest setGrantListener(GrantListener grantListener) {
            this.grantListener = grantListener;
            return this;
        }

        @Override
        public void start() {
            if (fragment.isAdded()) {
                statRequest();
            }
        }


        private void statRequest() {
            if (!permissionList.isEmpty()) {
                if (done.compareAndSet(false, true)) {
                    if (!hasPermission()) {
                        String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                        EasyPermissions.requestPermissions(fragment, tip, request_permission_code, permissions);
                    } else {
//                        if (grantListener != null) {
//                            grantListener.grant(permissionList);
//                        }
                    }
                }
            }
        }

        private boolean hasPermission() {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            boolean grant = EasyPermissions.hasPermissions(fragment.getContext(), permissions);
            if (grant) {
                if (grantListener != null) {
                    grantListener.grant(permissionList);
                }
            }
            return grant;
        }

        private void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

        private Runnable check_task = () -> {
            if (!hasPermission()) {
                if (refuseListener != null) {
                    refuseListener.refuse(permissionList);
                }
            }
        };

        private void release() {
            handler.removeCallbacks(check_task);
        }

        private void handleActivityResult(int requestCode) {
            if (requestCode == request_set_code) {
                handler.postDelayed(check_task, 200);
            }
        }

        private void handleRationaleRefuse(int requestCode) {
            if (requestCode == request_permission_code) {
                handler.postDelayed(check_task, 200);
            }
        }

        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            if (grantListener != null) {
                grantListener.grant(perms);
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
            if (openSetMessage != null) {
                showSetDialog();
            } else {
                if (refuseListener != null) {
                    refuseListener.refuse(perms);
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        }

        private void showSetDialog() {
            new AppSettingsDialog.Builder(fragment)
                    .setTitle(openSetMessage.title)
                    .setRationale(openSetMessage.showContent)
                    .setPositiveButton(openSetMessage.sureBtnText)
                    .setNegativeButton(openSetMessage.cancelBtnText)
                    .setRequestCode(request_set_code)
                    .build().show();
        }
    }

    /**
     * 跳转设置界面的提示弹窗
     */
    public static class OpenSetMessage {
        private String title;
        private String showContent;
        private String sureBtnText;
        private String cancelBtnText;

        public OpenSetMessage setTitle(String title) {
            this.title = title;
            return this;
        }

        public OpenSetMessage setShowContent(String showContent) {
            this.showContent = showContent;
            return this;
        }

        public OpenSetMessage setSureBtnText(String sureBtnText) {
            this.sureBtnText = sureBtnText;
            return this;
        }

        public OpenSetMessage setCancelBtnText(String cancelBtnText) {
            this.cancelBtnText = cancelBtnText;
            return this;
        }
    }

    public interface GrantListener {
        void grant(List<String> grantPermissions);
    }

    public interface RefuseListener {
        void refuse(List<String> refusePermissions);
    }
}
