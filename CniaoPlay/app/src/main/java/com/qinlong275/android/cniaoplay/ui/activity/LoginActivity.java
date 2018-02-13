package com.qinlong275.android.cniaoplay.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.qinlong275.android.cniaoplay.R;
import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.di.component.AppComponent;
import com.qinlong275.android.cniaoplay.di.component.DaggerLoginComponent;
import com.qinlong275.android.cniaoplay.di.module.LoginModule;
import com.qinlong275.android.cniaoplay.presenter.LoginpPresenter;
import com.qinlong275.android.cniaoplay.presenter.contract.LoginContract;
import com.qinlong275.android.cniaoplay.ui.widget.LoadingButton;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

import static com.jakewharton.rxbinding.widget.RxTextView.textChanges;

public class LoginActivity extends BaseActivity <LoginpPresenter> implements LoginContract.LoginView{

    LoginActivity mActivity;

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
    LoadingButton mBtnLogin;

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent.builder().appComponent(appComponent).loginModule(new LoginModule(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        mActivity=this;
        initView();
    }

    private void initView() {

        mToolBar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(Ionicons.Icon.ion_ios_arrow_back)
                        .sizeDp(16)
                        .color(getResources().getColor(R.color.md_white_1000)
                        )
        );

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

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
                mPresenter.login(mTxtMobi.getText().toString().trim(),mTxtPassword.getText().toString().trim());
            }
        });
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    @Override
    public void showLoading() {
        mBtnLogin.showLoading();
    }

    @Override
    public void dismissLoading() {
        mBtnLogin.showButtonText();
    }

    @Override
    public void showError(String msg) {
        mBtnLogin.showButtonText();
    }

    @Override
    public void checkPhoneError() {
        mViewMobiWrapper.setError("手机号格式不正确");
    }

    @Override
    public void checkPhoneSuccess() {
        mViewMobiWrapper.setError("");
        mViewMobiWrapper.setEnabled(false);
    }

    @Override
    public void loginSuccess(LoginBean bean) {
        this.finish();
    }

}
