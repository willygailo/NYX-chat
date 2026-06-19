package com.nyx.chat.di;

import com.nyx.chat.data.api.GrokApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideGrokApiFactory implements Factory<GrokApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideGrokApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public GrokApi get() {
    return provideGrokApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideGrokApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideGrokApiFactory(retrofitProvider);
  }

  public static GrokApi provideGrokApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideGrokApi(retrofit));
  }
}
