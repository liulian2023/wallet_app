package com.example.new_application.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cambodia.zhanbang.rxhttp.net.utils.LogUtils;
import com.example.new_application.MainActivity;
import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.utils.wallet.CreateWalletInteract;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.utils.wallet.WalletDaoUtils;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletActivity extends BaseActivity {

    private static final int CREATE_WALLET_RESULT = 2202;
    private static final int LOAD_WALLET_REQUEST = 1101;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_wallet_name)
    EditText etWalletName;
    @BindView(R.id.et_wallet_pwd)
    EditText etWalletPwd;
    @BindView(R.id.et_wallet_pwd_again)
    EditText etWalletPwdAgain;
    @BindView(R.id.et_wallet_pwd_reminder_info)
    EditText etWalletPwdReminderInfo;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.common_toolbar)
    Toolbar commonToolbar;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;

    private CreateWalletInteract createWalletInteract;

    private static final int REQUEST_WRITE_STORAGE = 112;


    @Override
    public int getLayoutId() {
        return R.layout.activity_create_wallet;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        createWalletInteract = new CreateWalletInteract();

        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnCreateWallet.setEnabled(isChecked);
            }
        });
    }






    @Override
    protected void onResume() {
        super.onResume();
        ImmersionBar.with(this)
                .titleBar(commonToolbar, false)
                .navigationBarColor(R.color.white)
                .init();


        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    showtoast("The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission");
                }
            }
        }

    }

    @OnClick({R.id.tv_agreement, R.id.btn_create_wallet
            , R.id.lly_wallet_agreement, R.id.btn_input_wallet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_agreement:
                break;
            case R.id.btn_create_wallet:
                String walletName = etWalletName.getText().toString().trim();
                String walletPwd = etWalletPwd.getText().toString().trim();
                String confirmPwd = etWalletPwdAgain.getText().toString().trim();
                String pwdReminder = etWalletPwdReminderInfo.getText().toString().trim();
                boolean verifyWalletInfo = verifyInfo(walletName, walletPwd, confirmPwd, pwdReminder);
                if (verifyWalletInfo) {
                    showLoadingTip(getString(R.string.creating_wallet_tip));
                    createWalletInteract.create(walletName, walletPwd, confirmPwd, pwdReminder).subscribe(this::jumpToWalletBackUp, this::showError);
                }
                break;
            case R.id.lly_wallet_agreement:
                if (cbAgreement.isChecked()) {
                    cbAgreement.setChecked(false);
                } else {
                    cbAgreement.setChecked(true);
                }
                break;
            case R.id.btn_input_wallet:
//                Intent intent = new Intent(this, ImportWalletActivity.class);
//                startActivityForResult(intent, LOAD_WALLET_REQUEST);
                break;
        }
    }

    private boolean verifyInfo(String walletName, String walletPwd, String confirmPwd, String pwdReminder) {
        if (WalletDaoUtils.walletNameChecking(walletName)) {
            showtoast(R.string.create_wallet_name_repeat_tips);
            // 同时不可重复
            return false;
        } else if (TextUtils.isEmpty(walletName)) {
            showtoast(R.string.create_wallet_name_input_tips);
            // 同时不可重复
            return false;
        } else if (TextUtils.isEmpty(walletPwd)) {
            showtoast(R.string.create_wallet_pwd_input_tips);
            // 同时判断强弱
            return false;
        } else if (TextUtils.isEmpty(confirmPwd) || !TextUtils.equals(confirmPwd, walletPwd)) {
            showtoast(R.string.create_wallet_pwd_confirm_input_tips);
            return false;
        }
        return true;
    }


    public void showError(Throwable errorInfo) {
        closeLoading();
        LogUtils.e("CreateWalletActivity", errorInfo.toString());
          showtoast(errorInfo.toString());
    }

    public void jumpToWalletBackUp(ETHWallet wallet) {
        showtoast("创建钱包成功");
        closeLoading();

        boolean firstAccount = getIntent().getBooleanExtra("first_account", false);

        setResult(CREATE_WALLET_RESULT, new Intent());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("walletId", wallet.getId());
        intent.putExtra("walletPwd", wallet.getPassword());
        intent.putExtra("walletAddress", wallet.getAddress());
        intent.putExtra("walletName", wallet.getName());
        intent.putExtra("walletMnemonic", wallet.getMnemonic());
        intent.putExtra("first_account", true);

        startActivity(intent);
        finish();
    }


}
