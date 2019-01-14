package net.wagnerjr.googlettshassio;

import net.wagnerjr.hassioapi.configuration.ConfigHandler;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.File;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {
        ConfigHandler.getConfig(new File("./config.json"));
        DataSource dataSource = ConfigHandler.getConfiguration().getDatabaseConfiguration().getDataSource();

        port(80);

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
        post("/tts", (request, response) -> {
            TextToSpeechRequest TtsReq = TextToSpeechRequest.fromJson(request.body());
            TextToSpeechRequest textToSpeechRequest = TtsReq.prepareMp3().store(dataSource);

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                  "{\"id\": " + textToSpeechRequest.getId() + "}");

            Request respondingRequest = new Request.Builder()
                  .url(textToSpeechRequest.getResponseUrl())
                  .post(body)
                  .build();

            client.newCall(respondingRequest).execute().close();

            return "{\"status\": \"success\"}";
        });

        get("/mp3/play.mp3", (request, response) -> {
            Integer id = Integer.valueOf(request.queryParams("id"));
            System.out.println(id);
            TextToSpeechRequest tts = TextToSpeechRequest.getFromDataBase(id, dataSource);
            response.type("audio/mp3");

            HttpServletResponse raw = response.raw();

            raw.getOutputStream().write(tts.getMp3().toByteArray());
            raw.getOutputStream().flush();
            raw.getOutputStream().close();

            return raw;
        });
    }
}
