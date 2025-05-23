// Generated by view binder compiler. Do not edit!
package com.ltw.qrreader.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.ltw.qrreader.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityHistoryBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView emptyHistoryText;

  @NonNull
  public final RecyclerView historyRecyclerView;

  private ActivityHistoryBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView emptyHistoryText, @NonNull RecyclerView historyRecyclerView) {
    this.rootView = rootView;
    this.emptyHistoryText = emptyHistoryText;
    this.historyRecyclerView = historyRecyclerView;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityHistoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_history, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityHistoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.emptyHistoryText;
      TextView emptyHistoryText = ViewBindings.findChildViewById(rootView, id);
      if (emptyHistoryText == null) {
        break missingId;
      }

      id = R.id.historyRecyclerView;
      RecyclerView historyRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (historyRecyclerView == null) {
        break missingId;
      }

      return new ActivityHistoryBinding((ConstraintLayout) rootView, emptyHistoryText,
          historyRecyclerView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
