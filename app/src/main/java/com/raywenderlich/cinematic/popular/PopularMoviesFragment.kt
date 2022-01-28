/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.cinematic.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.raywenderlich.cinematic.AnimationViewModel
import com.raywenderlich.cinematic.MoviesAdapter
import com.raywenderlich.cinematic.R
import com.raywenderlich.cinematic.databinding.FragmentPopularBinding
import com.raywenderlich.cinematic.model.Movie
import com.raywenderlich.cinematic.util.Events.Done
import com.raywenderlich.cinematic.util.Events.Loading
import com.raywenderlich.cinematic.util.MovieListClickListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.math.hypot

class PopularMoviesFragment : Fragment(R.layout.fragment_popular) {

    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PopularMoviesViewModel by inject()
    private val animationViewModel: AnimationViewModel by sharedViewModel()
    private val popularAdapter: MoviesAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularAdapter.setListener(object : MovieListClickListener {
            override fun onMovieClicked(movie: Movie) {
                findNavController().navigate(
                    PopularMoviesFragmentDirections.actionPopularMoviesFragmentToMovieDetailsFragment(
                        movie.id
                    )
                )
            }
        })

        binding.popularMoviesList.apply {
            adapter = popularAdapter
        }

        viewModel.getPopularMovies()
        attachObservers()
    }

    private fun attachObservers() {
        viewModel.movies.observe(viewLifecycleOwner, { movies ->
            popularAdapter.submitList(movies)
        })

        //  This block checks the Boolean value of the LiveData object and triggers
        // animateContentIn when it should animate. You’ll find the same structure in
        // FavoriteMoviesFragment.
        animationViewModel.animatePopularEntranceLiveData.observe(
            viewLifecycleOwner,
            { shouldAnimatie ->
                if (shouldAnimatie) {
                    animateContentIn()
                }
            }
        )

        viewModel.events.observe(viewLifecycleOwner, { event ->
            when (event) {
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.popularMoviesList.visibility = View.GONE
                }

                is Done -> {
                    binding.progressBar.visibility = View.GONE
                    binding.popularMoviesList.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun animateContentIn() {
        //  doOnPreDraw will execute an action exactly once, right before drawing the View. It’s
        // a handy way to ensure that the View is ready to be drawn before you execute an
        // animation. You call it on the root of the layout binding because this animation
        // should run on the whole layout.
        binding.root.doOnPreDraw {
            //  1. Get a shorter reference to the View you’re going to animate, which is the root
            // View of the layout.
            val view = binding.root
            //  2. Declare the X coordinate of the center point of the clipping circle. This is the
            // popular movies screen, so you want the circle to start from the left side of the
            // screen, close to the popular icon. Therefore, you set the value to 0.
            val centerX = 0
            //  3. Declare the Y coordinate of the center point of the clipping circle. The circle
            // should emanate out from the bottom-left of the screen, so the Y coordinate
            // should be the full height of the View — that is, at the bottom of the screen.
            val centerY = view.height

            val finalRadius = hypot(
                view.width.toDouble(),
                view.height.toDouble()
            )

            val anim = ViewAnimationUtils.createCircularReveal(
                view,
                centerX,
                centerY,
                0f,
                finalRadius.toFloat()
            )
            anim.duration = 600
            anim.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}