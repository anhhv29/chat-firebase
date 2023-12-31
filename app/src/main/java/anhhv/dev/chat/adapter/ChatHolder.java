package anhhv.dev.chat.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import anhhv.dev.chat.R;

class ChatHolder extends RecyclerView.ViewHolder {


    public final TextView tvUser, tvText, tvTime;

    public ChatHolder(@NonNull View itemView) {
        super(itemView);
        tvUser = itemView.findViewById(R.id.tvUser);
        tvText = itemView.findViewById(R.id.tvText);
        tvTime = itemView.findViewById(R.id.tvTime);
    }
}
