package com.example.new_application.ui.activity.home_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.bean.Address;
import com.example.new_application.bean.ConfirmationType;
import com.example.new_application.bean.ErrorEnvelope;
import com.example.new_application.bean.GasSettings;
import com.example.new_application.bean.WalletInfoEvenEntity;
import com.example.new_application.utils.ConfirmTransactionView;
import com.example.new_application.utils.InputPwdView;
import com.example.new_application.utils.wallet.BalanceUtils;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.utils.wallet.Md5Utils;
import com.example.new_application.viewmodel.C;
import com.example.new_application.viewmodel.ConfirmationViewModel;
import com.example.new_application.viewmodel.ConfirmationViewModelFactory;
import com.example.new_application.viewmodel.TokensViewModel;
import com.example.new_application.viewmodel.TokensViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.bouncycastle.jcajce.provider.digest.MD5;
import org.greenrobot.eventbus.EventBus;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class SendActivity extends BaseActivity {

    ConfirmationViewModelFactory confirmationViewModelFactory;
    ConfirmationViewModel viewModel;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.iv_btn)
    ImageView ivBtn;
    @BindView(R.id.rl_btn)
    LinearLayout rlBtn;

    @BindView(R.id.et_transfer_address)
    EditText etTransferAddress;

    @BindView(R.id.send_amount)
    EditText amountText;

    @BindView(R.id.lly_contacts)
    LinearLayout llyContacts;
    @BindView(R.id.seekbar)
    SeekBar seekbar;

    @BindView(R.id.tv_gas_cost)
    TextView tvGasCost;

    @BindView(R.id.gas_price)
    TextView tvGasPrice;

    @BindView(R.id.lly_gas)
    LinearLayout llyGas;
    @BindView(R.id.et_hex_data)
    EditText etHexData;
    @BindView(R.id.lly_advance_param)
    LinearLayout llyAdvanceParam;


    @BindView(R.id.advanced_switch)
    Switch advancedSwitch;

    @BindView(R.id.custom_gas_price)
    EditText customGasPrice;

    @BindView(R.id.custom_gas_limit)
    EditText customGasLimit;


    private String walletAddr;
    private String contractAddress;
    private int decimals;
    private String balance;
    private String symbol;

    private String netCost;
    private BigInteger gasPrice;
    private BigInteger gasLimit;


    private boolean sendingTokens = false;

    private Dialog dialog;

    private static final int QRCODE_SCANNER_REQUEST = 1100;

    private static final double miner_min = 5 ;
    private static final double miner_max = 55;

    private String scanResult;
    private TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;
    private ETHWallet currEthWallet;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send;
    }

    @Override
    public void getIntentData() {
        Intent intent = getIntent();
        walletAddr = intent.getStringExtra(C.EXTRA_ADDRESS);

        contractAddress = intent.getStringExtra(C.EXTRA_CONTRACT_ADDRESS);

        decimals = intent.getIntExtra(C.EXTRA_DECIMALS, C.ETHER_DECIMALS);
        symbol = intent.getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.ETH_SYMBOL : symbol;

        tvTitle.setText(symbol + getString(R.string.transfer_title));

        confirmationViewModelFactory = new ConfirmationViewModelFactory();
        viewModel = ViewModelProviders.of(this, confirmationViewModelFactory)
                .get(ConfirmationViewModel.class);

        viewModel.sendTransaction().observe(this, this::onTransaction);
        viewModel.gasSettings().observe(this, this::onGasSettings);
        viewModel.progress().observe(this, this::onProgress);
        viewModel.error().observe(this, this::onError);

        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory)
                .get(TokensViewModel.class);

        tokensViewModel.defaultWallet().observe(this,  this::showWallet);
        tokensViewModel.prepare();
        // 首页直接扫描进入
        scanResult = intent.getStringExtra("scan_result");
        if (!TextUtils.isEmpty(scanResult)) {
            parseScanResult(scanResult);
        }
    }
    public void showWallet(ETHWallet wallet) {
        currEthWallet = wallet;

    }
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.prepare(this, sendingTokens? ConfirmationType.ETH: ConfirmationType.ERC20);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initToolBar();
        configViews();


    }
    public void configViews() {
        advancedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llyAdvanceParam.setVisibility(View.VISIBLE);
                    llyGas.setVisibility(View.GONE);

                    customGasPrice.setText(Convert.fromWei(new BigDecimal(gasPrice), Convert.Unit.GWEI).toString());
                    customGasLimit.setText(gasLimit.toString());

                } else {
                    llyAdvanceParam.setVisibility(View.GONE);
                    llyGas.setVisibility(View.VISIBLE);

                }
            }
        });


        final DecimalFormat gasformater = new DecimalFormat();
        //保留几位小数
        gasformater.setMaximumFractionDigits(2);
        //模式  四舍五入
        gasformater.setRoundingMode(RoundingMode.CEILING);


        final String etherUnit = getString(R.string.transfer_ether_unit);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                double p = progress / 100f;
                double d = (miner_max - miner_min) * p + miner_min;

                gasPrice = BalanceUtils.gweiToWei(BigDecimal.valueOf(d));
                tvGasPrice.setText(gasformater.format(d) + " " + C.GWEI_UNIT);

                updateNetworkFee();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar.setProgress(10);
        try {
            netCost = BalanceUtils.weiToEth(gasPrice.multiply(gasLimit), 4) + etherUnit;
        } catch (Exception e) {
        }

        customGasPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    return;
                }
                gasPrice = BalanceUtils.gweiToWei(new BigDecimal(s.toString()));

                try {
                    netCost = BalanceUtils.weiToEth(gasPrice.multiply(gasLimit),  4) + etherUnit;
                    tvGasCost.setText(String.valueOf(netCost ));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        customGasLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                gasLimit = new BigInteger(s.toString());

                updateNetworkFee();
            }
        });
    }
    public void initToolBar() {
        ivBtn.setImageResource(R.drawable.ic_transfer_scanner);
        rlBtn.setVisibility(View.VISIBLE);
    }
    private void updateNetworkFee() {

        try {
            netCost = BalanceUtils.weiToEth(gasPrice.multiply(gasLimit),  4) + " " + C.ETH_SYMBOL;
            tvGasCost.setText(String.valueOf(netCost ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void onGasSettings(GasSettings gasSettings) {
        gasPrice = gasSettings.gasPrice;
        gasLimit = gasSettings.gasLimit;

    }

    private boolean verifyInfo(String address, String amount) {

        try {
            new Address(address);
        } catch (Exception e) {
            showtoast(R.string.addr_error_tips);
            return false;
        }

        try {
            String wei = BalanceUtils.EthToWei(amount);
            return wei != null;
        } catch (Exception e) {
            showtoast(R.string.amount_error_tips);

            return false;
        }
    }



    @OnClick({R.id.rl_btn, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_btn:
                Intent intent = new Intent(SendActivity.this, QRCodeScannerActivity.class);
                startActivityForResult(intent, QRCODE_SCANNER_REQUEST);
                break;
            case R.id.btn_next:

                // confirm info;
                String toAddr = etTransferAddress.getText().toString().trim();
                String amount = amountText.getText().toString().trim();

                if (verifyInfo(toAddr, amount)) {
                    ConfirmTransactionView confirmView = new ConfirmTransactionView(this, this::onClick);
                    confirmView.fillInfo(walletAddr, toAddr, " - " + amount + " " +  symbol, netCost, gasPrice, gasLimit);

                    dialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
                    dialog.setContentView(confirmView);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }

                break;
            case R.id.confirm_button:
                // send
                dialog.hide();

                InputPwdView pwdView = new InputPwdView(this, pwd -> {
                    String psw = etTransferAddress.getText().toString().trim();
//                    if(currEthWallet!=null&&Md5Utils.md5(psw).equals(currEthWallet.getPassword())){
                        if (sendingTokens) {
                            viewModel.createTokenTransfer(pwd,
                                    psw,
                                    contractAddress,
                                    BalanceUtils.tokenToWei(new BigDecimal(amountText.getText().toString().trim()), decimals).toBigInteger(),
                                    gasPrice,
                                    gasLimit
                            );
                        } else {


                            viewModel.createTransaction(pwd, psw,
                                    Convert.toWei(amountText.getText().toString().trim(), Convert.Unit.ETHER).toBigInteger(),
                                    gasPrice,
                                    gasLimit );
                        }
            /*        }else {
                        showtoast("密码不正确");
                    }*/

                });

                dialog = new BottomSheetDialog(this);
                dialog.setContentView(pwdView);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();

                break;
        }
    }


    private void onProgress(boolean shouldShowProgress) {
        closeLoading();
        if (shouldShowProgress) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_dialog_sending)
                    .setView(new ProgressBar(this))
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void onError(ErrorEnvelope error) {
        closeLoading();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.error_transaction_failed)
                .setMessage(error.message)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    // Do nothing
                })
                .create();
        dialog.show();
    }

    private void onTransaction(String hash) {

        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.transaction_succeeded)
                .setMessage(hash)
                .setPositiveButton(R.string.button_ok, (dialog1, id) -> {
                    finish();
                })
                .setNeutralButton(R.string.copy, (dialog1, id) -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("transaction hash", hash);
                    clipboard.setPrimaryClip(clip);
                    finish();
                })
                .create();
        dialog.show();
    }

    private void fillAddress(String addr) {
        try {
            new Address(addr);
            etTransferAddress.setText(addr);
        } catch (Exception e) {
            showtoast(R.string.addr_error_tips);
        }
    }

    private void parseScanResult(String result) {
        if (result.contains(":") && result.contains("?")) {  // 符合协议格式
            String[] urlParts = result.split(":");
            if (urlParts[0].equals("ethereum")) {
                urlParts =  urlParts[1].split("\\?");

                fillAddress(urlParts[0]);

                // ?contractAddress=0xdxx & decimal=1 & value=100000
//                 String[] params = urlParts[1].split("&");
//                for (String param : params) {
//                    String[] keyValue = param.split("=");
//                }

            }


        } else {  // 无格式， 只有一个地址
            fillAddress(result);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCODE_SCANNER_REQUEST) {
            if (data != null) {
                String scanResult = data.getStringExtra("scan_result");
                // 对扫描结果进行处理
                parseScanResult(scanResult);
//                ToastUtils.showLongToast(scanResult);
            }
        }
    }
}