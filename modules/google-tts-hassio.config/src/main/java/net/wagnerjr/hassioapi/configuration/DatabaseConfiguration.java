package net.wagnerjr.hassioapi.configuration;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import okio.BufferedSource;

import javax.sql.DataSource;
import java.io.IOException;

@AutoValue
public abstract class DatabaseConfiguration {

    protected static final Moshi MOSHI = new Moshi.Builder()
          .add(ConfigurationMoshiJsonAdapterFactory.FACTORY)
          .build();

    public static JsonAdapter<DatabaseConfiguration> jsonAdapter(Moshi moshi) {
        return new AutoValue_DatabaseConfiguration.MoshiJsonAdapter(moshi);
    }

    public static DatabaseConfiguration fromJsonString(String jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    public static DatabaseConfiguration fromJsonBufferedSource(BufferedSource jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    public static Builder builder() {
        return new AutoValue_DatabaseConfiguration.Builder();
    }

    @Memoized
    public String toJsonString() {
        return jsonAdapter(MOSHI).toJson(this);
    }

    @Memoized
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getJdbcUrl());
        hikariConfig.setUsername(getUsername());
        hikariConfig.setPassword(getPassword());
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaximumPoolSize(getMaximumPoolSize());
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(hikariConfig);
    }

    public abstract String getJdbcUrl();

    public abstract String getUsername();

    public abstract String getPassword();

    public abstract Integer getMaximumPoolSize();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setJdbcUrl(String newJdbcUrl);

        public abstract Builder setUsername(String newUsername);

        public abstract Builder setPassword(String newPassword);

        public abstract Builder setMaximumPoolSize(Integer newMaximumPoolSize);

        public abstract DatabaseConfiguration build();
    }
}
