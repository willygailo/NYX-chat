package com.nyx.chat.ui.screens.chat;

import android.content.Context;
import com.nyx.chat.data.repository.ChatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<ChatRepository> repositoryProvider;

  public ChatViewModel_Factory(Provider<Context> contextProvider,
      Provider<ChatRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<Context> contextProvider,
      Provider<ChatRepository> repositoryProvider) {
    return new ChatViewModel_Factory(contextProvider, repositoryProvider);
  }

  public static ChatViewModel newInstance(Context context, ChatRepository repository) {
    return new ChatViewModel(context, repository);
  }
}
