package com.raywenderlich.cinematic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.raywenderlich.cinematic.databinding.ItemMovieBinding
import com.raywenderlich.cinematic.model.Movie
import com.raywenderlich.cinematic.util.Constants
import com.raywenderlich.cinematic.util.MovieListClickListener

class MoviesRecyclerAdapter : RecyclerView.Adapter<MoviesRecyclerAdapter.MoviesViewHolder>() {

    private val items: MutableList<Movie> = mutableListOf()

    private var listener: MovieListClickListener? = null

    fun setListener(listener: MovieListClickListener) {
        this.listener = listener
    }

    fun setItems(newItems: List<Movie>) {
        this.items.clear()
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(items[position]) { movie ->
//            // Fetches the index of the item you want to remove.
//            val itemIndex = items.indexOf(movie)
//            // Removes the item from the list.
//            this.items.remove(movie)
//
//            // Notifies the adapter that you removed only one item at a given itemIndex.
//            notifyItemRemoved(itemIndex)
            val newIndex = (0..items.size).random()
            this.items.add(newIndex, movie)

            notifyItemInserted(newIndex)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MoviesViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onLongItemClick: (Movie) -> Unit) {
            binding.movieName.text = movie.title
            binding.movieRating.rating = movie.rating
            binding.ratingValue.text = movie.rating.toString()
            binding.movieInfo.text = movie.movieInfo
            binding.background.load(Constants.IMAGE_BASE + movie.backdropPath) {
                crossfade(true)
            }

            binding.root.setOnClickListener {
                listener?.onMovieClicked(movie)
            }

            binding.root.setOnLongClickListener {
                onLongItemClick(movie)

                true
            }
        }

    }
}