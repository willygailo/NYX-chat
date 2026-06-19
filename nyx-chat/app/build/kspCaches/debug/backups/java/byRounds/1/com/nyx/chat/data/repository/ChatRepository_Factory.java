package com.nyx.chat.data.repository;

import com.nyx.chat.data.api.GrokApi;
import com.nyx.chat.data.local.AppDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ChatRepository_Factory implements Factory<ChatRepository> {
  private final Provider<GrokApi> apiProvider;

  private final Provider<AppDatabase> dbProvider;

  public ChatRepository_Factory(Provider<GrokApi> apiProvider, Provider<AppDatabase> dbProvider) {
    this.apiProvider = apiProvider;
    this.dbProvider = dbProvider;
  }

  @Override
  public ChatRepository get() {
    return newInstance(apiProvider.get(), dbProvider.get());
  }

  public static ChatRepository_Factory create(Provider<GrokApi> apiProvider,
      Provider<AppDatabase> dbProvider) {
    return new ChatRepository_Factory(apiProvider, dbProvider);
  }

  public static ChatRepository newInstance(GrokApi api, AppDatabase db) {
    return new ChatRepository(api, db);
  }
}
