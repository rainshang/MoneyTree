package com.xyx.moneytree.fragment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingVH<out T : ViewDataBinding>(val binding: T) :
    RecyclerView.ViewHolder(binding.root)