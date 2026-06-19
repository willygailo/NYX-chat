package com.nyx.chat.ui.screens.conversationlist;

import com.nyx.chat.data.repository.ChatRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ConversationListViewModel_Factory implements Factory<ConversationListViewModel> {
  private final Provider<ChatRepository> repositoryProvider;

  public ConversationListViewModel_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ConversationListViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static ConversationListViewModel_Factory create(
      Provider<ChatRepository> repositoryProvider) {
    return new ConversationListViewModel_Factory(repositoryProvider);
  }

  public static ConversationListViewModel newInstance(ChatRepository repository) {
    return new ConversationListViewModel(repository);
  }
}
