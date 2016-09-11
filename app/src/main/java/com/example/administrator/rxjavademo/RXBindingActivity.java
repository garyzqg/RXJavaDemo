package com.example.administrator.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.functions.Action1;

public class RXBindingActivity extends AppCompatActivity {
    Button mSkip;
    EditText mAccountEdit;
    EditText mPasswordEdit;
    private String mAccount;//账号
    private String mPassword;//密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding);
        mSkip = (Button) findViewById(R.id.btn_login);
        mAccountEdit = (EditText) findViewById(R.id.et_login_account);
        mPasswordEdit = (EditText) findViewById(R.id.et_login_password);
        //RXBinding实现EditText的监听
        RxTextView.textChanges(mAccountEdit).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                mAccount = String.valueOf(charSequence);
            }
        });
        RxTextView.textChanges(mPasswordEdit).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                mPassword = String.valueOf(charSequence);
            }
        });

        //RXBinding实现Button点击事件的监听
        RxView.clicks(mSkip).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mAccount.equals("zhangsan") && mPassword.equals("123456")){
                    startActivity(new Intent(RXBindingActivity.this,MainActivity.class));
                    Toast.makeText(RXBindingActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RXBindingActivity.this, "账号或密码错误,请重新输入", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
