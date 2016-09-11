package com.example.administrator.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Subscriber<String> mSubscriber;
    ImageView mImageView;
    int picture = R.drawable.pxh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.iv_picture);

        //创建观察者---除了 Observer 接口之外，RxJava 还内置了一个实现了 Observer 的抽象类：Subscriber
        //Subscriber相比Observer多了两个方法,onstart和unsubscribe(用于解绑)
        mSubscriber = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };
        //创建 Observable被观察者，它决定什么时候触发事件以及触发怎样的事件。
        //RxJava 使用 create() 方法来创建一个 Observable ，并为它定义事件触发规则：
        /**
         * 可以看到，这里传入了一个 OnSubscribe 对象作为参数。OnSubscribe 会被存储在返回的 Observable 对象中，
         * 它的作用相当于一个计划表，当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，
         * 事件序列就会依照设定依次触发（对于上面的代码，就是观察者Subscriber 将会被调用三次 onNext() 和一次 onCompleted()）。
         * 这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式
         */

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                //由被观察者observable调用了观察者Subscriber的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式
                subscriber.onNext("hello");
                subscriber.onNext("Hi");
                subscriber.onNext("哈哈哈");
                subscriber.onCompleted();
            }
        });

        //Subscribe (订阅)创建了 Observable 和 Observer 之后，再用 subscribe() 方法将它们联结起来，整条链子就可以工作了。代码形式很简单：
        observable.subscribe(mSubscriber);


//------------------------------------------案例一-----------------------------------------------------------------
        /**
         * 打印字符串数组 将字符串数组names中的所有字符串依次打印出来：
         */
        //传入数组
        String[] name = {"张三","李四","王五"};
        Observable.from(name)
                //自定义subscriber的next方法
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "call: " + s);
                    }
                });
//---------------------------------------案例二---------------------------------------------------------------------

        /**
         * 指定一个 drawable并显示在 ImageView 中，获取图片在子线程,更新UI在主线程,并在出现异常的时候打印 Toast 报错：
         */
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(picture);
                subscriber.onCompleted();
            }
                //指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
        })      .subscribeOn(Schedulers.io())
                //指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Integer integer) {
                mImageView.setImageResource(integer);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSubscriber.unsubscribe();
    }
}
