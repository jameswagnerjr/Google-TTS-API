package net.wagnerjr.googlettshassio;

import net.wagnerjr.googlettshassio.annotations.Nullable;
import net.wagnerjr.googlettshassio.jsonAdapters.BaseObjectsMoshiJsonAdapterFactory;
import net.wagnerjr.googlettshassio.jsonAdapters.BooleanStringAdapter;
import net.wagnerjr.googlettshassio.jsonAdapters.ByteStringAdapter;
import net.wagnerjr.googlettshassio.jsonAdapters.HttpUrlAdapter;
import net.wagnerjr.googlettshassio.jsonAdapters.InstantLongAdapter;
import net.wagnerjr.googlettshassio.jsonAdapters.OffsetDateTimeAdapter;
import net.wagnerjr.hassioapi.configuration.ConfigHandler;
import net.wagnerjr.jooq.tables.Texttospeechcache;
import net.wagnerjr.jooq.tables.records.TexttospeechcacheRecord;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.cloud.texttospeech.v1beta1.AudioConfig;
import com.google.cloud.texttospeech.v1beta1.AudioEncoding;
import com.google.cloud.texttospeech.v1beta1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1beta1.SynthesisInput;
import com.google.cloud.texttospeech.v1beta1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1beta1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1beta1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.HttpUrl;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;
import java.io.IOException;

@AutoValue
public abstract class TextToSpeechRequest {
    protected static final Moshi MOSHI = new Moshi.Builder()
          .add(BaseObjectsMoshiJsonAdapterFactory.FACTORY)
          .add(InstantLongAdapter.ADAPTER)
          .add(HttpUrlAdapter.ADAPTER)
          .add(OffsetDateTimeAdapter.ADAPTER)
          .add(BooleanStringAdapter.ADAPTER)
          .add(ByteStringAdapter.ADAPTER)
          .build();

    private static final String AUDIO_PROFILE = "small-bluetooth-speaker-class-device";

    public static JsonAdapter<TextToSpeechRequest> jsonAdapter(Moshi moshi) {
        return new AutoValue_TextToSpeechRequest.MoshiJsonAdapter(moshi);
    }

    public static TextToSpeechRequest fromJson(String string) throws IOException {
        return jsonAdapter(MOSHI).fromJson(string);
    }

    public static TextToSpeechRequest getFromDataBase(Integer id, DataSource dataSource) {
        TexttospeechcacheRecord record = DSL.using(dataSource, SQLDialect.POSTGRES)
              .selectFrom(Texttospeechcache.TEXTTOSPEECHCACHE)
              .where(Texttospeechcache.TEXTTOSPEECHCACHE.ID.eq(id))
              .fetchOneInto(TexttospeechcacheRecord.class);

        return TextToSpeechRequest.builder().setId(record.getId())
              .setMp3(ByteString.copyFrom(record.getMp3()))
              .setResponseUrl(HttpUrlAdapter.ADAPTER.fromJson(record.getResponseurl()))
              .setText(record.getText())
              .build();
    }

    public static TextToSpeechRequest create(HttpUrl newResponseUrl, String newText, ByteString newMp3, Integer newId) {
        return builder()
              .setResponseUrl(newResponseUrl)
              .setText(newText)
              .setMp3(newMp3)
              .setId(newId)
              .build();
    }

    public static Builder builder() {
        return new AutoValue_TextToSpeechRequest.Builder();
    }

    @Memoized
    public TextToSpeechRequest prepareMp3() throws IOException {
        TextToSpeechClient textToSpeechClient = TextToSpeechClient.create();

        // Set the text input to be synthesized
        SynthesisInput input = SynthesisInput.newBuilder().setText(getText()).build();

        // Build the voice request
        VoiceSelectionParams voice =
              VoiceSelectionParams.newBuilder()
                    .setLanguageCode(
                          ConfigHandler.getConfiguration().getGoogleTextToSpeechConfiguration().getLanguageCode())
                    .setSsmlGender(SsmlVoiceGender.FEMALE)
                    .setName(ConfigHandler.getConfiguration().getGoogleTextToSpeechConfiguration().getName())
                    .build();

        // Select the type of audio file you want returned
        AudioConfig audioConfig =
              AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
                    .addEffectsProfileId(AUDIO_PROFILE)
                    .build();

        // Perform the text-to-speech request
        SynthesizeSpeechResponse response =
              textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

        // Get the audio contents from the response
        ByteString audioContents = response.getAudioContent();
        return this.withMp3(audioContents);
    }

    public TextToSpeechRequest store(DataSource dataSource) {
        TexttospeechcacheRecord tts = Texttospeechcache.TEXTTOSPEECHCACHE.newRecord()
              .setText(getText())
              .setMp3(getMp3().toByteArray())
              .setResponseurl(getResponseUrl().toString());

        tts.configuration().set(dataSource).set(SQLDialect.POSTGRES);
        tts.store();

        return this.withId(tts.getId());
    }

    public abstract HttpUrl getResponseUrl();

    public abstract String getText();

    @Nullable
    public abstract ByteString getMp3();

    public abstract TextToSpeechRequest withMp3(ByteString mp3);

    @Nullable
    public abstract Integer getId();

    public abstract TextToSpeechRequest withId(Integer id);

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setResponseUrl(HttpUrl newResponseUrl);

        public abstract Builder setText(String newText);

        public abstract Builder setMp3(ByteString newMp3);

        public abstract Builder setId(Integer newId);

        public abstract TextToSpeechRequest build();
    }
}
