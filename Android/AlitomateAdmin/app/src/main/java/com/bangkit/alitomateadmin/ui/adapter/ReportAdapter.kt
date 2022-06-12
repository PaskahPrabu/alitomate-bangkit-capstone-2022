package com.bangkit.alitomateadmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.alitomateadmin.data.Report
import com.bangkit.alitomateadmin.databinding.ItemReportBinding
import com.bangkit.alitomateadmin.utils.getDate
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ReportAdapter(var listItem: List<Report>): RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ReportViewHolder(private val binding: ItemReportBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(report: Report) {
            with(binding) {
                nameTv.text = report.name
                phoneNumTv.text = report.phoneNumber
                detailLocTv.text = report.detailLoc
                detailAccidentTv.text = report.detailReport
                Glide.with(itemView)
                    .load(report.imageUrl)
                    .into(reportImg)
                timestampTv.text = report.timestamp?.let { getDate(it.seconds) }

                root.setOnClickListener { onItemClickCallback.onItemClicked(report) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Report)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}