package com.vp.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.vp.detail.model.MovieDetail
import com.vp.favorites.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {
    private val detail by lazy { arguments!!.getParcelable<MovieDetail>("extras")!! }
    private var binding: DetailFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding!!) {
            title.text = detail.title
            year.text = detail.year
            plot.text = detail.plot
            director.text = detail.director
            Glide.with(poster).load(detail.poster).into(poster)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(detail: MovieDetail): Fragment {
            val bundle = Bundle().apply {
                putParcelable("extras", detail)
            }
            return DetailFragment().apply {
                arguments = bundle
            }
        }
    }
}