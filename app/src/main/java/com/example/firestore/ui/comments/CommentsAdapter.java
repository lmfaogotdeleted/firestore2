package com.example.firestore.ui.comments;
import com.example.firestore.R;
import com.example.firestore.ui.comments.CommentsAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import com.example.firestore.data.model.Comment;
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private List<Comment> lista;
    private final String miId;
    private OnComentarioLongClickListener listener;
    public interface OnComentarioLongClickListener {
        void onLongClick(Comment c);
    }
    public CommentsAdapter(OnComentarioLongClickListener listener) {
        this.lista = new ArrayList<>();
        this.listener = listener;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.miId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            this.miId = "";
        }
    }
    public void setLista(List<Comment> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment c = lista.get(position);
        holder.tvAuthor.setText(c.getName());
        holder.tvContent.setText(c.getTexto());
        if (c.getTimestamp() != null) {
            long now = System.currentTimeMillis();
            CharSequence prettyTime = DateUtils.getRelativeTimeSpanString(
                    c.getTimestamp().getTime(),
                    now,
                    DateUtils.MINUTE_IN_MILLIS);
            holder.tvDate.setText(prettyTime);
        } else {
            holder.tvDate.setText("Ahora mismo");
        }
        if (c.getIdAutor() != null && c.getIdAutor().equals(miId)) {
            holder.itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onLongClick(c);
                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
    }
    @Override
    public int getItemCount() {
        return lista.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvContent, tvDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
