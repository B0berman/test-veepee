package com.vp.list;

import androidx.annotation.NonNull;

public interface SearchHandler {
    void submitSearchQuery(@NonNull final String query);
    void resetSearchQuery();
}
