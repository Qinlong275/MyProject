package com.qinlong275.android.cniaoplay.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.ui.widget.LoadingButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

import static com.jakewharton.rxbinding.widget.RxTextView.textChanges;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.txt_mobi)
    EditText mTxtMobi;
    @BindView(R.id.view_mobi_wrapper)
    TextInputLayout mViewMobiWrapper;
    @BindView(R.id.txt_password)
    EditText mTxtPassword;
    @BindView(R.id.view_password_wrapper)
    TextInputLayout mViewPasswordWrapper;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        initView();
    }

    private void initView() {
        Observable<CharSequence> obMobi = RxTextView.textChanges(mTxtMobi);
        Observable<CharSequence> obPassword = RxTextView.textChanges(mTxtPassword);
        Observable.combineLatest(obMobi, obPassword, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence mobi, CharSequence pwd) {
                return isPhoneValid(mobi.toString())&&isPasswordValid(pwd.toString());
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                RxView.enabled(mBtnLogin).call(aBoolean);
            }
        });

        RxView.clicks(mBtnLogin).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!(mTxtMobi.getText().toString().trim().equals("11111222221"))) {
                    mViewMobiWrapper.setError("手机号错误");
                }else {
                    mViewMobiWrapper.setError("");
                    mViewMobiWrapper.setErrorEnabled(false);
                }
            }
        });
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}
