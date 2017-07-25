package com.seunghoshin.android.servernodejs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnWrite;
    private RecyclerView recyclerView;
    private List<Bbs> data;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        data = new ArrayList<>();
        adapter = new RecyclerAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loader();
    }


    private void initView() {
        btnWrite = (Button) findViewById(R.id.btnWrite);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        btnWrite.setOnClickListener(view -> {
            /*
            // todo 호출시 startActivitiy를 사용하면 onResume 처리를 따로 해줘야 한다 .
             */
            Intent intent = new Intent(this,WriteActivity.class);

            startActivity(intent);
        });
    }

    private void loader() {
        // 1. 레트로핏 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(IBbs.SERVER)
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        // 2. 서비스 연결
        IBbs myServer = client.create(IBbs.class);
        // 3. 서비스의 특정함수 호출 -> Observable 생성
        Observable<ResponseBody> observable = myServer.read();
        Log.e("myserver read", "====" + myServer.read());
        // 4. subscribe 등록
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            // 1. 데이터를 꺼내고
                            String jsonString = responseBody.string();
                            // todo int로 받을때 null값을 받으면 gson이 에러가 뜬다 . 이때는 string으로 직접받아서 일일이 예외처리해야함
                            Log.e("jsonString", "====" + jsonString);
                            Gson gson = new Gson();
//                            Type type = new TypeToken<List<Bbs>>(){}.getType(); // 컨버팅 하기 위한 타입지정
//                            List<Bbs> data = gson.fromJson(jsonString, type);
                            Bbs data[] = gson.fromJson(jsonString, Bbs[].class);    // 지정한 객체 타입으로 만들어서 넘겨준다
                            // 2. 아답터에 세팅하고
                            for (Bbs bbs : data) {
                                this.data.add(bbs);
                                Log.e("bbs", "====" + bbs); //게시물 개수의 주소값
                            }
                            Log.e("data", "====" + data);
                            // 3. 아답터 갱신
                            adapter.notifyDataSetChanged();
                        }
                );
    }


}
