package com.app.socialtruth.helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.view.View;

import com.app.socialtruth.App;
import com.app.socialtruth.actions.AddTruthAction;
import com.app.socialtruth.actions.PutUrlAction;
import com.app.socialtruth.actions.SearchTruthAction;
import com.app.socialtruth.actions.SummarizeTextAction;
import com.app.socialtruth.utils.Utils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class SocialTruthNetworkHelper {
    public static String FetchUploadUrl(String key) throws Exception {



        Gson gson = new Gson();
        String json = gson.toJson(new PutUrlAction(key));

        //  App.Log("test RequestS3Url "+json);
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(App.Url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string().replace("\"", "");
    }

    public static AddTruthAction SearchTruth(String path) throws Exception {
       /*String key =Long.toString(new Date().getTime()) ;
    String uploadUrl =    FetchUploadUrl( key);

        App.Log("searhTruResponse "+uploadUrl);

        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        Request request = new Request.Builder()
                // .addHeader("Authorization",token)
                .url(uploadUrl.replace("\"", "")).put(
                        CreateCustomRequestBody(
                                MediaType.parse("audio"), new File(path)))
                .build();

      client.newCall(request).execute();
*/

        OkHttpClient client2 =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        Gson gson = new Gson();

        byte[] bytes =org.apache.commons.io.IOUtils.toByteArray(new FileInputStream(path));


        String json = gson.toJson(new SearchTruthAction( Base64.encodeToString(bytes, Base64.DEFAULT)));
      //  App.Log("json");
       // App.Log(json);
        Request requestaction = new Request.Builder()
                .url(App.Url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client2.newCall(requestaction).execute();

        String searhTruResponse = response.body().string();
        App.Log("searhTruResponse "+searhTruResponse);
       AddTruthAction result = new Gson().fromJson(searhTruResponse,AddTruthAction.class);
        return result;
    }

    public static void AddTruth(Context context,AddTruthAction addTruthAction) throws Exception {

        String path = addTruthAction.getKey();
        File toFile = new File(Utils.FetchTempDir(context),Utils.FetchFileName(context, Uri.parse(path)));
        try {

            Utils.CopyFile(context,Uri.parse(path),toFile);
            addTruthAction.setKey( toFile.getAbsolutePath());
            //   videoTrimmerView.restoreTrimmer(videoTrimmerView.getTrimmerDraft());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key =Long.toString(new Date().getTime()) ;
        String uploadUrl =    FetchUploadUrl( key);

        App.Log("uploadUrl "+uploadUrl);
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();

        Request request = new Request.Builder()
                // .addHeader("Authorization",token)
                .url(uploadUrl.replace("\"", "")).put(
                        CreateCustomRequestBody(
                                MediaType.parse("audio"), new File(addTruthAction.getKey())))
                .build();
        client.newCall(request).execute();
        addTruthAction.setKey(key);


        Gson gson = new Gson();
        String json = gson.toJson(addTruthAction);
        Request requestaction = new Request.Builder()
                .url(App.Url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();

        App.Log("addTruthAction "+gson.toJson(addTruthAction));
        Response response = client.newCall(requestaction).execute();

    }

    public static String SummarizeText(String key) throws Exception {
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();


        Gson gson = new Gson();
        String json = gson.toJson(new SummarizeTextAction(key));
        Request requestaction = new Request.Builder()
                .url(App.Url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(requestaction).execute();
        return response.body().string();

    }

    private static RequestBody CreateCustomRequestBody(final MediaType contentType, final File file) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }
            @Override public long contentLength() {
                return file.length();
            }
            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);

                        //   Log.d(TAG, "source size: " + contentLength() + " remaining bytes: " + (remaining -= readCount));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
