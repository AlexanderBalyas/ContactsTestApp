package alex.balyas.mycontactstestapp.mvvm.filter

import alex.balyas.mycontactstestapp.R
import alex.balyas.mycontactstestapp.mvvm.adapters.FilterAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : AppCompatActivity() {

    private val adapter = FilterAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val checkedPosition = FilterSingleton.filterPosition
        initUILogic(checkedPosition)
    }

    private fun initUILogic(checkedPosition: Int) {
        val filterItems = arrayListOf<String>(
                getString(R.string.filter_creation_date),
                getString(R.string.filter_name_asc),
                getString(R.string.filter_name_desc),
                getString(R.string.filter_surname_asc),
                getString(R.string.filter_surname_desc)
        )

        adapter.items = filterItems
        adapter.currentCheckedPosition = checkedPosition
        rv_filter.adapter = adapter
    }
}
