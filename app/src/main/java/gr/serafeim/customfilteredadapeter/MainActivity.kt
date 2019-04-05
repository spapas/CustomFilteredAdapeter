package gr.serafeim.customfilteredadapeter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    data class PoiDao(
        val id: Int,
        val name: String,
        val city: String,
        val category_name: String
    )

    inner class PoiAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allPois: List<PoiDao>): ArrayAdapter<PoiDao>(context, layoutResource, allPois),
        Filterable {
        private var mPois: List<PoiDao> = allPois

        override fun getCount(): Int {
            return mPois.size
        }

        override fun getItem(p0: Int): PoiDao? {
            return mPois.get(p0)

        }
        override fun getItemId(p0: Int): Long {
            // Or just return p0
            return mPois.get(p0).id.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return createViewFromResource(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return createViewFromResource(position, convertView, parent)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                    mPois = filterResults.values as List<PoiDao>
                    notifyDataSetChanged()
                }

                override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                    val queryString = charSequence?.toString()?.toLowerCase()
                    var results: List<PoiDao> =
                        if (queryString==null || queryString.isEmpty()) {
                            allPois
                        } else {
                            allPois.filter {
                                it.name.toLowerCase().contains(queryString) || it.city.contains(queryString) || it.category_name.contains(queryString)
                            }
                        }
                    val filterResults = Filter.FilterResults()
                    filterResults.values = results
                    return filterResults
                }
            }
        }

        private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
            view.text = "${mPois[position].name} ${mPois[position].city} (${mPois[position].category_name})"
            return view
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pois_array = listOf<PoiDao>(
            PoiDao(1, "Taco Bell", "Athens", "Restaurant"),
            PoiDao(2, "McDonalds", "Athens","Restaurant"),
            PoiDao(3, "KFC", "Piraeus", "Restaurant"),
            PoiDao(4, "Shell", "Lamia","Gas Station"),
            PoiDao(5, "BP", "Thessaloniki", "Gas Station")
        )
        val adapter = PoiAdapter(this, android.R.layout.simple_list_item_1, pois_array)
        autoCompleteTextView.setAdapter(adapter)

        autoCompleteTextView.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as PoiDao?
            autoCompleteTextView.setText(selectedPoi?.name)
        }
    }
}
