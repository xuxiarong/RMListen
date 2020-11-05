//package com.rm.baselisten.jsondeserializer;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import com.xiangxue.network.base.INetworkRequiredInfo;
//import com.xiangxue.network.base.jsondeserializer.BigDecimalAdapter;
//import com.xiangxue.network.base.jsondeserializer.BigIntegerAdapter;
//import com.xiangxue.network.base.jsondeserializer.IntegerAdapter;
//import com.xiangxue.network.base.jsondeserializer.ListAdapter;
//import com.xiangxue.network.base.jsondeserializer.StringAdapter;
//import com.xiangxue.network.commoninterceptor.CommonRequestInterceptor;
//import com.xiangxue.network.commoninterceptor.CommonResponseInterceptor;
//import com.xiangxue.network.dns.TestDns;
//import com.xiangxue.network.environment.EnvironmentActivity;
//import com.xiangxue.network.environment.IEnvironment;
//import com.xiangxue.network.errorhandler.HttpErrorHandler;
//
//import java.util.HashMap;
//import java.util.List;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLSession;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.ObservableTransformer;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//public abstract class NetworkApi implements IEnvironment {
//    private static INetworkRequiredInfo iNetworkRequiredInfo;
//    private static HashMap<String, Retrofit> retrofitHashMap = new HashMap<>();
//    private String mBaseUrl;
//    private OkHttpClient mOkHttpClient;
//    private static boolean mIsFormal = true;
//
//    public NetworkApi() {
//        if (!mIsFormal) {
//            mBaseUrl = getTest();
//        }
//        mBaseUrl = getFormal();
//    }
//
//    public static void init(INetworkRequiredInfo networkRequiredInfo) {
//        iNetworkRequiredInfo = networkRequiredInfo;
//        mIsFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
//    }
//
//    protected Retrofit getRetrofit(Class service) {
//        if (retrofitHashMap.get(mBaseUrl + service.getName()) != null) {
//            return retrofitHashMap.get(mBaseUrl + service.getName());
//        }
//        Gson gson = new GsonBuilder()
//                .serializeNulls()
//                .registerTypeHierarchyAdapter(List.class, new BigDecimalAdapter())
//                .registerTypeHierarchyAdapter(String.class, new BigIntegerAdapter())
//                .registerTypeHierarchyAdapter(Integer.class, new BooleanAdapter())
//                .registerTypeHierarchyAdapter(List.class, new ByteAdapter())
//                .registerTypeHierarchyAdapter(String.class, new CharacterAdapter())
//                .registerTypeHierarchyAdapter(Integer.class, new DoubleAdapter())
//                .registerTypeHierarchyAdapter(List.class, new FloatAdapter())
//                .registerTypeHierarchyAdapter(String.class, new IntegerAdapter())
//                .registerTypeHierarchyAdapter(Integer.class, new JsonObjectAdapter())
//                .registerTypeHierarchyAdapter(List.class, new ListAdapter())
//                .registerTypeHierarchyAdapter(String.class, new LongAdapter())
//                .registerTypeHierarchyAdapter(Integer.class, new NumberAdapter())
//                .registerTypeHierarchyAdapter(List.class, new ShortAdapter())
//                .registerTypeHierarchyAdapter(String.class, new StringAdapter())
//                .create();
//        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
//        retrofitBuilder.baseUrl(mBaseUrl);
//        retrofitBuilder.client(getOkHttpClient());
//        retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
//        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//        Retrofit retrofit = retrofitBuilder.build();
//        retrofitHashMap.put(mBaseUrl + service.getName(), retrofit);
//        return retrofit;
//    }
//
//    private OkHttpClient getOkHttpClient() {
//        if (mOkHttpClient == null) {
//            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//            if (getInterceptor() != null) {
//                okHttpClientBuilder.addInterceptor(getInterceptor());
//            }
//            okHttpClientBuilder.addInterceptor(new CommonRequestInterceptor(iNetworkRequiredInfo));
//            okHttpClientBuilder.addInterceptor(new CommonResponseInterceptor());
//            if (iNetworkRequiredInfo != null &&(iNetworkRequiredInfo.isDebug())) {
//                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
//            }
//            okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//            okHttpClientBuilder.dns(new TestDns());
//            mOkHttpClient = okHttpClientBuilder.build();
//        }
//        return mOkHttpClient;
//    }
//
//    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
//        return new ObservableTransformer<T, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<T> upstream) {
//                Observable<T> observable = upstream.subscribeOn(Schedulers.io());
//                observable.observeOn(AndroidSchedulers.mainThread());
//                observable.map(getAppErrorHandler());
//                observable.subscribe(observer);
//                observable.onErrorResumeNext(new HttpErrorHandler<T>());
//                return observable;
//            }
//        };
//    }
//
//    protected abstract Interceptor getInterceptor();
//
//    protected abstract <T> Function<T, T> getAppErrorHandler();
//}
