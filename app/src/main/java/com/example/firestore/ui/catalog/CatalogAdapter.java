package com.example.firestore.ui.catalog;
import com.example.firestore.R;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.catalog.CatalogAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import com.example.firestore.data.local.MediaItem;
import com.example.firestore.databinding.ViewholderCatalogBinding;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.data.model.SerieDto;
public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogoViewHolder> {
    List<com.example.firestore.data.local.TrackingEntity> listaSeguimiento;
    List<PeliculaDto> listaMovies;
    List<SerieDto> listaSeries;
    private final LayoutInflater inflater;
    private CatalogViewModel viewModel;
    private Context context;
    List<MediaItem> listaFavoritos;
    public CatalogAdapter(Context context, CatalogViewModel viewModel) {
        this.inflater = LayoutInflater.from(context);
        this.listaMovies = new ArrayList<>();
        this.viewModel = viewModel;
        this.listaSeries = new ArrayList<>();
        this.context = context;
        this.listaFavoritos = new ArrayList<>();
        this.listaSeguimiento = new ArrayList<>();
    }
    @NonNull
    @Override
    public CatalogoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.viewholder_catalog, parent, false);
        return new CatalogoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CatalogoViewHolder holder, int position) {
        if (!listaSeguimiento.isEmpty()) {
            com.example.firestore.data.local.TrackingEntity item = listaSeguimiento.get(position);
            Glide.with(context).clear(holder.binding.imgPosterItem);
            holder.binding.imgPosterItem.setImageDrawable(null);
            holder.binding.imgPosterItem.setBackground(null);
            holder.binding.imgPosterItem.setColorFilter(null);
            holder.binding.tvTitleItem.setText(item.getTitulo());
            holder.binding.tvSubtituloItem.setText("Visto el: " + item.getFecha());
            holder.binding.btnFavorite.setVisibility(View.GONE);
            if (item.getFoto() != null) {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(
                        item.getFoto(), 0, item.getFoto().length
                );
                holder.binding.imgPosterItem.setImageBitmap(bitmap);
                holder.binding.imgPosterItem.setScaleType(android.widget.ImageView.ScaleType.FIT_CENTER);
                holder.binding.imgPosterItem.setBackgroundColor(android.graphics.Color.TRANSPARENT);
            } else {
                holder.binding.imgPosterItem.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "PuntuaciÃ³n: " + item.getPuntuacion(), Toast.LENGTH_SHORT).show();
            });
            holder.itemView.setOnClickListener(v -> {
                viewModel.seleccionarSeguimiento(item);
               Navigation.findNavController(v).navigate(R.id.action_seguimientoFragment_to_detalleSeguimientoFragment);
            });
        }
        else if (!listaFavoritos.isEmpty()) {
            MediaItem item = listaFavoritos.get(position);
            setupVista(holder, item.getTitulo(), item.getDescripcion(), item.getURL());
            holder.itemView.setOnClickListener(v -> {
                viewModel.selectDesdeFavoritos(item);
                Navigation.findNavController(v).navigate(R.id.toInformacion);
            });
        }
        else if (!listaMovies.isEmpty()) {
            PeliculaDto pelicula = listaMovies.get(position);
            setupVista(holder, pelicula.getTitle(), pelicula.getOverview(), pelicula.getPosterPath());
            holder.binding.btnFavorite.setOnClickListener(v ->
                    viewModel.insertarPelicula(new MediaItem(pelicula.getPosterPath(), pelicula.getId(), pelicula.getTitle(), pelicula.getOverview()))
            );
            holder.itemView.setOnClickListener(v -> {
                viewModel.selectPelicula(pelicula);
                Navigation.findNavController(v).navigate(R.id.toInformacion);
            });
        }
        else if (!listaSeries.isEmpty()) {
            SerieDto serie = listaSeries.get(position);
            setupVista(holder, serie.getName(), serie.getOverview(), serie.getPosterPath());
            holder.binding.btnFavorite.setOnClickListener(v ->
                    viewModel.insertarPelicula(new MediaItem(serie.getPosterPath(), serie.getId(), serie.getName(), serie.getOverview()))
            );
            holder.itemView.setOnClickListener(v -> {
                viewModel.selectSerie(serie);
                Navigation.findNavController(v).navigate(R.id.toInformacion);
            });
        }
    }
    private void setupVista(CatalogoViewHolder holder, String titulo, String desc, String url) {
        holder.binding.tvTitleItem.setText(titulo);
        holder.binding.tvSubtituloItem.setText(desc);
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + url)
                .placeholder(android.R.drawable.ic_menu_upload)
                .into(holder.binding.imgPosterItem);
    }
    private void establecerIconoFavorito( CatalogoViewHolder holder) {
            holder.binding.btnFavorite.setImageResource(android.R.drawable.star_on);
    }
    public void establecerListaSeguimiento(List<com.example.firestore.data.local.TrackingEntity> lista) {
        this.listaSeguimiento = lista;
        this.listaFavoritos.clear();
        this.listaMovies.clear();
        this.listaSeries.clear();
        notifyDataSetChanged();
    }
        public void establecerLista(List<MediaItem> listaFavoritos){
            this.listaFavoritos = listaFavoritos;
            notifyDataSetChanged();
        }
    @Override
    public int getItemCount() {
        if (listaSeguimiento != null && !listaSeguimiento.isEmpty()) return listaSeguimiento.size();
        if (listaFavoritos != null && !listaFavoritos.isEmpty()) return listaFavoritos.size();
        if (listaMovies != null && !listaMovies.isEmpty()) return listaMovies.size();
        if (listaSeries != null && !listaSeries.isEmpty()) return listaSeries.size();
        return 0;
    }
    public void addNuevasPeliculas(List<PeliculaDto> nuevos) {
        if (!listaSeries.isEmpty()) listaSeries.clear();
        int inicio = listaMovies.size();
        this.listaMovies.addAll(nuevos);
        notifyItemRangeInserted(inicio, nuevos.size());
    }
    public void addNuevasSeries(List<SerieDto> nuevos) {
        if (!listaMovies.isEmpty()) listaMovies.clear();
        int inicio = listaSeries.size();
        this.listaSeries.addAll(nuevos);
        notifyItemRangeInserted(inicio, nuevos.size());
    }
    public class CatalogoViewHolder extends RecyclerView.ViewHolder {
        public final ViewholderCatalogBinding binding;
        public CatalogoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ViewholderCatalogBinding.bind(itemView);
        }
    }
}
