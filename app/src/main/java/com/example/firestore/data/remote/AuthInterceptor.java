package com.example.firestore.data.remote;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor{
     private static final String MI_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyYTdhMjlhOTU0YzVkMDc3ZmExYjlmODhkMGY5NjVjZCIsIm5iZiI6MTc3MjM4OTE2NS42NjcsInN1YiI6IjY5YTQ4MzJkYjlhYTJmNjdiNmUyYjhkMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yWegs9m9wwr2gVMFzkD0Gs82R_dPj4sqSWPuprhSJq8";
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request requestOriginal = chain.request();
        Request requestConToken = requestOriginal.newBuilder()
                .header("Authorization", "Bearer " + MI_TOKEN)
                .build();
        return chain.proceed(requestConToken);
    }
}

